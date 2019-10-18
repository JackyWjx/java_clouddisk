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
    public List<Map<String, Object>> queryMsgList(Map<String, Object> map) {
        int page = JzbDataType.getInteger(map.get("pageno")) == 0 ? 0 : JzbDataType.getInteger(map.get("pageno")) - 1;
        map.put("pageno", page * JzbDataType.getInteger(map.get("pagesize")));
        map.put("pagesize", JzbDataType.getInteger(map.get("pagesize")));
        return messageListMapper.queryMsgList(map);
    }

    /**
     * 查询总数
     */
    public int queryMsgListCount(Map<String, Object> map) {
        return messageListMapper.queryMsgListCount(map);
    }

    /**
     * 模糊查询
     */
    public List<Map<String, Object>> searchMsgList(Map<String, Object> map) {
        int page = JzbDataType.getInteger(map.get("pageno")) == 0 ? 0 : JzbDataType.getInteger(map.get("pageno")) - 1;
        map.put("pageno", page * JzbDataType.getInteger(map.get("pagesize")));
        map.put("pagesize", JzbDataType.getInteger(map.get("pagesize")));
        return messageListMapper.searchMsgList(map);
    }

    /**
     * 模糊查询总数
     */
    public int searchMsgListCount(Map<String, Object> map) {
        return messageListMapper.searchMsgListCount(map);
    }

}
