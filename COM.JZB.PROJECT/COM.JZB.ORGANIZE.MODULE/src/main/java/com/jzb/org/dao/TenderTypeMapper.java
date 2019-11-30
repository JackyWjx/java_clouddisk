package com.jzb.org.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TenderTypeMapper {
    List<Map<String, Object>> queryTenderType(Map<String, Object> param);

    void addTenderType(Map<String, Object> param);

    Integer selectMaxNum();

    Integer delTenderType(String typeId);

    Integer putTenderType(Map<String, Object> param);
}
