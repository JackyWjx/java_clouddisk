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
     * @param param
     * @return
     */
    int saveTbProductFunction(Map<String, Object> param);

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

    /**
     * 根据id查询这个功能存不存在
     *
     * @param param
     * @return
     */
    List<Map<String, Object>> getProductFunctions(Map<String, Object> param);

    /**
     * 根据id进行功能数据的删除
     * @param param
     * @return
     */
    int updateProductFunctions(Map<String, Object> param);

    /**
     * 根据id进行功能数据的删除
     * @param list
     * @return
     */
    int deleteFunction(List<String> list);
}
