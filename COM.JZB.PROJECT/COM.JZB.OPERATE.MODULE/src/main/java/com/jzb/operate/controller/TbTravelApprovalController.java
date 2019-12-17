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
import com.jzb.operate.api.org.DeptOrgApi;
import com.jzb.operate.api.org.NewTbCompanyListApi;
import com.jzb.operate.api.org.TbDeptUserListApi;
import com.jzb.operate.service.*;
import com.jzb.operate.util.PrindexUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
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
    @PostMapping("/addTravelApproval")
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
            if (JzbCheckParam.haveEmpty(param, new String[]{"list", "travelid", "version"})) {
                response = Response.getResponseError();
            } else {
                List<Map<String, Object>> approvalList = (List<Map<String, Object>>) param.get("list");

                for (int i = 0, a = approvalList.size(); i < a; i++) {
                    approvalList.get(i).put("travelid", param.get("travelid"));
                    approvalList.get(i).put("apid", JzbRandom.getRandomChar(12));
                    Integer idx = (Integer) approvalList.get(i).get("idx");
                    if (idx == 1) {
                        approvalList.get(i).put("trstatus", 2);
                    } else {
                        approvalList.get(i).put("trstatus", 1);
                    }
                    approvalList.get(i).put("addtime", System.currentTimeMillis());
                    approvalList.get(i).put("adduid", userInfo.get("uid"));
                    approvalList.get(i).put("status", "1");
                    approvalList.get(i).put("version", param.get("version"));
                    travelApprovalService.save(approvalList.get(i));
                }
//                for (Map<String, Object> approval : approvalList) {
//
//                    approval.put("travelid", param.get("travelid"));
//                    approval.put("apid", JzbRandom.getRandomChar(12));
//                    Integer idx = (Integer) approval.get("idx");
//                    if (idx == 1) {
//                        approval.put("trstatus", 2);
//                    } else {
//                        approval.put("trstatus", 1);
//                    }
//                    approval.put("addtime", System.currentTimeMillis());
//                    approval.put("adduid", userInfo.get("uid"));
//                    approval.put("status", "1");
//                    approval.put("version", param.get("version"));
//                    travelApprovalService.save(approval);
//                }
                // 添加抄送人
                List<String> ccuidList = (List<String>) param.get("ccuid");
                param.put("ccuid", ccuidList.toString());
                param.put("status", "3");
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
     * 批量修改出差报销申请 (预留)
     *
     * @param param
     * @return
     */
    @CrossOrigin
    @Transactional
    @PostMapping("/updateTravelApproval")
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

            if (JzbCheckParam.haveEmpty(param, new String[]{"isOk", "travelid", "idx", "apid", "version"})) {
                response = Response.getResponseError();
            } else {
                Integer isOk = (Integer) param.get("isOk");
                int count = 0;
                if (isOk == 1) {// 同意
                    Map<String, Object> whereMap = new HashMap<>();
                    whereMap.put("trtime", System.currentTimeMillis());//审批时间
                    whereMap.put("trcomment", param.get("trcomment"));
                    whereMap.put("trstatus", 3);
                    whereMap.put("version", param.get("version"));
                    whereMap.put("apid", param.get("apid"));
                    int i = travelApprovalService.update(whereMap);
                    //判断是否是最后一个审批人
                    String lastApid = travelApprovalService.getMaxIdxApid(param);
                    boolean isLast = param.get("apid").equals(lastApid);
                    if (i > 0 && !isLast) { // 将下一个审批人的审批状态改为2
                        Map<String, Object> query = new HashMap<>();
                        query.put("idx", (Integer) param.get("idx") + 1);
                        query.put("travelid", param.get("travelid"));
                        query.put("version", param.get("version"));
                        query.put("trstatus", 2);
                        query.put("trtime", System.currentTimeMillis());
                        count = travelApprovalService.update(query);
                    }
                } else {// 退回
                    Map<String, Object> uMap = new HashMap<>();
//                    uMap.put("trstatus", 4);
                    //更新审批记录版本号
                    String randomVersion = JzbRandom.getRandom(8);
//                    uMap.put("newVersion", randomVersion);
//                    travelApprovalService.update(uMap);
                    // 更新 出差记录版本号
                    uMap.put("version", randomVersion);
                    uMap.put("status", 4);
                    uMap.put("travelid",param.get("travelid"));
                    count = travelPlanService.updateTravelRecord(uMap);
                }

                response = count > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
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
    public Response getTravelRecordList(@RequestBody Map<String, Object> param) {
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
//                for (Map<String, Object> travelMap : recordList) {
//                    Map<String, Object> whereParam = new HashMap<>();
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
//                        // Map<String, Object> regionID = (Map<String, Object>) resApi.getResponseEntity();
//
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
