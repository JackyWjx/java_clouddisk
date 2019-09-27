package com.jzb.resource.controller;

import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import com.jzb.resource.service.AdvertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Description:  系统广告控制台与前台对接
 * @Author duanfeiyu
 * @Version v1.0
 * @Since 1.0
 * @Date: 2019/8/16 15:08
 */
@RequestMapping("/advertising")
@RestController
public class AdvertController {


    @Autowired
    private AdvertService advertService;

    /**
     * 返回结果集
     * @param records  list集合
     * @param response 用json存储h
     */
    public  static  void setPageInfoList(List<Map<String,Object>> records,Response response){
        PageInfo pageInfo = new PageInfo();
        pageInfo.setList(records);
        response.setPageInfo(pageInfo);
    }


    /**
     * 广告系统查询
     * @return Response 返回json
     */
    @RequestMapping("/queryAdvertisingList")
    public Response queryAdvertisingList(@RequestBody Map<String, Object> param){
         Response response = null;
         try {
           List<Map<String,Object>> list =  advertService.queryAdvertisingList();
             // 定义返回结果
             Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
             response = Response.getResponseSuccess(userInfo);
           setPageInfoList(list,response);
         }catch (Exception e){
             JzbTools.logError(e);
         }
        return response;
    }
}
