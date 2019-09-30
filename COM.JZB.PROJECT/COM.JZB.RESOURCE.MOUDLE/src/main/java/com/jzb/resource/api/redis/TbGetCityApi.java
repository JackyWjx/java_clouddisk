package com.jzb.resource.api.redis;

import com.jzb.base.message.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/***
 *  redis数据库
 */
@FeignClient(name = "jzb-config")
@RequestMapping(value = "/config/city")
@Repository
public interface TbGetCityApi {
    /**
     * 更新缓存中的过时用户信息
     * @param
     */
    @RequestMapping(value = "/getCityList", method = RequestMethod.POST)
    public Response getCityList();


    /**
     * 更新缓存中的过时用户信息
     * @param
     */
    @RequestMapping(value = "/getCityListToo", method = RequestMethod.POST)
    public Response getCityListToo();

}


