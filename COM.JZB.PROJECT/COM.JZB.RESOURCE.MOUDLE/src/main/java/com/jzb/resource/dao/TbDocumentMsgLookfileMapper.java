package com.jzb.resource.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TbDocumentMsgLookfileMapper {

    public Integer addLookHistory(Map<String,Object> param);

    public List<Map<String,Object>> getLookHistory(Map<String,Object> param);

    Integer getLookHistoryCount(Map<String, Object> param);
}
