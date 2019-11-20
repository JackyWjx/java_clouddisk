package com.jzb.auth.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    /**
     * 获取认证类型数据
     *
     * @param param
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @Author: DingSC
     */
    List<Map<String, Object>> getAuthTypeList(Map<String, Object> param);
}
