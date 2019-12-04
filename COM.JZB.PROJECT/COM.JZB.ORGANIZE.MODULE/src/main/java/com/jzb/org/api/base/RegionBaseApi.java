package com.jzb.org.api.base;

import com.jzb.base.message.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/***
 *  BASE数据库Config模块API访问
 */
@FeignClient(name = "jzb-config")
@RequestMapping(value = "/config/city")
@Repository
public interface RegionBaseApi {
    /**
     * 根据地区名称获取地区ID信息
     * @param param
     */
    @RequestMapping(value = "/getRegionID", method = RequestMethod.POST)
    public Response getRegionID(Map<String, Object> param);

    /**
     * 根据地区ID获取地区信息
     * @param param
     */
    @RequestMapping(value = "/getRegionInfo", method = RequestMethod.POST)
    public Response getRegionInfo(Map<String, Object> param);
}
