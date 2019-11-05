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
import com.jzb.resource.service.TbMethodTypeService;
import com.jzb.resource.util.PageConvert;
import org.jsoup.select.Evaluator;
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
        try {
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
                node.put("typeid", record.get("typeid").toString());
                node.put("cname", record.get("cname").toString());
                node.put("parentid", parentId);
                node.put("addtime", JzbDateUtil.toDateString(JzbDataType.getLong(record.get("addtime")), JzbDateStr.yyyy_MM_dd));
                node.put("typedesc", record.get("typedesc").toString());
                node.put("children", new JSONArray());
                if(JzbTools.isEmpty(record.get("score"))){
                    node.put("score", null);
                }else {
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
        }catch (Exception e){
            // 打印异常信息
            e.printStackTrace();
            response=Response.getResponseError();
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
        try {
            // 设置分页参数
            PageConvert.setPageRows(param);
            //参照tbTempitemController
            List<Map<String, Object>> list = tbMethodTypeService.queryMethodTypePage(param);

            // 遍历转换时间格式
            for (int i = 0, l = list.size(); i < l; i++) {
                list.get(i).put("addtime", JzbDateUtil.toDateString(JzbDataType.getLong(list.get(i).get("addtime")), JzbDateStr.yyyy_MM_dd));
            }
            // 查询分页数据--总数
            int count=tbMethodTypeService.queryMethodTypePageCount();

            // 定义pageinfo
            PageInfo pi=new PageInfo();
            pi.setList(list);
            pi.setTotal(count);

            // 定义返回结果
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            response = Response.getResponseSuccess(userInfo);
            response.setPageInfo(pi);
        }catch (Exception e){
            // 打印异常信息
            e.printStackTrace();
            response=Response.getResponseError();
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
        try {
            // 设置分页参数
            PageConvert.setPageRows(param);
            //参照tbTempitemController
            List<Map<String, Object>> list = tbMethodTypeService.queryMethodTypePageSon(param);
            for (int i = 0, l = list.size(); i < l; i++) {
                list.get(i).put("addtime", JzbDateUtil.toDateString(JzbDataType.getLong(list.get(i).get("addtime")), JzbDateStr.yyyy_MM_dd));
            }
            // 定义pageinfo
            PageInfo pi=new PageInfo();
            pi.setList(list);
            pi.setTotal(list.size());

            // 定义返回结果
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            response = Response.getResponseSuccess(userInfo);
            response.setPageInfo(pi);
        }catch (Exception e){
            // 打印异常信息
            e.printStackTrace();
            response=Response.getResponseError();
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
        try {
            // 验证指定值为空则返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"cname", "ouid"})) {
                result = Response.getResponseError();
            } else {
                // 添加返回值大于0 则添加成功
                int count = tbMethodTypeService.saveMethodTypeBrother(param);
                if (count > 0) {
                    // 定义返回结果
                    Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                    result = Response.getResponseSuccess(userInfo);
                } else {
                    result = Response.getResponseError();
                }
            }
        } catch (Exception e) {
            // 打印异常信息
            e.printStackTrace();
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * @param param
     * @return
     * @deprecated 添加方法论子级
     */
    @RequestMapping(value = "/addMethodTypeSon", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response addMethodTypeSon(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 验证指定值为空则返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"cname", "ouid", "parentid"})) {
                result = Response.getResponseError();
            } else {
                // 添加返回值大于0 则添加成功
                int count = tbMethodTypeService.saveMethodTypeSon(param);
                if (count > 0) {
                    // 定义返回结果
                    Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                    result = Response.getResponseSuccess(userInfo);
                } else {
                    result = Response.getResponseError();
                }
            }
        } catch (Exception e) {
            // 打印异常信息
            e.printStackTrace();
            result = Response.getResponseError();
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
        try {
            // 验证指定值为空则返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"cname", "ouid", "typeid",})) {
                result = Response.getResponseError();
            } else {
                // 添加返回值大于0 则添加成功
                int count = tbMethodTypeService.updateMethodType(param);
                if (count > 0) {
                    // 定义返回结果
                    Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                    result = Response.getResponseSuccess(userInfo);
                } else {
                    result = Response.getResponseError();
                }
            }
        } catch (Exception e) {
            // 打印异常信息
            e.printStackTrace();
            result = Response.getResponseError();
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
        try {
            // 查询结果
            List<Map<String, Object>> list = tbMethodTypeService.getMethodLevel(param);
            // 定义返回结果
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            result = Response.getResponseSuccess(userInfo);
            PageInfo pi = new PageInfo<>();
            pi.setList(list);
            result.setPageInfo(pi);
        } catch (Exception e) {
            // 打印异常信息
            e.printStackTrace();
            result = Response.getResponseError();
        }
        return result;
    }

}
