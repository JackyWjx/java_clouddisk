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
     *
     *  receive 接受者
     *  map 合并配置  temp 模板  config  配置
     *  dataMap 基本数据
     */
    public static boolean saveWeChatInfo(String receive,Map<String , Object> map,Map<String , Object> dataMap){

        return true;
    }

    /**
     *  平台 添加至发送队列
     *
     *  receive 接受者
     *  map 合并配置  temp 模板  config  配置
     *  dataMap 基本数据
     */
    public static boolean saveSystemInfo(String receive,Map<String , Object> map,Map<String , Object> dataMap){
        boolean result ;
        try{
            logger.info("平台接收人=======>>"+receive);
            logger.info("平台模板  =======>>"+map.get("temp").toString());
            logger.info("平台推送服务器  =======>>"+map.get("config").toString());
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
     *  邮件 添加至发送队列
     *
     *  receive 接受者
     *  map 合并配置  temp 模板  config  配置
     *  dataMap 基本数据
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
     *
     *  receive 接受者
     *  map 合并配置  temp 模板  config  配置
     *  dataMap 基本数据
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

    /**
     * 找到指定消息类型  的模板参数
     *
     * list  模板
     * @return
     */
    public  static Map<String , Object> templeMap(List<Map<String , Object>>  list){
        Map<String , Object> map  = new HashMap<>();
        for(int i= 0 ; i< list.size() ;i++){
            if(list.get(i).get("msgtype").toString().equals("1")){
                map.put("1",list.get(i).get("context"));
            }else if(list.get(i).get("msgtype").toString().equals("2")){
                map.put("2",list.get(i).get("context"));
            }else if(list.get(i).get("msgtype").toString().equals("4")){
                map.put("4",list.get(i).get("context"));
            }else if(list.get(i).get("msgtype").toString().equals("8")){
                map.put("8",list.get(i).get("context"));
            }
        }
        return  map;
    }




    /***
     *  将模板 和配置 按照消息类型 分类使用
     *
     *  configList 配置
     *  tempList 以分类模板
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
     *  替换发送参数
     *
     *  {"code":"123"} 形式替换
     *  para 模板参数
     *  sendPara  用户发送参数
     *  msgtype 消息类型
     */
    public static Map<String , Object> setUpParaByUser(String para , String sendPara,String msgtype){
        Map<String , Object> map = new HashMap<>();
        try{
            if(!JzbTools.isEmpty(sendPara)){
                JSONObject smsJson = JSONObject.fromObject(sendPara);
                // 获取所有需要修改参数的key
                Set smsSet =  smsJson.keySet();
                // 循环替换
                Iterator<String> iterator =smsSet.iterator();
                JSONObject smsParaJson = JSONObject .fromObject(para);
                while (iterator.hasNext()){
                    String code =  iterator.next();
                    smsParaJson.remove(code);
                    smsParaJson.accumulate(code,smsJson.getString(code));
                }
                map.put(msgtype,smsParaJson);
            }else{
                map.put(msgtype,para);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  map;
    }


    /**
     *  替换发送参数
     *
     *  张三{{name}}  正则替换
     *  para 模板参数
     *  sendPara  用户发送参数
     *  msgtype 消息类型
     */
    public static Map<String , Object> setUpParaByMeil(String para , String sendPara,String msgtype){
        Map<String , Object> map = new HashMap<>();
        try{
            if(!JzbTools.isEmpty(para)){
                JSONObject paraJson =  JSONObject.fromObject(sendPara);
                String regex = "\\{\\{([^}])*\\}\\}";
                Pattern compile = Pattern.compile(regex);
                Matcher matcher = compile.matcher(para);
                while(matcher.find()){
                    String group = matcher.group();
                    para = para.replace(group,paraJson.getString(group.replace("{","").replace("}","")));
                }
                map.put(msgtype,para);
            }else{
                map.put(msgtype,para);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  map;
    }

    /**
     *  替换发送参数  用户组
     *
     *  list 模板类型
     *  sendPara  用户参数
     *  sendMap 用户常用参数配置 如username 张三 sex 男
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
     *
     *  msgType 权限
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
     *
     * msgType 权限
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
}
