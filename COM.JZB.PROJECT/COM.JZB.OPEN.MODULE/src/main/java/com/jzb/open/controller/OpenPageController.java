package com.jzb.open.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbTools;
import com.jzb.open.service.OpenPageService;
import com.sun.org.apache.xpath.internal.objects.XString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Spliterator;

/**
 * @Description:
 * @Author dell
 * @Version 1.0
 * @Since 1.0
 * @Date: 2019/11/21 14:51
 */
@RestController
@RequestMapping("open/page")
public class OpenPageController {
    @Autowired
    private OpenPageService openPageService;

    /**
     * 模糊查询开发者应用表
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     */
    @RequestMapping(value = "/searchOrgApplication", method = RequestMethod.POST)
    @CrossOrigin
    public Response searchOrgApplication(@RequestBody Map<String, Object> param) {
        Response result;
        PageInfo Info;
        try {
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            int rows = JzbDataType.getInteger(param.get("pagesize"));
            int page = JzbDataType.getInteger(param.get("pageno"));
            if (page > 0 && rows > 0) {
                param.put("start", rows * (page - 1));
                param.put("pagesize", rows);
                List<Map<String, Object>> orgAppList = openPageService.searchOrgApplication(param);
                result = Response.getResponseSuccess(userInfo);
                Info = new PageInfo();
                Info.setList(orgAppList);
                int count = JzbDataType.getInteger(param.get("count"));
                if (count == 0) {
                    int size = openPageService.searchOrgApplicationCount(param);
                    Info.setTotal(size > 0 ? size : orgAppList.size());
                }
                result.setPageInfo(Info);
            } else {
                result = Response.getResponseError();
            }
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 应用列表的修改和删除
     * @param param
     * @return
     */
    @RequestMapping(value = "/updateOrgApplication", method = RequestMethod.POST)
    @CrossOrigin
    public Response updateOrgApplication(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            //如果指定参数为空，则返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"appid"})) {
                result = Response.getResponseError();
            } else {
                //进行数据的修改
                int count = openPageService.updateOrgApplication(param);
                //int count = 1;
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                //返回响应成功信息
                result = count > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
            }
        } catch (Exception e) {
            //打印错误信息
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 新增应用菜单表
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     */
    @RequestMapping(value = "/addApplicationMenu", method = RequestMethod.POST)
    @CrossOrigin
    public Response addApplicationMenu(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String[] str = {"appid"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                param.put("uid", userInfo.get("uid"));
                String key = "parentid";
                if (JzbTools.isEmpty(param.get(key))) {
                    param.put(key, "000000000000000");
                }
                int add = openPageService.insertApplicationMenu(param);
                result = add > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
            } else {
                result = Response.getResponseError();
            }
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 应用页面表新增
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     */
    @RequestMapping(value = "/addApplicationPage", method = RequestMethod.POST)
    @CrossOrigin
    public Response addApplicationPage(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String[] str = {"appid", "mid", "cname", "pagepath"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                param.put("uid", userInfo.get("uid"));
                int add = openPageService.insertApplicationPage(param);
                result = add > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
            } else {
                result = Response.getResponseError();
            }
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 查询子菜单和页面
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     */
    @RequestMapping(value = "/getApplicationMenu", method = RequestMethod.POST)
    @CrossOrigin
    public Response getApplicationMenu(@RequestBody Map<String, Object> param) {
        Response result;
        PageInfo Info;
        try {
            String[] str = {"appid", "parentid"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                param.put("start", 0);
                param.put("pagesize", 2147483647);
                List<Map<String, Object>> menuList = openPageService.getApplicationMenuPage(param);
                result = Response.getResponseSuccess(userInfo);
                result.setResponseEntity(menuList);
            } else {
                result = Response.getResponseError();
            }
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 应用菜单的查询
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/serachApplicationMenu", method = RequestMethod.POST)
    @CrossOrigin
    public Response serachApplicationMenu(@RequestBody Map<String, Object> param) {
        Response response;
        try {
            //如果指定参数为空返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"appid"})) {
                response = Response.getResponseError();
            } else {
                //查询结果
                List<Map<String, Object>> records = openPageService.serachApplicationMenu(param);
                // Result JSON
                JSONArray result = new JSONArray();

                // record temp json
                JSONObject recordJson = new JSONObject();

                // Unknown json
                JSONObject unknownRecord = new JSONObject();

                // root id
                String firstParent = "000000000000000";

                for (int i = 0; i < records.size(); i++) {
                    Map<String, Object> map = records.get(i);

                    // if parentid is null.
                    String parentId;
                    if (map.get("parentid") == null) {

                        parentId = "000000000000000";
                    } else {
                        parentId = map.get("parentid").toString();
                    }
                    // set default JSON and childern node
                    JSONObject node = new JSONObject();
                    node.put("mid", map.get("mid").toString());
                    //node.put("typecode", map.get("typecode").toString());
                    if (map.get("cname") != null) {
                        node.put("cname", map.get("cname").toString());
                    }
                    // node.put("typedesc", map.get("typedesc").toString());
                    // node.put("adduid", map.get("adduid").toString());
                    if (map.get("appid") != null) {
                        node.put("appid", map.get("appid").toString());
                    }
                    node.put("parentid", parentId);
                    if (map.get("menupath") != null) {
                        node.put("menupath", map.get("menupath").toString());
                    }
                    node.put("statuss", map.get("statuss").toString());
                    node.put("parentid", map.get("parentid").toString());
                    node.put("children", new JSONArray());
                    //拿到菜单id去查询页面对应的菜单
                    Map<String, Object> hashMap = new HashMap<>();
                    hashMap.put("mid", map.get("mid").toString());
                    List<Map<String,Object>> list = openPageService.getApplicationPage(hashMap);
                    // if root node
                    if (parentId.equals(firstParent)) {
                        result.add(node);
                        recordJson.put(map.get("mid").toString(), node);
                        //把页面放到菜单下面
                        for (int j = 0; j <list.size() ; j++) {
                            List<Map<String,Object>> children = (List<Map<String, Object>>) node.get("children");
                            children.add(list.get(j));
                        }

                    } else if (recordJson.containsKey(parentId)) {
                        // add children
                        recordJson.getJSONObject(parentId).getJSONArray("children").add(node);
                        for (int j = 0; j <list.size() ; j++) {
                            List<Map<String,Object>> children = (List<Map<String, Object>>) node.get("children");
                            children.add(list.get(j));
                        }
                        recordJson.put(map.get("mid").toString(), node);
                        // Unknown relation node
                    } else {
                        String nodeId = map.get("mid").toString();
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
                                    recordJson.put(tempNode.get("mid").toString(), tempNode);
                                    unknownRecord.remove(tempNode.get("mid").toString());
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
                response = Response.getResponseSuccess();

                // 设置返回pageinfo
                response.setResponseEntity(result);
            }
        } catch (Exception e) {
            JzbTools.logError(e);
            response = Response.getResponseError();
        }
        return response;
    }

    /**
     * 修改菜单或者页面
     * @param param
     * @return
     */
    @RequestMapping(value = "/updateMenuPage",method = RequestMethod.POST)
    @CrossOrigin
    public Response updateMenuPage(@RequestBody Map<String,Object> param) {
        Response result;
        try {
            //如果指定参数为空，则返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"status"})) {
                result = Response.getResponseError();
            } else {
                //根据前端传过来的状态判断是菜单还是页面
                String status = (String) param.get("status");
                int count;
                if (status.equals("1")) {
                    //如果状态是1，则代表是菜单
                    param.put("status", "1");
                    count = openPageService.updateMenu(param);
                } else {
                    //否则就是页面
                    param.put("status", "1");
                    count = openPageService.updatePage(param);
                }
                //获取用户信息
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                //返回响应结果
                result = count > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
            }
        } catch (Exception e) {
            //打印错误信息
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }
}
