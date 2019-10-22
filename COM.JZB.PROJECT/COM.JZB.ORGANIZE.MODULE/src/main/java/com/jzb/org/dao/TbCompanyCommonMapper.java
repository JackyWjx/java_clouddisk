package com.jzb.org.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author chenzhengduan
 */
@Mapper
@Repository
public interface TbCompanyCommonMapper {

    /**
     * 查询不带条件的业主单位全部（不带条件）
     * @param param
     * @return
     */
    List<Map<String, Object>> queryCompanyCommon(Map<String, Object> param);


    /**
     * 查询带条件的业主单位全部（带条件）
     * @param param
     * @return
     */
    List<Map<String, Object>> queryCompanyCommonByKeyWord(Map<String, Object> param);


}
