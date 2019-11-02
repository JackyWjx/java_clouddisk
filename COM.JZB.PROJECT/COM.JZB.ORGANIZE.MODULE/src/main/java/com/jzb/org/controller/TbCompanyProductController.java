package com.jzb.org.controller;

import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbTools;
import com.jzb.org.service.TbCompanyProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author chenzhengduan
 * 企业产品
 */
@RestController
@RequestMapping(value = "/org/companyProduct")
public class TbCompanyProductController {

    @Autowired
    private TbCompanyProductService tbCompanyProductService;

    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(TbCompanyProductController.class);

    /**
     * 添加企业产品
     * @author chenzhengduan
     * @param params
     * @return
     */
    @RequestMapping(value = "/addCompanyProduct", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response addCompanyProduct(@RequestBody Map<String, Object> params) {
        Response response;
        String api="/org/companyProduct/addCompanyProduct";
        Map<String, Object> userInfo = null;
        boolean flag = true;
        try {
            if (params.get("userinfo") != null) {
                userInfo = (Map<String, Object>) params.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger( api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger( api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            if (JzbCheckParam.haveEmpty(params, new String[]{"list"})) {
                logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "add Company Method", "参数为空"));
                response = Response.getResponseError();
            } else {
                response = tbCompanyProductService.addCompanyProduct(params) > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
            }
        } catch (Exception ex) {
            flag=false;
            JzbTools.logError(ex);
            response=Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "addCompanyProduct Method", ex.toString()));
        }
        if (userInfo != null) {
            logger.info(JzbLoggerUtil.getApiLogger( api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
                    userInfo.get("msgTag").toString(), "User Login Message"));
        } else {
            logger.info(JzbLoggerUtil.getApiLogger( api, "2", "ERROR", "", "", "", "", "User Login Message"));
        }
        return response;
    }
}
