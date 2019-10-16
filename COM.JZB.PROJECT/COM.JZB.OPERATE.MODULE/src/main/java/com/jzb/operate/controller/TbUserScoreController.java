package com.jzb.operate.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.operate.service.TbUserScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @Description: 积分用户月报
 * @Author Han Bin
 */
@Controller
@RequestMapping("/user/score")
public class TbUserScoreController {

    @Autowired
    TbUserScoreService scoreService;


    /**
     *   查询积分
     */
    @RequestMapping("/qureyUserScore")
    @ResponseBody
    public Response qureyUserScore(@RequestBody Map<String, Object> map){
        Response response;
        try{
            PageInfo pageInfo = new PageInfo();
            pageInfo.setPages(JzbDataType.getInteger(map.get("page")) == 0 ? 1 : JzbDataType.getInteger(map.get("page")));
            List<Map<String , Object>> list = scoreService.qureyUserScore(map);
            int count  =  scoreService.qureyUserScoreCount(map);
            response =  Response.getResponseSuccess((Map)map.get("userinfo"));
            pageInfo.setList(list);
            pageInfo.setTotal(count);
            response.setPageInfo(pageInfo);
        }catch (Exception e){
            e.printStackTrace();
            response =  Response.getResponseError();
        }
        return response;
    }

    /**
     *   查询积分月报
     */
    @RequestMapping("/qureyUserMonthScore")
    @ResponseBody
    public Response qureyUserMonthScore(@RequestBody Map<String, Object> map){
        Response response;
        try{
            PageInfo pageInfo = new PageInfo();
            pageInfo.setPages(JzbDataType.getInteger(map.get("page")) == 0 ? 1 : JzbDataType.getInteger(map.get("page")));
            List<Map<String , Object>> list = scoreService.qureyUserMonthScore(map);
            int count  =  scoreService.qureyUserMonthScoreCount(map);
            response =  Response.getResponseSuccess((Map)map.get("userinfo"));
            pageInfo.setList(list);
            pageInfo.setTotal(count);
            response.setPageInfo(pageInfo);
        }catch (Exception e){
            e.printStackTrace();
            response =  Response.getResponseError();
        }
        return response;
    }

    /**
     *   模糊查询积分
     */
    @RequestMapping("/seachUserScore")
    @ResponseBody
    public Response seachUserScore(@RequestBody  Map<String, Object> map){
        Response response;
        try{
            PageInfo pageInfo = new PageInfo();
            pageInfo.setPages(JzbDataType.getInteger(map.get("page")) == 0 ? 1 : JzbDataType.getInteger(map.get("page")));
            List<Map<String , Object>> list = scoreService.seachUserScore(map);
            int count  =  scoreService.seachUserScoreCount(map);
            response =  Response.getResponseSuccess((Map)map.get("userinfo"));
            pageInfo.setList(list);
            pageInfo.setTotal(count);
            response.setPageInfo(pageInfo);
        }catch (Exception e){
            e.printStackTrace();
            response =  Response.getResponseError();
        }
        return response;
    }

    /**
     *   模糊查询积分月报
     */
    @RequestMapping("/seachUserMonthScore")
    @ResponseBody
    public Response seachUserMonthScore(@RequestBody  Map<String, Object> map){
        Response response;
        try{
            PageInfo pageInfo = new PageInfo();
            pageInfo.setPages(JzbDataType.getInteger(map.get("page")) == 0 ? 1 : JzbDataType.getInteger(map.get("page")));
            List<Map<String , Object>> list = scoreService.seachUserMonthScore(map);
            int count  =  scoreService.seachUserMonthScoreCount(map);
            response =  Response.getResponseSuccess((Map)map.get("userinfo"));
            pageInfo.setList(list);
            pageInfo.setTotal(count);
            response.setPageInfo(pageInfo);
        }catch (Exception e){
            e.printStackTrace();
            response =  Response.getResponseError();
        }
        return response;
    }

    /**
     *   添加积分
     */
    @RequestMapping("/saveUserScore")
    @ResponseBody
    public Response saveUserScore(@RequestBody Map<String, Object> map){
        Response response;
        try{
            response = scoreService.saveUserScore(map) > 0 ? Response.getResponseSuccess((Map)map.get("userinfo")) : Response.getResponseError();
        }catch (Exception e){
            e.printStackTrace();
            response =  Response.getResponseError();
        }
        return response;
    }

    /**
     *   添加积分月报
     */
    @RequestMapping("/saveUserMonthScoreRule")
    @ResponseBody
    public Response saveUserMonthScoreRule(@RequestBody Map<String, Object> map){
        Response response;
        try{
            response = scoreService.saveUserMonthScoreRule(map) > 0 ? Response.getResponseSuccess((Map)map.get("userinfo")) : Response.getResponseError();
        }catch (Exception e){
            e.printStackTrace();
            response =  Response.getResponseError();
        }
        return response;
    }

    /**
     *   修改积分
     */
    @RequestMapping("/upUserScore")
    @ResponseBody
    public Response upUserScore(@RequestBody Map<String, Object> map){
        Response response;
        try{
            response = scoreService.upUserScore(map) > 0 ? Response.getResponseSuccess((Map)map.get("userinfo")) : Response.getResponseError();
        }catch (Exception e){
            e.printStackTrace();
            response =  Response.getResponseError();
        }
        return response;
    }

    /**
     *   修改积分月报
     */
    @RequestMapping("/upUserMonthScoreRule")
    @ResponseBody
    public Response upUserMonthScoreRule(@RequestBody Map<String, Object> map){
        Response response;
        try{
            response = scoreService.upUserMonthScoreRule(map) > 0 ? Response.getResponseSuccess((Map)map.get("userinfo")) : Response.getResponseError();
        }catch (Exception e){
            e.printStackTrace();
            response =  Response.getResponseError();
        }
        return response;
    }

    /**
     *   禁用积分
     */
    @RequestMapping("/removeUserScore")
    @ResponseBody
    public Response removeUserScore(@RequestBody  Map<String, Object> map){
        Response response;
        try{
            response = scoreService.removeUserScore(map) > 0 ? Response.getResponseSuccess((Map)map.get("userinfo")) : Response.getResponseError();
        }catch (Exception e){
            e.printStackTrace();
            response =  Response.getResponseError();
        }
        return response;
    }

}
