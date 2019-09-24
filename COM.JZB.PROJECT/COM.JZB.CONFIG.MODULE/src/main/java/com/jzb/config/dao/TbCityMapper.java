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
}
