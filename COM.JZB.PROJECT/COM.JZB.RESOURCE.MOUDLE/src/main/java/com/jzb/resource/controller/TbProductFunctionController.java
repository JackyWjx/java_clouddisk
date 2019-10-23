package com.jzb.resource.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jzb.base.data.JzbDataType;
import com.jzb.base.data.date.JzbDateStr;
import com.jzb.base.data.date.JzbDateUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.resource.service.AdvertService;
import com.jzb.resource.service.TbProductFunctionService;
import com.jzb.resource.util.PageConvert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 合同配置中的产品功能
 */

@RestController
@RequestMapping(value = "/TbProductFunction")
public class TbProductFunctionController {
    @Autowired
    private TbProductFunctionService tbProductFunctionService;

    @Autowired
    private AdvertService advertService;
    /**
     * 查询产品功能表对应的资源产品
     * @param param
     * @return
     */
    @RequestMapping(value = "/getTbProductFunction", method = RequestMethod.POST)
    @CrossOrigin
    public Response getTbProductFunction(@RequestBody(required = false) Map<String, Object> param) {
        Response result;
        try {
            //判断分页参数是否为空
            if (JzbCheckParam.haveEmpty(param, new String[]{"pagesize", "pageno"})) {
                result = Response.getResponseError();
            } else {
                // 设置分页参数
               param = advertService.setPageSize(param);

                List<Map<String, Object>> list = tbProductFunctionService.getTbProductFunction(param);

                PageInfo pageInfo = new PageInfo();
                pageInfo.setList(list);
                //总数
                int count = tbProductFunctionService.getCount(param);
                pageInfo.setTotal(count);
                // 定义返回结果
                result = Response.getResponseSuccess();
                result.setPageInfo(pageInfo);
            }
        } catch (Exception ex) {
            //错误信息
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }


    /**
     * 添加产品功能
     * @param param
     * @return
     */
    @RequestMapping(value = "/saveTbProductFunction",method = RequestMethod.POST)
    @CrossOrigin
    public Response saveTbProductFunction(@RequestBody Map<String,Object> param) {
        Response result;
        try {
            //获取map中的list
            List<Map<String, Object>> paramList = (List) param.get("list");
            //循环设置创建时间和修改时间
            for (int i = 0; i < paramList.size(); i++) {
                long time = System.currentTimeMillis();
                paramList.get(i).put("addtime", time);
                paramList.get(i).put("updtime", time);
                paramList.get(i).put("funid", JzbRandom.getRandomCharCap(15));
            }
              int count = tbProductFunctionService.saveTbProductFunction(paramList);
              //如果返回值大于0，表示添加成功,否则就是添加失败
                if (count > 0) {
                    Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                    result = Response.getResponseSuccess(userInfo);
                } else {
                    result = Response.getResponseError();
                }
        } catch (Exception e) {
            //错误信息
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 修改产品功能
     * @param param
     * @return
     */
    @RequestMapping(value = "/updateTbProductFunction",method = RequestMethod.POST)
    @CrossOrigin
    public Response updateTbProductFunction(@RequestBody Map<String,Object> param) {
        Response result;
        try {
            //获取map中的list
            List<Map<String, Object>> paramList = (List) param.get("list");
            //循环设置创建时间和修改时间
            for (int i = 0; i < paramList.size(); i++) {
                long time = System.currentTimeMillis();
                paramList.get(i).put("updtime", time);
            }
            int count = tbProductFunctionService.updateTbProductFunction(paramList);
            //如果返回值大于0，表示添加成功,否则就是添加失败
            if (count > 0) {
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                result = Response.getResponseSuccess(userInfo);
            } else {
                result = Response.getResponseError();
            }
        } catch (Exception e) {
            //错误信息
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 点击修改时查询产品功能表中的数据
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getProductFunction", method = RequestMethod.POST)
    @CrossOrigin
    public Response getProductFunction(@RequestBody Map<String, Object> param) {
        Response response;
        try {
            List<Map<String, Object>> list = tbProductFunctionService.getProductFunction(param);

            // Result JSON
            List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();

            // record temp json
            JSONObject recordJson = new JSONObject();

            // Unknown json
            JSONObject unknownRecord = new JSONObject();

            // root id
            String firstParent = "000000000000000";

            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> map = list.get(i);

                // if parentid is null.
                String parentId;
                if (map.get("parentid") == null) {

                    parentId = "000000000000000";
                } else {
                    parentId = map.get("parentid").toString();
                }
                // set default JSON and childern node
                JSONObject node = new JSONObject();
                node.put("funid", map.get("funid").toString());
                node.put("cname", map.get("cname").toString());
                node.put("funtype", map.get("funtype").toString());
                node.put("parentid", parentId);
                node.put("addtime", JzbDateUtil.toDateString(JzbDataType.getLong(map.get("addtime")), JzbDateStr.yyyy_MM_dd));
                node.put("summary", map.get("summary").toString());
                node.put("children", new JSONArray());
                // if root node
                if (parentId.equals(firstParent)) {
                    result.add(node);
                    recordJson.put(map.get("funid").toString(), node);

                }else if (recordJson.containsKey(parentId)) {
                    // add children
                    recordJson.getJSONObject(parentId).getJSONArray("children").add(node);
                    recordJson.put(map.get("funid").toString(), node);
                    // Unknown relation node
                }else {
                    String nodeId = map.get("funid").toString();
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
                                recordJson.put(tempNode.get("funid").toString(), tempNode);
                                unknownRecord.remove(tempNode.get("funid").toString());
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
            //判断是pc端还是电脑端
            Map<String, Object> map = new HashMap<>();
            List list1 = new ArrayList<>();
            List list2 = new ArrayList<>();
            for (int i = 0; i < result.size(); i++) {
                if (result.get(i).get("funtype").equals("1")) {
                    list1.add(result.get(i));
                } else if (result.get(i).get("funtype").equals("2")){
                    list2.add(result.get(i));
                }
            }
            map.put("pc",list1);
            map.put("app",list2);
            response.setResponseEntity(map);


          //response.setResponseEntity(result);
        } catch (Exception e) {
            JzbTools.logError(e);
            response = Response.getResponseError();
        }

        return response;
    }
}
