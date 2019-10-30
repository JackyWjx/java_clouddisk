package com.jzb.message.message;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.util.JzbTools;
import com.jzb.message.service.ShortMessageService;
import com.jzb.message.util.JzbSendMail;
import com.jzb.message.util.JzbSendMsg;
import com.jzb.message.config.MqttGateway;
import com.jzb.message.util.JzbSendSys;
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
    public static void sendMessage(MssageInfo map,ShortMessageService ssmService){
        Map<String , Object> dataMap = new HashMap<>();
        Map<String , Object> parajson = (Map)map.getItem("para").getObject();
        // 是否有定时任务
//        if(JzbTools.isEmpty(parajson.get("sendtime"))){
        try{
            dataMap.put("sendtime",System.currentTimeMillis());
            dataMap.put("status","1");
            if(parajson.containsKey("senduid")){
                dataMap.put("receiveuid",parajson.get("senduid").toString());
            }
            if(parajson.containsKey("receivename")){
                dataMap.put("receivename",parajson.get("sendname").toString());
            }
            dataMap.put("msgtype",1);
            dataMap.put("msgid",parajson.get("msgid"));
            Map<String , Object> para = new HashMap<>();
            para.put("msgid",parajson.get("msgid").toString());
            para.put("sendtime",System.currentTimeMillis());
            para.put("sendpara",map.getItem("temp").getString());
            para.put("context",map.getItem("config").getString());
            ssmService.updateMessageListSendStatusByMsgid(para);
            String message = JzbSendMsg.sendShortMessage(map);
            logger.info("发送状态"+message);
            dataMap.put("summary",message);
        }catch (Exception e){
            JzbTools.logError(e);
            dataMap.put("msgtype",2);
        }finally {
            ssmService.saveSendsUserMessage(dataMap);
        }
    }

    /**
     * 发送邮件
     */
    public static void sendMail(MssageInfo msg,ShortMessageService ssmService){
        Map<String , Object> dataMap = new HashMap<>();
        try {
            Map<String , Object> parajson = (Map)msg.getItem("para").getObject();
            dataMap.put("sendtime", System.currentTimeMillis());
            dataMap.put("msgid", msg.getItem("msgid").getString());
            boolean result = JzbSendMail.sendMime(msg);
            //发送状态
            dataMap.put("msgtype", result ? 1 : 2);
            if (msg.isItem("userid")) {
                dataMap.put("receivename", msg.getItem("userid").getString());
                dataMap.put("receiveuid", msg.getItem("username").getString());
            }
            Map<String , Object> para = new HashMap<>();
            para.put("msgid",parajson.get("msgid").toString());
            para.put("sendtime",System.currentTimeMillis());
            para.put("sendpara",msg.getItem("temp").getString());
            para.put("context",msg.getItem("config").getString());
            ssmService.updateMessageListSendStatusByMsgid(para);
        } catch (Exception e) {
            JzbTools.logError(e);
            dataMap.put("msgtype", 2);
        } finally {
            // 添加日志
            ssmService.saveSendsUserMessage(dataMap);
        }
    }

    /**
     * 待发送队列 添加至发送队列
     *
     * @param map
     */
    public static void waitMessage(Map<String , Map<String , Map<String , MssageInfo>>> map){
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
                            }else if(typetry.getKey().equals("4")){
                                MessageQueue.addShortSys(info);
                                map.remove(mgtentry.getKey());
                                logger.info("=================>>添加至平台" );
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
                        }else if(typetry.getKey().equals("4")){
                            MessageQueue.addShortSys(info);
                            map.remove(mgtentry.getKey());
                            logger.info("=================>>添加至平台" );
                        }
                    }
                }
            }
        }
    }


    /**
     * 发送系统消息
     */
    public static void sendSys(MssageInfo msg, ShortMessageService ssmService, MqttGateway mqttGateway){
        Map<String , Object> dataMap = new HashMap<>();
        try {
            Map<String , Object> parajson = (Map)msg.getItem("para").getObject();
            dataMap.put("sendtime", System.currentTimeMillis());
            dataMap.put("msgid", msg.getItem("msgid").getString());
            boolean result = JzbSendSys.sendSysMq(msg,mqttGateway);
            //发送状态
            dataMap.put("msgtype", result ? 1 : 2);
            if (msg.isItem("userid")) {
                dataMap.put("receivename", msg.getItem("userid").getString());
                dataMap.put("receiveuid", msg.getItem("username").getString());
            }
            Map<String , Object> para = new HashMap<>();
            para.put("msgid",parajson.get("msgid").toString());
            para.put("sendtime",System.currentTimeMillis());
            para.put("sendpara",msg.getItem("temp").getString());
            para.put("context",msg.getItem("config").getString());
            ssmService.updateMessageListSendStatusByMsgid(para);
        } catch (Exception e) {
            JzbTools.logError(e);
            dataMap.put("msgtype", 2);
        } finally {
            // 添加日志
            ssmService.saveSendsUserMessage(dataMap);
        }
    }

    /**
     * 发送微信消息
     */
    public static void sendWeChat(MssageInfo msg,ShortMessageService ssmService){

    }

}
