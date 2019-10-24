package com.jzb.operate.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.data.date.JzbDateStr;
import com.jzb.base.data.date.JzbDateUtil;
import com.jzb.base.message.Response;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbTools;
import com.jzb.operate.service.TbCompanyMethodService;
import com.netflix.discovery.converters.jackson.EurekaXmlJacksonCodec;
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
        try {
            // 如果指定参数为空则返回error
            if(JzbCheckParam.haveEmpty(param,new String[]{"cid","projectid","methodid"})){
                result=Response.getResponseError();
            }else {
                result = tbCompanyMethodService.addCompanyMethod((List<Map<String, Object>>) param.get("list")) > 0 ? Response.getResponseSuccess((Map<String, Object>) param.get("userinfo")) : Response.getResponseError();
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
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
                node.put("typeid", record.get("typeid").toString());
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
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            response = Response.getResponseSuccess(userInfo);
            response.setResponseEntity(result);
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