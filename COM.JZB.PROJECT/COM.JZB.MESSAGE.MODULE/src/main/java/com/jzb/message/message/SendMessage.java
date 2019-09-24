package com.jzb.message.message;

import com.jzb.base.util.JzbTools;
import com.jzb.message.service.ShortMessageService;
import com.jzb.message.util.JzbSendMail;
import com.jzb.message.util.JzbSendMsg;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 消息中心发送
 * @Author Han Bin
 */
@Component
public class SendMessage {

    private final static Logger logger = LoggerFactory.getLogger(SendMessage.class);

    /**
     * 发送短信
     */
    public static void sendMessage(MssageInfo msg,ShortMessageService ssmService){
        Map<String , Object> dataMap = new HashMap<>();
        // 是否有定时任务
        if(msg.isItem("sendtime")){
            // 是否到发送时间
            if(System.currentTimeMillis() >= msg.getItem("sendtime").getLong()  ){
                try{
                    JSONObject json =  JSONObject.fromObject(msg.getItem("sendpara").getString());
                    dataMap.put("sendtime",System.currentTimeMillis());
                    dataMap.put("status","1");
                    if(msg.isItem("userid")){
                        dataMap.put("receiveuid",msg.getItem("userid").getString());
                        dataMap.put("receivename",msg.getItem("username").getString());
                    }
                    dataMap.put("msgtype",1);
                    dataMap.put("msgid",msg.getItem("msgid").getString());
                    ssmService.updateMessageListSendStatusByMsgid(msg.getItem("msgid").getString());
                    String message = JzbSendMsg.sendShortMessage(json);
                    logger.info("发送状态"+message);
                    dataMap.put("summary",message);
                }catch (Exception e){
                    JzbTools.logError(e);
                    dataMap.put("msgtype",2);
                }finally {
                    ssmService.saveSendsUserMessage(dataMap);
                }
            }else{
                // 返回发送队列
                MessageQueue.addShortMessage(msg);
            }
        }else{
            try{
                JSONObject json =  JSONObject.fromObject(msg.getItem("sendpara").getString());
                dataMap.put("sendtime",System.currentTimeMillis());
                dataMap.put("status","1");
                if(msg.isItem("userid")){
                    dataMap.put("receiveuid",msg.getItem("userid").getString());
                    dataMap.put("receivename",msg.getItem("username").getString());
                }
                dataMap.put("msgtype",1);
                dataMap.put("msgid",msg.getItem("msgid").getString());
                String message = JzbSendMsg.sendShortMessage(json);
                ssmService.updateMessageListSendStatusByMsgid(msg.getItem("msgid").getString());
                logger.info("发送状态"+message);
                dataMap.put("summary",message);
            }catch (Exception e){
                JzbTools.logError(e);
                dataMap.put("msgtype",2);
            }finally {
                ssmService.saveSendsUserMessage(dataMap);
            }
        }
    }

    /**
     * 发送邮件
     */
    public static void sendMail(MssageInfo msg,ShortMessageService ssmService){
        Map<String , Object> dataMap = new HashMap<>();
        // 是否定时任务
        if (msg.isItem("sendtime")) {
            // 是否到发送时间
            if (System.currentTimeMillis() >= msg.getItem("sendtime").getLong()) {
                try {
                    JSONObject json =  JSONObject.fromObject(msg.getItem("sendpara").getString());
                    dataMap.put("sendtime", System.currentTimeMillis());
                    dataMap.put("msgtype", 1);
                    dataMap.put("msgid", msg.getItem("msgid").getString());
                    // 获取邮件对象
                    boolean result = JzbSendMail.sendMime(json);
                    // 发送状态
                    dataMap.put("status", result ? "1" : "2");
                    dataMap.put("receiveuid",msg.getItem("userid").getString());
                    dataMap.put("receivename",msg.getItem("username").getString());
                } catch (Exception e) {
                    JzbTools.logError(e);
                    dataMap.put("msgtype", 2);
                } finally {
                    ssmService.saveSendsUserMessage(dataMap);
                }
            } else {
                //返回待发送队列
                MessageQueue.addShortMail(msg);
            }
        } else {
            try {
                JSONObject json =  JSONObject.fromObject(msg.getItem("sendpara").getString());
                // 无定时任务 直接发送
                dataMap.put("sendtime", System.currentTimeMillis());
                dataMap.put("msgtype", 1);
                dataMap.put("msgid", msg.getItem("msgid").getString());
                boolean result = JzbSendMail.sendMime(json);
                 //发送状态
                dataMap.put("status", result ? "1" : "2");
                if (msg.isItem("userid")) {
                    dataMap.put("receivename", msg.getItem("userid").getString());
                    dataMap.put("receiveuid", msg.getItem("username").getString());
                }
            } catch (Exception e) {
                JzbTools.logError(e);
                dataMap.put("msgtype", 2);
            } finally {
                // 添加日志
                ssmService.saveSendsUserMessage(dataMap);
            }
        }
    }

    /**
     * 发送系统消息
     */
    public static void sendSys(MssageInfo msg){

    }

    /**
     * 发送微信消息
     */
    public static void sendWeChat(MssageInfo msg){

    }

}
