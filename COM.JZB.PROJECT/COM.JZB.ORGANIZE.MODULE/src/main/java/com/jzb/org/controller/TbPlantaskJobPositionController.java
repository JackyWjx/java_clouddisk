package com.jzb.org.controller;

import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.org.service.TbPlantaskJobPositionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/org/roleDept")
public class TbPlantaskJobPositionController {

    @Autowired
    private TbPlantaskJobPositionService tbPlantaskJobPositionService;

    private final static Logger logger = LoggerFactory.getLogger(TbPlantaskJobPositionController.class);


    @RequestMapping(value = "/getRoles", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response getRoles(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/resource/roleDept/getRoles";
        boolean flag = true;
        try {
            if (param != null && param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            //1.得到所有部门
            List<Map<String, Object>> depts = tbPlantaskJobPositionService.getDepts(param);
            //2.得到所有角色
            List<Map<String,Object>> roles = tbPlantaskJobPositionService.getRoles(param);
            //3.将角色添加到部门下
            for(int i = 0,j=depts.size();i<j;i++){
                List<Map<String,Object>> list = new ArrayList<>();
                for (int a = 0,b=roles.size();a<b;a++){
                    roles.get(a).put("cdid",roles.get(a).get("crid"));
                    if(depts.get(i).get("cdid").equals(roles.get(a).get("cddid"))){
                        list.add(roles.get(a));
                    }
                }
                depts.get(i).put("children",list);
            }

            response = Response.getResponseSuccess();
            response.setResponseEntity(depts);

        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getNewAppVersion Method", ex.toString()));
        }
        if (userInfo != null) {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
                    userInfo.get("msgTag").toString(), "User Login Message"));
        } else {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
        }
        return response;
    }

    @RequestMapping(value = "/getDepts", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response getDepts(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/resource/roleDept/getDepts";
        boolean flag = true;
        try {
            if (param != null && param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }

            List<Map<String, Object>> depts = tbPlantaskJobPositionService.getDepts(param);
            response = Response.getResponseSuccess();
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(depts);
            pageInfo.setTotal(depts.size());
            response.setPageInfo(pageInfo);

        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getNewAppVersion Method", ex.toString()));
        }
        if (userInfo != null) {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
                    userInfo.get("msgTag").toString(), "User Login Message"));
        } else {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
        }
        return response;
    }

    @RequestMapping(value = "/addRoleAndDept", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response addRoleAndDept(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/resource/roleDept/getDepts";
        boolean flag = true;
        try {
            if (param != null && param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            param.put("crgid", JzbRandom.getRandomChar(26));
            response = tbPlantaskJobPositionService.addRoleAndDept(param)>0?Response.getResponseSuccess(userInfo):Response.getResponseError();
        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getNewAppVersion Method", ex.toString()));
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
