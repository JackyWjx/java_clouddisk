package com.jzb.operate.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jzb.base.data.JzbDataType;
import com.jzb.base.data.date.JzbDateStr;
import com.jzb.base.data.date.JzbDateUtil;
import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.operate.api.resource.MethodDataApi;
import com.jzb.operate.api.resource.MethodTypeApi;
import com.jzb.operate.service.TbCompanyMethodService;
import com.jzb.operate.service.TbCompanyMethodTargetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenzhengduan、
 * 企业方法论
 */
@RestController
@RequestMapping(value = "/operate/companyMethod")
public class TbCompanyMethodController {

    @Autowired
    private TbCompanyMethodService tbCompanyMethodService;
    @Autowired
    private MethodDataApi methodDataApi;
    @Autowired
    private MethodTypeApi methodTypeApi;

    @Autowired
    private TbCompanyMethodTargetService tbCompanyMethodTargetService;

    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(TbCompanyMethodController.class);

    /**
     * 导入方法论
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/addCompanyMethod", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response addCompanyMethod(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/operate/companyMethod/addCompanyMethod";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            // 如果指定参数为空则返回error
            if (JzbCheckParam.haveEmpty(param, new String[]{"list"})) {
                result = Response.getResponseError();
                logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "addCompanyMethod Method", "[param error] or [param is null]"));
            } else {
                List<Map<String, Object>> list = (List<Map<String, Object>>) param.get("list");
                StringBuffer sb=new StringBuffer();
                Map<String,String> qmap=new HashMap<>();
                Map<String,String> cmap=new HashMap<>();
                long time = System.currentTimeMillis();


                for (int i = 0; i < list.size(); i++) {
                    //list.get(i).put("plantime", JzbDateUtil.getDate(list.get(i).get("plantime").toString(), JzbDateStr.yyyy_MM_dd).getTime());
                    list.get(i).put("adduid", userInfo.get("uid").toString());
                    list.get(i).put("addtime", time);
                    list.get(i).put("updtime", time);
                    list.get(i).put("methodid",list.get(i).get("typeid"));
                    list.get(i).put("numberone",JzbRandom.getRandomChar(20));
                    qmap.put(list.get(i).get("typeid").toString(),list.get(i).get("projectid")==null?"":list.get(i).get("projectid").toString());
                    cmap.put(list.get(i).get("typeid").toString(),list.get(i).get("cid")==null?"":list.get(i).get("cid").toString());
                    sb.append("'"+list.get(i).get("typeid")+"'");

                    if(i!=list.size()-1){
                        sb.append(",");
                    }
                }
                param.put("typeids",sb.toString());
                Map<String,Object> map=new HashMap<>();
                map.put("cid",(list.get(0).get("cid")==null?"":list.get(0).get("cid").toString()));
                map.put("projectid",(list.get(0).get("projectid")==null?"":list.get(0).get("projectid").toString()));
                //删除重新导入
                tbCompanyMethodService.delcidsandprojectid(map);

                Response r= methodDataApi.getMethodDataTypeIdsAll(param);
                List<Map<String, Object>> datalist =r.getPageInfo().getList();
                // 遍历转格式
                for (int i = 0; i < datalist.size(); i++) {
                    datalist.get(i).put("addtime",time);
                    datalist.get(i).put("projectid",qmap.get(datalist.get(i).get("typeid").toString()));
                    datalist.get(i).put("cid",cmap.get(datalist.get(i).get("typeid").toString()));
                    datalist.get(i).put("numberone",JzbRandom.getRandomChar(20));
                    datalist.get(i).put("status",1);
                }
                int count=0;
                if(datalist.size()>0) {
                    count = tbCompanyMethodService.addMethodData(datalist);
                }
                int count2 = tbCompanyMethodService.addCompanyMethod(list);
                if (count > 0||count2>0) {
                    // 定义返回结果
                    result = Response.getResponseSuccess(userInfo);
                    result.setResponseEntity(count);

                } else {
                    result = Response.getResponseError();
                    logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "addMethodTypeBrother Method", "[addResult error]"));
                }
            }
        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "addCompanyMethod Method", ex.toString()));
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
     * @param param ()
     * @return
     * @deprecated 获取企业方法论类别
     */
    @RequestMapping(value = "/getCompanyMethod", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response getCompanyMethod(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/operate/companyMethod/getCompanyMethod";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }

                // Result JSON
                JSONArray result = new JSONArray();
                StringBuffer sb =new StringBuffer();
                //参照tbTempitemController
                List<Map<String, Object>> records = tbCompanyMethodService.queryCompanyMethod(param);
                if (JzbTools.isEmpty(records)) {
                    // Result JSON
                    result = null;
                }else {
                    for (int i = 0, a = records.size(); i < a; i++) {
                        System.out.println(records.get(i).get("typeid"));
                        sb.append("'" + records.get(i).get("methodid") + "',");
                    }
//                System.out.println(sb.toString().substring(0,sb.toString().length()-1));
                    param.put("typeids", sb.toString().substring(0, sb.toString().length() - 1));
                    List<Map<String,Object>> list = tbCompanyMethodService.getCompanyMethoddataAll(param);

                    if (list.size() > 0) {

                        for (int i = 0, l = list.size(); i < l; i++) {
                            Map<String, Object> addmap = new HashMap<String, Object>();
                            addmap.put("cname", list.get(i).get("cname"));
                            if (list.get(i).get("parentid").equals("0000000")) {
                                addmap.put("parentid", list.get(i).get("typeid"));
                            } else {
                                addmap.put("parentid", list.get(i).get("parentid"));
                            }
                            addmap.put("methodid", list.get(i).get("dataid"));
                            addmap.put("information", list.get(i).get("information"));
                            addmap.put("constraintcondition", list.get(i).get("paststandards"));
                            addmap.put("score", list.get(i).get("grade"));
                            addmap.put("addtime", list.get(i).get("addtime"));
                            addmap.put("days", list.get(i).get("addtime"));
                            addmap.put("plantime", null);
                            addmap.put("numberone", list.get(i).get("numberone"));

                            records.add(addmap);
                        }
                    }


//                    // Result JSON
//                    JSONArray result = new JSONArray();

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
                        node.put("methodid", record.get("methodid")==null?"":record.get("methodid").toString());
                        node.put("cname", JzbDataType.getString(record.get("cname")));
                        node.put("parentid", parentId);
                        node.put("addtime", JzbDateUtil.toDateString(JzbDataType.getLong(record.get("addtime")), JzbDateStr.yyyy_MM_dd));
                        //node.put("days", JzbDataType.getString(record.get("days")));
                        //node.put("plantime", JzbDateUtil.toDateString(JzbDataType.getLong(record.get("plantime")), JzbDateStr.yyyy_MM_dd));
                        node.put("summary", record.get("summary")==null?"":record.get("summary").toString());
                        node.put("money", record.get("money")==null?"":record.get("money").toString());
                        node.put("information", record.get("information")==null?"":record.get("information").toString());
                        node.put("constraintcondition", record.get("constraintcondition")==null?"":record.get("constraintcondition").toString());
                        node.put("idx", record.get("idx")==null?"":record.get("idx").toString());
                        node.put("score", record.get("score")==null?"":record.get("score").toString());
                        node.put("numberone", record.get("numberone")==null?"":record.get("numberone").toString());
                        node.put("children", new JSONArray());
                        if (JzbTools.isEmpty(record.get("score"))) {
                            node.put("score", null);
                        } else {
                            node.put("score", record.get("score").toString());
                        }

                        node.put("days", JzbDataType.getString(record.get("days")));

                        // if root node
                        if (parentId.equals(firstParent)) {
                            result.add(node);
                            recordJson.put(record.get("methodid").toString(), node);

                            // if parent exist
                        } else if (recordJson.containsKey(parentId)) {
                            // add children
                            recordJson.getJSONObject(parentId).getJSONArray("children").add(node);
                            recordJson.put(record.get("methodid").toString(), node);
                            // Unknown relation node
                        } else {
                            String nodeId = record.get("methodid").toString();
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
                                        recordJson.put(tempNode.get("methodid").toString(), tempNode);
                                        unknownRecord.remove(tempNode.get("methodid").toString());
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
                        } else {
                            // Error node
                            System.out.println("========================ERROR>> " + tempNodeId + "\t\t" + tempNode.toString());
                        }
                    }
                }

                response = Response.getResponseSuccess(userInfo);
                response.setResponseEntity(result);




        } catch (Exception e) {
            flag = false;
            // 打印异常信息
            e.printStackTrace();
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getMethodType Method", e.toString()));
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
     * @Author Reed
     * @Description //获取我的方法论
     * @Date 15:07 2020/1/17
     * @Param [param]
     * @return com.jzb.base.message.Response
    **/
    @RequestMapping(value = "/getMyCompanyMethod", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response getMyCompanyMethod(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/operate/companyMethod/getMyCompanyMethod";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }

            // Result JSON
            JSONArray result = new JSONArray();
            StringBuffer sb =new StringBuffer();
            //参照tbTempitemController
            List<Map<String, Object>> records = tbCompanyMethodService.queryMyCompanyMethod(param);
            if (JzbTools.isEmpty(records)) {
                // Result JSON
                result = null;
            }else {
                for (int i = 0, a = records.size(); i < a; i++) {
                    System.out.println(records.get(i).get("typeid"));
                    sb.append("'" + records.get(i).get("methodid") + "',");
                }
//                System.out.println(sb.toString().substring(0,sb.toString().length()-1));
                param.put("typeids", sb.toString().substring(0, sb.toString().length() - 1));
                List<Map<String,Object>> list = tbCompanyMethodService.getMyCompanyMethoddataAll(param);

                if (list.size() > 0) {

                    for (int i = 0, l = list.size(); i < l; i++) {
                        Map<String, Object> addmap = new HashMap<String, Object>();
                        addmap.put("cname", list.get(i).get("cname"));
                        if (list.get(i).get("parentid").equals("0000000")) {
                            addmap.put("parentid", list.get(i).get("typeid"));
                        } else {
                            addmap.put("parentid", list.get(i).get("parentid"));
                        }
                        addmap.put("methodid", list.get(i).get("dataid"));
                        addmap.put("information", list.get(i).get("information"));
                        addmap.put("constraintcondition", list.get(i).get("paststandards"));
                        addmap.put("score", list.get(i).get("grade"));
                        addmap.put("addtime", list.get(i).get("addtime"));
                        addmap.put("days", list.get(i).get("addtime"));
                        addmap.put("plantime", null);
                        addmap.put("numberone", list.get(i).get("numberone"));

                        records.add(addmap);
                    }
                }


//                    // Result JSON
//                    JSONArray result = new JSONArray();

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
                    node.put("methodid", record.get("methodid")==null?"":record.get("methodid").toString());
                    node.put("cname", JzbDataType.getString(record.get("cname")));
                    node.put("parentid", parentId);
                    node.put("addtime", JzbDateUtil.toDateString(JzbDataType.getLong(record.get("addtime")), JzbDateStr.yyyy_MM_dd));
                    //node.put("days", JzbDataType.getString(record.get("days")));
                    //node.put("plantime", JzbDateUtil.toDateString(JzbDataType.getLong(record.get("plantime")), JzbDateStr.yyyy_MM_dd));
                    node.put("summary", record.get("summary")==null?"":record.get("summary").toString());
                    node.put("money", record.get("money")==null?"":record.get("money").toString());
                    node.put("information", record.get("information")==null?"":record.get("information").toString());
                    node.put("constraintcondition", record.get("constraintcondition")==null?"":record.get("constraintcondition").toString());
                    node.put("idx", record.get("idx")==null?"":record.get("idx").toString());
                    node.put("score", record.get("score")==null?"":record.get("score").toString());
                    node.put("numberone", record.get("numberone")==null?"":record.get("numberone").toString());
                    node.put("children", new JSONArray());
                    if (JzbTools.isEmpty(record.get("score"))) {
                        node.put("score", null);
                    } else {
                        node.put("score", record.get("score").toString());
                    }

                    node.put("days", JzbDataType.getString(record.get("days")));

                    // if root node
                    if (parentId.equals(firstParent)) {
                        result.add(node);
                        recordJson.put(record.get("methodid").toString(), node);

                        // if parent exist
                    } else if (recordJson.containsKey(parentId)) {
                        // add children
                        recordJson.getJSONObject(parentId).getJSONArray("children").add(node);
                        recordJson.put(record.get("methodid").toString(), node);
                        // Unknown relation node
                    } else {
                        String nodeId = record.get("methodid").toString();
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
                                    recordJson.put(tempNode.get("methodid").toString(), tempNode);
                                    unknownRecord.remove(tempNode.get("methodid").toString());
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
                    } else {
                        // Error node
                        System.out.println("========================ERROR>> " + tempNodeId + "\t\t" + tempNode.toString());
                    }
                }
            }

            response = Response.getResponseSuccess(userInfo);
            response.setResponseEntity(result);




        } catch (Exception e) {
            flag = false;
            // 打印异常信息
            e.printStackTrace();
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getMyCompanyMethod Method", e.toString()));
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
     * @param param ()
     * @return
     * @deprecated 获取企业方法论类别 排除已导入的数据
     */
    @RequestMapping(value = "/getCompanyMethodByids", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response getCompanyMethodByids(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/operate/companyMethod/getCompanyMethodByids";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }


                //List<Map<String,Object>>listall=tbCompanyMethodService.getCompanyMethodByids(param);
                List<Map<String,Object>>retlist=null;


                param.put("typeids","'1'");
                retlist=methodTypeApi.getMethodTypedels(param).getPageInfo().getList();

                response = Response.getResponseSuccess(userInfo);
                PageInfo pi = new PageInfo<>();
                pi.setList(retlist);
                response.setPageInfo(pi);

        } catch (Exception e) {
            flag = false;
            // 打印异常信息
            e.printStackTrace();
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getMethodType Method", e.toString()));
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
     * 设置方法论的状态
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/updateCompanyMethodStatus", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response updateCompanyMethod(@RequestBody Map<String, Object> param) {
        Response response;
        try {
            // 如果指定参数为空则返回error
            if (JzbCheckParam.haveEmpty(param, new String[]{"cid", "methodid"})) {
                response = Response.getResponseError();
            } else {
                response = tbCompanyMethodService.updateCompanyMethodStatus(param) > 0 ? Response.getResponseSuccess((Map<String, Object>) param.get("userinfo")) : Response.getResponseError();
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            response = Response.getResponseError();
        }
        return response;
    }




    /**
     * 设置方法论的状态
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/delCompanyMethod", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response delCompanyMethod(@RequestBody Map<String, Object> param) {
        Response response = null;
        try {
            // 如果指定参数为空则返回error
            if (JzbCheckParam.haveEmpty(param, new String[]{"numberone"})) {
                response = Response.getResponseError();
            } else {
                StringBuffer sb=new StringBuffer();
                 if (param.get("numberone")!=null){
                     String [] s=param.get("numberone").toString().replaceAll("\\[","").replaceAll("\\]","").split(",");
                     for (int i=0;i<s.length;i++){
                         sb.append("'"+s[i].trim()+"'");
                         if(i!=s.length-1){
                             sb.append(",");
                         }
                     }
                 }
                tbCompanyMethodService.delCompanyMethod(sb.toString());
                response=Response.getResponseSuccess();
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            response = Response.getResponseError();
        }
        return response;
    }

    /**
     * @param param
     * @return
     * @deprecated 添加我的方法论同级
     */
    @RequestMapping(value = "/addMyMethodTypeBrother", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response addMyMethodTypeBrother(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/methodType/addMyMethodTypeBrother";
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
            if (JzbCheckParam.haveEmpty(param, new String[]{"cname", "ouid"})) {
                result = Response.getResponseError();
                logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "addMethodTypeBrother Method", "[param error] or [param is null]"));
            } else {
                // 添加返回值大于0 则添加成功
                int count = tbCompanyMethodService.addMyMethodTypeBrother(param);
                if (count > 0) {
                    // 定义返回结果
                    result = Response.getResponseSuccess(userInfo);
                } else {
                    result = Response.getResponseError();
                    logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "addMethodTypeBrother Method", "[addResult error]"));
                }
            }
        } catch (Exception e) {
            flag = false;
            // 打印异常信息
            e.printStackTrace();
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "addMyMethodTypeBrother Method", e.toString()));
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
     * @Author Reed
     * @Description //修改我的方法论同级
     * @Date 15:59 2020/1/17
     * @Param [param]
     * @return com.jzb.base.message.Response
    **/
    @RequestMapping(value = "/updateMyMethodType", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response updateMyMethodType(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/methodType/updateMyMethodType";
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
            if (JzbCheckParam.haveEmpty(param, new String[]{"cname", "ouid", "typeid",})) {
                result = Response.getResponseError();
                logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "updateMethodType Method", "[param error] or [param is null]"));
            } else {
                // 添加返回值大于0 则添加成功
                int count = tbCompanyMethodService.updateMyMethodType(param);
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
     * @Author Reed
     * @Description 新建我的方法论详情同级
     * @Date 17:04 2020/1/17
     * @Param [param]
     * @return com.jzb.base.message.Response
    **/
    @RequestMapping(value = "/addMyMethodDataBrother", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response addMyMethodDataBrother(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/methodData/addMyMethodDataBrother";
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
            if (JzbCheckParam.haveEmpty(param, new String[]{"typeid", "ouid"})) {
                result = Response.getResponseError();
                logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "addMethodTypeBrother Method", "[param error] or [param is null]"));
            } else {
                String dataid = param.get("typeid") + JzbRandom.getRandomCharCap(4);


                param.put("dataid", dataid);
                // 添加返回值大于0 则添加成功
                int count = tbCompanyMethodService.addMyMethodDataBrother(param);
                if (count > 0) {
                    // 定义返回结果
                    result = Response.getResponseSuccess(userInfo);
                } else {
                    result = Response.getResponseError();
                    logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "addMyMethodDataBrother Method", "[addResult error]"));
                }
            }
        } catch (Exception e) {
            flag = false;
            // 打印异常信息
            e.printStackTrace();
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "addMyMethodDataBrother Method", e.toString()));
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
     * @Author Reed
     * @Description 新建我的方法论详情子级
     * @Date 17:14 2020/1/17
     * @Param [param]
     * @return com.jzb.base.message.Response
    **/
    @RequestMapping(value = "/addMyMethodDataSon", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response addMyMethodDataSon(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/methodData/addMyMethodDataSon";
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
            if (JzbCheckParam.haveEmpty(param, new String[]{"cname", "ouid", "parentid"})) {
                result = Response.getResponseError();
                logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "addMyMethodDataSon Method", "[param error] or [param is null]"));
            } else {
                String dataid = param.get("typeid") + JzbRandom.getRandomCharCap(4);
                param.put("dataid", dataid);
                // 添加返回值大于0 则添加成功
                int count = tbCompanyMethodService.addMyMethodDataBrother(param);
                if (count > 0) {
                    // 定义返回结果
                    result = Response.getResponseSuccess(userInfo);
                } else {
                    result = Response.getResponseError();
                    logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "addMyMethodDataSon Method", "[addResult error]"));
                }
            }
        } catch (Exception e) {
            flag = false;
            // 打印异常信息
            e.printStackTrace();
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "addMethodTypeSon Method", e.toString()));
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
     * 修改我的方法论资料
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/updateMyMethodData", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response updateMethodData(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/methodData/updateMyMethodData";
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
            if (JzbCheckParam.haveEmpty(param, new String[]{"typeid", "dataid"})) {
                result = Response.getResponseError();
                logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "updateMyMethodData Method", "[param error] or [param is null]"));
            } else {
                // 修改返回值大于0 则修改成功
                int count = tbCompanyMethodService.updateMyMethodData(param);
                if (count > 0) {
                    // 定义返回结果
                    result = Response.getResponseSuccess(userInfo);
                } else {
                    result = Response.getResponseError();
                    logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "updateMethodData Method", "[sql update error]"));
                }
            }
        } catch (Exception e) {
            flag = false;
            // 打印异常信息
            e.printStackTrace();
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "updateMethodData Method", e.toString()));
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
     * 删除我的方法论
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/delMyCompanyMethod", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response delMyCompanyMethod(@RequestBody Map<String, Object> param) {
        Response response = null;
        try {
            // 如果指定参数为空则返回error
            if (JzbCheckParam.haveEmpty(param, new String[]{"numberone"})) {
                response = Response.getResponseError();
            } else {
                StringBuffer sb=new StringBuffer();
                if (param.get("numberone")!=null){
                    String [] s=param.get("numberone").toString().replaceAll("\\[","").replaceAll("\\]","").split(",");
                    for (int i=0;i<s.length;i++){
                        sb.append("'"+s[i].trim()+"'");
                        if(i!=s.length-1){
                            sb.append(",");
                        }
                    }
                }
                tbCompanyMethodService.delMyCompanyMethod(sb.toString());
                response=Response.getResponseSuccess();
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            response = Response.getResponseError();
        }
        return response;
    }


}
