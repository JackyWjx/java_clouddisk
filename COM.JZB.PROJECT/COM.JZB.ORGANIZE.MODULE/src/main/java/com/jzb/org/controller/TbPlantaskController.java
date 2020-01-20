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
 * 计划管理 个人
 */
@RestController
@RequestMapping(value = "/org/plantask")
@Scope("prototype")
public class TbPlantaskController {

    @Autowired
    private  TbPlantaskService tbPlantaskService;
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


    /**
     * @param
     * @return TbTempItemController
     * @deprecated 周计划 年机会 部门计划 月计划 通用查询///暂未使用
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

                            //System.out.println("一周" + param.get("ktime"));
                            //System.out.println("一周" + param.get("etime"));
                        } else if (param.get("aaaa").toString().equals("2")) {
                            cal.setTime(format1.parse(param.get("time").toString()));
                            //获取某月最大天数
                            int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
                            //设置日历中月份的最大天数
                            cal.set(Calendar.DAY_OF_MONTH, lastDay);

                            param.put("ktime", format1.parse(param.get("time").toString()).getTime());
                            param.put("etime", cal.getTimeInMillis());
                            //System.out.println("一月" + param.get("ktime"));
                            //System.out.println("一月" + param.get("etime"));
                        } else if (param.get("aaaa").toString().equals("3")) {
                            cal.setTime(format1.parse(param.get("time").toString()));
                            //获取年最大天数
                            int lastDay = cal.getActualMaximum(Calendar.DAY_OF_YEAR);
                            //设置日历中月份的最大天数
                            cal.set(Calendar.DAY_OF_YEAR, lastDay);

                            param.put("ktime", format1.parse(param.get("time").toString()).getTime());
                            param.put("etime", cal.getTimeInMillis());
                            //System.out.println("一年" + param.get("ktime"));
                            //System.out.println("一年" + param.get("etime"));
                        } else {
                            cal.setTime(format1.parse(param.get("time").toString()));
                            cal.set(Calendar.HOUR_OF_DAY, 23);
                            cal.set(Calendar.MINUTE, 59);
                            cal.set(Calendar.SECOND, 59);
                            cal.set(Calendar.MILLISECOND, 999);

                            param.put("ktime", format1.parse(param.get("time").toString()).getTime());
                            param.put("etime", cal.getTimeInMillis());
                            //System.out.println("一天" + param.get("ktime"));
                            //System.out.println("一天" + param.get("etime"));
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
                        //System.out.println("-0-0-0-0-00-0---------------"+ tempNodeId + "\t\t" + tempNode.toString());
                    } else {
                        // Error node
                        //recordJson.getJSONObject(tempNodeId).getJSONArray("children").add(tempNode);
                        //System.out.println("========================ERROR>> " + tempNodeId + "\t\t" + tempNode.toString());
                    }
                }

                PageInfo inf=new PageInfo();
                inf.setPages(count);
                // 定义返回结果
                response = Response.getResponseSuccess(userInfo);
                //System.out.println(result==null?"[]":result);
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
     * @deprecated 周计划 年机会 部门计划 月计划 通用插入
     */
    @RequestMapping(value = "/addPlantaskItem", method = RequestMethod.POST)
    @ResponseBody
    public Response addPlantaskItem(@RequestBody Map<String, Object> param) {
        Response response=null;
        Map<String, Object> userInfo = null;
        String api = "/org/plantask/addPlantaskItem";
        childCategoryList = new ArrayList<Map<String, Object>>();
        treestr=new StringBuffer();
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
                        param.put("progressofwork", 0);
                        break;
                    case "w":
                        param.put("tabname", "tb_plantask_week");
                        break;
                    default:
                        response = Response.getResponseError();
                }
                List<Map<String, Object>> a = (List<Map<String, Object>>) param.get("data");
                List<Map<String, Object>> list = null;
               // System.out.println("\n\n\n\n\n");
                list = getChildCategory(a);
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
//                    System.out.println("\n\n\n\n\n\n");
//                    System.out.println(userInfo.get("uid").toString());
//                    System.out.println("\n\n\n\n\n\n");

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
    static StringBuffer treestr=new StringBuffer();
    /**
     * 递归 lifei
     * @param allCategoryList
     * @return
     */
    public static List<Map<String, Object>> getChildCategory(List<Map<String, Object>> allCategoryList){
        for (Map<String, Object> category : allCategoryList) {
            //System.out.println(category.get("children"));
           // System.out.println(category.toString());
            // 判断是否存在子节点
           List<Map<String,Object>> list= (List<Map<String, Object>>) category.get("children");

            treestr.append(category.get("parentid").toString()+",");
            //System.out.println(treestr.toString());
           if (list.size()>0) {
                // 递归遍历下一级
               //category.put("treepath",treestr.toString());
               childCategoryList.add(category);
               getChildCategory(list);
            }else{
               //System.out.println("hjeihe");
               //category.put("treepath",treestr.toString());
               childCategoryList.add(category);
               treestr=new StringBuffer();
            }

        }
        //System.out.println("childCategoryList=" + childCategoryList.size());
        return childCategoryList;
    }





    /**
     * @param param
     * @return
     * @deprecated 个人日计划 右边 删除
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
     * @deprecated 个人日计划 右边 详情
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

                Long m=j+86400000;//明天
                Long h=m+86400000;//后天
                if(param.get("time").equals("0")){
                    param.put("starttime",j);
                    param.put("endtime",m-1);
                }else{
                    param.put("starttime",m);
                    param.put("endtime",h-1);
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
                    hm.put( "tasktype" ,map.get("tasktype")==null?"":map.get("tasktype").toString());
                    hm.put("planid", map.get("planid")==null?"":map.get("planid").toString());
                    if(map.get("starttime")!=null&&map.get("endtime")!=null){
                        hm.put("date",  JzbDateUtil.toDateString(JzbDataType.getLong(map.get("starttime")), JzbDateStr.yyyy_MM_dd)+"至"+JzbDateUtil.toDateString(JzbDataType.getLong(map.get("endtime")), JzbDateStr.yyyy_MM_dd));

                    }else{
                        hm.put("date","");
                    }
                    hm.put("name",map.get("plancontent")==null?"":map.get("plancontent").toString());
                    hm.put("plancontent",map.get("plancontent")==null?"":map.get("plancontent").toString());
                    //map.put("value1",map.get("addname").toString()+"至"+map.get("endtime").toString());
                    hm.put("taskstatus",map.get("taskstatus")==null?"":map.get("taskstatus").toString());
                    hm.put("input",map.get("progressofwork")==null?"0":map.get("progressofwork").toString());
                    hm.put("starttime",map.get("starttime"));
                    hm.put("endtime",map.get("endtime"));
                    hm.put("assistants",(map.get("assistants")==null?"":map.get("assistants").toString().trim()).split(","));
                    hm.put( "acceptors" ,map.get("acceptors")==null?"":map.get("acceptors").toString().split(","));
                    hm.put( "executors" ,map.get("executors")==null?"":map.get("executors").toString().split(","));

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

                param.put("tabname", "tb_plantask");
                param.put("uptime",System.currentTimeMillis());

                int progressofwork =Integer.parseInt(param.get("progressofwork").toString());
                Long starttime =Long.parseLong(param.get("starttime").toString());
                Long endtime =Long.parseLong(param.get("endtime").toString());
                Long dtime=(new Date()).getTime();

                if(progressofwork>0&&progressofwork<100&&(!param.get("tasktype").toString().equals("已终止"))){
                    if(endtime<dtime){
                        param.put("tasktype","已逾期");
                    }
                    if(starttime<=dtime&&endtime>dtime){
                        param.put("tasktype","进行中");
                    }else{
                        param.put("tasktype","未开始");
                    }
                }else if(progressofwork==100&&(!param.get("tasktype").toString().equals("已终止"))){
                    if(endtime<dtime){
                        param.put("tasktype","逾期完成");
                    }
                    if(endtime>dtime){
                        param.put("tasktype","已完成");
                    }
                }
                param.put("progressofwork",progressofwork);
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
     * @deprecated 所有计支宝 去重复后用户 cid cname
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
                Long st=System.currentTimeMillis();

                List<Map<String, Object>> duList = deptService.queryDeptUser(param);
                Long en=System.currentTimeMillis();
                //System.out.println(en-st);
                HashSet h = new HashSet();
                for (Map<String,Object> map : duList) {
                    //System.out.println(map.get("cname")+ " : " +map.get("uid"));
                    Map<String,Object> addmap=new HashMap<>();
                    addmap.put("cname",map.get("cname"));
                    addmap.put("uid",map.get("uid"));
                    h.add(addmap);
                }
                //en=System.currentTimeMillis();
                //System.out.println(en-st);
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
            if (JzbCheckParam.haveEmpty(param, new String[]{"pageno","pagesize","type","dept"})) {
                response = Response.getResponseError();
            } else {

                param.put("bumen",param.get("bumen").toString().replaceAll("\\[","").replaceAll("\\]",""));
                param.put("zhuangtai",param.get("zhuangtai").toString().replaceAll("\\[","").replaceAll("\\]",""));
                param.put("touid",param.get("touid").toString().replaceAll("\\[","").replaceAll("\\]",""));

                param.put("auid",userInfo.get("uid").toString());

                if(param.get("time")!=null) {
                    if (!param.get("time").toString().equals("")) {
                        DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(format1.parse(param.get("time").toString()));
                        //代表周
                        if (param.get("type").toString().equals("w")) {
                            //获取周
                            int lastDay = cal.getActualMaximum(Calendar.DAY_OF_WEEK);
                            cal.add(Calendar.DATE, 7);

                            param.put("ktime", format1.parse(param.get("time").toString()).getTime());
                            param.put("etime", cal.getTimeInMillis()-1);
                            //System.out.println("一周" + param.get("ktime"));
                            //System.out.println("一周" + param.get("etime"));
                        } else if (param.get("type").toString().equals("m")) {
                            cal.setTime(format1.parse(param.get("time").toString()));
                            //获取某月最大天数
                            int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
                            //设置日历中月份的最大天数
                            cal.set(Calendar.DAY_OF_MONTH, lastDay);
                            cal.set(Calendar.HOUR_OF_DAY, 23);
                            cal.set(Calendar.MINUTE, 59);
                            cal.set(Calendar.SECOND, 59);
                            cal.set(Calendar.MILLISECOND, 999);
                            param.put("ktime", format1.parse(param.get("time").toString()).getTime());
                            param.put("etime", cal.getTimeInMillis());
                            //System.out.println("一月" + param.get("ktime"));
                            //System.out.println("一月" + param.get("etime"));
                        } else if (param.get("type").toString().equals("y")) {
                            cal.setTime(format1.parse(param.get("time").toString()));
                            //获取年最大天数
                            int lastDay = cal.getActualMaximum(Calendar.DAY_OF_YEAR);
                            //设置日历中月份的最大天数
                            cal.set(Calendar.DAY_OF_YEAR, lastDay);
                            cal.set(Calendar.HOUR_OF_DAY, 23);
                            cal.set(Calendar.MINUTE, 59);
                            cal.set(Calendar.SECOND, 59);
                            cal.set(Calendar.MILLISECOND, 999);

                            param.put("ktime", format1.parse(param.get("time").toString()).getTime());
                            param.put("etime", cal.getTimeInMillis());
                            //System.out.println("一年" + param.get("ktime"));
                            //System.out.println("一年" + param.get("etime"));
                        } else {
                            cal.setTime(format1.parse(param.get("time").toString()));
                            cal.set(Calendar.HOUR_OF_DAY, 23);
                            cal.set(Calendar.MINUTE, 59);
                            cal.set(Calendar.SECOND, 59);
                            cal.set(Calendar.MILLISECOND, 999);
                            param.put("ktime", format1.parse(param.get("time").toString()).getTime());
                            param.put("etime", cal.getTimeInMillis());
                            //System.out.println("一天" + param.get("ktime"));
                            //System.out.println("一天" + param.get("etime"));
                        }
                    }else{
                        if (param.get("type").toString().equals("m")) {
                            param.put("ktime", JzbDateUtil.getCurrentMonthFirstDay());
                            param.put("etime", JzbDateUtil.getCurrentMonthLastDay());
                        }else if(param.get("type").toString().equals("w")){
                            param.put("ktime", JzbDateUtil.getFirstDayOfWeek(new Date()));
                            param.put("etime", JzbDateUtil.getLastDayOfWeek(new Date()));
                        }else if(param.get("type").toString().equals("d")){
                            param.put("ktime", JzbDateUtil.getFirstDayOfDay(new Date()));
                            param.put("etime", JzbDateUtil.getLastDayOfDay(new Date()));
                        }else if (param.get("type").toString().equals("y")){
                            param.put("ktime",JzbDateUtil.getCurrYearFirst().getTime());
                            param.put("etime",JzbDateUtil.getCurrYearLast().getTime());
                        }
                    }
                }
                //参照tbTempitemController
                JzbPageConvert.setPageRows(param);
                param.put("node",param.get("dept").toString());
                param.put("tabname","tb_plantask");
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
                    node.put( "id" ,record.get("id")==null?"":record.get("id").toString());
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
                    node.put( "progressofwork" ,record.get("progressofwork")==null?"":record.get("progressofwork").toString());
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
                        //System.out.println("-0-0-0-0-00-0---------------"+ tempNodeId + "\t\t" + tempNode.toString());
                    } else {
                        // Error node
                        //recordJson.getJSONObject(tempNodeId).getJSONArray("children").add(tempNode);
                        //System.out.println("========================ERROR>> " + tempNodeId + "\t\t" + tempNode.toString());
                    }
                }

                PageInfo inf=new PageInfo();
                inf.setPages(count);
                // 定义返回结果
                response = Response.getResponseSuccess(userInfo);
                //System.out.println(result==null?"[]":result);
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
     * @deprecated 通用插入数据
     */
    @RequestMapping(value = "/addPlantaskUserItem", method = RequestMethod.POST)
    @ResponseBody
    public Response addPlantaskUserItem(@RequestBody Map<String, Object> param) {
        Response response=null;
        Map<String, Object> userInfo = null;
        String api = "/org/plantask/addPlantaskUserItem";
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
                param.put("tabname", "tb_plantask");
                param.put("ptype",param.get("type").toString());
                List<Map<String, Object>> a = (List<Map<String, Object>>) param.get("data");
                List<Map<String, Object>> list = new ArrayList<>();
                // System.out.println("\n\n\n\n\n");
                list = getChildCategory(a);
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
                if (null!=idslist){
                    sb = new StringBuffer();
                    if (idslist.size() > 0) {
                        for (int i = 0, y = idslist.size(); i < y; i++) {
                            sb.append("'" + idslist.get(i).get("planid").toString() + "'");
                            if (i != y - 1) {
                                sb.append(",");
                            }
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



                   StringBuffer bumenids=new StringBuffer();
                    if(list.get(i).get("cdid")==null){
                        List<Map<String,Object>> cdids=tbPlantaskService.selcdids(userInfo.get("uid").toString());
                        for (Map<String,Object>map:cdids){
                            bumenids.append(map.get("cdid")+",");
                        }
                        list.get(i).put("cdid", bumenids.toString().substring(0,bumenids.toString().length()-1));
                    }else{
                        list.get(i).put("cdid", list.get(i).get("cdid").toString().replaceAll("\\[","").replaceAll("\\]","").trim());
                    }


                    //时间处理
                    if (list.get(i).get("valueDate") != null) {
                        if(!list.get(i).get("valueDate").equals("")) {

                            List shijian= (List) list.get(i).get("valueDate");
                            if(shijian.get(0).toString().equals(shijian.get(1).toString())){
                                list.get(i).put("starttime",shijian.get(0)+"");
                                list.get(i).put("endtime",((Long)shijian.get(1))+86399999+"");
                            }else{
                                list.get(i).put("starttime",shijian.get(0)+"");
                                list.get(i).put("endtime",shijian.get(1)+"");
                            }

                            if(list.get(i).get("progressofwork")!=null){
                                if(list.get(i).get("progressofwork").toString().equals("")){
                                    list.get(i).put("progressofwork",0);
                                }
                                int progressofwork=Integer.parseInt(list.get(i).get("progressofwork").toString());
                                Long starttime =Long.parseLong(list.get(i).get("starttime").toString());
                                Long endtime =Long.parseLong(list.get(i).get("endtime").toString());
                                Long dtime=(new Date()).getTime();

                                if(progressofwork>=0&&progressofwork<100&&(!list.get(i).get("tasktype").toString().equals("已终止"))){
                                    if(endtime<dtime){
                                        list.get(i).put("tasktype","已逾期");
                                    }
                                    if(starttime<=dtime&&endtime>dtime&&progressofwork>0){
                                        list.get(i).put("tasktype","进行中");
                                    }else{
                                        list.get(i).put("tasktype","未开始");
                                    }
                                }else if(progressofwork==100&&(!list.get(i).get("tasktype").toString().equals("已终止"))&&(!list.get(i).get("tasktype").toString().equals("已完成"))){
                                    if(endtime<dtime){
                                        list.get(i).put("tasktype","逾期完成");
                                    }
                                    if(endtime>dtime){
                                        list.get(i).put("tasktype","已完成");
                                    }
                                }
                            }
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
                    //修改状态
//                    list.get(i).get("starttime")
//                    list.get(i).get("endtime")



                    //转换Int类型
                    list.get(i).put("taskstatus",list.get(i).get("taskstatus")==null?"":(list.get(i).get("taskstatus").toString().equals("")?"":Integer.parseInt(list.get(i).get("taskstatus").toString())));
                    list.get(i).put("tasktype",list.get(i).get("tasktype")==null?"":list.get(i).get("tasktype").toString().trim());
                    list.get(i).put("sort",list.get(i).get("sort")==null?"":(list.get(i).get("sort").toString().equals("")?"":Integer.parseInt(list.get(i).get("sort").toString())));


                    list.get(i).put("node", param.get("dept").toString());
                    list.get(i).put("addid", userInfo.get("nickname").toString());
                    list.get(i).put("uid", userInfo.get("uid").toString());

//                    System.out.println("\n\n\n\n\n\n");
//                    System.out.println(userInfo.get("uid").toString());
//                    System.out.println("\n\n\n\n\n\n");

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



    /**
     * @param param
     * @return
     * @deprecated 个人日计划 右边 删除
     */
    @RequestMapping(value = "/delPlantaskUser", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response delPlantaskUser(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/org/plantask/delPlantaskUser";
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



                param.put("node",param.get("dept").toString());
                //param.put("uptime",System.currentTimeMillis());



                List<Map<String, Object>>lt=  tbPlantaskService.selFu(param);
                int count=0;
                for (Map<String,Object> map:lt){
                    map.put("uptime",System.currentTimeMillis());
                count =tbPlantaskService.delPlantask(map);
                }

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



    @RequestMapping(value = "/upDayzhongzhi", method = RequestMethod.POST)
    @ResponseBody
    public Response upDayzhongzhi(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/org/plantask/upDayzhongzhi";
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

                param.put("tabname", "tb_plantask");
                param.put("uptime",System.currentTimeMillis());
                param.put("tasktype","已终止");
                int count = tbPlantaskService.upDayzhongzhi(param);
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







    /**
     * @param
     * @return
     * @deprecated 赛选子找父
     */
    @RequestMapping(value = "/getPlantaskUserXuan", method = RequestMethod.POST)
    @ResponseBody
        public Response getPlantaskUserXuan(@RequestBody Map<String, Object> param) {
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
            if (JzbCheckParam.haveEmpty(param, new String[]{"pageno","pagesize","type","dept"})) {
                response = Response.getResponseError();
            } else {

                param.put("bumen",param.get("bumen").toString().replaceAll("\\[","").replaceAll("\\]",""));
                param.put("zhuangtai",param.get("zhuangtai").toString().replaceAll("\\[","").replaceAll("\\]",""));
                param.put("touid",param.get("touid").toString().replaceAll("\\[","").replaceAll("\\]",""));
                param.put("auid",userInfo.get("uid").toString());
                if(param.get("time")!=null) {
                    if (!param.get("time").toString().equals("")) {
                        DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(format1.parse(param.get("time").toString()));
                        //代表周
                        if (param.get("type").toString().equals("w")) {
                            //获取周
                            int lastDay = cal.getActualMaximum(Calendar.DAY_OF_WEEK);
                            cal.add(Calendar.DATE, 7);

                            param.put("ktime", format1.parse(param.get("time").toString()).getTime());
                            param.put("etime", cal.getTimeInMillis()-1);
                            //System.out.println("一周" + param.get("ktime"));
                            //System.out.println("一周" + param.get("etime"));
                        } else if (param.get("type").toString().equals("m")) {
                            cal.setTime(format1.parse(param.get("time").toString()));
                            //获取某月最大天数
                            int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
                            //设置日历中月份的最大天数
                            cal.set(Calendar.DAY_OF_MONTH, lastDay);
                            cal.set(Calendar.HOUR_OF_DAY, 23);
                            cal.set(Calendar.MINUTE, 59);
                            cal.set(Calendar.SECOND, 59);
                            cal.set(Calendar.MILLISECOND, 999);
                            param.put("ktime", format1.parse(param.get("time").toString()).getTime());
                            param.put("etime", cal.getTimeInMillis());
                            //System.out.println("一月" + param.get("ktime"));
                            //System.out.println("一月" + param.get("etime"));
                        } else if (param.get("type").toString().equals("y")) {
                            cal.setTime(format1.parse(param.get("time").toString()));
                            //获取年最大天数
                            int lastDay = cal.getActualMaximum(Calendar.DAY_OF_YEAR);
                            //设置日历中月份的最大天数
                            cal.set(Calendar.DAY_OF_YEAR, lastDay);
                            cal.set(Calendar.HOUR_OF_DAY, 23);
                            cal.set(Calendar.MINUTE, 59);
                            cal.set(Calendar.SECOND, 59);
                            cal.set(Calendar.MILLISECOND, 999);

                            param.put("ktime", format1.parse(param.get("time").toString()).getTime());
                            param.put("etime", cal.getTimeInMillis());
                            //System.out.println("一年" + param.get("ktime"));
                            //System.out.println("一年" + param.get("etime"));
                        } else {
                            cal.setTime(format1.parse(param.get("time").toString()));
                            cal.set(Calendar.HOUR_OF_DAY, 23);
                            cal.set(Calendar.MINUTE, 59);
                            cal.set(Calendar.SECOND, 59);
                            cal.set(Calendar.MILLISECOND, 999);
                            param.put("ktime", format1.parse(param.get("time").toString()).getTime());
                            param.put("etime", cal.getTimeInMillis());
                            //System.out.println("一天" + param.get("ktime"));
                            //System.out.println("一天" + param.get("etime"));
                        }
                    }else{
                        if (param.get("type").toString().equals("m")) {
                            param.put("ktime", JzbDateUtil.getCurrentMonthFirstDay());
                            param.put("etime", JzbDateUtil.getCurrentMonthLastDay());
                        }else if(param.get("type").toString().equals("w")){
                            param.put("ktime", JzbDateUtil.getFirstDayOfWeek(new Date()));
                            param.put("etime", JzbDateUtil.getLastDayOfWeek(new Date()));
                        }else if(param.get("type").toString().equals("d")){
                            param.put("ktime", JzbDateUtil.getFirstDayOfDay(new Date()));
                            param.put("etime", JzbDateUtil.getLastDayOfDay(new Date()));
                        }else if (param.get("type").toString().equals("y")){
                            param.put("ktime",JzbDateUtil.getCurrYearFirst().getTime());
                            param.put("etime",JzbDateUtil.getCurrYearLast().getTime());
                        }
                    }
                }
                //参照tbTempitemController
                JzbPageConvert.setPageRows(param);
                param.put("node",param.get("dept").toString());
                param.put("tabname","tb_plantask");
                List<Map<String, Object>> records = tbPlantaskService.getPantaskZifu(param);
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
                    node.put( "id" ,record.get("id")==null?"":record.get("id").toString());
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
                    node.put( "progressofwork" ,record.get("progressofwork")==null?"":record.get("progressofwork").toString());
                    if (record.get("starttime")!=null && record.get("endtime")!=null){
                        String [] valuedate={record.get("starttime").toString(),record.get("endtime").toString()};
                        node.put("valueDate",valuedate);
                    }else{
                        node.put("valueDate","");
                    }
                    node.put( "status" ,record.get("status")==null?"":record.get("status").toString());
                    node.put( "dutyid" ,record.get("dutyid")==null?"":record.get("dutyid").toString().trim().split(","));
                    node.put("dutyid",record.get("dutyid")==null?"":record.get("dutyid").toString().trim().split(","));
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
                        //System.out.println("-0-0-0-0-00-0---------------"+ tempNodeId + "\t\t" + tempNode.toString());
                    } else {
                        // Error node
                        //recordJson.getJSONObject(tempNodeId).getJSONArray("children").add(tempNode);
                        //System.out.println("========================ERROR>> " + tempNodeId + "\t\t" + tempNode.toString());
                        //tempNode.setString("parentid";
                        tempNode.put("parentid","0000000");
                        result.add(tempNode);

                    }
                }

                PageInfo inf=new PageInfo();
                inf.setPages(count);
                // 定义返回结果
                response = Response.getResponseSuccess(userInfo);
                //System.out.println(result==null?"[]":result);
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
