package com.jzb.resource.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/*
 * 模板类型
 * */
@Mapper
@Repository
public interface TbTempTypeMapper {

    /*
     * 1.添加系统模板
     * */
    int saveTempType(Map<String, Object> param);

    /*
     * 2.修改系统模板
     * */
    int updateTempType(Map<String, Object> param);

    /*
     * 3.获取排序
     * */
    int getTempTypeIdx();

    /*
     * 4.获取模板类型（父子级）
     * */
    List<Map<String, Object>> queryTempType();

    /*
     * 5.根据父级id查询模板类型
     * */
    List<Map<String, Object>> queryTempTypeById(Map<String, Object> param);

    /*
     * 6.修改状态  1正常  2不正常  4 删除
     * */
    int updateStatus(Map<String, Object> param);
}
