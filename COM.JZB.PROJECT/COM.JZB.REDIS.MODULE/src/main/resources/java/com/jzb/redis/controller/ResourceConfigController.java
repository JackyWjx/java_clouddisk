package com.jzb.redis.controller;

import com.jzb.base.message.JzbReturnCode;
import com.jzb.base.message.Response;
import com.jzb.redis.config.UserCacheKeys;
import com.jzb.redis.service.EnvironRedisService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 配置相关数据缓存到Redis
 * @author Chad
 * @date 2019年8月8日
 */
@RestController
@RequestMapping(value = "/redis/config")
public class ResourceConfigController {
    /**
     * 环境Redis服务对象
     */
    @Autowired
    private EnvironRedisService redisService;

    /**
     * 从Redis中读取一个资源类型的配置
     * 请求参数，restype
     * @param param 请求参数
     */
    @RequestMapping(value = "/getResourceType", method = RequestMethod.POST)
    public Response getResourceType(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String resType = param.get("restype").toString();
            if (!StringUtils.isEmpty(resType)) {
                Object obj = redisService.getRedisService().getMapValue(UserCacheKeys.JZB_RESOURCE_TYPE_TABLE, resType);
                if(obj != null){
                    result = Response.getResponseSuccess();
                    result.setResponseEntity(obj.toString());
                } else {
                    result = Response.getResponseError();
                }
            } else {
                result = Response.getResponseError();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            result = Response.getResponseError();
        }
        return result;
    } // End getResourceType

    /**
     * 从Redis中读取一个资源类型的配置
     * 请求参数，restype
     * @param param 请求参数
     */
    @RequestMapping(value = "/cacheResourceType", method = RequestMethod.POST)
    public Response cacheResourceType(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String resType = param.get("restype").toString();
            String resValue = param.get("value").toString();
            if (!StringUtils.isEmpty(resType)) {
                boolean res = redisService.getRedisService().setMapValue(UserCacheKeys.JZB_RESOURCE_TYPE_TABLE, resType, resValue);
                result = res ? Response.getResponseSuccess() : Response.getResponseError();
            } else {
                result = Response.getResponseError();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            result = Response.getResponseError();
        }
        return result;
    } // End cacheResourceType
} // End class ResourceConfigController
