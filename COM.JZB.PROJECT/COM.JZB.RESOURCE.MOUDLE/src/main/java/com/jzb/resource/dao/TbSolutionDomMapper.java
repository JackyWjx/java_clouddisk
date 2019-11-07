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
     *
     * @param param
     * @return
     */
    List<Map<String, Object>> querySolutionDom(Map<String, Object> param);


    /**
     * 查询方案文档（模糊查询）
     *
     * @param param
     * @return
     */
    List<Map<String, Object>> querySolutionDomCname(Map<String, Object> param);

    /**
     * 查询方案文档详情ById
     *
     * @param param
     * @return
     */
    List<Map<String, Object>> queryDomByDomid(Map<String, Object> param);

    /**
     * 查询总数
     *
     * @param param
     * @return
     */
    int queryCount(Map<String, Object> param);

    /**
     * 查询热门榜
     *
     * @param param
     * @return
     */
    List<Map<String, Object>> queryHotDom(Map<String, Object> param);

    /**
     * CRM-运营管理-解决方案-文章列表
     * 点击解决方案中的新建后加入新建的方案文章
     *
     * @author kuangbin
     */
    int insertSolutionDom(Map<String, Object> param);

    /**
     * CRM-运营管理-解决方案-文章列表
     * 点击保存后对解决方案中的文章进行修改
     *
     * @author kuangbin
     */
    int updateSolutionDom(Map<String, Object> param);

    /**
     * CRM-运营管理-解决方案-文章列表
     * 点击删除后对解决方案中的文章进行删除操作(即修改状态)
     *
     * @author kuangbin
     */
    int deleteSolutionDom(Map<String, Object> param);

    /**
     * CRM-运营管理-活动-文章列表
     * 点击搜索解决方案文章标题后进行模糊搜索的总数查询,可加入时间
     *
     * @author kuangbin
     */
    int searchSolutionDomCount(Map<String, Object> param);

    /**
     * CRM-运营管理-活动-文章列表
     * 点击搜索解决方案文章标题后进行模糊搜索,可加入时间
     *
     * @author kuangbin
     */
    List<Map<String, Object>> searchSolutionDom(Map<String, Object> param);

    /**
     * CRM-运营管理-解决方案-SEO优化
     * 点击保存后对解决方案中的SEO优化进行修改
     *
     * @author kuangbin
     */
    int updateSolutionDomSEO(Map<String, Object> param);

    /**
     * CRM-运营管理-活动-SEO优化2
     * 点击SEO优化显示活动首页SEO优化信息
     *
     * @author kuangbin
     */
    List<Map<String, Object>> querySolutionDomSEO(Map<String, Object> param);
}
