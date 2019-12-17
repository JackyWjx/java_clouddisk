package com.jzb.org.controller;


import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbPageConvert;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.org.service.EvaluationMethodService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 评标方法增删改
 *
 * @author wang jixiang
 * @Date 2019.12.7
 */
@RestController
@RequestMapping("/org/evaluationMethod")
public class EvaluationMethodController {
    @Autowired
    private EvaluationMethodService evaluationMethodService;

    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(EvaluationMethodController.class);


    /**
     * 获取评标方法的所有数据
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getEvaluationMethod", method = RequestMethod.POST)
    @CrossOrigin
    public Response getEvaluationMethod(@RequestBody(required = false) Map<String, Object> param) {
        Response response;
        boolean flag = true;
        String api = "/org/evaluationMethod/getEvaluationMethod";
        Map<String, Object> userInfo = null;
        try {
            userInfo = (Map<String, Object>) param.get("userinfo");
            // 如果获取参数userinfo不为空的话
            if (param.get("userinfo") != null) {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.error(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            //所传参数不为空，进行分页
            if (JzbCheckParam.haveEmpty(param, new String[]{"pageno", "pagesize"})) {
                response = Response.getResponseError();
                logger.error(JzbLoggerUtil.getApiLogger(api, "2", "ERROR",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User select EvaluationMethod"));
            } else {
                JzbPageConvert.setPageRows(param);
                List<Map<String, Object>> evaluationMethods = evaluationMethodService.queryEvaluationMethod(param);
                response = Response.getResponseSuccess(userInfo);
                // 定义pageinfo
                PageInfo pi = new PageInfo();
                pi.setList(evaluationMethods);
                // 如果有一个指定参数不为空，则返回list.size()  否则返回总数
                pi.setTotal(evaluationMethodService.quertTenderTypeCount(param));
                response.setPageInfo(pi);
            }
        } catch (Exception e) {
            flag = false;
            JzbTools.logError(e);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getEvaluationMethod Method", "[评标方法查询异常]"));
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
     * 增加评标方法
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/addEvaluationMethod", method = RequestMethod.POST)
    @CrossOrigin
    public Response addEvaluationMethod(@RequestBody Map<String, Object> param) {
        Response response;
        boolean flag = true;
        String api = "/org/evaluationMethod/addEvaluationMethod";
        Map<String, Object> userInfo = null;
        try {
            userInfo = (Map<String, Object>) param.get("userinfo");
            // 如果获取参数userinfo不为空的话
            if (param.get("userinfo") != null) {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.error(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            param.put("adduid", userInfo.get("uid"));
            param.put("addtime", System.currentTimeMillis());
            param.put("status", "1");
            param.put("typeid", Integer.parseInt(JzbRandom.getRandomNum(5)));
            evaluationMethodService.addEvaluationMethod(param);
            response = Response.getResponseSuccess(userInfo);
        } catch (Exception e) {
            flag = false;
            JzbTools.logError(e);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getEvaluationMethod Method", "[增加招标方法异常]"));
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
     * 删除评标方法
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/delEvaluationMethod", method = RequestMethod.POST)
    @CrossOrigin
    public Response delEvaluationMethod(@RequestBody Map<String, Object> param) {
        Response response;
        String api = "/org/evaluationMethod/delEvaluationMethod";
        Map<String, Object> userInfo = null;
        boolean flag = true;
        try {
            userInfo = (Map<String, Object>) param.get("userinfo");
            // 如果获取参数userinfo不为空的话
            if (param.get("userinfo") != null) {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.error(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            response = evaluationMethodService.delEvaluationMethod(param) > 0 ?
                    Response.getResponseSuccess(userInfo) : Response.getResponseError();
        } catch (Exception e) {
            flag = false;
            JzbTools.logError(e);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getEvaluationMethod Method", "[评标方法删除异常]"));
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
     * 修改评标方法
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/putEvaluationMethod", method = RequestMethod.POST)
    @CrossOrigin
    public Response putEvaluationMethod(@RequestBody Map<String, Object> param) {
        Response response;
        boolean flag =true;
        String api = "/org/evaluationMethod/putEvaluationMethod";
        Map<String, Object> userInfo = null;
        try {
            userInfo = (Map<String, Object>) param.get("userinfo");
            // 如果获取参数userinfo不为空的话
            if (param.get("userinfo") != null) {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.error(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            param.put("upduid", userInfo.get("uid"));
            param.put("updtime", System.currentTimeMillis());
            response = evaluationMethodService.putEvaluationMethod(param) > 0 ?
                    Response.getResponseSuccess(userInfo) : Response.getResponseError();
        } catch (Exception e) {
            flag = false;
            JzbTools.logError(e);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getEvaluationMethod Method", "[更新评标方法异常]"));
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
