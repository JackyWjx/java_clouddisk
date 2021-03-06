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
import com.jzb.resource.service.StandardService;
import com.jzb.resource.util.PageConvert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description: 标准接口控制器与前台对接
 * @Author duanfeiyu
 * @Version v1.0
 * @Since 1.0
 * @Date: 2019/8/16 18:10
 */
@RestController
@RequestMapping("/standardDocument")
public class StandardController {

    @Autowired
    private StandardService standardService;

    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(StandardController.class);

    /**
     * 标准查询,根据父类查子类
     *
     * @return
     */
    @RequestMapping(value = "/getFatherOne", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response queryFatherOne(@RequestBody(required = false) Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/standardDocument/getFatherOne";
        boolean flag = true;
        try {

            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }

            List<Map<String, Object>> records = standardService.queryFatherOne();

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

                // 设置parentId默认值
                String parentId = null;
                if (record.get("parentid") == null) {
                    // TODO
                    parentId = "0000000";
                } else {
                    parentId = record.get("parentid").toString();
                }

                // 建立一个json数组
                JSONObject node = new JSONObject();
                node.put("typeid", record.get("typeid").toString());
                node.put("cname", record.get("cname").toString());
                node.put("parentid", parentId);
                node.put("children", new JSONArray());

                // 判断firstParent是否存在
                if (parentId.equals(firstParent)) {
                    result.add(node);
                    recordJson.put(record.get("typeid").toString(), node);

                    // 判断是否存在
                } else if (recordJson.containsKey(parentId)) {
                    // 添加在 children
                    recordJson.getJSONObject(parentId).getJSONArray("children").add(node);
                    recordJson.put(record.get("typeid").toString(), node);
                    // 不存在
                } else {
                    String nodeId = record.get("typeid").toString();
                    if (unknownRecord.containsKey(parentId)) {
                        // 添加 children
                        unknownRecord.getJSONObject(parentId).getJSONArray("children").add(node);
                        recordJson.put(nodeId, node);
                    } else {
                        // 遍历unknownRecord
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
            // 定义返回结果
            response = Response.getResponseSuccess();
            response.setResponseEntity(result);
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "queryFatherOne Method", e.toString()));
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
     * 标准管理查询
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/getDocumentsList", method = RequestMethod.POST)
    @CrossOrigin
    public Response queryDocumentsList(@RequestBody Map<String, Object> params) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/standardDocument/getDocumentsList";
        boolean flag = true;
        try {
            if (params.get("userinfo") != null) {
                userInfo = (Map<String, Object>) params.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }

            // 判断如果参数为空则返回404
            if (JzbCheckParam.haveEmpty(params, new String[]{"pageno", "pagesize", "typeid"})) {
                result = Response.getResponseError();
                logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "queryDocumentsList Method", "[param error] or [param is null]"));

            } else {
                List<Map<String, Object>> list;
                int count;
                // 为1 的时候返回空list
                if (params.get("typeid").toString().equals("1")) {
                    list = new ArrayList<>();
                    count = 0;
                } else {
                    // 设置分页参数
                    PageConvert.setPageRows(params);

                    // 结果集
                    list = standardService.queryDocumentsList(params);

                    // 遍历转换格式
                    for (int i = 0, l = list.size(); i < l; i++) {
                        list.get(i).put("addtime", JzbDateUtil.toDateString(JzbDataType.getLong(list.get(i).get("addtime")), JzbDateStr.yyyy_MM_dd));
                        list.get(i).put("effective", JzbDateUtil.toDateString(JzbDataType.getLong(list.get(i).get("effective")), JzbDateStr.yyyy_MM_dd));
                    }
                    // 查询总数
                    count = standardService.queryDocumentsCount(params);
                }

                // 定义pageinfo
                PageInfo pi = new PageInfo();
                pi.setList(list);
                pi.setTotal(count);

                // 定义返回结果
                result = Response.getResponseSuccess();
                result.setPageInfo(pi);
            }
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "queryDocumentsList Method", e.toString()));
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
     * 获取文档详情
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/getStandartDocumentDesc", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response getStandartDocumentDesc(@RequestBody Map<String, Object> params) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/standardDocument/getStandartDocumentDesc";
        boolean flag = true;
        try {
            if (params.get("userinfo") != null) {
                userInfo = (Map<String, Object>) params.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }

            // 判断如果参数为空则返回404
            if (JzbCheckParam.haveEmpty(params, new String[]{"domid"})) {
                result = Response.getResponseError();
                logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getStandartDocumentDesc Method", "[param error] or [param is null]"));
            } else {

                // 结果集
                List<Map<String, Object>> list = standardService.getStandardDocumentDesc(params);
                for (int i = 0, l = list.size(); i < l; i++) {
                    list.get(i).put("addtime", JzbDateUtil.toDateString(JzbDataType.getLong(list.get(i).get("addtime")), JzbDateStr.yyyy_MM_dd));
                }
                // 定义返回pageinfo
                PageInfo pi = new PageInfo();
                pi.setList(list);

                // 定义返回result
                result = Response.getResponseSuccess();
                result.setPageInfo(pi);

            }
        } catch (Exception e) {
            flag = false;
            // 打印异常信息
            e.printStackTrace();
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getStandartDocumentDesc Method", e.toString()));
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
     * 标准文档热门榜
     *
     * @param param
     * @return
     * @author chenzhengduan
     */
    @RequestMapping(value = "/getStandartHotDom", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response getSolutionHotDom(@RequestBody(required = false) Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/standardDocument/getStandartHotDom";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }

            if (JzbCheckParam.haveEmpty(param, new String[]{"count"})) {
                result = Response.getResponseError();
                logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getSolutionHotDom Method", "[param error] or [param is null]"));
            } else {
                // 查询结果
                List<Map<String, Object>> list = standardService.queryHotDom(param);
                PageInfo pi = new PageInfo();
                pi.setList(list);
                // 定义返回result
                result = Response.getResponseSuccess();
                result.setPageInfo(pi);
            }
        } catch (Exception e) {
            flag = false;
            // 打印异常信息
            e.printStackTrace();
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getSolutionHotDom Method", e.toString()));
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