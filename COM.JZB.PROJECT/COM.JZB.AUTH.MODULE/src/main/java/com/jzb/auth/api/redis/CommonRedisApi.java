package com.jzb.auth.api.redis;

import com.jzb.base.message.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;


/**
 * @Description:
 * @Author dell
 * @Version 1.0
 * @Since 1.0
 * @Date: 2019/8/8 10:19
 */
@FeignClient(name = "jzb-redis")
@RequestMapping(value = "/redis/common")
public interface CommonRedisApi {
    /**
     * 从Redis服务器中获取手机号的验证码一个缓存数据。
     */
    @RequestMapping(value = "/getString/{key}", method = RequestMethod.GET)
    Response getString(@PathVariable(value = "key") String key);


    /**
     * 写入一个缓存数据，缓存时间(秒)参数key,value,time;其中time是缓存保存时间，单位秒
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     * @DateTime: 2019/9/16 15:30
     */
    @RequestMapping(value = "/cacheSmsCode", method = RequestMethod.POST)
    Response cacheSmsCode(@RequestBody Map<String, Object> param);

    /**
     * 获取手机的验证码参数phone
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     * @DateTime: 2019/9/16 15:51
     */
    @RequestMapping(value = "/getPhoneCode", method = RequestMethod.POST)
    public Response getPhoneCode(@RequestBody Map<String, Object> param);
    /**
     * 从Redis服务器中获取一个Map缓存数据。
     */
    @RequestMapping(value = "/getMap", method = RequestMethod.POST)
    Response getMap(@RequestBody Map<String, Object> param);
}
