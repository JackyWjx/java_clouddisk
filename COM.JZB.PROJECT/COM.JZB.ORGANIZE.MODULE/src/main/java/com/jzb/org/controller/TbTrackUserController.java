package com.jzb.org.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.Page;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbPageConvert;
import com.jzb.base.util.JzbTools;
import com.jzb.org.service.TbTrackUserService;
import com.jzb.org.util.SetPageSize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author chenhui
 * @description
 * @time2019/11/25
 * @other
 */
@RestController
@RequestMapping(value = "/orgTrack" ,method = RequestMethod.POST)
public class TbTrackUserController {

    @Autowired
    TbTrackUserService userService;

    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(TbCommonProjectInfoController.class);

    // 新建跟进人员记录
    @RequestMapping("/addTrackUser")
    public Response addTrackUser(@RequestBody Map<String,Object> param){
        Response response;
        try {
            Map<String,Object> userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("adduid",userInfo.get("uid"));
            int count = userService.addTrackUser(param);
            response = count > 0 ? Response.getResponseSuccess(userInfo):Response.getResponseSuccess();
        }catch (Exception e){
            JzbTools.logError(e);
            response = Response.getResponseError();
        }
        return response;
    }

    // 查询跟进人员记录信息
    @RequestMapping("/queryTrackUserList")
    public Response queryTrackUserList(@RequestBody Map<String,Object> param){
        Response response;
        try {
            Map<String,Object> userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("adduid",userInfo.get("uid"));
            // 查询跟进人员总记录数
            int count = JzbDataType.getInteger(param.get("count"));
            count = count < 0 ? 0:count;
            if (count == 0){
                count = userService.getTrackCount(param);
            }
            param.put("count",count);
            SetPageSize.setPagenoSize(param);
            // 查询跟进人员记录信息
            List<Map<String,Object>> list = userService.queryTrackUserList(param);
            response = Response.getResponseSuccess(userInfo);
            PageInfo pageInfo = new PageInfo();
            pageInfo.setTotal(count);
            pageInfo.setList(list);
            response.setPageInfo(pageInfo);
        } catch (Exception e) {
            JzbTools.logError(e);
            response = Response.getResponseError();
        }
        return response;
    }

    // 删除跟进人员记录信息
    @RequestMapping("/delTrackUser")
    public Response delTrackUser(@RequestBody Map<String,Object> param){
        Response response;
        try {
            Map<String,Object> userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("adduid",userInfo.get("uid"));
            int count =  userService.delTrackUser(param);
            response = count > 0 ? Response.getResponseSuccess(userInfo):Response.getResponseError();
        }catch (Exception e){
            JzbTools.logError(e);
            response = Response.getResponseError();
        }
        return response;
    }

    // 修改跟进人员记录信息
    @RequestMapping("/updTrackUser")
    public Response updTrackUser(@RequestBody Map<String,Object> param){
        Response response;
        try {
            Map<String,Object> userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("adduid",userInfo.get("uid"));
            int count =  userService.updTrackUser(param);
            response = count > 0 ? Response.getResponseSuccess(userInfo):Response.getResponseError();
        }catch (Exception e){
            JzbTools.logError(e);
            response = Response.getResponseError();
        }
        return response;
    }

    // 查询联系沟通信息
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
            List<Map<String ,Object>> list = userService.getInfo(param);
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
