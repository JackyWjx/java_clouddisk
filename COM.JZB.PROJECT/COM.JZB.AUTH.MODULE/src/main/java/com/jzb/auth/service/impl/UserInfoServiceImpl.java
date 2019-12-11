package com.jzb.auth.service.impl;

import com.jzb.auth.dao.UserInfoMapper;
import com.jzb.auth.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 *  @author: gongWei
 *  @Date: Created in 2019/12/11 9:10
 *  @Description:
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    UserInfoMapper userInfoMapper;
    /**
     *  @author: gongWei
     *  @Date:  2019/12/11 9:11
     *  @Description:
     *  @Param  
     *  @Return 
     *  @Exception 
     */
    
    public String getUsernameByUids(Map<String, Object> param) {
        Map<String,Object> whereMap = new HashMap<>();
        String[] uids = param.get("uids").toString().split(",");
        List<String> uidList = new ArrayList<>(Arrays.asList(uids));
        whereMap.put("uids",uidList);
        String unameStr = userInfoMapper.getUsernameByUids(whereMap);
        return unameStr;
    }
}
