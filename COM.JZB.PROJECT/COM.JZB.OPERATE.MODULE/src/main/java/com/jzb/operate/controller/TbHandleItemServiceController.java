package com.jzb.operate.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.data.date.JzbDateStr;
import com.jzb.base.data.date.JzbDateUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import com.jzb.operate.api.auth.AuthUserApi;
import com.jzb.operate.api.org.OrgCompanyApi;
import com.jzb.operate.api.org.OrgDeptApi;
import com.jzb.operate.api.org.TbCompanyProjectApi;
import com.jzb.operate.service.TbCompanyService;
import com.jzb.operate.service.TbHandItemService;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description: 服务统计分析
 * @Author Han Bin
 */
@RestController
@RequestMapping("/operate/handitem")
public class TbHandleItemServiceController {

    @Autowired
    private TbHandItemService service;

    @Autowired
    private OrgDeptApi api;

    @Autowired
    private OrgCompanyApi companyApi;

    @Autowired
    private TbCompanyProjectApi projectApi;

    @Autowired
    private TbCompanyService companyService;

    @Autowired
    private AuthUserApi authUserApi;

    /**
     * 获取所有部门
     *
     * @return
     *
     */
    @RequestMapping("/getDept")
    @ResponseBody
    public  Response getDept(@RequestBody Map<String , Object> map){
        Response result;
        try{
            result = api.getDeptList(map);
        }catch (Exception e ){
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 获取服务状态总数
     *
     * @return
     */
    @RequestMapping("/getTypeCount")
    @ResponseBody
    public Response getTypeCount(@RequestBody Map<String , Object> map){
        Response result;
        try{
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long begDate = formatter.parse(DateFormatUtils.format(JzbDateUtil.getDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date().getTime()), JzbDateStr.yyyy_MM_dd_HH_mm_ss), "yyyy-MM-dd 00:00:00")).getTime();
            long andDate = formatter.parse(DateFormatUtils.format(JzbDateUtil.getDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date().getTime()), JzbDateStr.yyyy_MM_dd_HH_mm_ss), "yyyy-MM-dd 23:59:59")).getTime();
            map.put("begDate",begDate);
            map.put("andDate",andDate);
            int count  =  service.queryCompanyServiceCountAAA(map);
            result = Response.getResponseSuccess();
            map.clear();
            map.put("typeCount",count);
            result.setResponseEntity(map);
        }catch (Exception e ){
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }


    /**
     * 获取信息列表
     * @return
     */
    @RequestMapping("/getCompanyService")
    @ResponseBody
    public Response getCompanyService(@RequestBody Map<String , Object> map){
        Response result ;
        try{
            JzbTools.logInfo("getCompanyService=================>>"+map.toString());
            PageInfo pageInfo = new PageInfo();
            // 返回数据
            List<Map<String , Object>> list = new ArrayList<>();
            //  总数
            int companyCount = 0;
            //  是否需要获取总数
            boolean isCount = true;
            if(JzbTools.isEmpty(map.get("person"))){
                map.remove("person");
            }
            pageInfo.setPages(JzbDataType.getInteger(map.get("page")) == 0 ? 1 : JzbDataType.getInteger(map.get("page")));
            // 判断是否存选择部门
            if(!JzbTools.isEmpty(map.get("cdid"))){
                //  用户 数组
                List<String> bblist =  new ArrayList<>();
                Response dept  =  api.getDeptUserChildList(map);
                Map<String , Object> deMap  =  (Map<String, Object>) ((ArrayList) dept.getResponseEntity()).get(0);
                List<Map<String , Object>> paraList = new ArrayList<>();
                if(!JzbTools.isEmpty(deMap.get("list"))){
                    paraList.addAll((List)deMap.get("list"));
                }
                if(!JzbTools.isEmpty(deMap.get("children"))){
                    paraList.addAll((List)deMap.get("children"));
                }
                for(int i = 0 ; i < paraList.size();i++){
                    Map<String , Object> uidMap =  paraList.get(i);
                    if(uidMap.containsKey("uid") && !JzbTools.isEmpty(uidMap.get("uid"))){
                        bblist.add(uidMap.get("uid").toString());
                    }
                }
                map.put("cuid",list.toString().replace("[","").replace("]",""));
            }
            // 是否有传项目名称 单位名称
            if(!JzbTools.isEmpty(map.get("cname")) || !JzbTools.isEmpty(map.get("projectname"))){
                list = service.selectCompanyService(new HashMap<>());
                Map<String , Object>  paraMap =  new HashMap<>();
                paraMap.put("list",list);
                // 添加分页数据 进行手动分页
                paraMap.put("pageno",map.get("pageno"));
                paraMap.put("pagesize",map.get("pagesize"));
                if(map.containsKey("cname")){
                    paraMap.put("cname",map.get("cname"));
                }else {
                    paraMap.put("projectname",map.get("projectname"));
                }
                Response api = projectApi.getCompany(paraMap);
                Map<String , Object> apiMap =  (Map<String , Object>) api.getResponseEntity();
                // 数据 替换
                List<Map<String , Object>> apiList= (List<Map<String, Object>>) apiMap.get("list");
                companyCount = JzbDataType.getInteger(apiMap.get("count"));
                isCount = false;
                list.clear();
                list.addAll(apiList);
            }else{
                list  = service.queryTbCompanyService(map);
            }
            // 根据项目id去重
            for(int i = 0 ;i <list.size() ;i++){
                for(int j = i+1 ;j < list.size();j++ ){
                    if(list.get(i).get("projectid") != null && list.get(i).get("projectid") != "" ||  list.get(j).get("projectid") != null && list.get(j).get("projectid") != ""){
                        // 去重
                        if(list.get(i).get("projectid").equals(list.get(j).get("projectid"))){
                            list.remove(i);
                            j--;
                        }
                    }
                }
            }
            for(int i =  0 ; i< list.size() ;i++){
                Map<String , Object> para = list.get(i);
                int count  =  service.queryCount(para);
                list.get(i).put("count",count);
                para.put("userinfo",map.get("userinfo"));
                Response user = authUserApi.getUserInfo(para);
                Map<String , Object> userMap = (Map<String, Object>) user.getResponseEntity();
                list.get(i).put("username",userMap.get("cname"));
                if(!JzbTools.isEmpty(para.get("projectid"))){
                    Response cpm  = companyApi.getCompanyProjct(para);
                    Map<String , Object> cpmMap = (Map<String, Object>) cpm.getResponseEntity();
                    list.get(i).put("projectname",cpmMap.get("projectname"));
                    list.get(i).put("proaddtime",cpmMap.get("addtime"));
                }
                if(!JzbTools.isEmpty(para.get("cid"))){
                    Response pro  = companyApi.getCompany(para);
                    Map<String,Object> proMap = (Map<String, Object>) pro.getResponseEntity();
                    list.get(i).put("cname",proMap.get("cname"));
                    list.get(i).put("caddtime",proMap.get("regtime"));
                }
            }
            if(isCount){
                companyCount =  service.queryTbCompanyServiceCount(map);
            }
            result =  Response.getResponseSuccess((Map)map.get("userinfo"));
            pageInfo.setList(list);
            pageInfo.setTotal(companyCount);
            result.setPageInfo(pageInfo);
        }catch (Exception e){
            JzbTools.logError(e);
            result = Response.getResponseSuccess();
        }
        return  result;
    }

    /**
     * 获取跟进列表
     *
     * @param map
     * @return
     */
    @RequestMapping("/queryCompanyService")
    @ResponseBody
    public Response queryCompanyService(@RequestBody Map<String , Object> map){
        Response result ;
        try{
            PageInfo pageInfo = new PageInfo();
            pageInfo.setPages(JzbDataType.getInteger(map.get("pageno")) == 0 ? 1 : JzbDataType.getInteger(map.get("pageno")));
            List<Map<String , Object>> list = service.queryTbCompanyServiceNotDis(map);
            for(int  i = 0 ; i < list.size() ;i++){
                Map<String , Object> para  = list.get(i);
                para.put("userinfo",map.get("userinfo"));
                Response user = authUserApi.getUserInfo(para);
                Map<String , Object> userMap = (Map<String, Object>) user.getResponseEntity();
                list.get(i).put("username",userMap.get("cname"));
            }
            int count  =  service.queryTbCompanyServiceNotDisCount(map);
            result =  Response.getResponseSuccess((Map)map.get("userinfo"));
            pageInfo.setList(list);
            pageInfo.setTotal(count);
            result.setPageInfo(pageInfo);
        }catch (Exception e){
            JzbTools.logError(e);
            result = Response.getResponseSuccess();
        }
        return  result;
    }

}
