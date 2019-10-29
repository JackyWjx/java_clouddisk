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

    /**
     * 修改单位信息
     * @param param
     * @return
     */
    int updateCompany(Map<String, Object> param);

    /**
     * 查询业主列表查询出来的总数
     * @param param
     * @return
     */
    int getCount(Map<String, Object> param);

    /**
     * 查询所有业主-业主列表
     * @param param
     * @return
     */
    List<Map<String, Object>> getCompanyCommon(Map<String, Object> param);

    /**
     * 所有业主-业主列表-新建
     * @param param
     * @return
     */
    int saveCompanyCommon(Map<String, Object> param);

    /**
     * 所有业主-业主列表-修改
     * @param param
     * @return
     */
    int updateCompanyCommon(Map<String, Object> param);

    /**
     * 所有业主-业主列表-删除
     * @param paramList
     * @return
     */
    int deleteCompanyCommon(List<Map<String, Object>> paramList);

    /**
     * 所有业主-业主列表-分配业务员
     * @param param
     * @return
     */
    int updateCompanys(Map<String, Object> param);
}
