package com.jzb.resource.dao;

import com.jzb.base.entity.uploader.Chunk;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface TbDocumentMsgUploadLogMapper {
    int insertBreakPointLog (Chunk chunk);

    Chunk selectBreakPointLog (Map<String,Object> map);

    int updateBreakPointLog(Chunk chunk);
}
