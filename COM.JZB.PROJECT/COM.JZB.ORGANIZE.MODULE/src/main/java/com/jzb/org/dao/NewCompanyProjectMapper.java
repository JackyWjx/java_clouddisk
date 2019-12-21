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

    List<Map<String, Object>> queryCompanyByid(Map<String, Object> param);

    //根据projectid查询项目列表
    List<Map<String, Object>> queryCompanyByProjectid(Map<String, Object> param);

    //根据cid查询公司列表
    List<Map<String, Object>> queryCommonCompanyListBycid(Map<String, Object> param);

    int updateCompanyProjectInfo(Map<String, Object> param);

    int updateCompanyProject(Map<String, Object> param);

    int updateCommonCompanyList(Map<String, Object> param);

    //产出情况总数
    int countProjectInfo(Map<String, Object> param);

    List<Map<String, Object>> queryCompanyNameBycid(Map<String, Object> param);

    List<Map<String, Object>> queryPronameByid(Map<String, Object> param);

    // 根据cid查询一条用户信息
    Map<String, Object> queryCompanyInfoByCid(Map<String, Object> param);
    // 根据cid查询项目信息集合(只获取项目id和项目名称)
    List<Map<String, Object>> queryProjectInfoByCid(Map<String, Object> param);

    // 根据projectId查询单个项目的基本信息
    Map<String, Object> queryProjectByProid(Map<String, Object> param);

    // 根据projectId查询单个项目下的情报信息
    Map<String, Object> queryProjectInfoByProid(Map<String, Object> param);
}
