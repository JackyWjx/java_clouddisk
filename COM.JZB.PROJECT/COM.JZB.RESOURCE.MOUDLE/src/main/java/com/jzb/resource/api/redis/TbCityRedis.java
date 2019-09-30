package com.jzb.resource.api.redis;

import com.jzb.base.message.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@FeignClient(name = "jzb-redis")
@RequestMapping(value = "/redis/common")
@Repository
public interface TbCityRedis {

    /**
     * 更新缓存中的过时用户信息
     * @param
     */
    @RequestMapping(value = "/cacheCity", method = RequestMethod.POST)
    public Response cacheCity(@RequestBody Map<String,Object> param);

    /**
     * 更新缓存中的过时用户信息
     * @param
     */
    @RequestMapping(value = "/getCityJson", method = RequestMethod.POST)
    public Response getCityJson(@RequestBody Map<String,Object> param);


    /**
     * 更新缓存中的过时用户信息
     * @param
     */
    @RequestMapping(value = "/setCityList", method = RequestMethod.POST)
    public Response setCityList(@RequestBody Map<String,Object> param);


    /**
     * 更新缓存中的过时用户信息
     * @param
     */
    @RequestMapping(value = "/getCityList", method = RequestMethod.POST)
    public Response getCityList(@RequestBody Map<String,Object> param);


}
