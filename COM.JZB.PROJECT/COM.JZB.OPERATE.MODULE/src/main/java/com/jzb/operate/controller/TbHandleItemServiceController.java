package com.jzb.operate.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.data.date.JzbDateStr;
import com.jzb.base.data.date.JzbDateUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
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
    TbCompanyProjectApi projectApi;

    @Autowired
    private TbCompanyService companyService;

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
            int count  =  companyService.queryCompanyServiceTypeCount(map);
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
            PageInfo pageInfo = new PageInfo();
            if(JzbTools.isEmpty(map.get("person"))){
                map.remove("person");
            }
            pageInfo.setPages(JzbDataType.getInteger(map.get("page")) == 0 ? 1 : JzbDataType.getInteger(map.get("page")));
            // 判断是否存选择部门
            if(!JzbTools.isEmpty(map.get("cdid"))){
                //  用户 数组
                List<String> list =  new ArrayList<>();
                Response dept  =  api.getDeptUserChildList(map);
                Map<String , Object> deMap  =  (Map<String, Object>) dept.getResponseEntity();
                for(Map.Entry <String , Object> typetry:deMap.entrySet()) {
                    Map<String , Object> typetryMap = (Map<String, Object>) deMap.get(typetry.getKey());
                    if(typetryMap.containsKey("list") && !JzbTools.isEmpty(typetryMap.get("list"))){
                        List<Map<String , Object>>  uidList  = (List<Map<String, Object>>) typetryMap.get("list");
                        for(int i = 0 ; i < uidList.size() ;i++){
                            list.add(uidList.get(i).get("uid").toString());
                        }
                    }
                }
                map.put("cuid",list.toString().replace("[","").replace("]",""));
            }
            List<Map<String , Object>> list = service.queryTbCompanyService(map);
            if(!JzbTools.isEmpty(map.get("cname")) || !JzbTools.isEmpty(map.get("projectname"))){
                Map<String , Object>  paraMap =  new HashMap<>();
                paraMap.put("list",list);
                if(map.containsKey("cname")){
                    paraMap.put("cname",map.get("cname"));
                }else {
                    paraMap.put("projectname",map.get("projectname"));
                }
                Response api = projectApi.getCompany(paraMap);
                List<Map<String , Object>> apiMap = (List<Map<String, Object>>) api.getResponseEntity();
                list.clear();
                list.addAll(apiMap);
            }
            for(int i =  0 ; i< list.size() ;i++){
                Map<String , Object> para = list.get(i);
                int count  =  service.queryCount(para);
                list.get(i).put("count",count);
                para.put("userinfo",(Map<String , Object>) map.get("userinfo"));
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
                }
            }
            int count  =  service.queryTbCompanyServiceCount(map);
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
            pageInfo.setPages(JzbDataType.getInteger(map.get("page")) == 0 ? 1 : JzbDataType.getInteger(map.get("page")));
            List<Map<String , Object>> list = service.queryTbCompanyServiceNotDis(map);
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
