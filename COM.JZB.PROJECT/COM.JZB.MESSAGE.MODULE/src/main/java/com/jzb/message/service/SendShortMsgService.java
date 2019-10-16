package com.jzb.message.service;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.data.date.JzbDateStr;
import com.jzb.base.data.date.JzbDateUtil;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.message.dao.MsgListMapper;
import com.jzb.message.message.MessageQueue;
import com.jzb.message.message.MssageInfo;
import com.jzb.message.message.ShortMessage;
import com.jzb.message.util.MessageUtile;
import com.jzb.message.util.SendShortMsgResult;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: SendMsg业务层
 * @Author Han Bin
 */
@Service
public class SendShortMsgService {

    private final static Logger logger = LoggerFactory.getLogger(SendShortMsgService.class);

    @Autowired
    MsgListMapper msgListMapper;



    /**
     *  SendMsg添加至发送队列
     */
    public SendShortMsgResult sendSendMsg(String send_phone, String sms_content, String sms_no, String push_time, String sms_title, String uid){
        SendShortMsgResult result ;
        try{
            // 设置参数实例
            Map<String , Object> map = new HashMap<>();
            Map<String , Object> resultMap =  new HashMap<>();
            map.put("sms_no",sms_no);
            map.put("sendname","SendMsg");
            map.put("sendid","SendMsg");
            map.put("cid","SendMsg");
            map.put("msgid", JzbRandom.getRandomNum(16));
            map.put("status",1);
            map.put("sendstatus",1);
            map.put("addtime", System.currentTimeMillis());
            map.put("msgtag","0000000" + JzbRandom.getRandomCharLow(12));

            // 设置发送主题
            if(!JzbTools.isEmpty(sms_title)){
                map.put("title",sms_title);
            }
            // 用来标识是一个用户还是多个用户
            if(!JzbTools.isEmpty(send_phone)){
                map.put("usertype", "1");
            }
            if(!JzbTools.isEmpty(push_time)){
                map.put("sendtime", JzbDateUtil.getDate(push_time,JzbDateStr.yyyy_MM_dd_HH_mm_ss).getTime());
            }
            // 是否有参数
            if(!JzbTools.isEmpty(sms_content)){
                // 去除转移字符
                map.put("sendpara",StringEscapeUtils.unescapeJava(sms_content));
            }
            map.put("phone",send_phone);
            resultMap.putAll(map);
            // 每次最多一千个发送
            List<String> list = MessageUtile.sendPhoneLengthString(send_phone);
            // 获取阿里云秘钥
            List<Map<String , Object>> paraList = msgListMapper.queryMsgGroupConfigure("10086");
            for(int i=0;i < list.size();i++) {
                try{
                    Map<String , Object> paraMap =  new HashMap<>();
                    map.put("receiver",list.get(i));
                    // 设置参数
                    JSONObject aliyun = JSONObject.fromObject(paraList.get(0).get("context").toString());
                    logger.info("短信日志 =========>"+map.toString());
                    paraMap.put("para",map);
                    paraMap.put("temp",sms_content);
                    Map<String , Object>  config =  new HashMap<>();
                    Map<String , Object>  context =  new HashMap<>();
                    context.put("title",aliyun.getString("title"));
                    context.put("sms_no",sms_no);
                    context.put("appid",aliyun.getString("appid"));
                    context.put("sercet",aliyun.getString("sercet"));
                    config.put("context",context);
                    paraMap.put("config",config);
                    paraMap.put("receive",list.get(i));
                    logger.info("添加短信消息队列 =========>"+paraMap.toString());
                    MssageInfo info = new ShortMessage(paraMap);
                    if(!JzbTools.isEmpty(uid)){
                       map.put("senduid",uid);
                       MessageQueue.setWaitSendQueue(info);
                    }else{
                        MessageQueue.addShortMessage(info);
                    }
                }catch (Exception e){
                    map.put("sendstatus",2);
                    JzbTools.logError(e);
                }finally {
                    msgListMapper.insertMsgList(map);
                }
            }
            result = SendShortMsgResult.getSuccess();
            if(resultMap.containsKey("sendname")){
                resultMap.remove("sendname");
                resultMap.remove("cid");
                resultMap.remove("status");
                resultMap.remove("usertype");
                resultMap.remove("addtime");
                resultMap.remove("sendid");
            }
            result.setData(resultMap);
        }catch (Exception e){
            JzbTools.logError(e);
            result = SendShortMsgResult.getRerror();
        }
        return result;
    }

}
