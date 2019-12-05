package com.jzb.org.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.data.date.JzbDateStr;
import com.jzb.base.data.date.JzbDateUtil;
import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbTools;
import com.jzb.org.service.TbCompanyContractFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author chenzhengduan
 * 合同附件表
 */
@RestController
@RequestMapping(value = "/org/companyContractFile")
public class TbCompanyContractFileController {

    @Autowired
    private TbCompanyContractFileService tbCompanyContractFileService;

    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(TbCompanyContractFileController.class);

    /**
     * 获取文件
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getFileByConId", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response getFileByConId(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/org/companyContractFile/getFileByConId";
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
            // 验证指定参数
            if (JzbCheckParam.haveEmpty(param, new String[]{"contid"})) {
                response = Response.getResponseError();
            } else {
                List<Map<String, Object>> list = tbCompanyContractFileService.findFileByContId(param);
                for (int i = 0, l = list.size(); i < l; i++) {
                    list.get(i).put("addtime", JzbDateUtil.toDateString(JzbDataType.getLong(list.get(i).get("addtime")), JzbDateStr.yyyy_MM_dd_HH_mm_ss));
                    list.get(i).put("updtime", JzbDateUtil.toDateString(JzbDataType.getLong(list.get(i).get("updtime")), JzbDateStr.yyyy_MM_dd_HH_mm_ss));
                }
                response = Response.getResponseSuccess(userInfo);
                PageInfo pageInfo = new PageInfo();
                pageInfo.setList(list);
                response.setPageInfo(pageInfo);
            }
        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getFileByConId Method", ex.toString()));
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
     * 添加附件
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/addFileByConId", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response addFileByConId(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/org/companyContractFile/addFileByConId";
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
            // 验证指定参数
            if (JzbCheckParam.haveEmpty(param, new String[]{"list"})) {
                response = Response.getResponseError();
            } else {
                List<Map<String, Object>> list = (List<Map<String, Object>>) param.get("list");
                if (list.size() > 0) {
                    for (int i = 0, l = list.size(); i < l; i++) {
                        long time = System.currentTimeMillis();
                        list.get(i).put("addtime", time);
                        list.get(i).put("updtime", time);
                        list.get(i).put("adduid", time);
                        list.get(i).put("upduid", time);
                    }
                    int count = tbCompanyContractFileService.addCompanyContractFile(list);
                    response = count > 0 ? Response.getResponseSuccess() : Response.getResponseError();
                } else {
                    response = Response.getResponseSuccess();
                }
            }
        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "addFileByConId Method", ex.toString()));
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
     * 修改附件
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/updateFileStatusByConId", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response updateFileStatusByConId(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/org/companyContractFile/addFileByConId";
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
            // 验证指定参数
            if (JzbCheckParam.haveEmpty(param, new String[]{"list"})) {
                response = Response.getResponseError();
            } else {
                List<Map<String, Object>> list = (List<Map<String, Object>>) param.get("list");
                if (list.size() > 0) {
                    for (int i = 0, l = list.size(); i < l; i++) {
                        long time = System.currentTimeMillis();
                        list.get(i).put("updtime", time);
                        list.get(i).put("upduid", time);
                    }
                    int count = tbCompanyContractFileService.updateFileStatus(list);
                    response = count > 0 ? Response.getResponseSuccess() : Response.getResponseError();
                } else {
                    response = Response.getResponseSuccess();
                }
            }
        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "addFileByConId Method", ex.toString()));
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
