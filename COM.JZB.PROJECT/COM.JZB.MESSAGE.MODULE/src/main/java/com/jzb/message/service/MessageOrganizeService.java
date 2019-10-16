package com.jzb.message.service;

import com.jzb.base.data.JzbDataType;
import com.jzb.message.dao.MessageOrganizeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MessageOrganizeService {
    @Autowired
    private MessageOrganizeMapper messageOrganizeMapper;

    /**
     * 查询
     */
    public List<Map<String, Object>> queryMsgOrganize(Map<String, Object> map) {
        int page = JzbDataType.getInteger(map.get("page")) == 0 ? 0 : JzbDataType.getInteger(map.get("page")) - 1;
        map.put("page", page * JzbDataType.getInteger(map.get("rows")));
        map.put("rows", JzbDataType.getInteger(map.get("rows")));
        return messageOrganizeMapper.queryMsgOrganize(map);
    }

    /**
     * 查询总数
     */
    public int queryMsgOrganizeCount(Map<String, Object> map) {
        return messageOrganizeMapper.queryMsgOrganizeCount(map);
    }

    /**
     * 模糊查询
     */
    public List<Map<String, Object>> searchMsgOrganize(Map<String, Object> map) {
        int page = JzbDataType.getInteger(map.get("page")) == 0 ? 0 : JzbDataType.getInteger(map.get("page")) - 1;
        map.put("page", page * JzbDataType.getInteger(map.get("rows")));
        map.put("rows", JzbDataType.getInteger(map.get("rows")));
        return messageOrganizeMapper.searchMsgOrganize(map);
    }

    /**
     * 模糊查询总数
     */
    public int searchMsgOrganizeCount(Map<String, Object> map) {
        return messageOrganizeMapper.searchMsgOrganizeCount(map);
    }

    /**
     * 添加
     */
    public int saveMsgOrganize(Map<String, Object> map) {
        map.put("status", '1');
        return messageOrganizeMapper.insertMsgOrganize(map);
    }

    /**
     * 修改
     */
    public int upMsgOrganize(Map<String, Object> map) {
        map.put("id", JzbDataType.getInteger(map.get("id")));
        return messageOrganizeMapper.updateMsgOrganize(map);
    }

    /**
     * 禁用
     */
    public int removeMsgOrganize(Map<String, Object> map) {
        map.put("id", JzbDataType.getInteger(map.get("id")));
        return messageOrganizeMapper.deleteMsgOrganize(map);
    }

}
