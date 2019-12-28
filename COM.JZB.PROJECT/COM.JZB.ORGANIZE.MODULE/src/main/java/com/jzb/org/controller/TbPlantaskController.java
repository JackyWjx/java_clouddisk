package com.jzb.org.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.jzb.base.log.JzbLoggerUtil;

import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbPageConvert;
import com.jzb.base.util.JzbRandom;

import com.jzb.org.api.auth.AuthApi;

import com.jzb.org.service.TbPlantaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author lifei
 * 计划管理
 */
@RestController
@RequestMapping(value = "/org/plantask")
public class TbPlantaskController {

    @Autowired
    private TbPlantaskService tbPlantaskService;
    @Autowired
    private AuthApi authApi;
    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(TbPlantaskController.class);
    /**
     * @param
     * @return TbTempItemController
     * @deprecated 周计划 年机会 部门计划 月计划 通用查询
     */
    @RequestMapping(value = "/getPlantaskItem", method = RequestMethod.POST)
    @ResponseBody
    public Response getPlantaskItem(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/org/plantask/getPlantaskItem";
        boolean flag = true;
        try {
            // 判断参数为空返回404
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            // 判断参数为空返回404

            // type=y 代表年计划
            // type=m 代表月份
            // type=d 代表天
            // type=w 代表周

            // dept=1 代表部门
            // dept=0 代表个人
            if (JzbCheckParam.haveEmpty(param, new String[]{"type","dept"})) {
                response = Response.getResponseError();
            } else {

                //参照tbTempitemController
                JzbPageConvert.setPageRows(param);
                param.put("node",param.get("dept").toString());
                switch(param.get("type").toString()){
                    case "y" :
                            param.put("tabname","tb_plantask_year");
                        break;
                    case "m" :
                        param.put("tabname","tb_plantask_month");
                        break;
                    case "d" :
                        param.put("tabname","tb_plantask_day");
                        break;
                    case "w" :
                        param.put("tabname","tb_plantask_week");
                        break;
                    default:
                        response = Response.getResponseError();
                }
                int count = tbPlantaskService.getPantaskCount(param);
                List<Map<String, Object>> records = tbPlantaskService.getPantaskList1(param);

                //处理 其他表数据 拼接 部门id 用户id数据拼接
                StringBuffer sb=new StringBuffer();
                List<Map<String, Object>> bumen= tbPlantaskService.getCompanydeptMap();
                for(int i=0,a=records.size();i<a;i++){
                    if (sb.toString().indexOf(records.get(i).get("uid").toString())==-1){
                        sb.append("'"+records.get(i).get("uid")+"',");
                    }
                    if(records.get(i).get("acceptors")!=null){
                        if(records.get(i).get("acceptors").toString().indexOf(",")>0){
                            for(int c=0,d=records.get(i).get("acceptors").toString().split(",").length;c<d;c++){
                                sb.append("'"+records.get(i).get("acceptors").toString().split(",")[c]+"',");
                            }
                        }else{
                            sb.append("'"+records.get(i).get("acceptors")+"',");
                        }
                    }
                    if(records.get(i).get("executors")!=null){
                        if(records.get(i).get("executors").toString().indexOf(",")>0){
                            for(int c=0,d=records.get(i).get("executors").toString().split(",").length;c<d;c++){
                                sb.append("'"+records.get(i).get("executors").toString().split(",")[c]+"',");
                            }
                        }else{
                            sb.append("'"+records.get(i).get("executors")+"',");
                        }
                    }
                    if( sb.toString().indexOf(records.get(i).get("addid").toString())==-1){
                        sb.append("'"+records.get(i).get("addid")+"',");
                    }
                    if(records.get(i).get("assistants")!=null){
                        if( records.get(i).get("assistants").toString().indexOf(",")>0){
                            for(int c=0,d=records.get(i).get("assistants").toString().split(",").length;c<d;c++){
                                sb.append("'"+records.get(i).get("assistants").toString().split(",")[c]+"',");
                            }
                        }else{
                            sb.append("'"+records.get(i).get("assistants")+"',");
                        }
                    }
                }

                param.put("ids",sb.toString().substring(0,sb.toString().length()-1));
                Response usnames = authApi.getUserNameList(param);

                List<Map<String, Object>> usernameList=usnames.getPageInfo().getList();
                Map<String,Object> usnamesMap=new HashMap<>();
                Map<String,Object> bumensMap=new HashMap<>();

                for (Map<String,Object> map:usernameList) {
                    usnamesMap.put(map.get("uid").toString(),map.get("cname"));
                }
                for (Map<String,Object> map:bumen) {
                    bumensMap.put(map.get("cdid").toString(),map.get("cname"));
                }
                for (int i=0,a=records.size();i<a;i++){
                    records.get(i).put("yname",usnamesMap.get(records.get(i).get("uid")));//姓名
                    records.get(i).put("bname",bumensMap.get(records.get(i).get("cdid")));//部门
                    //验收人
                    if(records.get(i).get("acceptors")!=null){
                        if(records.get(i).get("acceptors").toString().indexOf(",")>0){
                            StringBuffer acceptorsstr=new StringBuffer();
                            for(int c=0,d=records.get(i).get("acceptors").toString().split(",").length;c<d;c++){
                                acceptorsstr.append(usnamesMap.get(records.get(i).get("acceptors").toString().split(",")[c].toString())+",");
                            }
                            records.get(i).put("acceptorsname",acceptorsstr.toString().substring(0,acceptorsstr.toString().length()-1));//部门
                        }else{
                            records.get(i).put("acceptorsname",usnamesMap.get(records.get(i).get("acceptors").toString()));//部门
                        }
                    }

                    //执行人
                    if(records.get(i).get("executors")!=null){
                        if(records.get(i).get("executors").toString().indexOf(",")>0){
                            StringBuffer acceptorsstr=new StringBuffer();
                            for(int c=0,d=records.get(i).get("executors").toString().split(",").length;c<d;c++){
                                acceptorsstr.append(usnamesMap.get(records.get(i).get("executors").toString().split(",")[c].toString())+",");
                            }
                            records.get(i).put("executorsname",acceptorsstr.toString().substring(0,acceptorsstr.toString().length()-1));//部门
                        }else{
                            records.get(i).put("executorsname",usnamesMap.get(records.get(i).get("executors").toString()));//部门
                        }
                    }

                    //协助人
                    if(records.get(i).get("assistants")!=null){
                        if(records.get(i).get("assistants").toString().indexOf(",")>0){
                            StringBuffer acceptorsstr=new StringBuffer();
                            for(int c=0,d=records.get(i).get("assistants").toString().split(",").length;c<d;c++){
                                acceptorsstr.append(usnamesMap.get(records.get(i).get("assistants").toString().split(",")[c].toString())+",");
                            }
                            records.get(i).put("assistantsname",acceptorsstr.toString().substring(0,acceptorsstr.toString().length()-1));//部门
                        }else{
                            records.get(i).put("assistantsname",usnamesMap.get(records.get(i).get("assistants").toString()));//部门
                        }
                    }

                }

                // Result JSON
                JSONArray result = new JSONArray();

                // record temp json
                JSONObject recordJson = new JSONObject();

                // Unknown json
                JSONObject unknownRecord = new JSONObject();

                // root id
                String firstParent = "0";

                for (int i = 0, l = records.size(); i < l; i++) {
                    Map<String, Object> record = records.get(i);

                    // if parentid is null.
                    String parentId;
                    if (record.get("parentid") == null) {

                        parentId = "0";
                    } else {
                        parentId = record.get("parentid").toString();
                    }
                    // set default JSON and childern node
                    JSONObject node = new JSONObject();
                    node.put("planid", record.get("planid"));
                    node.put("plancontent", record.get("plancontent"));
                    node.put("parentid", parentId);
                    //node.put();
                    node.put("cdid",record.get("cdid"));
                    node.put( "uid" ,record.get("uid"));
                    node.put( "acceptors" ,record.get("acceptors"));
                    node.put( "executors" ,record.get("executors"));
                    node.put( "assistants" ,record.get("assistants"));
                    node.put( "outcome" ,record.get("outcome"));
                    node.put( "tasktype" ,record.get("tasktype"));
                    node.put( "taskstatus" ,record.get("taskstatus"));
                    node.put( "notes" ,record.get("notes"));
                    node.put( "path" ,record.get("path"));
                    node.put( "filenames" ,record.get("filenames"));
                    node.put( "addid" ,record.get("addid"));
                    node.put( "review" ,record.get("review"));
                    node.put( "parentid" ,record.get("parentid"));
                    node.put( "sort" ,record.get("sort"));
                    node.put( "node" ,record.get("node"));
                    node.put( "addtime" ,record.get("addtime"));
                    node.put( "uptime" ,record.get("uptime"));
                    node.put( "status" ,record.get("status"));
                    node.put( "ptype" ,record.get("ptype"));
                    node.put( "yname" ,record.get("yname"));
                    node.put( "bname" ,record.get("bname"));
                    node.put( "acceptorsname" ,record.get("acceptorsname"));
                    node.put( "executorsname" ,record.get("executorsname"));
                    node.put( "assistantsname" ,record.get("assistantsname"));
                    node.put("children", new JSONArray());

                    // if root node
                    if (parentId.equals(firstParent)) {
                        result.add(node);
                        recordJson.put(record.get("planid").toString(), node);

                        // if parent exist
                    } else if (recordJson.containsKey(parentId)) {
                        // add children
                        recordJson.getJSONObject(parentId).getJSONArray("children").add(node);
                        recordJson.put(record.get("planid").toString(), node);
                        // Unknown relation node
                    } else {
                        String nodeId = record.get("planid").toString();
                        if (unknownRecord.containsKey(parentId)) {
                            // add children
                            unknownRecord.getJSONObject(parentId).getJSONArray("children").add(node);
                            recordJson.put(nodeId, node);
                        } else {
                            // find subnode
                            for (Map.Entry<String, Object> entry : unknownRecord.entrySet()) {
                                JSONObject tempNode = (JSONObject) entry.getValue();
                                if (tempNode.getString("parentid").equals(nodeId)) {
                                    node.getJSONArray("children").add(tempNode);
                                    recordJson.put(tempNode.get("planid").toString(), tempNode);
                                    unknownRecord.remove(tempNode.get("planid").toString());
                                    break;
                                }
                            }
                            unknownRecord.put(nodeId, node);
                        }
                    }
                }
                // unknownRecord add to result
                // find subnode
                for (Map.Entry<String, Object> entry : unknownRecord.entrySet()) {
                    JSONObject tempNode = (JSONObject) entry.getValue();
                    String tempNodeId = tempNode.getString("parentid");
                    if (recordJson.containsKey(tempNodeId)) {
                        // add children
                        recordJson.getJSONObject(tempNodeId).getJSONArray("children").add(tempNode);
                        System.out.println("-0-0-0-0-00-0---------------"+ tempNodeId + "\t\t" + tempNode.toString());
                    } else {
                        // Error node
                        //recordJson.getJSONObject(tempNodeId).getJSONArray("children").add(tempNode);
                        System.out.println("========================ERROR>> " + tempNodeId + "\t\t" + tempNode.toString());
                    }
                }

            // 定义返回结果
            response = Response.getResponseSuccess(userInfo);
            response.setResponseEntity(result);
            }
        } catch (Exception e) {
            flag = false;
            // 打印异常信息
            e.printStackTrace();
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getMethodType Method", e.toString()));
        }

        if (userInfo != null) {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
        } else {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
        }
        return response;
    }

    /**
     * @param param
     * @return
     * @deprecated 添加计划管理同级
     */
    @RequestMapping(value = "/addPlantaskBrother", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response addPlantaskBrother(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/org/plantask/addPlantaskBrother";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            // 验证指定值为空则返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"cdid", "uid","dutyid","plancontent","starttime","endtime","acceptors","executors","assistants","tasktype","taskstatus","addid"})) {
                result = Response.getResponseError();
                logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "addMethodTypeBrother Method", "[param error] or [param is null]"));
            } else {

                List<Map<String, Object>> list = (List<Map<String, Object>>) param.get("list");

                param.put("planid", JzbRandom.getRandomChar(20));
                param.put("addtime",System.currentTimeMillis());
                // 添加返回值大于0 则添加成功
                int count = tbPlantaskService.addPlantaskBrother(list);
                if (count > 0) {
                    // 定义返回结果
                    result = Response.getResponseSuccess(userInfo);
                } else {
                    result = Response.getResponseError();
                  //  logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "addMethodTypeBrother Method", "[addResult error]"));
                }
            }
        } catch (Exception e) {
            flag = false;
            // 打印异常信息
            e.printStackTrace();
            result = Response.getResponseError();
           logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "addMethodTypeBrother Method", e.toString()));
        }
        if (userInfo != null) {
           logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
                    userInfo.get("msgTag").toString(), "User Login Message"));
        } else {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
        }
        return result;
    }

//    /**
//     * @param param
//     * @return
//     * @deprecated 添加计划管理子级
//     */
//    @RequestMapping(value = "/addPlantaskSon", method = RequestMethod.POST)
//    @ResponseBody
//    @CrossOrigin
//    public Response addPlantaskSon(@RequestBody Map<String, Object> param) {
//        Response result;
//        Map<String, Object> userInfo = null;
//        String api = "/org/plantask/addPlantaskSon";
//        boolean flag = true;
//        try {
//            if (param.get("userinfo") != null) {
//                userInfo = (Map<String, Object>) param.get("userinfo");
//                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
//                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
//            } else {
//               logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
//            }
//            // 验证指定值为空则返回404
//            if (JzbCheckParam.haveEmpty(param, new String[]{"cdid", "uid","dutyid","plancontent","starttime","endtime","acceptors","executors","assistants","tasktype","taskstatus","addid", "parentid"})) {
//                result = Response.getResponseError();
//                logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "addMethodTypeSon Method", "[param error] or [param is null]"));
//            } else {
//
//                List<Map<String, Object>> list = (List<Map<String, Object>>) param.get("list");
//
//                param.put("planid", JzbRandom.getRandomChar(20));
//                param.put("addtime",System.currentTimeMillis());
//                // 添加返回值大于0 则添加成功
//                int count =tbPlantaskService.addPlantaskBrother(list);
//                if (count > 0) {
//                    // 定义返回结果
//                    result = Response.getResponseSuccess(userInfo);
//                } else {
//                    result = Response.getResponseError();
//                    logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "addMethodTypeSon Method", "[addResult error]"));
//                }
//            }
//        } catch (Exception e) {
//            flag = false;
//            // 打印异常信息
//            e.printStackTrace();
//            result = Response.getResponseError();
//           logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "addMethodTypeSon Method", e.toString()));
//        }
//        if (userInfo != null) {
//           logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
//                    userInfo.get("msgTag").toString(), "User Login Message"));
//        } else {
//            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
//        }
//        return result;
//    }

    /**
     * @param param
     * @return
     * @deprecated 修改机会管理
     */
    @RequestMapping(value = "/updatePlantask", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response updatePlantask(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/org/plantask/updatePlantask";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                       userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            // 验证指定值为空则返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"cdid", "uid","dutyid","plancontent","starttime","endtime","acceptors","executors","assistants","tasktype","taskstatus","addid", "parentid"})) {
                result = Response.getResponseError();
                logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "updateMethodType Method", "[param error] or [param is null]"));
            } else {


                List<Map<String, Object>> list = (List<Map<String, Object>>) param.get("list");

                param.put("uptime",System.currentTimeMillis());
                // 添加返回值大于0 则添加成功
                int count =tbPlantaskService.updatePlantask(param);
                if (count > 0) {
                    // 定义返回结果
                    result = Response.getResponseSuccess(userInfo);
                } else {
                    result = Response.getResponseError();
                    logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "updateMethodType Method", "[addResult error]"));
                }
            }
        } catch (Exception e) {
            flag=false;
            // 打印异常信息
            e.printStackTrace();
            result = Response.getResponseError();
           logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "updateMethodType Method", e.toString()));
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
     * @param param
     * @return
     * @deprecated 删除机会管理
     */
    @RequestMapping(value = "/delPlantask", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response delPlantask(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/org/plantask/delPlantask";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            // 验证指定值为空则返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"planid","tasktype"})) {
                result = Response.getResponseError();
                logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "updateMethodType Method", "[param error] or [param is null]"));
            } else {
//                if(!(param.get("tasktype").toString().equals("0")||param.get("tasktype").toString().equals("6"))){
//                    result = Response.getResponseError();
//                }
                param.put("uptime",System.currentTimeMillis());
                // 添加返回值大于0 则添加成功
                param.put("status","1");
                int count =tbPlantaskService.delPlantask(param);

                if (count > 0) {
                    // 定义返回结果
                    result = Response.getResponseSuccess(userInfo);
                } else {
                    result = Response.getResponseError();
                    logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "updateMethodType Method", "[addResult error]"));
                }
            }
        } catch (Exception e) {
            flag=false;
            // 打印异常信息
            e.printStackTrace();
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "updateMethodType Method", e.toString()));
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
