package com.jzb.message.service;

import com.jzb.base.data.date.JzbDateStr;
import com.jzb.base.data.date.JzbDateUtil;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.message.dao.MsgListMapper;
import com.jzb.message.message.MessageQueue;
import com.jzb.message.message.MssageInfo;
import com.jzb.message.message.ShortMessage;
import com.jzb.message.util.MessageUtile;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
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
    public String  sendSendMsg(String send_phone,String sms_content,String sms_no,String push_time,String sms_title){
        String senbo;
        try{
            // 设置参数实例
            Map<String , Object> map = new HashMap<>();
            map.put("sms_no",sms_no);
            map.put("sendname","SendMsg");
            map.put("sendid","SendMsg");
            map.put("cid","SendMsg");
            map.put("msgid", JzbRandom.getRandomNum(16));
            map.put("status",1);
            map.put("sendstatus",1);
            map.put("addtime", System.currentTimeMillis());
            // 设置发送主题
            if(!JzbTools.isEmpty(sms_title)){
                map.put("title",sms_title);
            }
            // 用来标识是一个用户还是多个用户
            if(!JzbTools.isEmpty(send_phone)){
                map.put("usertype",send_phone.length()> 11  ? "2" : "1");
            }
            // 是否有参数
            if(!JzbTools.isEmpty(sms_content)){
                // 去除转移字符
                map.put("sendpara",StringEscapeUtils.unescapeJava(sms_content));
            }
            // 每次最多一千个发送
            List<String> list = MessageUtile.sendPhoneLengthString(send_phone);
            if(!JzbTools.isEmpty(list)){
                // 获取阿里云秘钥
                List<Map<String , Object>> paraList = msgListMapper.queryMsgGroupConfigure("10086");
                for(int i=0;i < list.size();i++) {
                    map.put("receiver",list.get(i));
                    // 设置参数
                    Map<String , Object> sendMap = new HashMap<>();
                    net.sf.json.JSONObject aliyun = net.sf.json.JSONObject.fromObject(paraList.get(0).get("context").toString());
                    map.put("appid",aliyun.getString("appid"));
                    map.put("sercet",aliyun.getString("sercet"));
                    map.put("title",aliyun.getString("title"));
                    sendMap.put("sendpara", JSONObject.toJSONString(map));
                    // 是否有定时任务
                    if(!JzbTools.isEmpty(push_time)){
                        // 转换为long值
                        sendMap.put("sendtime", JzbDateUtil.getDate(push_time,JzbDateStr.yyyy_MM_dd_HH_mm_ss).getTime());
                    }
                    sendMap.put("msgid",map.get("msgid"));
                    if(map.containsKey("senduid")){
                        sendMap.put("senduid",map.get("senduid"));
                        sendMap.put("sendname",map.get("senduname"));
                    }
                    map.put("receiver",list.get(i));
                    msgListMapper.insertMsgList(map);
                    logger.info("短信日志 =========>"+map.toString());
                    logger.info("添加短信消息队列 =========>"+sendMap.toString());
                    MssageInfo info = new ShortMessage(sendMap);
                    MessageQueue.addShortMessage(info);
                }
                senbo = "success";
            }else{
                senbo = "send is error";
            }
        }catch (Exception e){
            e.printStackTrace();
            senbo = "param is error";
        }
        return senbo;
    }

}
