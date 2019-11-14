package com.jzb.resource.controller;

import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbPageConvert;
import com.jzb.base.util.JzbTools;
import com.jzb.resource.service.TbContractTemplateItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/resource/contractTemplateItem")
public class TbContractTemplateItemController {
    @Autowired
    private TbContractTemplateItemService tbContractTemplateItemService;

    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(TbContractTemplateItemController.class);

    /**
     * 查询合同模板项
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getContractTemplateItem", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response getContractTemplateItem(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/resource/contractTemplateItem/getContractTemplateItem";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            // 判断指定参数为空则返回error
            if (JzbCheckParam.haveEmpty(param, new String[]{"tempid"})) {
                result = Response.getResponseError();
                logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getContractTemplateItem Method", "[param error] or [param is null]"));
            } else {
                // 获取结果集
                List<Map<String, Object>> list = tbContractTemplateItemService.quertContractTemplateItemByTempid(param);

                // 定义返回结果
                Map<String, Object> userinfo = (Map<String, Object>) param.get("userinfo");
                result = Response.getResponseSuccess(userinfo);
                PageInfo pi = new PageInfo();
                pi.setList(list);
                pi.setTotal(list.size());
                result.setPageInfo(pi);
            }

        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getContractTemplateItem Method", ex.toString()));
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
     * 查询合同模板项
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getBaseContractTemplateItem", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response getBaseContractTemplateItem(@RequestBody(required = false) Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/resource/contractTemplateItem/getBaseContractTemplateItem";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            param.put("tempid", "000000000");
            // 获取结果集
            List<Map<String, Object>> list = tbContractTemplateItemService.quertContractTemplateItemByTempid(param);

            // 定义返回结果
            Map<String, Object> userinfo = (Map<String, Object>) param.get("userinfo");
            result = Response.getResponseSuccess(userinfo);
            PageInfo pi = new PageInfo();
            pi.setList(list);
            pi.setTotal(list.size());
            result.setPageInfo(pi);
        } catch (Exception ex) {
            flag=false;
            JzbTools.logError(ex);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getBaseContractTemplateItem Method", ex.toString()));
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
     * 引用模板
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getContractTemplateItemByid", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response getContractTemplateItemByid(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/resource/contractTemplateItem/getContractTemplateItemByid";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            // 判断指定参数为空则返回error
            if (JzbCheckParam.haveEmpty(param, new String[]{"itemid"})) {
                result = Response.getResponseError();
                logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getContractTemplateItemByid Method", "[param error] or [param is null]"));
            } else {
                // 获取结果集
                List<Map<String, Object>> list = tbContractTemplateItemService.quertContractTemplateItemByItemid(param);

                // 定义返回结果
                Map<String, Object> userinfo = (Map<String, Object>) param.get("userinfo");
                result = Response.getResponseSuccess(userinfo);
                PageInfo pi = new PageInfo();
                pi.setList(list);
                pi.setTotal(list.size());
                result.setPageInfo(pi);
            }

        } catch (Exception ex) {
            flag=false;
            JzbTools.logError(ex);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getContractTemplateItemByid Method", ex.toString()));
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
     * 添加合同模板项
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/addContractTemplateItem", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response addContractTemplateItem(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/resource/contractTemplateItem/addContractTemplateItem";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            List<Map<String, Object>> list = (List<Map<String, Object>>) param.get("list");
            int count = tbContractTemplateItemService.addContractTemplateItem(list);
            if (count > 0) {
                // 定义返回结果
                result = Response.getResponseSuccess(userInfo);
            } else {
                result = Response.getResponseError();
                logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getContractTemplateItemByid Method", "[addResult error]"));
            }
        } catch (Exception ex) {
            flag=false;
            JzbTools.logError(ex);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "addContractTemplateItem Method", ex.toString()));

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
     * 修改合同模板项
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/updateContractTemplateItem", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response updateContractTemplateItem(@RequestBody Map<String, Object> param) {
        Response result;
        try {

            List<Map<String, Object>> list = (List<Map<String, Object>>) param.get("list");

            result = tbContractTemplateItemService.updateContractTemplateItem(param) > 0 ? Response.getResponseSuccess((Map<String, Object>) param.get("userinfo")) : Response.getResponseError();
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

}
