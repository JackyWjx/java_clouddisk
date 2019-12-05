package com.jzb.org.dao;


import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Author sapientia
 * @Date 2019/12/4 14:19
 */
@Mapper
@Repository
public interface NewCompanyProjectMapper {

    List<Map<String, Object>> queryCompanyByid(Map<String, Object> map);

    List<Map<String, Object>> queryCompanyByProjectid(Map<String, Object> map);

    List<Map<String, Object>> queryCommonCompanyListBycid(Map<String, Object> map);

    int updateCompanyProjectInfo(Map<String, Object> map);

    int updateCompanyProject(Map<String, Object> map);

    int updateCommonCompanyList(Map<String, Object> map);
}
