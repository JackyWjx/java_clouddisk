package com.jzb.org.controller;

import com.alibaba.fastjson.JSON;
import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbPageConvert;
import com.jzb.base.util.JzbTools;
import com.jzb.org.api.redis.TbCityRedisApi;
import com.jzb.org.service.CommonUserService;
import com.jzb.org.util.SetPageSize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @描述
 * @创建人 chenhui
 * @创建时间 2019/11/19
 * @修改人和其它信息
 */
@RestController
@RequestMapping("/orgCommon")
public class CommonUserController {
    @Autowired
    CommonUserService userService;

    /**
     * 查询redis缓存地区对象
     */
    @Autowired
    private TbCityRedisApi tbCityRedisApi;



    // 新建公海用户
    @RequestMapping("/addCommonUser")
    public Response addCommonUser(@RequestBody Map<String,Object> paramp){
        Response response;
        try {
            Map<String,Object> userinfo = (Map<String, Object>) paramp.get("userinfo");
            paramp.put("uid",userinfo.get("uid"));
            // 添加公海用户
            int count = userService.addCommUser(paramp);
            response = count > 0 ? Response.getResponseSuccess(userinfo):Response.getResponseError();
        } catch (Exception e) {
            JzbTools.logError(e);
            response = Response.getResponseError();
        }
        return response;
    }

    // 查询公海用户
    @RequestMapping("/queryCommonUser")
    public Response queryCommonUser(@RequestBody Map<String,Object> param){
        Response result;
        try {
            Map<String,Object> userinfo = (Map<String, Object>) param.get("userinfo");
            param.put("uid",userinfo.get("uid"));
            int count = JzbDataType.getInteger(param.get("count"));
            // 查询用户总数
            count = count > 0 ? count:userService.getCount(param);

            JzbPageConvert.setPageRows(param);
            List<Map<String, Object>> regionList = new ArrayList<>();
            if (!JzbDataType.isEmpty(JzbDataType.getString(param.get("province")))) {
                // 传入3代表查询县级地区
                if (!JzbDataType.isEmpty(JzbDataType.getString(param.get("county")))) {
                    // 定义存放每个省市县地区的map
                    Map<String, Object> regionMap = new HashMap<>();
                    // 加入县级地区id到参数对象中
                    regionMap.put("region", JzbDataType.getString(param.get("county")));
                    regionList.add(regionMap);
                    // 等于2代表传入的是市级地区ID
                } else if (!JzbDataType.isEmpty(JzbDataType.getString(param.get("city")))) {
                    // 添加查询地区的key
                    param.put("key", "jzb.system.city");

                    // 获取所有的地区信息
                    Response response = tbCityRedisApi.getCityJson(param);

                    // 将字符串转化为map
                    Map<String, Object> myJsonArray = (Map<String, Object>) JSON.parse(response.getResponseEntity().toString());
                    // 判断返回值中是否存在省信息
                    if (!JzbDataType.isEmpty(myJsonArray.get(JzbDataType.getString(param.get("province"))))) {
                        // 获取对应省下所有的城市信息
                        List<Map<String, Object>> myJsonList = (List<Map<String, Object>>) myJsonArray.get(JzbDataType.getString(param.get("province")));
                        for (int i = 0; i < myJsonList.size(); i++) {
                            // 获取省份下所有城市的信息
                            Map<String, Object> provinceMap = myJsonList.get(i);

                            // 如果为传入的城市ID则进行下一步
                            if (!JzbDataType.isEmpty(provinceMap.get(JzbDataType.getString(param.get("city"))))) {
                                // 获取城市下所有的县级信息
                                List<Map<String, Object>> countyMap = (List<Map<String, Object>>) provinceMap.get(JzbDataType.getString(param.get("city")));
                                Map<String, Object> county =  countyMap.get(0);
                                List<Map<String, Object>> cityList = (List<Map<String, Object>>) county.get("list");
                                for (int b = 0; b < cityList.size(); b++) {
                                    // 获取城市下单个的县级信息
                                    Map<String, Object> cityMap = cityList.get(b);

                                    // 定义存放每个省市县地区的map
                                    Map<String, Object> regionMap = new HashMap<>();

                                    // 将县级ID加入地区map对象中
                                    regionMap.put("region", JzbDataType.getString(cityMap.get("creaid")));
                                    regionList.add(regionMap);
                                }
                            }
                        }
                    }
                } else if (!JzbDataType.isEmpty(JzbDataType.getString(param.get("province")))) {
                    // 添加查询地区的key
                    param.put("key", "jzb.system.city");

                    // 获取所有的地区信息
                    Response response = tbCityRedisApi.getCityJson(param);

                    // 将字符串转化为map
                    Map<String, Object> myJsonArray = (Map<String, Object>) JSON.parse(response.getResponseEntity().toString());
                    if (!JzbDataType.isEmpty(myJsonArray.get(JzbDataType.getString(param.get("province"))))) {
                        List<Map<String, Object>> myJsonList = (List<Map<String, Object>>) myJsonArray.get(JzbDataType.getString(param.get("province")));
                        for (int i = 0; i < myJsonList.size(); i++) {
                            // 获取城市信息
                            Map<String, Object> provinceMap = myJsonList.get(i);
                            for (Map.Entry<String, Object> entry : provinceMap.entrySet()) {
                                if (!"list".equals(entry.getKey())) {
                                    String key = entry.getKey();
                                    // 定义存放每个省市县地区的map
                                    Map<String, Object> regionMap = new HashMap<>();
                                    regionMap.put("region", key);
                                    regionList.add(regionMap);
                                    List<Map<String, Object>> cityList = (List<Map<String, Object>>) entry.getValue();
                                    Map<String, Object> cityMap = cityList.get(0);
                                    List<Map<String, Object>> city = (List<Map<String, Object>>) cityMap.get("list");
                                    for (int k = 0; k < city.size(); k++) {
                                        // 获取城市下单个的县级信息
                                        Map<String, Object> cityMap1 = city.get(k);

                                        // 定义存放每个省市县地区的map
                                        Map<String, Object> region = new HashMap<>();

                                        // 将县级ID加入地区map对象中
                                        region.put("region", JzbDataType.getString(cityMap1.get("creaid")));
                                        regionList.add(region);
                                    }
                                }
                            }
                        }
                    }
                }
                // 将所有结果加入参数中传入
                param.put("list", regionList);
            }
            // 查询用户
            List<Map<String,Object>> list = userService.queryCommonUser(param);
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(list);
            pageInfo.setTotal(count);
            result = Response.getResponseSuccess(userinfo);
            result.setPageInfo(pageInfo);
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    // 修改公海用户
    @RequestMapping("/updCommonUser")
    public Response updCommonUser(@RequestBody Map<String,Object> paramp){
        Response response;
        try {
            int count = 0;
            Map<String,Object> userinfo = (Map<String, Object>) paramp.get("userinfo");
            if (!JzbTools.isEmpty(paramp.get("uid"))){
                 count = userService.updComUser(paramp);
            }
            response = count > 0 ? Response.getResponseSuccess(userinfo):Response.getResponseError();
        } catch (Exception e) {
            JzbTools.logError(e);
            response = Response.getResponseError();
        }
        return response;
    }

    // 删除公海用户
    @RequestMapping("/delUser")
    public Response delUser(@RequestBody Map<String,Object> paramp){
        Response response;
        try {
            Map<String,Object> userinfo = (Map<String, Object>) paramp.get("userinfo");
            // 删除公海用户
            int count = userService.delUser(paramp);
            response = count > 0 ? Response.getResponseSuccess(userinfo):Response.getResponseError();
        } catch (Exception e) {
            JzbTools.logError(e);
            response = Response.getResponseError();
        }
        return response;
    }



}
