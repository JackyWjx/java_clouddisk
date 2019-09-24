package com.jzb.resource.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface TbProductFunctionMapper {

    /**
     * 添加产品功能
     * @param list
     * @return
     */
    int addProductFunction(List<Map<String, Object>> list);


    /**
     * 查询产品功能
     * @param map
     * @return
     */
    List<Map<String, Object>> queryProductFunction(Map<String, Object> map);

    /**
     * 修改产品功能
     * @param map
     * @return
     */
    int updateProductFunction(Map<String, Object> map);
}
