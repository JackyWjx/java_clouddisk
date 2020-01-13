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

     List<Map<String, Object>> selPantaskids(Map<String, Object> param);

     List<Map<String, Object>> getPantaskDas(Map<String, Object> param);

     Map<String, Object> getPantaskDasXiangxi(Map<String, Object> param);

    int upDayXiangxi(Map<String, Object> param);

    List<Map<String, Object>> selcdids(String uid);

    List<Map<String, Object>> selFu(Map<String, Object> param);

    int upDayzhongzhi(Map<String, Object> param);
}
