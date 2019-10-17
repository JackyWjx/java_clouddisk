package com.jzb.message.util;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.util.JzbTools;
import com.jzb.message.dao.MsgListMapper;
import com.jzb.message.message.MessageQueue;
import com.jzb.message.message.MssageInfo;
import com.jzb.message.message.ShortMessage;
import com.jzb.message.service.ShortMessageService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description: 工具类
 * @Author Han Bin
 */
public class MessageUtile {

    private final static Logger logger = LoggerFactory.getLogger(MessageUtile.class);

    /**
     * 私有实例
     */
    private final static MessageUtile utile = new MessageUtile();

    /**
     *  微信 添加至发送队列
     */
    public static boolean saveWeChatInfo(String receive,Map<String , Object> map,Map<String , Object> dataMap){

        return true;
    }

    /**
     *  平台 添加至发送队列
     */
    public static boolean saveSystemInfo(String receive,Map<String , Object> map,Map<String , Object> dataMap){
        return true;
    }

    /**
     *  邮件 添加至发送队列
     */
    public static boolean saveMeilInfo(String receive,Map<String , Object> map,Map<String , Object> dataMap){
        boolean result ;
        try{
            logger.info("邮件接收人=======>>"+receive);
            logger.info("邮件模板  =======>>"+map.get("temp").toString());
            logger.info("邮件配置  =======>>"+map.get("config").toString());
            map.put("receive",receive);
            map.put("para",dataMap);
            // 添加至发送队列
            MssageInfo info = new ShortMessage(map);
            MessageQueue.setWaitSendQueue(info);
            logger.info("添加至发送队列");
            result = true;
        }catch (Exception e){
            result =  false;
            JzbTools.logError(e);
        }
        return result;
    }

    /**
     *  短信 添加至发送队列
     */
    public static boolean saveMessageInfo(String receive,Map<String , Object> map,Map<String , Object> dataMap){
        boolean result ;
        try{
            logger.info("短信接收人 =======>>"+receive);
            logger.info("短信模板   =======>>"+map.get("temp").toString());
            logger.info("短信配置   =======>>"+map.get("config").toString());
            map.put("receive",receive);
            map.put("para",dataMap);
            // 添加至发送队列
            MssageInfo info = new ShortMessage(map);
            MessageQueue.setWaitSendQueue(info);
            logger.info("添加至发送队列");
            result = true;
        }catch (Exception e){
            result =  false;
            JzbTools.logError(e);
        }
        return result;
    }

    /***
     *  分类
     */
    public static Map<String , Object> msgTypeGroup(List<Map<String , Object>> configList ,Map<String , Object> tempList){
        Map<String , Object> map  = new HashMap<>();
        try{
            for(int k = 0 ; k < configList.size() ;k++){
                Map<String , Object> list = new HashMap<>();
                if(JzbDataType.getInteger(configList.get(k).get("msgtype")) == 1){
                    list.put("config",configList.get(k));
                    list.put("temp",tempList.get("1"));
                    map.put("1",list);
                }else if(JzbDataType.getInteger(configList.get(k).get("msgtype")) == 2){
                    list.put("config",configList.get(k));
                    list.put("temp",tempList.get("2"));
                    map.put("2",list);
                }else if(JzbDataType.getInteger(configList.get(k).get("msgtype")) == 4){
                    list.put("config",configList.get(k));
                    list.put("temp",tempList.get("4"));
                    map.put("4",list);
                }else if (JzbDataType.getInteger(configList.get(k).get("msgtype")) == 8) {
                    list.put("config",configList.get(k));
                    list.put("temp",tempList.get("8"));
                    map.put("8",list);
                }
            }
        }catch (Exception e){
            JzbTools.logError(e);
        }
        return  map;
    }

    /**
     *  替换发送参数  用户
     */
    public static Map<String , Object> setUpParaByUser(String para , String sendPara,Map<String , Object> sendMap){
        Map<String , Object> map = new HashMap<>();
        try{
            if(!JzbTools.isEmpty(para)){
                JSONObject smsJson = JSONObject.fromObject(para);
                // 获取所有需要修改参数的key
                Set smsSet =  smsJson.keySet();
                // 循环替换
                Iterator<String> iterator =smsSet.iterator();
                JSONObject smsParaJson = JSONObject .fromObject(sendPara);
                while (iterator.hasNext()){
                    String code =  iterator.next();
                    smsParaJson.remove(code);
                    smsParaJson.accumulate(code,smsJson.getString(code));
                }
                map.put("1",smsParaJson);
            }else{
                map.put("1",sendPara);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  map;
    }


    /**
     *  替换发送参数  用户组
     */
    public static Map<String , Object> setUpParaByGroupid(List<Map<String , Object>> list , String sendPara,Map<String , Object> sendMap){
        Map<String , Object> map = new HashMap<>();
        try{
            JSONObject sendpa = JSONObject.fromObject(sendPara);
            for(int i = 0; i<list.size();i++){
                if(JzbDataType.getInteger(list.get(i).get("msgtype")) == 1){
                    JSONObject smsParaJson ;
                    JSONObject smsParas = JSONObject.fromObject( list.get(i).get("context").toString());
                    if (sendpa.containsKey(sendMap.get("groupid"))) {
                        smsParaJson = JSONObject.fromObject(sendpa.getString(sendMap.get("groupid").toString()));
                    } else {
                        smsParaJson = JSONObject.fromObject(sendpa.getString("common"));
                    }
                    if(!JzbTools.isEmpty(sendpa)){
                        Set set = smsParas.keySet();
                        // 循环替换
                        Iterator<String> iterator = set.iterator();
                        while (iterator.hasNext()){
                            String code =  iterator.next();
                            smsParas.remove(code);
                            smsParas.accumulate(code,smsParaJson.getString(code));
                        }
                        map.put("1",smsParas);
                    }else{
                        map.put("1",smsParas);
                    }
                } else if(JzbDataType.getInteger(list.get(i).get("msgtype")) == 2){
                    String context = list.get(i).get("context").toString();
                    if(!JzbTools.isEmpty(sendpa)){
                        JSONObject meilParaJson ;
                        if (sendpa.containsKey(sendMap.get("groupid"))) {
                            meilParaJson = JSONObject.fromObject(sendpa.getString(sendMap.get("groupid").toString()));
                        } else {
                            meilParaJson = JSONObject.fromObject(sendpa.getString("common"));
                        }
                        // 正则替换
                        String regex = "\\{\\{([^}])*\\}\\}";
                        Pattern compile = Pattern.compile(regex);
                        Matcher matcher = compile.matcher(context);
                        while(matcher.find()){
                            String group = matcher.group();
                            context = context.replace(group,meilParaJson.getString(group.replace("{","").replace("}","")));
                        }
                        map.put("2",context);
                    }else {
                        // 添加输出队列
                        map.put("2",context);
                    }
                } else if(JzbDataType.getInteger(list.get(i).get("msgtype")) == 4){
                } else if (JzbDataType.getInteger(list.get(i).get("msgtype")) == 8) {
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  map;
    }

    /**
     *  获取权限
     */
    public static int encryptionMsgType(String msgType){
        int msgtype = 0;
        try{
            String[] strings =  msgType.split(",");
            for(int i = 0; i < strings.length ;i++){
                if(i == 0){
                    msgtype = JzbDataType.getInteger(strings[i]);
                }else{
                    msgtype = JzbDataType.getInteger(strings[i]) | msgtype ;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            msgtype = 0;
        }
        return msgtype;
    }

    /**
     * 解析权限
     */
    public static String decryptMsgType(int msgType){
        String result ="";
        try{
            if((msgType & 1)== 1){
                result = result + "1,";
            }
            if((msgType & 2) == 2){
                result = result + "2,";
            }
            if((msgType & 4) ==  4){
                result = result + "4,";
            }
            if((msgType & 8) ==  8){
                result = result + "8,";
            }
            result = result.substring(0,result.length()-1);
        }catch (Exception e){
            e.printStackTrace();
            result = null;
        }
        return result;
    }

    /**
     *  获取邮件发送人
     */
    public static List<String> sendMailList(List<Map<String , Object>> list){
        List<String> resultlist =  new ArrayList<>();
        try{
            for(int i =0 ;i< list.size();i++){
                String[] str = list.get(i).get("tarobj").toString().split(",");
                for(int k = 0 ;k <str.length ;k++){
                    resultlist.add(str[k]);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            list = null;
        }
        return  resultlist;
    }

    /**
     * 发送的电话号码一次不能超过一千个 分割
     */
    public static List<String> sendPhoneLengthString(String phone){
        List<String> list =  new ArrayList<>();
        try{
            int count  = 999;
            String strPhone = "";
            String[] str = phone.split(",");
            if(str.length > 999){
                for(int i=0;i<str.length;i++){
                    if(i == count-1){
                        strPhone = strPhone +","+ str[i];
                        count = count + 999;
                        if(strPhone.indexOf(",") == 0){
                            strPhone = strPhone.substring(1,strPhone.length());
                        }
                        list.add(strPhone);
                        strPhone = "";
                    }else{
                        strPhone =  strPhone +","+ str[i];
                    }
                }
            }else{
                list.add(phone);
            }
        }catch (Exception e){
            e.printStackTrace();
            list = null;
        }
        return  list;
    }

    /***
     * 发送的电话号码一次不能超过一千个 分割
     */
    public static List<String> sendPhoneLengthMap(Map<String , Object> list){
        List<String> resultList =  new ArrayList<>();
        try{
            int count  = 999;
            String strPhone = "";
            String[] str = list.get("tarobj").toString().split(",");
            for(int k = 0; k<str.length;k++){
                if(k == count-1){
                    strPhone = strPhone +","+ str[k];
                    count = count + 999;
                    if(strPhone.indexOf(",") == 0){
                        strPhone = strPhone.substring(1,strPhone.length());
                    }
                    strPhone = "";
                }else{
                    strPhone =  strPhone +","+ str[k];
                    if(k == str.length - 1){
                        strPhone =  strPhone.substring(1,strPhone.length());
                        resultList.add(strPhone);
                        strPhone = "";
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            resultList = null;
        }
        return  resultList;
    }

    /**
     * 发送的电话号码一次不能超过一千个 分割
     */
    public static List<String> sendPhoneLengthList(List<Map<String , Object>> list){
        List<String> resultList =  new ArrayList<>();
        try{
            int count  = 999;
            String strPhone = "";
            for(int i=0;i<list.size();i++){
                String[] str = list.get(i).get("tarobj").toString().split(",");
                for(int k = 0; k<str.length;k++){
                    if(k == count-1){
                        strPhone = strPhone +","+ str[k];
                        count = count + 999;
                        if(strPhone.indexOf(",") == 0){
                            strPhone = strPhone.substring(1,strPhone.length());
                        }
                        strPhone = "";
                    }else{
                        strPhone =  strPhone +","+ str[k];
                        if(i == list.size() - 1){
                            strPhone =  strPhone.substring(1,strPhone.length());
                            resultList.add(strPhone);
                            strPhone = "";
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            resultList = null;
        }
        return  resultList;
    }
}
