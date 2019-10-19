package com.jzb.message.service;

import com.jzb.base.data.JzbDataType;
import com.jzb.message.dao.MessageGroupTemplateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MessageGroupTemplateService {
    @Autowired
    private MessageGroupTemplateMapper messageGroupTemplateMapper;

    /**
     * 查询
     */
    public List<Map<String, Object>> queryMsgGroupTemplate(Map<String, Object> map) {
        int page = JzbDataType.getInteger(map.get("pageno")) == 0 ? 0 : JzbDataType.getInteger(map.get("pageno")) - 1;
        map.put("pageno", page * JzbDataType.getInteger(map.get("pagesize")));
        map.put("pagesize", JzbDataType.getInteger(map.get("pagesize")));
        return messageGroupTemplateMapper.queryMsgGroupTemplate(map);
    }

    /**
     * 查询总数
     */
    public int queryMsgGroupTemplateCount(Map<String, Object> map) {
        return messageGroupTemplateMapper.queryMsgGroupTemplateCount(map);
    }

    /**
     * 模糊查询
     */
    public List<Map<String, Object>> searchMsgGroupTemplate(Map<String, Object> map) {
        int page = JzbDataType.getInteger(map.get("pageno")) == 0 ? 0 : JzbDataType.getInteger(map.get("pageno")) - 1;
        map.put("pageno", page * JzbDataType.getInteger(map.get("pagesize")));
        map.put("pagesize", JzbDataType.getInteger(map.get("pagesize")));
        return messageGroupTemplateMapper.searchMsgGroupTemplate(map);
    }

    /**
     * 模糊查询总数
     */
    public int searchMsgGroupTemplateCount(Map<String, Object> map) {
        return messageGroupTemplateMapper.searchMsgGroupTemplateCount(map);
    }

    /**
     * 添加
     */
    public int saveMsgGroupTemplate(Map<String, Object> map) {
        map.put("status", '1');
        map.put("addtime",System.currentTimeMillis());
        if(map.containsKey("ouid")){
            map.put("updtime",map.get("ouid"));
            map.put("addtime",map.get("ouid"));
        }
        map.put("addtime",System.currentTimeMillis());
        return messageGroupTemplateMapper.insertMsgGroupTemplate(map);
    }

    /**
     * 修改
     */
    public int upMsgGroupTemplate(Map<String, Object> map) {
        if(map.containsKey("ouid")){
            map.put("upduid",map.get("ouid"));
        }
        map.put("updtime",System.currentTimeMillis());
        return messageGroupTemplateMapper.updateMsgGroupTemplate(map);
    }

    /**
     * 禁用
     */
    public int removeMsgGroupTemplate(Map<String, Object> map) {
        if(map.containsKey("ouid")){
            map.put("updtime",map.get("ouid"));
        }
        map.put("addtime",System.currentTimeMillis());
        return messageGroupTemplateMapper.removeMsgGroupTemplate(map);
    }

}
