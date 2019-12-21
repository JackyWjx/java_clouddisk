package com.jzb.resource.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Mapper
@Repository
public interface TbDocumentMsgPowerMapper {
    /**
     * 查询所有权限列表
     * @return
     */
    List<Map<String, Object>> queryPowerList();


    int upPowerList(List<Map<String, Object>> param);

    int delPowerList(Map<String, Object> param);

    int addPowerList(List<Map<String, Object>> param);
}
