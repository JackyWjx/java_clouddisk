package com.jzb.operate.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.data.date.JzbDateStr;
import com.jzb.base.data.date.JzbDateUtil;
import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.Response;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbTools;
import com.jzb.operate.service.TbCompanyMethodService;
import com.netflix.discovery.converters.jackson.EurekaXmlJacksonCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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
        String  api="/operate/companyMethod/addCompanyMethod";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger( api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger( api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            // 如果指定参数为空则返回error
            if (JzbCheckParam.haveEmpty(param, new String[]{"list"})) {
                result = Response.getResponseError();
            } else {
                List<Map<String, Object>> list = (List<Map<String, Object>>) param.get("list");
                long time=System.currentTimeMillis();
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).put("adduid",userInfo.get("uid").toString());
                    list.get(i).put("addtime",time);
                    list.get(i).put("updtime",time);
                }
                result = tbCompanyMethodService.addCompanyMethod(list) > 0 ? Response.getResponseSuccess((Map<String, Object>) param.get("userinfo")) : Response.getResponseError();
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger("1.0", userInfo == null ? "" : userInfo.get("msgTag").toString(), "add Company Method", ex.toString()));
        }
        if (userInfo != null) {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
                    userInfo.get("msgTag").toString(), "User Login Message"));
        } else {
            logger.info(JzbLoggerUtil.getApiLogger( api, "2", "ERROR", "", "", "", "", "User Login Message"));
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
    public Response getMethodType(@RequestBody Map<String, Object> param) {
        Response response;
        try {
            if (JzbCheckParam.haveEmpty(param, new String[]{"cid"})) {
                response = Response.getResponseError();
            } else {

                //参照tbTempitemController
                List<Map<String, Object>> records = tbCompanyMethodService.queryCompanyMethod(param);

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
                    node.put("methodid", record.get("methodid").toString());
                    node.put("cname", record.get("cname").toString());
                    node.put("parentid", parentId);
                    node.put("addtime", JzbDateUtil.toDateString(JzbDataType.getLong(record.get("addtime")), JzbDateStr.yyyy_MM_dd));
                    node.put("days", record.get("days").toString());
                    node.put("remark", record.get("remark").toString());
                    node.put("plantime", JzbDateUtil.toDateString(JzbDataType.getLong(record.get("plantime")), JzbDateStr.yyyy_MM_dd));
                    node.put("children", new JSONArray());
                    if (JzbTools.isEmpty(record.get("score"))) {
                        node.put("score", null);
                    } else {
                        node.put("score", record.get("score").toString());
                    }
                    node.put("days", record.get("days").toString());

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
                // 定义返回结果
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                response = Response.getResponseSuccess(userInfo);
                response.setResponseEntity(result);
            }
        } catch (Exception e) {
            // 打印异常信息
            e.printStackTrace();
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
    @RequestMapping(value = "/updateCompanyMethodStatus", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response updateCompanyMethod(@RequestBody Map<String, Object> param) {
        Response response;
        try {
            // 如果指定参数为空则返回error
            if (JzbCheckParam.haveEmpty(param, new String[]{"cid", "projectid", "methodid"})) {
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
}
