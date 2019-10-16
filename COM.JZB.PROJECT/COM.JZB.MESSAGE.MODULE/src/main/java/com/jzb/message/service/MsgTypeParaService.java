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
        int page  = JzbDataType.getInteger(map.get("page"))  == 0  ? 0 : JzbDataType.getInteger(map.get("page"))- 1;
        map.put("page",page * JzbDataType.getInteger(map.get("rows")));
        map.put("rows",JzbDataType.getInteger(map.get("rows")));
        return  msgTypePara.queryMsgTypePara(map);
    }

    /**
     * 查询用户参数
     */
    public List<Map<String , Object>> queryUserPara(Map<String , Object> map){
        int page  = JzbDataType.getInteger(map.get("page"))  == 0  ? 0 : JzbDataType.getInteger(map.get("page"))- 1;
        map.put("page",page * JzbDataType.getInteger(map.get("rows")));
        map.put("rows",JzbDataType.getInteger(map.get("rows")));
        return  msgTypePara.queryUserPara(map);
    }

    /**
     * 查询服务商
     */
    public List<Map<String , Object>> queryServiceProviders(Map<String , Object> map){
        int page  = JzbDataType.getInteger(map.get("page"))  == 0  ? 0 : JzbDataType.getInteger(map.get("page"))- 1;
        map.put("page",page * JzbDataType.getInteger(map.get("rows")));
        map.put("rows",JzbDataType.getInteger(map.get("rows")));
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
        int page  = JzbDataType.getInteger(map.get("page"))  == 0  ? 0 : JzbDataType.getInteger(map.get("page"))- 1;
        map.put("page",page * JzbDataType.getInteger(map.get("rows")));
        map.put("rows",JzbDataType.getInteger(map.get("rows")));
        return  msgTypePara.searchMsgTypePara(map);
    }

    /**
     * 模糊查询用户参数
     */
    public List<Map<String , Object>>  searchUserPara(Map<String , Object> map){
        int page  = JzbDataType.getInteger(map.get("page"))  == 0  ? 0 : JzbDataType.getInteger(map.get("page"))- 1;
        map.put("page",page * JzbDataType.getInteger(map.get("rows")));
        map.put("rows",JzbDataType.getInteger(map.get("rows")));
        return  msgTypePara.searchUserPara(map);
    }

    /**
     * 模糊查询服务商
     */
    public List<Map<String , Object>>  searchServiceProviders(Map<String , Object> map){
        int page  = JzbDataType.getInteger(map.get("page"))  == 0  ? 0 : JzbDataType.getInteger(map.get("page"))- 1;
        map.put("page",page * JzbDataType.getInteger(map.get("rows")));
        map.put("rows",JzbDataType.getInteger(map.get("rows")));
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
        map.put("msgtype",JzbDataType.getInteger(map.get("msgtype")));
        map.put("paratype",JzbDataType.getInteger(map.get("paratype")));
        map.put("parasize",JzbDataType.getInteger(map.get("parasize")));
        return  msgTypePara.insertMsgTypePara(map);
    }

    /**
     * 添加用户参数
     */
    public int saveUserPara(Map<String , Object> map){
        map.put("status",'1');
        map.put("addtime",System.currentTimeMillis());
        map.put("updtime",System.currentTimeMillis());
        map.put("ouid",map.get("uid"));
        map.put("adduid",map.get("uid"));
        return  msgTypePara.insertUserPara(map);
    }

    /**
     * 添加服务商
     */
    public int saveServiceProviders(Map<String , Object> map){
        map.put("status",'1');
        map.put("msgtype",JzbDataType.getInteger(map.get("msgtype")));
        map.put("addtime",System.currentTimeMillis());
        map.put("updtime",System.currentTimeMillis());
        map.put("ouid",map.get("uid"));
        map.put("adduid",map.get("uid"));
        return  msgTypePara.insertServiceProviders(map);
    }

    /**
     * 修改消息参数
     */
    public int upMsgTypePara(Map<String , Object> map){
        map.put("msgtype",JzbDataType.getInteger(map.get("msgtype")));
        map.put("paratype",JzbDataType.getInteger(map.get("paratype")));
        map.put("parasize",JzbDataType.getInteger(map.get("parasize")));
        map.put("id",JzbDataType.getInteger(map.get("id")));
        return  msgTypePara.updateMsgTypePara(map);
    }

    /**
     * 修改用户参数
     */
    public int updateUserPara(Map<String , Object> map){
        map.put("id",JzbDataType.getInteger(map.get("id")));
        map.put("updtime",System.currentTimeMillis());
        map.put("ouid",map.get("uid"));
        return  msgTypePara.updateUserPara(map);
    }

    /**
     * 修改服务商
     */
    public int updateServiceProviders(Map<String , Object> map){
        map.put("msgtype",JzbDataType.getInteger(map.get("msgtype")));
        map.put("paratype",JzbDataType.getInteger(map.get("isp")));
        map.put("updtime",System.currentTimeMillis());
        map.put("ouid",map.get("uid"));
        map.put("id",JzbDataType.getInteger(map.get("id")));
        return  msgTypePara.updateServiceProviders(map);
    }

    /**
     * 禁用消息参数
     */
    public int removeMsgTypePara(Map<String , Object> map){
        map.put("id",JzbDataType.getInteger(map.get("id")));
        return  msgTypePara.deleteMsgTypePara(map);
    }

    /**
     * 禁用用户参数
     */
    public int removeUserPara(Map<String , Object> map){
        map.put("id",JzbDataType.getInteger(map.get("id")));
        return  msgTypePara.deleteUserPara(map);
    }

    /**
     * 禁用服务商
     */
    public int removeServiceProviders(Map<String , Object> map){
        map.put("id",JzbDataType.getInteger(map.get("id")));
        return  msgTypePara.deleteServiceProviders(map);
    }

}
