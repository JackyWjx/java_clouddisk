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
import com.jzb.operate.service.TbTravelService;
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
    * @Description 根据出差id查询出差详情列表
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
                List<Map<String, Object>> list = tbTravelService.queryTravelList(param);
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
     * @Date 10:49 2019/12/2
     * @Description 根据出差详情id查询出差的详情
     **/
    @RequestMapping(value = "/queryTravelListBydeid" ,method = RequestMethod.POST)
    @CrossOrigin
    public Response queryTravelListBydeid(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/operate/reimburseSystem/queryTravelListBydeid";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            if (JzbCheckParam.haveEmpty(param, new String[]{"pagesize", "pageno","deid"})) {
                response = Response.getResponseError();
            } else {
                JzbPageConvert.setPageRows(param);
                //获取出差详情
                param.put("uid",userInfo.get("uid"));
                param.put("deid",param.get("deid").toString().trim());
                List<Map<String, Object>> list = tbTravelService.queryTravelListDeta(param);
                for(Map<String,Object> damap:list){
                    // 通过出差详情id  获取出差资料信息
                    List<Map<String, Object>> daList = tbTravelService.queryTravelData(damap);
                    //通过出差详情id  获取出差情报信息
                    List<Map<String, Object>> infoList = tbTravelService.queryTravelInfo(damap);
                    damap.put("daList",daList);
                    damap.put("infoList",infoList);
                }
                response = Response.getResponseSuccess(userInfo);
                PageInfo pageInfo = new PageInfo();
                pageInfo.setList(list);
                pageInfo.setTotal(tbTravelService.countTravelListDeta(param));
                response.setPageInfo(pageInfo);
            }
        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "queryTravelListBydeid Method", ex.toString()));
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
            if (JzbCheckParam.haveEmpty(param, new String[]{"pagesize", "pageno","deid" })) {
                response = Response.getResponseError();
            } else {
                    JzbPageConvert.setPageRows(param);
                    param.put("uid",userInfo.get("uid"));
                    param.put("deid",param.get("deid").toString().trim());
                    // 获取出差详情
                    List<Map<String, Object>> list = tbTravelService.queryTravelListDeta(param);
                    Map<String, Object> promap = new HashMap<>();
                    promap.put("list",list);
                    promap.put("userinfo",userInfo);
                    promap.put("pageno",param.get("pageno"));
                    promap.put("pagesize",param.get("pagesize"));
                    Response res = newTbCompanyListApi.queryCompanyByid(promap);
                    response.setPageInfo(res.getPageInfo());
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
            if (JzbCheckParam.haveEmpty(param, new String[]{"pagesize", "pageno","deid" })) {
                response = Response.getResponseError();
            } else {
                JzbPageConvert.setPageRows(param);
                //获取出差详情记录
                List<Map<String, Object>> delist = tbTravelService.queryTrackUserList(param);
                for (int i = 0; i < delist.size(); i++) {
                    // 根据申请人 单位 拜访时间 查询跟进记录\
                    Map<String, Object> dataMap = new HashMap();
                    dataMap.put("userInfo",userInfo);
                    dataMap.put("list", delist.get(i));
                    Response res = tbTrackUserListApi.queryTrackUserByName(dataMap);
                    List<Map<String, Object>> reList = res.getPageInfo().getList();
                    delist.get(i).put("reList", reList);
                }
                response = Response.getResponseSuccess(userInfo);
                PageInfo pageInfo = new PageInfo();
                pageInfo.setList(delist);
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
            if (JzbCheckParam.haveEmpty(param, new String[]{"pagesize", "pageno","travelid" })) {
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
            if (JzbCheckParam.haveEmpty(param, new String[]{"pagesize", "pageno","cid" })) {
                response = Response.getResponseError();
            } else {
                JzbPageConvert.setPageRows(param);
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
            if (JzbCheckParam.haveEmpty(param, new String[]{"pagesize", "pageno","projectid" })) {
                response = Response.getResponseError();
            } else {
                JzbPageConvert.setPageRows(param);
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
            if (JzbCheckParam.haveEmpty(param, new String[]{"pagesize", "pageno","projectid" })) {
                response = Response.getResponseError();
            } else {
                JzbPageConvert.setPageRows(param);
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
            if (JzbCheckParam.haveEmpty(param, new String[]{"pagesize", "pageno","list" })) {
                response = Response.getResponseError();
            } else {
                JzbPageConvert.setPageRows(param);
                List<Map<String, Object>> list = (List<Map<String, Object>>) param.get("list");
                for (Map<String, Object> expMap : list) {
                    expMap.put("travelid", JzbRandom.getRandomChar(19));
                    expMap.put("exid", JzbRandom.getRandomChar(12));
                    expMap.put("addtime", System.currentTimeMillis());
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
            if (JzbCheckParam.haveEmpty(param, new String[]{"pagesize", "pageno","list" })) {
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
            if (JzbCheckParam.haveEmpty(param, new String[]{"pagesize", "pageno","exid" })) {
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
