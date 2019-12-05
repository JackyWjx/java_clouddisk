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
import com.jzb.operate.service.TbTravelProduceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
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
    @Autowired
    TbTravelProduceService travelProduceService;

    /**
     * 根据用户名或电话号码 获取同行人
     * @param param
     * @return
     */
    @CrossOrigin
    @PostMapping("/getPeers")
    public Response getTravelpeers(@RequestBody Map<String, Object> param){
        Response response = null;
        return deptOrgApi.getDeptUser(param);
    }

    /**
     * 获取预计产出列表
     * @param param
     * @return
     */
    @CrossOrigin
    @Transactional
    @PostMapping(value = "/getProduceList")
    public Response getProduceList(@RequestBody Map<String, Object> param){
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
    @PostMapping("/addTravelPlan")
    public Response addTravelRecord(@RequestBody Map<String, Object> param) {

        Response response = null;
        try{
            // 获取参数中的出差详情list
            List<Map<String, Object>> detailsList = (List) param.get("list");

            //出差记录
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
            response = Response.getResponseSuccess((Map<String, Object>) param.get("userinfo"));

            //   response = new Response();
        } catch (Exception ex) {
            ex.printStackTrace();
            JzbTools.isEmpty(ex);
            response = Response.getResponseError();
        }
        return response;
    }

    /**
     * 撤回 status = 3
     * @param param
     * @return
     */
    @PostMapping(value = "/setRecallStatus")
    @CrossOrigin
    @Transactional
    public Response setBackStatus(@RequestBody Map<String, Object> param) {
        Response response;

        param.put("status",3);
        param.put("version",JzbRandom.getRandom(8));
        try {
            if (JzbCheckParam.haveEmpty(param, new String[]{"travelid"})) {
                response = Response.getResponseError();
            } else {
                response = travelPlanService.updateTravelRecord(param) > 0 ? Response.getResponseSuccess((Map<String, Object>) param.get("userinfo")) : Response.getResponseError();
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            response = Response.getResponseError();
        }
        return response;
    }

    /**
     * 设置删除状态 status = 2
     * @param param
     * @return
     */
    @PostMapping(value = "/setDeleteStatus")
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
    @PostMapping(value = "/updateTravelPlan")
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
    @PostMapping("/getTravelPlanByTravelid")
    public Response queryTravelPlan(@RequestBody Map<String, Object> param){
        Response response = null;
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
            }

            // response = Response.getResponseSuccess((Map<String, Object>) param.get("userinfo"));
            response = new Response();
            response.setResponseEntity(travelMap);
        }catch (Exception e){
            e.printStackTrace();
            response =  Response.getResponseError();
        }
        return response;
    }


    /**
     * @Author sapientia
     * @Description &#x83b7;&#x53d6;&#x7701;&#x5e02;&#x53bf;&#x5217;&#x8868;
     * @Date 12:49
     * @Param [param]
     * @return com.jzb.base.message.Response
     **/
    @CrossOrigin
    @PostMapping("/getCityList")
    public Response getCityList(@RequestBody Map<String, Object> param){
        Response response = null;
        try{
            PageInfo pageInfo = new PageInfo();
            Response res  = regionBaseApi.getCityJson(param);
            List<Map<String , Object>>  cityList = res.getPageInfo().getList();
            pageInfo.setList(cityList);
            response = Response.getResponseSuccess();
            response.setPageInfo(pageInfo);
        }catch (Exception e){
            e.printStackTrace();
            response =  Response.getResponseError();
        }
        return response;
    }

}
