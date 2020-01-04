package com.jzb.open.controller;


import com.jzb.base.data.JzbDataType;
import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbTools;
import com.jzb.open.service.OpenPageService;
import com.jzb.open.service.TbApplicationVerifyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/open/ApplicationVerify")
public class TbApplicationVerifyController {


    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(TbApplicationVerifyController.class);

    @Autowired
    private TbApplicationVerifyService tbApplicationVerifyService;


    /**
     * 提交应用列表到审批列表
     * @param param
     * @return
     */
    @RequestMapping(value = "/saveApplicationVerify",method = RequestMethod.POST)
    @CrossOrigin
    public Response saveApplicationVerify(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            //如果指定参数为空则返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"appid"})) {
                result = Response.getResponseError();
            } else {
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                param.put("adduid", userInfo.get("uid"));
                param.put("appline", JzbDataType.getInteger(param.get("appline")));
                int count = tbApplicationVerifyService.saveApplicationVerify(param);
                //获取用户信息
                //响应成功结果
                result = count > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
            }
        } catch (Exception e) {
            //打印错误信息
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 提交应用菜单列表到审批表
     * @param param
     * @return
     */
    @RequestMapping(value = "/saveApplicationMenuVerify", method = RequestMethod.POST)
    @CrossOrigin
    public Response saveApplicationMenuVerify(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            //如果指定参数为空则返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"appid"})) {
                result = Response.getResponseError();
            } else {
                //获取用户信息
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                param.put("adduid", userInfo.get("uid"));
                int count = tbApplicationVerifyService.saveApplicationMenuVerify(param);
                //获取用户信息
                //响应成功结果
                result = count > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
            }
        } catch (Exception e) {
            //打印错误信息
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 提交应用页面到审批表
     * @param param
     * @return
     */
    @RequestMapping(value = "/saveApplicationPageVerify", method = RequestMethod.POST)
    @CrossOrigin
    public Response saveApplicationPageVerify(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            //如果指定参数为空则返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"appid"})) {
                result = Response.getResponseError();
            } else {
                //获取用户信息
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                param.put("adduid", userInfo.get("uid"));
                int count = tbApplicationVerifyService.saveApplicationPageVerify(param);
                //获取用户信息
                //响应成功结果
                result = count > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
            }
        } catch (Exception e) {
            //打印错误信息
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 单点登录审批通过后显示出来的应用
     * @param param
     * @return
     */
    @RequestMapping(value = "/getApplicationPageVerify", method = RequestMethod.POST)
    @CrossOrigin
    public Response getApplicationPageVerify(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/open/ApplicationVerify/getApplicationPageVerify";
        boolean flag = true;
        try {
            // 如果获取参数userinfo不为空的话
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            //如果指定的参数为空的话 返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"devtype", "apptype"})) {
                result = Response.getResponseError();
            } else {
                List<Map<String, Object>> mapList = tbApplicationVerifyService.getApplicationPageVerify(param);
                //响应成功结果信息
                result = Response.getResponseSuccess(userInfo);
                result.setResponseEntity(mapList);
            }
        } catch (Exception ex) {
            //打印错误信息
            flag = false;
            JzbTools.logError(ex);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getApplicationPageVerify Method", ex.toString()));
        }
        if (userInfo != null) {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
                    userInfo.get("msgTag").toString(), "User Login Message"));
        } else {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
        }
        return result;
    }
}
