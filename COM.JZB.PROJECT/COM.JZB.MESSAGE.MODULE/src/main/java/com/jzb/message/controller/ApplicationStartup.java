package com.jzb.message.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.util.JzbTools;
import com.jzb.message.message.MessageQueue;
import com.jzb.message.message.MssageInfo;
import com.jzb.message.message.SendMessage;
import com.jzb.message.service.ShortMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.swing.text.html.parser.Entity;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @Description: Spring boot 启动后加载
 * @Author Han Bin
 */
@Component
@Order(value = 1)
public class ApplicationStartup implements ApplicationRunner {

    private final static Logger logger = LoggerFactory.getLogger(SendMsgController.class);

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
                            Map<String , Map<String , Map<String , MssageInfo>>> map = MessageQueue.getWaitSendQueue();
                            if(!JzbTools.isEmpty(map)){
                                // 业务id层
                                for (Map.Entry<String , Map<String , Map<String , MssageInfo>>> mgtentry:map.entrySet()) {
                                    Map<String , Map<String , MssageInfo>> cidMap = map.get(mgtentry.getKey());
                                    // 企业id层
                                    for (Map.Entry<String , Map<String , MssageInfo>> cidentry:cidMap.entrySet()) {
                                        Map<String , MssageInfo> typeMap = cidMap.get(cidentry.getKey());
                                        // 消息类型
                                        for(Map.Entry <String , MssageInfo> typetry:typeMap.entrySet()){
                                            MssageInfo info = typeMap.get(typetry.getKey());
                                            Map<String , Object> parajson = (Map)info.getItem("para").getObject();
                                            // 是否是定时任务
                                            if(!JzbTools.isEmpty(parajson.get("sendtime"))){
                                                if(JzbDataType.getLong(parajson.get("sendtime")) >= System.currentTimeMillis()){
                                                    if(typetry.getKey().equals("1")){
                                                        MessageQueue.addShortMessage(info);
                                                        map.remove(mgtentry.getKey());
                                                        logger.info("=================>>添加至短信" );
                                                    }else if(typetry.getKey().equals("2")){
                                                        MessageQueue.addShortMail(info);
                                                        map.remove(mgtentry.getKey());
                                                        logger.info("=================>>添加至邮件" );
                                                    }
                                                }
                                            }else{
                                                // 添加发送队列
                                                if(typetry.getKey().equals("1")){
                                                    MessageQueue.addShortMessage(info);
                                                    map.remove(mgtentry.getKey());
                                                    logger.info("=================>>添加至短信" );
                                                }else if(typetry.getKey().equals("2")){
                                                    MessageQueue.addShortMail(info);
                                                    map.remove(mgtentry.getKey());
                                                    logger.info("=================>>添加至邮件" );
                                                }
                                            }
                                            Thread.sleep(1000);
                                        }
                                    }
                                }
                            }else{
                                Thread.sleep(5000);
                            }
                            break;
                        case 4:
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

        // 短信消息 待发送队列
        SendMessageThread waitSmsThread = new SendMessageThread(3);
        waitSmsThread.start();
        List<SendMessageThread> waitSmsList = new ArrayList<>();
        waitSmsList.add(waitSmsThread);
        SEND_MESSAGE_POOL.put("WAITSMS", waitSmsList);

        // 加入邮件发送线程
        SendMessageThread mailThread = new SendMessageThread(2);
        mailThread.start();
        List<SendMessageThread> mailList = new ArrayList<>();
        mailList.add(mailThread);
        SEND_MESSAGE_POOL.put("MAIL", mailList);

    }
}
