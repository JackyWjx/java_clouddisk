package com.jzb.operate.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.operate.api.base.RegionBaseApi;
import com.jzb.operate.service.TbTravelPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
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
    RegionBaseApi regionBaseApi;

    /**
     * 添加出差计划
     * @param param
     * @return
     */
    @CrossOrigin
    @Transactional
    @PostMapping("/addTravelPlan")
    public Response addTravelRecord(@RequestBody Map<String, Object> param) {

        Response result = null;
        try{
            // 获取参数中的出差详情list
            List<Map<String, Object>> detailsList = (List) param.get("list");

            //出差记录
            param.put("travelid",JzbRandom.getRandomChar(19));
            param.put("aptype",1);//1出差 2 报销
            param.put("version",1);//默认版本号为1

            //始末时间默认为第一条记录的时间
            long temp = JzbDataType.getLong(detailsList.get(0).get("trtime"));
            long startTime = temp;
            long endTime = temp;
            for(Map<String, Object> detailsMap: detailsList){
                detailsMap.put("deid",JzbRandom.getRandomChar(19));
                detailsMap.put("travelid",param.get("travelid"));
                long trTime = JzbDataType.getLong(detailsMap.get("trtime"));
                //统计始末时间
                startTime = startTime < trTime ? startTime : trTime;
                endTime = endTime > trTime ? endTime : trTime;
                travelPlanService.addTravelDetails(Arrays.asList(detailsMap));
            }
            param.put("orgtime",startTime);
            param.put("endtime",endTime);

            travelPlanService.addTravelRecord(Arrays.asList(param));
            result = Response.getResponseSuccess((Map<String, Object>) param.get("userinfo"));
            //   result = new Response();
        } catch (Exception ex) {
            ex.printStackTrace();
            JzbTools.isEmpty(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 撤回
     * @param param
     * @return
     */
    @PostMapping(value = "/setRecallStatus")
    @CrossOrigin
    @Transactional
    public Response setBackStatus(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            if (JzbCheckParam.haveEmpty(param, new String[]{"travelid"})) {
                result = Response.getResponseError();
            } else {
                result = travelPlanService.setRecallStatus(param) > 0 ? Response.getResponseSuccess((Map<String, Object>) param.get("userinfo")) : Response.getResponseError();
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 设置删除状态
     * @param param
     * @return
     */
    @PostMapping(value = "/setDeleteStatus")
    @CrossOrigin
    @Transactional
    public Response setDeleteStatus(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            if (JzbCheckParam.haveEmpty(param, new String[]{"travelid"})) {
                    result = Response.getResponseError();
                } else {
                    result = travelPlanService.setDeleteStatus(param) > 0 ? Response.getResponseSuccess((Map<String, Object>) param.get("userinfo")) : Response.getResponseError();
                }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 修改出差记录
     * @param param
     * @return
     */
    @PostMapping(value = "/updateTravelPlan")
    @CrossOrigin
    @Transactional
    public Response updateTravelRecord(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 获取参数中的出差详情list
            List<Map<String, Object>> detailsList = (List) param.get("list");

            //统计时间
            // 始末时间默认为第一条记录的时间
            long temp = JzbDataType.getLong(detailsList.get(0).get("trtime"));
            long startTime = temp;
            long endTime = temp;
            for(Map<String, Object> detailsMap: detailsList){
                long trTime = JzbDataType.getLong(detailsMap.get("trtime"));
                //统计始末时间
                startTime = startTime < trTime ? startTime : trTime;
                endTime = endTime > trTime ? endTime : trTime;
                travelPlanService.updateTravelDetials(detailsMap);
            }
            //设置始末时间
            param.put("orgtime",startTime);
            param.put("endtime",endTime);

            result = travelPlanService.updateTravelRecord(param) > 0 ? Response.getResponseSuccess((Map<String, Object>) param.get("userinfo")) : Response.getResponseError();

        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 根据Travelid查询出差记录
     */
    @CrossOrigin
    @PostMapping("/getTravelPlanByTravelid")
    public Response queryTravelPlan(@RequestBody Map<String, Object> param){
        Response result = null;
        try{
            Map<String,Object> travelMap = travelPlanService.queryTravelRecordByTravelid(param);
            List<Map<String,Object>> detailsList = travelPlanService.queryTravelDetailsByTravelid(param);
            travelMap.put("list",detailsList);
            result = Response.getResponseSuccess((Map<String, Object>) param.get("userinfo"));
            result.setResponseEntity(travelMap);
        }catch (Exception e){
            e.printStackTrace();
            result =  Response.getResponseError();
        }
        return result;
    }

    /**
     * @Author sapientia
     * @Description 获取省市县列表
     * @Date  12:49
     * @Param [param]
     * @return com.jzb.base.message.Response
     **/
    @CrossOrigin
    @PostMapping("/getCityList")
    public Response getCityList(@RequestBody Map<String, Object> param){
        Response result = null;
        try{
            PageInfo pageInfo = new PageInfo();
            Response res  = regionBaseApi.getCityJson(param);
            List<Map<String , Object>>  cityList = res.getPageInfo().getList();
            pageInfo.setList(cityList);
            result.setPageInfo(pageInfo);
        }catch (Exception e){
            e.printStackTrace();
            result =  Response.getResponseError();
        }
        return result;
    }

}
