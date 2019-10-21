package com.jzb.operate.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface TbTravelVehicleMapper {

    // 查询交通工具
    String queryCnameById(Map<String, Object> param);

    // 查询所有交通工具
    List<Map<String, Object>> queryVehicle();
}
