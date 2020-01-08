package com.jzb.org.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Mapper
@Repository
public interface TbPlantaskMapper {

     List<Map<String, Object>> getPantaskList1(Map<String ,Object> param);

     int addPlantaskBrother(Map<String, Object> param);

     int updatePlantask(Map<String, Object> param);

     int getPantaskCount(Map<String, Object> param);

     List<Map<String, Object>> getCompanydeptMap();

     int delPlantask(Map<String, Object> param);
}