package com.jzb.operate.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbPageConvert;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.operate.api.auth.AuthInfoApi;
import com.jzb.operate.api.base.RegionBaseApi;
import com.jzb.operate.api.org.NewTbCompanyListApi;
import com.jzb.operate.api.org.TbDeptUserListApi;
import com.jzb.operate.api.org.TbTrackUserListApi;
import com.jzb.operate.service.TbTravelExpenseService;
import com.jzb.operate.service.TbTravelProduceService;
import com.jzb.operate.service.TbTravelService;
import com.jzb.operate.util.PrindexUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @Author sapientia
 * @Date 2019/12/2 11:21
 * @Description   总结报销
 */
@RestController
@RequestMapping(value = "/operate/reimburseSystem")
public class TbTravelController {

    @Autowired
    private TbTravelService tbTravelService;

    @Autowired
    private TbTrackUserListApi tbTrackUserListApi;

    @Autowired
    private NewTbCompanyListApi newTbCompanyListApi;

    @Autowired
    private TbTravelExpenseService tbTravelExpenseService;

    @Autowired
    private TbDeptUserListApi tbDeptUserListApi;

    @Autowired
    private RegionBaseApi regionBaseApi;

    @Autowired
    private AuthInfoApi authInfoApi;

    @Autowired
    private TbTravelProduceService travelProduceService;
    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(TbTravelController.class);

    /**
     * @Author sapientia
     * @Date 16:04 2019/12/7
     * @Description 根据用户id查询出差记录列表
     **/
    @RequestMapping(value = "/queryAllTravelList" ,method = RequestMethod.POST)
    @CrossOrigin
    public Response queryAllTravelList(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/operate/reimburseSystem/queryAllTravelList";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            if (JzbCheckParam.haveEmpty(param, new String[]{"pagesize", "pageno"})) {
                response = Response.getResponseError();
            } else {
                JzbPageConvert.setPageRows(param);
                // 获取出差记录
                param.put("uid",userInfo.get("uid"));
                List<Map<String, Object>> list = tbTravelService.queryAllTravelList(param);
                for(int i = 0 , a = list.size(); i < a;i++){
                    Map<String, Object> appMap = new HashMap<>();
                    appMap.put("travelid", list.get(i).get("travelid").toString().trim());
                    if(list.get(i).get("rebstatus").equals("2")) {
                        appMap.put("rebversion", list.get(i).get("rebversion").toString().trim());
                        // 获取审批状态
                        List<Map<String, Object>> appList = tbTravelService.queryTravelApproval(appMap);
                        for (int k = 0, c = appList.size(); k < c; k++) {
                            Map<String, Object> uMap = new HashMap<>();
                            uMap.put("truid", appList.get(k).get("truid"));
                            uMap.put("userinfo", userInfo);
                            Response res = tbDeptUserListApi.queryPersonNameByuid(uMap);
                            List<Map<String, Object>> userList = res.getPageInfo().getList();
                            appList.get(k).put("userList", userList);
                        }
                        list.get(i).put("appList", appList);
                    }

                    // 获取出差详情
                    List<Map<String, Object>> reList = tbTravelService.queryTravelList(appMap);
                    for(int j = 0 ,b =reList.size(); j < b ;j++){
                        // 获取单位名称
                        Map<String,Object> caMap =new HashMap<>();
                        caMap.put("cid",reList.get(j).get("cid").toString().trim());
                        caMap.put("userinfo",userInfo);
                        Response res = newTbCompanyListApi.queryCompanyNameBycid(caMap);
                        List<Map<String,Object>> caList = res.getPageInfo().getList();
                        reList.get(j).put("caList",caList);

                        // 获取同行人名称
                        if(!JzbTools.isEmpty(reList.get(j).get("trpeers"))) {
                            Map<String, Object> uidMap = new HashMap<>();
                            uidMap.put("uids", reList.get(j).get("trpeers"));
                            uidMap.put("userinfo", userInfo);
                            Response uRes = tbDeptUserListApi.searchInvitee(uidMap);
                            String unameStr = uRes.getResponseEntity().toString();
                            reList.get(j).put("peersList", unameStr);
                        }
                        // 获取出差区域信息
                        if(!JzbTools.isEmpty(reList.get(j).get("trregion"))) {
                            Map<String, Object> regionMap = new HashMap<>();
                            regionMap.put("region", reList.get(j).get("trregion"));
                            Response region = regionBaseApi.getRegionInfo(regionMap);
                            reList.get(j).put("trregionList", region.getResponseEntity());
                        }
                        // 获取资料及情报
                        Map<String,Object> recMap =new HashMap<>();
                        recMap.put("deid",reList.get(j).get("deid").toString().trim());
                        // 通过出差详情id  获取出差资料信息
                        List<Map<String, Object>> daList = tbTravelService.queryTravelData(recMap);
                        // 通过出差详情id  获取出差情报信息
                        List<Map<String, Object>> infoList = tbTravelService.queryTravelInfo(recMap);
                        for (int l = 0, d = infoList.size();l < d;l++){
                            if(!JzbTools.isEmpty(infoList.get(l).get("prolist"))) {
                                Map<String,Object> proListMap =new HashMap<>();
                                proListMap.put("prolist",infoList.get(l).get("prolist"));
                                String prolist = infoList.get(l).get("prolist").toString();
                                String[] split = prolist.split(",");
                                proListMap.put("prolist",split);
                                infoList.get(l).put("prolist",proListMap);
                            }
                        }
                        reList.get(j).put("daList",daList);
                        reList.get(j).put("infoList",infoList);
                        // 获取产出情况
                        if(!JzbTools.isEmpty(reList.get(j).get("produce"))) {
                            List<Map<String, Object>> proList = tbTravelService.queryTravelProduce();
                            List<Integer> prindexList = PrindexUtil.getPrindex(JzbDataType.getInteger(reList.get(j).get("produce")), proList);
                            Map<String, Object> prindexMap = new HashMap<>();
                            prindexMap.put("prindex", prindexList);
                            List<Map<String, Object>> priList = travelProduceService.queryProduce(prindexMap);
                            reList.get(j).put("prindex", prindexList);
                            reList.get(j).put("produceMaps", priList);
                        }
                    }
                    list.get(i).put("reList",reList);
                }
                response = Response.getResponseSuccess(userInfo);
                PageInfo pageInfo = new PageInfo();
                pageInfo.setList(list);
                pageInfo.setTotal(tbTravelService.countAllList(param));
                response.setPageInfo(pageInfo);
            }
        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "queryAllTravelList Method", ex.toString()));
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
     * @Author sapientia
     * @Date 10:49 2019/12/2
     * @Description 根据出差id查询出差的详情
     **/
    @RequestMapping(value = "/queryTravelListBytrid" ,method = RequestMethod.POST)
    @CrossOrigin
    public Response queryTravelListBytrid(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/operate/reimburseSystem/queryTravelListBytrid";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            if (JzbCheckParam.haveEmpty(param, new String[]{"pagesize", "pageno","travelid"})) {
                response = Response.getResponseError();
            } else {
                JzbPageConvert.setPageRows(param);
                // 获取出差记录
                param.put("uid", userInfo.get("uid"));
                param.put("travelid", param.get("travelid").toString().trim());
                List<Map<String, Object>> appList = tbTravelService.queryAllTravelList(param);
                for(int a = 0 ,b = appList.size();a < b;a ++ ){
                    if(appList.get(a).get("rebstatus").equals("2")){
                        Map<String, Object> appMap = new HashMap<>();
                        appMap.put("rebversion", appList.get(a).get("rebversion"));
                        // 获取审批状态
                        List<Map<String, Object>> spList = tbTravelService.queryTravelApproval(appMap);
                        for (int k = 0, c = spList.size(); k < c; k++) {
                            Map<String, Object> uMap = new HashMap<>();
                            uMap.put("truid", spList.get(k).get("truid"));
                            uMap.put("userinfo", userInfo);
                            Response res = authInfoApi.getUsernameList(uMap);
                            Response res2 = tbDeptUserListApi.searchInvitee(uMap);
                            List<Map<String, Object>> userList = res.getPageInfo().getList();
                            List<Map<String, Object>> portraitList = res2.getPageInfo().getList();
                            spList.get(k).put("userList", userList);
                            spList.get(k).put("portraitList",portraitList);
                        }
                        appList.get(a).put("spList", spList);
                    }
                }
                List<Map<String, Object>> list = tbTravelService.queryTravelList(param);
                for (int i = 0, a = list.size(); i < a;i++) {
                    // 获取单位名称
                    Map<String, Object> caMap = new HashMap<>();
                    caMap.put("cid", list.get(i).get("cid").toString().trim());
                    caMap.put("userinfo", userInfo);
                    Response res = newTbCompanyListApi.queryCompanyNameBycid(caMap);
                    List<Map<String, Object>> caList = res.getPageInfo().getList();
                    list.get(i).put("caList", caList);

                    // 获取同行人名称
                    if(!JzbTools.isEmpty(list.get(i).get("trpeers"))) {
                        Map<String, Object> uidMap = new HashMap<>();
                        uidMap.put("uids", list.get(i).get("trpeers"));
                        uidMap.put("userinfo", userInfo);
                        Response uRes = tbDeptUserListApi.searchInvitee(uidMap);
                        String unameStr = uRes.getResponseEntity().toString();
                        list.get(i).put("peersList", unameStr);
                    }

                    // 获取出差区域信息
                    if(!JzbTools.isEmpty(list.get(i).get("trregion"))) {
                        Map<String, Object> regionMap = new HashMap<>();
                        regionMap.put("region", list.get(i).get("trregion"));
                        Response region = regionBaseApi.getRegionInfo(regionMap);
                        list.get(i).put("trregionList", region.getResponseEntity());
                    }
                    // 获取出差记录的花费及行程
                    List<Map<String, Object>> monList = tbTravelService.queryAllTravelList(param);
                    // 获取资料及情报
                    Map<String, Object> recMap = new HashMap<>();
                    recMap.put("deid", list.get(i).get("deid").toString().trim());
                    // 通过出差详情id  获取出差资料信息
                    List<Map<String, Object>> daList = tbTravelService.queryTravelData(recMap);
                    // 通过出差详情id  获取出差情报信息
                    List<Map<String, Object>> infoList = tbTravelService.queryTravelInfo(recMap);
                    for (int l = 0, d = infoList.size();l < d;l++){
                        if(!JzbTools.isEmpty(infoList.get(l).get("prolist"))) {
                            Map<String,Object> proListMap =new HashMap<>();
                            proListMap.put("prolist",infoList.get(l).get("prolist"));
                            String prolist = infoList.get(l).get("prolist").toString();
                            String[] split = prolist.split(",");
                            proListMap.put("prolist",split);
                            infoList.get(l).put("prolist",proListMap);
                        }
                    }

                    // 获取产出情况
                    if(!JzbTools.isEmpty(list.get(i).get("produce"))) {
                        List<Map<String, Object>> proList = tbTravelService.queryTravelProduce();
                        List<Integer> prindexList = PrindexUtil.getPrindex(JzbDataType.getInteger(list.get(i).get("produce")), proList);
                        Map<String, Object> prindexMap = new HashMap<>();
                        prindexMap.put("prindex", prindexList);
                        List<Map<String, Object>> priList = travelProduceService.queryProduce(prindexMap);
                        list.get(i).put("prindex", prindexList);
                        list.get(i).put("produceMaps", priList);
                    }
                    list.get(i).put("daList", daList);
                    list.get(i).put("infoList", infoList);
                    list.get(i).put("monList",monList);
                }
                response = Response.getResponseSuccess(userInfo);
                PageInfo pageInfo = new PageInfo();
                pageInfo.setList(list);
                pageInfo.setTotal(tbTravelService.countTravelInfo(param));
                response.setPageInfo(pageInfo);
            }
        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "queryTravelListBytrid Method", ex.toString()));
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
     * @Author sapientia
     * @Date 12:51 2019/12/5
     * @Description 查询产出情况
     **/
    @RequestMapping(value = "/queryInfoListByDeid" ,method = RequestMethod.POST)
    @CrossOrigin
    public Response queryInfoListByDeid(@RequestBody Map<String, Object> param) {
        Response response = null;
        Map<String, Object> userInfo = null;
        String api = "/operate/reimburseSystem/queryInfoListByDeid";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            if (JzbCheckParam.haveEmpty(param, new String[]{"pagesize", "pageno","travelid" })) {
                response = Response.getResponseError();
            } else {
                    JzbPageConvert.setPageRows(param);
                    param.put("uid",userInfo.get("uid"));
                    param.put("travelid",param.get("travelid").toString().trim());
                    // 获取出差详情
                    List<Map<String, Object>> list = tbTravelService.queryTravelList(param);
                    for(int i = 0,a = list.size();i < a;i++){
                        // 获取情报
                        Map<String, Object> deMap = new HashMap<>();
                        deMap.put("deid",list.get(i).get("deid"));
                        List<Map<String, Object>> infoList = tbTravelService.queryTravelInfo(deMap);
                        for (int l = 0, d = infoList.size();l < d;l++){
                            if(!JzbTools.isEmpty(infoList.get(l).get("prolist"))) {
                                Map<String,Object> proListMap =new HashMap<>();
                                proListMap.put("prolist",infoList.get(l).get("prolist"));
                                String prolist = infoList.get(l).get("prolist").toString();
                                String[] split = prolist.split(",");
                                proListMap.put("prolist",split);
                                infoList.get(l).put("prolist",proListMap);
                            }
                        }
                        // 获取项目产出
                        Map<String, Object> proMap = new HashMap<>();
                        proMap.put("userinfo",userInfo);
                        proMap.put("pageno",param.get("pageno"));
                        proMap.put("pagesize",param.get("pagesize"));
                        proMap.put("cid",list.get(i).get("cid"));
                        if(!JzbTools.isEmpty(list.get(i).get("projectid"))) {
                            proMap.put("projectid", list.get(i).get("projectid"));
                        }
                        Response res = newTbCompanyListApi.queryCompanyByid(proMap);
                        List<Map<String, Object>> resList = res.getPageInfo().getList();
                        // 获取产出资料
                        if(!JzbTools.isEmpty(list.get(i).get("produce"))) {
                            List<Map<String, Object>> proList = tbTravelService.queryTravelProduce();
                            List<Integer> prindexList = PrindexUtil.getPrindex(JzbDataType.getInteger(list.get(i).get("produce")), proList);
                            Map<String, Object> prindexMap = new HashMap<>();
                            prindexMap.put("prindex", prindexList);
                            List<Map<String, Object>> priList = travelProduceService.queryProduce(prindexMap);
                            list.get(i).put("prindex", prindexList);
                            list.get(i).put("produceMaps", priList);
                        }
                        list.get(i).put("infoList",infoList);
                        list.get(i).put("resList",resList);
                    }
                    response = Response.getResponseSuccess(userInfo);
                    PageInfo pageInfo = new PageInfo();
                    pageInfo.setTotal(tbTravelService.countTravelList(param));
                    pageInfo.setList(list);
                    response.setPageInfo(pageInfo);
                }
            }catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "queryInfoListByDeid Method", ex.toString()));
        }
        if (userInfo != null) {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
                    userInfo.get("msgTag").toString(), "User Login Message"));
        } else {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
        }
        return response;
    }

//    /**
//     * 修改出差费用(预留,现阶段不需要，未测试)
//     */
//    @RequestMapping(value = "/updateTravelFare" ,method = RequestMethod.POST)
//    @Transactional
//    public Response updateTravelFare(@RequestBody Map<String, Object> param) {
//        Response response;
//        Map<String, Object> userInfo = null;
//        String api = "/operate/reimburseSystem/updateTravelFare";
//        boolean flag = true;
//        try {
//            if (param.get("userinfo") != null) {
//                userInfo = (Map<String, Object>) param.get("userinfo");
//                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
//                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
//            } else {
//                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
//            }
//            if (JzbCheckParam.haveEmpty(param, new String[]{"pagesize", "pageno","travelid" })) {
//                response = Response.getResponseError();
//            } else {
//                JzbPageConvert.setPageRows(param);
//                param.put("updtime", System.currentTimeMillis());
//                response = tbTravelService.updateTravelFare(param) > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
//            }
//        } catch (Exception ex) {
//            flag = false;
//            JzbTools.logError(ex);
//            response = Response.getResponseError();
//            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "updateTravelFare Method", ex.toString()));
//        }
//        if (userInfo != null) {
//            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
//                    userInfo.get("msgTag").toString(), "User Login Message"));
//        } else {
//            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
//        }
//        return response;
//    }

    /**
     * @Author sapientia
     * @Date 10:52 2019/12/4
     * @Description 根据申请人id、单位id以及拜访时间获取跟进记录(总结表)
     **/
    @RequestMapping(value = "/queryTrackUserList",method = RequestMethod.POST)
    @CrossOrigin
    public Response queryTrackUserList(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/operate/reimburseSystem/queryTrackUserList";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            if (JzbCheckParam.haveEmpty(param, new String[]{"pagesize", "pageno","travelid" })) {
                response = Response.getResponseError();
            } else {
                JzbPageConvert.setPageRows(param);
                param.put("uid",userInfo.get("uid"));
                param.put("uname",userInfo.get("uname"));
                param.put("travelid",param.get("travelid").toString().trim());
                List<Map<String,Object>> list =tbTravelService.queryTravelList(param);
                for (int i = 0 ,a = list.size(); i < a ; i++){
                    // 根据申请人 单位 拜访时间 查询跟进记录
                    Map<String, Object> dataMap = new HashMap();
                    dataMap.put("userinfo",userInfo);
                    dataMap.put("uid",userInfo.get("uid"));
                    dataMap.put("pageno",param.get("pageno"));
                    dataMap.put("pagesize",param.get("pagesize"));
                    dataMap.put("cid",list.get(i).get("cid"));
                    dataMap.put("trtime",list.get(i).get("trtime"));
                    Response cname = newTbCompanyListApi.queryCompanyNameBycid(dataMap);
                    Response res = tbTrackUserListApi.queryTrackUserByName(dataMap);
                    List<Map<String,Object>> caList = cname.getPageInfo().getList();
                    List<Map<String, Object>> reList = res.getPageInfo().getList();
                    list.get(i).put("caList", caList);
                    list.get(i).put("reList", reList);
                    list.get(i).put("uname",userInfo.get("cname"));
                }
                response = Response.getResponseSuccess(userInfo);
                PageInfo pageInfo = new PageInfo();
                pageInfo.setTotal(tbTravelService.countTravelList(param));
                pageInfo.setList(list);
                response.setPageInfo(pageInfo);
            }
        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "queryTrackUserList Method", ex.toString()));
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
     * @Author sapientia
     * @Date 11:36 2019/12/5
     * @Description 设置报销单删除状态
     **/
    @RequestMapping(value = "/setDeleteStatus" ,method = RequestMethod.POST)
    @CrossOrigin
    @Transactional
    public Response setDeleteStatus(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/operate/reimburseSystem/setDeleteStatus";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            if (JzbCheckParam.haveEmpty(param, new String[]{"travelid" })) {
                response = Response.getResponseError();
            } else {
                param.put("travelid",param.get("travelid").toString().trim());
                response = tbTravelService.setDeleteStatus(param) > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
            }
        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "setDeleteStatus Method", ex.toString()));
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
     * @Author sapientia
     * @Date 11:36 2019/12/5
     * @Description 根据公司id修改公司信息
     **/
    @RequestMapping( value = "/updateCommonCompanyList",method = RequestMethod.POST)
    @CrossOrigin
    @Transactional
    public Response updateCommonCompanyList(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/operate/reimburseSystem/updateCommonCompanyList";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            if (JzbCheckParam.haveEmpty(param, new String[]{"cid" })) {
                response = Response.getResponseError();
            } else {
                param.put("cid",param.get("cid").toString().trim());
                param.put("userinfo",userInfo);
                Response res = newTbCompanyListApi.updateCommonCompanyList(param);
                if(res.getServerResult().getResultCode() == 200){
                    param.put("updtime" ,System.currentTimeMillis());
                    param.put("upduid",userInfo.get("uid"));
                    response = tbTravelService.updateInfoList(param) > 0 ? Response.getResponseSuccess(userInfo):Response.getResponseError();
                }else {
                    return Response.getResponseError();
                }
            }
        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "updateCommonCompanyList Method", ex.toString()));
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
     * @Author sapientia
     * @Date 11:36 2019/12/5
     * @Description 根据项目id修改项目信息
     **/
    @RequestMapping(value = "/updateCompanyProject",method = RequestMethod.POST)
    @CrossOrigin
    @Transactional
    public Response updateCompanyProject(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/operate/reimburseSystem/updateCompanyProject";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            if (JzbCheckParam.haveEmpty(param, new String[]{"projectid" })) {
                response = Response.getResponseError();
            } else {
                param.put("projectid",param.get("projectid").toString().trim());
                param.put("userinfo",userInfo);
                Response res = newTbCompanyListApi.updateCompanyProject(param);
                if(res.getServerResult().getResultCode() == 200){
                    param.put("updtime" ,System.currentTimeMillis());
                    param.put("upduid",userInfo.get("uid"));
                    response = tbTravelService.updateInfoList(param) > 0 ? Response.getResponseSuccess(userInfo):Response.getResponseError();
                }else {
                    return Response.getResponseError();
                }
            }
        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "updateCompanyProject Method", ex.toString()));
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
     * @Author sapientia
     * @Date 11:36 2019/12/5
     * @Description 根据项目id修改项目情报
     **/
    @RequestMapping(value = "/updateCompanyProjectInfo",method = RequestMethod.POST)
    @Transactional
    public Response updateCompanyProjectInfo(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/operate/reimburseSystem/updateCompanyProjectInfo";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            if (JzbCheckParam.haveEmpty(param, new String[]{"projectid" })) {
                response = Response.getResponseError();
            } else {
                JzbPageConvert.setPageRows(param);
                param.put("projectid",param.get("projectid").toString().trim());
                param.put("userinfo",userInfo);
                Response res = newTbCompanyListApi.updateCompanyProjectInfo(param);
                if(res.getServerResult().getResultCode() == 200){
                    param.put("updtime" ,System.currentTimeMillis());
                    param.put("upduid",userInfo.get("uid"));
                    response = tbTravelService.updateInfoList(param) > 0 ? Response.getResponseSuccess(userInfo):Response.getResponseError();
                }else {
                    return Response.getResponseError();
                }
            }
        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "updateCompanyProjectInfo Method", ex.toString()));
        }
        if (userInfo != null) {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
                    userInfo.get("msgTag").toString(), "User Login Message"));
        } else {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
        }
        return response;
    }

//    /**
//     * @Author sapientia
//     * @Date 17:28 2019/12/5
//     * @Description 添加报销单信息
//     **/
//    @RequestMapping(value = "/saveTravelExpense",method = RequestMethod.POST)
//    @Transactional
//    public Response saveTravelExpense(@RequestBody Map<String, Object> param) {
//        Response response;
//        Map<String, Object> userInfo = null;
//        String api = "/operate/reimburseSystem/saveTravelExpense";
//        boolean flag = true;
//        try {
//            if (param.get("userinfo") != null) {
//                userInfo = (Map<String, Object>) param.get("userinfo");
//                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
//                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
//            } else {
//                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
//            }
//            if (JzbCheckParam.haveEmpty(param, new String[]{"list" })) {
//                response = Response.getResponseError();
//            } else {
//                JzbPageConvert.setPageRows(param);
//                List<Map<String, Object>> list = (List<Map<String, Object>>) param.get("list");
//                for (int i = 0, a = list.size();i < a ;i++) {
//                    if(!JzbTools.isEmpty(list.get(i).get("trsum")))  list.get(i).put("trsum",JzbDataType.getInteger(list.get(i).get("trsum")));
//                    if(!JzbTools.isEmpty(list.get(i).get("exstrtime")))  list.get(i).put("exstrtime",JzbDataType.getInteger(list.get(i).get("exstrtime")));
//                    if(!JzbTools.isEmpty(list.get(i).get("exendtime"))) list.get(i).put("exendtime",JzbDataType.getInteger(list.get(i).get("exendtime")));
//                    if(!JzbTools.isEmpty(list.get(i).get("mail"))) list.get(i).put("mail",JzbDataType.getInteger(list.get(i).get("mail")));
//                    if(!JzbTools.isEmpty(list.get(i).get("crosum")))  list.get(i).put("crosum",JzbDataType.getInteger(list.get(i).get("crosum")));
//                    if(!JzbTools.isEmpty(list.get(i).get("getaccsum"))) list.get(i).put("getaccsum",JzbDataType.getInteger(list.get(i).get("getaccsum")));
//                    if(!JzbTools.isEmpty(list.get(i).get("subsidy")))  list.get(i).put("subsidy",JzbDataType.getInteger(list.get(i).get("subsidy")));
//                    if(!JzbTools.isEmpty(list.get(i).get("othsum")))   list.get(i).put("othsum",JzbDataType.getInteger(list.get(i).get("othsum")));
//                    if(!JzbTools.isEmpty(list.get(i).get("sum")))  list.get(i).put("sum",JzbDataType.getInteger(list.get(i).get("sum")));
//                    list.get(i).put("travelid", list.get(i).get("travelid").toString().trim());
//                    list.get(i).put("exid", JzbRandom.getRandomChar(12));
//                    list.get(i).put("addtime", System.currentTimeMillis());
//                    list.get(i).put("adduid",userInfo.get("uid"));
//                    list.get(i).put("status", 1);//默认状态1
//                    Map<String,Object> trMap = new HashMap<>();
//                    trMap.put("travelid",list.get(i).get("travelid"));
//                    tbTravelService.updateRebStatus(trMap);
//                }
//                tbTravelExpenseService.saveTravelExpense(list);
//                response = Response.getResponseSuccess(userInfo);
//            }
//        } catch (Exception ex) {
//            flag = false;
//            JzbTools.logError(ex);
//            response = Response.getResponseError();
//            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "saveTravelExpense Method", ex.toString()));
//        }
//        if (userInfo != null) {
//            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
//                    userInfo.get("msgTag").toString(), "User Login Message"));
//        } else {
//            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
//        }
//        return response;
//    }

    /**
     * @Author sapientia
     * @Date 17:37 2019/12/5
     * @Description 报销单信息修改
     **/
    @RequestMapping(value = "/updateTravelExpense" ,method = RequestMethod.POST)
    @Transactional
    public Response updateTravelExpense(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/operate/reimburseSystem/updateTravelExpense";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            if (JzbCheckParam.haveEmpty(param, new String[]{"list" })) {
                response = Response.getResponseError();
            } else {
                JzbPageConvert.setPageRows(param);
                List<Map<String, Object>> list = (List<Map<String, Object>>) param.get("list");
                for (int i = 0, a = list.size();i < a ;i++) {
                    if(!JzbTools.isEmpty(list.get(i).get("trsum")))  list.get(i).put("trsum",JzbDataType.getInteger(list.get(i).get("trsum")));
                    if(!JzbTools.isEmpty(list.get(i).get("exstrtime")))  list.get(i).put("exstrtime",JzbDataType.getInteger(list.get(i).get("exstrtime")));
                    if(!JzbTools.isEmpty(list.get(i).get("exendtime"))) list.get(i).put("exendtime",JzbDataType.getInteger(list.get(i).get("exendtime")));
                    if(!JzbTools.isEmpty(list.get(i).get("mail"))) list.get(i).put("mail",JzbDataType.getInteger(list.get(i).get("mail")));
                    if(!JzbTools.isEmpty(list.get(i).get("crosum")))  list.get(i).put("crosum",JzbDataType.getInteger(list.get(i).get("crosum")));
                    if(!JzbTools.isEmpty(list.get(i).get("getaccsum"))) list.get(i).put("getaccsum",JzbDataType.getInteger(list.get(i).get("getaccsum")));
                    if(!JzbTools.isEmpty(list.get(i).get("subsidy")))  list.get(i).put("subsidy",JzbDataType.getInteger(list.get(i).get("subsidy")));
                    if(!JzbTools.isEmpty(list.get(i).get("othsum")))   list.get(i).put("othsum",JzbDataType.getInteger(list.get(i).get("othsum")));
                    if(!JzbTools.isEmpty(list.get(i).get("sum")))  list.get(i).put("sum",JzbDataType.getInteger(list.get(i).get("sum")));
                    if(!JzbTools.isEmpty(list.get(i).get("exid"))){
                        tbTravelExpenseService.updateTravelExpense(list.get(i));
                    }
                    else if (JzbTools.isEmpty(list.get(i).get("exid"))){
                        list.get(i).put("exid", JzbRandom.getRandomChar(12));
                        list.get(i).put("addtime", System.currentTimeMillis());
                        list.get(i).put("adduid",userInfo.get("uid"));
                        list.get(i).put("status", 1);//默认状态1
                        Map<String,Object> trMap = new HashMap<>();
                        trMap.put("travelid",list.get(i).get("travelid"));
                        tbTravelService.updateRebStatus(trMap);
                        tbTravelExpenseService.saveTravelExpense(list.get(i));
                    }
                }
                response = Response.getResponseSuccess(userInfo);
            }
        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "updateTravelExpense Method", ex.toString()));
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
     * @Author sapientia
     * @Date 10:26 2019/12/6
     * @Description 查询报销单信息
     **/
    @RequestMapping(value = "/queryTravelExpenseByid",method = RequestMethod.POST)
    public Response queryTravelExpenseByid(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/operate/reimburseSystem/queryTravelExpenseByid";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            if (JzbCheckParam.haveEmpty(param, new String[]{"pagesize", "pageno","travelid" })) {
                response = Response.getResponseError();
            } else {
                JzbPageConvert.setPageRows(param);
                param.put("travelid",param.get("travelid").toString().trim());
                // 获取报销单详情
                List<Map<String, Object>> expList = tbTravelExpenseService.queryTravelExpenseByid(param);
                response = Response.getResponseSuccess(userInfo);
                PageInfo pageInfo = new PageInfo();
                pageInfo.setList(expList);
                response.setPageInfo(pageInfo);
            }
        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "queryTravelExpenseByid Method", ex.toString()));
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
     * @Author sapientia
     * @Date 18:03 2019/12/12
     * @Description  设置撤回状态
     **/
    @RequestMapping(value = "/setRecallStatus",method = RequestMethod.POST)
    @CrossOrigin
    @Transactional
    public Response setRecallStatus(@RequestBody Map<String, Object> param) {
        Response response;

        Map<String, Object> userInfo = null;
        String api = "/operate/reimburseSystem/setRecallStatus";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            if (JzbCheckParam.haveEmpty(param, new String[]{"travelid"})) {
                response = Response.getResponseError();
            } else {
                param.put("rebversion",JzbRandom.getRandom(8));
                response = tbTravelService.setRecallStatus(param) > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
            }
        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "setRecallStatus Method", ex.toString()));
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
     * @Author sapientia
     * @Date 9:34 2019/12/12
     * @Description 根据单位id获取资料
     **/
    @RequestMapping(value = "/queryDetaBycid",method = RequestMethod.POST)
    @CrossOrigin
    @Transactional
    public Response queryDetaBycid(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/operate/reimburseSystem/queryDetaBycid";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            if (JzbCheckParam.haveEmpty(param, new String[]{"cid"})) {
                response = Response.getResponseError();
            } else {
                param.put("uid",userInfo.get("uid"));
                // 根据cid获取出差信息
                List<Map<String,Object>> list = tbTravelService.queryDetaBycid(param);
               for(int i = 0, a =list.size();i < a;i++){
                   Map<String,Object> daMap = new HashMap<>();
                   daMap.put("did",list.get(i).get("did").toString().trim());
                   List<Map<String,Object>> daList = tbTravelService.queryTravelData(daMap);
                   list.get(i).put("daList",daList);
               }
                response = Response.getResponseSuccess(userInfo);
                PageInfo pageInfo = new PageInfo();
                pageInfo.setList(list);
                response.setPageInfo(pageInfo);
            }
        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "queryDetaBycid Method", ex.toString()));
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
     * @Author sapientia
     * @Date 14:09 2019/12/13
     * @Description 根据用户id查询出差数据
     **/
    @RequestMapping(value = "/queryTravelDetaByuid",method = RequestMethod.POST)
    @CrossOrigin
    @Transactional
    public Response queryTravelDetaByuid(@RequestBody Map<String, Object> param){
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/operate/reimburseSystem/queryTravelDetaByuid";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            if (JzbCheckParam.haveEmpty(param, new String[]{ "pagesize","pageno","cid"})) {
                response = Response.getResponseError();
            } else {
                JzbPageConvert.setPageRows(param);
                param.put("uid",userInfo.get("uid"));
                List<Map<String,Object>> list = tbTravelService.queryTravelList(param);
                for(int i = 0, a =list.size();i < a;i++){
                    if(!JzbTools.isEmpty(list.get(i).get("projectid"))) {
                        Map<String, Object> daMap = new HashMap<>();
                        daMap.put("projectid", list.get(i).get("projectid"));
                        daMap.put("userinfo", userInfo);
                        Response res = newTbCompanyListApi.queryPronameByid(daMap);
                        List<Map<String, Object>> proList = res.getPageInfo().getList();
                        list.get(i).put("proList", proList);
                        list.get(i).put("uname", userInfo.get("cname"));
                    }
                }
                response = Response.getResponseSuccess(userInfo);
                PageInfo pageInfo = new PageInfo();
                pageInfo.setList(list);
                pageInfo.setTotal(tbTravelService.countTravelList(param));
                response.setPageInfo(pageInfo);
            }
        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "queryDetaBycid Method", ex.toString()));
        }
        if (userInfo != null) {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
                    userInfo.get("msgTag").toString(), "User Login Message"));
        } else {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
        }
        return response;
    }


}
