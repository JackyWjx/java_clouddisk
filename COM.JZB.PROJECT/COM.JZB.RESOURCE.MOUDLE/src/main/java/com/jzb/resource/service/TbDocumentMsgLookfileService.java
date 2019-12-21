package com.jzb.resource.service;

import com.jzb.resource.dao.TbDocumentMsgLookfileMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TbDocumentMsgLookfileService {
    @Resource
    private TbDocumentMsgLookfileMapper tbDocumentMsgLookfileMapper;

    public List<Map<String,Object>> getLookHistory(Map<String,Object> param){
        return tbDocumentMsgLookfileMapper.getLookHistory(param);
    }

    public Integer addLookHistory(List<Map<String,Object>> list,String uid, Integer eventlog){
        Map<String,Object> param = new HashMap<>();
        param.put("uid",uid);
        param.put("eventlog",eventlog);
        param.put("addtime",System.currentTimeMillis());
        param.put("list",list);
        return tbDocumentMsgLookfileMapper.addLookHistory(param);
    }

    public Integer getLookHistoryCount(Map<String, Object> param) {
        return tbDocumentMsgLookfileMapper.getLookHistoryCount(param);
    }
}
