package com.jzb.operate.controller;

import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.operate.service.TbResourceViewService;
import com.jzb.operate.service.TbResourceVotesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.awt.geom.FlatteningPathIterator;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chenzhengduan
 * 活动点赞（无登录）
 */
@RestController
@RequestMapping(value = "/resourceVotes")
public class TbResourceVotesController {

    @Autowired
    private TbResourceVotesService tbResourceVotesService;

    @Autowired
    private TbResourceViewService tbResourceViewService;

    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(TbResourceVotesController.class);

    /**
     * 点赞
     * @param params
     * @return
     */
    @RequestMapping(value = "/addResourceVotes", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response addResourceVotes(@RequestBody Map<String, Object> params) {
        Response result;
        Map<String, Object> userInfo = null;
        String  api="/resourceVotes/addResourceVotes";
        boolean flag = true;
        try {
            if (params.get("userinfo") != null) {
                userInfo = (Map<String, Object>) params.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger( api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger( api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            // 验证指定参数为空则返回error
            if (JzbCheckParam.haveEmpty(params,new String[]{"actid","ouid"})) {
                result = Response.getResponseError();
                logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "addResourceVotes Method", "[param error] or [param is null]"));

            } else {
                params.put("restype", "R0001");
                // 获取是否存在
                int isAlready = tbResourceVotesService.queryIsAlreadyVotes(params);
                // 判断如果该用户点过赞了就不允许点了
                if (isAlready <= 0) {
                    // 添加时间
                    params.put("addtime",System.currentTimeMillis());
                    // 添加结果
                    int count = tbResourceVotesService.addResourceVotes(params);
                    // 更新活动表数据
//                    int countVotes=tbResourceVotesService.updateActivityVotes(params);
                    if (count > 0) {
                        // 定义返回结果
                        result = Response.getResponseSuccess();
                    } else {
                        result = Response.getResponseError();
                    }
                }else {
                    result = Response.getResponseError();
                }
            }
        } catch (Exception e) {
            flag=false;
            // 打印异常信息
            e.printStackTrace();
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "addResourceVotes Method", e.toString()));
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
     * 获取点赞次数
     * @param params
     * @return
     */
    @RequestMapping(value = "/getResourceVotes",method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response getResourceVotes(@RequestBody Map<String ,Object> params){
        Response result;
        Map<String, Object> userInfo = null;
        String  api="/resourceVotes/getResourceVotes";
        boolean flag = true;
        try {
            if (params.get("userinfo") != null) {
                userInfo = (Map<String, Object>) params.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger( api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger( api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            // 验证指定参数为空则返回error
            if (JzbCheckParam.haveEmpty(params,new String[]{"actid"})) {
                result = Response.getResponseError();
                logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getResourceVotes Method", "[param error] or [param is null]"));
            } else {
                params.put("restype","R0001");
                Map<String, Integer> votesMap = tbResourceVotesService.queryResourceVotes(params);
                Map<String, Integer>  viewMap= tbResourceViewService.queryResourceView(params);
                // 定义返回结果
                result = Response.getResponseSuccess();
                Map<String, Object> map=new HashMap();
                map.put("votes",votesMap);
                map.put("view",viewMap);
                result.setResponseEntity(map);
            }
        } catch (Exception e) {
            flag=false;
            // 打印异常信息
            e.printStackTrace();
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getResourceVotes Method", e.toString()));
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