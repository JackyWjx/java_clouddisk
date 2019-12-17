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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author chenhui
 * @description
 * @time2019/11/25
 * @other
 */
@RestController
@RequestMapping(value = "/orgTrack")
public class TbTrackUserController {

    @Autowired
    TbTrackUserService userService;

    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(TbTrackUserController.class);

    // 新建跟进人员记录
    @RequestMapping(value = "/addTrackUser",method = RequestMethod.POST)
    public Response addTrackUser(@RequestBody Map<String,Object> param){
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/orgTrack/getInfo";
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
            param.put("trackuname",userInfo.get("cname"));
            param.put("adduid",userInfo.get("uid"));
            response = userService.addTrackUser(param) > 0 ? Response.getResponseSuccess(userInfo):Response.getResponseSuccess();
        }catch (Exception e){
            flag = false;
            JzbTools.logError(e);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "queryTrackUserList Method", e.toString()));
        }
        if (userInfo != null) {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
                    userInfo.get("msgTag").toString(), "User Login Message"));
        } else {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
        }
        return response;
    }

    // 查询跟进人员记录信息
    @RequestMapping("/queryTrackUserList")
    public Response queryTrackUserList(@RequestBody Map<String,Object> param){
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/orgTrack/getInfo";
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
            // 查询跟进人员总记录数
            int count = JzbDataType.getInteger(param.get("count"));
            count = count < 0 ? 0:count;
            if (count == 0){
                count = userService.getTrackCount(param);
            }
            JzbPageConvert.setPageRows(param);
            // 查询跟进人员记录信息
            List<Map<String,Object>> list = null;
            if (!JzbTools.isEmpty(param.get("trackvalue"))){
                 list = userService.queryTrackUserList(param);
            }
                response = Response.getResponseSuccess(userInfo);
                PageInfo pageInfo = new PageInfo();
                pageInfo.setTotal(count);
                pageInfo.setList(list);
                response.setPageInfo(pageInfo);
        } catch (Exception e) {
            flag = false;
            JzbTools.logError(e);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "queryTrackUserList Method", e.toString()));
        }
        if (userInfo != null) {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
                    userInfo.get("msgTag").toString(), "User Login Message"));
        } else {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
        }
        return response;
    }

    // 删除跟进人员记录信息
    @RequestMapping(value = "/delTrackUser",method = RequestMethod.POST)
    public Response delTrackUser(@RequestBody Map<String,Object> param){
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/orgTrack/getInfo";
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
            response = userService.delTrackUser(param) > 0 ? Response.getResponseSuccess(userInfo):Response.getResponseError();
        }catch (Exception e){
            flag = false;
            JzbTools.logError(e);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "delTrackUser Method", e.toString()));
        }
        if (userInfo != null) {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
                    userInfo.get("msgTag").toString(), "User Login Message"));
        } else {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
        }
        return response;
    }

    // 修改跟进人员记录信息
    @RequestMapping(value = "/updTrackUser",method = RequestMethod.POST)
    public Response updTrackUser(@RequestBody Map<String,Object> param){
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/orgTrack/getInfo";
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
            response = userService.updTrackUser(param) > 0 ? Response.getResponseSuccess(userInfo):Response.getResponseError();
        }catch (Exception e){
            flag = false;
            JzbTools.logError(e);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "updTrackUser Method", e.toString()));
        }
        if (userInfo != null) {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
                    userInfo.get("msgTag").toString(), "User Login Message"));
        } else {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
        }
        return response;
    }

    // 查询联系沟通信息
    @RequestMapping(value = "/getInfo",method = RequestMethod.POST)
    public Response getInfo(@RequestBody Map<String,Object> param){
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/orgTrack/getInfo";
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
    // 查询有效客户
    @RequestMapping(value = "/getClient",method = RequestMethod.POST)
    public Response getClient(@RequestBody Map<String,Object> param){
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/orgTrack/getClient";
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
            if (JzbTools.isEmpty(param.get("adduid"))){
                param.put("adduid",userInfo.get("uid"));
            }
            userInfo = (Map<String, Object>) param.get("userinfo");
            List<Map<String ,Object>> list = userService.getClient(param);
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(list);
            result = Response.getResponseSuccess(userInfo);
            result.setPageInfo(pageInfo);
        }catch (Exception e){
            flag = false;
            JzbTools.logError(e);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getClient Method", e.toString()));
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
     *CRM-销售业主-联系-个人看板-
     * 愿意见-深度见-上会-签约数量
     * @param param
     * @return
     */
    @RequestMapping(value = "/getHandleCount", method = RequestMethod.POST)
    @CrossOrigin
    public Response getHandleCount(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/orgTrack/getHandleCount";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            param.put("uid",userInfo.get("uid"));
            List<Map<String, Object>> List = userService.getHandleCount(param);
            result = Response.getResponseSuccess(userInfo);
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(List);
            result.setPageInfo(pageInfo);
        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getHandleCount Method", ex.toString()));
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
     * 根据用户查询分别查询微信、qq、电话、拜访、的总记录数。与合计
     */
    @RequestMapping(value = "/getSingleCount", method = RequestMethod.POST)
    @CrossOrigin
    public Response getSingleCount(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/orgTrack/getSingleCount";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            param.put("adduid",userInfo.get("uid"));
            List<Map<String, Object>> List = userService.getSingleCount(param);
            result = Response.getResponseSuccess(userInfo);
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(List);
            result.setPageInfo(pageInfo);
        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getSingleCount Method", ex.toString()));
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
     * 根据跟进人查询 联系客户列表
     */
    @RequestMapping(value = "/getContactList", method = RequestMethod.POST)
    @CrossOrigin
    public Response getContactList(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/orgTrack/getContactList";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            param.put("adduid",userInfo.get("uid"));
            List<Map<String, Object>> List = userService.getContactList(param);
            result = Response.getResponseSuccess(userInfo);
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(List);
            result.setPageInfo(pageInfo);
        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getSingleCount Method", ex.toString()));
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
     * 根据跟进人查询不同跟进阶段客户列表
     */
    @RequestMapping(value = "/getHandleStage", method = RequestMethod.POST)
    @CrossOrigin
    public Response getHandleStage(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/orgTrack/getHandleStage";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            param.put("adduid",userInfo.get("uid"));
            List<Map<String, Object>> List = userService.getHandleStage(param);
            result = Response.getResponseSuccess(userInfo);
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(List);
            result.setPageInfo(pageInfo);
        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getHandleStage Method", ex.toString()));
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
