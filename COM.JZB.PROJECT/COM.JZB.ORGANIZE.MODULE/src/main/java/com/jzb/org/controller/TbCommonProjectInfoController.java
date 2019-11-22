package com.jzb.org.controller;

import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbTools;
import com.jzb.org.service.TbCommonProjectInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/org/CommonProjectInfo")
public class TbCommonProjectInfoController {

    @Autowired
    private TbCommonProjectInfoService tbCommonProjectInfoService;


    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(TbCommonProjectInfoController.class);

    /**
     * 项目情报的新增
     * @param param
     * @return
     */
    @RequestMapping(value = "/saveCommonProjectInfo", method = RequestMethod.POST)
    @CrossOrigin
    public Response saveCommonProjectInfo(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/org/CommonProjectInfo/saveCommonProjectInfo";
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
            if (JzbCheckParam.haveEmpty(param, new String[]{"projectid"})) {
                result = Response.getResponseError();
            } else {
                //获取用户信息
                userInfo = (Map<String, Object>) param.get("userinfo");
                //新建招标公告列表
                param.put("adduid", userInfo.get("uid"));
                int count = tbCommonProjectInfoService.saveCommonProjectInfo(param);
                //设置响应成功的结果
                result = count > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
            }
        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "saveCommonProjectInfo Method", ex.toString()));
        }
        if (userInfo != null) {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
                    userInfo.get("msgTag").toString(), "User Login Message"));
        } else {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
        }
        return result;
    }


    /**
     * 项目情报的修改
     *
     * @param param
     * @return
     */

    @RequestMapping(value = "/updateCommonProjectInfo", method = RequestMethod.POST)
    @CrossOrigin
    public Response updateCommonProjectInfo(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/org/ProjectRival/updateCommonProjectInfo";
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
            if (JzbCheckParam.haveEmpty(param, new String[]{"projectid"})) {
                result = Response.getResponseError();
            } else {
                //获取用户信息
                userInfo = (Map<String, Object>) param.get("userinfo");
                //新建招标公告列表
                param.put("upduid", userInfo.get("uid"));
                int count = tbCommonProjectInfoService.updateCommonProjectInfo(param);
                //设置响应成功的结果
                result = count > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
            }
        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "updateCommonProjectInfo Method", ex.toString()));
        }
        if (userInfo != null) {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
                    userInfo.get("msgTag").toString(), "User Login Message"));
        } else {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
        }
        return result;
    }



    /**
     * 项目情报的查询
     * @param param
     * @return
     */

    @RequestMapping(value = "/getCommonProjectInfo", method = RequestMethod.POST)
    @CrossOrigin
    public Response getCommonProjectInfo(@RequestBody Map<String, Object> param) {
        Response result;    
        Map<String, Object> userInfo = null;
        String api = "/org/ProjectRival/getCommonProjectInfo";
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
            if (JzbCheckParam.haveEmpty(param, new String[]{"projectid"})) {
                result = Response.getResponseError();
            } else {
                //获取查询出来的结果
                List<Map<String, Object>> list = tbCommonProjectInfoService.getCommonProjectInfo(param);
                //响应成功信息
                PageInfo pageInfo = new PageInfo();
                pageInfo.setList(list);
                result = Response.getResponseSuccess(userInfo);
                result.setPageInfo(pageInfo);
            }
        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getCommonProjectInfo Method", ex.toString()));
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
