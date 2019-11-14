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
        int page = JzbDataType.getInteger(map.get("pageno")) == 0 ? 0 : JzbDataType.getInteger(map.get("pageno")) - 1;
        map.put("pageno", page * JzbDataType.getInteger(map.get("pagesize")));
        map.put("pagesize", JzbDataType.getInteger(map.get("pagesize")));
        return sendUserMessageMapper.querySendUserMessage(map);
    }

    /**
     * 查询总数
     */
    public int querySendUserMessageCount(Map<String, Object> map) {
        return sendUserMessageMapper.querySendUserMessageCount(map);
    }

    public int updateSendCount(Map<String, Object> map){
        return sendUserMessageMapper.updateSendCount(map);
    }

    public int deleteSend(Map<String, Object> map){
        return sendUserMessageMapper.deleteSend(map);
    }

    /**
     * 查询总数
     */
    public int querySendCount(Map<String, Object> map){
        return sendUserMessageMapper.querySendCount(map);
    }

    /**
     * 查询
     */
    public List<Map<String, Object>> queryUserMessage(Map<String, Object> map) {
        int page = JzbDataType.getInteger(map.get("pageno")) == 0 ? 0 : JzbDataType.getInteger(map.get("pageno")) - 1;
        map.put("pageno", page * JzbDataType.getInteger(map.get("pagesize")));
        map.put("pagesize", JzbDataType.getInteger(map.get("pagesize")));
        return sendUserMessageMapper.queryUserMessage(map);
    }

    /**
     * 查询总数
     */
    public int queryUserMessageCount(Map<String, Object> map) {
        return sendUserMessageMapper.queryUserMessageCount(map);
    }

    /**
     * 模糊查询
     */
    public List<Map<String, Object>> searchSendUserMessage(Map<String, Object> map) {
        int page = JzbDataType.getInteger(map.get("pageno")) == 0 ? 0 : JzbDataType.getInteger(map.get("pageno")) - 1;
        map.put("pageno", page * JzbDataType.getInteger(map.get("pagesize")));
        map.put("pagesize", JzbDataType.getInteger(map.get("pagesize")));
        return sendUserMessageMapper.searchSendUserMessage(map);
    }

    /**
     * 模糊查询总数
     */
    public int searchSendUserMessageCount(Map<String, Object> map) {
        return sendUserMessageMapper.searchSendUserMessageCount(map);
    }

}
