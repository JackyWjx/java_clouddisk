package com.jzb.operate.controller;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jzb.base.data.JzbDataType;
import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import com.jzb.operate.service.TbScoreManualService;
import com.jzb.operate.util.PageConvert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
        *@描述
        *@创建人 chenhui
        *@创建时间 2019/11/9
        *@修改人和其它信息
*/
@RestController
@RequestMapping("/operate/score")
public class TbScoreManualTypeController {
    @Autowired
    TbScoreManualService scoreManual;

    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(TbScoreManualTypeController.class);

    @RequestMapping("/getScoreManualList")
    public Response getScoreManual(@RequestBody Map<String ,Object> paramap){

        Response response;
        try{
            //获取单位总数
            int count = JzbDataType.getInteger(paramap.get("count"));
            count = count < 0 ? 0:count;
            if (count == 0){
                //查询活动总数
                count = scoreManual.getCount(paramap);
            }
            PageConvert pageConvert = new PageConvert();
            pageConvert.setPageRows(paramap);
            // 查询指导手册
            List<Map<String, Object>> list  = scoreManual.getActivity(paramap);
            response = Response.getResponseSuccess();
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(list);
            pageInfo.setTotal(count > 0 ? count : list.size());

            response.setPageInfo(pageInfo);
        }catch (Exception e){
            JzbTools.logError(e);
            response =  Response.getResponseError();
        }
        return response;



    }

    /**
     * 查询积分规则
     * @param paramp
     * @return
     */
    @RequestMapping("/getScoreRuleList")
    public Response getScoreRuleList(@RequestBody Map<String,Object> paramp){
        Response response;
        try {
            int count = JzbDataType.getInteger(paramp.get("count"));
            count = count < 0 ? 0:count;
            if (count == 0){
                // 查询活动总数
                count = scoreManual.getScoreRuleCount(paramp);
            }
            PageConvert pageConvert = new PageConvert();
            pageConvert.setPageRows(paramp);
            // 查询积分规则
            List<Map<String,Object>> list = scoreManual.getScoreRule(paramp);
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(list);
            pageInfo.setTotal(count > 0 ? count : list.size());
            response = Response.getResponseSuccess();
            response.setPageInfo(pageInfo);
        } catch (Exception e) {
            JzbTools.logError(e);
            response = Response.getResponseError();
        }
        return response;
    }

    /**
     * 新建积分规则
     * @param paramp
     * @return
     */
    @RequestMapping("/addScoreRule")
    public Response addScoreRuleList(@RequestBody Map<String,Object> paramp){
        Response response;
        try {
            // 获取用户信息
            Map<String,Object> userinfo = (Map<String, Object>) paramp.get("userinfo");
            // 加入新建积分规则内容
            int count = scoreManual.insertScoreRule(paramp);
            response = count > 0 ? Response.getResponseSuccess(userinfo):Response.getResponseError();
        } catch (Exception e) {
            JzbTools.logError(e);
            response = Response.getResponseError();
        }
        return response;
    }

    @RequestMapping("/delScoreRule")
    public Response delScoreRule(@RequestBody Map<String ,Object> paramp){
        Response response;

        try {
            Map<String,Object> userinfo = (Map<String, Object>) paramp.get("userinfo");
            // 删除积分规则
            int count = scoreManual.delScoreRule(paramp);
            response = count > 0 ? Response.getResponseSuccess(userinfo):Response.getResponseError();
        } catch (Exception e) {
            JzbTools.logError(e);
            response = Response.getResponseError();
        }
        return response;
    }

    @RequestMapping("/updScoreRule")
    public Response modifyScoreRule(@RequestBody Map<String,Object> paramp){
        Response response;
        try {
            Map<String,Object> userinfo = (Map<String, Object>) paramp.get("userinfo");
            paramp.put("ouid",userinfo.get("uid"));
            int count = scoreManual.updScoreRule(paramp);
            response = count > 0 ? Response.getResponseSuccess(userinfo):Response.getResponseError();
        } catch (Exception e) {
            JzbTools.logError(e);
            response = Response.getResponseError();
        }
        return response;
    }


    /**
     * 查询我的任务
     * @param paramp
     * @return
     */
    @RequestMapping(value = "/queryMyTask",method = RequestMethod.POST)
    public Response queryMyTask(@RequestBody Map<String,Object> paramp){
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/operate/score/queryMyTask";
        boolean flag = true;
        // 查询用户信息
        try {
            if (paramp.get("userinfo") != null) {
                userInfo = (Map<String, Object>) paramp.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            paramp.put("uid",userInfo.get("uid"));
            // 查询积分值
            List<Map<String,Object>> list = scoreManual.querySocre(paramp);

            // 获取已完成任务总数
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(list);
            response = Response.getResponseSuccess(userInfo);
            response.setPageInfo(pageInfo);

        }catch (Exception e){
            flag = false;
            JzbTools.logError(e);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "queryMyTask Method", e.toString()));
        }
        if (userInfo != null) {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
                    userInfo.get("msgTag").toString(), "User Login Message"));
        } else {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
        }
        return response;
    }

    @RequestMapping("/queryTaskCount")
    public Response queryTaskCount(@RequestBody Map<String,Object> paramp){
        Response response;
        try {
            Map<String ,Object> userinfo = (Map<String, Object>) paramp.get("userinfo");
            paramp.put("uid",userinfo.get("uid"));
            // 获取已完成任务总数
            int sucCont =  scoreManual.getSucCount(paramp);
            int allCont = scoreManual.queryTaskCount(paramp);
            response = Response.getResponseSuccess(userinfo);
//            response = sucCont > 0 ? Response.getResponseSuccess(userinfo) : Response.getResponseError();
            // 任务已完成数与目标完成数
            Map<String,Object> countMap = new HashMap<>();
            countMap.put("count",sucCont);
            countMap.put("max",allCont);
            response.setResponseEntity(countMap);
        } catch (Exception e) {
            JzbTools.logError(e);
            response = Response.getResponseError();
        }
        return response;
    }

    /**
     * 领取积分
     * @param paramp
     * @return
     */
    @RequestMapping("/modifyScoreStatus")
    public Response modifyScoreStatus(@RequestBody Map<String,Object> paramp){
        Response response;
        try {
            Map<String,Object> userinfo = (Map<String, Object>) paramp.get("userinfo");
            paramp.put("uid",userinfo.get("uid"));
            int count = scoreManual.modifyStatus(paramp);
            response =  count > 0 ? Response.getResponseSuccess(userinfo):Response.getResponseError();
        } catch (Exception e) {
            JzbTools.logError(e);
            response = Response.getResponseError();
        }
        return response;
    }

    /**
     * 查询消费明细
     * @param parmp
     * @return
     */
    @RequestMapping("/getConsumeList")
    public Response getConsumeList(@RequestBody Map<String ,Object> parmp){
        Response response;
        try {
            Map<String,Object> userinfo = (Map<String, Object>) parmp.get("userinfo");
            parmp.put("uid",userinfo.get("uid"));

            // 查询消费明细总记录数
            int count = JzbDataType.getInteger(parmp.get("count"));
            count = count < 0 ? 0:count;
            if (count == 0){
                count = scoreManual.getConsumeCount(parmp);
            }
            PageConvert pageConvert = new PageConvert();
            pageConvert.setPageRows(parmp);

            // 查询消费明细
            List<Map<String,Object>> counsumeList = scoreManual.getConsumeList(parmp);
            for (int i = 0; i < counsumeList.size(); i++) {
                if (!JzbTools.isEmpty(counsumeList.get(i).get("optvalue"))){
                    String optvalue = (String) counsumeList.get(i).get("optvalue");
                    Map<String,Object> parse = (Map<String, Object>) JSON.parse(optvalue);
                    counsumeList.get(i).put("optvalue",parse);
                }
            }
            PageInfo pageInfo = new PageInfo();
            pageInfo.setTotal(count);
            pageInfo.setList(counsumeList);
            response = Response.getResponseSuccess(userinfo);
            response.setPageInfo(pageInfo);
        } catch (Exception e) {
            JzbTools.logError(e);
            response = Response.getResponseError();
        }
        return response;

    }
}
