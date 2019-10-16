package com.jzb.auth.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/***
 * 企业表
 */
@Mapper
public interface CompanyListMapper {
    /**
     * 创建企业
     */
    Integer saveCompanyList(Map<String, Object> param);

    /**
     * 单位信息表
     */
    Integer saveCompanyInfo(Map<String, Object> param);

    /**
     * 申请记录
     */
    Integer saveInviteUser(Map<String, Object> param);

    /**
     * 根据单位名称 查询单位是否存在
     */
    int getConmpanyByName(Map<String,Object> param);
}
