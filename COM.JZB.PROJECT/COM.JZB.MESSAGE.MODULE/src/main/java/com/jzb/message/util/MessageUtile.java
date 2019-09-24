package com.jzb.message.util;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.util.JzbTools;
import net.sf.json.JSONObject;

import java.util.*;

/**
 * @Description: 工具类
 * @Author Han Bin
 */
public class MessageUtile {

    /**
     *  替换发送参数
     */
    public static Map<String , Object> setUpPara(List<Map<String , Object>> list , String sendPara){
        Map<String , Object> map = new HashMap<>();
        try{
                JSONObject json = JSONObject.fromObject(sendPara);
                for(int i = 0; i<list.size();i++){
                    if(JzbDataType.getInteger(list.get(i).get("msgtype")) == 1){
                        // 短信消息 判断是否存在需要修改的参数
                        if(json.containsKey("sms")){
                            JSONObject smsJson =  JSONObject.fromObject(json.getString("sms"));
                            // 获取所有需要修改参数的key
                            Set smsSet =  smsJson.keySet();
                            // 循环替换
                            Iterator<String> iterator =smsSet.iterator();
                            JSONObject smsParaJson = JSONObject .fromObject(list.get(i).get("context").toString());
                            while (iterator.hasNext()){
                                String code =  iterator.next();
                                smsParaJson.remove(code);
                                smsParaJson.accumulate(code,smsJson.getString(code));
                            }
                            map.put("1",smsParaJson);
                        }else{
                            JSONObject smsParaJson = JSONObject .fromObject(list.get(i).get("context").toString());
                            map.put("1",smsParaJson);
                        }
                    }else if(JzbDataType.getInteger(list.get(i).get("msgtype")) == 2){
                        // 邮件消息
                        if (json.containsKey("mail")) {
                            JSONObject mailJson =  JSONObject.fromObject(json.getString("mail"));
                            // 获取所有需要修改参数的key
                            Set mailSet =  mailJson.keySet();
                            // 循环替换
                            Iterator<String> iterator =mailSet.iterator();
                            JSONObject mailParaJson = JSONObject .fromObject(list.get(i).get("context").toString());
                            while (iterator.hasNext()){
                                String code =  iterator.next();
                                mailParaJson.remove(code);
                                mailParaJson.accumulate(code,mailJson.getString(code));
                            }
                            // 添加输出队列
                            map.put("2",mailParaJson);
                        }else {
                            JSONObject mailParaJson = JSONObject.fromObject(list.get(i).get("context").toString());
                            map.put("2",mailParaJson);
                        }
                    }else if(JzbDataType.getInteger(list.get(i).get("msgtype")) == 4){
                        // 微信消息
                        if(json.containsKey("sys")){
                            JSONObject sysJson =  JSONObject.fromObject(json.getString("sys"));
                            // 获取所有需要修改参数的key
                            Set sysSet =  sysJson.keySet();
                            // 循环替换
                            Iterator<String> iterator =sysSet.iterator();
                            JSONObject sysParaJson = JSONObject .fromObject(list.get(i).get("context").toString());
                            while (iterator.hasNext()){
                                String code =  iterator.next();
                                sysParaJson.remove(code);
                                sysParaJson.accumulate(code,sysJson.getString(code));
                            }
                            map.put("4",sysParaJson);
                        }else{
                            JSONObject sysParaJson = JSONObject .fromObject(list.get(i).get("context").toString());
                            map.put("4",sysParaJson);
                        }

                    } else if (JzbDataType.getInteger(list.get(i).get("msgtype")) == 8) {
                        // 系统消息
                        if(json.containsKey("wechat")){
                            JSONObject wechatJson =  JSONObject.fromObject(json.getString("wechat"));
                            // 获取所有需要修改参数的key
                            Set wechatSet =  wechatJson.keySet();
                            // 循环替换
                            Iterator<String> iterator =wechatSet.iterator();
                            JSONObject weChatParaJson = JSONObject .fromObject(list.get(i).get("context").toString());
                            while (iterator.hasNext()){
                                String code =  iterator.next();
                                weChatParaJson.remove(code);
                                weChatParaJson.accumulate(code,wechatJson.getString(code));
                            }
                            map.put("8",weChatParaJson);
                        }else{
                            JSONObject weChatParaJson = JSONObject .fromObject(list.get(i).get("context").toString());
                            map.put("8",weChatParaJson);
                        }
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
     *
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
