package com.jzb.operate.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbTools;
import com.jzb.operate.service.TbScoreRuleService;
import com.jzb.operate.util.PageConvert;
import org.apache.poi.ss.formula.functions.Count;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/scoreRule")
public class TbScoreRuleController {

    @Autowired
    private TbScoreRuleService tbScoreRuleService;

    private final static Logger logger = LoggerFactory.getLogger(TbCompanyMethodController.class);


    /**
     * 充值得分记录的查询
     * @param param
     * @return
     */
    @RequestMapping(value = "/getScoreRule", method = RequestMethod.POST)
    @CrossOrigin
    public Response getScoreRule(@RequestBody(required = false) Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/scoreRule/getScoreRule";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("uid",userInfo.get("uid"));
            if (JzbCheckParam.haveEmpty(param, new String[]{"pageno", "pagesize"})) {
                result = Response.getResponseError();
            } else {
                int count = JzbDataType.getInteger(param.get("count"));
                // 获取单位总数
                count = count < 0 ? 0 : count;
                if (count == 0) {
                    // 查询单位总数
                    count = tbScoreRuleService.getScoreRuleCount(param);
                }
            PageConvert pageConvert = new PageConvert();
            pageConvert.setPageRows(param);
            //获取用户信息

            //调用查询的方法进行数据的查询
            List<Map<String,Object>> list = tbScoreRuleService.getScoreRule(param);

            PageInfo pageInfo = new PageInfo();
            //设置响应的参数
            pageInfo.setList(list);
            //设置响应成功的结果
                pageInfo.setTotal(count);
            result = Response.getResponseSuccess(userInfo);
            result.setPageInfo(pageInfo);
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getScoreRule Method", ex.toString()));
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
