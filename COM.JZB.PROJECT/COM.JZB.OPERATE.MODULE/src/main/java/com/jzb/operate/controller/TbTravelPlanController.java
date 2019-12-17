package com.jzb.operate.controller;

import com.alibaba.fastjson.JSONArray;
import com.jzb.base.data.JzbDataType;
import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbPageConvert;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.operate.api.base.RegionBaseApi;
import com.jzb.operate.api.org.DeptOrgApi;
import com.jzb.operate.api.org.NewTbCompanyListApi;
import com.jzb.operate.api.org.TbDeptUserListApi;
import com.jzb.operate.service.*;
import com.jzb.operate.util.PrindexUtil;
import com.jzb.operate.vo.ApproverVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import sun.security.krb5.internal.tools.Ktab;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author ：Champ-Ping
 * @date ：Created in 2019-12-03 10:14
 * @description：出差计划
 * @modified By：
 * @version: 1.0$
 */
@RestController
@RequestMapping(value = "/operate/travelPlan")
public class TbTravelPlanController {

    @Autowired
    TbTravelPlanService travelPlanService;
    @Autowired
    DeptOrgApi deptOrgApi;
    @Autowired
    TbTravelInfoService travelInfoService;
    @Autowired
    TbTravelDataService travelDataService;
    @Autowired
    RegionBaseApi regionBaseApi;
    @Autowired
    TbTravelProduceService travelProduceService;

    @Autowired
    TbDeptUserListApi tbDeptUserListApi;

    @Autowired
    NewTbCompanyListApi newTbCompanyListApi;

    @Autowired
    TbTravelApprovalService tbTravelApprovalService;

    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(TbTravelPlanController.class);

    /**
     * 根据用户名或电话号码 获取同行人 (貌似为废弃方法)
     *
     * @param param
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/getPeers", method = RequestMethod.POST)
    public Response getTravelpeers(@RequestBody Map<String, Object> param) {
        Response response = null;
        return deptOrgApi.getDeptUser(param);
    }


    /**
     * 获取预计产出列表
     *
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/getProduceList", method = RequestMethod.POST)
    public Response getProduceList() {
        Response response;
        try {
            PageInfo pageInfo = new PageInfo();
            List<Map<String, Object>> produceList = travelProduceService.list(null);
            long count = travelProduceService.count(null);
            pageInfo.setList(produceList);
            pageInfo.setTotal(count);
            response = Response.getResponseSuccess();
            response.setPageInfo(pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
            response = Response.getResponseError();
        }
        return response;
    }

    /**
     * @author: gongWei
     * @Date: 2019/12/17 10:11
     * @Description: 添加出差计划
     * @Param:
     * @Return:
     * @Exception:
     */
    @CrossOrigin
    @Transactional
    @RequestMapping(value = "/addTravelPlan", method = RequestMethod.POST)
    public Response addTravelRecord(@RequestBody Map<String, Object> param) {

        Response response;
        Map<String, Object> userInfo = null;
        String api = "/operate/travelPlan/addTravelPlan";
        boolean flag = true;

        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }

            // 获取参数中的出差详情list
            List<Map<String, Object>> detailsList = (List) param.get("list");

            //出差记录
            param.put("adduid", userInfo.get("uid"));
            param.put("travelid", JzbRandom.getRandomChar(19));
            param.put("aptype", 1);//1出差 2 报销
            param.put("version", JzbRandom.getRandom(8));
            param.put("status", '1');//默认状态'1'

            //始末时间默认为第一条记录的时间

            long temp = getTimestamp(JzbDataType.getString(detailsList.get(0).get("trtime")));
            long startTime = temp;
            long endTime = temp;

            //遍历细节集合
//            for (Map<String, Object> detailsMap : detailsList) {
//
//                detailsMap.put("deid", JzbRandom.getRandomChar(19));
//                detailsMap.put("travelid", param.get("travelid"));
//                detailsMap.put("uid", param.get("uid"));
//                detailsMap.put("addtime", System.currentTimeMillis());
//                detailsMap.put("status", 1);//默认状态1
//                // prindex加密处理
//
//                List<Integer> prindexLst = (List<Integer>) detailsMap.get("produce");
//                detailsMap.put("produce", PrindexUtil.setPrindex(prindexLst));
//
//                //出差详情--出差时间转化处理
//                long trTime = getTimestamp(JzbDataType.getString(detailsMap.get("trtime")));
//                detailsMap.put("trtime", trTime);
//                //统计始末时间
//                startTime = startTime < trTime ? startTime : trTime;
//                endTime = endTime > trTime ? endTime : trTime;
//
//                //获取并保存情报收集list
//                List<Map<String, Object>> travelinfolist = (List<Map<String, Object>>) detailsMap.get("travelinfolist");
//                //一般travelinfolist的长度为1
//                for (Map<String, Object> infoMap : travelinfolist) {
//                    infoMap.put("adduid", param.get("adduid"));
//                    infoMap.put("travelid", param.get("travelid"));
//                    infoMap.put("deid", detailsMap.get("deid"));
//                    infoMap.put("reshid", null); // 此除应为前端传参,现因 项目关系人来源暂不明确,故暂存null值处理
//                    infoMap.put("status", 1);//默认状态1
//                    infoMap.put("inid", JzbRandom.getRandomChar(19));
//                    infoMap.put("addtime", System.currentTimeMillis());
//                    travelInfoService.save(infoMap);
//                }
//
//                //获取并保存出差资料list
//                List<Map<String, Object>> traveldatalist = (List<Map<String, Object>>) detailsMap.get("traveldatalist");
//                for (Map<String, Object> dataMap : traveldatalist) {
//                    dataMap.put("coou", JSONArray.toJSONString(dataMap.get("coou")));
//                    dataMap.put("coppt", JSONArray.toJSONString(dataMap.get("coppt")));
//                    dataMap.put("couage", JSONArray.toJSONString(dataMap.get("couage")));
//                    dataMap.put("cocustomer", JSONArray.toJSONString(dataMap.get("cocustomer")));
//                    dataMap.put("coframe", JSONArray.toJSONString(dataMap.get("coframe")));
//                    dataMap.put("copropaganda", JSONArray.toJSONString(dataMap.get("copropaganda")));
//                    dataMap.put("contrast", JSONArray.toJSONString(dataMap.get("contrast")));
//                    dataMap.put("card", JSONArray.toJSONString(dataMap.get("card")));
//                    dataMap.put("account", JSONArray.toJSONString(dataMap.get("account")));
//                    dataMap.put("speechcraft", JSONArray.toJSONString(dataMap.get("speechcraft")));
//                    dataMap.put("signin", JSONArray.toJSONString(dataMap.get("signin")));
//                    dataMap.put("newsletter", JSONArray.toJSONString(dataMap.get("newsletter")));
//                    dataMap.put("train", JSONArray.toJSONString(dataMap.get("train")));
//                    dataMap.put("implement", JSONArray.toJSONString(dataMap.get("implement")));
//                    dataMap.put("offer", JSONArray.toJSONString(dataMap.get("offer")));
//                    dataMap.put("plan", JSONArray.toJSONString(dataMap.get("plan")));
//                    dataMap.put("invitation", JSONArray.toJSONString(dataMap.get("invitation")));
//                    dataMap.put("reviewed", JSONArray.toJSONString(dataMap.get("reviewed")));
//                    dataMap.put("cureviewed", JSONArray.toJSONString(dataMap.get("cureviewed")));
//                    dataMap.put("contract", JSONArray.toJSONString(dataMap.get("contract")));
//                    dataMap.put("adduid", param.get("adduid"));
//                    dataMap.put("travelid", param.get("travelid"));
//                    dataMap.put("deid", detailsMap.get("deid"));
//                    dataMap.put("did", JzbRandom.getRandomChar(19));
//                    dataMap.put("addtime", System.currentTimeMillis());
//                    dataMap.put("status", 1);//默认状态1
//
//                    travelDataService.save(dataMap);
//                }
//            }
            for (int i = 0, a = detailsList.size(); i < a; i++) {

                detailsList.get(i).put("deid", JzbRandom.getRandomChar(19));
                detailsList.get(i).put("travelid", param.get("travelid"));
                detailsList.get(i).put("uid", param.get("uid"));
                detailsList.get(i).put("addtime", System.currentTimeMillis());
                detailsList.get(i).put("status", 1);//默认状态1
                // prindex加密处理

                List<Integer> prindexLst = (List<Integer>) detailsList.get(i).get("produce");
                detailsList.get(i).put("produce", PrindexUtil.setPrindex(prindexLst));

                //出差详情--出差时间转化处理
                long trTime = getTimestamp(JzbDataType.getString(detailsList.get(i).get("trtime")));
                detailsList.get(i).put("trtime", trTime);
                //统计始末时间
                startTime = startTime < trTime ? startTime : trTime;
                endTime = endTime > trTime ? endTime : trTime;

                //获取并保存情报收集list
                List<Map<String, Object>> travelinfolist = (List<Map<String, Object>>) detailsList.get(i).get("travelinfolist");
                //一般travelinfolist的长度为1
                for (int j = 0, b = travelinfolist.size(); j < b; j++) {
                    travelinfolist.get(j).put("adduid", param.get("adduid"));
                    travelinfolist.get(j).put("travelid", param.get("travelid"));
                    travelinfolist.get(j).put("deid", detailsList.get(i).get("deid"));
                    travelinfolist.get(j).put("reshid", null); // 此除应为前端传参,现因 项目关系人来源暂不明确,故暂存null值处理
                    travelinfolist.get(j).put("status", 1);//默认状态1
                    travelinfolist.get(j).put("inid", JzbRandom.getRandomChar(19));
                    travelinfolist.get(j).put("addtime", System.currentTimeMillis());
                    travelInfoService.save(travelinfolist.get(j));
                }

                //获取并保存出差资料list
                List<Map<String, Object>> travelDataList = (List<Map<String, Object>>) detailsList.get(i).get("traveldatalist");
                for (int k = 0, c = travelDataList.size(); k < c; k++) {
                    travelDataList.get(k).put("coou", JSONArray.toJSONString(travelDataList.get(k).get("coou")));
                    travelDataList.get(k).put("coppt", JSONArray.toJSONString(travelDataList.get(k).get("coppt")));
                    travelDataList.get(k).put("couage", JSONArray.toJSONString(travelDataList.get(k).get("couage")));
                    travelDataList.get(k).put("cocustomer", JSONArray.toJSONString(travelDataList.get(k).get("cocustomer")));
                    travelDataList.get(k).put("coframe", JSONArray.toJSONString(travelDataList.get(k).get("coframe")));
                    travelDataList.get(k).put("copropaganda", JSONArray.toJSONString(travelDataList.get(k).get("copropaganda")));
                    travelDataList.get(k).put("contrast", JSONArray.toJSONString(travelDataList.get(k).get("contrast")));
                    travelDataList.get(k).put("card", JSONArray.toJSONString(travelDataList.get(k).get("card")));
                    travelDataList.get(k).put("account", JSONArray.toJSONString(travelDataList.get(k).get("account")));
                    travelDataList.get(k).put("speechcraft", JSONArray.toJSONString(travelDataList.get(k).get("speechcraft")));
                    travelDataList.get(k).put("signin", JSONArray.toJSONString(travelDataList.get(k).get("signin")));
                    travelDataList.get(k).put("newsletter", JSONArray.toJSONString(travelDataList.get(k).get("newsletter")));
                    travelDataList.get(k).put("train", JSONArray.toJSONString(travelDataList.get(k).get("train")));
                    travelDataList.get(k).put("implement", JSONArray.toJSONString(travelDataList.get(k).get("implement")));
                    travelDataList.get(k).put("offer", JSONArray.toJSONString(travelDataList.get(k).get("offer")));
                    travelDataList.get(k).put("plan", JSONArray.toJSONString(travelDataList.get(k).get("plan")));
                    travelDataList.get(k).put("invitation", JSONArray.toJSONString(travelDataList.get(k).get("invitation")));
                    travelDataList.get(k).put("reviewed", JSONArray.toJSONString(travelDataList.get(k).get("reviewed")));
                    travelDataList.get(k).put("cureviewed", JSONArray.toJSONString(travelDataList.get(k).get("cureviewed")));
                    travelDataList.get(k).put("contract", JSONArray.toJSONString(travelDataList.get(k).get("contract")));
                    travelDataList.get(k).put("adduid", param.get("adduid"));
                    travelDataList.get(k).put("travelid", param.get("travelid"));
                    travelDataList.get(k).put("deid", detailsList.get(i).get("deid"));
                    travelDataList.get(k).put("did", JzbRandom.getRandomChar(19));
                    travelDataList.get(k).put("addtime", System.currentTimeMillis());
                    travelDataList.get(k).put("status", 1);//默认状态1
                    travelDataService.save(travelDataList.get(k));
                }

            }

            //设置出差记录的时间域
            param.put("orgtime", startTime);
            param.put("endtime", endTime);

            // 添加出差细节
            travelPlanService.addTravelDetails(detailsList);

            //  添加出差记录
            travelPlanService.addTravelRecord(param);
            response = Response.getResponseSuccess(userInfo);

        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "addTravelPlan Method", ex.toString()));
        }

        if (userInfo != null) {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
                    userInfo.get("msgTag").toString(), "User Login Message"));
        } else {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
        }

        return response;
    }

    // 时间插件特殊无法传时间戳 后端做日期转时间戳处理
    private long getTimestamp(String str) throws ParseException {
        String dateStr = str;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse(dateStr);
        return date.getTime();
    }

    /**
     * 撤回 status = 4
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/setRecallStatus", method = RequestMethod.POST)
    @CrossOrigin
    @Transactional
    public Response setBackStatus(@RequestBody Map<String, Object> param) {
        Response response;

        Map<String, Object> userInfo = null;
        String api = "/operate/travelPlan/setRecallStatus";
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
                param.put("status", 4);
                param.put("version", JzbRandom.getRandom(8));
                response = travelPlanService.updateTravelRecord(param) > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
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
     * 设置删除状态 status = 2
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/setDeleteStatus", method = RequestMethod.POST)
    @CrossOrigin
    @Transactional
    public Response setDeleteStatus(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/operate/travelPlan/setDeleteStatus";
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
                travelInfoService.setStatusByTravelid(param); // 出差情报表删除
                travelDataService.setStatusByTravelid(param); // 出差资料表删除
                travelPlanService.setDetailsStatusByTravelid(param); // 出差记录详情表删除
                travelPlanService.setDeleteStatus(param); // 出差记录表删除
                response = travelPlanService.setDeleteStatus(param) > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
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
     * 修改出差记录
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/updateTravelPlan", method = RequestMethod.POST)
    @CrossOrigin
    @Transactional
    public Response updateTravelRecord(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/operate/travelPlan/updateTravelPlan";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            // 获取参数中的出差详情list
            List<Map<String, Object>> detailsList = (List) param.get("list");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            //统计时间// 始末时间默认为第一条记录的时间
            long temp = sdf.parse(JzbDataType.getString(detailsList.get(0).get("trtime"))).getTime(); //前端时间组件需要特殊处理一下

            long startTime = temp;
            long endTime = temp;
//            for (Map<String, Object> detailsMap : detailsList) {
//                long trTime = sdf.parse(JzbDataType.getString(detailsMap.get("trtime"))).getTime();
//
//                //统计始末时间
//                startTime = startTime < trTime ? startTime : trTime;
//                endTime = endTime > trTime ? endTime : trTime;
//                // prindex加密处理
//
//                List<Integer> prindexLst = (List<Integer>) detailsMap.get("produce");
//                detailsMap.put("trtime", trTime);
//                detailsMap.put("produce", PrindexUtil.setPrindex(prindexLst));
//
//                travelPlanService.updateTravelDetials(detailsMap);
//
//                //获取并保存情报收集list
//                List<Map<String, Object>> travelinfolist = (List<Map<String, Object>>) detailsMap.get("travelinfolist");
//                //一般travelinfolist的长度为1
//                for (Map<String, Object> infoMap : travelinfolist) {
//                    infoMap.put("updtime", System.currentTimeMillis());
//                    infoMap.put("deid", detailsMap.get("deid"));
//                    travelInfoService.update(infoMap);
//                }
//
//                //获取并保存出差资料list
//                List<Map<String, Object>> traveldatalist = (List<Map<String, Object>>) detailsMap.get("traveldatalist");
//                for (Map<String, Object> dataMap : traveldatalist) {
//                    dataMap.put("coou", JSONArray.toJSONString(dataMap.get("coou")));
//                    dataMap.put("coppt", JSONArray.toJSONString(dataMap.get("coppt")));
//                    dataMap.put("couage", JSONArray.toJSONString(dataMap.get("couage")));
//                    dataMap.put("cocustomer", JSONArray.toJSONString(dataMap.get("cocustomer")));
//                    dataMap.put("coframe", JSONArray.toJSONString(dataMap.get("coframe")));
//                    dataMap.put("copropaganda", JSONArray.toJSONString(dataMap.get("copropaganda")));
//                    dataMap.put("contrast", JSONArray.toJSONString(dataMap.get("contrast")));
//                    dataMap.put("card", JSONArray.toJSONString(dataMap.get("card")));
//                    dataMap.put("account", JSONArray.toJSONString(dataMap.get("account")));
//                    dataMap.put("speechcraft", JSONArray.toJSONString(dataMap.get("speechcraft")));
//                    dataMap.put("signin", JSONArray.toJSONString(dataMap.get("signin")));
//                    dataMap.put("newsletter", JSONArray.toJSONString(dataMap.get("newsletter")));
//                    dataMap.put("train", JSONArray.toJSONString(dataMap.get("train")));
//                    dataMap.put("implement", JSONArray.toJSONString(dataMap.get("implement")));
//                    dataMap.put("offer", JSONArray.toJSONString(dataMap.get("offer")));
//                    dataMap.put("plan", JSONArray.toJSONString(dataMap.get("plan")));
//                    dataMap.put("invitation", JSONArray.toJSONString(dataMap.get("invitation")));
//                    dataMap.put("reviewed", JSONArray.toJSONString(dataMap.get("reviewed")));
//                    dataMap.put("cureviewed", JSONArray.toJSONString(dataMap.get("cureviewed")));
//                    dataMap.put("contract", JSONArray.toJSONString(dataMap.get("contract")));
//                    dataMap.put("updtime", System.currentTimeMillis());
//                    dataMap.put("deid", detailsMap.get("deid"));
//                    travelDataService.update(dataMap);
//                }
//            }

            for (int i = 0, a = detailsList.size(); i < a; i++) {
                long trTime = sdf.parse(JzbDataType.getString(detailsList.get(i).get("trtime"))).getTime();

                //统计始末时间
                startTime = startTime < trTime ? startTime : trTime;
                endTime = endTime > trTime ? endTime : trTime;
                // prindex加密处理

                List<Integer> prindexLst = (List<Integer>) detailsList.get(i).get("produce");
                detailsList.get(i).put("trtime", trTime);
                detailsList.get(i).put("produce", PrindexUtil.setPrindex(prindexLst));

                travelPlanService.updateTravelDetials(detailsList.get(i));

                //获取并保存情报收集list
                List<Map<String, Object>> travelInfoList = (List<Map<String, Object>>) detailsList.get(i).get("travelinfolist");
                //一般travelinfolist的长度为1
                for (int j = 0, b = travelInfoList.size(); j < b; j++) {
                    travelInfoList.get(j).put("updtime", System.currentTimeMillis());
                    travelInfoList.get(j).put("deid", detailsList.get(i).get("deid"));
                    travelInfoService.update(travelInfoList.get(j));
                }

                //获取并保存出差资料list
                List<Map<String, Object>> travelDataList = (List<Map<String, Object>>) detailsList.get(i).get("traveldatalist");
                for (int k = 0, c = travelDataList.size(); k < c; k++) {
                    travelDataList.get(k).put("coou", JSONArray.toJSONString(travelDataList.get(k).get("coou")));
                    travelDataList.get(k).put("coppt", JSONArray.toJSONString(travelDataList.get(k).get("coppt")));
                    travelDataList.get(k).put("couage", JSONArray.toJSONString(travelDataList.get(k).get("couage")));
                    travelDataList.get(k).put("cocustomer", JSONArray.toJSONString(travelDataList.get(k).get("cocustomer")));
                    travelDataList.get(k).put("coframe", JSONArray.toJSONString(travelDataList.get(k).get("coframe")));
                    travelDataList.get(k).put("copropaganda", JSONArray.toJSONString(travelDataList.get(k).get("copropaganda")));
                    travelDataList.get(k).put("contrast", JSONArray.toJSONString(travelDataList.get(k).get("contrast")));
                    travelDataList.get(k).put("card", JSONArray.toJSONString(travelDataList.get(k).get("card")));
                    travelDataList.get(k).put("account", JSONArray.toJSONString(travelDataList.get(k).get("account")));
                    travelDataList.get(k).put("speechcraft", JSONArray.toJSONString(travelDataList.get(k).get("speechcraft")));
                    travelDataList.get(k).put("signin", JSONArray.toJSONString(travelDataList.get(k).get("signin")));
                    travelDataList.get(k).put("newsletter", JSONArray.toJSONString(travelDataList.get(k).get("newsletter")));
                    travelDataList.get(k).put("train", JSONArray.toJSONString(travelDataList.get(k).get("train")));
                    travelDataList.get(k).put("implement", JSONArray.toJSONString(travelDataList.get(k).get("implement")));
                    travelDataList.get(k).put("offer", JSONArray.toJSONString(travelDataList.get(k).get("offer")));
                    travelDataList.get(k).put("plan", JSONArray.toJSONString(travelDataList.get(k).get("plan")));
                    travelDataList.get(k).put("invitation", JSONArray.toJSONString(travelDataList.get(k).get("invitation")));
                    travelDataList.get(k).put("reviewed", JSONArray.toJSONString(travelDataList.get(k).get("reviewed")));
                    travelDataList.get(k).put("cureviewed", JSONArray.toJSONString(travelDataList.get(k).get("cureviewed")));
                    travelDataList.get(k).put("contract", JSONArray.toJSONString(travelDataList.get(k).get("contract")));
                    travelDataList.get(k).put("updtime", System.currentTimeMillis());
                    travelDataList.get(k).put("deid", detailsList.get(i).get("deid"));

                    travelDataService.save(travelDataList.get(k));
                }

            }
            //设置始末时间
            param.put("orgtime", startTime);
            param.put("endtime", endTime);
            response = travelPlanService.updateTravelRecord(param) > 0 ? Response.getResponseSuccess((Map<String, Object>) param.get("userinfo")) : Response.getResponseError();

        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "updateTravelPlan Method", ex.toString()));
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
     * 根据Travelid查询出差记录
     */
    @CrossOrigin
    @RequestMapping(value = "/getTravelPlanByTravelid", method = RequestMethod.POST)
    public Response queryTravelPlan(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/operate/travelPlan/getTravelPlanByTravelid";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            Map<String, Object> travelMap = travelPlanService.queryTravelRecordByTravelid(param);
            List<Map<String, Object>> detailsList = travelPlanService.queryTravelDetailsByTravelid(param);
//            travelMap.put("list", detailsList);
//
//            for (Map<String, Object> detialsMap : detailsList) {
//                Map<String, Object> query = new HashMap<>();
//                query.put("travelid", param.get("travelid"));
//                query.put("deid", detialsMap.get("deid"));
//                // 出差区域
//                query.put("region", detialsMap.get("trregion"));
//                Response resApi = regionBaseApi.getRegionInfo(query);
//                detialsMap.put("trregion", resApi.getResponseEntity());
//                //情报收集
//                detialsMap.put("travelinfolist", travelInfoService.list(query));
//                //出差资料
//                detialsMap.put("traveldatalist", travelDataService.list(query));
//                //预计产出
//                Integer produce = detialsMap.get("produce") == null ? 0 : (Integer) detialsMap.get("produce");
//                List<Map<String, Object>> produceMaps = travelProduceService.list(null);
//                List<Integer> produceList = PrindexUtil.getPrindex(produce, produceMaps);
//                detialsMap.put("produceList", produceList);
//            }

            for (int i = 0, a = detailsList.size(); i < a; i++) {
                Map<String, Object> query = new HashMap<>();
                query.put("travelid", param.get("travelid"));
                query.put("deid", detailsList.get(i).get("deid"));
                // 出差区域
                query.put("region", detailsList.get(i).get("trregion"));
                Response resApi = regionBaseApi.getRegionInfo(query);
                detailsList.get(i).put("trregion", resApi.getResponseEntity());
                //情报收集
                detailsList.get(i).put("travelinfolist", travelInfoService.list(query));
                //出差资料
                detailsList.get(i).put("traveldatalist", travelDataService.list(query));
                //预计产出
                Integer produce = detailsList.get(i).get("produce") == null ? 0 : (Integer) detailsList.get(i).get("produce");
                List<Map<String, Object>> produceMaps = travelProduceService.list(null);
                List<Integer> produceList = PrindexUtil.getPrindex(produce, produceMaps);
                detailsList.get(i).put("produceList", produceList);
            }
            travelMap.put("detailsList", detailsList);
            response = Response.getResponseSuccess(userInfo);
            response.setResponseEntity(travelMap);
        } catch (Exception e) {
            flag = false;
            JzbTools.logError(e);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getTravelPlanByTravelid Method", e.toString()));
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
     * @return com.jzb.base.message.Response
     * @Author sapientia
     * @Description 获取省市县
     * @Date 12:49
     * @Param [param]
     **/
    @CrossOrigin
    @RequestMapping(value = "/getCityList", method = RequestMethod.POST)
    public Response getCityList(@RequestBody Map<String, Object> param) {
        Response response;
        try {
            PageInfo pageInfo = new PageInfo();
            Response res = regionBaseApi.getCityJson(param);
            List<Map<String, Object>> cityList = res.getPageInfo().getList();
            pageInfo.setList(cityList);
            response = Response.getResponseSuccess((Map<String, Object>) param.get("userinfo"));
            response.setPageInfo(pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
            response = Response.getResponseError();
        }
        return response;
    }

    /**
     * @author: gongWei
     * @Date: 2019/12/11 10:59
     * @Description: 获取同行人列表信息
     * @Param:
     * @Return:
     * @Exception:
     */
    @RequestMapping(value = "/queryUsernameBydept", method = RequestMethod.POST)
    @CrossOrigin
    public Response queryUsernameBydept(@RequestBody Map<String, Object> param) {
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

            //获取同行人列表
            Response res = tbDeptUserListApi.queryUsernameBydept(param);
            response = Response.getResponseSuccess(userInfo);
            response.setResponseEntity(res.getPageInfo().getList());
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
     * 根据uid 获取出差申请记录
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getTravelRecordList", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response getTravelRecordList(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/operate/travelRecord/getTravelRecordList";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                param.put("uid", userInfo.get("uid"));
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }

            if (JzbCheckParam.allNotEmpty(param, new String[]{"uid", "pagesize", "pageno"})) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                // 设置分页
                JzbPageConvert.setPageRows(param);
                // 如果起始时间参数不为空则转为时间戳
                if (!JzbTools.isEmpty(param.get("beginTime"))) {
                    Date beginTime = sdf.parse(JzbDataType.getString(param.get("beginTime")));
                    param.put("beginTime", beginTime.getTime());
                }
                if (!JzbTools.isEmpty(param.get("endTime"))) {
                    Date beginTime = sdf.parse(JzbDataType.getString(param.get("endTime")));
                    param.put("endTime", beginTime.getTime());
                }
                // 得到出差记录结果集
                List<Map<String, Object>> recordList = travelPlanService.getTravelRecordListByUid(param);
                Response resApi;
//                for (Map<String, Object> travelMap : recordList) {
//                    // 1.根据查询出来的truids(审批人员id集合)调用tbDeptUserListApi查询审批人员名称用于界面展示
//                    Map<String, Object> whereParam = new HashMap<>();
//                    if (ObjectUtils.isEmpty(travelMap.get("truids"))) {
//                        travelMap.put("approvers", "未申请");
//                    } else {
//                        whereParam.put("userinfo", param.get("userinfo"));
//                        whereParam.put("uids", travelMap.get("truids"));
//                        resApi = tbDeptUserListApi.searchInvitee(whereParam);
//                        String unameStr = (String) resApi.getResponseEntity();
//                        String[] unames = unameStr.split(",");
//                        String[] trStatus = travelMap.get("trstatus").toString().split(",");
//                        List<ApproverVO> approverVOList = new ArrayList<>();
//                        for (int i = 0; i < unames.length; i++) {
//                            approverVOList.add(new ApproverVO(unames[i], trStatus[i]));
//                        }
//                        travelMap.put("approvers", approverVOList);
//                    }
//
//                    // 2.根据查询出来的travelid 查询 出差记录详情
//                    whereParam.put("travelid", travelMap.get("travelid"));
//                    List<Map<String, Object>> detailsList = travelPlanService.queryTravelDetailsByTravelid(whereParam);
//
//                    for (Map<String, Object> detialsMap : detailsList) {
//                        Map<String, Object> query = new HashMap<>();
//                        query.put("userinfo", param.get("userinfo"));
//                        // 获取同行人名称
//                        if (ObjectUtils.isEmpty(detialsMap.get("trpeers"))) {
//                            detialsMap.put("trpeers", "");
//                        } else {
//                            query.put("uids", detialsMap.get("trpeers"));
//                            resApi = tbDeptUserListApi.searchInvitee(query);
//                            String trpeers = (String) resApi.getResponseEntity();
//                            detialsMap.put("trpeers", trpeers);
//                        }
//                        // 出差区域
//                        query.put("region", detialsMap.get("trregion"));
//                        resApi = regionBaseApi.getRegionInfo(query);
//                        detialsMap.put("travelCity", resApi.getResponseEntity());
//                        //获取单位名称
//                        if (ObjectUtils.isEmpty(detialsMap.get("cid"))) {
//                            detialsMap.put("clist", null);
//                        } else {
//                            query.put("cid", detialsMap.get("cid"));
//                            resApi = newTbCompanyListApi.queryCompanyNameBycid(query);
//                            List<Map<String, Object>> calist = resApi.getPageInfo().getList();
//                            detialsMap.put("clist", calist);
//                        }
//
//                        query.put("travelid", detialsMap.get("travelid"));
//                        query.put("deid", detialsMap.get("deid"));
//                        //情报收集
//                        detialsMap.put("travelinfolist", travelInfoService.list(query));
//                        //出差资料
//                        detialsMap.put("traveldatalist", travelDataService.list(query));
//                        //预计产出
//                        Integer produce = detialsMap.get("produce") == null ? 0 : (Integer) detialsMap.get("produce");
//                        List<Map<String, Object>> produceMaps = travelProduceService.list(null);
//                        List<Integer> produceList = PrindexUtil.getPrindex(produce, produceMaps);
//                        //筛选过滤 获取出差详情的产出资料
//                        List<Map<String, Object>> selectedProduce = new ArrayList<>();
//                        for (Map<String, Object> map : produceMaps) {
//                            Integer prindex = (Integer) map.get("prindex");
//                            for (Integer i : produceList) {
//                                if (i == prindex) {
//                                    selectedProduce.add(map);
//                                    break;
//                                }
//                            }
//                        }
//                        detialsMap.put("produceList", selectedProduce);
//                    }
//
//                    travelMap.put("children", detailsList);
//
//                }

                for (int i = 0, a = recordList.size(); i < a; i++) {
                    Map<String, Object> whereParam = new HashMap<>();
                    // 1.根据查询出来的truids(审批人员id集合)调用tbDeptUserListApi查询审批人员名称用于界面展示
                    if (ObjectUtils.isEmpty(recordList.get(i).get("truids"))) {
                        recordList.get(i).put("approvers", null);
                    } else {
                        whereParam.put("userinfo", param.get("userinfo"));
                        whereParam.put("uids", recordList.get(i).get("truids"));
                        resApi = tbDeptUserListApi.searchInvitee(whereParam);
                        String unameStr = (String) resApi.getResponseEntity();
                        String[] unames = unameStr.split(",");
                        String[] trStatus = recordList.get(i).get("trstatus").toString().split(",");
                        String[] idxs = recordList.get(i).get("idxs").toString().split(",");
                        List<ApproverVO> approverVOList = new ArrayList<>();
                        for (int h = 0; h < trStatus.length; h++) {
                            approverVOList.add(new ApproverVO(unames[h], trStatus[h].trim(), JzbDataType.getInteger(idxs[h].trim())));
                        }
                        // 按idx排序
                        Collections.sort(approverVOList, Comparator.comparing(ApproverVO::getIdx));
                        System.out.println(approverVOList);
                        recordList.get(i).put("approvers", approverVOList);
                    }
                    // 2.根据查询出来的travelid 查询 出差记录详情
                    whereParam.put("travelid", recordList.get(i).get("travelid"));
                    List<Map<String, Object>> detailsList = travelPlanService.queryTravelDetailsByTravelid(whereParam);
                    for (int j = 0, b = detailsList.size(); j < b; j++) {
                        Map<String, Object> query = new HashMap<>();
                        query.put("userinfo", param.get("userinfo"));
                        // 获取同行人名称
                        if (ObjectUtils.isEmpty(detailsList.get(j).get("trpeers"))) {
                            detailsList.get(j).put("trpeers", "");
                        } else {
                            query.put("uids", detailsList.get(j).get("trpeers"));
                            resApi = tbDeptUserListApi.searchInvitee(query);
                            String trpeers = (String) resApi.getResponseEntity();
                            detailsList.get(j).put("trpeers", trpeers);
                        }
                        // 出差区域
                        query.put("region", detailsList.get(j).get("trregion"));
                        resApi = regionBaseApi.getRegionInfo(query);
                        // Map<String, Object> regionID = (Map<String, Object>) resApi.getResponseEntity();

                        detailsList.get(j).put("travelCity", resApi.getResponseEntity());
                        //获取单位名称
                        if (ObjectUtils.isEmpty(detailsList.get(j).get("cid"))) {
                            detailsList.get(j).put("clist", null);
                        } else {
                            query.put("cid", detailsList.get(j).get("cid"));
                            resApi = newTbCompanyListApi.queryCompanyNameBycid(query);
                            List<Map<String, Object>> calist = resApi.getPageInfo().getList();
                            detailsList.get(j).put("clist", calist);
                        }

                        query.put("travelid", detailsList.get(j).get("travelid"));
                        query.put("deid", detailsList.get(j).get("deid"));
                        //情报收集
                        detailsList.get(j).put("travelinfolist", travelInfoService.list(query));
                        //出差资料
                        detailsList.get(j).put("traveldatalist", travelDataService.list(query));
                        //预计产出
                        Integer produce = detailsList.get(j).get("produce") == null ? 0 : (Integer) detailsList.get(j).get("produce");
                        List<Map<String, Object>> produceMaps = travelProduceService.list(null);
                        List<Integer> produceList = PrindexUtil.getPrindex(produce, produceMaps);
                        //筛选过滤 获取出差详情的产出资料
                        List<Map<String, Object>> selectedProduce = new ArrayList<>();
                        for (Map<String, Object> map : produceMaps) {
                            Integer prindex = (Integer) map.get("prindex");
                            for (Integer k : produceList) {
                                if (k == prindex) {
                                    selectedProduce.add(map);
                                    break;
                                }
                            }
                        }

                        detailsList.get(j).put("produceList", selectedProduce);
                    }
                    recordList.get(i).put("children", detailsList);
                }
                // 得到总数
                int count = travelPlanService.getTravelRecordCountByUid(param);
                // 定义分页  pageinfo
                PageInfo pi = new PageInfo();
                pi.setList(recordList);
                pi.setTotal(count);
                // 设置userinfo
                response = Response.getResponseSuccess((Map<String, Object>) param.get("userinfo"));
                response.setPageInfo(pi);

            } else {
                logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getTravelRecordList Method", "[param error] or [param is null]"));
                response = Response.getResponseError();
            }
        } catch (Exception ex) {
            flag = false;
            // 返回错误
            JzbTools.logError(ex);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getTravelRecordList Method", ex.toString()));
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
