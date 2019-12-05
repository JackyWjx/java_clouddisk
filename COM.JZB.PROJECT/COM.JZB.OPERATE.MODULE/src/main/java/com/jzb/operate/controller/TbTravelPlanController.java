package com.jzb.operate.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.operate.api.base.RegionBaseApi;
import com.jzb.operate.api.org.DeptOrgApi;
import com.jzb.operate.service.TbTravelDataService;
import com.jzb.operate.service.TbTravelInfoService;
import com.jzb.operate.service.TbTravelPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
    /**
     * 根据用户名或电话号码 获取同行人
     * @param param
     * @return
     */
    @CrossOrigin
    @PostMapping("/getPeers")
    public Response getTravelpeers(@RequestBody Map<String, Object> param){
        Response result = null;
        return deptOrgApi.getDeptUser(param);
    }

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

            //遍历细节集合
            for(Map<String, Object> detailsMap: detailsList){

                detailsMap.put("deid",JzbRandom.getRandomChar(19));
                detailsMap.put("travelid",param.get("travelid"));
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
                    infoMap.put("inid",JzbRandom.getRandomChar(19));
                    travelInfoService.save(infoMap);
                }


                //获取并保存出差资料list
                List<Map<String,Object>> traveldatalist  = (List<Map<String, Object>>) detailsMap.get("travelinfolist");
                for(Map<String,Object> dataMap : traveldatalist){

                    dataMap.put("adduid",param.get("adduid"));
                    dataMap.put("travelid",param.get("travelid"));
                    dataMap.put("deid",detailsMap.get("deid"));
                    dataMap.put("did",JzbRandom.getRandomChar(19));
                    travelDataService.save(dataMap);
                }
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
           // result = new Response();
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
