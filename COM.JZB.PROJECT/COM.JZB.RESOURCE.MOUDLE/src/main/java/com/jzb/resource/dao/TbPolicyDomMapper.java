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

    /**
     * LBQ
     *  根据菜单类别进行查询  如果没有菜单类别则进行所有的查询
     * @param param
     * @return
     */
    List<Map<String, Object>> getTbPolicyDocument(Map<String, Object> param);

    /**
     * LBQ
     * 运营管理中政策中内容列表的新建
     *
     * @param param
     * @return
     */
    int saveTbPolicyDom(Map<String, Object> param);

    /**
     * 运营管理政策中内容列表的修改
     *
     * @param param
     * @return
     */
    int updatePolicyDom(Map<String, Object> param);

    /**
     * 设置删除状态
     *
     * @param param
     * @return
     */
    int updateDelete(Map<String, Object> param);
}
