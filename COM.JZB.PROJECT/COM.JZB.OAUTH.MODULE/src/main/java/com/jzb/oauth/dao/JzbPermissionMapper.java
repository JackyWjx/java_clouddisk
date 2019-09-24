package com.jzb.oauth.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 用户角色权限
 *
 * @author Chad
 * @date 2019年08月01日
 */
@Mapper
public interface JzbPermissionMapper {
    /**
     * 根据用户ID查询用户权限
     * @param param
     * @return
     */
    List<Map<String, Object>> queryRoleByUserId(Map<String, Object> param);
} // End interface JzbPermissionMapper
