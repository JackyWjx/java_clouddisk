package com.jzb.resource.service;

import com.jzb.base.entity.uploader.Chunk;
import com.jzb.resource.dao.TbDocumentMsgUploadLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class TbDocumentMsgUploadLogService {
    @Resource
    private TbDocumentMsgUploadLogMapper tbDocumentMsgUploadLogMapper;

    public Chunk getBreakPointLog(Map<String,Object> param){
        return tbDocumentMsgUploadLogMapper.selectBreakPointLog(param);
    }

    public Integer saveChunk(Chunk chunk){
        Integer changeNum;
        Map<String,Object> param = new HashMap<>();
        param.put("identifier",chunk.getIdentifier());
        param.put("chunknumber",chunk.getChunkNumber());
        if(tbDocumentMsgUploadLogMapper.selectBreakPointLog(param)==null){
            changeNum = tbDocumentMsgUploadLogMapper.insertBreakPointLog(chunk);
        }else {
            changeNum = tbDocumentMsgUploadLogMapper.updateBreakPointLog(chunk);
        }
        return changeNum;
    }

}
