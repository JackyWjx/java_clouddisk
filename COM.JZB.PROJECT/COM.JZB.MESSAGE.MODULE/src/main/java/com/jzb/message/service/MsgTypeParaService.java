package com.jzb.message.service;

import com.jzb.base.data.JzbDataType;
import com.jzb.message.dao.MsgTypeParaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description: 消息参数
 * @Author Han Bin
 */
@Service
public class MsgTypeParaService {

    @Autowired
    MsgTypeParaMapper msgTypePara;

    /**
     * 查询消息参数
     */
    public List<Map<String , Object>> queryMsgTypePara(Map<String , Object> map){
        int page = JzbDataType.getInteger(map.get("pageno")) == 0 ? 0 : JzbDataType.getInteger(map.get("pageno")) - 1;
        map.put("pageno", page * JzbDataType.getInteger(map.get("pagesize")));
        map.put("pagesize", JzbDataType.getInteger(map.get("pagesize")));
        return  msgTypePara.queryMsgTypePara(map);
    }

    /**
     * 查询用户参数
     */
    public List<Map<String , Object>> queryUserPara(Map<String , Object> map){
        int page = JzbDataType.getInteger(map.get("pageno")) == 0 ? 0 : JzbDataType.getInteger(map.get("pageno")) - 1;
        map.put("pageno", page * JzbDataType.getInteger(map.get("pagesize")));
        map.put("pagesize", JzbDataType.getInteger(map.get("pagesize")));
        return  msgTypePara.queryUserPara(map);
    }

    /**
     * 查询服务商
     */
    public List<Map<String , Object>> queryServiceProviders(Map<String , Object> map){
        int page = JzbDataType.getInteger(map.get("pageno")) == 0 ? 0 : JzbDataType.getInteger(map.get("pageno")) - 1;
        map.put("pageno", page * JzbDataType.getInteger(map.get("pagesize")));
        map.put("pagesize", JzbDataType.getInteger(map.get("pagesize")));
        return  msgTypePara.queryServiceProviders(map);
    }

    /**
     * 查询消息参数总数
     */
    public int  queryMsgTypeParaCount(Map<String , Object> map){
        return  msgTypePara.queryMsgTypeParaCount(map);
    }

    /**
     * 查询用户参数总数
     */
    public int  queryUserParaCount(Map<String , Object> map){
        return  msgTypePara.queryUserParaCount(map);
    }

    /**
     * 查询服务商总数
     */
    public int  queryServiceProvidersCount(Map<String , Object> map){
        return  msgTypePara.queryServiceProvidersCount(map);
    }

    /**
     * 模糊查询消息参数
     */
    public List<Map<String , Object>>  searchMsgTypePara(Map<String , Object> map){
        int page = JzbDataType.getInteger(map.get("pageno")) == 0 ? 0 : JzbDataType.getInteger(map.get("pageno")) - 1;
        map.put("pageno", page * JzbDataType.getInteger(map.get("pagesize")));
        map.put("pagesize", JzbDataType.getInteger(map.get("pagesize")));
        return  msgTypePara.searchMsgTypePara(map);
    }

    /**
     * 模糊查询用户参数
     */
    public List<Map<String , Object>>  searchUserPara(Map<String , Object> map){
        int page = JzbDataType.getInteger(map.get("pageno")) == 0 ? 0 : JzbDataType.getInteger(map.get("pageno")) - 1;
        map.put("pageno", page * JzbDataType.getInteger(map.get("pagesize")));
        map.put("pagesize", JzbDataType.getInteger(map.get("pagesize")));
        return  msgTypePara.searchUserPara(map);
    }

    /**
     * 模糊查询服务商
     */
    public List<Map<String , Object>>  searchServiceProviders(Map<String , Object> map){
        int page = JzbDataType.getInteger(map.get("pageno")) == 0 ? 0 : JzbDataType.getInteger(map.get("pageno")) - 1;
        map.put("pageno", page * JzbDataType.getInteger(map.get("pagesize")));
        map.put("pagesize", JzbDataType.getInteger(map.get("pagesize")));
        return  msgTypePara.searchServiceProviders(map);
    }

    /**
     * 模糊查询消息参数总数
     */
    public int  searchMsgTypeParaCount(Map<String , Object> map){
        return  msgTypePara.searchMsgTypeParaCount(map);
    }

    /**
     * 模糊查询用户参数总数
     */
    public int  searchUserParaCount(Map<String , Object> map){
        return  msgTypePara.searchUserParaCount(map);
    }

    /**
     * 模糊查询服务商总数
     */
    public int  searchServiceProvidersCount(Map<String , Object> map){
        return  msgTypePara.searchServiceProvidersCount(map);
    }


    /**
     * 添加消息参数
     */
    public int saveMsgTypePara(Map<String , Object> map){
        map.put("status",'1');
        if(map.containsKey("ouid")){
            map.put("adduid",map.get("ouid"));
            map.put("upduid",map.get("ouid"));
        }
        map.put("updtime",System.currentTimeMillis());
        map.put("addtime",System.currentTimeMillis());
        return  msgTypePara.insertMsgTypePara(map);
    }

    /**
     * 添加用户参数
     */
    public int saveUserPara(Map<String , Object> map){
        map.put("status",'1');
        map.put("addtime",System.currentTimeMillis());
        map.put("updtime",System.currentTimeMillis());
        if(map.containsKey("ouid")){
            map.put("upduid",map.get("ouid"));
            map.put("adduid",map.get("ouid"));
        }
        return  msgTypePara.insertUserPara(map);
    }

    /**
     * 添加服务商
     */
    public int saveServiceProviders(Map<String , Object> map){
        map.put("status",'1');
        map.put("addtime",System.currentTimeMillis());
        map.put("updtime",System.currentTimeMillis());
        if(map.containsKey("ouid")){
            map.put("upduid",map.get("ouid"));
            map.put("adduid",map.get("ouid"));
        }
        return  msgTypePara.insertServiceProviders(map);
    }

    /**
     * 修改消息参数
     */
    public int upMsgTypePara(Map<String , Object> map){
        if(map.containsKey("ouid")){
            map.put("upduid",map.get("ouid"));
        }
        map.put("updtime",System.currentTimeMillis());
        return  msgTypePara.updateMsgTypePara(map);
    }

    /**
     * 修改用户参数
     */
    public int updateUserPara(Map<String , Object> map){
        map.put("updtime",System.currentTimeMillis());
        if(map.containsKey("ouid")){
            map.put("updtime",map.get("ouid"));
        }
        return  msgTypePara.updateUserPara(map);
    }

    /**
     * 修改服务商
     */
    public int updateServiceProviders(Map<String , Object> map){
        map.put("updtime",System.currentTimeMillis());
        if (map.containsKey("ouid")) {
            map.put("upduid",map.get("ouid"));
        }
        return  msgTypePara.updateServiceProviders(map);
    }

    /**
     * 禁用消息参数
     */
    public int removeMsgTypePara(Map<String , Object> map){
        if(map.containsKey("ouid")){
            map.put("upduid",map.get("ouid"));
        }
        map.put("updtime",System.currentTimeMillis());
        return  msgTypePara.deleteMsgTypePara(map);
    }

    /**
     * 禁用用户参数
     */
    public int removeUserPara(Map<String , Object> map){
        if(map.containsKey("ouid")){
            map.put("upduid",map.get("ouid"));
        }
        map.put("updtime",System.currentTimeMillis());
        return  msgTypePara.deleteUserPara(map);
    }

    /**
     * 禁用服务商
     */
    public int removeServiceProviders(Map<String , Object> map){
        if(map.containsKey("ouid")){
            map.put("upduid",map.get("ouid"));
        }
        map.put("updtime",System.currentTimeMillis());
        return  msgTypePara.deleteServiceProviders(map);
    }

}
