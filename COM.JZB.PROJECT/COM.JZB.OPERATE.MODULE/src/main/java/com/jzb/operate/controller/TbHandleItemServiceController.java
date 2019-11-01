package com.jzb.operate.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import com.jzb.operate.api.org.OrgCompanyApi;
import com.jzb.operate.api.org.OrgDeptApi;
import com.jzb.operate.service.TbHandItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    /**
     * 获取所有部门
     *
     * @return
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
            // 判断是否存选择部门
            if(map.containsKey("cdid")){
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
                map.put("uid",list.toString().replace("[","").replace("]",""));
            }
            List<Map<String , Object>> list = service.queryTbCompanyService(map);
            for(int i =  0 ; i< list.size() ;i++){
                Map<String , Object> para = list.get(i);
                if(!JzbTools.isEmpty(para.get("projectid"))){
                    Response cpm  = companyApi.getCompanyProjct(para);
                    Map<String , Object> cpmMap = (Map<String, Object>) cpm.getResponseEntity();
                    list.get(i).put("projectname",cpmMap.get("projectname"));
                }
                if(!JzbTools.isEmpty(para.get("cid"))){
                    Response pro  = companyApi.getEnterpriseData(para);
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
     * 获取跟进详情列表
     *
     * @param
     * @return
     */
    @RequestMapping("/queryHandItem")
    @ResponseBody
    public Response  queryHandItem(@RequestBody Map<String , Object> map ){
        Response result ;
        try{
            PageInfo pageInfo = new PageInfo();
            pageInfo.setPages(JzbDataType.getInteger(map.get("pageno")) == 0 ? 1 : JzbDataType.getInteger(map.get("pageno")));
            List<Map<String , Object>> list = service.queryTbHandleItem(map);
            for(int i =  0 ; i< list.size() ;i++){
                Map<String , Object> para = list.get(i);
                if(!JzbTools.isEmpty(para.get("projectid"))){
                    Response cpm  = companyApi.getCompanyProjct(para);
                    Map<String , Object> cpmMap = (Map<String, Object>) cpm.getResponseEntity();
                    list.get(i).put("projectname",cpmMap.get("projectname"));
                }
                if(!JzbTools.isEmpty(para.get("cid"))){
                    Response pro  = companyApi.getEnterpriseData(para);
                    Map<String,Object> proMap = (Map<String, Object>) pro.getResponseEntity();
                    list.get(i).put("cname",proMap.get("cname"));
                }
            }
            int count  =  service.queryTbHandleItemCount(map);
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
