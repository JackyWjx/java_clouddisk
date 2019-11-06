package com.jzb.operate.controller;

import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.operate.service.TbResourceViewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author chenzhengduan
 * 活动浏览（无登录）
 */
@RestController
@RequestMapping(value = "/resourceView")
public class TbResourceViewController {

    @Autowired
    private TbResourceViewService tbResourceViewService;

    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(TbResourceViewController.class);

    /**
     * 浏览
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/addResourceView", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response addResourceVies(@RequestBody Map<String, Object> params) {
        Response result;
        Map<String, Object> userInfo = null;
        String  api="/resourceView/addResourceView";
        boolean flag = true;
        try {
            if (params.get("userinfo") != null) {
                userInfo = (Map<String, Object>) params.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger( api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger( api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            // 验证指定参数为空则返回error
            if (JzbCheckParam.haveEmpty(params,new String[]{"actid","ouid"})) {
                result = Response.getResponseError();
                logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "addResourceVies Method", "[param error] or [param is null]"));
            } else {
                params.put("restype", "R0001");
                // 获取是否存在
                int isAlready = tbResourceViewService.queryIsAlreadyView(params);
                // 判断如果该用户点过赞了就不允许点了
                if (isAlready <= 0) {
                    // 添加时间
                    params.put("addtime", System.currentTimeMillis());
                    // 添加结果
                    int count = tbResourceViewService.addResourceView(params);
                    if (count > 0) {
                        // 定义返回结果
                        result = Response.getResponseSuccess(userInfo);
                    } else {
                        result = Response.getResponseError();
                    }
                } else {
                    result = Response.getResponseError();
                }
            }
        } catch (Exception e) {
            flag=false;
            // 打印异常信息
            e.printStackTrace();
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "addResourceVies Method", e.toString()));
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