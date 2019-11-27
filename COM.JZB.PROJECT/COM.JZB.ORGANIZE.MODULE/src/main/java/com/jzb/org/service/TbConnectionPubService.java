package com.jzb.org.service;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.util.JzbRandom;
import com.jzb.org.dao.TbConnectionPubMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author chenhui
 * @description
 * @time2019/11/26
 * @other
 */
@Service
public class TbConnectionPubService {
    @Autowired
    TbConnectionPubMapper pubMapper;

    // 查询发帖总数
    public int getConnectionCount(Map<String, Object> param) {
        return pubMapper.getConnectionCount(param);
    }

    // 查询发帖信息
    public List<Map<String, Object>> getConnectionList(Map<String, Object> param) {
        param.put("status",'1');
        return pubMapper.getConnectionList(param);
    }
    // 修改发帖信息
    public int modifyConnectionList(Map<String, Object> param) {
        param.put("updtime",System.currentTimeMillis());
        return pubMapper.modifyConnectionList(param);
    }
    // 新建发帖信息
    public int insertConnectionList(Map<String, Object> param) {
        param.put("status",'1');
        param.put("addtime",System.currentTimeMillis());
        param.put("pubid", JzbRandom.getRandomCharCap(7));
        return pubMapper.insertConnectionList(param);
    }

    // 删除发帖信息
    public int removeConnectionList(Map<String, Object> param) {
        param.put("status",'2');
        param.put("updtime",System.currentTimeMillis());
        return pubMapper.removeConnectionList(param);
    }
}
