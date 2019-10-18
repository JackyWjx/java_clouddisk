package com.jzb.message.service;

import com.jzb.base.data.JzbDataType;
import com.jzb.message.dao.SendUserMessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SendUserMessageService {

    @Autowired
    private SendUserMessageMapper sendUserMessageMapper;


    /**
     * 查询
     */
    public List<Map<String, Object>> querySendUserMessage(Map<String, Object> map) {
        int page = JzbDataType.getInteger(map.get("page")) == 0 ? 0 : JzbDataType.getInteger(map.get("page")) - 1;
        map.put("page", page * JzbDataType.getInteger(map.get("rows")));
        map.put("rows", JzbDataType.getInteger(map.get("rows")));
        return sendUserMessageMapper.querySendUserMessage(map);
    }

    /**
     * 查询总数
     */
    public int querySendUserMessageCount(Map<String, Object> map) {
        return sendUserMessageMapper.querySendUserMessageCount(map);
    }

    /**
     * 模糊查询
     */
    public List<Map<String, Object>> searchSendUserMessage(Map<String, Object> map) {
        int page = JzbDataType.getInteger(map.get("page")) == 0 ? 0 : JzbDataType.getInteger(map.get("page")) - 1;
        map.put("page", page * JzbDataType.getInteger(map.get("rows")));
        map.put("rows", JzbDataType.getInteger(map.get("rows")));
        return sendUserMessageMapper.searchSendUserMessage(map);
    }

    /**
     * 模糊查询总数
     */
    public int searchSendUserMessageCount(Map<String, Object> map) {
        return sendUserMessageMapper.searchSendUserMessageCount(map);
    }

    /**
     * 添加
     */
    public int saveSendUserMessage(Map<String, Object> map) {
        map.put("status", '1');
        map.put("msgtype", JzbDataType.getInteger(map.get("msgtype")));
        map.put("sendtime",System.currentTimeMillis());
        map.put("receivetime",System.currentTimeMillis());
        return sendUserMessageMapper.insertSendUserMessage(map);
    }

    /**
     * 修改
     */
    public int upSendUserMessage(Map<String, Object> map) {
        map.put("msgtype", JzbDataType.getInteger(map.get("msgtype")));
        map.put("sendtime",System.currentTimeMillis());
        map.put("receivetime",System.currentTimeMillis());
        map.put("id", JzbDataType.getInteger(map.get("id")));
        return sendUserMessageMapper.updateSendUserMessage(map);
    }

    /**
     * 禁用
     */
    public int removeSendUserMessage(Map<String, Object> map) {
        map.put("id", JzbDataType.getInteger(map.get("id")));
        return sendUserMessageMapper.deleteSendUserMessage(map);
    }

}
