package com.jzb.resource.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author duanfeiyu
 * @Version v1.0
 * @Since 1.0
 * @Date: 2019/8/16 18:20
 */
@Mapper
@Repository
public interface StandardMapper {
    /**
     * 查询父子关系
     *
     * @return
     */
    List<Map<String, Object>> queryFatherOne();

    /**
     * 标准文档总数
     *
     * @return
     */
    int queryDocumentsCount(Map<String, Object> params);

    /**
     * 标准文档查询
     *
     * @param params
     * @return
     */
    List<Map<String, Object>> queryDocumentsList(Map<String, Object> params);

    /*
    * 查询文档详情
    * */
    List<Map<String, Object>> queryDocumentDesc(Map<String, Object> params);


    /**
     * 查询热门榜
     * @param param
     * @return
     */
    List<Map<String, Object>> queryHotDom(Map<String, Object> param);
}
