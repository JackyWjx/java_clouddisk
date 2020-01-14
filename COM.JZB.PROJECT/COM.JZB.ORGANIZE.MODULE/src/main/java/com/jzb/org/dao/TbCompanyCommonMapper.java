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
     * 查询不带条件的业主单位全部（zongshu ）
     * @param param
     * @return
     */
    int queryCompanyCommonCount(Map<String, Object> param);


    /**
     * 查询带条件的业主单位全部（带条件）
     * @param param
     * @return
     */
    List<Map<String, Object>> queryCompanyCommonByKeyWord(Map<String, Object> param);

    /**
     * 查询带条件的业主单位全部（带条件）
     * @param param
     * @return
     */
    int queryCompanyCommonByKeyWordCount(Map<String, Object> param);

    /**
     * 查询单位名称
     * @param param
     * @return
     */
    String queryCompanyNameByID(Map<String, Object> param);

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

    /**
     * @Author Reed
     * @Description 退回公海
     * @Date 18:55 2020/1/2
     * @Param [param]
     * @return int
    **/
    int rebackCompanys(Map<String, Object> param);

    /**
     * 所有业主-业主列表查询
     * @param param
     * @return
     */
    List<Map<String, Object>> getCompanyCommoms(Map<String, Object> param);

    /**
     * CRM-所有业主-业主列表-修改
     * 点击修改判断是否是系统中的用户如果不是就新建用户
     *
     * @Author: Kuang Bin
     * @DateTime: 2019/10/11
     */
    int updateCompanyListInfo(Map<String, Object> param);

    Map<String, Object> queryCompanySales(Map<String, Object> param);

    int  updateCompanysAddSales(Map<String, Object> sMap);

    int relpaceCompanysSales(Map<String, Object> param);

    int rebackCompanysToHistory(Map<String, Object> param);

    List<Map<String, Object>> getCompanyCommonListHistory(Map<String, Object> param);

    int getCompanyCommonListHistoryCount(Map<String, Object> param);

    List<Map<String, Object>> queryCompanysToHistory(Map<String, Object> param);

    int queryCompanysToHistoryCount(Map<String, Object> param);
}
