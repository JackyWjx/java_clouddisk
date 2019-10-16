package com.jzb.resource.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface TbProductSystemStructMapper {


    // 查询
    List<Map<String, Object>> queryStructList(Map<String, Object> param);

    // 添加
    int addStructList(Map<String, Object> param);

    // 修改
    int updateStructList(Map<String, Object> param);
}
