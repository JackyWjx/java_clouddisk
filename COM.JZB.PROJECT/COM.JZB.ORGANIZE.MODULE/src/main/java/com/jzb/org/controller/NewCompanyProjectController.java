package com.jzb.org.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbPageConvert;
import com.jzb.base.util.JzbTools;
import com.jzb.org.service.NewCompanyProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author sapientia
 * @Date 2019/12/4 14:18
 */
@RestController
@RequestMapping(value = "/org/companyproject")
public class NewCompanyProjectController {

    @Autowired
    private NewCompanyProjectService newCompanyProjectService;

    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(NewCompanyProjectController.class);
    /**
     * @Author sapientia
     * @Date  14:48
     * @Description  查询项目详情
     **/
    @RequestMapping(value = "/queryCompanyByid" , method = RequestMethod.POST)
    @CrossOrigin
    public Response queryCompanyByid(@RequestBody Map<String, Object> param){
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/org/companyproject/queryCompanyByid";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            if (JzbCheckParam.haveEmpty(param, new String[]{"pagesize", "pageno", "list"})) {
                response = Response.getResponseError();
            } else {
                JzbPageConvert.setPageRows(param);
                param.put("cid",param.get("cid").toString().trim());
                //获取单位信息
                List<Map<String, Object>> list = newCompanyProjectService.queryCommonCompanyListBycid(param);
                for (int i = 0 ,a = list.size(); i < a; i++) {
                    Map<String, Object> proMap = new HashMap<>();
                    proMap.put("pageno",param.get("pageno"));
                    proMap.put("pagesize",param.get("pagesize"));
                    proMap.put("cid", list.get(i).get("cid").toString().trim());
                    //获取单位下的项目
                    List<Map<String, Object>> prolist = newCompanyProjectService.queryCompanyByid(proMap);
                    for (int j = 0,b = prolist.size(); j < b; j++) {
                        Map<String, Object> infoMap = new HashMap<>();
                        infoMap.put("pageno",param.get("pageno"));
                        infoMap.put("pagesize",param.get("pagesize"));
                        infoMap.put("projectid", prolist.get(j).get("projectid").toString().trim());
                        //获取项目下的情报
                        List<Map<String, Object>> infolist = newCompanyProjectService.queryCompanyByProjectid(infoMap);
                        list.get(j).put("infoList", infolist);
                    }
                    list.get(i).put("prolist", prolist);
                }
                response = Response.getResponseSuccess(userInfo);
                PageInfo pageInfo = new PageInfo();
                pageInfo.setTotal(newCompanyProjectService.countProjectInfo(param));
                pageInfo.setList(list);
                response.setPageInfo(pageInfo);
            }
        }catch (Exception ex) {
                flag = false;
                JzbTools.logError(ex);
                response = Response.getResponseError();
                logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "queryCompanyByid Method", ex.toString()));
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
     * @Author sapientia
     * @Date 18:05 2019/12/12
     * @Description 获取单位下的人
     **/
    @RequestMapping(value = "/queryCompanyNameBycid" , method = RequestMethod.POST)
    @CrossOrigin
    public Response queryCompanyNameBycid(@RequestBody Map<String, Object> param){
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/org/companyproject/queryCompanyNameBycid";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            if (JzbCheckParam.haveEmpty(param, new String[]{"cid"})) {
                response = Response.getResponseError();
            } else {
                JzbPageConvert.setPageRows(param);
                param.put("cid",param.get("cid").toString().trim());
                //获取单位信息
                List<Map<String, Object>> list = newCompanyProjectService.queryCompanyNameBycid(param);
                response = Response.getResponseSuccess(userInfo);
                PageInfo pageInfo = new PageInfo();
                pageInfo.setList(list);
                response.setPageInfo(pageInfo);
            }
        }catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "queryCompanyNameBycid Method", ex.toString()));
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
     * @Author sapientia
     * @Date  15:01
     * @Description 修改项目信息
     **/
    @RequestMapping(value = "/updateCompanyProject",method = RequestMethod.POST)
    @Transactional
    public Response updateCompanyProject(@RequestBody Map<String, Object> param){
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/org/companyproject/updateCompanyProject";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            if (JzbCheckParam.haveEmpty(param, new String[]{"projectid"})) {
                response = Response.getResponseError();
            } else {
                param.put("uid",userInfo.get("uid"));
                param.put("updtime", System.currentTimeMillis());
                response = newCompanyProjectService.updateCompanyProject(param) > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
            }
        }catch (Exception ex) {
                flag = false;
                JzbTools.logError(ex);
                response = Response.getResponseError();
                logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "updateCompanyProject Method", ex.toString()));
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
     * @Author sapientia
     * @Date  18:11
     * @Description 修改项目详情信息
     **/
    @PostMapping("/updateCompanyProjectInfo")
    @Transactional
    public Response updateCompanyProjectInfo(@RequestBody Map<String, Object> param){
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/org/companyproject/updateCompanyProjectInfo";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            if (JzbCheckParam.haveEmpty(param, new String[]{"projectid"})) {
                response = Response.getResponseError();
            } else {
                param.put("uid",userInfo.get("uid"));
                param.put("updtime", System.currentTimeMillis());
                response = newCompanyProjectService.updateCompanyProjectInfo(param) > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
            }
        }catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "updateCompanyProjectInfo Method", ex.toString()));
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
     * @Author sapientia
     * @Date  18:12
     * @Description 修改单位信息
     **/
    @PostMapping("/updateCommonCompanyList")
    @Transactional
    public Response updateCommonCompanyList(@RequestBody Map<String, Object> param){
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/org/companyproject/updateCommonCompanyList";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            if (JzbCheckParam.haveEmpty(param, new String[]{"cid"})) {
                response = Response.getResponseError();
            } else {
                param.put("uid",userInfo.get("uid"));
                param.put("updtime", System.currentTimeMillis());
                response = newCompanyProjectService.updateCommonCompanyList(param) > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
            }
        }catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "updateCommonCompanyList Method", ex.toString()));
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
