package com.jzb.resource.controller;

import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbPageConvert;
import com.jzb.base.util.JzbTools;
import com.jzb.resource.service.TbContractTemplateTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author chenzhengduan
 * 合同模板类型
 */
@RestController
@RequestMapping(value = "/resource/contracttemptype")
public class TbContractTemplateTypeController {

    @Autowired
    private TbContractTemplateTypeService tbContactTemplateTypeService;

    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(TbContractTemplateTypeController.class);

    /**
     * 获取合同模板分类
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getContractTemplateType", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response getContractTemplateType(@RequestBody(required = false) Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/resource/contracttemptype/getContractTemplateType";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            if (JzbCheckParam.haveEmpty(param, new String[]{"pageno", "pagesize"})) {
                result = Response.getResponseError();
                logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getContractTemplateType Method", "[param error] or [param is null]"));
            } else {
                JzbPageConvert.setPageRows(param);
                List<Map<String, Object>> list = tbContactTemplateTypeService.getContactTemplateType(param);
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
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getContractTemplateType Method", ex.toString()));
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
