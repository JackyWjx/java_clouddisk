package com.jzb.auth.controller;

import com.jzb.auth.service.TbUserControlAuthService;
import com.jzb.base.data.JzbDataType;
import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/auth/userControlAuth")
public class TbUserControlAuthController {

    @Autowired
    private TbUserControlAuthService tbUserControlAuthService;

    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(TbUserControlAuthController.class);

    /**
     * 给单个用户授权
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/addUserControlAuth", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response addUserControlAuth(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/auth/userControlAuth/addUserControlAuth";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            // 如果参数都正确
            if (JzbCheckParam.allNotEmpty(param, new String[]{"list"})) {
                long time = System.currentTimeMillis();
                List<Map<String, Object>> list = (List<Map<String, Object>>) param.get("list");
                for (int i = 0, l = list.size(); i < l; i++) {
                    list.get(i).put("addtime", time);
                    list.get(i).put("updtime", time);
                    list.get(i).put("ouid", JzbDataType.getString(userInfo.get("uid")));
                }
                int count = tbUserControlAuthService.addUserControlAuth(list);
                response = count > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
            } else {
                logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "addUserControlAuth Method", "[param error] or [param is null]"));
                response = Response.getResponseError();

            }
        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "addUserControlAuth Method", ex.toString()));
        }
        if (userInfo != null) {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
                    userInfo.get("msgTag").toString(), "User Login Message"));
        } else {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
        }
        return response;
    }

    /**
     * 给单个用户授权
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/theCidIsExists", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response theCidIsExists(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/auth/userControlAuth/theCidIsExists";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            // 如果参数都正确
            if (JzbCheckParam.allNotEmpty(param, new String[]{"cid"})) {
                int count = tbUserControlAuthService.queryIsExistsCid(param);
                response = Response.getResponseSuccess();
                response.setResponseEntity(count);
            } else {
                logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "theCidIsExists Method", "[param error] or [param is null]"));
                response = Response.getResponseError();
            }
        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "theCidIsExists Method", ex.toString()));
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
