package com.jzb.org.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

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
@Repository
public interface ProductMapper {
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
     * 模板导入新建菜单或子级
     *
     * @author kuangbin
     */
    int insertProductMenu(List<Map<String, Object>> param);

    /**
     * 根据产品查询产品下的所有菜单
     *
     * @author kuangbin
     */
    List<Map<String, Object>> queryProductMenuList(Map<String, Object> param);

    /**
     * 根据查询出来的页面所有信息加入数据库
     *
     * @author kuangbin
     */
    int insertProductPage(List<Map<String, Object>> pageList);

    /**
     * 根据产品查询产品下的所有菜单
     *
     * @author kuangbin
     */
    List<Map<String, Object>> queryProductPageList(Map<String, Object> param);

    /**
     * 根据查询出来的控件所有信息加入页面控件表
     *
     * @author kuangbin
     */
    int insertPageControlList(List<Map<String, Object>> pageList);

    /**
     * 根据查询出来的控件所有信息加入控件API表
     *
     * @author kuangbin
     */
    int insertControlPowerList(List<Map<String, Object>> pageList);

    Map<String,Object> getPhoneByPhone(Map<String, Object> param);
}
