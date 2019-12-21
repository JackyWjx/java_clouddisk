package com.jzb.resource.service;

import com.jzb.base.message.Response;
import com.jzb.resource.dao.TbDocumentMsgUsercollectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class TbDocumentMsgUsercollectService {

    @Resource
    private TbDocumentMsgUsercollectMapper tbDocumentMsgUsercollectMapper;

    public List<Map<String, Object>> getCollectFileMsg(Map<String, Object> param) {
        return tbDocumentMsgUsercollectMapper.getCollectionHistory(param);
    }


    public Integer getCollectFileMsgCount(Map<String, Object> param) {
        return tbDocumentMsgUsercollectMapper.getCollectFileMsgCount(param);
    }

    public Integer addCollectFileMsg(Map<String, Object> param) {
        return tbDocumentMsgUsercollectMapper.addCollectionHistory(param);
    }

    public Integer delCollectFileMsg(Map<String, Object> param) {
        return tbDocumentMsgUsercollectMapper.delCollectionHistory(param);
    }
}
