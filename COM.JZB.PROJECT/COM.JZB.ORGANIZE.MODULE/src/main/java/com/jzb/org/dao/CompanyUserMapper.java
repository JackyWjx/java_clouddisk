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
     * 点击调入公海是加入公海单位表
     *
     * @author kuangbin
     */
    int insertCompanyCommon(Map<String, Object> param);
} // End interface CompanyMapper
