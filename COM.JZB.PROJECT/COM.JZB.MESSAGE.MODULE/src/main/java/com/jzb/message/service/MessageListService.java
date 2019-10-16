package com.jzb.message.service;

import com.jzb.base.data.JzbDataType;
import com.jzb.message.dao.MessageListMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MessageListService {

    @Autowired
    private MessageListMapper messageListMapper;

    /**
     * 查询
     */
    public List<Map<String, Object>> queryMsgType(Map<String, Object> map) {
        int page = JzbDataType.getInteger(map.get("page")) == 0 ? 0 : JzbDataType.getInteger(map.get("page")) - 1;
        map.put("page", page * JzbDataType.getInteger(map.get("rows")));
        map.put("rows", JzbDataType.getInteger(map.get("rows")));
        return messageListMapper.queryMsgList(map);
    }

    /**
     * 查询总数
     */
    public int queryMsgTypeCount(Map<String, Object> map) {
        return messageListMapper.queryMsgListCount(map);
    }

    /**
     * 模糊查询
     */
    public List<Map<String, Object>> searchMsgType(Map<String, Object> map) {
        int page = JzbDataType.getInteger(map.get("page")) == 0 ? 0 : JzbDataType.getInteger(map.get("page")) - 1;
        map.put("page", page * JzbDataType.getInteger(map.get("rows")));
        map.put("rows", JzbDataType.getInteger(map.get("rows")));
        return messageListMapper.searchMsgList(map);
    }

    /**
     * 模糊查询总数
     */
    public int searchMsgTypeCount(Map<String, Object> map) {
        return messageListMapper.searchMsgListCount(map);
    }

    /**
     * 添加
     */
    public int saveMsgType(Map<String, Object> map) {
        map.put("status", '1');
        map.put("msgtype", JzbDataType.getInteger(map.get("msgtype")));
        return messageListMapper.insertMsgList(map);
    }

    /**
     * 修改
     */
    public int upMsgType(Map<String, Object> map) {
        map.put("msgtype", JzbDataType.getInteger(map.get("msgtype")));
        map.put("id", JzbDataType.getInteger(map.get("id")));
        return messageListMapper.updateMsgList(map);
    }

    /**
     * 禁用
     */
    public int removeMsgType(Map<String, Object> map) {
        map.put("id", JzbDataType.getInteger(map.get("id")));
        return messageListMapper.deleteMsgList(map);
    }

}
