package com.jzb.operate.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbPageConvert;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.operate.api.org.NewTbCompanyListApi;
import com.jzb.operate.api.org.TbTrackUserListApi;
import com.jzb.operate.service.TbTravelExpenseService;
import com.jzb.operate.service.TbTravelProduceService;
import com.jzb.operate.service.TbTravelService;
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
 * @Author sapientia
 * @Date 2019/12/2 11:21
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
                    Map<String,Object> appmap =new HashMap<>();
                    appmap.put("travelid",list.get(i).get("travelid").toString().trim());

                    //获取审批状态
                    List<Map<String, Object>> applist = tbTravelService.queryTravelApproval(appmap);
                    list.get(i).put("applist",applist);

                    //获取出差详情
                    List<Map<String, Object>> relist = tbTravelService.queryTravelListDeta(appmap);
                    for(int j = 0 ,b =relist.size(); j < b ;j++){
                        //获取单位名称
                        Map<String,Object> camap =new HashMap<>();
                        camap.put("cid",relist.get(j).get("cid").toString().trim());
                        camap.put("userinfo",userInfo);
                        Response res = newTbCompanyListApi.queryCompanyNameBycid(camap);
                        List<Map<String,Object>> calist = res.getPageInfo().getList();
                        relist.get(j).put("calist",calist);

                        //获取资料及情报
                        Map<String,Object> recmap =new HashMap<>();
                        recmap.put("deid",relist.get(j).get("deid").toString().trim());
                        // 通过出差详情id  获取出差资料信息
                        List<Map<String, Object>> daList = tbTravelService.queryTravelData(recmap);
                        //通过出差详情id  获取出差情报信息
                        List<Map<String, Object>> infoList = tbTravelService.queryTravelInfo(recmap);
                        relist.get(j).put("daList",daList);
                        relist.get(j).put("infoList",infoList);

//                    //获取产出
//                    Map<String,Object> promap = new HashMap<>();
//                    promap.put("produce",list.get(i).get("produce"));
                    }
                    list.get(i).put("reList",relist);
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

//   /**
//    * @Author sapientia
//    * @Date 10:49 2019/12/2
//    * @Description 根据出差id查询出差详情列表
//    **/
//    @RequestMapping(value = "/queryTravelListBytrid" ,method = RequestMethod.POST)
//    @CrossOrigin
//    public Response queryTravelListBytrid(@RequestBody Map<String, Object> param) {
//        Response response;
//        Map<String, Object> userInfo = null;
//        String api = "/operate/reimburseSystem/queryTravelListBytrid";
//        boolean flag = true;
//        try {
//            if (param.get("userinfo") != null) {
//                userInfo = (Map<String, Object>) param.get("userinfo");
//                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
//                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
//            } else {
//                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
//            }
//            if (JzbCheckParam.haveEmpty(param, new String[]{"pagesize", "pageno","travelid"})) {
//                response = Response.getResponseError();
//            } else {
//                JzbPageConvert.setPageRows(param);
//                param.put("travelid",param.get("travelid").toString().trim());
//                // 获取出差详情列表
//                List<Map<String, Object>> list = tbTravelService.queryTravelList(param);
//                for(int i = 0 ,a =list.size(); i < a ;i++){
//                    //获取单位名称
//                    Map<String,Object> camap =new HashMap<>();
//                    camap.put("cid",list.get(i).get("cid"));
//                    camap.put("userinfo",userInfo);
//                    Response res = newTbCompanyListApi.queryCompanyNameBycid(camap);
//                    List<Map<String,Object>> calist = res.getPageInfo().getList();
//                    list.get(i).put("calist",calist);
//
//                    //获取资料及情报
//                    Map<String,Object> recmap =new HashMap<>();
//                    recmap.put("deid",list.get(i).get("deid"));
//                    // 通过出差详情id  获取出差资料信息
//                    List<Map<String, Object>> daList = tbTravelService.queryTravelData(recmap);
//                    //通过出差详情id  获取出差情报信息
//                    List<Map<String, Object>> infoList = tbTravelService.queryTravelInfo(recmap);
//                    list.get(i).put("daList",daList);
//                    list.get(i).put("infoList",infoList);
//
////                    //获取产出
////                    Map<String,Object> promap = new HashMap<>();
////                    promap.put("produce",list.get(i).get("produce"));
//
//                }
//                response = Response.getResponseSuccess(userInfo);
//                PageInfo pageInfo = new PageInfo();
//                pageInfo.setList(list);
//                pageInfo.setTotal(tbTravelService.countTravelList(param));
//                response.setPageInfo(pageInfo);
//            }
//        } catch (Exception ex) {
//            flag = false;
//            JzbTools.logError(ex);
//            response = Response.getResponseError();
//            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "queryTravelListBytrid Method", ex.toString()));
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
                //获取出差记录
                param.put("uid", userInfo.get("uid"));
                param.put("travelid", param.get("travelid").toString().trim());
                List<Map<String, Object>> list = tbTravelService.queryTravelList(param);
                for (int i = 0, a = list.size(); i < a;i++) {
                    //获取单位名称
                    Map<String, Object> camap = new HashMap<>();
                    camap.put("cid", list.get(i).get("cid").toString().trim());
                    camap.put("userinfo", userInfo);
                    Response res = newTbCompanyListApi.queryCompanyNameBycid(camap);
                    List<Map<String, Object>> calist = res.getPageInfo().getList();
                    list.get(i).put("calist", calist);

                    //获取出差记录
                    List<Map<String, Object>> monlist = tbTravelService.queryAllTravelList(param);
                    //获取资料及情报
                    Map<String, Object> recmap = new HashMap<>();
                    recmap.put("deid", list.get(i).get("deid").toString().trim());
                    // 通过出差详情id  获取出差资料信息
                    List<Map<String, Object>> daList = tbTravelService.queryTravelData(recmap);
                    //通过出差详情id  获取出差情报信息
                    List<Map<String, Object>> infoList = tbTravelService.queryTravelInfo(recmap);
                    list.get(i).put("daList", daList);
                    list.get(i).put("infoList", infoList);
                    list.get(i).put("monList",monlist);
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
     * @Date 17:26 2019/12/10
     * @Description 情报搜集
     **/


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
            if (JzbCheckParam.haveEmpty(param, new String[]{"pagesize", "pageno","deid" })) {
                response = Response.getResponseError();
            } else {
                    JzbPageConvert.setPageRows(param);
                    param.put("uid",userInfo.get("uid"));
                    param.put("deid",param.get("deid").toString().trim());
                    // 获取出差详情
                    List<Map<String, Object>> list = tbTravelService.queryTravelListDeta(param);
                    for(int i = 0,a = list.size();i < a;i++){
                        Map<String, Object> promap = new HashMap<>();
                        promap.put("list",list);
                        promap.put("userinfo",userInfo);
                        promap.put("pageno",param.get("pageno"));
                        promap.put("pagesize",param.get("pagesize"));
                        promap.put("cid",list.get(i).get("cid"));
                        promap.put("projectid",list.get(i).get("projectid"));

                        //获取情报
                        List<Map<String, Object>> infolist = tbTravelService.queryTravelInfo(param);
                        //获取产出
                        Response res = newTbCompanyListApi.queryCompanyByid(promap);
                        List<Map<String, Object>> reList = res.getPageInfo().getList();
                        list.get(i).put("infoList",infolist);
                        list.get(i).put("reList", reList);
                    }
                    response = Response.getResponseSuccess(userInfo);
                    PageInfo pageInfo = new PageInfo();
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
                List<Map<String,Object>> list =tbTravelService.queryTravelListDeta(param);
                for (int i = 0 ,a = list.size(); i < a ; i++){
                    // 根据申请人 单位 拜访时间 查询跟进记录\
                    Map<String, Object> dataMap = new HashMap();
                    dataMap.put("userinfo",userInfo);
                    dataMap.put("list", list);
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
     * @Date 13:45 2019/12/10
     * @Description 查询产出表(未测)
     **/
    @RequestMapping(value = "/queryTravelProduce",method = RequestMethod.POST)
    @CrossOrigin
    public Response queryTravelProduce(@RequestBody Map<String, Object> param){
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
                List<Map<String,Object>> list =tbTravelService.queryTravelProduce(param);
                response = Response.getResponseSuccess(userInfo);
                PageInfo pageInfo = new PageInfo();
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
     * @Description 设置出差记录删除状态
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
                JzbPageConvert.setPageRows(param);
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
                response = newTbCompanyListApi.updateCommonCompanyList(param);
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
                response = newTbCompanyListApi.updateCompanyProject(param);
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
                response = newTbCompanyListApi.updateCompanyProjectInfo(param);
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

    /**
     * @Author sapientia
     * @Date 17:28 2019/12/5
     * @Description 添加报销单信息
     **/
    @RequestMapping(value = "/saveTravelExpense",method = RequestMethod.POST)
    @Transactional
    public Response saveTravelExpense(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/operate/reimburseSystem/saveTravelExpense";
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
                    Map<String, Object> expMap = new HashMap<>();
                    expMap.put("travelid", list.get(i).get("travelid"));
                    expMap.put("exid", JzbRandom.getRandomChar(12));
                    expMap.put("addtime", System.currentTimeMillis());
                    expMap.put("adduid",userInfo.get("uid"));
                    expMap.put("status", 1);//默认状态1
                }
                tbTravelExpenseService.saveTravelExpense(list);
                response = Response.getResponseSuccess(userInfo);
            }
        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "saveTravelExpense Method", ex.toString()));
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
                for (Map<String, Object> exMap : list) {
                    exMap.put("addtime", System.currentTimeMillis());
                }
                tbTravelExpenseService.updateTravelExpense(list);
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
                //获取报销单详情
                List<Map<String, Object>> explist = tbTravelExpenseService.queryTravelExpenseByid(param);
                response = Response.getResponseSuccess(userInfo);
                PageInfo pageInfo = new PageInfo();
                pageInfo.setList(explist);
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
     * @Date 11:21 2019/12/6
     * @Description 设置报销单删除状态
     **/
    @RequestMapping(value = "/setExpenseDeleteStatus",method = RequestMethod.POST)
    @CrossOrigin
    @Transactional
    public Response setExpenseDeleteStatus(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/operate/reimburseSystem/setExpenseDeleteStatus";
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
                JzbPageConvert.setPageRows(param);
                response = tbTravelExpenseService.setExpenseDeleteStatus(param) > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
            }
        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "setExpenseDeleteStatus Method", ex.toString()));
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
