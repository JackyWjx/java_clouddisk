package com.jzb.resource.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface TbProductResListMapper {

    /**
     * 查询合同配置中的产品参数
     * 根据产品线的id查询产品表
     *      * 根据产品id查询出产品参数
     * @param param
     * @return
     */
    List<Map<String, Object>> getProductResList(Map<String, Object> param);

    /**
     * 添加新建产品参数
     * @param paramList
     * @return
     */
    int saveTbProductParameteItem(List<Map<String, Object>> paramList);


    /**
     * 修改合同配置中的产品参数列表
     * @param paramList
     * @return
     */
    int updateTbProductParameteItem(List<Map<String, Object>> paramList);


    /**
     * 添加资源产品表的数据
     *
     * @param param
     * @return
     */
    int saveTbProductResList(Map<String, Object> param);

    /**
     * 点击修改的时候查询产品参数
     * @param param
     * @return
     */
    List<Map<String, Object>> getTbProductParameteItem(Map<String, Object> param);



    /**
     * 产品参数修改时的查询
     * @param param
     * @return
     */
    List<Map<String, Object>> getProductItem(Map<String, Object> param);
}