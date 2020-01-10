package com.jzb.base.util;

import com.alibaba.fastjson.JSON;
import com.jzb.base.data.code.JzbDataCheck;
import com.jzb.base.message.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: gongWei
 * @Date: Created in 2020/1/4 10:48
 * @Description:
 */
public class SendSysMsgUtil {

    /**
     * APPID
     */
    private final static String APPID = "SADJHJ1FHAUS45FAJ455";
    /**
     * 入驻密码
     */
    private final static String SERCRET = "ABSUY0FASD4AA";
    /**
     * 校验码
     */
    private final static String CHECK_CODE = "FAHJKSFHJK400800FHAJK";
    /**
     * 消息组id
     */
    private final static String GROUPID = "10088";

    private final static String TITLE = "计支云";

    private final static String USER_TYPE = "1";

    private SendSysMsgUtil() {

    }

    /**
     *  @author: gongWei
     *  @Date:  2020/1/10 11:21
     *  @description: 适用于系统消息--消息发布对象为个人的情况
     */

    public static Map<String, Object> setMsgArg(Map<String, Object> map) {
        Map<String, Object> sendMap = new HashMap<>();

        Map<String, Object> valueMap = new HashMap<>(2);
        valueMap.put("Message",map.get("msg"));
        valueMap.put("uid",map.get("senduid"));
        valueMap.put("Code",map.get("code"));
        Map<String, Object> textMap = new HashMap<>(2);
        textMap.put("text",valueMap);
        Map<String, Object> sysMap = new HashMap<>(2);
        // 发送消息的内容context
        sysMap.put("sys", textMap);
        //发送参数填写
        sendMap.put("sendpara", JSON.toJSONString(sysMap));

        Map<String, Object> topicMap = new HashMap<>(2);
        topicMap.put("topic", map.get("topic_name"));
        topicMap.put("uid",map.get("senduid"));
        List<Map<String, Object>> topicList = new ArrayList<>();
        topicList.add(topicMap);
        sysMap.put("sys", topicList);
        String receiver = JSON.toJSONString(sysMap);
        sendMap.put("receiver", receiver);

        sendMap.put("groupid", GROUPID);
        sendMap.put("title", TITLE);
        sendMap.put("usertype", USER_TYPE);
        // 加密 appId + secret + groupId + title + userType + receiver + checkCode
        String MD5Str = APPID + SERCRET + GROUPID + TITLE + USER_TYPE + receiver + CHECK_CODE;
        String checkCodeMD5;
        try {
            checkCodeMD5 = JzbDataCheck.Md5(MD5Str);
            sendMap.put("checkcode", checkCodeMD5);
            sendMap.put("appid", APPID);
            sendMap.put("secret", SERCRET);
            sendMap.put("msgtag","00004");// ?????
            sendMap.put("senduid",map.get("senduid"));// ?????  这两个key的作用??? 在发送系统消息时应该如何定义key对应value的值
        } catch (Exception e) {
            Response.getResponseError();
            JzbTools.logError(e);
            e.printStackTrace();
        }
        return sendMap;
    }

}
