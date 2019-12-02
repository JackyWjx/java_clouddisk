package com.jzb.org.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 数据库处理对象
 */
@Mapper
@Repository
public interface ProductLineMapper {
    /**
     * 显示产品线列表
     *
     * @return
     */
    List<Map<String, Object>> queryProductLineList(Map<String, Object> param);

    /**
     * 获取产品线的总数
     *
     * @return
     */
    int queryProductLineCount(Map<String, Object> param);

    /**
     * 获取状态为2的产品线ID
     *
     * @return
     */
    List<Map<String, Object>> queryProductLineId(Map<String, Object> param);

    /**
     * 修改产品线信息
     *
     * @return
     */
    int updateProductLine(Map<String, Object> param);

    /**
     * CRM菜单管理-记支宝电脑端
     * 电脑端-全界面-记支宝电脑端下全界面显示企业下所有产品的菜单列表
     *
     * @author kuangbin
     */
    List<Map<String, Object>> queryCompanyMenuList(Map<String, Object> param);

    /**
     * CRM菜单管理-记支宝电脑端
     * 电脑端-全界面-记支宝电脑端下全界面显示企业下所有产品的顶级菜单下的页面
     *
     * @author kuangbin
     */
    List<Map<String, Object>> queryCompanyPageList(Map<String, Object> param);

    /**
     * 电脑端-全界面-记支宝电脑端下全界面显示菜单下的所有页面
     *
     * @author kuangbin
     */
    List<Map<String, Object>> queryProductPageList(Map<String, Object> param);

    /**
     * CRM菜单管理-记支宝电脑端
     * 记支宝电脑端下新建产品线
     *
     * @author kuangbin
     */
    int insertProductLine(Map<String, Object> param);

    /**
     * CRM菜单管理
     * 根据分页获取菜单信息
     *
     * @author kuangbin
     */
    List<Map<String, Object>> queryProductMenuList(Map<String, Object> param);

    /**
     * CRM菜单管理
     * CRM菜单新建同级菜单或子级--加入产品菜单表
     *
     * @author kuangbin
     */
    int insertProductMenu(Map<String, Object> param);

    /**
     * CRM菜单管理
     * CRM菜单新建同级菜单或子级--加入产品页面表
     *
     * @author kuangbin
     */
    int insertProductPage(Map<String, Object> param);

    /**
     * CRM菜单管理
     * 电脑端-全界面-记支宝电脑端下全界面修改页面或者删除页面
     *
     * @author kuangbin
     */
    int updateProductPage(Map<String, Object> param);

    /**
     * CRM菜单管理-记支宝电脑端
     * 电脑端-全界面-记支宝电脑端下全界面根据页面名称模糊搜索页面
     *
     * @author kuangbin
     */
    List<Map<String, Object>> searchProductPageList(Map<String, Object> param);

    /**
     * CRM菜单管理
     * CRM菜单新建同级菜单或子级--加入页面控件表
     *
     * @author kuangbin
     */
    int insertPageControl(Map<String, Object> param);

    /**
     * CRM菜单管理
     * CRM菜单新建同级菜单或子级--加入控件API表
     *
     * @author kuangbin
     */
    int insertControlPower(Map<String, Object> param);

    /**
     * CRM菜单管理
     * CRM菜单修改菜单信息
     *
     * @author kuangbin
     */
    int updateProductMenu(Map<String, Object> param);

    /**
     * CRM菜单管理
     * CRM菜单删除菜单信息
     *
     * @author kuangbin
     */
    int deleteProductMenu(Map<String, Object> param);

    /**
     * CRM菜单管理-记支宝电脑端-子产品
     * 电脑端-子产品-记支宝电脑端下产品线下显示所有产品
     *
     * @author kuangbin
     */
    List<Map<String, Object>> queryProductList(Map<String, Object> param);

    /**
     * CRM菜单管理-记支宝电脑端-子产品
     * 电脑端-子产品-产品线下新建企业产品,加入产品表
     *
     * @author kuangbin
     */
    int insertProduct(Map<String, Object> param);

    /**
     * CRM菜单管理-记支宝电脑端-子产品
     * 电脑端-子产品-产品线下新建企业产品,加入企业产品表
     *
     * @author kuangbin
     */
    int insertCompanyProduct(Map<String, Object> param);

    /**
     * CRM菜单管理-记支宝电脑端-子产品
     * 电脑端-子产品-产品线下修改企业产品更新产品表信息
     *
     * @author kuangbin
     */
    int updateProductList(Map<String, Object> param);

    /**
     * CRM菜单管理-记支宝电脑端-子产品
     * 电脑端-子产品-产品线下修改企业产品更新企业产品表信息
     *
     * @author kuangbin
     */
    int updateCompanyProduct(Map<String, Object> param);

    /**
     * CRM菜单管理-记支宝电脑端-子产品
     * 电脑端-子产品-点击从页面添加按钮,将页面加入当前菜单中
     *
     * @author kuang Bin
     */
    int insertExistingPage(List<Map<String, Object>> list);

    List<Map<String, Object>> queryProductPageLists(Map<String, Object> param);


} // End interface CompanyMapper
