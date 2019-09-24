package com.jzb.oauth.service;

import com.jzb.oauth.dao.JzbUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户信息处理
 */
@Service
public class JzbUserService {
    @Autowired
    private JzbUserMapper userMapper;

    /**
     * 根据用户注册电话获取用户信息
     * @param phone
     * @return
     */
    public Map<String, Object> getUserByPhone(String phone) {
        Map<String, Object> param = new HashMap<>();
        param.put("phone", phone);
        List<Map<String, Object>> records = userMapper.queryUserByPhone(param);
        return records.get(0);
    } // End getUserByPhone
} // End class JzbUserService
