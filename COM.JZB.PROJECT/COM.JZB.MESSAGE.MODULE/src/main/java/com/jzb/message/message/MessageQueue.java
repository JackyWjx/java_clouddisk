package com.jzb.message.message;

import com.jzb.base.util.JzbTools;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 消息队列
 */
public final class MessageQueue {

    /**
     * 各消息队列
     */
    private final static Queue<MssageInfo> SMS_QUEUE = new ConcurrentLinkedQueue<>();

    private final static Queue<MssageInfo> MAIL_QUEUE = new ConcurrentLinkedQueue<>();

    private final static Queue<MssageInfo> SYSTEM_QUEUE = new ConcurrentLinkedQueue<>();

    /**
     * 待发送队列
     */
    private volatile  static Map<String , Map<String , Map<String , MssageInfo>>> waitSendQueue = new HashMap<>();

    /**
     * 获取对象的公平锁
     */
    private final static ReentrantLock SMS_LOCK = new ReentrantLock(true);

    private final static ReentrantLock MAIL_LOCK = new ReentrantLock(true);

    private final static ReentrantLock SYSTEM_LOCK = new ReentrantLock(true);

    /**
     * 私有构造方法
     */
    private MessageQueue() {
    } // End MessageQueue

    /**
     * 获取待发送队列
     * @return
     */
    public static Map<String , Map<String , Map<String , MssageInfo>>> getWaitSendQueue(){
        return waitSendQueue;
    }

    /***
     * 删除待发送队列
     * @param map
     * @return
     */
    public static boolean removerSendQuere(Map<String , Object > map ){
        boolean result ;
        try{
            Map<String , Map<String , Map<String , MssageInfo>>> waitMap = MessageQueue.getWaitSendQueue();
            String msgtag = map.get("msgtag").toString();
            // 根据业务id获取
            Map<String , Map<String , MssageInfo>> umap =  waitMap.get(msgtag);
            // 业务id下有多少个待发送消息
            int  msgtagCount  =   umap.size();
            if(map.containsKey("userids")){
                JSONObject  parajson = JSONObject.fromObject(map.get("userids"));
                if(map.get("usertype").equals("1")){
                    // 用户
                    if(parajson.containsKey("sms")  ){
                        List<Map<String , Object>> list  =(List<Map<String, Object>>) parajson.get("sms");
                        for(int i = 0 ; i< list.size() ;i++){
                            String  sms  =  list.get(i).get("sms").toString();
                            Map<String , MssageInfo> ms  =  umap.get(sms);
                            //  是否只有一种发送记录
                            if(map.size() == 1){
                                if(msgtagCount == 1){
                                    //  根据业务id删除
                                    umap.remove(msgtag);
                                } else{
                                    // 根据用户删除
                                    umap.remove(sms);
                                }
                            } else{
                                if(!JzbTools.isEmpty(map.get("msgtype"))){
                                    // 删除单个消息
                                    ms.remove(map.get("msgtype"));
                                } else{
                                    // 根据用户删除
                                    umap.remove(sms);
                                }
                            }
                        }
                    } else if(parajson.containsKey("meil") &&  map.get("msgtype").equals("2")){
                        List<Map<String , Object>> list  =(List<Map<String, Object>>)  parajson.get("meli");
                        for(int i = 0 ; i< list.size() ;i++){
                            String  emil  =  list.get(i).get("emil").toString();
                            Map<String , MssageInfo> ms  =  umap.get(emil);
                            //  是否只有一种发送记录
                            if(map.size() == 1){
                                if(msgtagCount == 1){
                                    //  根据业务id删除
                                    umap.remove(msgtag);
                                } else{
                                    // 根据用户删除
                                    umap.remove(emil);
                                }
                            } else{
                                if(!JzbTools.isEmpty(map.get("msgtype"))){
                                    // 删除单个消息
                                    ms.remove(map.get("msgtype"));
                                } else{
                                    // 根据用户删除
                                    umap.remove(emil);
                                }
                            }
                        }
                    }

                }else{
                    // 用户
                    if(parajson.containsKey("sms")  &&  map.get("msgtype").equals("1")){
                        List<Map<String , Object>> list  =(List<Map<String, Object>>) parajson.get("sms");
                        for(int i = 0 ; i< list.size() ;i++){
                            String  sms  =  list.get(i).get("sms").toString();
                            Map<String , MssageInfo> ms  =  umap.get(sms);
                            //  是否只有一种发送记录
                            if(map.size() == 1){
                                if(msgtagCount == 1){
                                    //  根据业务id删除
                                    umap.remove(msgtag);
                                } else{
                                    // 根据用户删除
                                    umap.remove(sms);
                                }
                            } else{
                                if(!JzbTools.isEmpty(map.get("msgtype"))){
                                    // 删除单个消息
                                    ms.remove(map.get("msgtype"));
                                } else{
                                    // 根据用户删除
                                    umap.remove(sms);
                                }
                            }
                        }
                    } else if(parajson.containsKey("meil") &&  map.get("msgtype").equals("2")){
                        List<Map<String , Object>> list  =(List<Map<String, Object>>)  parajson.get("meli");
                        for(int i = 0 ; i< list.size() ;i++){
                            String  emil  =  list.get(i).get("emil").toString();
                            Map<String , MssageInfo> ms  =  umap.get(emil);
                            //  是否只有一种发送记录
                            if(map.size() == 1){
                                if(msgtagCount == 1){
                                    //  根据业务id删除
                                    umap.remove(msgtag);
                                } else{
                                    // 根据用户删除
                                    umap.remove(emil);
                                }
                            } else{
                                if(!JzbTools.isEmpty(map.get("msgtype"))){
                                    // 删除单个消息
                                    ms.remove(map.get("msgtype"));
                                } else{
                                    // 根据用户删除
                                    umap.remove(emil);
                                }
                            }
                        }
                    }
                }
            }else{
                //  根据业务id删除
                umap.remove(msgtag);
            }
            result = true;
        }catch (Exception e){
            JzbTools.logError(e);
            result = false;
        }
        return result;
    }

    /**
     * 添加至待发送
     * @param info
     * @return
     */
    public static boolean setWaitSendQueue(MssageInfo info){
        boolean result ;
        try{
            Map<String , Object> parajson = (Map)info.getItem("para").getObject();
            Map<String , MssageInfo> msgMap = new HashMap<>();
            Map<String , Map<String , MssageInfo>> uidMap = new HashMap<>();
            msgMap.put(parajson.get("msgtype").toString(),info);
            uidMap.put(parajson.get("senduid").toString(),msgMap);
            if(waitSendQueue.containsKey(parajson.get("msgtag").toString())){
                if(waitSendQueue.get(parajson.get("msgtag").toString()).containsKey(parajson.get("senduid").toString())){
                    waitSendQueue.get(parajson.get("msgtag").toString()).get(parajson.get("senduid").toString()).put(parajson.get("msgtype").toString(),info);
                }else{
                    waitSendQueue.get(parajson.get("msgtag").toString()).put(parajson.get("senduid").toString(),msgMap);
                }
            }else{
                waitSendQueue.put(parajson.get("msgtag").toString(),uidMap);
            }
            result = true;
        }catch (Exception e){
            result = false;
            JzbTools.logError(e);
        }
        return result;
    }

    /**
     * 获取短消息
     * @param type
     * @return
     */
    public static MssageInfo getMessageInfo(int type) {
        MssageInfo result;
        switch (type) {
            case 1:
                result = getShortMessage();
                break;
            case 2:
                result = getShortMail();
                break;
            case 4:
                result = getShortSys();
                break;
            default:
                result = null;
                break;
        }
        return result;
    }

    /**
     * 加入一个短信消息到消息队列
     * @param msg
     */
    public static void addShortMessage(MssageInfo msg) {
        try {
            SMS_LOCK.lock();
            SMS_QUEUE.offer(msg);
        } finally {
            SMS_LOCK.unlock();
        }
    } // End addShortMessage

    /**
     * 获取一个短消息
     * @return
     */
    private static MssageInfo getShortMessage() {
        MssageInfo result = null;
        try {
            SMS_LOCK.lock();
            result = SMS_QUEUE.poll();
        } finally {
            SMS_LOCK.unlock();
        }
        return result;
    } // End getShortMessage

    /**
     * 加入一个邮件到消息队列
     * @param msg
     */
    public static void addShortMail(MssageInfo msg) {
        try {
            MAIL_LOCK.lock();
            MAIL_QUEUE.offer(msg);
        } finally {
            MAIL_LOCK.unlock();
        }
    } // End addShortMessage

    /**
     * 获取一个邮件信息
     * @return
     */
    private static MssageInfo getShortMail() {
        MssageInfo result = null;
        try {
            MAIL_LOCK.lock();
            result = MAIL_QUEUE.poll();
        } finally {
            MAIL_LOCK.unlock();
        }
        return result;
    } // End getShortMessage

    /**
     * 加入一个系统消息到消息队列
     * @param msg
     */
    public static void addShortSys(MssageInfo msg) {
        try {
            SYSTEM_LOCK.lock();
            SYSTEM_QUEUE.offer(msg);
        } finally {
            SYSTEM_LOCK.unlock();
        }
    } // End addShortMessage

    /**
     * 获取一个系统消息
     * @return
     */
    private static MssageInfo getShortSys() {
        MssageInfo result = null;
        try {
            SYSTEM_LOCK.lock();
            result = SYSTEM_QUEUE.poll();
        } finally {
            SYSTEM_LOCK.unlock();
        }
        return result;
    } // End getShortMessage
} // End class MessageQueue
