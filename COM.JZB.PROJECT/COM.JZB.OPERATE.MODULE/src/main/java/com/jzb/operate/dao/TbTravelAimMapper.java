package com.jzb.operate.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Mapper
@Repository
public interface TbTravelAimMapper {

    int addTravelAim(Map<String, Object> param);
}
