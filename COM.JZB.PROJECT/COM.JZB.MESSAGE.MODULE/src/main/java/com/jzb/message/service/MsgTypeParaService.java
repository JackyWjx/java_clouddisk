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
     * 查询
     */
    public List<Map<String , Object>> queryMsgTypePara(Map<String , Object> map){
        int page  = JzbDataType.getInteger(map.get("page"))  == 0  ? 0 : JzbDataType.getInteger(map.get("page"))- 1;
        map.put("page",page * JzbDataType.getInteger(map.get("rows")));
        map.put("rows",JzbDataType.getInteger(map.get("rows")));
        return  msgTypePara.queryMsgTypePara(map);
    }

    /**
     * 查询总数
     */
    public int  queryMsgTypeParaCount(Map<String , Object> map){
        return  msgTypePara.queryMsgTypeParaCount(map);
    }

    /**
     * 模糊查询
     */
    public List<Map<String , Object>>  searchMsgTypePara(Map<String , Object> map){
        int page  = JzbDataType.getInteger(map.get("page"))  == 0  ? 0 : JzbDataType.getInteger(map.get("page"))- 1;
        map.put("page",page * JzbDataType.getInteger(map.get("rows")));
        map.put("rows",JzbDataType.getInteger(map.get("rows")));
        return  msgTypePara.searchMsgTypePara(map);
    }

    /**
     * 模糊查询总数
     */
    public int  searchMsgTypeParaCount(Map<String , Object> map){
        return  msgTypePara.searchMsgTypeParaCount(map);
    }

    /**
     * 添加
     */
    public int saveMsgTypePara(Map<String , Object> map){
        map.put("status",'1');
        map.put("msgtype",JzbDataType.getInteger(map.get("msgtype")));
        map.put("paratype",JzbDataType.getInteger(map.get("paratype")));
        map.put("parasize",JzbDataType.getInteger(map.get("parasize")));
        return  msgTypePara.insertMsgTypePara(map);
    }

    /**
     * 修改
     */
    public int upMsgTypePara(Map<String , Object> map){
        map.put("msgtype",JzbDataType.getInteger(map.get("msgtype")));
        map.put("paratype",JzbDataType.getInteger(map.get("paratype")));
        map.put("parasize",JzbDataType.getInteger(map.get("parasize")));
        map.put("id",JzbDataType.getInteger(map.get("id")));
        return  msgTypePara.updateMsgTypePara(map);
    }

    /**
     * 禁用
     */
    public int removeMsgTypePara(Map<String , Object> map){
        map.put("id",JzbDataType.getInteger(map.get("id")));
        return  msgTypePara.deleteMsgTypePara(map);
    }

}
