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
    List<Map<String , Object>>  queryTbHandleItem(Map<String , Object> map);

    /**
     * 根据项目id 获取项目跟进详情总数
     *
     * @param proid
     * @return
     */
    int  queryTbHandleItemCount(Map<String , Object> map);

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
    int  queryTbCompanyServiceCount(Map<String , Object> map);

    /**
     * 查询项目总数
     * @param map
     * @return
     */
    int queryCompanyServiceTypeCount(Map<String , Object> map);
}
