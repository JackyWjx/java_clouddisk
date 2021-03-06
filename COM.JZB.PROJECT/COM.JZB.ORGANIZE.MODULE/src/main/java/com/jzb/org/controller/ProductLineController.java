package com.jzb.org.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.deserializer.StringFieldDeserializer;
import com.jzb.base.constant.JzbStatusConstant;
import com.jzb.base.data.JzbDataType;
import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.org.api.open.OpenPageApi;
import com.jzb.org.api.redis.OrgRedisServiceApi;
import com.jzb.org.service.ProductLineService;

import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 业务控制层
 *
 * @author kuang Bin
 * @date 2019年8月7日
 */
@RestController
@RequestMapping("org")
public class ProductLineController {

    @Autowired
    private ProductLineService productLineService;

    /**
     * 查询redis缓存企业对象
     */
    @Autowired
    private OrgRedisServiceApi orgRedisServiceApi;

    /**
     * 查询Open开放平台应用列表
     */
    @Autowired
    private OpenPageApi openPageApi;


    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(ProductLineController.class);

    /**
     * CRM菜单管理-记支宝电脑端
     * 记支宝电脑端下新建产品线
     *
     * @author kuang Bin
     */
    @RequestMapping(value = "/addProductLine", method = RequestMethod.POST)
    @CrossOrigin
    public Response addProductLine(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("uid", JzbDataType.getString(userInfo.get("uid")));
            int count = productLineService.addProductLine(param);
            result = count == 1 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * CRM菜单管理-记支宝电脑端
     * 显示产品线列表
     *
     * @author kuang Bin
     */
    @RequestMapping(value = "/getProductLineList", method = RequestMethod.POST)
    @CrossOrigin
    public Response getProductLineList(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 产品线数据量不大不需要做分页
            List<Map<String, Object>> productLineList = productLineService.getProductLineList(param);
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(productLineList);
            result = Response.getResponseSuccess(userInfo);
            result.setPageInfo(pageInfo);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * CRM菜单管理-记支宝电脑端
     * 记支宝电脑端下修改,删除产品线
     *
     * @author kuang Bin
     */
    @RequestMapping(value = "/modifyProductLine", method = RequestMethod.POST)
    @CrossOrigin
    public Response modifyProductLine(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 获取用户信息
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("uid", JzbDataType.getString(userInfo.get("uid")));
            // 记支宝电脑端下修改,删除产品线
            int count = productLineService.modifyProductLine(param);
            result = count == 1 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * CRM菜单管理-记支宝电脑端
     * 电脑端-全界面-记支宝电脑端下全界面显示企业下所有产品的菜单列表
     *
     * @author kuang Bin
     */
    @RequestMapping(value = "/getCompanyMenuList", method = RequestMethod.POST)
    @CrossOrigin
    public Response getCompanyMenuList(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            List<Map<String, Object>> productMenuList = productLineService.getCompanyMenuList(param);

            List<Map<String, Object>> productPageList = productLineService.getCompanyPageList(param);
            // 设置树结构
            Response response = setTreeStructure(productPageList, productMenuList, param);
            //遍历集合拿到里面的值
            List<Map<String, Object>> list = response.getPageInfo().getList();

            //获取应用列表的菜单
            param.put("count", 0);
            param.put("value", "");
            //获取开放平台的应用
            Response response1 = openPageApi.searchOrgApplications(param);
            List<Map<String, Object>> pageInfo = response1.getPageInfo().getList();
            //循环遍历把有应用的菜单和页面放在应用下面
            for (int i = 0; i < list.size(); i++) {
                int count = 0;
                for (int j = 0; j < pageInfo.size(); j++) {
                    if (list.get(i).get("pid").equals(pageInfo.get(j).get("appid"))) {
                        if (pageInfo.get(j).get("children") == null) {
                            pageInfo.get(j).put("children", new ArrayList<Map<String, Object>>());
                            List<Map<String, Object>> children = (List<Map<String, Object>>) pageInfo.get(j).get("children");
                            children.add(list.get(i));
                            count++;
                        } else {
                            List<Map<String, Object>> children = (List<Map<String, Object>>) pageInfo.get(j).get("children");
                            children.add(list.get(i));
                        }

                    }
                }
                if (count == 0) {
                    pageInfo.add(list.get(i));
                }
            }
            List<Map<String, Object>> objects = new ArrayList<>();
            for (int i = 0; i < pageInfo.size(); i++) {
                if (pageInfo.get(i).get("children") == null && pageInfo.get(i).get("appid") != null) {
                    pageInfo.get(i).clear();
                }
                if (pageInfo.get(i) != null && pageInfo.get(i).size() > 0) {
                    objects.add(pageInfo.get(i));
                }
            }
            //获取全界面添加的应用
            Map<String, Object> map = new HashMap<>();
            ArrayList<Object> arrayList = new ArrayList<>();
            ArrayList<Object> arrayLists = new ArrayList<>();
            List<Map<String, Object>> getOrgApplicationList = productLineService.getOrgApplication(map);
            for (int i = 0; i < getOrgApplicationList.size(); i++) {
                getOrgApplicationList.get(i).put("cname", getOrgApplicationList.get(i).get("appname"));
                getOrgApplicationList.get(i).put("type", "3");
                for (int j = 0; j < objects.size(); j++) {
                    if (getOrgApplicationList.get(i).get("appid").equals(objects.get(j).get("pid"))) {
                        if (getOrgApplicationList.get(i).get("children") == null) {
                            getOrgApplicationList.get(i).put("children", new ArrayList<Map<String, Object>>());
                        }
                        List<Map<String, Object>> children = (List<Map<String, Object>>) getOrgApplicationList.get(i).get("children");
                        children.add(objects.get(j));
                        objects.get(j).put("idx", "0123");
                    }
                }
                arrayLists.add(getOrgApplicationList.get(i));
            }

            for (int i = 0; i < objects.size(); i++) {
                if (!(objects.get(i).get("idx") != null && objects.get(i).get("idx").equals("0123"))) {
                    arrayList.add(objects.get(i));
                }
            }
            for (int i = 0; i < arrayLists.size(); i++) {
                arrayList.add(arrayLists.get(i));
            }

            result = Response.getResponseSuccess();
            result.setResponseEntity(arrayList);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * CRM菜单管理-计支宝电脑端-全界面，应用的新增
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/addOrgApplication", method = RequestMethod.POST)
    @CrossOrigin
    public Response addOrgApplication(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/org/addOrgApplication";
        boolean flag = true;
        try {
            // 如果获取参数userinfo不为空的话
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            if (JzbCheckParam.haveEmpty(param, new String[]{"appname"})) {
                result = Response.getResponseError();
            } else {
                param.put("ouid", userInfo.get("uid"));
                param.put("cid", userInfo.get("cid"));
                int count = productLineService.addOrgApplication(param);
                //响应结果
                result = count > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
            }

        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "addOrgApplication Method", ex.toString()));
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
     * CRM菜单管理-记支宝电脑端
     * 电脑端-全界面-记支宝电脑端下全界面显示菜单下的所有页面
     *
     * @author kuang Bin
     */
    @RequestMapping(value = "/getProductPageList", method = RequestMethod.POST)
    @CrossOrigin
    public Response getProductPageList(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 查询菜单下的所有页面
            List<Map<String, Object>> productLineList = productLineService.getProductPageList(param);

            // 获取用户信息
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(productLineList);
            // 设置token
            result = Response.getResponseSuccess(userInfo);
            result.setPageInfo(pageInfo);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * CRM菜单管理-记支宝电脑端
     * 电脑端-全界面-记支宝电脑端下全界面新建同级页面或菜单
     *
     * @author kuang Bin
     */
    @RequestMapping(value = "/addProductPage", method = RequestMethod.POST)
    @CrossOrigin
    public Response addProductPage(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 获取用户信息
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("uid", JzbDataType.getString(userInfo.get("uid")));
            int maybe = JzbDataType.getInteger(param.get("maybe"));
            int count;
            // maybe为1代表新增菜单
            if (maybe == 1) {
                // 返回成功数
                count = productLineService.addProductMenu(param);
            } else {
                // 返回成功数
                count = productLineService.addProductPage(param);
            }
            if (count == 1) {
                // 判断缓存中是否存在产品数并删除
                comHasMenuTree(param);
                result = Response.getResponseSuccess();
            } else {
                result = Response.getResponseError();
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * CRM菜单管理-记支宝电脑端
     * 电脑端-全界面-记支宝电脑端下全界面新建同级页面或菜单
     *
     * @author kuang Bin
     */
    @RequestMapping(value = "/addProductPages", method = RequestMethod.POST)
    @CrossOrigin
    public Response addProductPages(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 获取用户信息
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("uid", JzbDataType.getString(userInfo.get("uid")));
            param.put("cid", userInfo.get("cid"));
            int maybe = JzbDataType.getInteger(param.get("maybe"));
            int count;
            // maybe为1代表新增菜单
            if (maybe == 1) {
                // 返回成功数
                count = productLineService.addProductMenus(param);
            } else {
                // 返回成功数
                count = productLineService.addProductPages(param);
            }
            if (count == 1) {
                // 判断缓存中是否存在产品数并删除
                comHasMenuTree(param);
                result = Response.getResponseSuccess();
            } else {
                result = Response.getResponseError();
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * CRM菜单管理-记支宝电脑端
     * 电脑端-全界面-记支宝电脑端下全界面修改页面或者删除页面
     *
     * @author kuang Bin
     */
    @RequestMapping(value = "/modifyProductPage", method = RequestMethod.POST)
    @CrossOrigin
    public Response modifyProductPage(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 获取用户信息
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("uid", JzbDataType.getString(userInfo.get("uid")));
            // 修改页面或者删除页面
            int count = productLineService.modifyProductPage(param);
            if (count != 0) {
                // 判断缓存中是否存在产品数并删除
                comHasMenuTree(param);
                result = Response.getResponseSuccess(userInfo);
            } else {
                result = Response.getResponseError();
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * CRM菜单管理-记支宝电脑端
     * 电脑端-全界面-记支宝电脑端下全界面根据页面名称模糊搜索页面
     *
     * @author kuang Bin
     */
    @RequestMapping(value = "/searchProductPage", method = RequestMethod.POST)
    @CrossOrigin
    public Response searchProductPage(@RequestBody Map<String, Object> param) {
        Response response;
        try {
            List<Map<String, Object>> menuList = productLineService.searchProductPage(param);
            List<JSONObject> resultList = new ArrayList<>();
            here:
            for (int i = menuList.size() - 1; i >= 0; i--) {
                // 获取查询到的页面
                Map<String, Object> menuMap = menuList.get(i);
                menuMap.put("type", "2");
                String mid = JzbDataType.getString(menuMap.get("mid"));
                if ("000000000000000".equals(mid)) {
                    JSONObject page = new JSONObject();
                    page.put("type", "2");
                    page.put("pid", JzbDataType.getString(menuMap.get("pid")));
                    page.put("pageid", JzbDataType.getString(menuMap.get("pageid")));
                    page.put("mid", JzbDataType.getString(menuMap.get("mid")));
                    page.put("pagecode", JzbDataType.getString(menuMap.get("pagecode")));
                    page.put("cname", JzbDataType.getString(menuMap.get("cname")));
                    page.put("icon", JzbDataType.getString(menuMap.get("icon")));
                    page.put("pagepath", JzbDataType.getString(menuMap.get("pagepath")));
                    page.put("summary", JzbDataType.getString(menuMap.get("summary")));
                    resultList.add(page);
                    menuList.remove(menuMap);
                    // 跳出此内循环至外循环重新开始循环
                    continue here;
                }
                // 遍历结果集中的数据
                for (int b = 0; b < resultList.size(); b++) {
                    JSONObject result = resultList.get(b);
                    // 如果有页面存在与同一级
                    if (mid.equals(result.getString("mid")) && !"000000000000000".equals(result.getString("mid"))) {
                        // 直接添加至此级的pageList中
                        result.getJSONArray("children").add(menuMap);
                        menuList.remove(menuMap);
                        // 跳出此内循环至外循环重新开始循环
                        continue here;
                    }
                }
                // 获取到页面中的菜单ID
                param.put("mid", mid);

                // 获取当前页面所属菜单级菜单下所有子集
                List<Map<String, Object>> pageList = productLineService.getProductMenuList(menuMap);

                // 记录临时json
                JSONObject recordJson = new JSONObject();

                // Unknown json
                JSONObject unknownRecord = new JSONObject();

                // 定义根目录ID
                String firstParent = "";

                // 定义层级,从1级开始
                int level = 1;
                for (int k = 0; k < pageList.size(); k++) {
                    Map<String, Object> record = pageList.get(k);
                    // 判断根目录是否存在
                    if (firstParent.equals("")) {
                        firstParent = JzbDataType.getString(record.get("parentid"));
                    }
                    String parentId;
                    if (JzbDataType.getString(record.get("parentid")) == null) {
                        // TODO
                        parentId = "000000000000000";
                    } else {
                        parentId = JzbDataType.getString(record.get("parentid"));
                    }
                    // 建立一个json数组
                    JSONObject node = new JSONObject();
                    node.put("children", new JSONArray());
                    node.put("parentid", parentId);
                    node.put("type", "1");
                    node.put("mid", JzbDataType.getString(record.get("mid")));
                    node.put("pid", JzbDataType.getString(record.get("pid")));
                    node.put("cname", JzbDataType.getString(record.get("cname")));
                    node.put("menupath", JzbDataType.getString(record.get("menupath")));
                    node.put("icon", JzbDataType.getString(record.get("icon")));
                    node.put("photo", JzbDataType.getString(record.get("photo")));
                    node.put("summary", JzbDataType.getString(record.get("summary")));
                    // 判断firstParent是否存在
                    if (parentId.equals(firstParent)) {
                        // 加入一级菜单到结果集
                        resultList.add(node);
                        // 加入当前层级
                        node.put("level", level);
                        if ("000000000000000".equals(mid)) {
                            JSONObject page = new JSONObject();
                            page.put("type", "2");
                            page.put("pid", JzbDataType.getString(menuMap.get("pid")));
                            page.put("pageid", JzbDataType.getString(menuMap.get("pageid")));
                            page.put("mid", JzbDataType.getString(menuMap.get("mid")));
                            page.put("pagecode", JzbDataType.getString(menuMap.get("pagecode")));
                            page.put("cname", JzbDataType.getString(menuMap.get("cname")));
                            page.put("icon", JzbDataType.getString(menuMap.get("icon")));
                            page.put("pagepath", JzbDataType.getString(menuMap.get("pagepath")));
                            page.put("summary", JzbDataType.getString(menuMap.get("summary")));
                            resultList.add(page);
                            menuList.remove(menuMap);
                        } else {
                            node.getJSONArray("children").add(menuMap);
                            menuList.remove(menuMap);
                        }
                        recordJson.put(JzbDataType.getString(record.get("mid")), node);
                    } else if (recordJson.containsKey(parentId)) {
                        // 如果此对象的父级不是根级,则添加在对象父id存在的node对象中的children中
                        recordJson.getJSONObject(parentId).getJSONArray("children").add(node);

                        // 加入父级children对象之后加入当前子级的层级
                        node.put("level", JzbDataType.getInteger(recordJson.getJSONObject(parentId).get("level")) + 1);
                        recordJson.put(JzbDataType.getString(record.get("mid")), node);
                    } else {
                        String nodeId = JzbDataType.getString(record.get("mid"));
                        if (unknownRecord.containsKey(parentId)) {
                            // add children
                            unknownRecord.getJSONObject(parentId).getJSONArray("children").add(node);

                            // 加入父级children对象之后加入当前子级的层级
                            node.put("level", JzbDataType.getInteger(unknownRecord.getJSONObject(parentId).get("level")) + 1);
                            recordJson.put(nodeId, node);
                        } else {
                            // 找到节点
                            for (Map.Entry<String, Object> entry : unknownRecord.entrySet()) {
                                JSONObject tempNode = (JSONObject) entry.getValue();
                                if (tempNode.getString("parentid").equals(nodeId)) {
                                    node.getJSONArray("children").add(tempNode);
                                    recordJson.put(JzbDataType.getString(tempNode.get("mid")), tempNode);
                                    unknownRecord.remove(JzbDataType.getString(tempNode.get("mid")));
                                    break;
                                }
                            }
                            unknownRecord.put(nodeId, node);
                        }
                    }
                }
                // 将unknownRecord添加到结果中
                // 找到节点
                for (Map.Entry<String, Object> entry : unknownRecord.entrySet()) {
                    JSONObject tempNode = (JSONObject) entry.getValue();
                    String tempNodeId = tempNode.getString("parentid");
                    if (recordJson.containsKey(tempNodeId)) {
                        // add children
                        recordJson.getJSONObject(tempNodeId).getJSONArray("children").add(tempNode);

                        // 加入父级的children对象之后加入当前子级的层级
                        tempNode.put("level", JzbDataType.getInteger(recordJson.getJSONObject(tempNodeId).get("level")) + 1);
                    }
                }
            }
            // 分页对象
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(resultList);
            // 获取用户信息
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            response = Response.getResponseSuccess(userInfo);
            response.setPageInfo(pageInfo);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            response = Response.getResponseError();
        }
        return response;
    }

    /**
     * CRM菜单管理
     * 点击crm菜单管理获取菜单信息
     *
     * @author kuang Bin
     */
    @RequestMapping(value = "/getProductMenuList", method = RequestMethod.POST)
    @CrossOrigin
    public Response getProductMenuList(@RequestBody Map<String, Object> param) {
        Response response;
        try {
            List<Map<String, Object>> productMenuList = productLineService.getProductMenuList(param);
            List<Map<String, Object>> productPageList = productLineService.getCompanyPageList(param);
            // 设置树结构
            response = setTreeStructures(productPageList, productMenuList, param);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            response = Response.getResponseError();
        }
        return response;
    }


    /**
     * CRM菜单管理
     * 点击crm菜单管理获取菜单信息
     *
     * @author kuang Bin
     */
    @RequestMapping(value = "/getProductMenuLists", method = RequestMethod.POST)
    @CrossOrigin
    public Response getProductMenuLists(@RequestBody Map<String, Object> param) {
        Response response;
        try {
            List<Map<String, Object>> productMenuList = productLineService.getProductMenuLists(param);
            List<Map<String, Object>> productPageList = productLineService.getCompanyPageLists(param);
            // 设置树结构
            response = setTreeStructures(productPageList, productMenuList, param);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            response = Response.getResponseError();
        }
        return response;
    }

    /**
     * CRM菜单管理
     * CRM菜单新建同级菜单或子级
     *
     * @author kuang Bin
     */
    @RequestMapping(value = "/addProductMenu", method = RequestMethod.POST)
    @CrossOrigin
    public Response addProductMenu(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 获取用户信息
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            int count = productLineService.addProductMenu(param);
            if (count == 1) {
                // 判断缓存中是否存在产品数并删除
                comHasMenuTree(param);
                result = Response.getResponseSuccess(userInfo);
            } else {
                result = Response.getResponseError();
            }
        } catch (
                Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * CRM菜单管理
     * CRM菜单修改菜单信息或删除菜单信息
     *
     * @author kuang Bin
     */
    @RequestMapping(value = "/modifyProductMenu", method = RequestMethod.POST)
    @CrossOrigin
    public Response modifyProductMenu(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 获取用户信息
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            int count = productLineService.modifyProductMenu(param);
            if (count > 0) {
                // 判断缓存中是否存在产品数并删除
                comHasMenuTree(param);
                result = Response.getResponseSuccess(userInfo);
            } else {
                result = Response.getResponseError();
            }
        } catch (
                Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }


    /**
     * 处理树结构,得到结果集
     *
     * @param list  要处理的树对象
     * @param param 顶级父级,map包含parentid
     * @return Response
     * @author kuang Bin
     */
    public Response setTreeStructures(List<Map<String, Object>> pageList, List<Map<String, Object>> list, Map<String, Object> param) {
        Response response;
        // 结果集 JSON
        JSONArray result = new JSONArray();

        // 记录临时json
        JSONObject recordJson = new JSONObject();

        // Unknown json
        JSONObject unknownRecord = new JSONObject();

        // 定义根目录ID
        String firstParent = JzbDataType.getString(param.get("parentid")).equals("") ?
                "000000000000000" : JzbDataType.getString(param.get("parentid"));

        // 定义层级,从1级开始
        int level = 1;
        for (int i = 0, l = list.size(); i < l; i++) {
            Map<String, Object> record = list.get(i);
            String parentId;
            if (JzbDataType.getString(record.get("parentid")) == null) {
                // TODO
                parentId = "000000000000000";
            } else {
                parentId = JzbDataType.getString(record.get("parentid"));
            }
            // 建立一个json数组
            JSONObject node = new JSONObject();
            node.put("children", new JSONArray());
            node.put("parentid", parentId);
            // 加入对象类型,1代表菜单,2代表页面
            node.put("type", "1");
            node.put("mid", JzbDataType.getString(record.get("mid")));
            node.put("pid", JzbDataType.getString(record.get("pid")));
            node.put("cname", JzbDataType.getString(record.get("cname")));
            node.put("menupath", JzbDataType.getString(record.get("menupath")));
            node.put("idx", JzbDataType.getString(record.get("idx")));
            node.put("icon", JzbDataType.getString(record.get("icon")));
            node.put("photo", JzbDataType.getString(record.get("photo")));
            node.put("summary", JzbDataType.getString(record.get("summary")));
            node.put("numIdx", JzbRandom.getRandomChar(7));
            if ("000000000000000".equals(parentId) && pageList.size() != 0) {
                for (int b = pageList.size() - 1; b >= 0; b--) {
                    Map<String, Object> productPage = pageList.get(b);
                    if (productPage.get("pid").equals(JzbDataType.getString(record.get("pid")))) {
                        // type为2代表是页面
                        productPage.put("type", "2");
                        result.add(productPage);
                        pageList.remove(b);
                    }
                }
            }
            // 查询每级菜单下的页面
            List<Map<String, Object>> productPageList = productLineService.getProductPageLists(record);
            for (int p = 0; p < productPageList.size(); p++) {
                Map<String, Object> productPageMap = productPageList.get(p);
                // type为2代表是页面
                productPageMap.put("type", "2");
                productPageMap.put("numIdx", JzbRandom.getRandomChar(7));
                // 将菜单下的页面加入children中并标记为2
                node.getJSONArray("children").add(productPageMap);
            }
            // 判断firstParent是否存在
            if (parentId.equals(firstParent)) {
                // 加入一级菜单到结果集
                result.add(node);

                // 加入当前层级
                node.put("level", level);
                recordJson.put(JzbDataType.getString(record.get("mid")), node);
            } else if (recordJson.containsKey(parentId)) {
                // 如果此对象的父级不是根级,则添加在对象父id存在的node对象中的children中
                recordJson.getJSONObject(parentId).getJSONArray("children").add(node);

                // 加入父级children对象之后加入当前子级的层级
                node.put("level", JzbDataType.getInteger(recordJson.getJSONObject(parentId).get("level")) + 1);
                recordJson.put(JzbDataType.getString(record.get("mid")), node);
            } else {
                String nodeId = JzbDataType.getString(record.get("mid"));
                if (unknownRecord.containsKey(parentId)) {
                    // add children
                    unknownRecord.getJSONObject(parentId).getJSONArray("children").add(node);

                    // 加入父级children对象之后加入当前子级的层级
                    node.put("level", JzbDataType.getInteger(unknownRecord.getJSONObject(parentId).get("level")) + 1);
                    recordJson.put(nodeId, node);
                } else {
                    // 找到节点
                    for (Map.Entry<String, Object> entry : unknownRecord.entrySet()) {
                        JSONObject tempNode = (JSONObject) entry.getValue();
                        if (tempNode.getString("parentid").equals(nodeId)) {
                            node.getJSONArray("children").add(tempNode);
                            recordJson.put(JzbDataType.getString(tempNode.get("mid")), tempNode);
                            unknownRecord.remove(JzbDataType.getString(tempNode.get("mid")));
                            break;
                        }
                    }
                    unknownRecord.put(nodeId, node);
                }
            }
        }
        // 将unknownRecord添加到结果中
        // 找到节点
        for (Map.Entry<String, Object> entry : unknownRecord.entrySet()) {
            JSONObject tempNode = (JSONObject) entry.getValue();
            String tempNodeId = tempNode.getString("parentid");
            if (recordJson.containsKey(tempNodeId)) {
                // add children
                recordJson.getJSONObject(tempNodeId).getJSONArray("children").add(tempNode);

                // 加入父级的children对象之后加入当前子级的层级
                tempNode.put("level", JzbDataType.getInteger(recordJson.getJSONObject(tempNodeId).get("level")) + 1);
            }
        }
        // 分页对象
        PageInfo pageInfo = new PageInfo();
        pageInfo.setList(result);
        // 获取用户信息
        Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
        response = Response.getResponseSuccess();
        response.setPageInfo(pageInfo);
        return response;
    }


    /**
     * 处理树结构,得到结果集
     *
     * @param list  要处理的树对象
     * @param param 顶级父级,map包含parentid
     * @return Response
     * @author kuang Bin
     */
    public Response setTreeStructure(List<Map<String, Object>> pageList, List<Map<String, Object>> list, Map<String, Object> param) {
        Response response;
        // 结果集 JSON
        JSONArray result = new JSONArray();

        // 记录临时json
        JSONObject recordJson = new JSONObject();

        // Unknown json
        JSONObject unknownRecord = new JSONObject();

        // 定义根目录ID
        String firstParent = JzbDataType.getString(param.get("parentid")).equals("") ?
                "000000000000000" : JzbDataType.getString(param.get("parentid"));

        // 定义层级,从1级开始
        int level = 1;
        for (int i = 0, l = list.size(); i < l; i++) {
            Map<String, Object> record = list.get(i);
            String parentId;
            if (JzbDataType.getString(record.get("parentid")) == null) {
                // TODO
                parentId = "000000000000000";
            } else {
                parentId = JzbDataType.getString(record.get("parentid"));
            }
            // 建立一个json数组
            JSONObject node = new JSONObject();
            node.put("children", new JSONArray());
            node.put("parentid", parentId);
            // 加入对象类型,1代表菜单,2代表页面
            node.put("type", "1");
            node.put("mid", JzbDataType.getString(record.get("mid")));
            node.put("pid", JzbDataType.getString(record.get("pid")));
            node.put("cname", JzbDataType.getString(record.get("cname")));
            node.put("menupath", JzbDataType.getString(record.get("menupath")));
            node.put("idx", JzbDataType.getString(record.get("idx")));
            node.put("icon", JzbDataType.getString(record.get("icon")));
            node.put("photo", JzbDataType.getString(record.get("photo")));
            node.put("summary", JzbDataType.getString(record.get("summary")));
            if ("000000000000000".equals(parentId) && pageList.size() != 0) {
                for (int b = pageList.size() - 1; b >= 0; b--) {
                    Map<String, Object> productPage = pageList.get(b);
                    if (productPage.get("pid").equals(JzbDataType.getString(record.get("pid")))) {
                        // type为2代表是页面
                        productPage.put("type", "2");
                        result.add(productPage);
                        pageList.remove(b);
                    }
                }
            }
            // 查询每级菜单下的页面
            List<Map<String, Object>> productPageList = productLineService.getProductPageList(record);
            for (int p = 0; p < productPageList.size(); p++) {
                Map<String, Object> productPageMap = productPageList.get(p);
                // type为2代表是页面
                productPageMap.put("type", "2");

                // 将菜单下的页面加入children中并标记为2
                node.getJSONArray("children").add(productPageMap);
            }
            // 判断firstParent是否存在
            if (parentId.equals(firstParent)) {
                // 加入一级菜单到结果集
                result.add(node);

                // 加入当前层级
                node.put("level", level);
                recordJson.put(JzbDataType.getString(record.get("mid")), node);
            } else if (recordJson.containsKey(parentId)) {
                // 如果此对象的父级不是根级,则添加在对象父id存在的node对象中的children中
                recordJson.getJSONObject(parentId).getJSONArray("children").add(node);

                // 加入父级children对象之后加入当前子级的层级
                node.put("level", JzbDataType.getInteger(recordJson.getJSONObject(parentId).get("level")) + 1);
                recordJson.put(JzbDataType.getString(record.get("mid")), node);
            } else {
                String nodeId = JzbDataType.getString(record.get("mid"));
                if (unknownRecord.containsKey(parentId)) {
                    // add children
                    unknownRecord.getJSONObject(parentId).getJSONArray("children").add(node);

                    // 加入父级children对象之后加入当前子级的层级
                    node.put("level", JzbDataType.getInteger(unknownRecord.getJSONObject(parentId).get("level")) + 1);
                    recordJson.put(nodeId, node);
                } else {
                    // 找到节点
                    for (Map.Entry<String, Object> entry : unknownRecord.entrySet()) {
                        JSONObject tempNode = (JSONObject) entry.getValue();
                        if (tempNode.getString("parentid").equals(nodeId)) {
                            node.getJSONArray("children").add(tempNode);
                            recordJson.put(JzbDataType.getString(tempNode.get("mid")), tempNode);
                            unknownRecord.remove(JzbDataType.getString(tempNode.get("mid")));
                            break;
                        }
                    }
                    unknownRecord.put(nodeId, node);
                }
            }
        }
        // 将unknownRecord添加到结果中
        // 找到节点
        for (Map.Entry<String, Object> entry : unknownRecord.entrySet()) {
            JSONObject tempNode = (JSONObject) entry.getValue();
            String tempNodeId = tempNode.getString("parentid");
            if (recordJson.containsKey(tempNodeId)) {
                // add children
                recordJson.getJSONObject(tempNodeId).getJSONArray("children").add(tempNode);

                // 加入父级的children对象之后加入当前子级的层级
                tempNode.put("level", JzbDataType.getInteger(recordJson.getJSONObject(tempNodeId).get("level")) + 1);
            }
        }
        // 分页对象
        PageInfo pageInfo = new PageInfo();
        pageInfo.setList(result);
        // 获取用户信息
        Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
        response = Response.getResponseSuccess(userInfo);
        response.setPageInfo(pageInfo);
        return response;
    }

    /**
     * CRM菜单管理-记支宝电脑端-子产品
     * 电脑端-子产品-记支宝电脑端下产品线下显示所有产品
     *
     * @author kuang Bin
     */
    @RequestMapping(value = "/getProductList", method = RequestMethod.POST)
    @CrossOrigin
    public Response getProductList(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 每条产品线下产品数据量不大不需要做分页
            List<Map<String, Object>> productList = productLineService.getProductList(param);
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(productList);
            // 获取用户信息
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            result = Response.getResponseSuccess(userInfo);
            result.setPageInfo(pageInfo);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * CRM菜单管理-记支宝电脑端-子产品
     * 电脑端-子产品-产品线下新建企业产品
     *
     * @author kuang Bin
     */
    @RequestMapping(value = "/addCompanyProduct", method = RequestMethod.POST)
    @CrossOrigin
    public Response addCompanyProduct(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 获取用户信息
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("uid", JzbDataType.getString(userInfo.get("uid")));
            int count = productLineService.addCompanyProduct(param);
            result = count >= 1 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * CRM菜单管理-记支宝电脑端-子产品
     * 电脑端-子产品-产品线下修改企业产品或删除企业产品
     *
     * @author kuang Bin
     */
    @RequestMapping(value = "/modifyCompanyProduct", method = RequestMethod.POST)
    @CrossOrigin
    public Response modifyCompanyProduct(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            int count = productLineService.modifyCompanyProduct(param);
            // 获取用户信息
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            result = count == 1 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * CRM菜单管理-记支宝电脑端-子产品
     * 电脑端-子产品-点击从页面添加按钮,将页面加入当前菜单中
     *
     * @author kuang Bin
     */
    @RequestMapping(value = "/addExistingPage", method = RequestMethod.POST)
    @CrossOrigin
    public Response addExistingPage(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            List<Map<String, Object>> list = (List<Map<String, Object>>) param.get("list");
            int count = productLineService.addExistingPage(list, param);
            if (count > 0) {
                // 判断缓存中是否存在产品数并删除
                comHasMenuTree(param);
                // 获取用户信息
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                result = Response.getResponseSuccess(userInfo);
            } else {
                result = Response.getResponseError();
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 根据产品ID判断是否存在菜单树
     *
     * @author kuang Bin
     */
    public boolean comHasMenuTree(@RequestBody Map<String, Object> param) {
        boolean bl;
        try {
            // 判断缓存中是否存在对应的key
            Response key = orgRedisServiceApi.comHasMenuTree(param);
            Object obj = key.getResponseEntity();
            if (JzbDataType.isBoolean(obj)) {
                bl = (boolean) obj;
                if (bl) {
                    // 删除菜单树
                    orgRedisServiceApi.removeProductTree(param);
                }
            } else {
                bl = false;
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            bl = false;
        }
        return bl;
    }

    /**
     * CRM菜单管理-记支宝电脑端
     * 电脑端-全界面-记支宝电脑端下全界面新建控件
     *
     * @author kuang Bin
     */
    @RequestMapping(value = "/addPageControl", method = RequestMethod.POST)
    @CrossOrigin
    public Response addPageControl(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 获取用户信息
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("uid", JzbDataType.getString(userInfo.get("uid")));
            // 返回成功数
            int count = productLineService.addPageControl(param);
            if (count == 1) {
                // 判断缓存中是否存在产品数并删除
                comHasMenuTree(param);
                result = Response.getResponseSuccess();
            } else {
                result = Response.getResponseError();
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }
}
