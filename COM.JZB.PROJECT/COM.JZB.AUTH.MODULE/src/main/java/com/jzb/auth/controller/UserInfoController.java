package com.jzb.auth.controller;

import com.jzb.auth.service.UserInfoService;
import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: gongWei
 * @Date: Greated in
 * @Description: 用于业务操作时获取业务关联的用户信息
 * @version: 0.0.1
 */
@RestController
@RequestMapping(value = "/userInfo")
public class UserInfoController {

    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(UserInfoController.class);

    @Autowired
    UserInfoService userInfoService;

    /**
     * 根据uid集合获取多个用户名称
     * @param param
     * @return
     */
    @RequestMapping(value = "/getUsernameList",method = RequestMethod.POST)
    public Response getUsernameList(@RequestBody Map<String,Object> param){
        Response response;
        Map<String, Object> userInfo = null;
        boolean flag = true;
        String  api="/userInfo/getUsernameList";
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger( api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger( api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            String unames = userInfoService.getUsernameByUids(param);
            response = Response.getResponseSuccess((Map<String, Object>) param.get("userinfo"));
            response.setResponseEntity(unames);

        } catch (Exception e) {
            flag=false;
            // 返回错误
            JzbTools.logError(e);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getUsernameList Method", e.toString()));
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
