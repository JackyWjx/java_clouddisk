package com.jzb.auth.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Map;

/***
 * 企业表
 */
@Mapper
@Repository
public interface CompanyListMapper {


    /**
     * 根据用户姓名获取id合集
     *
     * @param param
     * @return java.lang.String
     * @Author: DingSC
     */
     String searchUidByUidCname(Map<String, Object> param);
}
