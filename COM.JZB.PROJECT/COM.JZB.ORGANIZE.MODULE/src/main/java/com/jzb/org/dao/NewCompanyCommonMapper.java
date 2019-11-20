package com.jzb.org.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author chenzhengduan
 */
@Mapper
@Repository
public interface NewCompanyCommonMapper {
    /**
     * CRM-销售业主-公海-单位1
     * 点击公海显示所有的单位信息的总数
     *
     * @author kuangbin
     */
    int queryCompanyCommonListCount(Map<String, Object> param);

    /**
     * 我的单位基本资料CRM-单位用户-所有单位-单位列表
     * 查询所有的企业列表
     *
     * @author kuangbin
     */
    List<Map<String, Object>> queryCompanyCommonList(Map<String, Object> param);

    /**
     * 管理员创建公海单位
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: Kuang Bin
     * @DateTime: 2019/9/20 18:00
     */
    int insertCompanyCommonList(Map<String, Object> param);

    /**
     * CRM-销售业主-公海-单位3
     * 点击修改按钮,进行公海单位修改
     *
     * @author kuangbin
     */
    int updateCompanyCommonList(Map<String, Object> param);

    /**
     * CRM-销售业主-公海-单位4
     * 点击删除按钮,进行公海单位删除
     *
     * @author kuangbin
     */
    int deleteCompanyCommonList(Map<String, Object> param);
}
