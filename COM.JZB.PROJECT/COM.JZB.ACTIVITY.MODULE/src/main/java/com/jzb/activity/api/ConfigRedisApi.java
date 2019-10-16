package com.jzb.activity.api;

import com.jzb.base.message.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * Redis配置
 * @author Chad
 * @date 2019年08月08日
 */
@FeignClient(name = "jzb-redis")
@RequestMapping(value = "/redis/config")
public interface ConfigRedisApi {
    /**
     * 获取资源类型信息
     * @return
     */
    @RequestMapping(value = "/getResourceType", method = RequestMethod.POST)
    public Response<String> getResourceType(Map<String, Object> param);
} // End class ConfigRedisApi
