package com.jzb.auth.service;

import com.jzb.auth.dao.AuthInfoMapper;
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

    public String getUserByUids(Map<String, Object> param) {
        Map<String,Object> whereMap = new HashMap<>();
        String[] uids = param.get("uids").toString().split(",");
        List<String> uidList = new ArrayList<>(Arrays.asList(uids));
        whereMap.put("uids",uidList);
        String unameStr = authInfoMapper.getUserByUids(whereMap);
        return unameStr;
    }
}
