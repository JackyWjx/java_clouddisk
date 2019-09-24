package com.jzb.message.service;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.data.date.JzbDateUtil;
import com.jzb.base.util.JzbRandom;
import com.jzb.message.dao.MessageGroupMapper;
import com.jzb.message.util.MessageUtile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description: 企业消息业务
 * @Author Han Bin
 */
@Service
public class MessageGroupService {

    /**
     * 企业消息DB
     */
    @Autowired
    MessageGroupMapper groupMapper;

    /**
     * 查询企业消息组
     */
    public List<Map<String , Object>> listMessageGroup(Map<String , Object> map){
        List<Map<String , Object>> resultMap;
        try {
            int page = JzbDataType.getInteger(map.get("page")) == 0 ? 0 : JzbDataType.getInteger(map.get("page")) - 1;
            map.put("page", page * JzbDataType.getInteger(map.get("rows")));
            map.put("rows", JzbDataType.getInteger(map.get("rows")));
            resultMap = groupMapper.queryMessageGroup(map);
            // 解析
            for(int i =0 ;i <resultMap.size();i++){
                resultMap.get(i).put("msgtype",MessageUtile.decryptMsgType(JzbDataType.getInteger(resultMap.get(i).get("msgtype"))));
            }
        }catch (Exception e){
            e.printStackTrace();
            resultMap =null;
        }
        return resultMap;
    }

    /**
     * 查询企业消息组用户
     */
    public List<Map<String , Object>> listMessageUserGroup(Map<String , Object> map){
        List<Map<String , Object>> resultMap;
        try {
            int page = JzbDataType.getInteger(map.get("page")) == 0 ? 0 : JzbDataType.getInteger(map.get("page")) - 1;
            map.put("page", page * JzbDataType.getInteger(map.get("rows")));
            map.put("rows", JzbDataType.getInteger(map.get("rows")));
            resultMap = groupMapper.queryMessageUserGroup(map);
            // 解析
            for(int i =0 ;i <resultMap.size();i++){
                resultMap.get(i).put("msgtype",MessageUtile.decryptMsgType(JzbDataType.getInteger(resultMap.get(i).get("msgtype"))));
            }
        }catch (Exception e){
            e.printStackTrace();
            resultMap =null;
        }
        return resultMap ;
    }

    /**
     * 查询企业消息组配置
     */
    public List<Map<String , Object>> listMsgGroupConfigure(Map<String , Object> map){
        List<Map<String , Object>> resultMap;
        try{
            int page  = JzbDataType.getInteger(map.get("page"))  == 0  ? 0 : JzbDataType.getInteger(map.get("page"))- 1;
            map.put("page",page * JzbDataType.getInteger(map.get("rows")));
            map.put("rows",JzbDataType.getInteger(map.get("rows")));
            resultMap =  groupMapper.queryMsgGroupConfigure(map);
            // 解析
            for(int i =0 ;i <resultMap.size();i++){
                resultMap.get(i).put("msgtype",MessageUtile.decryptMsgType(JzbDataType.getInteger(resultMap.get(i).get("msgtype"))));
            }
        }catch (Exception e){
            e.printStackTrace();
            resultMap = null;
        }
        return resultMap;
    }

    /**
     * 查询企业消息组总数
     */
    public int queryMessageGroupCount(Map<String , Object> map){
        return groupMapper.queryMessageGroupCount(map);
    }

    /**
     * 查询企业消息组用户总数
     */
    public int queryMessageUserGroupCount(Map<String , Object> map){
        return groupMapper.queryMessageUserGroupCount(map);
    }

    /**
     * 查询企业消息组配置总数
     */
    public int queryMsgGroupConfigureCount(Map<String , Object> map){
        return groupMapper.queryMsgGroupConfigureCount(map);
    }

    /**
     * 模糊查询企业消息组
     */
    public List<Map<String , Object>> searchMessageGroup(Map<String , Object> map){
        List<Map<String , Object>> resultMap;
        try {
            int page  = JzbDataType.getInteger(map.get("page"))  == 0  ? 0 : JzbDataType.getInteger(map.get("page"))- 1;
            map.put("page",page * JzbDataType.getInteger(map.get("rows")));
            map.put("rows",JzbDataType.getInteger(map.get("rows")));
            resultMap =  groupMapper.searchMessageGroup(map);
            // 解析
            for(int i =0 ;i <resultMap.size();i++){
                resultMap.get(i).put("msgtype",MessageUtile.decryptMsgType(JzbDataType.getInteger(resultMap.get(i).get("msgtype"))));
            }
        }catch (Exception e){
            e.printStackTrace();
            resultMap = null;
        }
        return groupMapper.searchMessageGroup(map);
    }

    /**
     * 模糊查询企业消息组用户
     */
    public List<Map<String , Object>> searchMessageUserGroup(Map<String , Object> map){
        List<Map<String , Object>> resultMap;
        try {
            int page  = JzbDataType.getInteger(map.get("page"))  == 0  ? 0 : JzbDataType.getInteger(map.get("page"))- 1;
            map.put("page",page * JzbDataType.getInteger(map.get("rows")));
            map.put("rows",JzbDataType.getInteger(map.get("rows")));
            resultMap =  groupMapper.searchMessageUserGroup(map);
            // 解析
            for(int i =0 ;i <resultMap.size();i++){
                resultMap.get(i).put("msgtype",MessageUtile.decryptMsgType(JzbDataType.getInteger(resultMap.get(i).get("msgtype"))));
            }
        }catch (Exception e){
            e.printStackTrace();
            resultMap = null;
        }
        return resultMap;
    }

    /**
     * 模糊查询企业消息组配置
     */
    public List<Map<String , Object>> searchMsgGroupConfigure(Map<String , Object> map){
        List<Map<String , Object>> resultMap;
        try {
            int page  = JzbDataType.getInteger(map.get("page"))  == 0  ? 0 : JzbDataType.getInteger(map.get("page"))- 1;
            map.put("page",page * JzbDataType.getInteger(map.get("rows")));
            map.put("rows",JzbDataType.getInteger(map.get("rows")));
            resultMap =  groupMapper.searchMsgGroupConfigure(map);
            // 解析
            for(int i =0 ;i <resultMap.size();i++){
                resultMap.get(i).put("msgtype",MessageUtile.decryptMsgType(JzbDataType.getInteger(resultMap.get(i).get("msgtype"))));
            }
        }catch (Exception e){
            e.printStackTrace();
            resultMap = null;
        }
        return resultMap;
    }

    /**
     * 模糊查询企业消息组总数
     */
    public int searchMessageGroupCount(Map<String , Object> map){
        return groupMapper.searchMessageGroupCount(map);
    }

    /**
     * 模糊查询企业消息组用户总数
     */
    public int searchMessageUserGroupCount(Map<String , Object> map){
        return groupMapper.searchMessageUserGroupCount(map);
    }

    /**
     * 模糊查询企业消息组配置总数
     */
    public int searchMsgGroupConfigureCount(Map<String , Object> map){
        return groupMapper.searchMsgGroupConfigureCount(map);
    }

    /**
     * 添加企业消息组
     */
    public boolean saveMessageGroup(Map<String , Object> map){
        boolean resuleBoolean;
        try{
            // 设置修改时间 修改人
            map.put("ouid",map.get("ouid"));
            map.put("addtime", System.currentTimeMillis());
            map.put("status",1);
            map.put("msgtype",JzbDataType.getInteger(map.get("msgtype")));
            map.put("updtime", System.currentTimeMillis());
            resuleBoolean = groupMapper.insertMessageGroup(map) > 0 ? true :false ;
        }catch (Exception e){
            e.printStackTrace();
            resuleBoolean = false;
        }
        return resuleBoolean;
    }

    /**
     * 添加企业消息组用户
     */
    public boolean saveMessageUserGroup(Map<String , Object> map){
        boolean resuleBoolean;
        try{
            // 设置修改时间 修改人
            map.put("ouid",map.get("ouid"));
            map.put("uid",map.get("uid"));
            map.put("status",1);
            map.put("updtime",System.currentTimeMillis());
            map.put("msgtype",MessageUtile.encryptionMsgType(map.get("msgtype").toString()));
            resuleBoolean = groupMapper.insertMessageUserGroup(map) > 0 ? true : false ;
        }catch (Exception e){
            e.printStackTrace();
            resuleBoolean = false;
        }
        return resuleBoolean;
    }

    /**
     * 添加企业消息组配置
     */
    public boolean saveMsgGroupConfigure(Map<String , Object> map){
        boolean resuleBoolean;
        try{
            // 设置修改时间 修改人
            map.put("ouid",map.get("ouid"));
            map.put("updtime",System.currentTimeMillis());
            map.put("addtime",System.currentTimeMillis());
            // 解析
            map.put("msgtype",JzbDataType.getInteger(map.get("msgtype")));
            map.put("status",1);
            resuleBoolean = groupMapper.insertMsgGroupConfigure(map) > 0 ? true : false ;
        }catch (Exception e){
            e.printStackTrace();
            resuleBoolean = false;
        }
        return resuleBoolean;
    }

    /**
     * 修改企业消息组
     */
    public boolean upMessageGroup(Map<String , Object> map){
        boolean resuleBoolean;
        try{
            // 设置修改时间 修改人
            map.put("ouid",map.get("userid"));
            map.put("updtime",System.currentTimeMillis());
            // 解析
            map.put("msgtype", MessageUtile.encryptionMsgType(map.get("msgtype").toString()));
            map.put("groupid",JzbDataType.getInteger(map.get("groupid")));
            map.put("id",JzbDataType.getInteger(map.get("id")));
            resuleBoolean =  groupMapper.updateMessageGroup(map) > 0 ? true : false ;
        }catch (Exception e){
            e.printStackTrace();
            resuleBoolean = false;
        }
        return resuleBoolean;
    }

    /**
     * 修改企业消息组用户
     */
    public boolean upMessageUserGroup(Map<String , Object> map){
        boolean resuleBoolean;
        try{
            // 设置修改时间 修改人
            map.put("ouid",map.get("userid"));
            map.put("updtime",System.currentTimeMillis());
            // 解析
            map.put("msgtype", MessageUtile.encryptionMsgType(map.get("msgtype").toString()));
            map.put("groupid",JzbDataType.getInteger(map.get("groupid")));
            map.put("id",JzbDataType.getInteger(map.get("id")));
            resuleBoolean  = groupMapper.updateMessageUserGroup(map) > 0 ? true : false ;
        }catch (Exception e){
            e.printStackTrace();
            resuleBoolean = false;
        }
        return resuleBoolean;
    }

    /**
     * 修改企业消息组配置
     */
    public boolean upMsgGroupConfigure(Map<String , Object> map){
        boolean resuleBoolean;
        try{
            // 设置修改时间 修改人
            map.put("ouid",map.get("userid"));
            map.put("updtime",System.currentTimeMillis());
            // 解析
            map.put("msgtype", MessageUtile.encryptionMsgType(map.get("msgtype").toString()));
            map.put("groupid",JzbDataType.getInteger(map.get("groupid")));
            map.put("id",JzbDataType.getInteger(map.get("id")));
            resuleBoolean  = groupMapper.updateMsgGroupConfigure(map) > 0 ? true : false ;
        }catch (Exception e){
            e.printStackTrace();
            resuleBoolean = false;
        }
        return resuleBoolean;
    }

    /**
     * 禁用企业消息组
     */
    public boolean removeMessageGroup(Map<String , Object> map){
        boolean resuleBoolean;
        try{
            // 设置修改时间 修改人
            map.put("ouid",map.get("userid"));
            map.put("updtime",System.currentTimeMillis());
            map.put("id",JzbDataType.getInteger(map.get("id")));
            resuleBoolean  = groupMapper.deleteMessageGroup(map) > 0 ? true : false ;
        }catch (Exception e){
            e.printStackTrace();
            resuleBoolean = false;
        }
        return resuleBoolean;
    }

    /**
     * 禁用企业消息组用户
     */
    public boolean removeMessageUserGroup (Map<String , Object> map){
        boolean resuleBoolean = false;
        try{
            // 设置修改时间 修改人
            map.put("ouid",map.get("ouid"));
            map.put("id",JzbDataType.getInteger(map.get("id")));
            map.put("updtime",System.currentTimeMillis());
            resuleBoolean  = groupMapper.deleteMessageUserGroup(map) > 0 ? true : false ;
        }catch (Exception e){
            e.printStackTrace();
            resuleBoolean = false;
        }
        return resuleBoolean;
    }

    /**
     * 禁用企业消息组配置
     */
    public boolean removeMsgGroupConfigure(Map<String , Object> map){
        boolean resuleBoolean = false;
        try{
            // 设置修改时间 修改人
            map.put("ouid",map.get("ouid"));
            map.put("updtime",System.currentTimeMillis());
            map.put("id",JzbDataType.getInteger(map.get("id")));
            resuleBoolean  = groupMapper.deleteMsgGroupConfigure(map) > 0 ? true : false ;
        }catch (Exception e){
            e.printStackTrace();
            resuleBoolean = false;
        }
        return resuleBoolean;
    }

}
