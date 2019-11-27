package com.jzb.org.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Description: 类型
 * @Author Han Bin
 */
@Mapper
@Repository
public interface TbTypeMapper {

    /***
     *  查询 类型表
     * @param param
     * @return
     */
    List<Map<String , Object>>  queryTbTypeList(Map<String , Object> param);

    /**
     * 类型参数表
     * @param param
     * @return
     */
    List<Map<String , Object>> queryTbTypeInfo(Map<String  , Object> param);

    /***
     *  查询 类型表
     * @param param
     * @return
     */
    int  queryTbTypeListCount(Map<String , Object> param);

    /**
     * 类型参数表
     * @param param
     * @return
     */
    int queryTbTypeInfoCount(Map<String  , Object> param);

}
