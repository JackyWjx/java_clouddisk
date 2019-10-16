package com.jzb.oauth.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 用户查询
 *
 * @author Chad
 * @date 2019年08月01日
 */
@Mapper
@Repository
public interface JzbUserMapper {
    /**
     * 根据用户的电话号码查询用户信息
     *
     * @param param 查询参数
     * @return List<Map<String, Object>> 查询的用户列表
     */
    List<Map<String, Object>> queryUserByPhone(Map<String, Object> param);
} // End interface JzbUserMapper
