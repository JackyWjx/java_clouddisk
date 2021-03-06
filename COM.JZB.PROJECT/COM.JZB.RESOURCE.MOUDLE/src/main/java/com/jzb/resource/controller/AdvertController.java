package com.jzb.resource.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.resource.service.AdvertService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Description: 系统广告控制台与前台对接
 * @Author duanfeiyu
 * @Version v1.0
 * @Since 1.0
 * @Date: 2019/8/16 15:08
 */
@RequestMapping("/advertising")
@RestController
public class AdvertController {

    @Autowired
    private AdvertService advertService;

    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(AdvertController.class);

    /**
     * 返回结果集
     *
     * @param records  list集合
     * @param response 用json存储h
     */
    public static void setPageInfoList(List<Map<String, Object>> records, Response response) {
        PageInfo pageInfo = new PageInfo();
        pageInfo.setList(records);
        response.setPageInfo(pageInfo);
    }

    /**
     * 广告系统查询
     *
     * @return Response 返回json
     */
    @RequestMapping("/queryAdvertisingList")
    public Response queryAdvertisingList(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/advertising/queryAdvertisingList";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            List<Map<String, Object>> list = advertService.queryAdvertisingList();
            // 定义返回结果
            response = Response.getResponseSuccess(userInfo);
            setPageInfoList(list, response);
        } catch (Exception e) {
            flag = false;
            JzbTools.logError(e);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "queryAdvertisingList Method", e.toString()));
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
     * CRM-运营管理-活动-推广图片
     * 点击活动获取活动所有的系统推广信息
     *
     * @author kuangbin
     */
    @RequestMapping(value = "/getAdvertList", method = RequestMethod.POST)
    @CrossOrigin
    public Response getAdvertList(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/advertising/getAdvertList";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            int count = JzbDataType.getInteger(param.get("count"));
            // 获取推广信息总数
            count = count < 0 ? 0 : count;
            if (count == 0) {
                // 查询所有符合条件的总数
                count = advertService.getAdvertListCount(param);
            }
            // 返回所有的推广信息列表
            List<Map<String, Object>> adverList = advertService.getAdvertList(param);
            result = Response.getResponseSuccess(userInfo);
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(adverList);
            pageInfo.setTotal(count > 0 ? count : adverList.size());
            result.setPageInfo(pageInfo);
        } catch (Exception e) {
            flag = false;
            JzbTools.logError(e);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getAdvertList Method", e.toString()));
        }
        if (userInfo != null) {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
                    userInfo.get("msgTag").toString(), "User Login Message"));
        } else {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
        }
        return result;
    } // End getAdvertList

    /**
     * CRM-运营管理-活动-推广图片
     * 点击保存后修改对应的推广信息
     *
     * @author kuangbin
     */
    @RequestMapping(value = "/modifyAdvertData", method = RequestMethod.POST)
    @CrossOrigin
    public Response modifyAdvertData(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/advertising/modifyAdvertData";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            // 获取用户信息
            param.put("uid", JzbDataType.getString(userInfo.get("uid")));
            String advid = JzbDataType.getString(param.get("advid"));
            int count;
            if (JzbDataType.isEmpty(advid)) {
                count = advertService.addAdvertData(param);
            } else {
                // 获取修改成功值
                count = advertService.modifyAdvertData(param);
            }
            result = count == 1 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
        } catch (Exception e) {
            flag = false;
            JzbTools.logError(e);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "modifyAdvertData Method", e.toString()));
        }
        if (userInfo != null) {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
                    userInfo.get("msgTag").toString(), "User Login Message"));
        } else {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
        }
        return result;
    } // End modifyAdvertData

    /**
     * CRM-运营管理-活动-推广图片
     * 点击活动获取活动所有的系统推广信息(首页中需要加入白名单)
     *
     * @author kuangbin
     */
    @RequestMapping(value = "/getAdvertListPass", method = RequestMethod.POST)
    @CrossOrigin
    public Response getAdvertListPass(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/advertising/getAdvertListPass";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            int count = JzbDataType.getInteger(param.get("count"));
            // 获取推广信息总数
            count = count < 0 ? 0 : count;
            if (count == 0) {
                // 查询所有符合条件的总数
                count = advertService.getAdvertListCount(param);
            }
            // 返回所有的推广信息列表
            List<Map<String, Object>> adverList = advertService.getAdvertList(param);
            result = Response.getResponseSuccess();
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(adverList);
            pageInfo.setTotal(count > 0 ? count : adverList.size());
            result.setPageInfo(pageInfo);
        } catch (Exception e) {
            flag = false;
            JzbTools.logError(e);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getAdvertListPass Method", e.toString()));
        }
        if (userInfo != null) {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
                    userInfo.get("msgTag").toString(), "User Login Message"));
        } else {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
        }
        return result;
    } // End getAdvertList

    /**
     * CRM-运营管理-活动-推广图片
     * 点击删除后修改推广信息的状态
     *
     * @author kuangbin
     */
    @RequestMapping(value = "/removeAdvertData", method = RequestMethod.POST)
    @CrossOrigin
    public Response removeAdvertData(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/advertising/removeAdvertData";
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
            // 获取修改成功值
            int count = advertService.removeAdvertData(param);
            result = count == 1 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
        } catch (Exception e) {
            flag = false;
            JzbTools.logError(e);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "removeAdvertData Method", e.toString()));
        }
        if (userInfo != null) {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
                    userInfo.get("msgTag").toString(), "User Login Message"));
        } else {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
        }
        return result;
    } // End modifyAdvertData
}
