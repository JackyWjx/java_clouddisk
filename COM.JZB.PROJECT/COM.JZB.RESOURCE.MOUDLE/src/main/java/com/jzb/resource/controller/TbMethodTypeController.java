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
import com.jzb.resource.service.TbMethodTypeService;
import com.jzb.resource.util.PageConvert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 方法论类型
 *
 * @author chenzhengduan
 */
@RestController
@RequestMapping(value = "/methodType")
public class TbMethodTypeController {

    @Autowired
    private TbMethodTypeService tbMethodTypeService;

    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(TbMethodTargetController.class);

    /**
     * @param param (keyword)
     * @return
     * @deprecated 获取方法论类别
     */
    @RequestMapping(value = "/getMethodType", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response getMethodType(@RequestBody(required = false) Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/methodType/getMethodType";
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
            List<Map<String, Object>> records = tbMethodTypeService.getMethodType(param);

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
                node.put("methodInfo",record.get("methodInfo"));
                System.out.println(record.get("methodInfo"));

                node.put("money", record.get("money")==null?"":record.get("money").toString());
                node.put("information", record.get("information")==null?"":record.get("information").toString());
                node.put("constraintcondition", record.get("constraintcondition")==null?"":record.get("constraintcondition").toString());
                node.put("typeid", record.get("typeid")==null?"":record.get("typeid").toString());

                node.put("cname", record.get("cname")==null?"":record.get("cname").toString());
                node.put("parentid", parentId);
                node.put("addtime", JzbDateUtil.toDateString(JzbDataType.getLong(record.get("addtime")), JzbDateStr.yyyy_MM_dd));
                node.put("typedesc", record.get("typedesc")==null?"":record.get("typedesc").toString());
                node.put("idx", record.get("idx")==null?"":record.get("idx").toString());
                node.put("summary", record.get("summary")==null?"":record.get("summary").toString());
                node.put("children", new JSONArray());
                if (JzbTools.isEmpty(record.get("score"))) {
                    node.put("score", null);
                } else {
                    node.put("score", record.get("score").toString());
                }

                if (JzbTools.isEmpty(record.get("days"))) {
                    node.put("days", null);
                } else {
                    node.put("days", record.get("days").toString());
                }

                // if root node
                if (parentId.equals(firstParent)) {
                    result.add(node);
                    recordJson.put(record.get("typeid").toString(), node);

                    // if parent exist
                } else if (recordJson.containsKey(parentId)) {
                    // add children
                    recordJson.getJSONObject(parentId).getJSONArray("children").add(node);
                    recordJson.put(record.get("typeid").toString(), node);
                    // Unknown relation node
                } else {
                    String nodeId = record.get("typeid").toString();
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
                                recordJson.put(tempNode.get("typeid").toString(), tempNode);
                                unknownRecord.remove(tempNode.get("typeid").toString());
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
     * @param param (keyword)
     * @return
     * @deprecated 获取方法论类别(分页)
     */
    @RequestMapping(value = "/getMethodTypePage", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response getMethodTypePage(@RequestBody(required = false) Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/methodType/getMethodTypePage";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            if (JzbCheckParam.haveEmpty(param, new String[]{"pageno", "pagesize"})) {
                response = Response.getResponseError();
                logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getMethodTypePage Method", "[param error] or [param is null]"));
            } else {

                // 设置分页参数
                PageConvert.setPageRows(param);
                //参照tbTempitemController
                List<Map<String, Object>> list = tbMethodTypeService.queryMethodTypePage(param);

                // 遍历转换时间格式
                for (int i = 0, l = list.size(); i < l; i++) {
                    list.get(i).put("addtime", JzbDateUtil.toDateString(JzbDataType.getLong(list.get(i).get("addtime")), JzbDateStr.yyyy_MM_dd));
                }
                // 查询分页数据--总数
                int count = tbMethodTypeService.queryMethodTypePageCount();

                // 定义pageinfo
                PageInfo pi = new PageInfo();
                pi.setList(list);
                pi.setTotal(count);

                // 定义返回结果
                response = Response.getResponseSuccess(userInfo);
                response.setPageInfo(pi);
            }

        } catch (Exception e) {
            flag = false;
            // 打印异常信息
            e.printStackTrace();
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getMethodTypePage Method", e.toString()));
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
     * @param param (keyword)
     * @return
     * @deprecated 获取方法论类别(分页)
     */
    @RequestMapping(value = "/getMethodTypePageSon", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response getMethodTypePageSon(@RequestBody(required = false) Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/methodType/getMethodTypePageSon";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }

            if (JzbCheckParam.haveEmpty(param, new String[]{"pageno", "pagesize"})) {
                response = Response.getResponseError();
                logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getMethodTypePageSon Method", "[param error] or [param is null]"));
            } else {
                // 设置分页参数
                PageConvert.setPageRows(param);
                //参照tbTempitemController
                List<Map<String, Object>> list = tbMethodTypeService.queryMethodTypePageSon(param);
                for (int i = 0, l = list.size(); i < l; i++) {
                    list.get(i).put("addtime", JzbDateUtil.toDateString(JzbDataType.getLong(list.get(i).get("addtime")), JzbDateStr.yyyy_MM_dd));
                }
                // 定义pageinfo
                PageInfo pi = new PageInfo();
                pi.setList(list);
                pi.setTotal(list.size());

                // 定义返回结果
                response = Response.getResponseSuccess(userInfo);
                response.setPageInfo(pi);
            }
        } catch (Exception e) {
            flag = false;
            // 打印异常信息
            e.printStackTrace();
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getMethodTypePageSon Method", e.toString()));
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
     * @param param
     * @return
     * @deprecated 添加方法论同级
     */
    @RequestMapping(value = "/addMethodTypeBrother", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response addMethodTypeBrother(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/methodType/addMethodTypeBrother";
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
                int count = tbMethodTypeService.saveMethodTypeBrother(param);
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
     * @return 暂时废弃 取消了添加子级 lifei
     * @deprecated 添加方法论子级
     */
    @RequestMapping(value = "/addMethodTypeSon", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response addMethodTypeSon(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/methodType/addMethodTypeSon";
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
                // 添加返回值大于0 则添加成功
                int count = tbMethodTypeService.saveMethodTypeSon(param);
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
     * @param param
     * @return
     * @deprecated 修改方法论
     */
    @RequestMapping(value = "/updateMethodType", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response updateMethodType(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/methodType/updateMethodType";
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
                int count = tbMethodTypeService.updateMethodType(param);
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
     * @return
     * @deprecated 获取方法论level  参数 parantid  有查子，无查父
     */
    @RequestMapping(value = "/getMethodLevel", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response getMethodLevel(@RequestBody(required = false) Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/methodType/updateMethodType";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            // 查询结果
            List<Map<String, Object>> list = tbMethodTypeService.getMethodLevel(param);
            // 定义返回结果
            result = Response.getResponseSuccess(userInfo);
            PageInfo pi = new PageInfo<>();
            pi.setList(list);
            result.setPageInfo(pi);
        } catch (Exception e) {
            flag=false;
            // 打印异常信息
            e.printStackTrace();
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getMethodLevel Method", e.toString()));
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
     * @deprecated 批量删除方法论类别
     */
    @RequestMapping(value = "/delMethodType", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response delMethodType(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/methodType/delMethodType";
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
            if (JzbCheckParam.haveEmpty(param, new String[]{"typeids"})) {
                result = Response.getResponseError();
                logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "updateMethodType Method", "[param error] or [param is null]"));
            } else {
                StringBuffer sb=new StringBuffer();
                String [] s=param.get("typeids").toString().replaceAll("\\[","").replaceAll("\\]","").split(",");
                for (int i=0;i<s.length;i++){
                    sb.append("'"+s[i].trim()+"'");
                    if(i!=s.length-1){
                        sb.append(",");
                    }
                }
                param.put("typeids",sb.toString());
                // 添加返回值大于0 则删除成功
                int count = tbMethodTypeService.delMethodType(param);
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
     * @deprecated 获取方法论类别排除已勾选的 类别 单位 项目
     */
    @RequestMapping(value = "/getMethodTypedels", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response getMethodTypedels(@RequestBody(required = false) Map<String, Object> param) {
        Response response = null;
        Map<String, Object> userInfo = null;
        String api = "/methodType/getMethodTypedels";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }

            if (JzbCheckParam.haveEmpty(param, new String[]{"typeids"})) {
                response = Response.getResponseError();
                logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "updateMethodType Method", "[param error] or [param is null]"));
            }else{
                //参照tbTempitemController
                List<Map<String, Object>> records = tbMethodTypeService.getMethodTypeDel(param);

                for (int i=0;i<records.size();i++){
                    records.get(i).put("addtime", JzbDateUtil.toDateString(JzbDataType.getLong(records.get(i).get("addtime")), JzbDateStr.yyyy_MM_dd));
                }


                PageInfo pi = new PageInfo();
                pi.setList(records);
                // 定义返回结果
                response = Response.getResponseSuccess(userInfo);
                response.setPageInfo(pi);
            }

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


}
