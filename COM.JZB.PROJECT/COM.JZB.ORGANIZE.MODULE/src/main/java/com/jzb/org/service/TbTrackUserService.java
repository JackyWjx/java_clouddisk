package com.jzb.org.service;

import com.alibaba.fastjson.JSON;
import com.jzb.base.data.JzbDataType;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.org.dao.TbTrackUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author chenhui
 * @description
 * @time2019/11/25
 * @other
 */
@Service
public class TbTrackUserService {
    @Autowired
    TbTrackUserMapper userMapper;

    // 新建跟进人员记录
    public int addTrackUser(Map<String, Object> param) {
        param.put("addtime",System.currentTimeMillis());
        param.put("tracktime",JzbDataType.getLong(param.get("tracktime")));
        param.put("trackid", JzbRandom.getRandomCharCap(17));
        param.put("customer",JzbRandom.getRandomCharCap(12));
        param.put("status",'1');
        param.put("image",param.get("image").toString());
        return userMapper.addTrackUser(param);
    }
    // 查询跟进人员记录总数
    public int getTrackCount(Map<String, Object> param) {
        return userMapper.getTrackCount(param);
    }
    // 查询跟进人员记录信息
    public List<Map<String, Object>> queryTrackUserList(Map<String, Object> param) {

        return userMapper.queryTrackUserList(param);
    }

    // 删除跟进人员记录信息
    public int delTrackUser(Map<String, Object> param) {
        param.put("updtime",System.currentTimeMillis());
        return userMapper.delTrackUser(param);
    }

    // 修改跟进人员记录信息
    public int updTrackUser(Map<String, Object> param) {
        param.put("upduid",param.get("adduid"));
        param.put("updtime",System.currentTimeMillis());
        return userMapper.updTrackUser(param);
    }
}
