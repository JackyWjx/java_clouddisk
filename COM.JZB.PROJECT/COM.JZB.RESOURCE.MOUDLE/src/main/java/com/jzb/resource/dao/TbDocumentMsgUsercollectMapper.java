package com.jzb.resource.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TbDocumentMsgUsercollectMapper {

    public Integer addCollectionHistory(Map<String, Object> param);

    public Integer delCollectionHistory(Map<String, Object> param);

    public List<Map<String,Object>> getCollectionHistory(Map<String, Object> param);

    Integer getCollectFileMsgCount(Map<String, Object> param);
}
