package com.jzb.operate.api.redis;

import com.jzb.base.message.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * 用户相关数据缓存到Redis
 *
 * @author Chad
 * @date 2019年7月20日
 */
@FeignClient(name = "jzb-redis")
@RequestMapping(value = "/redis/user")
@Repository
public interface UserRedisServiceApi {
    /**
     * 获取用户名称
     *
     * @return
     */
    @RequestMapping(value = "/getNameById", method = RequestMethod.POST)
    public Response getNameById(@RequestBody Map<String, Object> param);

    /**
     * 缓存用户名称
     *
     * @return
     */
    @RequestMapping(value = "/cacheIdName", method = RequestMethod.POST)
    public Response cacheIdName(@RequestBody Map<String, Object> param);

    /**
     * 获取缓存中用户信息
     * uid 请求参数
     *
     * @param param
     */
    @RequestMapping(value = "/getCacheUserInfo", method = RequestMethod.POST)
    public Response getCacheUserInfo(@RequestBody Map<String, Object> param);

    /**
     * 根据联系方式获取用户ID
     *
     * @param phone
     * @return
     */
    @RequestMapping(value = "/getPhoneUid", method = RequestMethod.POST)
    Response getPhoneUid(@RequestParam("phone") String phone);

    /**
     * 获取缓存中用户信息
     *
     * @param param
     */
    @RequestMapping(value = "/getTokenUser", method = RequestMethod.POST)
    public Response getTokenUser(@RequestBody Map<String, Object> param);

} // End interface UserRedisServiceApi
