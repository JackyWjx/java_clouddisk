package com.jzb.api.util;

import com.jzb.api.api.auth.UserAuthApi;
import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.JzbReturnCode;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: service类型公用类
 * @Author dell
 * @Version 1.0
 * @Since 1.0
 * @Date: 2019/9/17 19:16
 */
@Service
public class ApiToken {
    /**
     * 用户认证
     */
    @Autowired
    private UserAuthApi authApi;


    /**
     * 根据token获取用户userInfo
     *
     * @param token
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @Author: DingSC
     * @DateTime: 2019/9/18 9:06
     */
    public Map<String, Object> getUserInfoByToken(String token) {
        Map<String, Object> tokenParam = new HashMap<>(2);
        tokenParam.put("token", token);
        Response tokenRes = authApi.checkToken(tokenParam);
        Map<String, Object> userInfo = new HashMap<>(4);
        if (tokenRes.getServerResult().getResultCode() == JzbReturnCode.HTTP_200) {
            if (JzbDataType.isMap(tokenRes.getResponseEntity())) {
                userInfo = (Map<String, Object>) tokenRes.getResponseEntity();
                userInfo.put("token", tokenRes.getToken() == null ? "" : tokenRes.getToken());
                userInfo.put("session", tokenRes.getSession() == null ? "" : tokenRes.getSession());
            }
        }
        return userInfo;
    }
}
