package com.jzb.operate.controller;

import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbTools;
import com.jzb.operate.api.auth.AuthUserApi;
import com.jzb.operate.api.redis.UserRedisServiceApi;
import com.jzb.operate.service.TbHandleItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.DelegatingServletInputStream;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/opt/HandleItem")
public class TbHandleItemController {

    @Autowired
    private TbHandleItemService tbhandleitemservice;

    @Autowired
    private UserRedisServiceApi userRedisServiceApi;

    @Autowired
    private AuthUserApi authUserApi;

    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(TbCompanyMethodController.class);

    /**
     * 销售统计分析跟进人详情查询
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getHandleItem", method = RequestMethod.POST)
    @CrossOrigin
    public Response getHandleItem(@RequestBody(required = false) Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/opt/HandleItem/getHandleItem";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            //如果指定参数为空则返回404
                //获取map中的list
                List<Map<String, Object>> paramList = (List) param.get("list");
                List list1 = new ArrayList<>();

                for (int i = 0; i < paramList.size(); i++) {
                     Response cacheUserInfo = userRedisServiceApi.getCacheUserInfo(paramList.get(i));
                    //遍历list 调用服务查询数据
                    List<Map<String, Object>> list  = tbhandleitemservice.queryHandleItem(paramList.get(i));
                    Map<String, Object> map = new HashMap<>();
                    Map<String,Object> map1 = (Map<String, Object>) cacheUserInfo.getResponseEntity();
                    //如果list为空，但是缓存中查询出来的数据uid和cname不为空，则默认吧其他参数置为空返回给前端
                    if (list.size() <=0  && cacheUserInfo.getResponseEntity() != null) {
                        //往map中添加空字符串，返回给前端
                            map.put("summary", "");
                            map.put("projectname", "");
                            map.put("unitName", "");
                            map.put("dictvalue", "");
                            map.put("invest", "");
                            map.put("contamount", "");
                            map.put("needres", "");
                            map.put("handletime", "");
                            map.put("uid", "");
                            map.put("nexttime", "");
                            map.put("person", "");
                            map.put("context", "");
                            map.put("personid", "");
                            map.put("attach", "");
                            map.put("region", "");
                            map.put("projectid", "");
                            map.put("cid", "");
                       //遍历缓存中查询出来的map，
                            if (param.get("cnames") == null) {
                                map.put("uidcname", map1.get("cname"));
                                map.put("uid", map1.get("uid"));
                            }
                            if (param.get("cnames") != null) {
                                if (map1.containsValue(param.get("cnames"))) {
                                    map.put("uidcname", map1.get("cname"));
                                    map.put("uid", map1.get("uid"));
                                } else {
                                    map.clear();
                                }
                         }
                        //添加到list1中返回
                            list1.add(map);
                        } else {
                        //遍历缓存中查询出来的map数据 添加到list中返回给前端
                            for (int j = 0; j < list.size(); j++) {
                                    if (param.get("cnames") == null) {
                                        list.get(j).put("uidcname", map1.get("cname"));
                                        list.get(j).put("uid", map1.get("uid"));
                                    }
                                    if (param.get("cnames") != null) {
                                        if (map1.containsValue(param.get("cnames"))) {
                                            list.get(j).put("uidcname", map1.get("cname"));
                                            list.get(j).put("uid", map1.get("uid"));
                                        } else {
                                            list.get(j).clear();
                                        }
                                }
                                 list1.add(list.get(j));
                        }
                    }
                }

                /*for (int i = 0; i < list.size(); i++) {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("uid", list.get(i).get("uid"));

                    Response userInfo1 = userRedisServiceApi.getNameById(list.get(i));

                    list.get(i).put("uidname",userInfo1.getResponseEntity());
                }*/

                //获取用户信息
                userInfo = (Map<String, Object>) param.get("userinfo");
               PageInfo pageInfo = new PageInfo();
               pageInfo.setList(list1);
                //响应成功信息
                result = Response.getResponseSuccess(userInfo);
                result.setPageInfo(pageInfo);

        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getHandleItem Method", ex.toString()));
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
     * 所有业主-销售统计分许-根据页面的超链接进行查询
     * @param param
     * @return
     */
    @RequestMapping(value = "/queryHandleItem",method = RequestMethod.POST)
    @CrossOrigin
    public Response queryHandleItem(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/opt/HandleItem/queryHandleItem";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }

            List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();


                Response cacheUserInfo;

                if (param.get("projectid") != null && param.get("projectid") != "" || param.get("personid") != null &&
                param.get("personid") != "" ||
                        param.get("cid") != null && param.get("cid") != "" || param.get("cname") != null && param.get("cname") != "" ||
                        param.get("dictvalue") != null && param.get("dictvalue") != ""
                ) {

                    //遍历list 调用服务查询数据
                    List<Map<String, Object>> list  = tbhandleitemservice.getHandleItem(param);



                        //遍历缓存中查询出来的map数据 添加到list中返回给前端
                        for (int  x = list.size() -1; x >=0 ; x--) {
                            cacheUserInfo = userRedisServiceApi.getCacheUserInfo(list.get(x));
                            Map<String,Object> map1 = (Map<String, Object>) cacheUserInfo.getResponseEntity();
                            //如果list为空，但是缓存中查询出来的数据uid和cname不为空，则默认吧其他参数置为空返回给前端

                            if (list.get(x) != null) {
                                if (map1 != null) {
                                    list.get(x).put("uidcname", map1.get("cname"));
                                    list.get(x).put("uid", map1.get("uid"));
                                }
                            }
                            if (param.get("cnames") != null && param.get("cnames") !="") {
                                if (map1.containsValue(param.get("cnames"))) {
                                    list.get(x).put("uidcname", map1.get("cname"));
                                    list.get(x).put("uid", map1.get("uid"));
                                } else {
                                    list.get(x).clear();
                                }
                            }
                            list1.add(list.get(x));
                        }
                    }

                if (param.get("uid") != null && param.get("uid") != ""){

                    //遍历list 调用服务查询数据
                    List<Map<String, Object>> list  = tbhandleitemservice.getHandleItem(param);

                    cacheUserInfo = userRedisServiceApi.getCacheUserInfo(param);
                    Map<String,Object> map1 = (Map<String, Object>) cacheUserInfo.getResponseEntity();
                    //如果list为空，但是缓存中查询出来的数据uid和cname不为空，则默认吧其他参数置为空返回给前端

                    //遍历缓存中查询出来的map数据 添加到list中返回给前端
                    for (int j = 0; j < list.size(); j++) {
                        if (list.get(j) != null) {
                            list.get(j).put("uidcname", map1.get("cname"));
                            list.get(j).put("uid", map1.get("uid"));
                        }
                        list1.add(list.get(j));
                    }
                }

                if (param.get("cnames") != null && param.get("cnames") != "") {
                    Map<String, Object> hashMap = new HashMap<>();
                    hashMap = (Map<String, Object>) param;
                    hashMap.put("cname", param.get("cnames"));
                    Response response = authUserApi.searchInvitee(hashMap);
                    List<Map<String,Object>> entitys = (List<Map<String, Object>>) response.getPageInfo().getList();
                    if (entitys != null) {

                    for (int y = 0; y < entitys.size(); y++) {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("uid", entitys.get(y).get("uid"));
                        //遍历list 调用服务查询数据
                        List<Map<String, Object>> list  = tbhandleitemservice.getHandleItem(map);

                        cacheUserInfo = userRedisServiceApi.getCacheUserInfo(entitys.get(y));
                        Map<String,Object> map1 = (Map<String, Object>) cacheUserInfo.getResponseEntity();
                        //如果list为空，但是缓存中查询出来的数据uid和cname不为空，则默认吧其他参数置为空返回给前端

                        //遍历缓存中查询出来的map数据 添加到list中返回给前端
                        for (int j = 0; j < list.size(); j++) {
                            if (list.get(j) != null) {
                                list.get(j).put("uidcname", map1.get("cname"));
                                list.get(j).put("uid", map1.get("uid"));
                            }
                            list1.add(list.get(j));
                        }

                    }
                }
                }

            List<Object> objects = new ArrayList<>();

            for (int i = 0; i < list1.size(); i++) {
                if (list1.get(i).get("cid") != null) {
                    objects.add(list1.get(i));
                }
            }

            //获取用户信息
            userInfo = (Map<String, Object>) param.get("userinfo");
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(objects);
            //响应成功信息
            result = Response.getResponseSuccess(userInfo);
            result.setPageInfo(pageInfo);

        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "queryHandleItem Method", ex.toString()));
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
