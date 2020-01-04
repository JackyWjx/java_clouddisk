package com.jzb.auth.service;

import com.jzb.auth.dao.AuthInfoMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author sapientia
 * @Date 2019/12/23 19:12
 * @Descrpition
 */
@Service
public class AuthInfoService {

    @Autowired
    private AuthInfoMapper authInfoMapper;

    public Map<String, Object> getUserByUids(Map<String, Object> param) {

        Map<String, Object> userMap = authInfoMapper.getUserByUids(param);
        return userMap;
    }
}
