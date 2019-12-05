package com.jzb.org.controller;

import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import com.jzb.org.service.TenderTypeService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/org/TenderType")
@RestController
public class TenderTypeController {

    @Autowired
    private TenderTypeService tenderTypeService;

    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(TenderTypeController.class);


    @RequestMapping(value = "/getTenderType", method = RequestMethod.POST)
    @CrossOrigin
    public Response getTenderType(@RequestBody(required = false) Map<String, Object> param) {
        Response result;
        String api = "/org/TenderType/getTenderType";
        Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
        try {
            if (!"".equals(param.get("pageno")) && param.get("pageno") != null) {
                int pageno = (int) param.get("pageno") - 1;
                int pagesize = (int) param.get("pagesize");
                param.put("start", pageno * pagesize);
            }
            List<Map<String, Object>> tenderType = tenderTypeService.queryTenderType(param);
            result = Response.getResponseSuccess(userInfo);
            // 定义pageinfo
            PageInfo pi = new PageInfo();

            pi.setList(tenderType);

            // 如果有一个指定参数不为空，则返回list.size()  否则返回总数
            pi.setTotal(tenderTypeService.quertTenderTypeCount(param));
            result.setPageInfo(pi);

//            result.setResponseEntity(tenderType);
            logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                    userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User select TenderType"));
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getTenderType Method", "[sql select error]"));
        }
        return result;
    }

    @RequestMapping(value = "/addTenderType", method = RequestMethod.POST)
    @CrossOrigin
    public Response addTenderType(@RequestBody Map<String, Object> param) {
        Response result;
        String api = "/org/TenderType/addTenderType";
        Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
        try {
            if ("".equals(param.get("cname")) && param.get("cname") != null) {
                param.put("adduid", userInfo.get("uid"));
                param.put("addtime", System.currentTimeMillis());
                param.put("status", "1");
                param.put("typeid", tenderTypeService.selectMaxNum() + 1);
                tenderTypeService.addTenderType(param);
                result = Response.getResponseSuccess(userInfo);
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User add TenderType"));
            } else {
                result = Response.getResponseError();
            }
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getTenderType Method", "[sql add error]"));
        }
        return result;
    }


    @RequestMapping(value = "/delTenderType", method = RequestMethod.POST)
    @CrossOrigin
    public Response delTenderType(@RequestBody Map<String, Object> param) {
        Response result;
        String api = "/org/TenderType/addTenderType";
        Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
        try {
            String typeId = (String) param.get("typeid");
            Integer changeNum = tenderTypeService.delTenderType(typeId);
            result = Response.getResponseSuccess(userInfo);
            logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                    userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User del TenderType"));
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getTenderType Method", "[sql del error]"));

        }
        return result;
    }

    @RequestMapping(value = "/putTenderType", method = RequestMethod.POST)
    @CrossOrigin
    public Response putTenderType(@RequestBody Map<String, Object> param) {
        Response result;
        String api = "/org/TenderType/putTenderType";
        Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
        try {
            param.put("upduid", userInfo.get("uid"));
            param.put("updtime", System.currentTimeMillis());
            Integer changeNum = tenderTypeService.putTenderType(param);
            result = Response.getResponseSuccess(userInfo);
            logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                    userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User update TenderType"));
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getTenderType Method", "[sql update error]"));
        }
        return result;
    }
}


