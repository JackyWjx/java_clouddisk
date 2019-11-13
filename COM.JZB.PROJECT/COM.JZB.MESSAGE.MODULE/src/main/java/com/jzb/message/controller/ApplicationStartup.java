package com.jzb.message.controller;

import com.jzb.base.util.JzbTools;
import com.jzb.message.message.MessageQueue;
import com.jzb.message.message.MssageInfo;
import com.jzb.message.message.SendMessage;
import com.jzb.message.config.MqttGateway;
import com.jzb.message.service.ShortMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @Description: Spring boot 启动后加载
 * @Author Han Bin
 */
@Component
@Order(value = 1)
public class ApplicationStartup implements ApplicationRunner {


    /**
     * 线程池
     */
    private final static Map<String, List<SendMessageThread>> SEND_MESSAGE_POOL = new ConcurrentHashMap<>();

    /**
     * 消息业务
     */
    @Autowired
    private  ShortMessageService ssmService;

    /**
     * mqtt 服务
     */
    @Autowired
    private MqttGateway mqttGateway;

    /**
     * 消息队列发送线程
     */
    class SendMessageThread extends Thread {
        final int type;
        SendMessageThread(int type) {
            this.type = type;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    MssageInfo msg;
                    switch (type) {
                        case 1:
                            // 短信发送队列
                            msg = MessageQueue.getMessageInfo(type);
                            if(!JzbTools.isEmpty(msg)){
                                SendMessage  message = new SendMessage();
                                message.sendMessage(msg,ssmService);
                                Thread.sleep(1000);
                            }else{
                                Thread.sleep(5000);
                            }
                            break;
                        case 2:
                            // 邮件发送队列
                            msg = MessageQueue.getMessageInfo(type);
                            if(!JzbTools.isEmpty(msg)){
                                SendMessage  message = new SendMessage();
                                message.sendMail(msg,ssmService);
                                Thread.sleep(1000);
                            }else{
                                Thread.sleep(5000);
                            }
                            break;
                        case 3:
                            // 待发送队列
                            Map<String , Map<String , Map<String , MssageInfo>>> map = MessageQueue.getWaitSendQueue();
                            if(!JzbTools.isEmpty(map)){
                                SendMessage  message = new SendMessage();
                                message.waitMessage(map);
                                Thread.sleep(1000);
                            }else{
                                Thread.sleep(5000);
                            }
                            break;
                        case 4:
                            // 平台发送队列
                            msg = MessageQueue.getMessageInfo(type);
                            if(!JzbTools.isEmpty(msg)){
                                SendMessage  message = new SendMessage();
                                message.sendSys(msg,ssmService,mqttGateway);
                                Thread.sleep(1000);
                            }else{
                                Thread.sleep(5000);
                            }
                            break;
                        case 8:
                            // 微信发送队列

                            break;
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } // End run
    } // End SendMessageThread

    /**
     * Spring boot 启动后加载
     * 配置线程池
     * 启动线程
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 初始化线程池
        // 加入短消息发送线程
        SendMessageThread smsThread = new SendMessageThread(1);
        smsThread.start();
        List<SendMessageThread> smsList = new ArrayList<>();
        smsList.add(smsThread);
        SEND_MESSAGE_POOL.put("SMS", smsList);

        // 加入邮件发送线程
        SendMessageThread meilThread = new SendMessageThread(2);
        meilThread.start();
        List<SendMessageThread> meilList = new ArrayList<>();
        meilList.add(meilThread);
        SEND_MESSAGE_POOL.put("MAIL", meilList);

        // 短信消息 待发送队列
        SendMessageThread waitSmsThread = new SendMessageThread(3);
        waitSmsThread.start();
        List<SendMessageThread> waitSmsList = new ArrayList<>();
        waitSmsList.add(waitSmsThread);
        SEND_MESSAGE_POOL.put("WAITSMS", waitSmsList);

        // 加入平台发送线程
        SendMessageThread sysThread = new SendMessageThread(4);
        sysThread.start();
        List<SendMessageThread> sysList = new ArrayList<>();
        sysList.add(sysThread);
        SEND_MESSAGE_POOL.put("SYS", sysList);

    }
}
