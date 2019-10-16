package com.jzb.resource.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface TbMethodTypeMapper {

    /*
     * 1.查询方法论（父子级）
     * */
    List<Map<String, Object>> queryMethodType(Map<String, Object> param);


    /*
     * 1.查询方法论（父子级）(分页)
     * */
    List<Map<String, Object>> queryMethodTypePage(Map<String, Object> param);

    /*
     * 1.查询方法论（父子级）(分页) (总数)
     * */
    int queryMethodTypePageCount();

    /*
     * 1.查询方法论（父子级）(分页)(子级)
     * */
    List<Map<String, Object>> queryMethodTypePageSon(Map<String, Object> param);

    /*
     * 2.新建方法论
     * */
    int saveMethodType(Map<String, Object> param);

    /*
     * 3.修改方法论
     * */
    int updateMethodType(Map<String, Object> param);

    /*
     * 4.获取排序
     * */
    int getMethodTypeIdx();

    /*
     * 5.根据同级typeid查询 parentid
     * */
    String getParentByBrotherId(Map<String, Object> param);

    /*
     * 6.查询方法论资料tab方法论类别第一级  如果传了parentid就查子级，没传就查第一级
     * */
    List<Map<String, Object>> queryMethodLevel(Map<String, Object> param);
}
