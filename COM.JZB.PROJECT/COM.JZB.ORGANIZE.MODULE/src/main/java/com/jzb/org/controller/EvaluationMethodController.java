package com.jzb.org.controller;


import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbPageConvert;
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

/**
 * 评标方法增删改
 * @Date 2019.12.7
 * @author wang jixiang
 */
@RestController
@RequestMapping("/org/evaluationMethod")
public class EvaluationMethodController {
    @Autowired
    private EvaluationMethodService evaluationMethodService;

    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(EvaluationMethodController.class);


    /**
     * 获取评标方法的所有数据
     * @param param
     * @return
     */
    @RequestMapping(value = "/getEvaluationMethod", method = RequestMethod.POST)
    @CrossOrigin
    public Response getEvaluationMethod(@RequestBody(required = false) Map<String, Object> param) {
        Response result;
        String  api="/org/evaluationMethod/getEvaluationMethod";
        Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
        try {
            //所传参数不为空，进行分页
            if(JzbCheckParam.haveEmpty(param,new String[]{"pageno","pagesize"})){
                result=Response.getResponseError();
                logger.info(JzbLoggerUtil.getApiLogger( api, "1", "ERROR",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User select EvaluationMethod"));
            }else {
                JzbPageConvert.setPageRows(param);
                List<Map<String, Object>> evaluationMethods = evaluationMethodService.queryEvaluationMethod(param);
                //转化时间戳
                for (Map<String,Object> evaluationMethod:evaluationMethods) {
                    Date date = new Date();
                    Long dateNum = (Long) evaluationMethod.get("addtime");
                    Long dateNum1 = (Long) evaluationMethod.get("updtime");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    if(!("").equals(dateNum)&&dateNum!=null){
                        date.setTime(dateNum);//java里面应该是按毫秒
                        evaluationMethod.put("addtime",sdf.format(date));

                    }
                    if(!("").equals(dateNum1)&&dateNum1!=null){
                        date.setTime(dateNum1);//java里面应该是按毫秒
                        evaluationMethod.put("updtime",sdf.format(date));

                    }
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

            }
           } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getEvaluationMethod Method", "[评标方法查询异常]"));
        }
        return result;
    }

    /**
     * 增加评标方法
     * @param param
     * @return
     */
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
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getEvaluationMethod Method", "[增加招标方法异常]"));
        }
        return result;
    }

    /**
     * 删除评标方法
     * @param param
     * @return
     */
    @RequestMapping(value = "/delEvaluationMethod", method = RequestMethod.POST)
    @CrossOrigin
    public Response delEvaluationMethod(@RequestBody Map<String, Object> param) {
        Response result;
        String  api="/org/evaluationMethod/delEvaluationMethod";
        Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
        try {
            result = evaluationMethodService.delEvaluationMethod(param)>0?
                    Response.getResponseSuccess(userInfo):Response.getResponseError();
            logger.info(JzbLoggerUtil.getApiLogger( api, "1", "INFO",
                    userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User del EvaluationMethod"));
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getEvaluationMethod Method", "[评标方法删除异常]"));

        }
        return result;
    }

    /**
     * 修改评标方法
     * @param param
     * @return
     */
    @RequestMapping(value = "/putEvaluationMethod", method = RequestMethod.POST)
    @CrossOrigin
    public Response putEvaluationMethod(@RequestBody Map<String, Object> param) {
        Response result;
        String  api="/org/evaluationMethod/putEvaluationMethod";
        Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
        try {
            param.put("upduid", userInfo.get("uid"));
            param.put("updtime", System.currentTimeMillis());
            result = evaluationMethodService.putEvaluationMethod(param)>0?
                    Response.getResponseSuccess(userInfo):Response.getResponseError();
            logger.info(JzbLoggerUtil.getApiLogger( api, "1", "INFO",
                    userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User update EvaluationMethod"));
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getEvaluationMethod Method", "[更新评标方法异常]"));
        }
        return result;
    }
}
