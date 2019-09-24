package com.jzb.resource.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface TbPolicyDomMapper {

    /**
     *     查询政策文档列表(模糊查询)
      */
    List<Map<String, Object>> queryPolicyDomList(Map<String, Object> param);

    /**
     *     标准文档总数
     */
    int queryDocumentsCount(Map<String, Object> param);

    /**
     *     查询政策文档详情
     */
    List<Map<String, Object>> queryPolicyDomDesc(Map<String, Object> param);

    /**
     *     查询热门文档
     */
    List<Map<String, Object>> queryHotDom(Map<String, Object> param);
}
