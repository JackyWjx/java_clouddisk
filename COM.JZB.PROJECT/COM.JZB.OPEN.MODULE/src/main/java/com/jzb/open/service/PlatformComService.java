package com.jzb.open.service;

import com.jzb.open.dao.PlatformComMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author dell
 * @Version 1.0
 * @Since 1.0
 * @Date: 2019/11/7 15:04
 */
@Service
public class PlatformComService {
    @Autowired
    private PlatformComMapper platformComMapper;


    /**
     * 获取所有开放平台的企业id
     *
     * @param param
     * @return java.lang.String
     * @Author: DingSC
     */
    public Map<String, Object> queryPlatformIds(Map<String, Object> param) {
        return platformComMapper.queryPlatformIds(param);
    }

    /**
     * 根据cid和集获取管理员信息
     *
     * @param param
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @Author: DingSC
     */
    public List<Map<String, Object>> getComAndMan(Map<String, Object> param) {
        return platformComMapper.getComAndMan(param);
    }

    /**
     * 开发者列表查询
     *
     * @param
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @Author: DingSC
     */
    public List<Map<String, Object>> searchAppDeveloper(Map<String, Object> param) {
        return platformComMapper.searchAppDeveloper(param);
    }
}
