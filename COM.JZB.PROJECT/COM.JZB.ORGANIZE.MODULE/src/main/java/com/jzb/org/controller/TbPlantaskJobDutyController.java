package com.jzb.org.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbPageConvert;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.org.service.TbPlantaskJobDutyService;
import com.jzb.org.service.TbPlantaskJobPositionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/org/jobResponsibilities")
public class TbPlantaskJobDutyController {


    @Autowired
    private TbPlantaskJobDutyService tbPlantaskJobDutyService;

    @Autowired
    private TbPlantaskJobPositionService tbPlantaskJobPositionService;

    private final static Logger logger = LoggerFactory.getLogger(TbPlantaskJobDutyController.class);


    @RequestMapping(value = "/getJobResponsibilities", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response getJobResponsibilities(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/resource/jobResponsibilities/getJobResponsibilities";
        boolean flag = true;
        try {
            if (param != null && param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            if (JzbCheckParam.haveEmpty(param, new String[]{"pageno", "pagesize"})) {
                response = Response.getResponseError();
                logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getContractTemplate Method", "[param error] or [param is null]"));
            } else {
                JzbPageConvert.setPageRows(param);
                List<Map<String, Object>> roleList;
                //1.查询Jzb角色
                roleList = tbPlantaskJobPositionService.getRoles(param);
                //判断是否查询部门
                if(!JzbCheckParam.haveEmpty(param,new String[]{"cddid"})){
                    //根据部门id查询其下的角色
                    List<Map<String, Object>> list = tbPlantaskJobPositionService.selectRoleByDeptid(param);
                    param.put("crid",list);
                }
                //2.查询角色ID的所有岗位职责
                List<Map<String, Object>> resultId = tbPlantaskJobDutyService.getAllIdByCid(param);
                //3.查询所有的岗位职责信息
                List<Map<String, Object>> content = tbPlantaskJobDutyService.getAllJobRBE(param);

                List<Map<String, Object>> result = new ArrayList<>();
                Map<String, Object> map = new HashMap<>();
                Map<String, Object> list = new HashMap<>();
                Map<String, Object> dept = new HashMap<>();

                for (int i = 0, a = content.size(); i < a; i++) {
                    map.put(content.get(i).get("uniqueid").toString(), content.get(i).get("content"));
                }
                for (int i = 0, a = roleList.size(); i < a; i++) {
                    list.put(roleList.get(i).get("crid").toString(), roleList.get(i).get("content"));
                }
                for (int i = 0, a = roleList.size(); i < a; i++) {
                    dept.put(roleList.get(i).get("crid").toString(), roleList.get(i).get("cname"));
                }
                //4.匹配岗位职责信息
                for (Map reid : resultId) {
                    Map<String, Object> resultMap = new HashMap<>();
                    resultMap.put("crid", reid.get("crid"));
                    resultMap.put("crgcontent",dept.get(resultMap.get("crid")));
                    resultMap.put("dept",list.get(reid.get("crid")));
                    resultMap.put("dutyid", reid.get("dutyid"));
                    resultMap.put("dutycontent", map.get(reid.get("dutyid")));
                    resultMap.put("workid", reid.get("workid"));
                    resultMap.put("workcontent", map.get(reid.get("workid")));
                    resultMap.put("outputid", reid.get("outputid"));
                    resultMap.put("outputcontent", map.get(reid.get("outputid")));
                    resultMap.put("workstandardid", reid.get("workstandardid"));
                    resultMap.put("workstandardcontent", map.get(reid.get("workstandardid")));
                    resultMap.put("kpiid", reid.get("kpiid"));
                    resultMap.put("kpicontent", map.get(reid.get("kpiid")));
                    resultMap.put("jobdutyid", reid.get("jobdutyid"));
                    result.add(resultMap);
                }


                response = Response.getResponseSuccess();
                PageInfo pageInfo = new PageInfo();
                pageInfo.setList(result);
                pageInfo.setTotal(tbPlantaskJobDutyService.getAllCount(param));
                response.setPageInfo(pageInfo);
            }
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

    @RequestMapping(value = "/addJobResponsibilities", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response addJobResponsibilities(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/resource/jobResponsibilities/addJobResponsibilities";
        boolean flag = true;
        try {
            if (param != null && param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            if (JzbCheckParam.haveEmpty(param, new String[]{"list"})) {
                response = Response.getResponseError();
                logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getContractTemplate Method", "[param error] or [param is null]"));
            } else {
                List<Map<String, Object>> lists = (List<Map<String, Object>>) param.get("list");
                for (int i = 0, j = lists.size(); i < j; i++) {
                    lists.get(i).put("adduid", userInfo.get("uid"));
                    lists.get(i).put("addtime", System.currentTimeMillis());
                    lists.get(i).put("jobdutyid", JzbRandom.getRandomChar(26));
                    //查询该字段是否存在
                    String crcontent = tbPlantaskJobDutyService.selectContentIsNotExist(JzbDataType.getString(lists.get(i).get("crcontent")));
                    if(!("").equals(crcontent)&&crcontent!=null){
                        lists.get(i).put("crid",crcontent);
                        lists.get(i).put("cridExist",'1');
                    }else {
                        lists.get(i).put("crid", JzbRandom.getRandomChar(26));
                    }
                    //查询该字段是否存在
                    String dutycontent = tbPlantaskJobDutyService.selectDutyContentIsNotExist(JzbDataType.getString(lists.get(i).get("dutycontent")));
                    if(!("").equals(dutycontent)&&dutycontent!=null){
                        lists.get(i).put("dutyid",dutycontent);
                        lists.get(i).put("dutyidExist",'1');
                    }else {
                        lists.get(i).put("dutyid", JzbRandom.getRandomChar(26));
                    }
                    String workcontent = tbPlantaskJobDutyService.selectWorkContentIsNotExist(JzbDataType.getString(lists.get(i).get("workcontent")));
                    if(!("").equals(workcontent)&&workcontent!=null){
                        lists.get(i).put("workid",workcontent);
                        lists.get(i).put("workidExist",'1');
                    }else {
                        lists.get(i).put("workid", JzbRandom.getRandomChar(26));
                    }
                    String outputcontent = tbPlantaskJobDutyService.selectOutputContentIsNotExist(JzbDataType.getString(lists.get(i).get("outputcontent")));
                    if(!("").equals(workcontent)&&workcontent!=null){
                        lists.get(i).put("outputid",outputcontent);
                        lists.get(i).put("outputidExist",'1');
                    }else {
                        lists.get(i).put("outputid", JzbRandom.getRandomChar(26));
                    }
                    String workstandarcontent = tbPlantaskJobDutyService.selectWorkstandarContentIsNotExist(JzbDataType.getString(lists.get(i).get("workstandarcontent")));
                    if(!("").equals(workcontent)&&workcontent!=null){
                        lists.get(i).put("workstandardid",workstandarcontent);
                        lists.get(i).put("workstandardidExist",'1');
                    }else {
                        lists.get(i).put("workstandardid", JzbRandom.getRandomChar(26));
                    }
                    String kpicontent = tbPlantaskJobDutyService.selectkpiContentIsNotExist(JzbDataType.getString(lists.get(i).get("kpicontent")));
                    if(!("").equals(workcontent)&&workcontent!=null){
                        lists.get(i).put("kpiid",kpicontent);
                        lists.get(i).put("kpiidExist",'1');
                    }else {
                        lists.get(i).put("kpiid", JzbRandom.getRandomChar(26));
                    }
                }
                param.put("lists", lists);
                response = tbPlantaskJobDutyService.insertJobResponsibilities(param) > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
            }
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

    @RequestMapping(value = "/updateJobResponsibilities", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response updateJobResponsibilities(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/resource/jobResponsibilities/updateJobResponsibilities";
        boolean flag = true;
        try {
            if (param != null && param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            if (JzbCheckParam.haveEmpty(param, new String[]{"list"})) {
                response = Response.getResponseError();
                logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getContractTemplate Method", "[param error] or [param is null]"));
            } else {
                List<Map<String, Object>> lists = (List<Map<String, Object>>) param.get("list");

                param.put("upuid", userInfo.get("uid"));
                param.put("uptime", System.currentTimeMillis());

                response = tbPlantaskJobDutyService.updateJobResponsibilities(param) > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseSuccess();
            }
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

    @RequestMapping(value = "/updateJobResponsibilitiesDelStatus", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response updateJobResponsibilitiesDelStatus(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/resource/jobResponsibilities/updateJobResponsibilitiesDelStatus";
        boolean flag = true;
        try {
            if (param != null && param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            if (JzbCheckParam.haveEmpty(param, new String[]{"list"})) {
                response = Response.getResponseError();
                logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getContractTemplate Method", "[param error] or [param is null]"));
            } else {
                param.put("upuid", userInfo.get("uid"));
                param.put("uptime", System.currentTimeMillis());
                response = tbPlantaskJobDutyService.updateJobResponsibilitiesDelStatus(param) > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseSuccess();
            }
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

    @RequestMapping(value = "/selectDutyByCid", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response selectDutyByCid(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/resource/jobResponsibilities/selectDutyByCid";
        boolean flag = true;
        try {
            if (param != null && param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            if (JzbCheckParam.haveEmpty(param, new String[]{"crid"})) {
                response = Response.getResponseError();
                logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getContractTemplate Method", "[param error] or [param is null]"));
            } else {

                List<Map<String, Object>> list = tbPlantaskJobDutyService.selectDutyByCid(param);
                response = Response.getResponseSuccess();
                PageInfo pageInfo = new PageInfo();
                pageInfo.setList(list);
                response.setPageInfo(pageInfo);
            }
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

    //根据部门id查询角色
    @RequestMapping(value = "/selectRoleByDeptid", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response selectRoleByDeptid(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/resource/jobResponsibilities/selectRoleByDeptid";
        boolean flag = true;
        try {
            if (param != null && param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            if (JzbCheckParam.haveEmpty(param, new String[]{"cddid"})) {
                response = Response.getResponseError();
                logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getContractTemplate Method", "[param error] or [param is null]"));
            } else {

                List<Map<String, Object>> list = tbPlantaskJobPositionService.selectRoleByDeptid(param);
                response = Response.getResponseSuccess();
                PageInfo pageInfo = new PageInfo();
                pageInfo.setList(list);
                response.setPageInfo(pageInfo);
            }
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
