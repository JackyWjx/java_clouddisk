package com.jzb.resource.controller;

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
import com.jzb.resource.service.TbMethodDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenzhengduan
 * @date 2019-8-15
 * 获取方法论资料
 */
@RestController
@RequestMapping(value = "/methodData")
public class TbMethodDataController {

    @Autowired
    private TbMethodDataService tbMethodDataService;

    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(TbMethodDataController.class);

//    /**
//     * 查询方法论资料
//     *
//     * @param param
//     * @return
//     */
//    @RequestMapping(value = "/getMethodData", method = RequestMethod.POST)
//    @ResponseBody
//    @Transactional
//    @CrossOrigin
//    public Response getMethodData(@RequestBody Map<String, Object> param) {
//        Response result;
//        Map<String, Object> userInfo = null;
//        String api = "/methodData/getMethodData";
//        boolean flag = true;
//        try {
//            if (param.get("userinfo") != null) {
//                userInfo = (Map<String, Object>) param.get("userinfo");
//                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
//                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
//            } else {
//                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
//            }
//            // 验证如果指定参数为空则返回404  error
//            if (JzbCheckParam.haveEmpty(param, new String[]{"typeid"})) {
//                result = Response.getResponseError();
//                logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getMethodData Method", "[param error] or [param is null]"));
//
//            } else {
//                // 查询结果
//                List<Map<String, Object>> list = tbMethodDataService.quertMethodData(param);
//                // 遍历转格式
//                for (int i = 0, l = list.size(); i < l; i++) {
//                    list.get(i).put("addtime", JzbDateUtil.toDateString(JzbDataType.getLong(list.get(i).get("addtime")), JzbDateStr.yyyy_MM_dd));
//                }
//                // 定义pageinfo
//                PageInfo pi = new PageInfo();
//                pi.setList(list);
//
//                // 定义返回结果
//                result = Response.getResponseSuccess(userInfo);
//                result.setPageInfo(pi);
//            }
//        } catch (Exception e) {
//            flag = false;
//            // 打印异常信息
//            e.printStackTrace();
//            result = Response.getResponseError();
//            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getMethodData Method", e.toString()));
//        }
//        if (userInfo != null) {
//            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
//                    userInfo.get("msgTag").toString(), "User Login Message"));
//        } else {
//            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
//        }
//        return result;
//    }

    /**
     * 添加方法论资料
     * @param param
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/addMethodData", method = RequestMethod.POST)
    @Transactional
    @ResponseBody
    public Response addMethodData(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/methodData/addMethodData";
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
            if (JzbCheckParam.haveEmpty(param, new String[]{"typeid"})) {
                result = Response.getResponseError();
                logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "addMethodData Method", "[param error] or [param is null]"));

            } else {
                // 添加返回值大于0 则添加成功
                String dataid = param.get("typeid") + JzbRandom.getRandomCharCap(4);
                param.put("dataid", dataid);
                int count = tbMethodDataService.saveMethodData(param);
                if (count > 0) {
                    // 定义返回结果
                    result = Response.getResponseSuccess(userInfo);
                    // 将dataid返回
                    Map<String, Object> map = new HashMap<>();
                    map.put("dataid", dataid);
                    result.setResponseEntity(map);
                } else {
                    result = Response.getResponseError();
                    logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "addMethodData Method", "[sql add error]"));
                }
            }
        } catch (Exception e) {
            flag = false;
            // 打印异常信息
            e.printStackTrace();
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "addMethodData Method", e.toString()));
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
     * 修改方法论资料
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/updateMethodData", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response updateMethodData(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/methodData/updateMethodData";
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
                logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "updateMethodData Method", "[param error] or [param is null]"));
            } else {
                // 修改返回值大于0 则修改成功
                int count = tbMethodDataService.updateMethodData(param);
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
     * @param param
     * @return
     * @deprecated 添加方法论资料同级
     */
    @RequestMapping(value = "/addMethodDataBrother", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response addMethodDataBrother(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/methodData/addMethodDataBrother";
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
                int count = tbMethodDataService.saveMethodDatanew(param);
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



    /**
     * @param param
     * @return
     * @deprecated 添加方法论资料子级 lifei
     */
    @RequestMapping(value = "/addMethodDataSon", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response addMethodDataSon(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/methodData/addMethodDataSon";
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
                logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "addMethodTypeSon Method", "[param error] or [param is null]"));
            } else {
                String dataid = param.get("typeid") + JzbRandom.getRandomCharCap(4);
                param.put("dataid", dataid);
                // 添加返回值大于0 则添加成功
                int count = tbMethodDataService.saveMethodDatanew(param);
                if (count > 0) {
                    // 定义返回结果
                    result = Response.getResponseSuccess(userInfo);
                } else {
                    result = Response.getResponseError();
                    logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "addMethodTypeSon Method", "[addResult error]"));
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
     * @param param (keyword) lifei
     * @return
     * @deprecated 获取方法论类别(父子级)
     */
    @RequestMapping(value = "/getMethodDataItme", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response getMethodDataItme(@RequestBody(required = false) Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/methodData/getMethodDataItme";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            //参照tbTempitemController
            List<Map<String, Object>> records = tbMethodDataService.getMethodDataItme(param);

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


                node.put("paststandards", record.get("paststandards")==null?"":record.get("paststandards").toString());
                node.put("information", record.get("information")==null?"":record.get("information").toString());
                node.put("grade", record.get("grade")==null?"":record.get("grade").toString());
                node.put("dataid", record.get("dataid").toString());
                node.put("cname", record.get("cname")==null?"":record.get("cname").toString());
                node.put("parentid", parentId);
                node.put("addtime", JzbDateUtil.toDateString(JzbDataType.getLong(record.get("addtime")), JzbDateStr.yyyy_MM_dd));
                node.put("typeid", record.get("typeid").toString());
                node.put("typedesc", record.get("typedesc")==null?"":record.get("typedesc").toString());
                node.put("idx", record.get("idx")==null?"":record.get("idx").toString());
                node.put("summary", record.get("summary")==null?"":record.get("summary").toString());
                node.put("children", new JSONArray());


                // if root node
                if (parentId.equals(firstParent)) {
                    result.add(node);
                    recordJson.put(record.get("dataid").toString(), node);

                    // if parent exist
                } else if (recordJson.containsKey(parentId)) {
                    // add children
                    recordJson.getJSONObject(parentId).getJSONArray("children").add(node);
                    recordJson.put(record.get("dataid").toString(), node);
                    // Unknown relation node
                } else {
                    String nodeId = record.get("dataid").toString();
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
                                recordJson.put(tempNode.get("dataid").toString(), tempNode);
                                unknownRecord.remove(tempNode.get("dataid").toString());
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
            // 定义返回结果
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
     * 删除方法论资料 (父子级) lifei
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/delMethodData", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response delMethodData(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/methodData/delMethodData";
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
                logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "updateMethodData Method", "[param error] or [param is null]"));
            } else {
                // 修改返回值大于0 则修改成功
                int count = tbMethodDataService.delMethodData(param);
                if (count > 0) {
                    // 定义返回结果
                    result = Response.getResponseSuccess(userInfo);
                    result.setResponseEntity(count);
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
     * @param param
     * @return
     * @deprecated 批量删除方法论资料库
     */
    @RequestMapping(value = "/delMethodDataids", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response delMethodDataids(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/methodData/delMethodDataids";
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
            if (JzbCheckParam.haveEmpty(param, new String[]{"dataids"})) {
                result = Response.getResponseError();
                logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "updateMethodType Method", "[param error] or [param is null]"));
            } else {
                StringBuffer sb=new StringBuffer();
                String [] s=param.get("dataids").toString().replaceAll("\\[","").replaceAll("\\]","").split(",");
                for (int i=0;i<s.length;i++){
                    sb.append("'"+s[i].trim()+"'");
                    if(i!=s.length-1){
                        sb.append(",");
                    }
                }
                param.put("dataids",sb.toString());
                // 添加返回值大于0 则添加成功
                int count = tbMethodDataService.delMethodDataids(param);
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
     * 查询方法论资料 是否有子级 lifei
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getMethodDatazi", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    @CrossOrigin
    public Response getMethodDatazi(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/methodData/getMethodDatazi";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            // 验证如果指定参数为空则返回404  error
            if (JzbCheckParam.haveEmpty(param, new String[]{"dataid"})) {
                result = Response.getResponseError();
                logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getMethodData Method", "[param error] or [param is null]"));

            } else {
                // 查询结果
                int count  = tbMethodDataService.getMethodDatazi(param);

                if (count > 0) {
                    // 定义返回结果
                    result = Response.getResponseSuccess(userInfo);
                    result.setResponseEntity(count);

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
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getMethodData Method", e.toString()));
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
     * 查询方法论资料 所有typeids （ 父级 和子级）
     *lifei
     * @param param
     * @return
     */
    @RequestMapping(value = "/getMethodDataTypeIdsAll", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    @CrossOrigin
    public Response getMethodDataTypeIdsAll(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/methodData/getMethodDataTypeIdsAll";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            // 验证如果指定参数为空则返回404  error
            if (JzbCheckParam.haveEmpty(param, new String[]{"typeids"})) {
                result = Response.getResponseError();
                logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getMethodData Method", "[param error] or [param is null]"));

            } else {
                // 查询结果
                List<Map<String, Object>> list = tbMethodDataService.getMethodDataTypeIdsAll(param);
                // 遍历转格式
                for (int i = 0, l = list.size(); i < l; i++) {
                    list.get(i).put("addtime", JzbDateUtil.toDateString(JzbDataType.getLong(list.get(i).get("addtime")), JzbDateStr.yyyy_MM_dd));
                }
                // 定义pageinfo
                PageInfo pi = new PageInfo();
                pi.setList(list);

                // 定义返回结果
                result = Response.getResponseSuccess(userInfo);
                result.setPageInfo(pi);
            }
        } catch (Exception e) {
            flag = false;
            // 打印异常信息
            e.printStackTrace();
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getMethodData Method", e.toString()));
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
     * 查询方法论资料 根据方法论typeid查询
     *chenhui
     * @param param
     * @return
     */
    @RequestMapping(value = "/getMethodDataByTypeid", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    @CrossOrigin
    public Response getMethodDataByTypeid(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/methodData/getMethodDataByTypeid";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            // 验证如果指定参数为空则返回404  error
            if (JzbCheckParam.haveEmpty(param, new String[]{"typeid"})) {
                result = Response.getResponseError();
                logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getMethodDataByTypeid Method", "[param error] or [param is null]"));

            } else {
                // 查询结果
                List<Map<String, Object>> list = tbMethodDataService.getMethodDataByTypeid(param);

                // 定义pageinfo
                PageInfo pi = new PageInfo();
                pi.setList(list);

                // 定义返回结果
                result = Response.getResponseSuccess(userInfo);
                result.setPageInfo(pi);
            }
        } catch (Exception e) {
            flag = false;
            // 打印异常信息
            e.printStackTrace();
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getMethodDataByTypeid Method", e.toString()));
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