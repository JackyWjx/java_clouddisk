package com.jzb.auth.api.redis;

import com.jzb.base.message.Response;
import org.json.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/***
 *  redis数据库
 */
@FeignClient(name = "jzb-redis")
@RequestMapping(value = "/redis/user")
public interface UserRedisApi {
    /**
     * 更新缓存中的过时用户信息
     *
     * @param param
     */
    @RequestMapping(value = "/updateUserInfo", method = RequestMethod.POST)
    public Response updateUserInfo(Map<String, Object> param);

    /**
     * 缓存用户信息
     *
     * @param param
     */
    @RequestMapping(value = "/cacheUserInfo", method = RequestMethod.POST)
    public Response cacheUserInfo(@RequestBody Map<String, Object> param);

    /**
     * 更新缓存中用户Token
     *
     * @param param
     */
    @RequestMapping(value = "/updateUserToken", method = RequestMethod.POST)
    public Response updateUserToken(@RequestBody Map<String, Object> param);

    /**
     * 获取缓存中用户信息
     *
     * @param param
     */
    @RequestMapping(value = "/getCacheUserInfo", method = RequestMethod.POST)
    public Response getCacheUserInfo(@RequestBody Map<String, Object> param);

    /**
     * 获取缓存中用户信息
     *
     * @param param
     */
    @RequestMapping(value = "/getTokenUser", method = RequestMethod.POST)
    public Response getTokenUser(@RequestBody Map<String, Object> param);

    /**
     * 判断在缓存中是否存在对应的key值
     */
    @RequestMapping(value = "/comHasUserKey", method = RequestMethod.POST)
    Response comHasUserKey(@RequestBody Map<String, Object> param);

    /**
     * 根据电话缓存一个用户ID
     * 请求参数 uid,name
     *
     * @param param 请求参数
     *
     */
    @RequestMapping(value = "/cachePhoneUid", method = RequestMethod.POST)
    public Response cachePhoneUid(@RequestBody Map<String, Object> param);

    /**
     * 根据联系方式获取用户ID
     *
     * @param phone
     * @return
     */
    @RequestMapping(value = "/getPhoneUid", method = RequestMethod.POST)
    Response getPhoneUid(@RequestParam("phone") String phone);
}
