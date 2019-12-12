package com.jzb.operate.controller;

import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbPageConvert;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.operate.api.org.TbDeptUserListApi;
import com.jzb.operate.service.TbTravelApprovalService;
import com.jzb.operate.service.TbTravelPlanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author ：Champ-Ping
 * @date ：Created in 2019-12-04 20:09
 * @description：出差/报销申请
 * @modified By：
 * @version: 1.0$
 */
@RestController
@RequestMapping(value = "/operate/travelApproval")
public class TbTravelApprovalController {

    @Autowired
    private TbTravelApprovalService travelApprovalService;
    @Autowired
    private TbTravelPlanService travelPlanService;
    @Autowired
    private TbDeptUserListApi tbDeptUserListApi;
    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(TbTravelPlanController.class);


    /**
     * 添加出差报销申请
     * @param param
     * @return
     */
    @CrossOrigin
    @Transactional
    @PostMapping("/addTravelApproval")
    public Response addTravelApproval(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/operate/travelApproval/addTravelApproval";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }if (JzbCheckParam.haveEmpty(param, new String[]{"list"})) {
                response = Response.getResponseError();
            } else {
                List<Map<String, Object>> approvalList = (List<Map<String, Object>>) param.get("list");
                for (Map<String, Object> approval : approvalList) {
                    approval.put("adduid", userInfo.get("uid"));
                    approval.put("travelid", param.get("travelid"));
                    approval.put("apid", JzbRandom.getRandomChar(12));
                    approval.put("addtime", System.currentTimeMillis());
                    //默认状态
                    approval.put("trstatus", 1);
                    approval.put("version",param.get("version"));
                    travelApprovalService.save(approval);
                }
                travelPlanService.updateTravelRecord(param);
                response = Response.getResponseSuccess(userInfo);
            }
        }catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "addTravelApproval Method", ex.toString()));
        }
        if (userInfo != null) {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
                    userInfo.get("msgTag").toString(), "User Login Message"));
        } else {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
        }
        return response;
    }

    /**
     * 批量修改出差报销申请 (预留)
     * @param param
     * @return
     */
    @CrossOrigin
    @Transactional
    @PostMapping("/updateTravelApproval")
    public Response updateTravelApproval(@RequestBody Map<String, Object> param) {

        Response response;

        try {
            List<Map<String,Object>> approvalList = (List<Map<String, Object>>) param.get("list");
            for (Map<String,Object> approval : approvalList){

                approval.put("travelid",param.get("travelid"));
                travelApprovalService.update(approval);
            }

            travelPlanService.updateTravelRecord(param);
            response = Response.getResponseSuccess((Map<String, Object>) param.get("userinfo"));
        }catch (Exception e) {
            response = Response.getResponseError();
        }
        return response;
    }

    /**
     * 同意出差申请
     * param中添加
     * "isOk" : "0" 表示退回
     * "isOk" : "1" 表示同意
     * @param param
     * @return
     */
    @CrossOrigin
    @Transactional
    @PostMapping("/setTravelApproval")
    public Response setTravelApproval(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/operate/travelApproval/addTravelApproval";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }if (JzbCheckParam.haveEmpty(param, new String[]{"list"})) {
                response = Response.getResponseError();
            } else {
                param.put("trtime", System.currentTimeMillis());//审批时间
                Integer isOk = (Integer) param.get("isOk");
                //审批时间
                param.put("trtime", System.currentTimeMillis());
                if (isOk == 1) {// 同意
                    //判断是否是最后一级审批人
                    String lastApid = travelApprovalService.getMaxIdxApid((String) param.get("travelid"));
                    if (param.get("apid").equals(lastApid)) {
                        param.put("trstatus", 3);
                    } else {
                        param.put("trstatus", 2);
                    }
                } else {// 退回
                    param.put("trstatus", 4);
                    //更新版本号
                    param.put("version", JzbRandom.getRandom(8));
                }
                travelApprovalService.update(param);
                response = Response.getResponseSuccess(userInfo);
            }
        }catch (Exception ex) {
                flag = false;
                JzbTools.logError(ex);
                response = Response.getResponseError();
                logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "addTravelApproval Method", ex.toString()));
            }
            if (userInfo != null) {
                logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
                        userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
            }
            return response;
    }



    /**
     * 查询显示出差申请
     * @param param
     * @return
     */
    @CrossOrigin
    @Transactional
    @PostMapping("/selectTravelApproval")
    public Response setTravelApprovalOk(@RequestBody Map<String, Object> param) {
        Response response;
        try {

            PageInfo pageInfo = new PageInfo();
            List<Map<String , Object>>  approvalList = travelApprovalService.list(param);
            long count = travelApprovalService.count(param);
            pageInfo.setList(approvalList);
            pageInfo.setTotal(count);
            response = Response.getResponseSuccess();
            response.setPageInfo(pageInfo);
            response = Response.getResponseSuccess((Map<String, Object>) param.get("userinfo"));

        }catch (Exception e) {
            response = Response.getResponseError();
        }
        return response;
    }

    /**
     * @Author sapientia
     * @Date 17:04 2019/12/11
     * @Description 获取审批人列表
     **/
    @RequestMapping(value = "/queryOtherPersonBycid", method = RequestMethod.POST)
    @CrossOrigin
    public Response queryOtherPersonByuid(@RequestBody Map<String, Object> param){
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/operate/travelApproval/queryOtherPersonBycid";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            param.put("userinfo",userInfo);
            Response res = tbDeptUserListApi.queryOtherPersonBycid(param);
            List<Map<String, Object>> list = res.getPageInfo().getList();
            response = Response.getResponseSuccess(userInfo);
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(list);
            response.setPageInfo(pageInfo);

        }catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "queryOtherPersonByuid Method", ex.toString()));
        }
        if (userInfo != null) {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
                    userInfo.get("msgTag").toString(), "User Login Message"));
        } else {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
        }
        return response;
    }
}
