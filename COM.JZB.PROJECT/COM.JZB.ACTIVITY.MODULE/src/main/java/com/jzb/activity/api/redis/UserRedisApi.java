package com.jzb.activity.api.redis;

import com.jzb.base.message.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/***
 *  redis数据库
 */
@FeignClient(name = "jzb-redis")
@RequestMapping(value = "/redis/user")
@Repository
public interface UserRedisApi {

    /**
     * 获取缓存中用户信息
     * @param param
     */
    @RequestMapping(value = "/getCacheUserInfo", method = RequestMethod.POST)
    public Response getCacheUserInfo(@RequestBody Map<String, Object> param);
}
