package com.jzb.resource.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface TbProductSystemMenuMapper {

    // 查询
    List<Map<String, Object>> queryMenuList(Map<String, Object> param);

    // 添加
    int addMenuList(Map<String, Object> param);

    // 修改
    int updateMenuList(Map<String, Object> param);
}
