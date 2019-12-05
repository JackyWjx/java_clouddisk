package com.jzb.redis.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.data.date.JzbDateStr;
import com.jzb.base.data.date.JzbDateUtil;
import com.jzb.base.message.JzbReturnCode;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import com.jzb.redis.config.CommonCacheKeys;
import com.jzb.redis.service.CommonRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Redis控制层
 *ComProject
 * @author Chad
 * @date 2019年7月20日
 */
@RestController
@RequestMapping(value = "/redis/common")
public class CommonRedisController {
    @Autowired
    private CommonRedisService redisService;

    /**
     * 像Redis服务器写入一个数据
     */
    @RequestMapping(value = "/cacheString/{key}/{value}", method = RequestMethod.GET)
    public Response cacheString(@PathVariable(value = "key") String key, @PathVariable(value = "value") String value) {
        Response result;
        try {
            result = redisService.getRedisService().setString(key, value) ? new Response(JzbReturnCode.HTTP_200, "OK") :
                    new Response(JzbReturnCode.HTTP_404, "FAILED");
        } catch (Exception ex) {
            ex.printStackTrace();
            result = new Response(JzbReturnCode.HTTP_404, "FAILED");
        }
        return result;
    } // End cacheString

    /**
     * 从Redis服务器0                                                                                                 中获取一个缓存数据。
     */
    @RequestMapping(value = "/getString/{key}", method = RequestMethod.GET)
    public Response getString(@PathVariable(value = "key") String key) {
        Response result;
        try {
            String value = redisService.getRedisService().getString(key);
            result = new Response(JzbReturnCode.HTTP_200, "OK");
            result.setResponseEntity(value);
        } catch (Exception ex) {
            ex.printStackTrace();
            result = new Response(JzbReturnCode.HTTP_404, "FAILED");
        }
        return result;
    } // End getString

    /**
     * 写入一个缓存数据，缓存时间(秒)参数key,value,time;其中time是缓存保存时间，单位秒
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     * @DateTime: 2019/9/16 15:33
     */
    @RequestMapping(value = "/cacheSmsCode", method = RequestMethod.POST)
    public Response cacheSmsCode(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String key = param.get("key").toString();
            String value = param.get("value").toString();
            int time = JzbDataType.getInteger(param.get("time"));
            result = redisService.getRedisService().setString(CommonCacheKeys.JZB_COM_PHONE + key, value, time) ? Response.getResponseSuccess() :
                    Response.getResponseError();
        } catch (Exception ex) {
            ex.printStackTrace();
            result = Response.getResponseError();
        }
        return result;
    } // End cacheSmsCode

    /**
     * 获取手机的验证码参数phone
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     * @DateTime: 2019/9/16 15:51
     */
    @RequestMapping(value = "/getPhoneCode", method = RequestMethod.POST)
    public Response getPhoneCode(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String key = JzbDataType.getString(param.get("phone"));
            String value = redisService.getRedisService().getString(CommonCacheKeys.JZB_COM_PHONE + key);
            result = new Response(JzbReturnCode.HTTP_200, "OK");
            result.setResponseEntity(value);
        } catch (Exception ex) {
            ex.printStackTrace();
            JzbTools.logError(ex);
            result = new Response(JzbReturnCode.HTTP_404, "FAILED");
        }
        return result;
    }

    /**
     * 往redis写入城市 json
     *
     * @param
     * @return
     * @author chenzhengduan
     * @Datatime 2019/9/29
     */
    @RequestMapping(value = "/cacheCity", method = RequestMethod.POST)
    public Response cacheCity(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            result = redisService.getRedisService().setString(param.get("key").toString(), param.get("value").toString()) ? new Response(JzbReturnCode.HTTP_200, "OK") :
                    new Response(JzbReturnCode.HTTP_404, "FAILED");
        } catch (Exception ex) {
            ex.printStackTrace();
            JzbTools.logError(ex);
            result = new Response(JzbReturnCode.HTTP_404, "FAILED");
        }
        return result;
    }

    /**
     * 往redis读取城市 json
     *
     * @param
     * @return
     * @author chenzhengduan
     * @Datatime 2019/9/29
     */
    @RequestMapping(value = "/getCityJson", method = RequestMethod.POST)
    public Response getCityJson(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String key = param.get("key").toString();
            String value = redisService.getRedisService().getString(key);
            result = new Response(JzbReturnCode.HTTP_200, "OK");
            result.setResponseEntity(value);
        } catch (Exception ex) {
            ex.printStackTrace();
            JzbTools.logError(ex);
            result = new Response(JzbReturnCode.HTTP_404, "FAILED");
        }
        return result;
    }

    /**
     * 往redis设置citylist
     * @param param
     * @return
     */
    @RequestMapping(value = "/setCityList", method = RequestMethod.POST)
    public Response setCityList(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            result = redisService.getRedisService().setMap(CommonCacheKeys.JZB_STSTEM_CITY_LIST, param) ? new Response(JzbReturnCode.HTTP_200, "OK") : new Response(JzbReturnCode.HTTP_404, "FAILED");
        } catch (Exception ex) {
            ex.printStackTrace();
            JzbTools.logError(ex);
            result = new Response(JzbReturnCode.HTTP_404, "FAILED");
        }
        return result;
    }

    /**
     * 往redis设置citylist
     * @param param
     * @return
     */
    @RequestMapping(value = "/getCityList", method = RequestMethod.POST)
    public Response getCityList(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String key="";

            if(param.get("key")==null||param.get("key").equals("null")){
                key="error";
            }else {
                key=param.get("key").toString();
            }
            String  mapValue = JzbDataType.getString(redisService.getRedisService().getMapValue(CommonCacheKeys.JZB_STSTEM_CITY_LIST, key));
            result=Response.getResponseSuccess();
            result.setResponseEntity(mapValue);
        } catch (Exception ex) {
            ex.printStackTrace();
            JzbTools.logError(ex);
            result = new Response(JzbReturnCode.HTTP_404, "FAILED");
        }
        return result;
    }

    /**
     * 像Redis服务器写入一个数据
     */
    @RequestMapping(value = "/cacheMap", method = RequestMethod.POST)
    public Response cacheMap(String key, String value) {
        Response result;
        try {
            result = redisService.getRedisService().setString(key, value) ? new Response(JzbReturnCode.HTTP_200, "OK") :
                    new Response(JzbReturnCode.HTTP_404, "FAILED");
        } catch (Exception ex) {
            ex.printStackTrace();
            result = new Response(JzbReturnCode.HTTP_404, "FAILED");
        }
        return result;
    } // End cacheMap
} // End class CommonRedisController
