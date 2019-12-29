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
                if (!JzbCheckParam.haveEmpty(param, new String[]{"cddid"})) {
                    //根据部门id查询其下的角色
                    List<Map<String, Object>> list = tbPlantaskJobPositionService.selectRoleByDeptid(param);
                    param.put("crid", list);
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
                    resultMap.put("crgcontent", dept.get(resultMap.get("crid")));
                    resultMap.put("dept", list.get(reid.get("crid")));
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
                //记录角色
                List<Map<String, Object>> crContent = new ArrayList<>();
                //记录其余字段
                List<String> contentList = new ArrayList<>();
                //将部门和角色名弄进去
                for (int i = 0, j = lists.size(); i < j; i++) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("crcontent", JzbDataType.getString(lists.get(i).get("crcontent")));
                    map.put("cddid", JzbDataType.getString(lists.get(i).get("cddid")));
                    crContent.add(map);
                }
                for (int i = 0, j = lists.size(); i < j; i++) {
                    contentList.add(JzbDataType.getString(lists.get(i).get("dutycontent")));
                    contentList.add(JzbDataType.getString(lists.get(i).get("workcontent")));
                    contentList.add(JzbDataType.getString(lists.get(i).get("outputcontent")));
                    contentList.add(JzbDataType.getString(lists.get(i).get("workstandarcontent")));
                    contentList.add(JzbDataType.getString(lists.get(i).get("kpicontent")));
                }
                param.put("crContent", crContent);
                List<Map<String, Object>> Crlist = tbPlantaskJobDutyService.selectExistCrContent(param);
                param.put("contentList", contentList);
                List<Map<String, Object>> list = tbPlantaskJobDutyService.selectExistContent(param);
                for (int i = crContent.size() - 1; i >= 0; i--) {
                    for (int a = 0, b = Crlist.size(); a < b; a++) {
                        if (crContent.get(i).get("crcontent").equals(Crlist.get(a).get("crcontent"))) {
                            crContent.remove(i);
                            break;
                        }
                    }
                }
                //crContent是结果
                for (int i = contentList.size() - 1; i >= 0; i--) {
                    for (int a = 0, b = list.size(); a < b; a++) {
                        if (contentList.get(i).equals(list.get(a).get("content"))) {
                            contentList.remove(i);
                            break;
                        }
                    }
                }
                List<Map<String, Object>> crMap = new ArrayList<>();
                //contentList是结果.接下来生成那些不存在的id值
                for (int i = 0, j = crContent.size(); i < j; i++) {
                    crContent.get(i).put("crid", JzbRandom.getRandomChar(26));
                    Crlist.add(crContent.get(i));
                    crMap.add(crContent.get(i));
                }
                for (int i = 0, j = contentList.size(); i < j; i++) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("content", contentList.get(i));
                    map.put("uniqueid", JzbRandom.getRandomChar(26));
                    list.add(map);
                }
                for (int i = 0, j = Crlist.size(); i < j; i++) {
                    list.add(Crlist.get(i));
                }

                //此时list是所有的那个啥
                //执行添加功能
                for (int i = 0, j = lists.size(); i < j; i++) {
                    for (int a = 0, b = list.size(); a < b; a++) {
                        lists.get(i).put("jobdutyid", JzbRandom.getRandomChar(26));
                        if (lists.get(i).get("crcontent").equals(list.get(a).get("crcontent"))) {
                            lists.get(i).put("crid", list.get(a).get("crid"));
                        }
                        if (lists.get(i).get("dutycontent").equals(list.get(a).get("content"))) {
                            lists.get(i).put("dutyid", list.get(a).get("uniqueid"));
                        }
                        if (lists.get(i).get("workcontent").equals(list.get(a).get("content"))) {
                            lists.get(i).put("workid", list.get(a).get("uniqueid"));
                        }
                        if (lists.get(i).get("outputcontent").equals(list.get(a).get("content"))) {
                            lists.get(i).put("outputid", list.get(a).get("uniqueid"));
                        }
                        if (lists.get(i).get("workstandarcontent").equals(list.get(a).get("content"))) {
                            lists.get(i).put("workstandardid", list.get(a).get("uniqueid"));
                        }
                        if (lists.get(i).get("kpicontent").equals(list.get(a).get("content"))) {
                            lists.get(i).put("kpiid", list.get(a).get("uniqueid"));
                        }

                    }
                }
                List<Map<String, Object>> res = new ArrayList<>();
                for (Map<String, Object> map : list) {
                    for (String str : contentList) {
                        if (str.equals(map.get("content"))) {
                            res.add(map);
                            break;
                        }
                    }
                }
                //此时lists已变成id的样子
                //行插入
                param.put("lists", lists);
                param.put("adduid",userInfo.get("uid"));
                param.put("addtime",System.currentTimeMillis());
                //字典库
                param.put("list", res);
                //角色字典库
                param.put("crMap", crMap);
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

    //查询所有角色及其职责
    @RequestMapping(value = "/selectAllRoleAndDuty", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response selectAllRoleAndDuty(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/resource/jobResponsibilities/selectAllRoleAndDuty";
        boolean flag = true;
        try {
            if (param != null && param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            //1.查询所有角色
            List<Map<String, Object>> roles = tbPlantaskJobPositionService.getRoles(param);
            //根据角色查询全部职责
            param.put("list",roles);
            List<Map<String, Object>> list = tbPlantaskJobDutyService.selectAllDutyByRole(param);
            for(int i = 0 ,j = roles.size();i<j;i++){
                List<Map<String,Object>> tempList = new ArrayList<>();
                for(int a = 0 ,b = list.size();a<b;a++){
                    if(roles.get(i).get("crid").equals(list.get(a).get("crid"))){
                        tempList.add(list.get(a));
                    }
                }
                roles.get(i).put("children",tempList);
            }
            response = Response.getResponseSuccess();
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(roles);
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
}
