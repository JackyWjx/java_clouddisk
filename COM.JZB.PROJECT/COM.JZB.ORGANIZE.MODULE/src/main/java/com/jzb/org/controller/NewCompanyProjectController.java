package com.jzb.org.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.org.service.NewCompanyProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author sapientia
 * @Date 2019/12/4 14:18
 */
@RestController
@RequestMapping(value = "/company/project")
public class NewCompanyProjectController {

    @Autowired
    private NewCompanyProjectService newCompanyProjectService;

    /**
     * @Author sapientia
     * @Date  14:48
     * @Description  查询项目详情
     **/
    @PostMapping("/queryCompanyByid")
    public Response queryCompanyByid(@RequestBody Map<String, Object> map){
        Response result;
        try {
            PageInfo pageInfo = new PageInfo();
            pageInfo.setPages(JzbDataType.getInteger(map.get("page")) == 0 ? 1 : JzbDataType.getInteger(map.get("page")));
            //获取单位信息
            List<Map<String,Object>> list = newCompanyProjectService.queryCommonCompanyListBycid(map);
            for(int i = 0;i < list.size();i++){
                Map<String , Object> proMap = new HashMap<>();
                proMap.put("cid",list.get(i).get("cid").toString());
                //获取单位
                List<Map<String,Object>> prolist = newCompanyProjectService.queryCompanyByid(proMap);
                //获取单位下的项目
                for(int j = 0;j < prolist.size();j++){
                    Map<String , Object> infoMap = new HashMap<>();
                    infoMap.put("projectid",prolist.get(j).get("projectid").toString());
                    List<Map<String,Object>> infolist = newCompanyProjectService.queryCompanyByProjectid(infoMap);
                    list.get(j).put("infoList",infolist);
                }
                list.get(i).put("prolist",prolist);
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
     * @Author sapientia
     * @Date  15:01
     * @Description 修改项目信息
     **/
    @PostMapping("/updateCompanyProject")
    @Transactional
    public Response updateCompanyProject(@RequestBody Map<String, Object> map){
        Response result;
        try {
            map.put("updtime",System.currentTimeMillis());
            result = newCompanyProjectService.updateCompanyProject(map) > 0 ? Response.getResponseSuccess() : Response.getResponseError();
        }catch (Exception e){
            e.printStackTrace();
            result =  Response.getResponseError();
        }
        return result;
    }

    /**
     * @Author sapientia
     * @Date  18:11
     * @Description 修改项目详情信息
     **/
    @PostMapping("/updateCompanyProjectInfo")
    @Transactional
    public Response updateCompanyProjectInfo(@RequestBody Map<String, Object> map){
        Response result;
        try {
            map.put("updtime",System.currentTimeMillis());
            result = newCompanyProjectService.updateCompanyProjectInfo(map) > 0 ? Response.getResponseSuccess() : Response.getResponseError();
        }catch (Exception e){
            e.printStackTrace();
            result =  Response.getResponseError();
        }
        return result;
    }

    /**
     * @Author sapientia
     * @Date  18:12
     * @Description 修改单位信息
     **/
    @PostMapping("/updateCommonCompanyList")
    @Transactional
    public Response updateCommonCompanyList(@RequestBody Map<String, Object> map){
        Response result;
        try {
            map.put("updtime",System.currentTimeMillis());
            result = newCompanyProjectService.updateCommonCompanyList(map) > 0 ? Response.getResponseSuccess() : Response.getResponseError();
        }catch (Exception e){
            e.printStackTrace();
            result =  Response.getResponseError();
        }
        return result;
    }

}
