package com.jzb.redis.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.data.code.JzbDataCheck;
import com.jzb.base.message.JzbReturnCode;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import com.jzb.redis.config.OrgCacheKeys;
import com.jzb.redis.config.UserCacheKeys;
import com.jzb.redis.service.UserRedisService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户相关数据缓存到Redis
 *
 * @author Chad
 * @date 2019年7月20日
 */
@RestController
@RequestMapping(value = "/redis/user")
public class UserCacheController {
    /**
     * 用户Redis服务对象
     */
    @Autowired
    private UserRedisService redisService;

    /**
     * 从Redis中读取一个用户ID的用户名称
     * 请求参数，uid
     *
     * @param param 请求参数
     */
    @RequestMapping(value = "/getNameById", method = RequestMethod.POST)
    public Response getNameById(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String name = null;
            Object uid = param.get("uid");
            if (uid != null) {
                Object obj = redisService.getRedisService().getMapValue(UserCacheKeys.JZB_USER_ID_TO_NAME, uid.toString());
                if (obj != null) {
                    name = obj.toString();
                }
                if (StringUtils.isEmpty(name)) {
                    result = new Response(JzbReturnCode.HTTP_404, "FAILED");
                } else {
                    result = new Response(JzbReturnCode.HTTP_200, "OK");
                    result.setResponseEntity(name);
                }
            } else {
                result = new Response(JzbReturnCode.HTTP_404, "FAILED");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            result = new Response(JzbReturnCode.HTTP_404, "FAILED");
        }
        return result;
    } // End getNameById

    /**
     * 缓存一个用户ID和用户名称
     * 请求参数 uid,name
     *
     * @param param 请求参数
     */
    @RequestMapping(value = "/cacheIdName", method = RequestMethod.POST)
    public Response cacheIdName(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String uid = param.get("uid").toString();
            String name = param.get("name").toString();
            redisService.getRedisService().setMapValue(UserCacheKeys.JZB_USER_ID_TO_NAME, uid, name);
            result = new Response(JzbReturnCode.HTTP_200, "OK");
        } catch (Exception ex) {
            ex.printStackTrace();
            result = new Response(JzbReturnCode.HTTP_404, "FAILED");
        }
        return result;
    } // End cacheIdName

    /**
     * 根据电话获取一个用户ID
     *
     * @param phone 请求参数
     */
    @RequestMapping(value = "/getPhoneUid", method = RequestMethod.POST)
    public Response getPhoneUid(@RequestParam("phone") String phone) {
        Response result;
        try {
            Object obj = redisService.getRedisService().getMapValue(UserCacheKeys.JZB_PHONE_TO_USER_ID, phone);
            String id = null;
            if (obj != null) {
                id = obj.toString();
            }
            if (StringUtils.isEmpty(id)) {
                result = new Response(JzbReturnCode.HTTP_404, "FAILED");
            } else {
                result = new Response(JzbReturnCode.HTTP_200, "OK");
                result.setResponseEntity(id);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            result = new Response(JzbReturnCode.HTTP_404, "FAILED");
        }
        return result;
    } // End cachePhoneUid

    /**
     * 根据电话缓存一个用户ID
     * 请求参数 uid,name
     *
     * @param param 请求参数
     */
    @RequestMapping(value = "/cachePhoneUid", method = RequestMethod.POST)
    public Response cachePhoneUid(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String phone = param.get("phone").toString();
            String uid = param.get("uid").toString();
            redisService.getRedisService().setMapValue(UserCacheKeys.JZB_PHONE_TO_USER_ID, phone, uid);
            result = new Response(JzbReturnCode.HTTP_200, "OK");
        } catch (Exception ex) {
            ex.printStackTrace();
            result = new Response(JzbReturnCode.HTTP_404, "FAILED");
        }
        return result;
    } // End getPhoneUid

    /**
     * 修改缓存中的过时用户信息
     * 请求参数 uid
     */
    @RequestMapping(value = "/updateUserInfo", method = RequestMethod.POST)
    public Response updateUserInfo(@RequestBody Map<String, Object> param) {
        Response response;
        try {
            String uid = JzbDataType.getString(param.get("uid"));
            for (Map.Entry<String, Object> entry : param.entrySet()) {
                if (entry.getValue() == null) {
                    entry.setValue("");
                }
                redisService.getRedisService().setMapValue(UserCacheKeys.JZB_USER_INFO_CACHE + uid, entry.getKey(), entry.getValue().toString());
            }
            response = Response.getResponseSuccess();
        } catch (Exception ex) {
            ex.printStackTrace();
            response = Response.getResponseError();
        }
        return response;
    }

    /**
     * 缓存用户信息
     * Author: Chad
     *
     * @param param
     */
    @RequestMapping(value = "/cacheUserInfo", method = RequestMethod.POST)
    public Response cacheUserInfo(@RequestBody Map<String, Object> param) {
        Response response;
        try {
            String token = param.get("token").toString();
            String phone = param.get("phone").toString();
            String uid = param.get("uid").toString();
            int timeout = JzbDataType.getInteger(param.get("timeout"));
            Map<String, Object> cacheInfo = new HashMap<>(param.size());
            for (Map.Entry<String, Object> entry : param.entrySet()) {
                cacheInfo.put(entry.getKey(), JzbDataType.getString(entry.getValue()));
            }
            cacheInfo.put("tokentime", System.currentTimeMillis() + "");

            // 缓存用户信息
            redisService.getRedisService().setMap(UserCacheKeys.JZB_USER_INFO_CACHE + uid, cacheInfo);

            // 缓存TOKEN
            redisService.getRedisService().setString(UserCacheKeys.JZB_USER_TOKEN_CACHE + token, uid, timeout);

            // 缓存电话与ID的关键
            redisService.getRedisService().setMapValue(UserCacheKeys.JZB_PHONE_TO_USER_ID, phone, uid);
            response = Response.getResponseSuccess();
        } catch (Exception ex) {
            ex.printStackTrace();
            response = Response.getResponseError();
        }
        return response;
    } // End cacheUserInfo

    /**
     * 更新缓存中用户Token
     *
     * @param param
     */
    @RequestMapping(value = "/updateUserToken", method = RequestMethod.POST)
    public Response updateUserToken(@RequestBody Map<String, Object> param) {
        Response response;
        try {
            JzbTools.logInfo("========================>>", "updateUserToken", param.toString());
            String token = param.get("token").toString();
            String uid = param.get("uid").toString();
            String session = param.get("session").toString();
            int timeout = JzbDataType.getInteger(param.get("timeout"));

            // 获取用户Token
            Object oldToken = redisService.getRedisService().getMapValue(UserCacheKeys.JZB_USER_INFO_CACHE + uid, "token");

            // 删除历史的Token
            redisService.getRedisService().comRemoveKey(UserCacheKeys.JZB_USER_TOKEN_CACHE + oldToken);

            // 缓存TOKEN
            redisService.getRedisService().setString(UserCacheKeys.JZB_USER_TOKEN_CACHE + token, uid, timeout);

            // 缓存用户信息
            redisService.getRedisService().setMapValue(UserCacheKeys.JZB_USER_INFO_CACHE + uid, "token", token);
            redisService.getRedisService().setMapValue(UserCacheKeys.JZB_USER_INFO_CACHE + uid, "session", session);
            redisService.getRedisService().setMapValue(UserCacheKeys.JZB_USER_INFO_CACHE + uid, "tokentime", System.currentTimeMillis() + "");
            response = Response.getResponseSuccess();
        } catch (Exception ex) {
            ex.printStackTrace();
            response = Response.getResponseError();
        }
        return response;
    } // End updateUserToken

    /**
     * 从Redis中读取一个用户ID的用户基本信息
     * 存放了用户tb_user_list表中已有的信息
     * 请求参数，uid
     *
     * @param param 请求参数
     */
    @RequestMapping(value = "/getCacheUserInfo")
    public Response getCacheUserInfo(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String uid = JzbDataType.getString(param.get("uid"));
            if (!JzbTools.isEmpty(uid)) {
                Map<Object, Object> userInfo = redisService.getRedisService().getMap(UserCacheKeys.JZB_USER_INFO_CACHE + uid);
                result = Response.getResponseSuccess();
                result.setResponseEntity(userInfo);
            } else {
                result = Response.getResponseError();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            result = Response.getResponseError();
        }
        return result;
    } // End getCacheUserInfo

    /**
     * 从Redis中读取一个用户ID的用户基本信息
     * 请求参数，uid
     *
     * @param param 请求参数
     */
    @RequestMapping(value = "/getTokenUser")
    public Response getTokenUser(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String token = param.containsKey("token") ? param.get("token").toString() : "";
            JzbTools.logInfo("=====================>> getTokenUser", token, param.toString());
            if (!JzbTools.isEmpty(token)) {
                // 获取TOKEN对应的用户ID，再根据UID获取用户对象
                String uid = redisService.getRedisService().getString(UserCacheKeys.JZB_USER_TOKEN_CACHE + token);
                if (!JzbTools.isEmpty(uid)) {
                    Map<Object, Object> userInfo = redisService.getRedisService().getMap(UserCacheKeys.JZB_USER_INFO_CACHE + uid);
                    result = Response.getResponseSuccess();
                    result.setResponseEntity(userInfo);

                    // 检查TOKEN超时。
                    long addTime = JzbDataType.getLong(userInfo.get("tokentime"));
                    int timeout = JzbDataType.getInteger(userInfo.get("timeout"));
                    timeout = timeout == 0 ? 1800 : timeout;

                    if (System.currentTimeMillis() - addTime >= timeout * 1000) {
                        // 更新TOKEN
                        long currTime = System.currentTimeMillis();
                        String newToken = JzbDataCheck.Md5(uid + currTime);

                        // 删除历史的Token
                        redisService.getRedisService().comRemoveKey(UserCacheKeys.JZB_USER_TOKEN_CACHE + token);

                        // 缓存TOKEN
                        JzbTools.logInfo("==============================>>", UserCacheKeys.JZB_USER_TOKEN_CACHE + newToken, uid, timeout);
                        redisService.getRedisService().setString(UserCacheKeys.JZB_USER_TOKEN_CACHE + newToken, uid, timeout);

                        // 缓存用户信息
                        redisService.getRedisService().setMapValue(UserCacheKeys.JZB_USER_INFO_CACHE + uid, "token", newToken);
                        redisService.getRedisService().setMapValue(UserCacheKeys.JZB_USER_INFO_CACHE + uid, "tokentime", currTime + "");
                        result.setToken(newToken);
                    } else {
                        // 缓存TOKEN的超时时间
                        redisService.getRedisService().setString(UserCacheKeys.JZB_USER_TOKEN_CACHE + token, uid, timeout);
                    }
                    userInfo.remove("token");
                    userInfo.remove("tokentime");
                    userInfo.remove("timeout");
                    userInfo.remove("session");
                } else {
                    result = Response.getResponseTimeout();
                }
            } else {
                result = Response.getResponseError();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            result = Response.getResponseError();
        }
        return result;
    } // End getTokenUser


    /**
     * 判断是否存在用户信息
     * 请求参数，param
     *
     * @param param 请求参数
     * @author kuangbin
     */
    @RequestMapping(value = "/comHasUserKey", method = RequestMethod.POST)
    public Response comHasUserKey(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String uid = JzbDataType.getString(param.get("uid"));
            // 判断是否存在KEY
            boolean bl = redisService.getRedisService().comHasKey(UserCacheKeys.JZB_USER_INFO_CACHE + uid);
            result = Response.getResponseSuccess();
            result.setResponseEntity(bl);
        } catch (Exception ex) {
            ex.printStackTrace();
            result = Response.getResponseError();
        }
        return result;
    }
} // End class UserCacheController
