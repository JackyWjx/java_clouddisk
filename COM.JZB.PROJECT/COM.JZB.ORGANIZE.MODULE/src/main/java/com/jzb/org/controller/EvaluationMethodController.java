package com.jzb.org.controller;


import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.org.service.EvaluationMethodService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/org/evaluation")
public class EvaluationMethodController {
    @Autowired
    private EvaluationMethodService evaluationMethodService;

    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(EvaluationMethodController.class);



    @RequestMapping(value = "/getEvaluationMethod", method = RequestMethod.POST)
    @CrossOrigin
    public Response getEvaluationMethod(@RequestBody(required = false) Map<String, Object> param) {
        Response result;
        String  api="/org/evaluationMethod/getEvaluationMethod";
        Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
        try {
            if(!"".equals(param.get("pageno"))&&param.get("pageno")!=null){
                int pageno = (int)param.get("pageno")-1;
                int pagesize = (int)param.get("pagesize");
                param.put("start",pageno*pagesize);
            }
            List<Map<String, Object>> evaluationMethods = evaluationMethodService.queryEvaluationMethod(param);
            result = Response.getResponseSuccess(userInfo);
            result.setResponseEntity(evaluationMethods);
            logger.info(JzbLoggerUtil.getApiLogger( api, "1", "INFO",
                    userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User select EvaluationMethod"));
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getEvaluationMethod Method", "[sql select error]"));
        }
        return result;
    }

    @RequestMapping(value = "/addEvaluationMethod", method = RequestMethod.POST)
    @CrossOrigin
    public Response addEvaluationMethod(@RequestBody Map<String, Object> param) {
        Response result;
        String  api="/org/evaluationMethod/addEvaluationMethod";
        Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
        try {
            param.put("adduid", userInfo.get("uid"));
            param.put("addtime", System.currentTimeMillis());
            param.put("status", "1");
            param.put("typeid", Integer.parseInt(JzbRandom.getRandomNum(5)));
            evaluationMethodService.addEvaluationMethod(param);
            result = Response.getResponseSuccess(userInfo);
            logger.info(JzbLoggerUtil.getApiLogger( api, "1", "INFO",
                    userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User add EvaluationMethod"));
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getEvaluationMethod Method", "[sql add error]"));
        }
        return result;
    }


    @RequestMapping(value = "/delEvaluationMethod", method = RequestMethod.POST)
    @CrossOrigin
    public Response delEvaluationMethod(@RequestBody Map<String, Object> param) {
        Response result;
        String  api="/org/evaluationMethod/delEvaluationMethod";
        Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
        try {
            Integer typeId = (Integer) param.get("typeid");
            Integer changeNum = evaluationMethodService.delEvaluationMethod(typeId);
            result = Response.getResponseSuccess(userInfo);
            logger.info(JzbLoggerUtil.getApiLogger( api, "1", "INFO",
                    userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User del EvaluationMethod"));
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getEvaluationMethod Method", "[sql del error]"));

        }
        return result;
    }

    @RequestMapping(value = "/putEvaluationMethod", method = RequestMethod.POST)
    @CrossOrigin
    public Response putEvaluationMethod(@RequestBody Map<String, Object> param) {
        Response result;
        String  api="/org/evaluationMethod/putEvaluationMethod";
        Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
        try {
            param.put("upduid", userInfo.get("uid"));
            param.put("updtime", System.currentTimeMillis());
            Integer changeNum = evaluationMethodService.putEvaluationMethod(param);
            result = Response.getResponseSuccess(userInfo);
            logger.info(JzbLoggerUtil.getApiLogger( api, "1", "INFO",
                    userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User update EvaluationMethod"));
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getEvaluationMethod Method", "[sql update error]"));
        }
        return result;
    }
}
