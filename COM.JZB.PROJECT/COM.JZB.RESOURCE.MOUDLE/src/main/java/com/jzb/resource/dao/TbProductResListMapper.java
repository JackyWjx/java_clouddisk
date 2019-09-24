package com.jzb.resource.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface TbProductResListMapper {

    /**
     * 查询模板功能信息
     * @param param
     * @return
     */
    List<Map<String, Object>> queryProductResList(Map<String, Object> param);

    /**
     * 添加模板功能信息
     * @param param
     * @return
     */
    int addProductResList(Map<String, Object> param);

}