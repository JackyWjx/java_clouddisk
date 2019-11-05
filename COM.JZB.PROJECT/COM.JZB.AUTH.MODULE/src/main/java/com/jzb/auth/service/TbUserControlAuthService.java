package com.jzb.auth.service;

import com.jzb.auth.dao.TbUserControlAuthMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TbUserControlAuthService {

    @Autowired
    private TbUserControlAuthMapper tbUserControlAuthMapper;


    /**
     * 给单个用户授权
     * @param list
     * @return
     */
    public int addUserControlAuth(List<Map<String, Object>> list){
        return tbUserControlAuthMapper.addUserControlAuth(list);
    }
}
