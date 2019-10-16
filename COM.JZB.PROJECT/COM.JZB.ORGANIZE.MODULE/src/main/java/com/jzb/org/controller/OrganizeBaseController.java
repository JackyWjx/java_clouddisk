package com.jzb.org.controller;

import com.jzb.base.message.Response;
import com.jzb.org.api.redis.UserRedisServiceApi;
import com.jzb.org.service.OrganizeBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 组织基础信息获取
 * @author Chad
 * @date 2019年7月22日
 */
@RestController
@RequestMapping(value = "/org")
public class OrganizeBaseController {
    /**
     * 业务处理对象
     */
    @Autowired
    private OrganizeBaseService baseService;

    /**
     * 用户Redis操作API
     */
    @Autowired
    private UserRedisServiceApi userApi;

    /**
     * 测试API
     */
    @RequestMapping(value = "/getUserInfo", method = RequestMethod.GET)
    public Response<String> getUserInfo() {
        Map<String, Object> cacheMap = new HashMap<>();
        cacheMap.put("uid", "U000002");
        cacheMap.put("name", "李四");
        userApi.cacheIdName(cacheMap);

        Map<String, Object> getMap = new HashMap<>();
        getMap.put("uid", "U000002");
        return userApi.getNameById(getMap);
    } // End getUserInfo
} // End class OrganizeBaseController
