package com.jzb.operate.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.*;
import com.jzb.operate.api.base.RegionBaseApi;
import com.jzb.operate.api.message.OptMsgApi;
import com.jzb.operate.api.org.DeptOrgApi;
import com.jzb.operate.api.org.NewTbCompanyListApi;
import com.jzb.operate.api.org.TbDeptUserListApi;
import com.jzb.operate.service.*;
import com.jzb.operate.util.PrindexUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author ：Champ-Ping
 * @date ：Created in 2019-12-04 20:09
 * @description：出差/报销申请
 * @modified By：
 * @version: 1.0$
 */
@RestController
@RequestMapping(value = "/operate/travelApproval")
public class TbTravelApprovalController {

    @Autowired
    private TbTravelApprovalService travelApprovalService;
    @Autowired
    private TbTravelPlanService travelPlanService;
    @Autowired
    private TbDeptUserListApi tbDeptUserListApi;

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
    NewTbCompanyListApi newTbCompanyListApi;

    @Autowired
    private OptMsgApi optMsgApi;
    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(TbTravelApprovalController.class);


    /**
     * 添加出差报销申请
     *
     * @param param trstatus审批状态: 待审批(1); 审批中(2); 审批通过(3); 退回(4)
     * @return
     */
    @CrossOrigin
    @Transactional
    @RequestMapping(value = "/addTravelApproval", method = RequestMethod.POST)
    public Response addTravelApproval(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/operate/travelApproval/addTravelApproval";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            if (JzbCheckParam.haveEmpty(param, new String[]{"list", "travelid", "version", "aptype","cid"})) {
                response = Response.getResponseError();
            } else {
                List<Map<String, Object>> approvalList = (List<Map<String, Object>>) param.get("list");
                //获取审批类型
                int apType = JzbDataType.getInteger(param.get("aptype"));

                for (int i = 0, a = approvalList.size(); i < a; i++) {
                    approvalList.get(i).put("travelid", param.get("travelid"));
                    approvalList.get(i).put("apid", JzbRandom.getRandomChar(12));
                    int idx = JzbDataType.getInteger(approvalList.get(i).get("idx"));
                    if (idx == 1) {
                        approvalList.get(i).put("trstatus", 2);
                        // 消息业务

                        String sendUid = approvalList.get(i).get("truid").toString().trim();
                        Map<String, Object> argMap = getOptMsgArg(idx, JzbDataType.getString(param.get("cid")), sendUid, apType, false, true);
                        optMsgApi.sendOptSysMsg(SendSysMsgUtil.setMsgArg(argMap));
                    } else {
                        approvalList.get(i).put("trstatus", 1);
                    }
                    approvalList.get(i).put("addtime", System.currentTimeMillis());
                    approvalList.get(i).put("adduid", userInfo.get("uid"));
                    approvalList.get(i).put("status", "1");
                    approvalList.get(i).put("version", param.get("version"));
                    travelApprovalService.save(approvalList.get(i));
                }
                // 添加抄送人
                if (!JzbTools.isEmpty(param.get("ccuid"))) {
                    List<String> ccuidList = (List<String>) param.get("ccuid");
                    param.put("ccuid", StrUtil.list2String(ccuidList, ","));
                } else {
                    param.put("ccuid", "");
                }
                if (apType == 1) {
                    param.put("trastatus", "2"); // 设置出差状态
                } else {
                    param.put("rebstatus", "2"); //设置报销状态
                }
                travelPlanService.updateTravelRecord(param);
                response = Response.getResponseSuccess(userInfo);
            }

        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "addTravelApproval Method", ex.toString()));
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
     * 同意出差申请
     * param中添加
     * "isOk" : "0" 表示退回
     * "isOk" : "1" 表示同意
     *
     * @param param
     * @return
     */
    @CrossOrigin
    @Transactional
    @PostMapping("/setTravelApproval")
    public Response setTravelApproval(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/operate/travelApproval/setTravelApproval";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }

            if (JzbCheckParam.haveEmpty(param, new String[]{"isOk", "travelid", "idx", "apid", "version", "aptype", "cid"})) {
                response = Response.getResponseError();
            } else {
                Integer isOk = (Integer) param.get("isOk");
                //获取审批类型
                Integer apType = JzbDataType.getInteger(param.get("aptype"));
                // topic主题前缀
                String cid = JzbDataType.getString(param.get("cid"));
                // 用于设置发消息的map参数
                Map<String, Object> argMap;
                if (isOk == 1) {// 同意
                    Map<String, Object> whereMap = new HashMap<>();
                    whereMap.put("trtime", System.currentTimeMillis());//审批时间
                    whereMap.put("trcomment", param.get("trcomment"));
                    whereMap.put("trstatus", 3);
                    whereMap.put("version", param.get("version"));
                    whereMap.put("apid", param.get("apid"));
                    int i = travelApprovalService.update(whereMap);
                    //判断是否是最后一个审批人
                    Map<String, Object> lastMap = travelApprovalService.getMaxIdxApid(param);
                    String lastApid = lastMap.get("apid").toString();
                    boolean isLast = param.get("apid").equals(lastApid);
                    Map<String, Object> query = new HashMap<>();
                    if (i > 0 && !isLast) { // 将下一个审批人的审批状态改为2
                        query.put("idx", (Integer) param.get("idx") + 1);
                        query.put("travelid", param.get("travelid"));
                        query.put("version", param.get("version"));
                        query.put("trstatus", 2);
                        query.put("trtime", System.currentTimeMillis());
                        travelApprovalService.update(query);

                        //发消息通知下一个审批人 审批人id
                        String sendUid = lastMap.get("truid").toString();
                        argMap = getOptMsgArg(0, cid, sendUid, apType, false, true);
                        optMsgApi.sendOptSysMsg(SendSysMsgUtil.setMsgArg(argMap));
                    } else if (i > 0 && isLast && apType == 1) { // 如果是最后是最后一个审批人,则更新rebversion(审批版本号),审批类型
                        query.put("rebversion", JzbRandom.getRandom(8));
                        query.put("aptype", 2);
                        query.put("travelid", param.get("travelid"));
                        travelPlanService.updateTravelRecord(query);

                        // 最后一个出差申请审批通过发消息通知出差申请人 申请人id
                        String sendUid = JzbDataType.getString(param.get("applyuid"));
                        argMap = getOptMsgArg(0, cid, sendUid, apType, true, true);
                        optMsgApi.sendOptSysMsg(SendSysMsgUtil.setMsgArg(argMap));
                    } else if (i > 0 && isLast && apType == 2) {
                        // 最后一个报销申请审批通过发消息通知报销申请人
                        String sendUid = JzbDataType.getString(param.get("applyuid"));
                        argMap = getOptMsgArg(0, cid, sendUid, apType, true, true);
                        optMsgApi.sendOptSysMsg(SendSysMsgUtil.setMsgArg(argMap));
                    }
                } else {// 退回
                    Map<String, Object> uMap = new HashMap<>();
                    String randomVersion = JzbRandom.getRandom(8);
                    if (apType == 1) {
                        // 更新 出差版本号 和 出差申请状态
                        uMap.put("traversion", randomVersion);
                        uMap.put("trastatus", 1);
                    } else {
                        // 更新 报销版本号 和 报销申请状态
                        uMap.put("rebversion", randomVersion);
                        uMap.put("rebstatus", 1);
                    }
                    uMap.put("travelid", param.get("travelid"));
                    travelPlanService.updateTravelRecord(uMap);

                    // 出差 / 报销申请被退回发消息通知  申请人
                    String sendUid = param.get("applyuid").toString();
                    argMap = getOptMsgArg(0, cid, sendUid, apType, true, false);
                    optMsgApi.sendOptSysMsg(SendSysMsgUtil.setMsgArg(argMap));
                }
                response = Response.getResponseSuccess(userInfo);
            }
        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "addTravelApproval Method", ex.toString()));
        }
        if (userInfo != null) {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
                    userInfo.get("msgTag").toString(), "User Login Message"));
        } else {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
        }
        return response;
    }

    private Map<String, Object> getOptMsgArg(int idx, String cid, String sendUid, int apType, boolean lastFlag, boolean agreeFlag) {
        Map<String, Object> setMap = new HashMap<>();
        if (agreeFlag) {
            if (idx == 1 || !lastFlag) {
                setMap.put("senduid", sendUid);
                setMap.put("msg", apType == 1 ? "您有一条出差申请审批[待处理]" : "您有一条报销申请审批[待处理]"); // 消息内容
                setMap.put("code", apType == 1 ? "CCSP" : "BXSP");
                setMap.put("topic_name", cid + "/" + sendUid + "/opt/appro"); // 主题
            } else if (lastFlag) {
                setMap.put("senduid", sendUid);
                setMap.put("msg", apType == 1 ? "您的出差申请[已通过审批]" : "您的报销申请[已通过审批]");
                setMap.put("code", apType == 1 ? "CCSQ" : "BXSQ");
                setMap.put("topic_name", cid + "/" + sendUid + "/opt/apply");
            }
        } else {
            setMap.put("senduid", sendUid);
            setMap.put("msg", apType == 1 ? "注意:您的出差申请被[退回]" : "注意:您的报销申请被[退回]");
            setMap.put("code", apType == 1 ? "UN_CCSQ" : "UN_BXSQ");
            setMap.put("topic_name", cid + "/" + sendUid + "/opt/apply");
        }
        return setMap;
    }

    /**
     * 批量修改出差报销申请 (预留)
     *
     * @param param
     * @return
     */
    @CrossOrigin
    @Transactional
    @RequestMapping(value = "/updateTravelApproval", method = RequestMethod.POST)
    public Response updateTravelApproval(@RequestBody Map<String, Object> param) {
        Response response;
        try {
            List<Map<String, Object>> approvalList = (List<Map<String, Object>>) param.get("list");
            for (Map<String, Object> approval : approvalList) {

                approval.put("travelid", param.get("travelid"));
                travelApprovalService.update(approval);
            }

            travelPlanService.updateTravelRecord(param);
            response = Response.getResponseSuccess((Map<String, Object>) param.get("userinfo"));
        } catch (Exception e) {
            response = Response.getResponseError();
        }
        return response;
    }

    /**
     * 查询显示出差申请
     *
     * @param param
     * @return
     */
    @CrossOrigin
    @Transactional
    @PostMapping("/selectTravelApproval")
    public Response setTravelApprovalOk(@RequestBody Map<String, Object> param) {
        Response response;
        try {

            PageInfo pageInfo = new PageInfo();
            List<Map<String, Object>> approvalList = travelApprovalService.list(param);
            long count = travelApprovalService.count(param);
            pageInfo.setList(approvalList);
            pageInfo.setTotal(count);
            response = Response.getResponseSuccess();
            response.setPageInfo(pageInfo);
            response = Response.getResponseSuccess((Map<String, Object>) param.get("userinfo"));

        } catch (Exception e) {
            response = Response.getResponseError();
        }
        return response;
    }

    /**
     * @Author sapientia
     * @Date 17:04 2019/12/11
     * @Description 获取审批人列表
     **/
    @RequestMapping(value = "/queryOtherPersonBycid", method = RequestMethod.POST)
    @CrossOrigin
    public Response queryOtherPersonByuid(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/operate/travelApproval/queryOtherPersonBycid";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            param.put("userinfo", userInfo);
            Response res = tbDeptUserListApi.queryOtherPersonBycid(param);
            List<Map<String, Object>> list = res.getPageInfo().getList();
            response = Response.getResponseSuccess(userInfo);
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(list);
            response.setPageInfo(pageInfo);

        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "queryOtherPersonByuid Method", ex.toString()));
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
     * 根据uid 获取出差审批记录列表
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getTravelApprovalList", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response getTravelApprovalList(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/operate/travelApproval/getTravelApprovalList";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }

            if (JzbCheckParam.allNotEmpty(param, new String[]{"uid", "pagesize", "pageno"})) {
                param.put("uid", userInfo.get("uid"));
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
                List<Map<String, Object>> recordList = travelPlanService.queryTravelApprovalRecordByUid(param);
                Response resApi;
                for (int i = 0, a = recordList.size(); i < a; i++) {
                    Map<String, Object> whereParam = new HashMap<>();
                    // 2.根据查询出来的travelid 查询 出差记录详情
                    whereParam.put("travelid", recordList.get(i).get("travelid"));
                    List<Map<String, Object>> detailsList = travelPlanService.queryTravelDetailsByTravelid(whereParam);
                    List<Map<String, Object>> travelInfoList;
                    for (int j = 0, b = detailsList.size(); j < b; j++) {
                        Map<String, Object> query = new HashMap<>();
                        query.put("userinfo", param.get("userinfo"));
                        // 获取同行人名称
                        if (JzbTools.isEmpty(detailsList.get(j).get("trpeers"))) {
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
                        detailsList.get(j).put("travelCity", resApi.getResponseEntity());
                        //获取单位名称
                        if (JzbTools.isEmpty(detailsList.get(j).get("cid"))) {
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
                        travelInfoList = travelInfoService.list(query);
                        for (int l = 0, d = travelInfoList.size(); l < d; l++) {
                            List<String> proList = new ArrayList<>();
                            if (!JzbTools.isEmpty(travelInfoList.get(l).get("prolist"))) {
                                proList = StrUtil.string2List(travelInfoList.get(l).get("prolist").toString(), ",");
                                travelInfoList.get(l).put("prolist", proList);
                            } else {
                                travelInfoList.get(l).put("prolist", proList);
                            }
                        }
                        detailsList.get(j).put("travelinfolist", travelInfoList);
                        //出差资料
                        detailsList.get(j).put("traveldatalist", travelDataService.list(query));
                        //预计产出
                        Integer produce = (Integer) detailsList.get(j).get("produce");
                        List<Map<String, Object>> produceMaps = travelProduceService.list(null);
                        List<Integer> produceList = PrindexUtil.getPrindex(produce, produceMaps);
                        //筛选过滤 获取出差详情的产出资料
                        List<Map<String, Object>> selectedProduce = new ArrayList<>();
                        for (Integer k : produceList) {
                            for (Map<String, Object> map : produceMaps) {
                                Integer prindex = (Integer) map.get("prindex");
                                if (k.equals(prindex)) {
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
                int count = travelPlanService.getTravelApprovalRecordCountByUid(param);
                // 定义分页  pageinfo
                PageInfo pi = new PageInfo();
                pi.setList(recordList);
                pi.setTotal(count);
                // 设置userinfo
                response = Response.getResponseSuccess(userInfo);
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
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getTravelApprovalList Method", ex.toString()));
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
