package com.jzb.org.service;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.JzbReturnCode;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import com.jzb.org.api.redis.UserRedisServiceApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author dell
 * @Version 1.0
 * @Since 1.0
 * @Date: 2019/9/19 17:38
 */
@Service
public class OrgToken {
    @Autowired
    private UserRedisServiceApi userRedisApi;

    /**
     * 根据token获取用户userInfo
     * 返回map的size（）==0是获取失败
     *
     * @param token
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @Author: DingSC
     * @DateTime: 2019/9/18 9:06
     */
    public Map<String, Object> getUserInfoByToken(String token) {
        Map<String, Object> tokenParam = new HashMap<>(2);
        tokenParam.put("token", token);
        JzbTools.logInfo("=====================>> checkToken", tokenParam.toString());
        Map<String, Object> userInfo = new HashMap<>(4);
        if (tokenParam.containsKey("token")) {
            Response tokenRes = userRedisApi.getTokenUser(tokenParam);
            if (tokenRes.getServerResult().getResultCode() == JzbReturnCode.HTTP_200) {
                if (JzbDataType.isMap(tokenRes.getResponseEntity())) {
                    userInfo = (Map<String, Object>) tokenRes.getResponseEntity();
                    userInfo.put("token", tokenRes.getToken() == null ? "" : tokenRes.getToken());
                    userInfo.put("session", tokenRes.getSession() == null ? "" : tokenRes.getSession());
                }
            }
        }

        return userInfo;
    }
}
