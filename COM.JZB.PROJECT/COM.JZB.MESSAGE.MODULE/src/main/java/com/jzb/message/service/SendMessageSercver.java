package com.jzb.message.service;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.message.dao.MsgListMapper;
import com.jzb.message.util.MessageUtile;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 平台推送中心
 * @Author Han Bin
 */
@Service
public class SendMessageSercver {

    private final static Logger logger = LoggerFactory.getLogger(ShortMessageService.class);

    /**
     * 消息组DB
     */
    @Autowired
    MsgListMapper msgListMapper;

    /**
     * 消息中心 发送平台
     *
     * @param map  请求参数
     * @param cmap  企业信息
     * @return
     */
    public Map<String , Object> SendMessage(Map<String , Object> map,Map<String , Object> cmap){
        Map<String , Object> dataMap = new HashMap<>();
        try {
            dataMap.put("cid", cmap.get("cid"));
            dataMap.put("groupid", map.get("groupid"));
            dataMap.put("title", map.get("title"));
            dataMap.put("usertype", map.get("usertype"));
            dataMap.put("msgtag", map.get("msgtag"));
            dataMap.put("addtime", System.currentTimeMillis());
            dataMap.put("status", "1");
            dataMap.put("sendstatus", 1);
            dataMap.put("senduid", map.get("senduid"));
            if (map.containsKey("sendname")) {
                dataMap.put("sendname", map.get("sendname"));
            }
            if (map.containsKey("sendtime")) {
                dataMap.put("sendtime", map.get("sendtime"));
            }
            if (map.containsKey("remark")) {
                dataMap.put("summary", map.get("remark"));
            }
            // 获取模板配置信息
            List<Map<String, Object>> listGroupTemplate = msgListMapper.queryMsgUserGroupTemplate(map.get("groupid").toString());
            // 获取用户发送配置
            List<Map<String, Object>> listGroupConfig = msgListMapper.queryMsgGroupConfigure(map.get("groupid").toString());
            if(map.get("usertype").equals("1")){
                JSONObject receiverJson = JSONObject.fromObject(map.get("receiver"));
                JSONObject paraJson = JSONObject.fromObject(map.get("sendpara"));
                // 找到指定模板参数
                Map<String , Object> listGroupTemplateMap = MessageUtile.templeMap(listGroupTemplate);
                if (receiverJson.containsKey("sms")) {
                    // 短信消息
                    List<Map<String , Object>> list =  (List<Map<String, Object>>) receiverJson.get("sms");
                    for(int i = 0 ; i < list.size() ; i++){
                        dataMap.put("msgid", JzbRandom.getRandomNum(16));
                        Map<String , Object> smsUserMap =  list.get(i);
                        if(dataMap.containsKey("uid")){
                            dataMap.put("senduid",smsUserMap.get("uid"));
                            smsUserMap.remove("uid");
                        }else{
                            dataMap.put("senduid",smsUserMap.get("photo"));
                        }
                        dataMap.put("receiver", smsUserMap.get("photo"));
                        dataMap.put("msgtype",'1');
                        smsUserMap.remove("phone");
                        // 参数替换
                        Map<String, Object> paraGroupMap = MessageUtile.setUpParaByUser(listGroupTemplateMap.get("1").toString(),paraJson.get("sms").toString(),"1");
                        // 找到对应的参数
                        Map<String, Object> paraMap = MessageUtile.msgTypeGroup(listGroupConfig, paraGroupMap);
                        // 添加到待发送队列
                        boolean msgboolean = MessageUtile.saveMessageInfo(dataMap.get("receiver").toString(), (Map<String, Object>) paraMap.get("1"), dataMap);
                        logger.info("添加短信消息队列" + msgboolean);
                        // 添加 日志记录
                        msgListMapper.insertMsgList(dataMap);
                    }
                }
                if(receiverJson.containsKey("meil")){
                    // 邮件消息
                    List<Map<String , Object>> list =  (List<Map<String, Object>>) receiverJson.get("meil");
                    for(int i = 0 ; i < list.size() ; i++){
                        dataMap.put("msgid", JzbRandom.getRandomNum(16));
                        Map<String , Object> meilUserMap =  list.get(i);
                        if(dataMap.containsKey("uid")){
                            dataMap.put("senduid",meilUserMap.get("uid"));
                            meilUserMap.remove("uid");
                        }else{
                            dataMap.put("senduid",meilUserMap.get("photo"));
                        }
                        dataMap.put("receiver", meilUserMap.get("photo"));
                        dataMap.put("msgtype",'2');
                        meilUserMap.remove("photo");
                        // 参数替换
                        Map<String, Object> paraGroupMap = MessageUtile.setUpParaByMeil(listGroupTemplateMap.get("2").toString(),paraJson.get("meil").toString(),"2");
                        // 找到对应的参数
                        Map<String, Object> paraMap = MessageUtile.msgTypeGroup(listGroupConfig, paraGroupMap);
                        // 添加到待发送队列
                        boolean msgboolean = MessageUtile.saveMeilInfo(dataMap.get("receiver").toString(), (Map<String, Object>) paraMap.get("2"), dataMap);
                        logger.info("添加邮件消息队列" + msgboolean);
                        // 添加 日志记录
                        msgListMapper.insertMsgList(dataMap);
                    }
                }
                if(receiverJson.containsKey("sys")){
                    // 系统消息
                    List<Map<String , Object>> list =  (List<Map<String, Object>>) receiverJson.get("sys");
                    for(int i = 0 ; i < list.size() ; i++){
                        dataMap.put("msgid", JzbRandom.getRandomNum(16));
                        Map<String , Object> meilUserMap =  list.get(i);
                        if(dataMap.containsKey("uid")){
                            dataMap.put("senduid",meilUserMap.get("uid"));
                            meilUserMap.remove("uid");
                        }else{
                            dataMap.put("senduid",meilUserMap.get("topic"));
                        }
                        dataMap.put("receiver", meilUserMap.get("topic"));
                        dataMap.put("msgtype",'4');
                        meilUserMap.remove("topic");
                        // 参数替换
                        Map<String, Object> paraGroupMap = MessageUtile.setUpParaByMeil(listGroupTemplateMap.get("4").toString(),paraJson.get("sys").toString(),"4");
                        // 找到对应的参数
                        Map<String, Object> paraMap = MessageUtile.msgTypeGroup(listGroupConfig, paraGroupMap);
                        // 添加到待发送队列
                        boolean msgboolean = MessageUtile.saveSystemInfo(dataMap.get("receiver").toString(), (Map<String, Object>) paraMap.get("4"), dataMap);
                        logger.info("添加平台推送消息队列" + msgboolean);
                        // 添加 日志记录
                        msgListMapper.insertMsgList(dataMap);
                    }
                }
                if(receiverJson.containsKey("wecat")){

                }
            }else{
                // 用户组 获取用户信息
                List<Map<String , Object>> sendUser = msgListMapper.queryMsgUserGroup(map.get("groupid").toString());
                // 用户组
                for(int i = 0 ; i < sendUser.size(); i++){
                    try{
                        // 获取用户信息
                        Map<String , Object> userMap =  msgListMapper.queryUserParameter(sendUser.get(i).get("uid").toString());
                        // 替换参数
                        Map<String , Object> paraGroupMap = MessageUtile.setUpParaByGroupid(listGroupTemplate,userMap.get("param").toString(),dataMap);
                        // 合并
                        Map<String , Object> paraMap =  MessageUtile.msgTypeGroup(listGroupConfig,paraGroupMap);
                        // 使用用户组id
                        int msgtype = JzbDataType.getInteger(sendUser.get(i).get("msgtype"));
                        if(msgtype == 1){
                            // 短信消息
                            dataMap.put("receiver",sendUser.get(i).get("tarobj"));
                            dataMap.put("msgtype","1");
                            dataMap.put("senduid",sendUser.get(i).get("uid").toString());
                            boolean msgboolean = MessageUtile.saveMessageInfo(sendUser.get(i).get("tarobj").toString(),(Map<String, Object>) paraMap.get("1"),dataMap);
                            logger.info("添加短信消息队列"+msgboolean);
                        } else if(msgtype == 2){
                            // 邮件消息
                            dataMap.put("receiver",sendUser.get(i).get("tarobj"));
                            dataMap.put("msgtype","2");
                            dataMap.put("senduid",sendUser.get(i).get("uid").toString());
                            boolean msgboolean = MessageUtile.saveMeilInfo(sendUser.get(i).get("tarobj").toString(),(Map<String, Object>) paraMap.get("2"),dataMap);
                            logger.info("添加邮件消息队列"+msgboolean);
                        } else if(msgtype == 4){
                            // 系统消息
                            dataMap.put("receiver",sendUser.get(i).get("tarobj"));
                            dataMap.put("msgtype","4");
                            dataMap.put("senduid",sendUser.get(i).get("uid").toString());
                            boolean msgboolean = MessageUtile.saveSystemInfo(sendUser.get(i).get("tarobj").toString(),(Map<String, Object>) paraMap.get("4"),dataMap);
                            logger.info("添加平台消息队列"+msgboolean);
                        } else if(msgtype == 8){
                            // 微信消息
                        }
                    }catch (Exception e){
                        dataMap.put("sendstatus",2);
                        JzbTools.logError(e);
                    }finally {
                        // 添加 日志记录
                        msgListMapper.insertMsgList(dataMap);
                    }
                }
            }
        }catch (Exception e){
            JzbTools.logError(e);
        }
        return dataMap;
    }
}
