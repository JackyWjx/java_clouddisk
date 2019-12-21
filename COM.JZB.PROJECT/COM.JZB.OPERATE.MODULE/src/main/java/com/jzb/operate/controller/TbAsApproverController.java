package com.jzb.operate.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbPageConvert;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.operate.api.base.RegionBaseApi;
import com.jzb.operate.api.org.NewTbCompanyListApi;
import com.jzb.operate.api.org.TbDeptUserListApi;
import com.jzb.operate.api.org.TbTrackUserListApi;
import com.jzb.operate.service.TbAsApproverService;
import com.jzb.operate.service.TbTravelExpenseService;
import com.jzb.operate.service.TbTravelProduceService;
import com.jzb.operate.service.TbTravelService;
import com.jzb.operate.util.PrindexUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author sapientia
 * @Date 2019/12/18 17:46
 * @Descrpition 作为审批人登录
 */
@RestController
@RequestMapping(value = "/operate/approvalSystem")
public class TbAsApproverController {

    @Autowired
    private TbAsApproverService tbAsApproverService;

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
    private TbTravelProduceService travelProduceService;

    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(TbTravelController.class);
    /**
     * @Author sapientia
     * @Date 17:28 2019/12/18
     * @Description 作为审批人登录接口
     **/
    @RequestMapping(value = "/queryAsApprover" ,method = RequestMethod.POST)
    @CrossOrigin
    public Response queryAsApprover(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/operate/approvalSystem/queryAsApprover";
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
                List<Map<String, Object>> list = tbAsApproverService.queryAsApprover(param);
                for(int i = 0 , a = list.size(); i < a;i++){
                    Map<String,Object> appMap =new HashMap<>();
                    appMap.put("travelid",list.get(i).get("travelid").toString().trim());
                    appMap.put("pageno",param.get("pageno"));
                    appMap.put("pagesize",param.get("pagesize"));
                    // 获取申请人的出差详情
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
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "queryAsApprover Method", ex.toString()));
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
     * @Date 18:04 2019/12/18
     * @Description 申请单
     **/
    @RequestMapping(value = "/queryTravelListBytrid" ,method = RequestMethod.POST)
    @CrossOrigin
    public Response queryTravelListBytrid(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/operate/approvalSystem/queryTravelListBytrid";
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
                param.put("travelid", param.get("travelid").toString().trim());
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
                pageInfo.setTotal(tbAsApproverService.countAsApprover(param));
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
     * @Date 18:07 2019/12/18
     * @Description  产出情况
     **/
    @RequestMapping(value = "/queryInfoListByDeid" ,method = RequestMethod.POST)
    @CrossOrigin
    public Response queryInfoListByDeid(@RequestBody Map<String, Object> param) {
        Response response = null;
        Map<String, Object> userInfo = null;
        String api = "/operate/approvalSystem/queryInfoListByDeid";
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
                // 获取出差详情
                List<Map<String, Object>> list = tbTravelService.queryTravelList(param);
                for(int i = 0,a = list.size();i < a;i++){
                    //获取情报
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
                    list.get(i).put("resList", resList);
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

    /**
     * @Author sapientia
     * @Date 18:10 2019/12/18
     * @Description 总结表
     **/
    @RequestMapping(value = "/queryTrackUserList",method = RequestMethod.POST)
    @CrossOrigin
    public Response queryTrackUserList(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/operate/approvalSystem/queryTrackUserList";
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
                List<Map<String,Object>> userList =tbAsApproverService.queryAsApprover(param);
                for(int m = 0,n = userList.size();m < n;m ++){
                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put("uid",userList.get(m).get("uid"));
                    userMap.put("travelid",param.get("travelid").toString().trim());
                    List<Map<String,Object>> list =tbTravelService.queryTravelList(userMap);
                    for (int i = 0 ,a = list.size(); i < a ; i++){
                        // 根据申请人 单位 拜访时间 查询跟进记录
                        Map<String, Object> dataMap = new HashMap();
                        dataMap.put("userinfo",userInfo);
                        dataMap.put("uid",userMap.get("uid"));
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
                        list.get(i).put("uname",userList.get(m).get("uname"));
                    }
                    userList.get(m).put("trList",list);
                }
                response = Response.getResponseSuccess(userInfo);
                PageInfo pageInfo = new PageInfo();
                pageInfo.setTotal(tbTravelService.countTravelList(param));
                pageInfo.setList(userList);
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
     * @Date 18:25 2019/12/18
     * @Description  报销单
     **/
    @RequestMapping(value = "/queryTravelExpenseByid",method = RequestMethod.POST)
    public Response queryTravelExpenseByid(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/operate/approvalSystem/queryTravelExpenseByid";
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
     * @Date 9:22 2019/12/19
     * @Description 设置审批退回状态
     **/
    @RequestMapping(value = "/setReturnStatus",method = RequestMethod.POST)
    @CrossOrigin
    @Transactional
    public Response setReturnStatus(@RequestBody Map<String, Object> param) {
        Response response;

        Map<String, Object> userInfo = null;
        String api = "/operate/approvalSystem/setReturnStatus";
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
                response = tbAsApproverService.setReturnStatus(param) > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
            }
        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "setReturnStatus Method", ex.toString()));
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
