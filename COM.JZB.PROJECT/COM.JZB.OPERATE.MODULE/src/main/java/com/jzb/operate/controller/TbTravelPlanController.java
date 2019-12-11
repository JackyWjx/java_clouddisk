package com.jzb.operate.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.data.date.JzbDateStr;
import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbPageConvert;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.operate.api.auth.UserInfoApi;
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
import org.springframework.web.bind.annotation.*;

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
    private TbTravelRecordService tbTravelRecordService;

    @Autowired
    UserInfoApi userInfoApi;

    @Autowired
    NewTbCompanyListApi newTbCompanyListApi;

    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(TbTravelPlanController.class);

    /**
     * 根据用户名或电话号码 获取同行人
     * @param param
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/getPeers",method = RequestMethod.POST)
    public Response getTravelpeers(@RequestBody Map<String, Object> param){
        Response response = null;
        return deptOrgApi.getDeptUser(param);
    }


    /**
     * 获取预计产出列表
     * @return
     */
    @CrossOrigin
    @Transactional
    @RequestMapping(value = "/getProduceList",method = RequestMethod.POST)
    public Response getProduceList(){
        Response response;
        try{
            PageInfo pageInfo = new PageInfo();
            List<Map<String,Object>> produceList = travelProduceService.list(null);
            long count = travelProduceService.count(null);
            pageInfo.setList(produceList);
            pageInfo.setTotal(count);
            response = Response.getResponseSuccess();
            response.setPageInfo(pageInfo);
        }catch (Exception e){
            e.printStackTrace();
            response =  Response.getResponseError();
        }
        return response;
    }

    /**
     * 添加出差计划
     * @param param
     * @return
     */
    @CrossOrigin
    @Transactional
    @RequestMapping(value = "/addTravelPlan",method = RequestMethod.POST)
    public Response addTravelRecord(@RequestBody Map<String, Object> param) {

        Response response ;
        Map<String, Object> userInfo = null;
        String api = "/operate/travelPlan/addTravelPlan";
        boolean flag = true;

        try{
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
            param.put("adduid",userInfo.get("uid"));
            param.put("travelid",JzbRandom.getRandomChar(19));
            param.put("aptype",1);//1出差 2 报销
            param.put("version",JzbRandom.getRandom(8));
            param.put("status",1);//默认状态1

            //始末时间默认为第一条记录的时间
            long temp = JzbDataType.getLong(detailsList.get(0).get("trtime"));
            long startTime = temp;
            long endTime = temp;

            //遍历细节集合
            for(Map<String, Object> detailsMap: detailsList){

                detailsMap.put("deid",JzbRandom.getRandomChar(19));
                detailsMap.put("travelid",param.get("travelid"));
                detailsMap.put("uid",param.get("uid"));
                detailsMap.put("addtime",System.currentTimeMillis());
                detailsMap.put("status",1);//默认状态1
                // prindex加密处理
                Integer[] prindexs = (Integer[]) detailsMap.get("prindexs");
                List<Integer> prindexLst = new ArrayList<>(Arrays.asList(prindexs));
                detailsMap.put("produce",PrindexUtil.setPrindex(prindexLst));

                long trTime = JzbDataType.getLong(detailsMap.get("trtime"));
                //统计始末时间
                startTime = startTime < trTime ? startTime : trTime;
                endTime = endTime > trTime ? endTime : trTime;

                //获取并保存情报收集list
                List<Map<String,Object>> travelinfolist = (List<Map<String, Object>>) detailsMap.get("travelinfolist");
                //一般travelinfolist的长度为1
                for( Map<String,Object> infoMap : travelinfolist){

                    infoMap.put("adduid",param.get("adduid"));
                    infoMap.put("travelid",param.get("travelid"));
                    infoMap.put("deid",detailsMap.get("deid"));
                    infoMap.put("status",1);//默认状态1
                    infoMap.put("inid",JzbRandom.getRandomChar(19));
                    infoMap.put("addtime",System.currentTimeMillis());


                    travelInfoService.save(infoMap);
                }

                //获取并保存出差资料list
                List<Map<String,Object>> traveldatalist  = (List<Map<String, Object>>) detailsMap.get("traveldatalist");
                for(Map<String,Object> dataMap : traveldatalist){

                    dataMap.put("adduid",param.get("adduid"));
                    dataMap.put("travelid",param.get("travelid"));
                    dataMap.put("deid",detailsMap.get("deid"));
                    dataMap.put("did",JzbRandom.getRandomChar(19));
                    dataMap.put("addtime",System.currentTimeMillis());
                    dataMap.put("status",1);//默认状态1

                    travelDataService.save(dataMap);
                }
            }

            //设置出差记录的时间域
            param.put("orgtime",startTime);
            param.put("endtime",endTime);
            //添加出差细节
            travelPlanService.addTravelDetails(detailsList);
            //添加出差记录
            travelPlanService.addTravelRecord(Arrays.asList(param));
            response = Response.getResponseSuccess(userInfo);

            //   response = new Response();
        } catch (Exception ex) {
            JzbTools.logError(ex);
            response = Response.getResponseError();
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
     * 撤回 status = 3
     * @param param
     * @return
     */
    @RequestMapping(value = "/setRecallStatus",method = RequestMethod.POST)
    @CrossOrigin
    @Transactional
    public Response setBackStatus(@RequestBody Map<String, Object> param) {
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

            if (JzbCheckParam.haveEmpty(param, new String[]{"travelid"})) {
                response = Response.getResponseError();
            } else {
                param.put("status",3);
                param.put("version",JzbRandom.getRandom(8));
                response = travelPlanService.updateTravelRecord(param) > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            response = Response.getResponseError();
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
     * @param param
     * @return
     */
    @RequestMapping(value = "/setDeleteStatus",method = RequestMethod.POST)
    @CrossOrigin
    @Transactional
    public Response setDeleteStatus(@RequestBody Map<String, Object> param) {
        Response response;
        try {
            if (JzbCheckParam.haveEmpty(param, new String[]{"travelid"})) {
                response = Response.getResponseError();
            } else {
                response = travelPlanService.setDeleteStatus(param) > 0 ? Response.getResponseSuccess((Map<String, Object>) param.get("userinfo")) : Response.getResponseError();
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            response = Response.getResponseError();
        }
        return response;
    }

    /**
     * 修改出差记录
     * @param param
     * @return
     */
    @RequestMapping(value = "/updateTravelPlan",method = RequestMethod.POST)
    @CrossOrigin
    @Transactional
    public Response updateTravelRecord(@RequestBody Map<String, Object> param) {
        Response response;
        try {
            // 获取参数中的出差详情list
            List<Map<String, Object>> detailsList = (List) param.get("list");

            //统计时间// 始末时间默认为第一条记录的时间
            long temp = JzbDataType.getLong(detailsList.get(0).get("trtime"));
            long startTime = temp;
            long endTime = temp;
            for(Map<String, Object> detailsMap: detailsList){
                long trTime = JzbDataType.getLong(detailsMap.get("trtime"));
                //统计始末时间
                startTime = startTime < trTime ? startTime : trTime;
                endTime = endTime > trTime ? endTime : trTime;
                // prindex加密处理
                Integer[] prindexs = (Integer[]) detailsMap.get("prindexs");
                List<Integer> prindexLst = new ArrayList<>(Arrays.asList(prindexs));
                detailsMap.put("produce",PrindexUtil.setPrindex(prindexLst));
                travelPlanService.updateTravelDetials(detailsMap);

                //获取并保存情报收集list
                List<Map<String,Object>> travelinfolist = (List<Map<String, Object>>) detailsMap.get("travelinfolist");
                //一般travelinfolist的长度为1
                for( Map<String,Object> infoMap : travelinfolist){

                    infoMap.put("updtime",System.currentTimeMillis());
                    travelInfoService.update(infoMap);
                }

                //获取并保存出差资料list
                List<Map<String,Object>> traveldatalist  = (List<Map<String, Object>>) detailsMap.get("traveldatalist");
                for(Map<String,Object> dataMap : traveldatalist){

                    dataMap.put("updtime",System.currentTimeMillis());
                    travelDataService.update(dataMap);
                }
            }

            //设置始末时间
            param.put("orgtime",startTime);
            param.put("endtime",endTime);
            response = travelPlanService.updateTravelRecord(param) > 0 ? Response.getResponseSuccess((Map<String, Object>) param.get("userinfo")) : Response.getResponseError();

        } catch (Exception ex) {
            JzbTools.logError(ex);
            response = Response.getResponseError();
        }
        return response;
    }

    /**
     * 根据Travelid查询出差记录
     */
    @CrossOrigin
    @RequestMapping(value = "/getTravelPlanByTravelid",method = RequestMethod.POST)
    public Response queryTravelPlan(@RequestBody Map<String, Object> param){
        Response response ;
        try{
            Map<String,Object> travelMap = travelPlanService.queryTravelRecordByTravelid(param);
            List<Map<String,Object>> detailsList = travelPlanService.queryTravelDetailsByTravelid(param);
            travelMap.put("list",detailsList);

            for(Map<String,Object> detialsMap : detailsList){
                Map<String,Object> query = new HashMap<>();
                query.put("travelid",param.get("travelid"));
                query.put("deid",detialsMap.get("deid"));
                //情报收集
                detialsMap.put("travelinfolist",travelInfoService.list(query));
                //出差资料
                detialsMap.put("traveldatalist",travelDataService.list(query));
                //预计产出
                Integer produce = (Integer) detialsMap.get("produce");
                List<Map<String,Object>> produceMaps = travelProduceService.list(null);
                List<Integer> produceList = PrindexUtil.getPrindex(produce,produceMaps);
                detialsMap.put("produceList",produceList);
            }

            response = Response.getResponseSuccess((Map<String, Object>) param.get("userinfo"));
            // response = new Response();
            response.setResponseEntity(travelMap);
        }catch (Exception e){
            e.printStackTrace();
            response =  Response.getResponseError();
        }
        return response;
    }


    /**
     * @Author sapientia
     * @Description 获取省市县
     * @Date 12:49
     * @Param [param]
     * @return com.jzb.base.message.Response
     **/
    @CrossOrigin
    @RequestMapping(value = "/getCityList",method = RequestMethod.POST)
    public Response getCityList(@RequestBody Map<String, Object> param){
        Response response ;
        try{
            PageInfo pageInfo = new PageInfo();
            Response res  = regionBaseApi.getCityJson(param);
            List<Map<String , Object>>  cityList = res.getPageInfo().getList();
            pageInfo.setList(cityList);
            response = Response.getResponseSuccess((Map<String, Object>) param.get("userinfo"));
            response.setPageInfo(pageInfo);
        }catch (Exception e){
            e.printStackTrace();
            response =  Response.getResponseError();
        }
        return response;
    }

    /**
     *  @author: gongWei
     *  @Date:  2019/12/11 10:59
     *  @Description: 获取同行人列表信息
     *  @Param:
     *  @Return:
     *  @Exception:
     */
    @RequestMapping(value = "/queryUsernameBydept", method = RequestMethod.POST)
    @CrossOrigin
    public Response queryUsernameBydept(@RequestBody Map<String, Object> param){
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
        Response result;
        Map<String, Object> userInfo = null;
        String  api="/operate/travelRecord/getTravelRecordList";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                param.put("uid",userInfo.get("uid"));
                logger.info(JzbLoggerUtil.getApiLogger( api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger( api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }

            if (JzbCheckParam.allNotEmpty(param, new String[]{"uid", "pagesize", "pageno"})) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
                // 得到结果集
                List<Map<String, Object>> recordList = tbTravelRecordService.getTravelRecordListByUid(param);
                Response resApi;
                for (Map<String,Object> travelMap: recordList) {
                    // 1.根据查询出来的truids(审批人员id集合)调用用户Aip查询审批人员名称用于界面展示
                    Map<String,Object> whereParam = new HashMap<>();
                    if(JzbTools.isEmpty(travelMap.get("truids"))){
                        travelMap.put("approvers","");
                    }
                    whereParam.put("unames",travelMap.get("truids"));
                    resApi = userInfoApi.searchInvitee(param);
                    String unameStr = (String) resApi.getResponseEntity();
                    travelMap.put("approvers",unameStr);
                    // 2.根据查询出来的travelid 查询 出差详情记录
                    whereParam.put("travelid",travelMap.get("travelid"));
                    List<Map<String,Object>> detailsList = travelPlanService.queryTravelDetailsByTravelid(whereParam);

                    for(Map<String,Object> detialsMap : detailsList){
                        Map<String,Object> query = new HashMap<>();
                        // 获取同行人名称
                        if(JzbTools.isEmpty(detialsMap.get("trpeers"))){
                            travelMap.put("trpeers","");
                        }
                        query.put("unames",detialsMap.get("trpeers"));
                        resApi = userInfoApi.searchInvitee(query);
                        String trpeers = (String) resApi.getResponseEntity();
                        detialsMap.put("trpeers",trpeers);
                        // 出差区域
                        query.put("",detialsMap.get("trregion"));
                        resApi = regionBaseApi.getCityJson(query);
                        String travelCity = (String) resApi.getResponseEntity();
                        detialsMap.put("travelCity",travelCity);
                        //获取单位名称
                        query.put("cid",detialsMap.get("cid"));
                        resApi = newTbCompanyListApi.queryCompanyNameBycid(query);
                        List<Map<String,Object>> calist = resApi.getPageInfo().getList();
                        detialsMap.put("clist",calist);

                        query.put("travelid",detialsMap.get("travelid"));
                        query.put("deid",detialsMap.get("deid"));
                        //情报收集
                        detialsMap.put("travelinfolist",travelInfoService.list(query));
                        //出差资料
                        detialsMap.put("traveldatalist",travelDataService.list(query));
                        //预计产出
                        Integer produce = (Integer) detialsMap.get("produce");
                        List<Map<String,Object>> produceMaps = travelProduceService.list(null);
                        List<Integer> produceList = PrindexUtil.getPrindex(produce,produceMaps);
                        //筛选过滤 获取出差详情的产出资料
                        List<Map<String,Object>> selectedProduce = new ArrayList<>();
                        for(Map<String,Object> map : produceMaps){
                            Integer prindex = (Integer) map.get("prindex");
                            for (Integer i : produceList){
                                if(i == prindex){
                                    selectedProduce.add(map);
                                    break;
                                }
                            }
                        }
                        detialsMap.put("produceList",produceList);
                    }

                    travelMap.put("detailsList",detailsList);

                }

                // 得到总数
                int count = tbTravelRecordService.getTravelRecordCountByUid(param);

                // 定义分页  pageinfo
                PageInfo pi = new PageInfo();
                pi.setList(recordList);
                pi.setTotal(count);
                // 设置userinfo
                result = Response.getResponseSuccess((Map<String, Object>) param.get("userinfo"));
                result.setPageInfo(pi);

            } else {
                logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getTravelRecordList Method", "[param error] or [param is null]"));
                result = Response.getResponseError();
            }
        } catch (Exception ex) {
            flag=false;
            // 返回错误
            JzbTools.logError(ex);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getTravelRecordList Method", ex.toString()));
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
