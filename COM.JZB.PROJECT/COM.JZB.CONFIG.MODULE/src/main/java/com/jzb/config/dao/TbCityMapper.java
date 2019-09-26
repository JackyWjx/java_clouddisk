package com.jzb.config.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface TbCityMapper {

    /*
    * 添加城市
    *
    * */
   int addCityList(List<Map<String, Object>> list);


    /*
    * 获取城市
    * */
   List<Map<String, Object>> getCityList(Map<String, Object> param);

    /**
     * 根据地区名称获取地区ID信息
     *
     * @param param(包含地区名称)
     * @author kuangbin
     */
    Map<String, Object> queryRegionID(Map<String, Object> param);

    /**
     * 根据地区名称获取地区ID信息
     *
     * @param param(包含地区名称)
     * @author kuangbin
     */
    Map<String, Object> queryRegionInfo(Map<String, Object> param);
}
