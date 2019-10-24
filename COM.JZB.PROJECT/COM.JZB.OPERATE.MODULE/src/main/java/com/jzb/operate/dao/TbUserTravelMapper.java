package com.jzb.operate.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface TbUserTravelMapper {

    /**
     * 添加用户出差记录
     * @param list
     * @return
     */
    int addUserTavel(List<Map<String, Object>> list);


    List<Map<String, Object>> queryUserTravel(Map<String, Object> param);
}

