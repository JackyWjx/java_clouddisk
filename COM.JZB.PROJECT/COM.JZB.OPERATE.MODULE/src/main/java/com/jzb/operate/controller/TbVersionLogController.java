package com.jzb.operate.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.data.date.JzbDateStr;
import com.jzb.base.data.date.JzbDateUtil;
import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbPageConvert;
import com.jzb.base.util.JzbTools;
import com.jzb.operate.api.redis.UserRedisServiceApi;
import com.jzb.operate.service.TbVersionLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Description: 系统日志
 * @Author chenzhengduan
 */
@RestController
@RequestMapping("/operate/log")
public class TbVersionLogController {

    @Autowired
    private TbVersionLogService tbVersionLogService;

    @Autowired
    private UserRedisServiceApi userRedisServiceApi;

    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(TbVersionLogController.class);

    /**
     * 查询日志信息
     *
     * @param param
     * @return
     * @Author czd
     */
    @RequestMapping(value = "/getVersionLog", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response getVersionLog(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/operate/log/getVersionLog";
        boolean flag = true;
        try {
            /**  如果获取参数userinfo不为空的话 */
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            /** 验证指定参数为空则返回error */
            if (JzbCheckParam.haveEmpty(param, new String[]{"pagesize", "pageno"})) {
                response = Response.getResponseError();
            } else {
                /**  设置参数信息 */
                JzbPageConvert.setPageRows(param);

                /**  获取结果集 */
                List<Map<String, Object>> list = tbVersionLogService.queryVersionLog(param);
                for (int i = 0, l = list.size(); i < l; i++) {
                    /**  转换时间 */
                    list.get(i).put("gadate", JzbDateUtil.toDateString(JzbDataType.getLong(list.get(i).get("gadate")), JzbDateStr.yyyy_MM_dd_HH_mm_ss));
                    list.get(i).put("vsndate", JzbDateUtil.toDateString(JzbDataType.getLong(list.get(i).get("vsndate")), JzbDateStr.yyyy_MM_dd_HH_mm_ss));

                    /**  获取用户id去缓存区查 */
                    param.put("uid", list.get(i).get("ouid"));
                    Response region = userRedisServiceApi.getCacheUserInfo(param);
                    list.get(i).put("userInfo", region.getResponseEntity());
                }
                response = Response.getResponseSuccess();
                PageInfo pageInfo = new PageInfo();
                pageInfo.setList(list);
                pageInfo.setTotal(tbVersionLogService.queryVersionLogCount(param));
                response.setPageInfo(pageInfo);
            }
        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getVersionLog Method", ex.toString()));
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
     * 条件搜索日志信息
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/searchVersionLog", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response searchVersionLog(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/operate/log/searchVersionLog";
        boolean flag = true;
        try {
            /**  如果获取参数userinfo不为空的话 */
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            /** 验证指定参数为空则返回error */
            if (JzbCheckParam.haveEmpty(param, new String[]{"pagesize", "pageno"})) {
                response = Response.getResponseError();
            } else {
                /**  设置参数信息 */
                JzbPageConvert.setPageRows(param);

                /**  获取结果集 */
                List<Map<String, Object>> list = tbVersionLogService.searchVersionLog(param);

                for (int i = 0, l = list.size(); i < l; i++) {
                    /**  转换时间 */
                    list.get(i).put("gadate", JzbDateUtil.toDateString(JzbDataType.getLong(list.get(i).get("gadate")), JzbDateStr.yyyy_MM_dd_HH_mm_ss));
                    list.get(i).put("vsndate", JzbDateUtil.toDateString(JzbDataType.getLong(list.get(i).get("vsndate")), JzbDateStr.yyyy_MM_dd_HH_mm_ss));

                    /**  获取用户id去缓存区查 */
                    param.put("uid", list.get(i).get("ouid"));
                    Response region = userRedisServiceApi.getCacheUserInfo(param);
                    list.get(i).put("userInfo", region.getResponseEntity());
                }
                response = Response.getResponseSuccess();
                PageInfo pageInfo = new PageInfo();
                pageInfo.setList(list);
                pageInfo.setTotal(tbVersionLogService.searchVersionLogCount(param));
                response.setPageInfo(pageInfo);
            }
        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "searchVersionLog Method", ex.toString()));
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
     * 添加日志信息
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/saveVersionLog", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response saveVersionLog(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/operate/log/saveVersionLog";
        boolean flag = true;
        try {
            /**  如果获取参数userinfo不为空的话 */
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            /**  验证指定参数为空则返回error */
            if (JzbCheckParam.haveEmpty(param, new String[]{"vsncn", "vsncode", "vsndate", "vsndesc"})) {
                response = Response.getResponseError();
            } else {
                param.put("ouid", userInfo.get("uid"));
                response = tbVersionLogService.saveVersionLog(param) > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
            }
        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "saveVersionLog Method", ex.toString()));
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
     * 修改日志信息
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/updateVersionLog", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response upVersionLog(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/operate/log/updateVersionLog";
        boolean flag = true;
        try {
            /**  如果获取参数userinfo不为空的话 */
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            /**  验证指定参数为空则返回error */
            if (JzbCheckParam.haveEmpty(param, new String[]{"id"})) {
                response = Response.getResponseError();
            } else {
                response = tbVersionLogService.upVersionLog(param) > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
            }
        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "upVersionLog Method", ex.toString()));
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
     * 禁用
     */
    @RequestMapping(value = "/removeVersionLog", method = RequestMethod.POST)
    @ResponseBody
    public Response removeVersionLog(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/operate/log/removeVersionLog";
        boolean flag = true;
        try {
            /**  如果获取参数userinfo不为空的话 */
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            /**  验证指定参数为空则返回error */
            if (JzbCheckParam.haveEmpty(param, new String[]{"id"})) {
                response = Response.getResponseError();
            } else {
                response = tbVersionLogService.removeVersionLog(param) > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
            }
        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "removeVersionLog Method", ex.toString()));
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
