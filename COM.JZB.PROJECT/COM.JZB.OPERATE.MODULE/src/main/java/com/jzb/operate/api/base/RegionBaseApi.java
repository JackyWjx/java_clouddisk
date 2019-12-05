package com.jzb.operate.api.base;

import com.jzb.base.message.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/***
 *  BASE数据库Config模块API访问
 */
@FeignClient(name = "jzb-config")
@RequestMapping(value = "/config/city")
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

    /**
     * @Author sapientia
     * @Description 获取省市县
     * @Date  12:00
     * @Param [params]
     * @return com.jzb.base.message.Response
     **/
    
    @RequestMapping(value = "/getCityList", method = RequestMethod.POST)
    @CrossOrigin
    public Response getCityJson(@RequestBody(required = false) Map<String, Object> params);
}
