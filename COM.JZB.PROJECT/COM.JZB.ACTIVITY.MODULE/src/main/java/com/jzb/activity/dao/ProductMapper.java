package com.jzb.activity.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Description: 产品Map层
 * @Author duanfeiyu
 * @Version v1.0
 * @Since 1.0
 * @Date: 2019/8/13 9:45
 */
@Mapper
public interface ProductMapper {
    /**
     * 查询产品的总数
     */
    int selectProductCount();

    /**
     * 查询产品总数
     * @param param
     * @return
     */
    int queryProductTotal(Map<String, Object> param);

    /**
     * 查询产品列表
     * @param param
     * @return
     */
    List<Map<String, Object>> queryProductList(Map<String, Object> param);

    /**
     * 查询产品信息
     * @return   List<Map<String, Object>> 返回数组
     */
    List<Map<String, Object>> selectProductList(Map<String, Object> param);


    /**
     * 产品包信息总数
     * @return int 返回多少条数据
     */
    int selectProductPackageCount();

    /**
     * 分页查询 产品包信息
     * @return   List<Map<String, Object>> 返回数组
     * @param param
     */
    List<Map<String, Object>> selectProductPackageList();

    /**
     * 产品模糊查询
     * @param param
     * @return
     */
    List<Map<String, Object>> likeProductList(Map<String, Object> param);

    /**
     * 产品包模糊查询
     * @param param
     * @return
     */
    List<Map<String, Object>> likeProductPackageList();

    /**
     * <!--    产品总数模糊查询-->
     * @return
     */
    int likeProductCount();

    /**
     * 产品包总数模糊查询
     * @return
     */
    int likeProductPackageCount();
}
