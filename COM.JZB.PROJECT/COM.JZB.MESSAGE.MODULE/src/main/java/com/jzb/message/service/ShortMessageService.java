package com.jzb.message.service;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.message.dao.MsgListMapper;
import com.jzb.message.message.MailMessage;
import com.jzb.message.message.MessageQueue;
import com.jzb.message.message.MssageInfo;
import com.jzb.message.message.ShortMessage;
import com.jzb.message.util.MessageUtile;
import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 短消息服务类
 *
 * @author Chad
 * @date 2019年08月07日
 */
@Service
public class ShortMessageService {

    private final static Logger logger = LoggerFactory.getLogger(ShortMessageService.class);

    /**
     * 消息组DB
     */
    @Autowired
    MsgListMapper msgListMapper;


    /**
     * 短信接口添加至发送队列
     */
    public String sendShortMsg(Map<String , Object> map){
        String result ;
        try{
            Map<String , Object> dataMap = new HashMap<>();
            if(map.containsKey("senduid")){
                dataMap.put("senduid",map.get("senduid"));
            }
            if(map.containsKey("sendname")){
                dataMap.put("sendname",map.get("sendname"));
            }
            if(map.containsKey("sendtime")){
                dataMap.put("data",map.get("sendtime"));
            }
            if(map.containsKey("remark")){
                dataMap.put("summary",map.get("remark"));
            }
            dataMap.put("groupid",map.get("groupid"));
            dataMap.put("title",map.get("title"));
            dataMap.put("usertype",map.get("usertype"));
            // 基础参数
            dataMap.put("msgid", JzbRandom.getRandomNum(16));
            dataMap.put("addtime", System.currentTimeMillis());
            dataMap.put("status","1");
            dataMap.put("sendstatus",1);
            // 获取模板信息
            Map<String ,Object> listGroupTemplate = msgListMapper.queryMsgUserGroupTemplate(map.get("groupid").toString());
            dataMap.put("sms_no",listGroupTemplate.get("tempname"));
            // 获取模板参数
            List<Map<String , Object>>   listPara = msgListMapper.queryMsgGroupConfigure(map.get("groupid").toString());
            if(dataMap.containsKey("usertype")){
                try{
                    if("1".equals(dataMap.get("usertype").toString())){
                        // 个人用户就用模板的msgtype
                        int msgType =  JzbDataType.getInteger(listGroupTemplate.get("msgtype"));
                        // 获取模板参数 并判断是否需要替换
                        Map<String , Object> paraMap =  MessageUtile.setUpPara(listPara,map.get("sendpara").toString());
                        // 短信信息
                        if((msgType & 1) == 1){
                            dataMap.put("receiver",map.get("receiver").toString());
                            dataMap.put("sendpara",JSONObject.toJSONString((Map)paraMap.get("1")));
                            // 设置发送参数
                            Map<String , Object> sendMap = new HashMap<>();
                            sendMap.put("sendpara",JSONObject.toJSONString(dataMap));
                            if(map.containsKey("sendtime")){
                                sendMap.put("sendtime",map.get("sendtime"));
                            }
                            sendMap.put("msgid",dataMap.get("msgid"));
                            if(map.containsKey("senduid")){
                                sendMap.put("senduid",map.get("senduid"));
                                sendMap.put("sendname",map.get("senduname"));
                            }
                            // 添加日志
                            dataMap.put("receiver",map.get("receiver").toString());
                            msgListMapper.insertMsgList(dataMap);
                            logger.info("添加短信日志 =========>"+dataMap.toString());
                            logger.info("添加短信消息队列 =========>"+sendMap.toString());
                            // 添加至发送队列
                            MssageInfo info = new ShortMessage(sendMap);
                            MessageQueue.addShortMessage(info);
                        }
                        // 邮件消息
                        if((msgType & 2) == 2){
                            // 设置发送参数
                            Map<String , Object> sendMap = new HashMap<>();
                            sendMap.put("receiver",map.get("receiver").toString());
                            sendMap.put("sendpara",paraMap.get("2"));
                            if(map.containsKey("sendtime")){
                                sendMap.put("sendtime",map.get("sendtime"));
                            }
                            sendMap.put("msgid",dataMap.get("msgid"));
                            if(map.containsKey("senduid")){
                                sendMap.put("senduid",map.get("senduid"));
                                sendMap.put("sendname",map.get("senduname"));
                            }
                            // 添加日志
                            dataMap.put("receiver",map.get("receiver").toString());
                            msgListMapper.insertMsgList(dataMap);
                            logger.info("添加邮件日志 =========>"+dataMap.toString());
                            logger.info("添加邮件消息队列 =========>"+sendMap.toString());
                            // 添加至发送队列
                            MssageInfo info = new MailMessage(sendMap);
                            MessageQueue.addShortMessage(info);
                        }
                        // 系统消息
                        if((msgType & 4) == 4){
                        }
                        // 微信消息
                        if((msgType & 8) == 8){
                        }
                    }else{
                        // 用户组
                        // 获取企业消息表
                        List<Map<String , Object>> queryPhone =  msgListMapper.queryMsgUserGroup(map.get("groupid").toString());
                        for(int k = 0 ;k<queryPhone.size();k++){
                            // 判断手机号是否超过一千
                            List<String> phoneList = MessageUtile.sendPhoneLengthMap(queryPhone.get(k));
                            for(int i = 0 ;i<phoneList.size();i++){
                                int msgType = JzbDataType.getInteger(queryPhone.get(k).get("msgtype"));
                                Map<String , Object> paraMap =  MessageUtile.setUpPara(listPara,map.get("sendpara").toString());
                                // 短信消息
                                if((msgType & 1) == 1){
                                    dataMap.put("receiver",phoneList.get(i));
                                    dataMap.put("sendpara",JSONObject.toJSONString((Map)paraMap.get("1")));
                                    // 设置发送参数
                                    Map<String , Object> sendMap = new HashMap<>();
                                    sendMap.put("receiver",phoneList.get(i));
                                    sendMap.put("sendpara",JSONObject.toJSONString(dataMap));
                                    if(map.containsKey("sendtime")){
                                        sendMap.put("sendtime",map.get("sendtime"));
                                    }
                                    // 添加日志
                                    dataMap.put("receiver",phoneList.get(i));
                                    msgListMapper.insertMsgList(dataMap);
                                    logger.info("添加短信日志 =========>"+dataMap.toString());
                                    logger.info("添加短信消息队列 =========>"+sendMap.toString());
                                    //添加只发送队列
                                    MssageInfo info = new ShortMessage(sendMap);
                                    MessageQueue.addShortMessage(info);
                                }
                                // 邮件消息
                                if((msgType & 2) == 2){
                                    // 设置发送参数
                                    Map<String , Object> sendMap = new HashMap<>();
                                    sendMap.put("receiver",map.get("receiver").toString());
                                    sendMap.put("sendpara",paraMap.get("2"));
                                    if(map.containsKey("sendtime")){
                                        sendMap.put("sendtime",map.get("sendtime"));
                                    }
                                    sendMap.put("msgid",dataMap.get("msgid"));
                                    if(map.containsKey("senduid")){
                                        sendMap.put("senduid",map.get("senduid"));
                                        sendMap.put("sendname",map.get("senduname"));
                                    }
                                    // 添加日志
                                    dataMap.put("receiver",map.get("receiver").toString());
                                    msgListMapper.insertMsgList(dataMap);
                                    logger.info("添加邮件日志 =========>"+dataMap.toString());
                                    logger.info("添加邮件消息队列 =========>"+sendMap.toString());
                                    // 添加至发送队列
                                    MssageInfo info = new MailMessage(sendMap);
                                    MessageQueue.addShortMail(info);
                                }
                                // 系统消息
                                if((msgType & 4) == 4){
                                }
                                // 微信消息
                                if((msgType & 8) == 8){
                                }
                            }
                        }
                    }
                    result = "success";
                }catch (Exception e){
                    JzbTools.logError(e);
                    result = "send is error";
                }
            }else{
                result = "usertype is error";
            }
        }catch (Exception e){
            JzbTools.logError(e);
            result = "param is error";
        }
        return result;
    }

    /**
     * 添加消息模板
     */
    public boolean saveMsgUserTeamplate(Map<String , Object> map){
        map.put("tempid",JzbRandom.getRandomChar(9));
        map.put("status",1);
        return msgListMapper.insertMsgUserTemplate(map) > 0 ? true : false ;
    }

    /**
     * 插入一条发送记录`
     */
    public int saveSendsUserMessage(Map<String , Object> map){
        return msgListMapper.insertSendUserMessage(map);
    }

    /**
     * 根据appid 获取checkcode
     */
    public String queryMsgOrganizeCheckcode(String appid){
        return msgListMapper.queryMsgOrganizeCheckcode(appid);
    }

    /**
     * 修改成已发送
     */
    public int  updateMessageListSendStatusByMsgid(String msgid){
        return msgListMapper.updateMessageListSendStatusByMegid(msgid);
    }

} // End class ShortMessageService
