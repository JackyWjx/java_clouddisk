package com.jzb.api.service;

import com.jzb.api.api.auth.RoleAuthApi;
import com.jzb.api.api.auth.UserAuthApi;
import com.jzb.api.api.org.CompanyOrgApi;
import com.jzb.api.config.ApiConfigProperties;
import com.jzb.base.data.JzbDataType;
import com.jzb.base.io.http.JsoupHttpClient;
import com.jzb.base.io.http.JzbHttpMethod;
import com.jzb.base.message.JzbReturnCode;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户认证服务
 *
 * @author Chad
 * @date 2019年08月01日
 */
@Service
public class JzbUserAuthService {
    /**
     * 用户认证操作API
     */
    @Autowired
    private JzbEurekaService eurekaService;

    /**
     *
     */
    @Autowired
    private ApiConfigProperties apiConfig;

    /**
     * 用户接口
     */
    @Autowired
    private UserAuthApi userApi;

    @Autowired
    private CompanyOrgApi companyOrgApi;
    /**
     * 角色和角色组接口
     */
    @Autowired
    private RoleAuthApi roleAuthApi;

    /**
     * 用户登录
     *
     * @param user
     * @param pwd
     * @return
     */
    public String userLogin(String user, String pwd) {
        String result;
        String url = eurekaService.getServiceUrl("JZB-OAUTH") + "/login";

        // 设置消息头
        Map<String, String> head = new HashMap<>();
        head.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0");

        // 设置请求参数
        Map<String, String> param = new HashMap<>();
        param.put("username", user);
        param.put("password", pwd);

        // 请求并返回数据
        Map<String, String> loginRes = JsoupHttpClient.request(url, JzbHttpMethod.POST, head, null, param, apiConfig.getRequestTimeout());
        JzbTools.logInfo("==============================loginRes>> ", loginRes.toString());
        if (loginRes != null && loginRes.get("body").equals("SUCCESSED")) {
            result = loginRes.containsKey("SESSION") ? loginRes.get("SESSION") : loginRes.get("JSESSIONID");
        } else {
            result = "";
        }
        return result;
    } // End userLogin

    /**
     * 获取用户Token
     *
     * @param session
     * @param uid
     * @param idType
     * @param appId
     * @param appSecret
     * @return
     */
    public Map<String, String> getToken(String session, String uid, String idType, String appId, String appSecret) {
        Map<String, String> result;
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("session", session);
            param.put("uid", uid);
            param.put("idtype", idType);
            param.put("appid", appId);
            param.put("secret", appSecret);
            Response res = userApi.getToken(param);
            if (res.getServerResult().getResultCode() == JzbReturnCode.HTTP_200) {
                result = (Map<String, String>) res.getResponseEntity();
            } else {
                result = null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            result = null;
        }
        return result;
    } // End getToken

    /**
     * 发送注册短信
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     * @DateTime: 2019/9/16 10:56
     */
    public Response sendCode(Map<String, Object> param) {
        return userApi.sendCode(param);
    }

    /**
     * 注册的第二步操作创建用户
     *
     * @param param
     * @return
     */
    public Response addRegistration(Map<String, Object> param) {
        return userApi.addRegistration(param);
    }

    /**
     * 用户注册短信验证
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     * @DateTime: 2019/9/16 17:00
     */
    public Response userVerify(Map<String, Object> param) {
        return userApi.userVerify(param);
    }

    /**
     * 添加管理员到角色组
     *
     * @param map
     * @return int
     * @Author: DingSC
     * @DateTime: 2019/9/23 11:30
     */
    public Response addAdmin(Map<String, Object> map) {
        Response result;
        Map<String, Object> param = new HashMap<>(2);
        param.put("cid", map.get("cid"));
        //获取管理员id
        Response uidRes = companyOrgApi.getAdministrator(param);
        if (JzbDataType.isMap(uidRes.getResponseEntity())) {
            Map<String, Object> userInfo = (Map<String, Object>) uidRes.getResponseEntity();
            String uid = JzbDataType.getString(userInfo.get("uid"));
            if (JzbTools.isEmpty(uid)) {
                String cid = JzbDataType.getString(param.get("cid"));
                String crgId = cid + "1111";
                param.put("crgid", crgId);
                param.put("rrid", uid);
                param.put("rrtype", "4");
                result = roleAuthApi.addRoleRelation(param);
            } else {
                result = Response.getResponseError();
            }
        } else {
            result = Response.getResponseError();
        }
        return result;
    }
} // End class JzbUserAuthService

