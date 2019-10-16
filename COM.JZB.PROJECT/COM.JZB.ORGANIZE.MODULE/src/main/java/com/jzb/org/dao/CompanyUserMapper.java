package com.jzb.org.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 数据库处理对象
 */
@Mapper
@Repository
public interface CompanyUserMapper {
    /**
     * 我的单位基本资料CRM-单位用户-所有单位-单位列表
     * 获取单位总数
     *
     * @author kuangbin
     */
    int queryCompanyListCount(Map<String, Object> param);

    /**
     * 我的单位基本资料CRM-单位用户-所有单位-单位列表
     * 查询所有的企业列表
     *
     * @author kuangbin
     */
    List<Map<String, Object>> queryCompanyList(Map<String, Object> param);

    /**
     * 我的单位基本资料CRM-单位用户-所有单位-单位列表
     * 查询所有的企业列表中的是否app已授权或电脑端已授权
     *
     * @author kuangbin
     */
    List<Map<String, Object>> queryAppType(Map<String, Object> param);

    /**
     * 我的单位基本资料CRM-单位用户-所有单位-单位列表
     * 点击调入公海是加入公海单位表
     *
     * @author kuangbin
     */
    int insertCompanyCommon(Map<String, Object> param);

    /**
     * CRM-销售业主-公海-业主1
     * 点击公海显示所有的单位信息的总数
     *
     * @author kuangbin
     */
    int queryCommonListCount(Map<String, Object> param);

    /**
     * CRM-销售业主-公海-业主1
     * 点击公海显示所有的单位信息
     *
     * @author kuangbin
     */
    List<Map<String, Object>> queryCompanyCommonList(Map<String, Object> param);

    /**
     * CRM-销售业主-公海-业主4
     * 根据单位ID显示对应的供应商或全部供应商的总数
     *
     * @author kuangbin
     */
    int queryCompanySupplierCount(Map<String, Object> param);

    /**
     * CRM-销售业主-公海-业主4
     * 根据单位ID显示对应的供应商或全部供应商
     *
     * @author kuangbin
     */
    List<Map<String, Object>> queryCompanySupplierList(Map<String, Object> param);
} // End interface CompanyMapper
