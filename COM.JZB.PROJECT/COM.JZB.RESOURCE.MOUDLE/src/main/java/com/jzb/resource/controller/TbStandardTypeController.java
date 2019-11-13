package com.jzb.resource.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jzb.base.data.JzbDataType;
import com.jzb.base.data.date.JzbDateStr;
import com.jzb.base.data.date.JzbDateUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbTools;
import com.jzb.resource.service.TbStandardTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;



/**
 * libenqiu
 * 
 * 运营管理中的标准菜单分类
 */
@RestController
@RequestMapping(value = "/resource/standardType")
public class TbStandardTypeController {

    @Autowired
    private TbStandardTypeService tbStandardTypeService;


    /**
     * 标准分类中的新建
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/saveStandardType", method = RequestMethod.POST)
    @CrossOrigin
    public Response saveStandardType(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            //如果有空值，返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{ "cname"})) {
                result = Response.getResponseError();
            } else {
                //添加一条记录
                int count = tbStandardTypeService.saveStandardType(param);
                //如果返回值大于0，添加成功
                if (count > 0) {
                    //定义返回的结果
                    Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                    result = Response.getResponseSuccess(userInfo);
                } else {
                    //如果返回值小于等于0.添加失败
                    result = Response.getResponseError();
                }
            }
        } catch (Exception ex) {
            //错误信息
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 运营管理下菜单分类的修改
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/updateStandardType", method = RequestMethod.POST)
    @CrossOrigin
    public Response updateStandardType(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            //如果有空值，返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"typeid"})) {
                result = Response.getResponseError();
            } else {
                //修改一条记录
                int count = tbStandardTypeService.updateStandardType(param);
                //如果返回值大于0，修改成功
                if (count > 0) {
                    //定义返回的结果
                    Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                    result = Response.getResponseSuccess(userInfo);
                } else {
                    //如果返回值小于等于0.修改失败
                    result = Response.getResponseError();
                }
            }
        } catch (Exception ex) {
            //错误信息
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 设置删除状态
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/updateStatus", method = RequestMethod.POST)
    @CrossOrigin
    public Response updateStatus(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            //如果有空值，返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"typeid"})) {
                result = Response.getResponseError();
            } else {
                //修改一条记录
                int count = tbStandardTypeService.updateStatus(param);
                //如果返回值大于0，修改成功
                if (count > 0) {
                    //定义返回的结果
                    Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                    result = Response.getResponseSuccess(userInfo);
                } else {
                    //如果返回值小于等于0.修改失败
                    result = Response.getResponseError();
                }
            }
        } catch (Exception ex) {
            //错误信息
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 查询运营管理中的菜单类别
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/getStandardType",method = RequestMethod.POST)
    @CrossOrigin
    public Response getStandardType(@RequestBody(required = false) Map<String,Object> param) {
        Response response;
        try {
            //查询运营管理中的菜单类别
            List<Map<String, Object>> records = tbStandardTypeService.getStandardType(param);

            // Result JSON
            JSONArray result = new JSONArray();

            // record temp json
            JSONObject recordJson = new JSONObject();

            // Unknown json
            JSONObject unknownRecord = new JSONObject();

            // root id
            String firstParent = "0000000";

            for (int i = 0; i < records.size(); i++) {
                Map<String, Object> map = records.get(i);

                // if parentid is null.
                String parentId;
                if (map.get("parentid") == null) {

                    parentId = "0000000";
                } else {
                    parentId = map.get("parentid").toString();
                }
                // set default JSON and childern node
                JSONObject node = new JSONObject();
                node.put("typeid", map.get("typeid").toString());
                //node.put("typecode", map.get("typecode").toString());
                if (map.get("cname") != null) {
                    node.put("cname", map.get("cname").toString());
                }
               // node.put("typedesc", map.get("typedesc").toString());
               // node.put("adduid", map.get("adduid").toString());
                if (map.get("summary") != null) {
                    node.put("summary", map.get("summary").toString());
                }
                node.put("parentid", parentId);
                node.put("addtime", JzbDateUtil.toDateString(JzbDataType.getLong(map.get("addtime")), JzbDateStr.yyyy_MM_dd));
                node.put("children", new JSONArray());
                // if root node
                if (parentId.equals(firstParent)) {
                    result.add(node);
                    recordJson.put(map.get("typeid").toString(), node);

                }else if (recordJson.containsKey(parentId)) {
                    // add children
                    recordJson.getJSONObject(parentId).getJSONArray("children").add(node);
                    recordJson.put(map.get("typeid").toString(), node);
                    // Unknown relation node
                }else {
                    String nodeId = map.get("typeid").toString();
                    if (unknownRecord.containsKey(parentId)) {
                        // add children
                        unknownRecord.getJSONObject(parentId).getJSONArray("children").add(node);
                        recordJson.put(nodeId, node);
                    }else {
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

            // 设置返回响应结果
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            response = Response.getResponseSuccess(userInfo);

            // 设置返回pageinfo
            response.setResponseEntity(result);
        } catch (Exception ex) {
            //错误信息
            JzbTools.logError(ex);
            response = Response.getResponseError();
        }
        return response;

    }
}
