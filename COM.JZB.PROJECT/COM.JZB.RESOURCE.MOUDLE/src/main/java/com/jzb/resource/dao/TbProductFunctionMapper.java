package com.jzb.resource.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface TbProductFunctionMapper {

    /**
     * 查询产品功能表对应的资源产品
     * @param param
     * @return
     */
    List<Map<String, Object>> getTbProductFunction(Map<String, Object> param);


    /**
     * 添加产品功能
     * @param paramList
     * @return
     */
    int saveTbProductFunction(List<Map<String, Object>> paramList);

    /**
     * 添加产品功能
     * @param paramList
     * @return
     */
    int updateTbProductFunction(List<Map<String, Object>> paramList);

    /**
     * 点击修改时查询产品功能表中的数据
     * @param param
     * @return
     */
    List<Map<String, Object>> getProductFunction(Map<String, Object> param);

    /**
     * 查询分页的总数
     * @param param
     * @return
     */
    int getCount(Map<String, Object> param);
}
