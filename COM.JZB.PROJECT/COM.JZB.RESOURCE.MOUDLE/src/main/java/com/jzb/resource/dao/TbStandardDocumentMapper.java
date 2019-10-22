package com.jzb.resource.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 运营管理中的内容列表
 */
@Mapper
@Repository
public interface TbStandardDocumentMapper {
    /**
     * 查询运营管理下的内容列表
     *
     * @param param
     * @return
     */
    List<Map<String, Object>> getTbStandardDocument(Map<String, Object> param);


    /**
     * 运营管理中标准中内容列表的新建
     * @param param
     * @return
     */
    int saveTbStandardDom(Map<String, Object> param);

    /**
     * 运营管理中标准中内容列表的修改
     * @param param
     * @return
     */
    int updateTbStandardDom(Map<String, Object> param);

    /**
     * 设置删除状态
     * @param param
     * @return
     */
    int updateDelete(Map<String, Object> param);

    /**
     * 根据名称进行模糊查询
     *
     * @param param
     * @return
     */
    List<Map<String, Object>> getCname(Map<String, Object> param);
}
