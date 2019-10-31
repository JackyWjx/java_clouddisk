package com.jzb.operate.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
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

    /**
     * 获取所有部门
     *
     * @return
     */
    @RequestMapping("/getDept")
    @ResponseBody
    public  Response getDept(Map<String , Object> map){
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
                Map<String , Object> companUid  =  (Map)api.getDeptUserChildList(map);
                List<String> uid = new ArrayList<>();
                // 获取部门下的用户
                if(companUid.containsKey("list") && !JzbTools.isEmpty(companUid.get("list"))){
                    List  list  = (List) companUid.get("list");
                    for(int i = 0 ; i < list.size() ;i++){
                       Map<String , Object> uidMap =  (Map)list.get(i);
                       uid.add(uidMap.get("uid").toString());
                    }
                }
                // 获取子部门的用户
                if(companUid.containsKey("children") && !JzbTools.isEmpty(companUid.get("children"))){
                    Map<String , Object>  childrenMap  = (Map) companUid.get("children");
                    List  list  = (List) childrenMap.get("list");
                    for(int i = 0 ; i < list.size() ;i++){
                        Map<String , Object> uidMap =  (Map)list.get(i);
                        uid.add(uidMap.get("uid").toString());
                    }
                }
                map.put("uid",uid.toString().replace("[","").replace("]",""));
            }
            List<Map<String , Object>> list = service.queryTbCompanyService(map);
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
     * @param map
     * @return
     */
    @RequestMapping("/queryHandItem")
    @ResponseBody
    public Response  queryHandItem(@RequestBody Map<String , Object> map ){
        Response result ;
        try{
            PageInfo pageInfo = new PageInfo();
            pageInfo.setPages(JzbDataType.getInteger(map.get("page")) == 0 ? 1 : JzbDataType.getInteger(map.get("page")));
            List<Map<String , Object>> list = service.queryTbHandleItem(map);
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
