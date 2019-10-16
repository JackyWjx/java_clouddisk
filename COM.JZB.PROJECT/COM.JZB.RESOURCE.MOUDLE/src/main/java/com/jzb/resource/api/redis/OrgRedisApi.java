package com.jzb.resource.api.redis;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/***
 *  redis数据库
 */
@FeignClient(name = "jzb-redis")
@RequestMapping(value = "/redis/org")
public interface OrgRedisApi {
    /**
     *
     * @param param 请求参数
     */
    @RequestMapping(value = "/getOrgList")
    public Map<Object, Object> getOrgList(@RequestBody Map<String, Object> param);
} // End interface OrgRedisApi
