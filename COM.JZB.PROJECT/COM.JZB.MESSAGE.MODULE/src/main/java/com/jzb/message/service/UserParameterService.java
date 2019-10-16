package com.jzb.message.service;

import com.jzb.base.data.JzbDataType;
import com.jzb.message.dao.UserParameterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserParameterService {

    @Autowired
    private UserParameterMapper userParameterMapper;

    /**
     * 查询
     */
    public List<Map<String, Object>> queryUserParameter(Map<String, Object> map) {
        int page = JzbDataType.getInteger(map.get("page")) == 0 ? 0 : JzbDataType.getInteger(map.get("page")) - 1;
        map.put("page", page * JzbDataType.getInteger(map.get("rows")));
        map.put("rows", JzbDataType.getInteger(map.get("rows")));
        return userParameterMapper.queryUserParameter(map);
    }

    /**
     * 查询总数
     */
    public int queryUserParameterCount(Map<String, Object> map) {
        return userParameterMapper.queryUserParameterCount(map);
    }

    /**
     * 模糊查询
     */
    public List<Map<String, Object>> searchUserParameter(Map<String, Object> map) {
        int page = JzbDataType.getInteger(map.get("page")) == 0 ? 0 : JzbDataType.getInteger(map.get("page")) - 1;
        map.put("page", page * JzbDataType.getInteger(map.get("rows")));
        map.put("rows", JzbDataType.getInteger(map.get("rows")));
        return userParameterMapper.searchUserParameter(map);
    }

    /**
     * 模糊查询总数
     */
    public int searchUserParameterCount(Map<String, Object> map) {
        return userParameterMapper.searchUserParameterCount(map);
    }

    /**
     * 添加
     */
    public int saveUserParameter(Map<String, Object> map) {
        map.put("status", '1');
        map.put("addtime", JzbDataType.getInteger(map.get("addtime")));
        map.put("updtime", JzbDataType.getInteger(map.get("updtime")));
        return userParameterMapper.insertUserParameter(map);
    }

    /**
     * 修改
     */
    public int upUserParameter(Map<String, Object> map) {
        map.put("addtime", JzbDataType.getInteger(map.get("addtime")));
        map.put("updtime", JzbDataType.getInteger(map.get("updtime")));
        map.put("id", JzbDataType.getInteger(map.get("id")));
        return userParameterMapper.updateUserParameter(map);
    }

    /**
     * 禁用
     */
    public int removeUserParameter(Map<String, Object> map) {
        map.put("id", JzbDataType.getInteger(map.get("id")));
        return userParameterMapper.deleteUserParameter(map);
    }
}
