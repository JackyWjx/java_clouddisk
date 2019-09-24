package com.jzb.oauth.service;

import com.jzb.oauth.dao.JzbPermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 获取权限
 * @author Chad
 * @date 2019年08月01日
 */
@Service
public class JzbPermissionService {
    /**
     * 权限查询
     */
    @Autowired
    private JzbPermissionMapper permissionMapper;

    /**
     * 查询用户权限
     * @param userId
     * @return
     */
    public List<Map<String, Object>> findByUserId(String userId) {
        List<Map<String, Object>> result;
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("uid", userId);
            result = permissionMapper.queryRoleByUserId(param);
        } catch (Exception ex) {
            ex.printStackTrace();
            result = null;
        }
        return result;
    } // End findByUserId
} // End class JzbPermissionService
