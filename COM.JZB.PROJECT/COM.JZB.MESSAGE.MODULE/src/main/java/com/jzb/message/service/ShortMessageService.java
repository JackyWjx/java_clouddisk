package com.jzb.message.service;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.message.dao.MsgListMapper;
import com.jzb.message.message.MessageQueue;
import com.jzb.message.message.MssageInfo;
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
    public  Map<String , Object> sendShortMsg(Map<String , Object> map,Map<String , Object> cmap){
        Map<String , Object> dataMap = new HashMap<>();
        try{
            dataMap.put("cid",cmap.get("cid"));
            dataMap.put("groupid",map.get("groupid"));
            dataMap.put("title",map.get("title"));
            dataMap.put("usertype",map.get("usertype"));
            dataMap.put("msgtag",map.get("msgtag"));
            dataMap.put("msgid", JzbRandom.getRandomNum(16));
            dataMap.put("addtime", System.currentTimeMillis());
            dataMap.put("status","1");
            dataMap.put("sendstatus",1);
            if(map.containsKey("senduid")){
                dataMap.put("senduid",map.get("senduid"));
            }
            if(map.containsKey("sendname")){
                dataMap.put("sendname",map.get("sendname"));
            }
            if(map.containsKey("sendtime")){
                dataMap.put("sendtime",map.get("sendtime"));
            }
            if(map.containsKey("remark")){
                dataMap.put("summary",map.get("remark"));
            }
            // 获取模板配置信息
            List<Map<String , Object>> listGroupTemplate = msgListMapper.queryMsgUserGroupTemplate(map.get("groupid").toString());
            // 获取用户发送配置
            List<Map<String , Object>>   listGroupConfig = msgListMapper.queryMsgGroupConfigure(map.get("groupid").toString());
            if("1".equals(dataMap.get("usertype"))){
                try {
                    JSONObject sendJson = JSONObject.fromObject(map.get("receiver"));
                    JSONObject  json = JSONObject.fromObject(map.get("sendpara"));
                    if (sendJson.containsKey("sms")) {
                        // 短信消息
                        List<Map<String , Object>> list =  (List<Map<String, Object>>) sendJson.get("sms");
                        for(int i = 0 ; i < list.size() ; i++){
                            Map<String , Object> smsUserMap =  list.get(i);
                            dataMap.put("receiver", smsUserMap.get("photo"));
                            dataMap.put("msgtype",'1');
                            if(dataMap.containsKey("uid")){
                                dataMap.put("senduid",smsUserMap.get("uid"));
                                smsUserMap.remove("uid");
                            }else{
                                dataMap.put("senduid",smsUserMap.get("photo"));
                            }
                            smsUserMap.remove("photo");
                            // 参数替换
                            Map<String, Object> paraGroupMap = MessageUtile.setUpParaByUser(smsUserMap.toString(),json.get("sms").toString(),dataMap);
                            Map<String, Object> paraMap = MessageUtile.msgTypeGroup(listGroupConfig, paraGroupMap);
                            boolean msgboolean = MessageUtile.saveMessageInfo(dataMap.get("receiver").toString(), (Map<String, Object>) paraMap.get("1"), dataMap);
                            logger.info("添加短信消息队列" + msgboolean);
                            // 添加 日志记录
                            msgListMapper.insertMsgList(dataMap);
                        }
                    } else if (sendJson.containsKey("meil")) {
                        // 邮件消息
                        List<Map<String , Object>> list =  (List<Map<String, Object>>) sendJson.get("meil");
                        for(int i = 0 ; i < list.size() ; i++){
                            Map<String , Object> meilUserMap =  list.get(i);
                            dataMap.put("receiver", meilUserMap.get("photo"));
                            dataMap.put("msgtype",'1');
                            if(dataMap.containsKey("uid")){
                                dataMap.put("senduid",meilUserMap.get("uid"));
                                meilUserMap.remove("uid");
                            }else{
                                dataMap.put("senduid",meilUserMap.get("photo"));
                            }
                            meilUserMap.remove("photo");
                            // 参数替换
                            Map<String, Object> paraGroupMap = MessageUtile.setUpParaByUser(meilUserMap.toString(),json.get("sms").toString(),dataMap);
                            Map<String, Object> paraMap = MessageUtile.msgTypeGroup(listGroupConfig, paraGroupMap);
                            boolean msgboolean = MessageUtile.saveMeilInfo(dataMap.get("photo").toString(), (Map<String, Object>) paraMap.get("2"), dataMap);
                            logger.info("添加邮件消息队列" + msgboolean);
                            // 添加 日志记录
                            msgListMapper.insertMsgList(dataMap);
                        }
                    } else if (sendJson.containsKey("sys")) {
                          // 系统消息
                    } else if (sendJson.containsKey("wechat")) {
                          // 微信消息
                    }
                }catch (Exception e){
                    dataMap.put("sendstatus", 2);
                    JzbTools.logError(e);
                }
            }else {
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
        return  dataMap;
    }

    /**
     * 根据业务删除指定id
     */
    public Map<String , Object> waitSendMessage(Map<String , Object> map){
        Map<String , Object>  result = new HashMap<>();
        try{
            result.put("message","success");
            Map<String , Map<String , Map<String , MssageInfo>>> waitMap = MessageQueue.getWaitSendQueue();
            String msgtag = map.get("msgtag").toString();
            // 根据业务id获取
            if(waitMap.containsKey(msgtag)){
                Map<String , Map<String , MssageInfo>> umap =  waitMap.get(msgtag);
                if(umap.containsKey(map.get("uid"))){
                    Map<String , MssageInfo> msgmap =   umap.get(map.get("uid"));
                    if(msgmap.containsKey("msgtype")){
//                        MssageInfo info = msgmap.get(map.get("msgtype"));
//                        // 判断是否删除指定人
//                        if(!JzbTools.isEmpty(map.get("delsendname"))){
//                            String  receive = info.getItem("receive").getString();
//                            try{
//                                // 删除 指定参数
//                                String[] sendname = map.get("delsendname").toString().split(",");
//                                for(int i = 0 ; i < sendname.length-1 ;i++){
//                                    receive = receive.replace(sendname[i],"");
//                                }
//                            }catch (Exception e){
//                                receive = info.getItem("receive").getString();
//                                JzbTools.logError(e);
//                                result.put("message","del is delsendname error");
//                            }finally {
//                                //  联系人是否删除为空
//                                if(!JzbTools.isEmpty(receive)){
//                                    info.setItem("receive",receive);
//                                }else{
//                                    waitMap.remove(msgtag);
//                                }
//                            }
//                        }else{
//                            result.put("message","msgtype is success delsendname is null");
                            msgmap.remove(map.get("msgtype").toString());
//                        }
                    }else{
                        result.put("message","uid is success msgtype is null");
                        umap.remove(map.get("uid"));
                    }
                }else{
                    result.put("message","msgtag is success uid is null");
                    waitMap.remove(msgtag);
                }
            }else{
                result.put("message","msgtag is null");
            }
        }catch (Exception e){
            JzbTools.logError(e);
            result.put("message","para is error");
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
    public Map<String , Object> queryMsgOrganizeCheckcode(String appid){
        return msgListMapper.queryMsgOrganizeCheckcode(appid);
    }

    /**
     * 修改成已发送
     */
    public int  updateMessageListSendStatusByMsgid(Map<String , Object> msgid){
        return msgListMapper.updateMessageListSendStatusByMegid(msgid);
    }

} // End class ShortMessageService
