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
import com.jzb.base.util.JzbTools;
import com.jzb.resource.service.AdvertService;
import com.jzb.resource.service.TbProductFunctionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 合同配置中的产品功能
 * @author libenqiu      czd--update
 */

@RestController
@RequestMapping(value = "/TbProductFunction")
public class TbProductFunctionController {
    @Autowired
    private TbProductFunctionService tbProductFunctionService;

    @Autowired
    private AdvertService advertService;

    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(TbContractTemplateController.class);


    /**
     * 查询产品功能表对应的资源产品
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getTbProductFunction", method = RequestMethod.POST)
    @CrossOrigin
    public Response getTbProductFunction(@RequestBody(required = false) Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/TbProductFunction/getTbProductFunction";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
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
            flag = false;
            //错误信息
            JzbTools.logError(ex);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getPolicyType Method", ex.toString()));
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
     * 添加产品功能
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/saveTbProductFunction", method = RequestMethod.POST)
    @CrossOrigin
    public Response saveTbProductFunction(@RequestBody(required = false) Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/TbProductFunction/saveTbProductFunction";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            //获取map中的list
            List<Map<String, Object>> paramList = (List) param.get("list");
            //循环设置创建时间和修改时间
            for (int i = 0; i < paramList.size(); i++) {
                long time = System.currentTimeMillis();
                paramList.get(i).put("addtime", time);
                paramList.get(i).put("updtime", time);
                //paramList.get(i).put("funid", JzbRandom.getRandomCharCap(15));
            }

            int count = tbProductFunctionService.saveTbProductFunction(paramList);
            //如果返回值大于0，表示添加成功,否则就是添加失败
            if (count > 0) {
                // Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                result = Response.getResponseSuccess();
            } else {
                result = Response.getResponseSuccess();
            }
        } catch (Exception e) {
            flag = false;
            //错误信息
            JzbTools.logError(e);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getPolicyType Method", e.toString()));
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
     * 修改产品功能
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/updateTbProductFunction", method = RequestMethod.POST)
    @CrossOrigin
    public Response updateTbProductFunction(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/TbProductFunction/updateTbProductFunction";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
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
                result = Response.getResponseSuccess(userInfo);
            } else {
                result = Response.getResponseError();
            }
        } catch (Exception e) {
            flag=false;
            //错误信息
            JzbTools.logError(e);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getPolicyType Method", e.toString()));
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
     * 点击修改时查询产品功能表中的数据
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getProductFunction", method = RequestMethod.POST)
    @CrossOrigin
    public Response getProductFunction(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/TbProductFunction/getProductFunction";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            List<Map<String, Object>> list = tbProductFunctionService.getProductFunction(param);

            // Result JSON
            List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

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
                node.put("funsubtype", map.get("funsubtype").toString());
                node.put("introduce", map.get("introduce").toString());
                node.put("pid", map.get("pid").toString());
                node.put("id", map.get("id").toString());
                node.put("parentid", parentId);
                node.put("addtime", JzbDateUtil.toDateString(JzbDataType.getLong(map.get("addtime")), JzbDateStr.yyyy_MM_dd));
                node.put("summary", map.get("summary").toString());
                node.put("children", new JSONArray());
                // if root node
                if (parentId.equals(firstParent)) {
                    result.add(node);
                    recordJson.put(map.get("funid").toString(), node);

                } else if (recordJson.containsKey(parentId)) {
                    // add children
                    recordJson.getJSONObject(parentId).getJSONArray("children").add(node);
                    recordJson.put(map.get("funid").toString(), node);
                    // Unknown relation node
                } else {
                    String nodeId = map.get("funid").toString();
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
            response = Response.getResponseSuccess(userInfo);
            //判断是pc端还是电脑端
            Map<String, Object> map = new HashMap<>();
            List list1 = new ArrayList<>();
            List list2 = new ArrayList<>();
            for (int i = 0; i < result.size(); i++) {
                if (result.get(i).get("funtype").equals("1")) {
                    list1.add(result.get(i));
                } else if (result.get(i).get("funtype").equals("2")) {
                    list2.add(result.get(i));
                }
            }
            map.put("pc", list1);
            map.put("app", list2);
            response.setResponseEntity(map);


            //response.setResponseEntity(result);
        } catch (Exception e) {
            flag=false;
            JzbTools.logError(e);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getPolicyType Method", e.toString()));
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
     * 产品功能pc端和移动端的删除
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/delProductFunction",method = RequestMethod.POST)
    @CrossOrigin
    public Response delProductFunction(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/TbProductFunction/delProductFunction";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            List<Map<String, Object>> list = tbProductFunctionService.getProductFunctions(param);
            //如果这个功能不存在，就说明前端还没有添加过来，那就在前端用js删除就可以了
            if (list == null || list.size() <= 0) {
                result = Response.getResponseSuccess();
                result.setResponseEntity("这个功能没有添加过来");
            } else {
                int count = tbProductFunctionService.updateProductFunctions(param);
                result = count > 0 ? Response.getResponseSuccess() : Response.getResponseError();
            }
        } catch (Exception e) {
            flag=false;
            //打印错误信息
            JzbTools.logError(e);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getPolicyType Method", e.toString()));
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
     * 删除产品功能
     * @author czd
     * @param param
     * @return
     */
    @RequestMapping(value = "/deleteProductFunction", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response deleteProductFunction(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/resource/contractTemplate/deleteProductFunction";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            if(JzbCheckParam.haveEmpty(param,new String[]{"pid"})){
                result=Response.getResponseError();
            }else {
                String[] pids = JzbDataType.getString(param.get("pid")).split(",");
                List<String> list = new ArrayList<>();
                for (int i = 0; i < pids.length; i++) {
                    list.add(pids[i]);
                }
                int count = list.size() > 0 ? tbProductFunctionService.deleteFunction(list) : 0;
                result = count > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
            }
        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "deleteProductFunction Method", ex.toString()));
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
