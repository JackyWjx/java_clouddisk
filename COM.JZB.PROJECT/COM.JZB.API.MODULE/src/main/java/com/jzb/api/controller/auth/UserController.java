package com.jzb.api.controller.auth;

import com.jzb.api.api.auth.UserAuthApi;
import com.jzb.api.api.org.CompanyUserApi;
import com.jzb.api.config.ApiConfigProperties;
import com.jzb.api.service.CompanyService;
import com.jzb.api.service.JzbUserAuthService;
import com.jzb.api.util.ApiToken;
import com.jzb.base.data.JzbDataType;

import com.jzb.base.data.code.JzbDataCheck;
import com.jzb.base.message.JzbReturnCode;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jzb.base.message.Response;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 认证层业务控制层
 *
 * @author Chad
 * @date 2019年7月7日
 */
@RestController
@RequestMapping(value = "/api/auth")
public class UserController {
    @Autowired
    private ApiConfigProperties config;

    /**
     * 用户认证服务
     */
    @Autowired
    private JzbUserAuthService authService;

    /**
     * 用户认证
     */
    @Autowired
    private UserAuthApi authApi;

    @Autowired
    private ApiToken apiToken;

    @Autowired
    private CompanyUserApi companyUserApi;

    /**
     * 企业服务
     */
    @Autowired
    private CompanyService companyService;

    /**
     * 用户登录
     * 登录参数：appid/secret/uid/idtype/pwd
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/userLogin", method = RequestMethod.POST)
    @CrossOrigin
    public Response userLogin(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            JzbTools.logInfo("===========================>>", "Receive", param.toString());
            String appId = param.get("appid").toString();
            String appSecret = param.get("secret").toString();
            String uid = param.get("uid").toString();
            String idType = param.get("idtype").toString();
            String pwd = param.get("pwd").toString();

            // 获取SESSION
            String session = authService.userLogin(uid, pwd);
            JzbTools.logInfo("=============>>", "SESSION", session);
            // 验证appid
            if (!JzbTools.isEmpty(session)) {
                Map<String, String> token = authService.getToken(session, uid, idType, appId, appSecret);
                JzbTools.logInfo("==================>>", "TOKEN", token == null ? "NULL" : token.toString());
                if (token != null) {
                    result = Response.getResponseSuccess();
                    result.setToken(token.get("token"));
                    result.setSession(token.get("session"));
                    token.remove("token");
                    token.remove("session");
                    result.setResponseEntity(token);
                } else {
                    JzbTools.logError("===========================>>", "Get Token Failed");
                    result = Response.getResponseError();
                }
            } else {
                // 错误
                result = Response.getResponseError();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            result = Response.getResponseError();
        }
        return result;
    } // End userLogin

    /**
     * 用户Token登录
     * 登录参数：token
     *
     * @return
     */
    @RequestMapping(value = "/checkToken", method = RequestMethod.POST)
    @CrossOrigin
    public Response checkToken(@RequestHeader("token") String token) {
        Response result;
        try {
            Map<String, Object> tokenParam = new HashMap<>();
            tokenParam.put("token", token);
            Response tokenRes = authApi.checkToken(tokenParam);
            if (tokenRes.getServerResult().getResultCode() == JzbReturnCode.HTTP_200) {
                Map<String, Object> userInfo = (Map<String, Object>) tokenRes.getResponseEntity();
                result = Response.getResponseSuccess();
                result.setToken(tokenRes.getToken() == null ? "" : tokenRes.getToken());
                result.setSession(tokenRes.getSession() == null ? "" : tokenRes.getSession());
                userInfo.remove("session");
                userInfo.remove("token");
                result.setResponseEntity(userInfo);
            } else {
                // 返回超时
                result = Response.getResponseTimeout();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            result = Response.getResponseError();
        }
        return result;
    } // End checkToken

    /**
     * 注册专用发送短信
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     * @DateTime: 2019/9/16 11:06
     */
    @RequestMapping(value = "/sendCode", method = RequestMethod.POST)
    @CrossOrigin
    public Response sendCode(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            result = authService.sendCode(param);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 注册的第二步操作创建用户
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     * @DateTime: 2019/9/16 16:31
     */
    @PostMapping("/addRegistration")
    @CrossOrigin
    public Response addRegistration(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            result = authService.addRegistration(param);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 用户注册短信验证
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     * @DateTime: 2019/9/16 17:01
     */
    @PostMapping("/userVerify")
    @CrossOrigin
    public Response userVerify(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            result = authService.userVerify(param);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * CRM-单位用户-所有用户-单位列表
     * 点击添加单位时加入单位和用户并发送消息提醒
     *
     * @param param
     * @Author: Kuang Bin
     * @DateTime: 2019/9/16 16:31
     */
    @PostMapping("/addRegistrationCompany")
    @CrossOrigin
    public Response addRegistrationCompany(@RequestBody Map<String, Object> param, @RequestHeader(value = "token") String token) {
        Response result;
        try {
            Map<String, Object> userInfo = apiToken.getUserInfoByToken(token);
            if (userInfo.size() > 0) {
                param.put("userinfo", userInfo);
                param.put("uid", userInfo.get("uid"));
                // 获取随机密码
                String passwd = "*jzb" + JzbRandom.getRandomNum(3);
                param.put("passwd", JzbDataCheck.Md5(passwd).toLowerCase(Locale.ENGLISH));
                System.out.println(passwd);
                System.out.println(JzbDataCheck.Md5(passwd));
                System.out.println(JzbDataCheck.Md5(passwd).toLowerCase(Locale.ENGLISH));
                param.put("password", passwd);
                // 创建用户返回用户UID
                result = authService.addRegistration(param);
                Object objUser = result.getResponseEntity();
                // 判断返回的是否是MAP
                if (JzbDataType.isMap(objUser)) {
                    Map<String, Object> map = (Map<String, Object>) objUser;
                    // 判断map中是否包含uid
                    if (!JzbDataType.isEmpty(JzbDataType.getString(map.get("uid")))) {
                        // 加入状态,1为创建单位
                        param.put("type", "1");
                        result = companyService.addCompany(param);
                        Object objCompany = result.getResponseEntity();
                        // 判断返回的是否是MAP
                        if (JzbDataType.isMap(objCompany)) {
                            Map<String, Object> mapCompany = (Map<String, Object>) objCompany;
                            // 判断map中是否包含uid
                            if (!JzbDataType.isEmpty(JzbDataType.getString(mapCompany.get("cid")))) {
                                Response send = companyUserApi.sendRemind(param);
                                result = Response.getResponseSuccess(userInfo);
                                // 获取短信接口返回值并加入到此接口返回值中
                                result.setResponseEntity(send.getResponseEntity());
                            }
                        }
                    }
                } else {
                    result = Response.getResponseError();
                }
            } else {
                result = Response.getResponseError();
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 添加管理员到管理角色组
     *
     * @param param
     * @param token
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     * @DateTime: 2019/9/23 15:05
     */
    @RequestMapping(value = "/addAdmin", method = RequestMethod.POST)
    @CrossOrigin
    public Response addAdmin(@RequestBody Map<String, Object> param, @RequestHeader(value = "token") String token) {
        Response result;
        try {
            String[] str = {"cid"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                Map<String, Object> userInfo = apiToken.getUserInfoByToken(token);
                if (userInfo.size() > 0) {
                    param.put("userinfo", userInfo);
                    result = authService.addAdmin(param);
                } else {
                    result = Response.getResponseError();
                }
            } else {
                result = Response.getResponseError();
            }
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }
} // End class UserController
