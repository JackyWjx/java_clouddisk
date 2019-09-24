package com.jzb.operate.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface TbUserTravelConsumeMapper {

    /**
     * 添加用户出差报销记录
     * @param list
     * @return
     */
    int addUserTravelConsume(List<Map<String, Object>> list);

    /**
     * 修改用户出差报销记录
     * @param list
     * @return
     */
    int updateUserTravelConsume(List<Map<String, Object>> list);

    /**
     * 查询用户出差记录
     * @param param
     * @return
     */
    List<Map<String, Object>> quertUserTravelComsume(Map<String, Object> param);
}
