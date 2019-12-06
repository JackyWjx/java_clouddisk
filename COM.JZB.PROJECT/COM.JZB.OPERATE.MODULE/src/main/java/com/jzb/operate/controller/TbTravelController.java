package com.jzb.operate.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.operate.api.org.NewTbCompanyListApi;
import com.jzb.operate.api.org.TbTrackUserListApi;
import com.jzb.operate.service.TbTravelExpenseService;
import com.jzb.operate.service.TbTravelService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author sapientia
 * @Date 2019/12/2 11:21
 */
@RestController
@RequestMapping(value = "/reimburseSystem")
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
     * 查询出差记录
     */
    @PostMapping("/queryList")
    public Response queryTravelList(@RequestBody Map<String, Object> map){
        Response result;
        try{
            PageInfo pageInfo = new PageInfo();
            pageInfo.setPages(JzbDataType.getInteger(map.get("page")) == 0 ? 1 : JzbDataType.getInteger(map.get("page")));
            // 获取出差记录
            List<Map<String , Object>> list = tbTravelService.queryTravelList(map);
            // 根据出差记录获取出差详情  出差记录的id 获取
            for(int i = 0 ; i<list.size();i++){
               Map<String , Object> deMap = new HashMap<>();
               deMap.put("travelid",list.get(i).get("travelid"));
                // 获取出差详情 deList<Map>
                List<Map<String , Object>>  deList =  tbTravelService.queryTravelListDeta(deMap);
                for (int j = 0 ;j<deList.size();j++){
                    Map<String,Object> damap = new HashMap<>();
                    damap.put("deid",deList.get(j).get("deid"));
                    // 通过出差详情id  获取出差资料信息
                    List<Map<String , Object>> daList = tbTravelService.queryTravelData(damap);
                    //通过出差详情id  获取出差情报信息
                    List<Map<String , Object>> infoList = tbTravelService.queryTravelInfo(damap);
                    // 添加至出差详情里
                    deList.get(j).put("data",daList);
                    deList.get(j).put("info",infoList);
                }
                //list.get(i).put("deta",deList)
                list.get(i).put("deta",deList);
            }
            result =  Response.getResponseSuccess();
            pageInfo.setList(list);
            result.setPageInfo(pageInfo);
        }catch (Exception e){
            e.printStackTrace();
            result =  Response.getResponseError();
        }
        return result;
    }

    @PostMapping("/queryListByDeid")
    public Response queryListByDeid(@RequestBody Map<String, Object> map){
        Response result;
        try {
            PageInfo pageInfo = new PageInfo();
            pageInfo.setPages(JzbDataType.getInteger(map.get("page")) == 0 ? 1 : JzbDataType.getInteger(map.get("page")));
            List<Map<String , Object>> list = tbTravelService.queryTravelListDeta(map);
            for(int i =0 ;i < list.size();i++){
                Map<String,Object> promap = new HashMap<>();
                if(list.get(i).get("cid")!=null && list.get(i).get("projectid")!=null){
                    promap.put("cid",list.get(i).get("cid"));
                    promap.put("projectid",list.get(i).get("projectid"));
                    promap.put("pageInfo",pageInfo);
                    Response res = newTbCompanyListApi.queryCompanyByid(promap);
                    List<Map<String , Object>>  reList = res.getPageInfo().getList();
                    list.get(i).put("reList",reList);
                }
            }
            result =  Response.getResponseSuccess();
            pageInfo.setList(list);
            result.setPageInfo(pageInfo);
        }catch (Exception e){
            e.printStackTrace();
            result =  Response.getResponseError();
        }
        return result;
    }

    /**
     *  修改出差费用
     */
    @PostMapping("/updateTravelFare")
    @Transactional
    public Response updateTravelFare(@RequestBody Map<String, Object> map){
        Response result;
        try {
            result =  tbTravelService.updateTravelFare(map) > 0 ? Response.getResponseSuccess() : Response.getResponseError();
        }catch (Exception e){
            e.printStackTrace();
            result =  Response.getResponseError();
        }
        return result;
    }

    /**
     * 根据申请人id、单位id以及拜访时间获取跟进记录
     */
    @PostMapping("/queryTrackUserList")
    public Response queryTrackUserList(@RequestBody Map<String, Object> map){
        Response result;
        try {
            PageInfo pageInfo = new PageInfo();
            pageInfo.setPages(JzbDataType.getInteger(map.get("page")) == 0 ? 1 : JzbDataType.getInteger(map.get("page")));
            //获取出差详情记录
            List<Map<String , Object>> delist = tbTravelService.queryTrackUserList(map);
            for (int i = 0 ;i < delist.size() ;i++){
                 // 根据申请人 单位 拜访时间 查询跟进记录\
                Map<String , Object> dataMap =  new HashedMap();
                dataMap.put("userinfo",map.get("userinfo"));
                dataMap.put("param",delist.get(i));
                dataMap.put("pageInfo",pageInfo);
                 Response res  = api.queryTrackUserByName(dataMap);
                 List<Map<String , Object>>  reList = res.getPageInfo().getList();
                 delist.get(i).put("reList",reList);
            }
            result =  Response.getResponseSuccess();
            pageInfo.setList(delist);
            result.setPageInfo(pageInfo);
        }catch (Exception e){
            e.printStackTrace();
            result =  Response.getResponseError();
        }
        return result;
    }

   /**
    * @Author sapientia
    * @Date 11:36 2019/12/5
    * @Description 设置删除状态
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
     * @Description  根据公司id修改公司信息
     **/
    @PostMapping("/updateCommonCompanyList")
    @Transactional
    public Response updateCommonCompanyList(@RequestBody Map<String, Object> map){
        Response result;
        try {
            result = newTbCompanyListApi.updateCommonCompanyList(map);
        }catch (Exception e){
            e.printStackTrace();
            result =  Response.getResponseError();
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
    public Response updateCompanyProject(@RequestBody Map<String, Object> map){
        Response result;
        try {
            result = newTbCompanyListApi.updateCompanyProject(map);
        }catch (Exception e){
            e.printStackTrace();
            result =  Response.getResponseError();
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
    public Response updateCompanyProjectInfo(@RequestBody Map<String, Object> map){
        Response result;
        try {
           result = newTbCompanyListApi.updateCompanyProjectInfo(map);
        }catch (Exception e){
            e.printStackTrace();
            result =  Response.getResponseError();
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
    public Response saveTravelExpense(@RequestBody Map<String, Object> param){
        Response result;
        try {
            List<Map<String, Object>> list = (List<Map<String, Object>>) param.get("list");

            for(Map<String, Object> expMap: list){
                expMap.put("exid",JzbRandom.getRandomChar(12));
                expMap.put("travelid",param.get("travelid"));
                expMap.put("addtime",System.currentTimeMillis());
                expMap.put("status",1);//默认状态1
                list.add(expMap);
            }
            tbTravelExpenseService.saveTravelExpense(list);
            result = Response.getResponseSuccess((Map<String, Object>) param.get("userinfo"));
        }catch (Exception e){
            e.printStackTrace();
            result =  Response.getResponseError();
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
    public Response updateTravelExpense(@RequestBody Map<String, Object> param ){
        Response result;
        try {
            List<Map<String, Object>> list = (List<Map<String, Object>>) param.get("list");

            for(Map<String, Object> expMap: list){
                expMap.put("travelid",param.get("travelid"));
                expMap.put("addtime",System.currentTimeMillis());
                expMap.put("status",1);//默认状态1
                list.add(expMap);
            }
            tbTravelExpenseService.saveTravelExpense(list);
            result = Response.getResponseSuccess((Map<String, Object>) param.get("userinfo"));
        }catch (Exception e){
            e.printStackTrace();
            result =  Response.getResponseError();
        }
        return result;
    }
}
