package com.jzb.operate.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface TbTravelConsumeMapper {

    /**
     * 添加出差报销记录
     * @param list
     * @return
     */
    int addTravelConsume(List<Map<String, Object>> list);
}
