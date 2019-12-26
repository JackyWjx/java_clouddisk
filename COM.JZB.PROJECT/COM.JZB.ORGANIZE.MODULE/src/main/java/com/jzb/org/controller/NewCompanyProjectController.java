package com.jzb.org.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbPageConvert;
import com.jzb.base.util.JzbTools;
import com.jzb.base.util.StrUtil;
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
            if (JzbCheckParam.haveEmpty(param, new String[]{"pagesize", "pageno", "cid"})) {
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
                    proMap.put("cid", param.get("cid"));
                    if(!JzbTools.isEmpty(param.get("projectid"))) {
                        proMap.put("projectid",param.get("projectid"));
                        //获取单位下的项目
                        List<Map<String, Object>> proList = newCompanyProjectService.queryCompanyByid(proMap);
                        //获取项目下的情报
                        List<Map<String, Object>> infoList = newCompanyProjectService.queryCompanyByProjectid(proMap);
                        for (int l = 0, d = infoList.size();l < d;l++){
                            if(!JzbTools.isEmpty(infoList.get(l).get("prolist"))) {
                                List<String> prolistArr = StrUtil.string2List(infoList.get(l).get("prolist").toString(), ",");
                                infoList.get(l).put("prolistArr",prolistArr);
                            }
                        }
                        list.get(i).put("reList",proList);
                        list.get(i).put("infoList",infoList);
                    }
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

    /**
     * @Author sapientia
     * @Date 19:40 2019/12/13
     * @Description
     **/
    @PostMapping("/queryPronameByid")
    @Transactional
    public Response queryPronameByid(@RequestBody Map<String, Object> param){
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/org/companyproject/queryPronameByid";
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
                List<Map<String,Object>> list = newCompanyProjectService.queryPronameByid(param);
                response = Response.getResponseSuccess(userInfo);
                PageInfo pageInfo = new PageInfo();
                pageInfo.setList(list);
                response.setPageInfo(pageInfo);
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

    /**
     *  @author: gongWei
     *  @Date:  2019/12/20 19:33
     *  @Description:  获取拜访单位的基本信息
     *  @Param:
     *  @Return:
     *  @Exception:
     */

    @CrossOrigin
    @RequestMapping(value = "/getCompanyInfoByCid", method = RequestMethod.POST)
    public Response getCompanyInfoByCid(@RequestBody Map<String,Object> param){
        Response response;
        Map<String,Object> userInfo = null;
        String api = "/org/companyproject/getCompanyInfoByCid";
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
                Map<String,Object> resultInfo = newCompanyProjectService.getCompanyInfoByCid(param);
                param.put("prolist", StrUtil.string2List(param.get("prolist").toString(),","));
                response = Response.getResponseSuccess(userInfo);
                response.setResponseEntity(resultInfo);

            }
        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getCompanyInfoByCid Method", ex.toString()));
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
     *  @author: gongWei
     *  @Date:  2019/12/21 9:39
     *  @Description:  获取拜访单位下的所有项目的详细信息
     *  @param param
     *  @Return:
     *  @Exception:
     */
    @RequestMapping(value = "/getProjectInfoList" , method = RequestMethod.POST)
    @CrossOrigin
    public Response getProjectInfoList(@RequestBody Map<String, Object> param){
        Response response;
        Map<String,Object> userInfo = null;
        String api = "/org/companyproject/getProjectInfoList";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }

            if (JzbCheckParam.haveEmpty(param, new String[]{"projectId"})) {
                response = Response.getResponseError();
            } else {
                Map<String,Object> resultInfo = newCompanyProjectService.getProjectInfoByProid(param);
                response = Response.getResponseSuccess(userInfo);
                response.setResponseEntity(resultInfo);
            }
        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getProjectInfoList Method", ex.toString()));
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
