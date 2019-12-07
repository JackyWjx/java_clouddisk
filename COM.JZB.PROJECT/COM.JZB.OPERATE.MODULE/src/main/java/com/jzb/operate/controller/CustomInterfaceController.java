package com.jzb.operate.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import com.jzb.operate.service.CustomInterfaceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author kuangbin
 * 自定义界面操作类
 * chenzhengduan 添加日志记录
 */
@RestController
@RequestMapping(value = "/operate/customInterface")
public class CustomInterfaceController {

    @Autowired
    private CustomInterfaceService customInterfaceService;

    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(CustomInterfaceController.class);

    /**
     * 中台首页-用户自定义界面1
     * 查询自定义界面数据
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getCustomInterface", method = RequestMethod.POST)
    @CrossOrigin
    public Response getCustomInterface(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/operate/customInterface/getCustomInterface";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            param.put("uid", JzbDataType.getString(userInfo.get("uid")));
            List<Map<String, Object>> record = customInterfaceService.getCustomInterface(param);
            result = Response.getResponseSuccess(userInfo);
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(record);
            result.setPageInfo(pageInfo);
        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getCustomInterface Method", ex.toString()));
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
     * 中台首页-用户自定义界面2
     * 修改自定义界面数据
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/modifyCustomInterface", method = RequestMethod.POST)
    @CrossOrigin
    public Response modifyCustomInterface(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/operate/customInterface/modifyCustomInterface";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            // 获取网关层前台传入的list
            List<Map<String, Object>> list = (List<Map<String, Object>>) param.get("list");
            long updTime = System.currentTimeMillis();
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> map = list.get(i);
                map.put("uid", userInfo.get("uid"));
                map.put("updtime", updTime);
            }
            result = customInterfaceService.modifyCustomInterface(list) > 0 ?
                    Response.getResponseSuccess(userInfo) : Response.getResponseError();
        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "modifyCustomInterface Method", ex.toString()));
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