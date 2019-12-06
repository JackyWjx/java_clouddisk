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
import org.apache.commons.collections.map.HashedMap;
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
    TbTrackUserListApi api;

    @Autowired
    NewTbCompanyListApi newTbCompanyListApi;

    @Autowired
    TbTravelExpenseService tbTravelExpenseService;

    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(TbTravelController.class);

    @RequestMapping(value = "/queryAllTravelList" ,method = RequestMethod.POST)
    @CrossOrigin
    public Response queryAllTravelList(@RequestBody Map<String, Object> param) {
        Response result;
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
                result = Response.getResponseError();
            } else {
                JzbPageConvert.setPageRows(param);
                // 获取出差记录
                param.put("uid",userInfo.get("uid"));
                List<Map<String, Object>> list = tbTravelService.queryAllTravelList(param);
                result = Response.getResponseSuccess(userInfo);
                PageInfo pageInfo = new PageInfo();
                pageInfo.setList(list);
                pageInfo.setTotal(tbTravelService.countList(param));
                result.setPageInfo(pageInfo);
            }
        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "queryAllTravelList Method", ex.toString()));
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
    * @Author sapientia
    * @Date 10:49 2019/12/2
    * @Description 查询出差记录及详情
    **/
    @RequestMapping(value = "/queryTravelListByid" ,method = RequestMethod.POST)
    @CrossOrigin
    public Response queryTravelListByid(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/operate/reimburseSystem/queryTravelListByid";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            if (JzbCheckParam.haveEmpty(param, new String[]{"pagesize", "pageno","travelid","deid"})) {
                result = Response.getResponseError();
            } else {
                JzbPageConvert.setPageRows(param);
                // 获取出差记录
                param.put("uid",userInfo.get("uid"));
                List<Map<String, Object>> list = tbTravelService.queryTravelList(param);
                // 根据出差记录获取出差详情  出差记录的id 获取
                for (int i = 0, a = list.size(); i < a; i++) {
                    Map<String, Object> deMap = new HashMap<>();
                    deMap.put("travelid", list.get(i).get("travelid"));
                    // 获取出差详情 deList<Map>
                    List<Map<String, Object>> deList = tbTravelService.queryTravelListDeta(deMap);
                    for (int j = 0 ,b = deList.size(); j < b; j++) {
                        Map<String, Object> damap = new HashMap<>();
                        damap.put("deid", deList.get(j).get("deid"));
                        // 通过出差详情id  获取出差资料信息
                        List<Map<String, Object>> daList = tbTravelService.queryTravelData(damap);
                        //通过出差详情id  获取出差情报信息
                        List<Map<String, Object>> infoList = tbTravelService.queryTravelInfo(damap);
                        // 添加至出差详情里
                        deList.get(j).put("data", daList);
                        deList.get(j).put("info", infoList);
                    }
                    //放入出差记录
                    list.get(i).put("deta", deList);
                }
                result = Response.getResponseSuccess(userInfo);
                PageInfo pageInfo = new PageInfo();
                pageInfo.setList(list);
                pageInfo.setTotal(tbTravelService.countList(param));
                result.setPageInfo(pageInfo);
            }
        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "queryTravelListByid Method", ex.toString()));
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
     * @Author sapientia
     * @Date 12:51 2019/12/5
     * @Description 查询产出情况
     **/
    @RequestMapping(value = "/queryListByDeid" ,method = RequestMethod.POST)
    @CrossOrigin
    public Response queryListByDeid(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/operate/reimburseSystem/queryListByDeid";
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
                    // 获取出差详情
                    param.put("uid", userInfo.get("uid"));
                    List<Map<String, Object>> list = tbTravelService.queryTravelListDeta(param);
                    for (int i = 0, a = list.size(); i < a; i++) {
                        Map<String, Object> promap = new HashMap<>();
                        promap.put("cid", list.get(i).get("cid"));
                        promap.put("projectid", list.get(i).get("projectid"));
                        Response res = newTbCompanyListApi.queryCompanyByid(promap);
                        List<Map<String, Object>> reList = res.getPageInfo().getList();
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
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "queryListByDeid Method", ex.toString()));
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
     * 修改出差费用
     */
    @PostMapping("/updateTravelFare")
    @Transactional
    public Response updateTravelFare(@RequestBody Map<String, Object> map) {
        Response result;
        try {
            map.put("updtime", System.currentTimeMillis());
            result = tbTravelService.updateTravelFare(map) > 0 ? Response.getResponseSuccess() : Response.getResponseError();
        } catch (Exception e) {
            e.printStackTrace();
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * @Author sapientia
     * @Date 10:52 2019/12/4
     * @Description 根据申请人id、单位id以及拜访时间获取跟进记录(总结表)
     **/
    @PostMapping("/queryTrackUserList")
    public Response queryTrackUserList(@RequestBody Map<String, Object> map) {
        Response result;
        try {
            PageInfo pageInfo = new PageInfo();
            pageInfo.setPages(JzbDataType.getInteger(map.get("page")) == 0 ? 1 : JzbDataType.getInteger(map.get("page")));
            //获取出差详情记录
            List<Map<String, Object>> delist = tbTravelService.queryTrackUserList(map);
            for (int i = 0; i < delist.size(); i++) {
                // 根据申请人 单位 拜访时间 查询跟进记录\
                Map<String, Object> dataMap = new HashedMap();
                dataMap.put("userinfo", map.get("userinfo"));
                dataMap.put("param", delist.get(i));
                dataMap.put("pageInfo", pageInfo);
                Response res = api.queryTrackUserByName(dataMap);
                List<Map<String, Object>> reList = res.getPageInfo().getList();
                delist.get(i).put("reList", reList);
            }
            result = Response.getResponseSuccess();
            pageInfo.setList(delist);
            result.setPageInfo(pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * @Author sapientia
     * @Date 11:36 2019/12/5
     * @Description 设置出差记录删除状态
     **/
    @PostMapping(value = "/setDeleteStatus")
    @CrossOrigin
    @Transactional
    public Response setDeleteStatus(@RequestBody Map<String, Object> map) {
        Response result;
        try {
            if (JzbCheckParam.haveEmpty(map, new String[]{"travelid"})) {
                result = Response.getResponseError();
            } else {
                result = tbTravelService.setDeleteStatus(map) > 0 ? Response.getResponseSuccess() : Response.getResponseError();
            }
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * @Author sapientia
     * @Date 11:36 2019/12/5
     * @Description 根据公司id修改公司信息
     **/
    @PostMapping("/updateCommonCompanyList")
    @Transactional
    public Response updateCommonCompanyList(@RequestBody Map<String, Object> map) {
        Response result;
        try {
            result = newTbCompanyListApi.updateCommonCompanyList(map);
        } catch (Exception e) {
            e.printStackTrace();
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * @Author sapientia
     * @Date 11:36 2019/12/5
     * @Description 根据项目id修改项目信息
     **/
    @PostMapping("/updateCompanyProject")
    @Transactional
    public Response updateCompanyProject(@RequestBody Map<String, Object> map) {
        Response result;
        try {
            result = newTbCompanyListApi.updateCompanyProject(map);
        } catch (Exception e) {
            e.printStackTrace();
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * @Author sapientia
     * @Date 11:36 2019/12/5
     * @Description 根据项目id修改项目情报
     **/
    @PostMapping("/updateCompanyProjectInfo")
    @Transactional
    public Response updateCompanyProjectInfo(@RequestBody Map<String, Object> map) {
        Response result;
        try {
            result = newTbCompanyListApi.updateCompanyProjectInfo(map);
        } catch (Exception e) {
            e.printStackTrace();
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * @Author sapientia
     * @Date 17:28 2019/12/5
     * @Description 添加报销单信息
     **/
    @PostMapping("/saveTravelExpense")
    @Transactional
    public Response saveTravelExpense(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            List<Map<String, Object>> list = (List<Map<String, Object>>) param.get("list");

            for (Map<String, Object> expMap : list) {
                expMap.put("travelid", JzbRandom.getRandomChar(19));
                expMap.put("exid", JzbRandom.getRandomChar(12));
                expMap.put("addtime", System.currentTimeMillis());
                expMap.put("status", 1);//默认状态1
            }
            tbTravelExpenseService.saveTravelExpense(list);
            result = Response.getResponseSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * @Author sapientia
     * @Date 17:37 2019/12/5
     * @Description 报销单信息修改
     **/
    @PostMapping("/updateTravelExpense")
    @Transactional
    public Response updateTravelExpense(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            List<Map<String, Object>> list = (List<Map<String, Object>>) param.get("list");
            for (Map<String, Object> exMap : list) {
                exMap.put("addtime", System.currentTimeMillis());
            }
            tbTravelExpenseService.updateTravelExpense(list);
            result = Response.getResponseSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * @Author sapientia
     * @Date 10:26 2019/12/6
     * @Description 查询报销单信息
     **/
    @PostMapping("/queryTravelExpenseByid")
    public Response queryTravelExpenseByid(@RequestBody Map<String, Object> param) {
        Response response;
        try {
            PageInfo pageInfo = new PageInfo();
            //获取出差详情记录
            List<Map<String, Object>> explist = tbTravelExpenseService.queryTravelExpenseByid(param);
            response = Response.getResponseSuccess();
            pageInfo.setList(explist);
            response.setPageInfo(pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
            response = Response.getResponseError();
        }
        return response;
    }

    /**
     * @Author sapientia
     * @Date 11:21 2019/12/6
     * @Description 设置报销单删除状态
     **/
    @PostMapping(value = "/setExpenseDeleteStatus")
    @CrossOrigin
    @Transactional
    public Response setExpenseDeleteStatus(@RequestBody Map<String, Object> param) {
        Response response;
        try {
            if (JzbCheckParam.haveEmpty(param, new String[]{"exid"})) {
                response = Response.getResponseError();
            } else {
                response = tbTravelExpenseService.setExpenseDeleteStatus(param) > 0 ? Response.getResponseSuccess() : Response.getResponseError();
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            response = Response.getResponseError();
        }
        return response;
    }
}
