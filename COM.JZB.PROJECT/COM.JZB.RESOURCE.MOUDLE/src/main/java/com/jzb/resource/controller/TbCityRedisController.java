package com.jzb.resource.controller;

import com.jzb.base.message.Response;
import com.jzb.resource.api.redis.TbCityRedis;
import com.jzb.resource.api.redis.TbGetCityApi;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping(value = "/resource/city")
public class TbCityRedisController {

    @Autowired
    private TbCityRedis tbCityRedis;


    @Autowired
    private TbGetCityApi tbGetCityApi;

    /**
     *  存redis操作 json
     * @return
     */
    @RequestMapping(value = "/setCity",method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response setCity(){
        Response response = tbGetCityApi.getCityList();
        Map<String, Object> map=(Map<String, Object>) response.getResponseEntity();
        String  object= JSONObject.fromObject(map).toString();
        Map<String,Object> map1=new HashMap<>();
        map1.put("key","jzb.system.city");
        map1.put("value",object);
        Response response1 = tbCityRedis.cacheCity(map1);
        System.out.println(111);
        return response1;
    }

    /**
     * 从redis取  json
     * @param param
     * @return
     */
    @RequestMapping(value = "/getCityJson",method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response getCityJson(@RequestBody Map<String, Object> param){
//        map1.put("key","jzb.system.city");
        Response response1 = tbCityRedis.getCityJson(param);
        return response1;
    }

    /**
     *  存redis操作 json
     * @return
     */
    @RequestMapping(value = "/setCityList",method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response setCityList(){
        Response response = tbGetCityApi.getCityListToo();
        Map<String, Object> map=(Map<String, Object>) response.getResponseEntity();
        Response response1 = tbCityRedis.setCityList(map);
        return response1;
    }

    /**
     *  存redis操作 json
     * @return
     */
    @RequestMapping(value = "/getCityList",method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response getCityList(@RequestBody Map<String, Object> param){
        Response response = tbCityRedis.getCityList(param);
        return response;
    }
}
