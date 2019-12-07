package com.jzb.org.controller;

import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbPageConvert;
import com.jzb.base.util.JzbTools;
import com.jzb.org.service.TbDeptUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author sapientia
 * @Date 2019/12/5 16:08
 */

@RestController
@RequestMapping("/dept/user")
public class TbDeptUserController {

    @Autowired
    private TbDeptUserService tbDeptUserService;

    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(TbDeptUserController.class);
    /**
     * @Author sapientia
     * @Date 16:21 2019/12/5
     * @Description
     **/
    @RequestMapping(value = "/queryUsernameBydept", method = RequestMethod.POST)
    @CrossOrigin
    public Response queryUsernameBydept(@RequestBody Map<String, Object> param){
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/operate/reimburseSystem/queryUsernameBydept";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            if (JzbCheckParam.haveEmpty(param, new String[]{"pagesize", "pageno" })) {
                response = Response.getResponseError();
            } else {
                JzbPageConvert.setPageRows(param);

                List<Map<String, Object>> list = tbDeptUserService.queryUsernameBydept(param);
                for (Map<String, Object> usermap : list) {
                    if (usermap.get("uid") == param.get("uid")) {
                        list.remove(usermap);
                        break;
                    }
                }
                response = Response.getResponseSuccess(userInfo);
                PageInfo pageInfo = new PageInfo();
                pageInfo.setList(list);
                response.setPageInfo(pageInfo);
            }
        }catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "queryUsernameBydept Method", ex.toString()));
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
