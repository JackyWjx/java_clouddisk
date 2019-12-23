package com.jzb.auth.controller;

import com.jzb.auth.service.AuthInfoService;
import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Author sapientia
 * @Date 2019/12/23 19:07
 * @Descrpition 获取用户信息
 */
@RestController
@RequestMapping("/auth/user")
public class AuthInfoController {

    @Autowired
    private AuthInfoService authInfoService;

    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(AuthInfoController.class);
    /**
     * @Author sapientia
     * @Date 19:08 2019/12/23
     * @Description 根据uids获取用户信息
     **/
    @RequestMapping(value = "/getUsersList",method = RequestMethod.POST)
    @CrossOrigin
    public Response getUsernameList(@RequestBody Map<String,Object> param){
        Response response;
        Map<String, Object> userInfo = null;
        boolean flag = true;
        String  api="/userInfo/getUsersList";
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger( api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger( api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            String unames = authInfoService.getUserByUids(param);
            response = Response.getResponseSuccess((Map<String, Object>) param.get("userinfo"));
            response.setResponseEntity(unames);

        } catch (Exception e) {
            flag=false;
            // 返回错误
            JzbTools.logError(e);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getUsersList Method", e.toString()));
        }
        if (userInfo != null) {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
                    userInfo.get("msgTag").toString(), "User Login Message"));
        } else {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
        }
        return response;
    }
}
