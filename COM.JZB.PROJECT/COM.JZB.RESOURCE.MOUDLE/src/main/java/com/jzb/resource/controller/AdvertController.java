package com.jzb.resource.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import com.jzb.resource.service.AdvertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    /**
     * CRM-运营管理-活动-推广图片
     * 点击活动获取所有的系统推广信息
     *
     * @author kuangbin
     */
    @RequestMapping(value = "/getAdvertList", method = RequestMethod.POST)
    @CrossOrigin
    public Response getAdvertList(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            int count = JzbDataType.getInteger(param.get("count"));
            // 获取单位总数
            count = count < 0 ? 0 : count;
            if (count == 0) {
                // 查询单位总数
                count = advertService.getAdvertListCount(param);
            }
            // 返回所有的企业列表
            List<Map<String, Object>> adverList = advertService.getAdvertList(param);
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            result = Response.getResponseSuccess(userInfo);
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(adverList);
            pageInfo.setTotal(count > 0 ? count : adverList.size());
            result.setPageInfo(pageInfo);
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    } // End getAdvertList

    /**
     * CRM-运营管理-活动-推广图片
     * 点击保存后修改对应的推广信息
     *
     * @author kuangbin
     */
    @RequestMapping(value = "/modifyAdvertData", method = RequestMethod.POST)
    @CrossOrigin
    public Response modifyAdvertData(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 获取用户信息
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("uid", JzbDataType.getString(userInfo.get("uid")));
            // 获取修改成功值
            int count = advertService.modifyAdvertData(param);
            result = count==1? Response.getResponseSuccess(userInfo):Response.getResponseError();
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    } // End modifyAdvertData
}
