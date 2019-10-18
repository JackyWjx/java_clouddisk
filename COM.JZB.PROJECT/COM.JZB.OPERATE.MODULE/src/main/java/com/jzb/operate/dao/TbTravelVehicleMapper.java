package com.jzb.operate.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Mapper
@Repository
public interface TbTravelVehicleMapper {

     // 查询交通工具
     String queryCnameById(Map<String, Object> param);
}
