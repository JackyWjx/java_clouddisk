package com.jzb.org.service;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.org.dao.CommonUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @描述
 * @创建人 chenhui
 * @创建时间 2019/11/19
 * @修改人和其它信息
 */
@Service
public class CommonUserService {
    @Autowired
    CommonUserMapper userMapper;

    // 添加公海用户
    public int addCommUser(Map<String, Object> paramp) {
        paramp.put("uid", JzbRandom.getRandomCharCap(12));
        paramp.put("status",'1');
        paramp.put("age", JzbDataType.getInteger(paramp.get("age")));
        return userMapper.addCommUser(paramp);
    }

    // 获取公海用户总数
    public int getCount(Map<String, Object> paramp) {
        return userMapper.getCount(paramp);
    }

    // 获取公海用户信息
    public List<Map<String, Object>> queryCommonUser(Map<String, Object> paramp) {

        if (!JzbTools.isEmpty(paramp.get("searchtext"))){
            String searchtext = (String) paramp.get("searchtext");
            if (searchtext.length() == 11){
                paramp.put("phone",searchtext);
            }else {
                paramp.put("uname",searchtext);
            }
        }
        return userMapper.queryComUser(paramp);
    }
    // 修改公海用户信息
    public int updComUser(Map<String, Object> paramp) {
        paramp.put("updtime",System.currentTimeMillis());
        return userMapper.updComUser(paramp);
    }

    // 删除公海用户信息
    public int delUser(Map<String,Object> map){
        map.put("updtime",System.currentTimeMillis());
        map.put("status",'2');
        return userMapper.delUser(map);
    }
}
