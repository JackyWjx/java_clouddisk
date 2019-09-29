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
     *
     * @param param
     * @return
     */
    default int addProductResList(Map<String, Object> param) {
        return 0;
    }

    /**
     * 查询合同配置中的产品参数
     * 根据产品线的id查询产品表
     *      * 根据产品id查询出产品参数
     * @param param
     * @return
     */
    List<Map<String, Object>> getProductResList(Map<String, Object> param);

    /**
     * 根据产品名称模糊查询产品参数列表
     * @param param
     * @return
     */
    List<Map<String, Object>> getProductResListCname(Map<String, Object> param);

    /**
     * 合同配置中产品参数中新建中查询出产品名称
     * @param param
     * @return
     */
    List<Map<String, Object>> queryProductListCname(Map<String, Object> param);

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
     * 修改产品表中的数据
     * @param param
     * @return
     */
    int updateTbProductResList(Map<String, Object> param);

    /**
     * 点击修改的时候查询产品参数
     * @param param
     * @return
     */
    List<Map<String, Object>> getTbProductParameteItem(Map<String, Object> param);
}