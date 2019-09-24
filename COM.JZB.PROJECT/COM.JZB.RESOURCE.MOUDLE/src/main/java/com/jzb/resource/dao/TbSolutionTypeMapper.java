package com.jzb.resource.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface TbSolutionTypeMapper {

    /**
     * 1.查询方案类型（父子级）
     */
    List<Map<String, Object>> querySolutionType(Map<String, Object> param);
}


