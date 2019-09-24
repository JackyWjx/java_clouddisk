package com.jzb.resource.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface TbSolutionDomMapper {

    /**
     * 查询方案文档
     * @param param
     * @return
     */
    List<Map<String, Object>> querySolutionDom(Map<String, Object> param);


    /**
     * 查询方案文档（模糊查询）
     * @param param
     * @return
     */
    List<Map<String, Object>> querySolutionDomCname(Map<String, Object> param);

    /**
     * 查询方案文档详情ById
     * @param param
     * @return
     */
    List<Map<String, Object>> queryDomByDomid(Map<String, Object> param);

    /**
     * 查询总数
     * @param param
     * @return
     */
    int queryCount(Map<String, Object> param);

    /**
     * 查询热门榜
     * @param param
     * @return
     */
    List<Map<String, Object>> queryHotDom(Map<String, Object> param);
}
