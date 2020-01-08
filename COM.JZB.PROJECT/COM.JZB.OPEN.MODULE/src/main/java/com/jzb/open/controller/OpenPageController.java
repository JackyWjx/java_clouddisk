package com.jzb.open.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.jzb.base.data.code.JzbDataCheck;
import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.open.api.auth.UserAuthApi;
import com.jzb.open.api.org.CompanyApi;
import com.jzb.open.api.redis.UserRedisServiceApi;
import com.jzb.open.service.OpenPageService;
import jdk.nashorn.internal.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

/**
 * @Description:
 * @Author dell
 * @Version 1.0
 * @Since 1.0
 * @Date: 2019/11/21 14:51
 */
@RestController
@RequestMapping("/open/page")
public class OpenPageController {
    @Autowired
    private OpenPageService openPageService;


    @Autowired
    private UserRedisServiceApi userRedisServiceApi;

    @Autowired
    private CompanyApi companyApi;

    @Autowired
    private UserAuthApi userAuthApi;
    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(OpenPageController.class);

    /**
     * 根据checkcode和appkey，appsecret 数据库中进行对比，如果相同则返回给前端用信息
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getOrgApplication", method = RequestMethod.POST)
    @CrossOrigin
    public Response getOrgApplication(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/open/page/getOrgApplication";
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
            //如果指定参数为空 则返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"appkey", "appsecret", "appid"})) {
                result = Response.getResponseError();
            } else {
                String appkey = param.get("appkey").toString();
                String appsecret = param.get("appsecret").toString();
                String appid = param.get("appid").toString();
                Map<String, Object> checkCode = openPageService.getOrgApplication(appid);
                //进行MD5加密
                String md5 = JzbDataCheck.Md5(appid + appkey + appsecret + checkCode.get("checkcode").toString());
                //如果验证MD5加密与第三方传过来的是相等的 则把用户信息返回给第三方
                Map<String, Object> map = new HashMap<>();
                Map<String, Object> map1 = new HashMap<>();
                if (md5.equals(param.get("checkcode"))) {
                    map.put("uid", param.get("uid"));
                    //从缓存中查询用户信息
                    Response response = getCid(map);
                    Map<String, Object> entity = (Map<String, Object>) response.getResponseEntity();
                    Response cacheUserInfo = userAuthApi.getUserInfo(map);
                    Map<String, Object> Entity = (Map<String, Object>) cacheUserInfo.getResponseEntity();
                    map1.put("uid", Entity.get("uid"));
                    map1.put("cname", Entity.get("cname"));
                    map1.put("cid", Entity.get("cid"));
                    result = Response.getResponseSuccess();
                    result.setResponseEntity(map1);
                } else {
                    result = Response.getResponseError();
                    map1.put("error", "checkcode加密错误");
                    result.setResponseEntity(map1);

                }
            }
        } catch (Exception ex) {
            //打印错误信息
            flag = false;
            JzbTools.logError(ex);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getOrgApplication Method", ex.toString()));
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
     * 根据token进行对比 如果第三方传过来的token正确 则返回用户信息给第三方进行登录
     * <p>
     * 单点登录
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getUserId", method = RequestMethod.POST)
    @CrossOrigin
    public Response getUserId(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/open/page/getUserId";
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
            //如果指定参数为空 则返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"token"})) {
                result = Response.getResponseError();
            } else {
                Response response = userAuthApi.checkToken(param);
                Map<String, Object> entity = (Map<String, Object>) response.getResponseEntity();
                /** 如果token验证真确有用用户信息返回则返回成功信息*/
                if (entity != null && entity.size() > 0) {
                    result = Response.getResponseSuccess();
                    result.setResponseEntity(entity);
                } else {
                    //否则返回错误信息
                    result = Response.getResponseError();
                }
            }
        } catch (Exception ex) {
            //打印错误信息
            flag = false;
            JzbTools.logError(ex);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getUserId Method", ex.toString()));
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
     * 登录时获取企业id
     * @param param
     * @return
     */
    @RequestMapping(value = "/getCid",method = RequestMethod.POST)
    @CrossOrigin
    public Response getCid(@RequestBody Map<String, Object> param) {
        Response response;
        try {
            response = Response.getResponseSuccess();
            Map<Object, Object> map = new HashMap<>();
            map.put("cid", param.get("cid"));
            response.setResponseEntity(map);
        } catch (Exception e) {
            JzbTools.logError(e);
            response = Response.getResponseError();
        }
        return response;
    }


    /**
     * 调第三方接口传给我项目信息
     * @param param
     * @return
     */
    @RequestMapping(value = "/getProject",method = RequestMethod.POST)
    @CrossOrigin
    public Response getProject(@RequestBody(required = false) Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/open/page/getProject";
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
            param.put("uid", userInfo.get("uid"));


            //
            /*Response cacheUserInfo = userAuthApi.getUserInfo(param);*/
            /*Map<String, Object> Entity = (Map<String, Object>) cacheUserInfo.getResponseEntity();*/
            /*String uid = "KWWWCPERKXNX";*/
            String uid = param.get("uid").toString();
            //调用第三方接口
            String sr = sendPost("http://bhz.jizhibao.com.cn/Api/GetProjListV3", "uid" +"="+ uid);
            //把返回值转成JSON格式
            JSONObject jsonObject =  JSON.parseObject(sr);
            result = Response.getResponseSuccess(userInfo);
            result.setResponseEntity(jsonObject);
        } catch (Exception ex) {
          //打印错误信息
            flag = false;
            JzbTools.logError(ex);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getProject Method", ex.toString()));
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
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }


    /**
     * 模糊查询开发者应用表
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     */
    @RequestMapping(value = "/searchOrgApplication", method = RequestMethod.POST)
    @CrossOrigin
    public Response searchOrgApplication(@RequestBody(required = false) Map<String, Object> param) {
        Response result;
        PageInfo Info;
        try {
            Map<String, Object> map = new HashMap<>();
            Map<String, Object> map2 = new HashMap<>();
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("cid", param.get("cid"));
            map.put("cid", param.get("cid"));
            map.put("userinfo", param.get("userinfo"));
            //根据cid查询企业的管理员
            Response response = companyApi.getEnterpriseData(map);
            Map<String, Object> map1 = (Map<String, Object>) response.getResponseEntity();
            //如果是企业的管理员,则可以看到企业下面所有的应用  不是管理员只能看到自己小组的应用
            List<Map<String, Object>> orgAppList = new ArrayList<>();
            if (!map1.get("manager").equals(userInfo.get("uid"))) {
                map2.put("uid", userInfo.get("uid"));
                List<Map<String, Object>> list = openPageService.getAppDeveloper(map2);
                for (int i = 0; i < list.size(); i++) {
                    param.put("appid", list.get(i).get("appid"));
                    List<Map<String, Object>> orgAppLists = openPageService.searchOrgApplication(param);
                    for (int j = 0; j < orgAppLists.size(); j++) {
                        orgAppList.add(orgAppLists.get(j));
                    }
                }
            } else {

           /* int rows = JzbDataType.getInteger(param.get("pagesize"));
            int page = JzbDataType.getInteger(param.get("pageno"));
            if (page > 0 && rows > 0) {
                param.put("start", rows * (page - 1));
                param.put("pagesize", rows);*/
                orgAppList = openPageService.searchOrgApplication(param);
            }
            result = Response.getResponseSuccess();
            Info = new PageInfo();
            Info.setList(orgAppList);
                /*int count = JzbDataType.getInteger(param.get("count"));
                if (count == 0) {
                    int size = openPageService.searchOrgApplicationCount(param);
                    Info.setTotal(size > 0 ? size : orgAppList.size());
                }*/
            result.setPageInfo(Info);
            /*} else {
                result = Response.getResponseError();
            }*/
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }


    /**
     * 全界面的应用查询
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     */
    @RequestMapping(value = "/searchOrgApplications", method = RequestMethod.POST)
    @CrossOrigin
    public Response searchOrgApplications(@RequestBody(required = false) Map<String, Object> param) {
        Response result;
        PageInfo Info;
        try {

            if (param.get("userinfo") != null) {
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            }
           /* int rows = JzbDataType.getInteger(param.get("pagesize"));
            int page = JzbDataType.getInteger(param.get("pageno"));
            if (page > 0 && rows > 0) {
                param.put("start", rows * (page - 1));
                param.put("pagesize", rows);*/
            List<Map<String, Object>> orgAppList = openPageService.searchOrgApplications(param);
            for (int i = 0; i < orgAppList.size(); i++) {
                orgAppList.get(i).put("cname", orgAppList.get(i).get("appname"));
                orgAppList.get(i).put("type", "3");
            }
            result = Response.getResponseSuccess();
            Info = new PageInfo();
            Info.setList(orgAppList);
                /*int count = JzbDataType.getInteger(param.get("count"));
                if (count == 0) {
                    int size = openPageService.searchOrgApplicationCount(param);
                    Info.setTotal(size > 0 ? size : orgAppList.size());
                }*/
            result.setPageInfo(Info);
            /*} else {
                result = Response.getResponseError();
            }*/
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 应用列表的修改和删除
     *
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
                    node.put("numIdx", JzbRandom.getRandomChar(7));
                    //拿到菜单id去查询页面对应的菜单
                    Map<String, Object> hashMap = new HashMap<>();
                    hashMap.put("mid", map.get("mid").toString());
                    List<Map<String, Object>> list = openPageService.getApplicationPage(hashMap);
                    // if root node
                    if (parentId.equals(firstParent)) {
                        result.add(node);
                        recordJson.put(map.get("mid").toString(), node);
                        //把页面放到菜单下面
                        for (int j = 0; j < list.size(); j++) {
                            List<Map<String, Object>> children = (List<Map<String, Object>>) node.get("children");
                            list.get(j).put("numIdx", JzbRandom.getRandomChar(7));
                            children.add(list.get(j));
                        }

                    } else if (recordJson.containsKey(parentId)) {
                        // add children
                        recordJson.getJSONObject(parentId).getJSONArray("children").add(node);
                        for (int j = 0; j < list.size(); j++) {
                            List<Map<String, Object>> children = (List<Map<String, Object>>) node.get("children");
                            list.get(j).put("numIdx", JzbRandom.getRandomChar(7));
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
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/updateMenuPage", method = RequestMethod.POST)
    @CrossOrigin
    public Response updateMenuPage(@RequestBody Map<String, Object> param) {
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
