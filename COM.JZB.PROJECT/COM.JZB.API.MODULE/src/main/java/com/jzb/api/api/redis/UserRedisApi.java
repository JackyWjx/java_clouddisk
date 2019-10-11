package com.jzb.api.api.redis;

import com.jzb.base.message.Response;
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
     * @param param
     */
    @RequestMapping(value = "/updateUserInfo", method = RequestMethod.POST)
    public Response updateUserInfo(Map<String, Object> param);

    /**
     * 缓存用户信息
     * @param param
     */
    @RequestMapping(value = "/cacheUserInfo", method = RequestMethod.POST)
    public Response cacheUserInfo(@RequestBody Map<String, Object> param);

    /**
     * 更新缓存中用户Token
     * @param param
     */
    @RequestMapping(value = "/updateUserToken", method = RequestMethod.POST)
    public Response updateUserToken(@RequestBody Map<String, Object> param);

    /**
     * 从Redis中读取一个用户ID的用户基本信息
     * 存放了用户tb_user_list表中已有的信息
     * 请求参数，uid
     *
     * @param param 请求参数
     */
    @RequestMapping(value = "/getCacheUserInfo", method = RequestMethod.POST)
    public Response getCacheUserInfo(@RequestBody Map<String, Object> param);

    /**
     * 获取缓存中用户信息
     * @param param
     */
    @RequestMapping(value = "/getTokenUser", method = RequestMethod.POST)
    public Response getTokenUser(@RequestBody Map<String, Object> param);


    /**
     * 根据联系方式获取用户ID
     *
     * @param phone
     * @return
     */
    @RequestMapping(value = "/getPhoneUid", method = RequestMethod.POST)
    Response getPhoneUid(@RequestParam("phone") String phone);
}
