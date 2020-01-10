package com.jzb.org.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jzb.base.data.JzbDataType;
import com.jzb.base.data.date.JzbDateStr;
import com.jzb.base.data.date.JzbDateUtil;
import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbPageConvert;
import com.jzb.org.api.auth.AuthApi;
import com.jzb.org.service.DeptService;
import com.jzb.org.service.TbPlantaskJobPositionService;
import com.jzb.org.service.TbPlantaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author lifei
 * 计划管理
 */
@RestController
@RequestMapping(value = "/org/plantask")
@Scope("prototype")
public class TbPlantaskController {

    @Autowired
    private TbPlantaskService tbPlantaskService;
    @Autowired
    private DeptService deptService;
    @Autowired
    private TbPlantaskJobPositionService tbPlantaskJobPositionService;
    @Autowired
    private AuthApi authApi;
    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(TbPlantaskController.class);
//    /**
//     * @param
//     * @return TbTempItemController
//     * @deprecated 周计划 年机会 部门计划 月计划 通用查询
//     */
//    @RequestMapping(value = "/getPlantaskItem", method = RequestMethod.POST)
//    @ResponseBody
//    public Response getPlantaskItem(@RequestBody Map<String, Object> param) {
//        Response response;
//        Map<String, Object> userInfo = null;
//        String api = "/org/plantask/getPlantaskItem";
//        boolean flag = true;
//        try {
//            // 判断参数为空返回404
//            if (param.get("userinfo") != null) {
//                userInfo = (Map<String, Object>) param.get("userinfo");
//                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
//                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
//            } else {
//                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
//            }
//            // 判断参数为空返回404
//
//            // type=y 代表年计划
//            // type=m 代表月份
//            // type=d 代表天
//            // type=w 代表周
//
//            // dept=1 代表部门
//            // dept=0 代表个人
//            if (JzbCheckParam.haveEmpty(param, new String[]{"pageno","pagesize","type","dept"})) {
//                response = Response.getResponseError();
//            } else {
//
//                //参照tbTempitemController
//                JzbPageConvert.setPageRows(param);
//                param.put("node",param.get("dept").toString());
//                switch(param.get("type").toString()){
//                    case "y" :
//                            param.put("tabname","tb_plantask_year");
//                        break;
//                    case "m" :
//                        param.put("tabname","tb_plantask_month");
//                        break;
//                    case "d" :
//                        param.put("tabname","tb_plantask_day");
//                        break;
//                    case "w" :
//                        param.put("tabname","tb_plantask_week");
//                        break;
//                    default:
//                        response = Response.getResponseError();
//                }
//
//                List<Map<String, Object>> records = tbPlantaskService.getPantaskList1(param);
//                if(records.size()==0){
//                    response = Response.getResponseSuccess(userInfo);
//
//                    response.setResponseEntity(new String[]{});
//                    return response;
//                }
//                int count = tbPlantaskService.getPantaskCount(param);
//
//                //处理 其他表数据 拼接 部门id 用户id数据拼接
//                StringBuffer sb=new StringBuffer();
//                StringBuffer zhisb=new StringBuffer() ;
//                List<Map<String, Object>> bumen= tbPlantaskService.getCompanydeptMap();
//                for(int i=0,a=records.size();i<a;i++){
//                    if (zhisb.toString().indexOf(records.get(i).get("cdid").toString())==-1){
//                        zhisb.append("'"+records.get(i).get("cdid")+"',");
//                    }
//
//
//                    if (sb.toString().indexOf(records.get(i).get("uid").toString())==-1){
//                        sb.append("'"+records.get(i).get("uid")+"',");
//                    }
//                    if(records.get(i).get("acceptors")!=null){
//                        if(records.get(i).get("acceptors").toString().indexOf(",")>0){
//                            for(int c=0,d=records.get(i).get("acceptors").toString().split(",").length;c<d;c++){
//                                sb.append("'"+records.get(i).get("acceptors").toString().split(",")[c]+"',");
//                            }
//                        }else{
//                            sb.append("'"+records.get(i).get("acceptors")+"',");
//                        }
//                    }
//                    if(records.get(i).get("executors")!=null){
//                        if(records.get(i).get("executors").toString().indexOf(",")>0){
//                            for(int c=0,d=records.get(i).get("executors").toString().split(",").length;c<d;c++){
//                                sb.append("'"+records.get(i).get("executors").toString().split(",")[c]+"',");
//                            }
//                        }else{
//                            sb.append("'"+records.get(i).get("executors")+"',");
//                        }
//                    }
//                    if( sb.toString().indexOf(records.get(i).get("addid").toString())==-1){
//                        sb.append("'"+records.get(i).get("addid")+"',");
//                    }
//                    if(records.get(i).get("assistants")!=null){
//                        if( records.get(i).get("assistants").toString().indexOf(",")>0){
//                            for(int c=0,d=records.get(i).get("assistants").toString().split(",").length;c<d;c++){
//                                sb.append("'"+records.get(i).get("assistants").toString().split(",")[c]+"',");
//                            }
//                        }else{
//                            sb.append("'"+records.get(i).get("assistants")+"',");
//                        }
//                    }
//                }
//
//                param.put("ids",sb.toString().substring(0,sb.toString().length()-1));
//                param.put("cddids",zhisb.toString().substring(0,zhisb.toString().length()-1));
//                List<Map<String, Object>> zhizelist=tbPlantaskJobPositionService.selectRoleByDeptids(param);
//
//                Response usnames = authApi.getUserNameList(param);
//
//                List<Map<String, Object>> usernameList=usnames.getPageInfo().getList();
//                Map<String,Object> usnamesMap = new HashMap<>();
//                Map<String,Object> bumensMap = new HashMap<>();
//                Map<String,Object> zhizeMap = new HashMap<>();
//                for (Map<String,Object> map:zhizelist) {
//                    zhizeMap.put(map.get("crid").toString(),map.get("content"));
//                }
//
//                for (Map<String,Object> map:usernameList) {
//                    usnamesMap.put(map.get("uid").toString(),map.get("cname"));
//                }
//                for (Map<String,Object> map:bumen) {
//                    bumensMap.put(map.get("cdid").toString(),map.get("cname"));
//                }
//
//                for (int i=0,a=records.size();i<a;i++){
//                    records.get(i).put("yname",usnamesMap.get(records.get(i).get("uid")));//姓名
//                    records.get(i).put("bname",bumensMap.get(records.get(i).get("cdid")));//部门
//                    records.get(i).put("gname",zhizeMap.get(records.get(i).get("dutyid")));//岗位职责
//                    records.get(i).put("addname",usnamesMap.get(records.get(i).get("addid")));//姓名
//                    //验收人
//                    if(records.get(i).get("acceptors")!=null){
//                        if(records.get(i).get("acceptors").toString().indexOf(",")>0){
//                            StringBuffer acceptorsstr=new StringBuffer();
//                            for(int c=0,d=records.get(i).get("acceptors").toString().split(",").length;c<d;c++){
//                                acceptorsstr.append(usnamesMap.get(records.get(i).get("acceptors").toString().split(",")[c].toString())+",");
//                            }
//                            records.get(i).put("acceptorsname",acceptorsstr.toString().substring(0,acceptorsstr.toString().length()-1));//部门
//                        }else{
//                            records.get(i).put("acceptorsname",usnamesMap.get(records.get(i).get("acceptors").toString()));//部门
//                        }
//                    }
//
//                    //执行人
//                    if(records.get(i).get("executors")!=null){
//                        if(records.get(i).get("executors").toString().indexOf(",")>0){
//                            StringBuffer acceptorsstr=new StringBuffer();
//                            for(int c=0,d=records.get(i).get("executors").toString().split(",").length;c<d;c++){
//                                acceptorsstr.append(usnamesMap.get(records.get(i).get("executors").toString().split(",")[c].toString())+",");
//                            }
//                            records.get(i).put("executorsname",acceptorsstr.toString().substring(0,acceptorsstr.toString().length()-1));//部门
//                        }else{
//                            records.get(i).put("executorsname",usnamesMap.get(records.get(i).get("executors").toString()));//部门
//                        }
//                    }
//
//                    //协助人
//                    if(records.get(i).get("assistants")!=null){
//                        if(records.get(i).get("assistants").toString().indexOf(",")>0){
//                            StringBuffer acceptorsstr=new StringBuffer();
//                            for(int c=0,d=records.get(i).get("assistants").toString().trim().split(",").length;c<d;c++){
//                                acceptorsstr.append(usnamesMap.get(records.get(i).get("assistants").toString().split(",")[c].toString())+",");
//                            }
//                            records.get(i).put("assistantsname",acceptorsstr.toString().substring(0,acceptorsstr.toString().length()-1));//部门
//                        }else{
//                            records.get(i).put("assistantsname",usnamesMap.get(records.get(i).get("assistants").toString()));//部门
//                        }
//                    }
//
//                }
//
//                // Result JSON
//                JSONArray result = new JSONArray();
//
//                // record temp json
//                JSONObject recordJson = new JSONObject();
//
//                // Unknown json
//                JSONObject unknownRecord = new JSONObject();
//
//                // root id
//                String firstParent = "0000000";
//
//                for (int i = 0, l = records.size(); i < l; i++) {
//                    Map<String, Object> record = records.get(i);
//
//                    // if parentid is null.
//                    String parentId;
//                    if (record.get("parentid") == null) {
//
//                        parentId = "0000000";
//                    } else {
//                        parentId = record.get("parentid").toString();
//                    }
//                    // set default JSON and childern node
//                    JSONObject node = new JSONObject();
//                    node.put("planid", record.get("planid"));
//                    node.put("plancontent", record.get("plancontent"));
//                    node.put("parentid", parentId);
//                    //node.put();
//                    node.put("cdid",record.get("cdid"));
//                    node.put( "uid" ,record.get("uid"));
//                    node.put( "acceptors" ,record.get("acceptors"));
//                    node.put( "executors" ,record.get("executors"));
//                    node.put( "assistants" ,record.get("assistants"));
//                    node.put( "outcome" ,record.get("outcome"));
//                    node.put( "tasktype" ,record.get("tasktype"));
//                    node.put( "taskstatus" ,record.get("taskstatus"));
//                    node.put( "notes" ,record.get("notes"));
//                    node.put( "path" ,record.get("path"));
//                    node.put( "filenames" ,record.get("filenames"));
//                    node.put( "addid" ,record.get("addid"));
//                    node.put( "review" ,record.get("review"));
//                    node.put( "parentid" ,record.get("parentid"));
//                    node.put( "sort" ,record.get("sort"));
//                    node.put( "node" ,record.get("node"));
//                    node.put( "addtime" ,record.get("addtime"));
//                    node.put( "uptime" ,record.get("uptime"));
//                    node.put( "status" ,record.get("status"));
//                    node.put( "ptype" ,record.get("ptype"));
//                    node.put( "yname" ,record.get("yname"));
//                    node.put( "bname" ,record.get("bname"));
//                    node.put( "dutyid" ,record.get("dutyid"));
//                    node.put( "acceptorsname" ,record.get("acceptorsname"));
//                    node.put( "executorsname" ,record.get("executorsname"));
//                    node.put( "assistantsname" ,record.get("assistantsname"));
//                    node.put("gname",record.get("gname"));
//                    node.put("addname",record.get("addname"));
//                    node.put("children", new JSONArray());
//
//
//                    // if root node
//                    if (parentId.equals(firstParent)) {
//                        result.add(node);
//                        recordJson.put(record.get("planid").toString(), node);
//
//                        // if parent exist
//                    } else if (recordJson.containsKey(parentId)) {
//                        // add children
//                        recordJson.getJSONObject(parentId).getJSONArray("children").add(node);
//                        recordJson.put(record.get("planid").toString(), node);
//                        // Unknown relation node
//                    } else {
//                        String nodeId = record.get("planid").toString();
//                        if (unknownRecord.containsKey(parentId)) {
//                            // add children
//                            unknownRecord.getJSONObject(parentId).getJSONArray("children").add(node);
//                            recordJson.put(nodeId, node);
//                        } else {
//                            // find subnode
//                            for (Map.Entry<String, Object> entry : unknownRecord.entrySet()) {
//                                JSONObject tempNode = (JSONObject) entry.getValue();
//                                if (tempNode.getString("parentid").equals(nodeId)) {
//                                    node.getJSONArray("children").add(tempNode);
//                                    recordJson.put(tempNode.get("planid").toString(), tempNode);
//                                    unknownRecord.remove(tempNode.get("planid").toString());
//                                    break;
//                                }
//                            }
//                            unknownRecord.put(nodeId, node);
//                        }
//                    }
//                }
//                // unknownRecord add to result
//                // find subnode
//                for (Map.Entry<String, Object> entry : unknownRecord.entrySet()) {
//                    JSONObject tempNode = (JSONObject) entry.getValue();
//                    String tempNodeId = tempNode.getString("parentid");
//                    if (recordJson.containsKey(tempNodeId)) {
//                        // add children
//                        recordJson.getJSONObject(tempNodeId).getJSONArray("children").add(tempNode);
//                        System.out.println("-0-0-0-0-00-0---------------"+ tempNodeId + "\t\t" + tempNode.toString());
//                    } else {
//                        // Error node
//                        //recordJson.getJSONObject(tempNodeId).getJSONArray("children").add(tempNode);
//                        System.out.println("========================ERROR>> " + tempNodeId + "\t\t" + tempNode.toString());
//                    }
//                }
//
//            // 定义返回结果
//            response = Response.getResponseSuccess(userInfo);
//            response.setResponseEntity(result==null?"[]":result);
//
//            }
//        } catch (Exception e) {
//            flag = false;
//            // 打印异常信息
//            e.printStackTrace();
//            response = Response.getResponseError();
//            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getMethodType Method", e.toString()));
//        }
//
//
//        return response;
//    }
//


    /**
     * @param
     * @return TbTempItemController
     * @deprecated 周计划 年机会 部门计划 月计划 通用查询
     */
    @RequestMapping(value = "/getPlantaskItem1", method = RequestMethod.POST)
    @ResponseBody
    public Response getPlantaskItem1(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/org/plantask/getPlantaskItem1";
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
            if (JzbCheckParam.haveEmpty(param, new String[]{"pageno","pagesize","type","dept"})) {
                response = Response.getResponseError();
            } else {

                param.put("bumen",param.get("bumen").toString().replaceAll("\\[","").replaceAll("\\]",""));
                param.put("zhuangtai",param.get("zhuangtai").toString().replaceAll("\\[","").replaceAll("\\]",""));

                if(param.get("time")!=null) {
                    if (!param.get("time").toString().equals("")) {
                        DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(format1.parse(param.get("time").toString()));
                        //代表周
                        if (param.get("aaaa").toString().equals("1")) {
                            //获取周
                            int lastDay = cal.getActualMaximum(Calendar.DAY_OF_WEEK);
                            cal.add(Calendar.DATE, 7);
                            param.put("ktime", format1.parse(param.get("time").toString()).getTime());
                            param.put("etime", cal.getTimeInMillis());

                            System.out.println("一周" + param.get("ktime"));
                            System.out.println("一周" + param.get("etime"));
                        } else if (param.get("aaaa").toString().equals("2")) {
                            cal.setTime(format1.parse(param.get("time").toString()));
                            //获取某月最大天数
                            int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
                            //设置日历中月份的最大天数
                            cal.set(Calendar.DAY_OF_MONTH, lastDay);

                            param.put("ktime", format1.parse(param.get("time").toString()).getTime());
                            param.put("etime", cal.getTimeInMillis());
                            System.out.println("一月" + param.get("ktime"));
                            System.out.println("一月" + param.get("etime"));
                        } else if (param.get("aaaa").toString().equals("3")) {
                            cal.setTime(format1.parse(param.get("time").toString()));
                            //获取年最大天数
                            int lastDay = cal.getActualMaximum(Calendar.DAY_OF_YEAR);
                            //设置日历中月份的最大天数
                            cal.set(Calendar.DAY_OF_YEAR, lastDay);

                            param.put("ktime", format1.parse(param.get("time").toString()).getTime());
                            param.put("etime", cal.getTimeInMillis());
                            System.out.println("一年" + param.get("ktime"));
                            System.out.println("一年" + param.get("etime"));
                        } else {
                            cal.setTime(format1.parse(param.get("time").toString()));
                            cal.set(Calendar.HOUR_OF_DAY, 23);
                            cal.set(Calendar.MINUTE, 59);
                            cal.set(Calendar.SECOND, 59);
                            cal.set(Calendar.MILLISECOND, 999);

                            param.put("ktime", format1.parse(param.get("time").toString()).getTime());
                            param.put("etime", cal.getTimeInMillis());
                            System.out.println("一天" + param.get("ktime"));
                            System.out.println("一天" + param.get("etime"));
                        }

                    }
                }
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

                List<Map<String, Object>> records = tbPlantaskService.getPantaskList1(param);
                if(records.size()==0){
                    response = Response.getResponseSuccess(userInfo);
                    response.setResponseEntity(new String[]{});
                    return response;
                }

                int count = tbPlantaskService.getPantaskCount(param);
                // Result JSON
                JSONArray result = new JSONArray();
                // record temp json
                JSONObject recordJson = new JSONObject();
                // Unknown json
                JSONObject unknownRecord = new JSONObject();
                // root id
                String firstParent = "0000000";

                for (int i = 0, l = records.size(); i < l; i++) {
                    Map<String, Object> record = records.get(i);
                    // if parentid is null.
                    String parentId;
                    if (record.get("parentid") == null) {

                        parentId = "0000000";
                    } else {
                        parentId = record.get("parentid").toString();
                    }
                    // set default JSON and childern node
                    JSONObject node = new JSONObject();
                    node.put("planid", record.get("planid")==null?"":record.get("planid").toString());
                    node.put("plancontent", record.get("plancontent")==null?"":record.get("plancontent").toString());
                    node.put("parentid", parentId);
                    //node.put();
                    node.put("cdid",record.get("cdid")==null?"":record.get("cdid").toString().trim().split(","));
                    node.put( "uid" ,record.get("uid")==null?"":record.get("uid").toString());
                    node.put( "acceptorsname" ,record.get("acceptors")==null?"":record.get("acceptors").toString());
                    node.put( "executorsname" ,record.get("executors")==null?"":record.get("executors").toString());
                    node.put( "assistantsname" ,record.get("assistants")==null?"":(record.get("assistants").toString().trim().equals("")?new String []{}:record.get("assistants").toString().trim().split(",")));
                    node.put( "outcome" ,record.get("outcome")==null?"":record.get("outcome").toString());
                    node.put( "tasktype" ,record.get("tasktype")==null?"":record.get("tasktype").toString());
                    node.put( "taskstatus" ,record.get("taskstatus")==null?"":record.get("taskstatus").toString());
                    node.put( "notes" ,record.get("notes")==null?"":record.get("notes").toString());
                    node.put( "path" ,record.get("path")==null?"":record.get("path").toString());
                    node.put( "filenames" ,record.get("filenames")==null?"":record.get("filenames").toString());
                    node.put( "addid" ,record.get("addid")==null?"":record.get("addid").toString());
                    node.put( "review" ,record.get("review")==null?"":record.get("review").toString());
                    node.put( "parentid" ,record.get("parentid")==null?"":record.get("parentid").toString());
                    node.put( "sort" ,record.get("sort")==null?"":record.get("sort").toString());
                    node.put( "node" ,record.get("node")==null?"":record.get("node").toString());
                    node.put( "addtime" ,record.get("addtime")==null?"":record.get("addtime").toString());
                    node.put( "uptime" ,record.get("uptime")==null?"":record.get("uptime").toString());
                    node.put( "starttime" ,record.get("starttime")==null?"":record.get("starttime").toString());
                    node.put( "endtime" ,record.get("endtime")==null?"":record.get("endtime").toString());
                    if (record.get("starttime")!=null && record.get("endtime")!=null){
                        String [] valuedate={record.get("starttime").toString(),record.get("endtime").toString()};
                        node.put("valueDate",valuedate);
                    }else{
                        node.put("valueDate","");
                    }
                    node.put( "status" ,record.get("status")==null?"":record.get("status").toString());
                    //node.put( "ptype" ,record.get("ptype")==null?"":record.get("ptype").toString());
                    //node.put( "yname" ,record.get("yname")==null?"":record.get("yname").toString());
                   //node.put( "bname" ,record.get("bname")==null?"":record.get("bname").toString());
                    node.put( "dutyid" ,record.get("dutyid")==null?"":record.get("dutyid").toString().trim().split(","));
//                    node.put( "acceptorsname" ,record.get("acceptorsname")==null?"":record.get("acceptorsname").toString().split(","));
//                    node.put( "executorsname" ,record.get("executorsname")==null?"":record.get("executorsname").toString().split(","));
//                    node.put( "assistantsname" ,record.get("assistantsname")==null?"":record.get("assistantsname").toString().split(","));
                    node.put("dutyid",record.get("dutyid")==null?"":record.get("dutyid").toString().trim().split(","));
                   // node.put("addname",record.get("addname")==null?"":record.get("addname").toString());
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

                PageInfo inf=new PageInfo();
                inf.setPages(count);
                // 定义返回结果
                response = Response.getResponseSuccess(userInfo);
                System.out.println(result==null?"[]":result);
                response.setResponseEntity(result);

            }
        } catch (Exception e) {
            flag = false;
            // 打印异常信息
            e.printStackTrace();
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getMethodType Method", e.toString()));
        }


        return response;
    }


    /**
     * @param
     * @return TbTempItemController
     * @deprecated 周计划 年机会 部门计划 月计划 通用查询
     */
    @RequestMapping(value = "/addPlantaskItem", method = RequestMethod.POST)
    @ResponseBody
    public Response addPlantaskItem(@RequestBody Map<String, Object> param) {
        Response response=null;
        Map<String, Object> userInfo = null;
        String api = "/org/plantask/addPlantaskItem";
        childCategoryList = new ArrayList<Map<String, Object>>();
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
            //System.out.println(param);

            if (JzbCheckParam.haveEmpty(param, new String[]{"type","dept"})) {
                response = Response.getResponseError();
                logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "addMethodTypeBrother Method", "[param error] or [param is null]"));
            } else {


                switch (param.get("type").toString()) {
                    case "y":
                        param.put("tabname", "tb_plantask_year");
                        break;
                    case "m":
                        param.put("tabname", "tb_plantask_month");
                        break;
                    case "d":
                        param.put("tabname", "tb_plantask_day");
                        param.put("progressOfWork", 0);
                        break;
                    case "w":
                        param.put("tabname", "tb_plantask_week");
                        break;
                    default:
                        response = Response.getResponseError();
                }

                List<Map<String, Object>> a = (List<Map<String, Object>>) param.get("data");
                List<Map<String, Object>> list = null;
                //{data=[{planid=1,
                // parentid=0,
                // children=[],
                // select=1,
                // assistantsname=[ATMJQCPEGACB, AFIUMIIJDCNL, AZZZVGGQCKTC, AQSAYGJOPGMY],
                // cdid=[JZB00010000],
                // plancontent=21312,
                // outcome=1231231,
                // valueDate=[2020-01-15T16:00:00.000Z, 2020-02-10T16:00:00.000Z],
                // executorsname=AFIUMIIJDCNL,
                // acceptorsname=AQSAYGJOPGMY,
                // gname=[mkVVqYdrPManUwtqncUvXWQWlo, mkVVqYdrPManUwtqncUvXWQWlo],
                // taskstatus=2,
                // tasktype=3,
                // review=3,
                // addname=33,
                // filenames=33,
                // notes=333额}],
                System.out.println("\n\n\n\n\n");
                list = getChildCategory(a);
//                for (int i = 0; i < list.size(); i++) {
//                    System.out.println("planid-----" + (list.get(i).get("planid") == null ? "" : list.get(i).get("planid").toString()));
//                    System.out.println("parentid-----" + (list.get(i).get("parentid") == null ? "" : list.get(i).get("parentid").toString()));
//                    //System.out.println(list.get(i).get("children")==null?"":list.get(i).get("children").toString()));
//                    System.out.println("select-----" + (list.get(i).get("select") == null ? "" : list.get(i).get("select").toString()));
//                    System.out.println("assistantsname-----" + (list.get(i).get("assistantsname") == null ? "" : list.get(i).get("assistantsname").toString()));
//                    System.out.println("cdid-----" + (list.get(i).get("cdid") == null ? "" : list.get(i).get("cdid").toString()));
//                    System.out.println("plancontent-----" + (list.get(i).get("plancontent") == null ? "" : list.get(i).get("plancontent").toString()));
//                    System.out.println("outcome-----" + (list.get(i).get("outcome") == null ? "" : list.get(i).get("outcome").toString()));
//                    System.out.println("valueDate-----" + (list.get(i).get("valueDate") == null ? "" : list.get(i).get("valueDate").toString()));
//                    System.out.println("executorsname-----" + (list.get(i).get("executorsname") == null ? "" : list.get(i).get("executorsname").toString()));
//                    System.out.println("acceptorsname-----" + (list.get(i).get("acceptorsname") == null ? "" : list.get(i).get("acceptorsname").toString()));
//                    System.out.println("gname-----" + (list.get(i).get("gname") == null ? "" : list.get(i).get("gname").toString()));
//                    System.out.println("taskstatus-----" + (list.get(i).get("taskstatus") == null ? "" : list.get(i).get("taskstatus").toString()));
//                    System.out.println("tasktype-----" + (list.get(i).get("tasktype") == null ? "" : list.get(i).get("tasktype").toString()));
//                    System.out.println("review-----" + (list.get(i).get("review") == null ? "" : list.get(i).get("review").toString()));
//                    System.out.println("addname-----" + (list.get(i).get("addname") == null ? "" : list.get(i).get("addname").toString()));
//                    System.out.println("filenames-----" + (list.get(i).get("filenames") == null ? "" : list.get(i).get("filenames").toString()));
//                    System.out.println("notes-----" + (list.get(i).get("notes") == null ? "" : list.get(i).get("notes").toString()));
//                    System.out.println("\n\n\n\n\n");
//                }
                StringBuffer sb = new StringBuffer();
                List<Map<String, Object>> idslist = new ArrayList<>();
                if (list.size() > 0) {
                    for (int i = 0, l = list.size(); i < l; i++) {
                        sb.append("'" + list.get(i).get("planid").toString() + "'");
                        if (i != l - 1) {
                            sb.append(",");
                        }
                    }

                    param.put("planids", sb.toString());
                    //查询数据库
                    idslist = tbPlantaskService.selPantaskids(param);
                }

                sb = new StringBuffer();
                if (idslist.size() > 0) {
                    for (int i = 0, y = idslist.size(); i < y; i++) {
                        sb.append("'" + idslist.get(i).get("planid").toString() + "'");
                        if (i != y - 1) {
                            sb.append(",");
                        }
                    }

                }
                String upstr = sb.toString();
                List<Map<String, Object>> uplist = new ArrayList<>();
                List<Map<String, Object>> inerlist = new ArrayList<>();

                for (int i = 0, y = list.size(); i < y; i++) {
                    //数据处理
                    list.get(i).put("assistants", list.get(i).get("assistantsname") == null ? "" : list.get(i).get("assistantsname").toString().replaceAll("\\[", "").replaceAll("\\]", "").trim());
                    list.get(i).put("acceptors", list.get(i).get("acceptorsname") == null ? "" : list.get(i).get("acceptorsname").toString().replaceAll("\\[", "").replaceAll("\\]", "").trim());
                    list.get(i).put("executors", list.get(i).get("executorsname") == null ? "" : list.get(i).get("executorsname").toString().replaceAll("\\[", "").replaceAll("\\]", "").trim());



                    list.get(i).put("cdid", (list.get(i).get("cdid") == null ? "" : list.get(i).get("cdid").toString().replaceAll("\\[", "").replaceAll("\\]", "")));

                    //时间处理
                    if (list.get(i).get("valueDate") != null) {
                            if(!list.get(i).get("valueDate").equals("")) {
                                list.get(i).put("starttime", JzbDataType.getDateTime(list.get(i).get("valueDate").toString().replaceAll("\\[", "").replaceAll("\\]", "").trim().split(",")[0]).getTime());
                                list.get(i).put("endtime", JzbDataType.getDateTime(list.get(i).get("valueDate").toString().replaceAll("\\[", "").replaceAll("\\]", "").trim().split(",")[1]).getTime());
                            }else{
                                list.get(i).put("starttime", "");
                                list.get(i).put("endtime", "");
                            }
                    } else {
                        list.get(i).put("starttime", "");
                        list.get(i).put("endtime", "");
                    }


                    if (list.get(i).get("dutyid") != null) {
                        list.get(i).put("dutyid", (list.get(i).get("dutyid").toString().replaceAll("\\[", "").replaceAll("\\]", "")).trim());

                    }
                    //转换Int类型
                    list.get(i).put("taskstatus",list.get(i).get("taskstatus")==null?"":(list.get(i).get("taskstatus").toString().equals("")?"":Integer.parseInt(list.get(i).get("taskstatus").toString())));
                    list.get(i).put("tasktype",list.get(i).get("tasktype")==null?"":list.get(i).get("tasktype").toString());
                    list.get(i).put("sort",list.get(i).get("sort")==null?"":(list.get(i).get("sort").toString().equals("")?"":Integer.parseInt(list.get(i).get("sort").toString())));


                    list.get(i).put("node", param.get("dept").toString());
                    list.get(i).put("addid", userInfo.get("nickname").toString());
                    list.get(i).put("uid", userInfo.get("uid").toString());
                    System.out.println("\n\n\n\n\n\n");
                    System.out.println(userInfo.get("uid").toString());
                    System.out.println("\n\n\n\n\n\n");

                    if (upstr.indexOf(list.get(i).get("planid").toString()) > -1) {
                        list.get(i).put("addtime", System.currentTimeMillis());
                        list.get(i).put("uptime", System.currentTimeMillis());
                        uplist.add(list.get(i));
                    } else {
                        list.get(i).put("addtime", System.currentTimeMillis());
                        inerlist.add(list.get(i));
                    }
                }


                int upcount = 0, addcount = 0;
                param.put("uplist", uplist);
                param.put("inerlist", inerlist);
                if (inerlist.size() > 0) {
                    addcount = tbPlantaskService.addPlantaskBrother(param);
                }
                if (uplist.size() > 0) {
                    upcount = tbPlantaskService.updatePlantask(param);
                }
                if (addcount > 0 || upcount > 0) {
                    // 判断参数为空返回404
                    response = Response.getResponseSuccess();
                } else {
                    response = Response.getResponseError();
                }
                response=Response.getResponseSuccess();
            }
        } catch (Exception e) {
            flag = false;
            // 打印异常信息
            e.printStackTrace();
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getMethodType Method", e.toString()));
        }


        return response;
    }

    static List<Map<String, Object>> childCategoryList = new ArrayList<Map<String, Object>>();

    /**
     * 递归 lifei
     * @param allCategoryList
     * @return
     */
    public static List<Map<String, Object>> getChildCategory(List<Map<String, Object>> allCategoryList){
        for (Map<String, Object> category : allCategoryList) {
            // 判断是否存在子节点
           List<Map<String,Object>> list= (List<Map<String, Object>>) category.get("children");
            if (list.size()>0) {
                // 递归遍历下一级
                getChildCategory(list);
                childCategoryList.add(category);
            }else{
                childCategoryList.add(category);
            }

        }
        System.out.println("childCategoryList=" + childCategoryList.size());
        return childCategoryList;
    }



//    /**
//     * @param param
//     * @return
//     * @deprecated 添加计划管理同级
//     */
//    @RequestMapping(value = "/addPlantaskBrother", method = RequestMethod.POST)
//    @ResponseBody
//    @CrossOrigin
//    public Response addPlantaskBrother(@RequestBody Map<String, Object> param) {
//        Response result;
//        Map<String, Object> userInfo = null;
//        String api = "/org/plantask/addPlantaskBrother";
//        boolean flag = true;
//        try {
//            if (param.get("userinfo") != null) {
//                userInfo = (Map<String, Object>) param.get("userinfo");
//                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
//                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
//            } else {
//                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
//            }
//            // 验证指定值为空则返回404
//            if (JzbCheckParam.haveEmpty(param, new String[]{"list","type","dept"})) {
//                result = Response.getResponseError();
//                logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "addMethodTypeBrother Method", "[param error] or [param is null]"));
//            } else {
//                switch(param.get("type").toString()){
//                    case "y" :
//                        param.put("tabname","tb_plantask_year");
//                        break;
//                    case "m" :
//                        param.put("tabname","tb_plantask_month");
//                        break;
//                    case "d" :
//                        param.put("tabname","tb_plantask_day");
//                        param.put("progressOfWork",0);
//                        break;
//                    case "w" :
//                        param.put("tabname","tb_plantask_week");
//                        break;
//                    default:
//                        result = Response.getResponseError();
//                }
//
//                List<Map<String, Object>> list = (List<Map<String, Object>>) param.get("list");
//                for (Map<String, Object> map:list) {
//                    map.put("planid", JzbRandom.getRandomChar(20));
//                    map.put("addtime",System.currentTimeMillis());
//                    map.put("node",param.get("dept").toString());
//                    map.put("assistants",map.get("assistants").toString().replaceAll("\\[","").replaceAll("\\]",""));
//                    map.put("uid",userInfo.get("uid").toString());
//                    map.put("addid",userInfo.get("uid").toString());
//                    map.put("starttime",JzbDataType.getDateTime(map.get("valueDate").toString().replaceAll("\\[","").replaceAll("\\]","").split(",")[0]).getTime());
//                    map.put("endtime",JzbDataType.getDateTime(map.get("valueDate").toString().replaceAll("\\[","").replaceAll("\\]","").split(",")[0]).getTime());
//                }
//
//                param.put("list",list);
//                // 添加返回值大于0 则添加成功
//                int count = tbPlantaskService.addPlantaskBrother(param);
//                if (count > 0) {
//                    // 定义返回结果
//                    result = Response.getResponseSuccess(userInfo);
//                } else {
//                    result = Response.getResponseError();
//                  //  logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "addMethodTypeBrother Method", "[addResult error]"));
//                }
//            }
//        } catch (Exception e) {
//            flag = false;
//            // 打印异常信息
//            e.printStackTrace();
//            result = Response.getResponseError();
//           logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "addMethodTypeBrother Method", e.toString()));
//        }
//        if (userInfo != null) {
//           logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
//                    userInfo.get("msgTag").toString(), "User Login Message"));
//        } else {
//            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
//        }
//        return result;
//    }
//
//
//
//    /**
//     * @param param
//     * @return
//     * @deprecated 修改机会管理
//     */
//    @RequestMapping(value = "/updatePlantask", method = RequestMethod.POST)
//    @ResponseBody
//    @CrossOrigin
//    public Response updatePlantask(@RequestBody Map<String, Object> param) {
//        Response result;
//        Map<String, Object> userInfo = null;
//        String api = "/org/plantask/updatePlantask";
//        boolean flag = true;
//        try {
//            if (param.get("userinfo") != null) {
//                userInfo = (Map<String, Object>) param.get("userinfo");
//                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
//                       userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
//            } else {
//                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
//            }
//            // 验证指定值为空则返回404
//            if (JzbCheckParam.haveEmpty(param, new String[]{"list","type","dept"})) {
//                result = Response.getResponseError();
//                logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "updateMethodType Method", "[param error] or [param is null]"));
//            } else {
//
//                switch(param.get("type").toString()){
//                    case "y" :
//                        param.put("tabname","tb_plantask_year");
//                        break;
//                    case "m" :
//                        param.put("tabname","tb_plantask_month");
//                        break;
//                    case "d" :
//                        param.put("tabname","tb_plantask_day");
//                        break;
//                    case "w" :
//                        param.put("tabname","tb_plantask_week");
//                        break;
//                    default:
//                        result = Response.getResponseError();
//                }
//
//                List<Map<String, Object>> list = (List<Map<String, Object>>) param.get("list");
//                for (Map<String, Object> map:list) {
//                    map.put("uptime",System.currentTimeMillis());
//                    map.put("node",param.get("dept").toString());
//                    map.put("assistants",map.get("assistants")==null?null:map.get("assistants").toString().replaceAll("\\[","").replaceAll("\\]",""));
//                    map.put("uid",userInfo.get("uid").toString());
//                    map.put("addid",userInfo.get("uid").toString());
//                    map.put("starttime",map.get("valueDate")==null?null:JzbDataType.getDateTime(map.get("valueDate").toString().replaceAll("\\[","").replaceAll("\\]","").split(",")[0]).getTime());
//                    map.put("endtime",map.get("valueDate")==null?null:JzbDataType.getDateTime(map.get("valueDate").toString().replaceAll("\\[","").replaceAll("\\]","").split(",")[0]).getTime());
//
//                }
//                param.put("list",list);
//
//                // 添加返回值大于0 则添加成功
//                int count =tbPlantaskService.updatePlantask(param);
//                if (count > 0) {
//                    // 定义返回结果
//                    result = Response.getResponseSuccess(userInfo);
//                } else {
//                    result = Response.getResponseError();
//                    logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "updateMethodType Method", "[addResult error]"));
//                }
//            }
//        } catch (Exception e) {
//            flag=false;
//            // 打印异常信息
//            e.printStackTrace();
//            result = Response.getResponseError();
//           logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "updateMethodType Method", e.toString()));
//        }
//        if (userInfo != null) {
//            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
//                    userInfo.get("msgTag").toString(), "User Login Message"));
//        } else {
//           logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
//        }
//        return result;
//    }

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
            if (JzbCheckParam.haveEmpty(param, new String[]{"type","planid","dept"})) {
                result = Response.getResponseError();
                logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "updateMethodType Method", "[param error] or [param is null]"));
            } else {
//                if(!(param.get("tasktype").toString().equals("0")||param.get("tasktype").toString().equals("6"))){
//                    result = Response.getResponseError();
//                }

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
                        result = Response.getResponseError();
                }
                param.put("node",param.get("dept").toString());
                param.put("uptime",System.currentTimeMillis());
                param.put("status",1);
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




    /**
     * @param
     * @return TbTempItemController
     * @deprecated 周计划 年机会 部门计划 月计划 通用查询
     */
    @RequestMapping(value = "/getPlantaskDayItem", method = RequestMethod.POST)
    @ResponseBody
    public Response getPlantaskDayItem(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/org/plantask/getPlantaskDayItem";
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
            if (JzbCheckParam.haveEmpty(param, new String[]{"time"})) {
                response = Response.getResponseError();
            } else {
                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.HOUR_OF_DAY, 00);
                calendar1.set(Calendar.MINUTE, 00);
                calendar1.set(Calendar.SECOND, 00);
                calendar1.set(Calendar.MILLISECOND, 000);
                Long j=(calendar1.getTime()).getTime();//今天
                System.out.println(j);
                Long m=j+86400000;//明天
                Long h=m+86400000;//后天
                if(param.get("time").equals("0")){
                    param.put("starttime",j);
                    param.put("endtime",m);
                }else{
                    param.put("starttime",m);
                    param.put("endtime",h);
                }
                param.put("uid",userInfo.get("uid").toString());
                List<Map<String, Object>> records = tbPlantaskService.getPantaskDas(param);
                if(records.size()==0){
                    response = Response.getResponseSuccess(userInfo);
                    response.setResponseEntity(new String[]{});
                    return response;
                }
                List<Map<String,Object>>ret=new ArrayList<>();
                for (Map<String,Object>map: records) {
                    Map<String,Object> hm=new HashMap<>();
//                    map.put("planid", map.get("planid")==null?"":map.get("planid").toString());
//                    map.put("plancontent", map.get("plancontent")==null?"":map.get("plancontent").toString());
//                    map.put("parentid", map.get("parentid")==null?"":map.get("parentid").toString());
//                    map.put("cdid",map.get("cdid")==null?"":map.get("cdid").toString().trim().split(","));
//                    map.put( "uid" ,map.get("uid")==null?"":map.get("uid").toString());
//                    map.put( "acceptorsname" ,map.get("acceptors")==null?"":map.get("acceptors").toString());
//                    map.put( "executorsname" ,map.get("executors")==null?"":map.get("executors").toString());
//                    map.put( "assistantsname" ,map.get("assistants")==null?"":(map.get("assistants").toString().trim().equals("")?new String []{}:map.get("assistants").toString().trim().split(",")));
//                    map.put( "outcome" ,map.get("outcome")==null?"":map.get("outcome").toString());
//                    map.put( "tasktype" ,map.get("tasktype")==null?"":map.get("tasktype").toString());
//                    map.put( "taskstatus" ,map.get("taskstatus")==null?"":map.get("taskstatus").toString());
//                    map.put( "notes" ,map.get("notes")==null?"":map.get("notes").toString());
//                    map.put( "path" ,map.get("path")==null?"":map.get("path").toString());
//                    map.put( "filenames" ,map.get("filenames")==null?"":map.get("filenames").toString());
//                    map.put( "addid" ,map.get("addid")==null?"":map.get("addid").toString());
//                    map.put( "review" ,map.get("review")==null?"":map.get("review").toString());
//                    map.put( "parentid" ,map.get("parentid")==null?"":map.get("parentid").toString());
//                    map.put( "sort" ,map.get("sort")==null?"":map.get("sort").toString());
//                    map.put( "node" ,map.get("node")==null?"":map.get("node").toString());
//                    map.put( "addtime" ,map.get("addtime")==null?"":map.get("addtime").toString());
//                    map.put( "uptime" ,map.get("uptime")==null?"":map.get("uptime").toString());
//                    map.put( "starttime" ,map.get("starttime")==null?"":map.get("starttime").toString());
//                    map.put( "endtime" ,map.get("endtime")==null?"":map.get("endtime").toString());
//                    if (map.get("starttime")!=null && map.get("endtime")!=null){
//                        String [] valuedate={map.get("starttime").toString(),map.get("endtime").toString()};
//                        map.put("valueDate",valuedate);
//                    }else{
//                        map.put("valueDate","");
//                    }
//                    map.put( "status" ,map.get("status")==null?"":map.get("status").toString());
//                    map.put( "dutyid" ,map.get("dutyid")==null?"":map.get("dutyid").toString().trim().split(","));
//                    map.put("gname",map.get("dutyid")==null?"":map.get("dutyid").toString().trim().split(","));
                    hm.put("planid", map.get("planid")==null?"":map.get("planid").toString());
                    if(map.get("starttime")!=null&&map.get("endtime")!=null){
                        hm.put("date",  JzbDateUtil.toDateString(JzbDataType.getLong(map.get("starttime")), JzbDateStr.yyyy_MM_dd)+"至"+JzbDateUtil.toDateString(JzbDataType.getLong(map.get("endtime")), JzbDateStr.yyyy_MM_dd));

                    }else{
                        hm.put("date","");
                    }
                    hm.put("name",userInfo.get("cname")==null?"":userInfo.get("cname").toString());
                    hm.put("plancontent",map.get("plancontent")==null?"":map.get("plancontent").toString());
                    //map.put("value1",map.get("addname").toString()+"至"+map.get("endtime").toString());
                    hm.put("taskstatus",map.get("taskstatus")==null?"":map.get("taskstatus").toString());
                    hm.put("input",map.get("progressofwork")==null?"0":map.get("progressofwork").toString());
                    ret.add(hm);
                }
                // 定义返回结果
                response = Response.getResponseSuccess(userInfo);
                response.setResponseEntity(ret==null?new String []{}:ret);

            }
        } catch (Exception e) {
            flag = false;
            // 打印异常信息
            e.printStackTrace();
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getMethodType Method", e.toString()));
        }
        return response;
    }




    /**
     * @param
     * @return TbTempItemController
     * @deprecated 周计划 年机会 部门计划 月计划 通用查询
     */
    @RequestMapping(value = "/getPlantaskDayXiangxi", method = RequestMethod.POST)
    @ResponseBody
    public Response getPlantaskDayXiangxi(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/org/plantask/getPlantaskDayXiangxi";
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
            if (JzbCheckParam.haveEmpty(param, new String[]{"planid"})) {
                response = Response.getResponseError();
            } else {
                StringBuffer sb=new StringBuffer();
                Map<String, Object> map = tbPlantaskService.getPantaskDasXiangxi(param);

                if (sb.toString().indexOf(map.get("uid").toString())==-1){
                        sb.append("'"+map.get("uid").toString().trim()+"',");
                    }
                    if(map.get("acceptors")!=null){
                        if(map.get("acceptors").toString().indexOf(",")>0){
                            for(int c=0,d=map.get("acceptors").toString().split(",").length;c<d;c++){
                                sb.append("'"+map.get("acceptors").toString().split(",")[c].trim()+"',");
                            }
                        }else{
                            sb.append("'"+map.get("acceptors")+"',");
                        }
                    }

                    if(map.get("executors")!=null){
                        if(map.get("executors").toString().indexOf(",")>0){
                            for(int c=0,d=map.get("executors").toString().split(",").length;c<d;c++){
                                sb.append("'"+map.get("executors").toString().split(",")[c].trim()+"',");
                            }
                        }else{
                            sb.append("'"+map.get("executors")+"',");
                        }
                    }

                    if(map.get("assistants")!=null){
                        if( map.get("assistants").toString().indexOf(",")>0){
                            for(int c=0,d=map.get("assistants").toString().split(",").length;c<d;c++){
                                sb.append("'"+map.get("assistants").toString().split(",")[c].trim()+"',");
                            }
                        }else{
                            sb.append("'"+map.get("assistants")+"',");
                        }
                    }
                param.put("ids",(sb.toString().substring(0,sb.toString().length()-1)));
                Response usnames = authApi.getUserNameList(param);

                List<Map<String, Object>> usernameList=usnames.getPageInfo().getList();
                Map<String,Object> usnamesMap = new HashMap<>();


                for (Map<String,Object> map1:usernameList) {
                    usnamesMap.put(map1.get("uid").toString(),map1.get("cname"));
                }


                    //验收人
                    if(map.get("acceptors")!=null){
                        if(map.get("acceptors").toString().indexOf(",")>0){
                            StringBuffer acceptorsstr=new StringBuffer();
                            for(int c=0,d=map.get("acceptors").toString().split(",").length;c<d;c++){
                                acceptorsstr.append(usnamesMap.get(map.get("acceptors").toString().split(",")[c].toString().trim())+",");
                            }
                            map.put("acceptorsname",acceptorsstr.toString().substring(0,acceptorsstr.toString().length()-1));//部门
                        }else{
                            map.put("acceptorsname",usnamesMap.get(map.get("acceptors").toString()));//部门
                        }
                    }

                    //执行人
                    if(map.get("executors")!=null){
                        if(map.get("executors").toString().indexOf(",")>0){
                            StringBuffer acceptorsstr=new StringBuffer();
                            for(int c=0,d=map.get("executors").toString().split(",").length;c<d;c++){
                                acceptorsstr.append(usnamesMap.get(map.get("executors").toString().split(",")[c].toString().trim())+",");
                            }
                            map.put("executorsname",acceptorsstr.toString().substring(0,acceptorsstr.toString().length()-1));//部门
                        }else{
                            map.put("executorsname",usnamesMap.get(map.get("executors").toString()));//部门
                        }
                    }

                    //协助人
                    if(map.get("assistants")!=null){
                        if(map.get("assistants").toString().indexOf(",")>0){
                            StringBuffer acceptorsstr=new StringBuffer();
                            for(int c=0,d=map.get("assistants").toString().trim().split(",").length;c<d;c++){
                                acceptorsstr.append(usnamesMap.get(map.get("assistants").toString().split(",")[c].toString().trim())+",");
                            }
                            map.put("assistantsname",acceptorsstr.toString().substring(0,acceptorsstr.toString().length()-1));//部门
                        }else{
                            map.put("assistantsname",usnamesMap.get(map.get("assistants").toString()));//部门
                        }
                    }
                    if(map.get("dutyid")!=null){
                        if(map.get("dutyid").toString().indexOf(",")>0){
                            param.put("dutyids",map.get("dutyid").toString().split(",")[map.get("dutyid").toString().split(",").length-1].trim());
                        }else{
                            param.put("dutyids",map.get("dutyid").toString());
                        }

                        Map<String, Object> zhizelist=tbPlantaskJobPositionService.selecttContent(param);
                        map.put("gname",zhizelist.get("content"));
                    }
                    //2016, 9, 10, 8, 40
                    if(map.get("starttime")!=null&&map.get("endtime")!=null){

                        map.put("starttime",(JzbDateUtil.toDateString(JzbDataType.getLong(map.get("starttime")),JzbDateStr.yyyy_MM_dd)).replaceAll("-",","));
                        map.put("endtime",(JzbDateUtil.toDateString(JzbDataType.getLong(map.get("endtime")),JzbDateStr.yyyy_MM_dd)).replaceAll("-",","));

                    }else{
                        map.put("starttime","");
                        map.put("endtime","");
                    }

//                    map.put("planid", map.get("planid")==null?"":map.get("planid").toString());
//                    map.put("plancontent", map.get("plancontent")==null?"":map.get("plancontent").toString());
//                    map.put("parentid", map.get("parentid")==null?"":map.get("parentid").toString());
//                    map.put("cdid",map.get("cdid")==null?"":map.get("cdid").toString().trim().split(","));
//                    map.put( "uid" ,map.get("uid")==null?"":map.get("uid").toString());
//                    map.put( "acceptorsname" ,map.get("acceptors")==null?"":map.get("acceptors").toString());
//                    map.put( "executorsname" ,map.get("executors")==null?"":map.get("executors").toString());
//                    map.put( "assistantsname" ,map.get("assistants")==null?"":(map.get("assistants").toString().trim().equals("")?new String []{}:map.get("assistants").toString().trim().split(",")));
//                    map.put( "outcome" ,map.get("outcome")==null?"":map.get("outcome").toString());
//                    map.put( "tasktype" ,map.get("tasktype")==null?"":map.get("tasktype").toString());
//                    map.put( "taskstatus" ,map.get("taskstatus")==null?"":map.get("taskstatus").toString());
//                    map.put( "notes" ,map.get("notes")==null?"":map.get("notes").toString());
//                    map.put( "path" ,map.get("path")==null?"":map.get("path").toString());
//                    map.put( "filenames" ,map.get("filenames")==null?"":map.get("filenames").toString());
//                    map.put( "addid" ,map.get("addid")==null?"":map.get("addid").toString());
//                    map.put( "review" ,map.get("review")==null?"":map.get("review").toString());
//                    map.put( "parentid" ,map.get("parentid")==null?"":map.get("parentid").toString());
//                    map.put( "sort" ,map.get("sort")==null?"":map.get("sort").toString());
//                    map.put( "node" ,map.get("node")==null?"":map.get("node").toString());
//                    map.put( "addtime" ,map.get("addtime")==null?"":map.get("addtime").toString());
//                    map.put( "uptime" ,map.get("uptime")==null?"":map.get("uptime").toString());
//                    map.put( "starttime" ,map.get("starttime")==null?"":map.get("starttime").toString());
//                    map.put( "endtime" ,map.get("endtime")==null?"":map.get("endtime").toString());
//                    if (map.get("starttime")!=null && map.get("endtime")!=null){
//                        String [] valuedate={map.get("starttime").toString(),map.get("endtime").toString()};
//                        map.put("valueDate",valuedate);
//                    }else{
//                        map.put("valueDate","");
//                    }
//                    map.put( "status" ,map.get("status")==null?"":map.get("status").toString());
//                    map.put( "dutyid" ,map.get("dutyid")==null?"":map.get("dutyid").toString().trim().split(","));
//                    map.put("gname",map.get("dutyid")==null?"":map.get("dutyid").toString().trim().split(","));
//                    hm.put("name",userInfo.get("cname")==null?"":userInfo.get("cname").toString());
//                    hm.put("plancontent",map.get("plancontent")==null?"":map.get("plancontent").toString());
//                    //map.put("value1",map.get("addname").toString()+"至"+map.get("endtime").toString());
//                    hm.put("taskstatus",map.get("taskstatus")==null?"":map.get("taskstatus").toString());
//                    hm.put("input",map.get("progressofwork")==null?"0":map.get("progressofwork").toString());


                // 定义返回结果
                response = Response.getResponseSuccess(userInfo);
                response.setResponseEntity(map==null?new String []{}:map);

            }
        } catch (Exception e) {
            flag = false;
            // 打印异常信息
            e.printStackTrace();
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getMethodType Method", e.toString()));
        }
        return response;
    }



    @RequestMapping(value = "/upDayXiangxi", method = RequestMethod.POST)
    @ResponseBody
    public Response upDayXiangxi(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/org/plantask/upDayXiangxi";
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
            if (JzbCheckParam.haveEmpty(param, new String[]{"planid"})) {
                response = Response.getResponseError();
            } else {

                switch (param.get("type").toString()) {
                    case "y":
                        param.put("tabname", "tb_plantask_year");
                        break;
                    case "m":
                        param.put("tabname", "tb_plantask_month");
                        break;
                    case "d":
                        param.put("tabname", "tb_plantask_day");
                        param.put("progressOfWork", 0);
                        break;
                    case "w":
                        param.put("tabname", "tb_plantask_week");
                        break;
                    default:
                        response = Response.getResponseError();
                }
                param.put("uptime",System.currentTimeMillis());

                int count = tbPlantaskService.upDayXiangxi(param);
                // 定义返回结果
                response = Response.getResponseSuccess(userInfo);
                response.setResponseEntity(count);

            }
        } catch (Exception e) {
            flag = false;
            // 打印异常信息
            e.printStackTrace();
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getMethodType Method", e.toString()));
        }
        return response;
    }

//==================================================个人-===========================================================
    /**
     * @param
     * @return TbTempItemController
     * @deprecated 周计划 年机会 部门计划 月计划 通用查询
     */
    @RequestMapping(value = "/getPlantaskUsers", method = RequestMethod.POST)
    @ResponseBody
    public Response getPlantaskUsers(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/org/plantask/getPlantaskUsers";
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
                param.put("cid","JZB0001");
                param.put("pagesize",5000);
                param.put("pageno","1");
                param.put("start",1);

                List<Map<String, Object>> duList = deptService.queryDeptUser(param);

                HashSet h = new HashSet(duList);

                for (Map<String,Object> map : duList) {
                    System.out.println(map.get("cname")+ " : " +map.get("uid"));
                    Map<String,Object> addmap=new HashMap<>();
                    addmap.put("cname",map.get("cname"));
                    addmap.put("uid",map.get("uid"));
                    h.add(addmap);
                }
                // 定义返回结果
                response = Response.getResponseSuccess(userInfo);
                response.setResponseEntity(h);


        } catch (Exception e) {
            flag = false;
            // 打印异常信息
            e.printStackTrace();
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getMethodType Method", e.toString()));
        }
        return response;
    }



    /**
     * @param
     * @return TbTempItemController
     * @deprecated 周计划 年计划 月计划 通用查询
     */
    @RequestMapping(value = "/getPlantaskUserItem", method = RequestMethod.POST)
    @ResponseBody
    public Response getPlantaskUserItem(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/org/plantask/getPlantaskUserItem";
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
            if (JzbCheckParam.haveEmpty(param, new String[]{"pageno","pagesize","time"})) {
                response = Response.getResponseError();
            } else {

                param.put("bumen",param.get("bumen").toString().replaceAll("\\[","").replaceAll("\\]",""));
                param.put("zhuangtai",param.get("zhuangtai").toString().replaceAll("\\[","").replaceAll("\\]",""));

                if(param.get("time")!=null) {
                    if (!param.get("time").toString().equals("")) {
                        DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(format1.parse(param.get("time").toString()));
                        //代表周
                        if (param.get("aaaa").toString().equals("1")) {
                            //获取周
                            int lastDay = cal.getActualMaximum(Calendar.DAY_OF_WEEK);
                            cal.add(Calendar.DATE, 7);
                            param.put("ktime", format1.parse(param.get("time").toString()).getTime());
                            param.put("etime", cal.getTimeInMillis());

                            System.out.println("一周" + param.get("ktime"));
                            System.out.println("一周" + param.get("etime"));
                        } else if (param.get("aaaa").toString().equals("2")) {
                            cal.setTime(format1.parse(param.get("time").toString()));
                            //获取某月最大天数
                            int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
                            //设置日历中月份的最大天数
                            cal.set(Calendar.DAY_OF_MONTH, lastDay);

                            param.put("ktime", format1.parse(param.get("time").toString()).getTime());
                            param.put("etime", cal.getTimeInMillis());
                            System.out.println("一月" + param.get("ktime"));
                            System.out.println("一月" + param.get("etime"));
                        } else if (param.get("aaaa").toString().equals("3")) {
                            cal.setTime(format1.parse(param.get("time").toString()));
                            //获取年最大天数
                            int lastDay = cal.getActualMaximum(Calendar.DAY_OF_YEAR);
                            //设置日历中月份的最大天数
                            cal.set(Calendar.DAY_OF_YEAR, lastDay);

                            param.put("ktime", format1.parse(param.get("time").toString()).getTime());
                            param.put("etime", cal.getTimeInMillis());
                            System.out.println("一年" + param.get("ktime"));
                            System.out.println("一年" + param.get("etime"));
                        } else {
                            cal.setTime(format1.parse(param.get("time").toString()));
                            cal.set(Calendar.HOUR_OF_DAY, 23);
                            cal.set(Calendar.MINUTE, 59);
                            cal.set(Calendar.SECOND, 59);
                            cal.set(Calendar.MILLISECOND, 999);

                            param.put("ktime", format1.parse(param.get("time").toString()).getTime());
                            param.put("etime", cal.getTimeInMillis());
                            System.out.println("一天" + param.get("ktime"));
                            System.out.println("一天" + param.get("etime"));
                        }

                    }
                }
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

                List<Map<String, Object>> records = tbPlantaskService.getPantaskList1(param);
                if(records.size()==0){
                    response = Response.getResponseSuccess(userInfo);
                    response.setResponseEntity(new String[]{});
                    return response;
                }

                int count = tbPlantaskService.getPantaskCount(param);
                // Result JSON
                JSONArray result = new JSONArray();
                // record temp json
                JSONObject recordJson = new JSONObject();
                // Unknown json
                JSONObject unknownRecord = new JSONObject();
                // root id
                String firstParent = "0000000";

                for (int i = 0, l = records.size(); i < l; i++) {
                    Map<String, Object> record = records.get(i);
                    // if parentid is null.
                    String parentId;
                    if (record.get("parentid") == null) {

                        parentId = "0000000";
                    } else {
                        parentId = record.get("parentid").toString();
                    }
                    // set default JSON and childern node
                    JSONObject node = new JSONObject();
                    node.put("planid", record.get("planid")==null?"":record.get("planid").toString());
                    node.put("plancontent", record.get("plancontent")==null?"":record.get("plancontent").toString());
                    node.put("parentid", parentId);
                    //node.put();
                    node.put("cdid",record.get("cdid")==null?"":record.get("cdid").toString().trim().split(","));
                    node.put( "uid" ,record.get("uid")==null?"":record.get("uid").toString());
                    node.put( "acceptorsname" ,record.get("acceptors")==null?"":record.get("acceptors").toString());
                    node.put( "executorsname" ,record.get("executors")==null?"":record.get("executors").toString());
                    node.put( "assistantsname" ,record.get("assistants")==null?"":(record.get("assistants").toString().trim().equals("")?new String []{}:record.get("assistants").toString().trim().split(",")));
                    node.put( "outcome" ,record.get("outcome")==null?"":record.get("outcome").toString());
                    node.put( "tasktype" ,record.get("tasktype")==null?"":record.get("tasktype").toString());
                    node.put( "taskstatus" ,record.get("taskstatus")==null?"":record.get("taskstatus").toString());
                    node.put( "notes" ,record.get("notes")==null?"":record.get("notes").toString());
                    node.put( "path" ,record.get("path")==null?"":record.get("path").toString());
                    node.put( "filenames" ,record.get("filenames")==null?"":record.get("filenames").toString());
                    node.put( "addid" ,record.get("addid")==null?"":record.get("addid").toString());
                    node.put( "review" ,record.get("review")==null?"":record.get("review").toString());
                    node.put( "parentid" ,record.get("parentid")==null?"":record.get("parentid").toString());
                    node.put( "sort" ,record.get("sort")==null?"":record.get("sort").toString());
                    node.put( "node" ,record.get("node")==null?"":record.get("node").toString());
                    node.put( "addtime" ,record.get("addtime")==null?"":record.get("addtime").toString());
                    node.put( "uptime" ,record.get("uptime")==null?"":record.get("uptime").toString());
                    node.put( "starttime" ,record.get("starttime")==null?"":record.get("starttime").toString());
                    node.put( "endtime" ,record.get("endtime")==null?"":record.get("endtime").toString());
                    if (record.get("starttime")!=null && record.get("endtime")!=null){
                        String [] valuedate={record.get("starttime").toString(),record.get("endtime").toString()};
                        node.put("valueDate",valuedate);
                    }else{
                        node.put("valueDate","");
                    }
                    node.put( "status" ,record.get("status")==null?"":record.get("status").toString());
                    //node.put( "ptype" ,record.get("ptype")==null?"":record.get("ptype").toString());
                    //node.put( "yname" ,record.get("yname")==null?"":record.get("yname").toString());
                    //node.put( "bname" ,record.get("bname")==null?"":record.get("bname").toString());
                    node.put( "dutyid" ,record.get("dutyid")==null?"":record.get("dutyid").toString().trim().split(","));
//                    node.put( "acceptorsname" ,record.get("acceptorsname")==null?"":record.get("acceptorsname").toString().split(","));
//                    node.put( "executorsname" ,record.get("executorsname")==null?"":record.get("executorsname").toString().split(","));
//                    node.put( "assistantsname" ,record.get("assistantsname")==null?"":record.get("assistantsname").toString().split(","));
                    node.put("dutyid",record.get("dutyid")==null?"":record.get("dutyid").toString().trim().split(","));
                    // node.put("addname",record.get("addname")==null?"":record.get("addname").toString());
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

                PageInfo inf=new PageInfo();
                inf.setPages(count);
                // 定义返回结果
                response = Response.getResponseSuccess(userInfo);
                System.out.println(result==null?"[]":result);
                response.setResponseEntity(result);

            }
        } catch (Exception e) {
            flag = false;
            // 打印异常信息
            e.printStackTrace();
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getMethodType Method", e.toString()));
        }


        return response;
    }



}
