package com.jzb.org.controller;


import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.org.service.EvaluationMethodService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/org/evaluationMethod")
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
            for (Map<String,Object> evaluationMethod:evaluationMethods) {
                Date date = new Date();
                Long dateNum = (Long) evaluationMethod.get("addtime");
                Long dateNum1 = (Long) evaluationMethod.get("updtime");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                date.setTime(dateNum);//java里面应该是按毫秒
                evaluationMethod.put("addtime",sdf.format(date));
                date.setTime(dateNum1);//java里面应该是按毫秒
                evaluationMethod.put("updtime",sdf.format(date));
            }
            result = Response.getResponseSuccess(userInfo);
            // 定义pageinfo
            PageInfo pi=new PageInfo();

            pi.setList(evaluationMethods);

            // 如果有一个指定参数不为空，则返回list.size()  否则返回总数
            pi.setTotal(evaluationMethodService.quertTenderTypeCount(param));
            result.setPageInfo(pi);
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
