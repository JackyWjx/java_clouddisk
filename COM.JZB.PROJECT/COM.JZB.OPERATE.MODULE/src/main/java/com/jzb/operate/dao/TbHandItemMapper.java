package com.jzb.operate.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Description: 跟进详情DB
 * @Author Han Bin
 */
@Mapper
@Repository
public interface TbHandItemMapper {

    /**
     * 根据项目id 获取项目跟进详情
     *
     * @param proid
     * @return
     */
    List<Map<String , Object>>  queryTbCompanyServiceNotDis(Map<String , Object> map);

    /**
     * 根据项目id 获取项目跟进详情总数
     *
     * @param proid
     * @return
     */
    int  queryTbCompanyServiceNotDisCount(Map<String , Object> map);

    /**
     * 根据项目id
     *
     * @param proid
     * @return
     */
    int  queryCount(Map<String , Object> map);

    /**
     * 根据项目id 获取项目跟进详情
     *
     * @param proid
     * @return
     */
    List<Map<String , Object>>  queryTbCompanyService(Map<String , Object> map);

    /**
     * 根据项目id 获取项目跟进详情总数
     *
     * @param proid
     * @return
     */
    int queryTbCompanyServiceCount(Map<String , Object> map);

    /**
     * 获取所有
     * @param map
     * @return
     */
    List<Map<String , Object>>  selectCompanyService(Map<String , Object> map);

    int queryCompanyServiceCountAAA(Map<String , Object> map);

}
