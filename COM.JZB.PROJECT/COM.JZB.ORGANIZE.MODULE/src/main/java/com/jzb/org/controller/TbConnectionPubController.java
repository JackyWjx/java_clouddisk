package com.jzb.org.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbPageConvert;
import com.jzb.base.util.JzbTools;
import com.jzb.org.service.TbConnectionPubService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author chenhui
 * @description
 * @time2019/11/26
 * @other
 */
@RestController
@RequestMapping("/org/connction")
public class TbConnectionPubController {
    @Autowired
    TbConnectionPubService pubService;

    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(TbCommonProjectInfoController.class);

    // 查询发帖信息
    @RequestMapping("/getConnectionList")
    public Response getConnectionList(@RequestBody Map<String,Object> param){
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/org/connction/getConnectionList";
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
            param.put("adduid",userInfo.get("uid"));
            int count = JzbDataType.getInteger(param.get("count"));
            count = count < 0 ? 0:count;
            if (count == 0){
                // 查询发帖总数
                count = pubService.getConnectionCount(param);
            }
            JzbPageConvert.setPageRows(param);
            userInfo = (Map<String, Object>) param.get("userinfo");
            List<Map<String ,Object>> list = pubService.getConnectionList(param);
            PageInfo pageInfo = new PageInfo();
            pageInfo.setTotal(count);
            pageInfo.setList(list);
            result = Response.getResponseSuccess(userInfo);
            result.setPageInfo(pageInfo);
        }catch (Exception e){
            flag = false;
            JzbTools.logError(e);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getConnectionList Method", e.toString()));
        }
        if (userInfo != null) {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
                    userInfo.get("msgTag").toString(), "User Login Message"));
        } else {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
        }
        return result;
    }

    // 修改发帖信息
    @RequestMapping("/modifyConnectionList")
    public Response modifyConnectionList(@RequestBody Map<String,Object> param){
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/org/connction/modifyConnectionList";
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
            //如果指定参数为空的话，则返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"pubid"})) {
                result = Response.getResponseError();
            }else {
                userInfo = (Map<String, Object>) param.get("userinfo");
                // 修改发帖信息
                param.put("adduid",userInfo.get("uid"));
                int count = pubService.modifyConnectionList(param);
                // 设置响应成功的结果
                result = count > 0 ? Response.getResponseSuccess(userInfo):Response.getResponseError();
            }
        }catch (Exception e){
            flag = false;
            JzbTools.logError(e);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "modifyConnectionList Method", e.toString()));
        }
        if (userInfo != null) {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
                    userInfo.get("msgTag").toString(), "User Login Message"));
        } else {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
        }
        return result;
    }

    // 新建发帖信息
    @RequestMapping("/insertConnectionList")
    public Response insertConnectionList(@RequestBody Map<String,Object> param){
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/org/connction/insertConnectionList";
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
                userInfo = (Map<String, Object>) param.get("userinfo");
                // 新建发帖信息
                param.put("adduid",userInfo.get("uid"));
                param.put("uname",userInfo.get("cname"));
                int count = pubService.insertConnectionList(param);
                // 设置响应成功的结果
                result = count > 0 ? Response.getResponseSuccess(userInfo):Response.getResponseError();

        }catch (Exception e){
            flag = false;
            JzbTools.logError(e);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "insertConnectionList Method", e.toString()));
        }
        if (userInfo != null) {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
                    userInfo.get("msgTag").toString(), "User Login Message"));
        } else {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
        }
        return result;
    }

    // 删除发帖信息
    @RequestMapping("/removeConnectionList")
    public Response removeConnectionList(@RequestBody Map<String,Object> param){
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/org/connction/removeConnectionList";
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
            //如果指定参数为空的话，则返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"pubid"})) {
                result = Response.getResponseError();
            }else{
                userInfo = (Map<String, Object>) param.get("userinfo");
                // 新建发帖信息
                param.put("adduid",userInfo.get("uid"));
                int count = pubService.removeConnectionList(param);
                // 设置响应成功的结果
                result = count > 0 ? Response.getResponseSuccess(userInfo):Response.getResponseError();
            }
        }catch (Exception e){
            flag = false;
            JzbTools.logError(e);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "removeConnectionList Method", e.toString()));
        }
        if (userInfo != null) {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
                    userInfo.get("msgTag").toString(), "User Login Message"));
        } else {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
        }
        return result;
    }

    // 查询百度朋友圈发帖数目

    // 新建任务目标参数
    @RequestMapping("/insertTask")
    public Response insertTask(@RequestBody Map<String,Object> param){
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/org/connction/insertTask";
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
            userInfo = (Map<String, Object>) param.get("userinfo");
            // 新建发帖信息
            param.put("adduid",userInfo.get("uid"));
            int count = pubService.insertTask(param);
            // 设置响应成功的结果
            result = count > 0 ? Response.getResponseSuccess(userInfo):Response.getResponseError();

        }catch (Exception e){
            flag = false;
            JzbTools.logError(e);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "insertTask Method", e.toString()));
        }
        if (userInfo != null) {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
                    userInfo.get("msgTag").toString(), "User Login Message"));
        } else {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
        }
        return result;
    }

    // 查询任务目标参数
    @RequestMapping("/getTask")
    public Response getTask(@RequestBody Map<String,Object> param){
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/org/connction/getTask";
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
            param.put("adduid",userInfo.get("uid"));
            userInfo = (Map<String, Object>) param.get("userinfo");
            List<Map<String ,Object>> list = pubService.getTask(param);
            PageInfo pageInfo = new PageInfo();

            pageInfo.setList(list);
            result = Response.getResponseSuccess(userInfo);
            result.setPageInfo(pageInfo);
        }catch (Exception e){
            flag = false;
            JzbTools.logError(e);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getTask Method", e.toString()));
        }
        if (userInfo != null) {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
                    userInfo.get("msgTag").toString(), "User Login Message"));
        } else {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
        }
        return result;
    }

    // 修改发帖信息
    @RequestMapping("/modifyTask")
    public Response modifyTask(@RequestBody Map<String,Object> param){
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/org/connction/modifyTask";
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

                userInfo = (Map<String, Object>) param.get("userinfo");
                // 修改发帖信息
                param.put("upduid",userInfo.get("uid"));
                int count = pubService.modifyTask(param);
                // 设置响应成功的结果
                result = count > 0 ? Response.getResponseSuccess(userInfo):Response.getResponseError();

        }catch (Exception e){
            flag = false;
            JzbTools.logError(e);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "modifyTask Method", e.toString()));
        }
        if (userInfo != null) {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
                    userInfo.get("msgTag").toString(), "User Login Message"));
        } else {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
        }
        return result;
    }

    // 查询百度/朋友圈发帖信息
    @RequestMapping("/getInfo")
    public Response getInfo(@RequestBody Map<String,Object> param){
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/org/connction/getInfo";
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
            param.put("adduid",userInfo.get("uid"));
            userInfo = (Map<String, Object>) param.get("userinfo");
            List<Map<String ,Object>> list = pubService.getBaiduInfo(param);
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(list);
            result = Response.getResponseSuccess(userInfo);
            result.setPageInfo(pageInfo);
        }catch (Exception e){
            flag = false;
            JzbTools.logError(e);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getInfo Method", e.toString()));
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
