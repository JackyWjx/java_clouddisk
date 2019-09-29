package com.jzb.operate.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import com.jzb.operate.service.TbScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @Description: 用户积分/月报
 * @Author Han Bin
 */
@Controller
@RequestMapping("/score")
public class TbScoreController {

    /**
     * 业务
     */
    @Autowired
    TbScoreService scoreService;

    @RequestMapping("/consumeUserIntegration")
    @ResponseBody
    public Response consumeUserIntegration(@RequestBody  Map<String, Object> paramap){
        Response response;
        try{
            Map<String, Object> map  = scoreService.consumeUserIntegration(paramap);
            response =   Response.getResponseSuccess() ;
            response.setResponseEntity(map);
        }catch (Exception e){
            JzbTools.logError(e);
            response =  Response.getResponseError();
        }
        return response;
    }


    /**
     *   用户完成任务操作添加
     */
    @RequestMapping("/saveUserIntegration")
    @ResponseBody
    public Response saveUserIntegration(@RequestBody  Map<String, Object> map){
        Response response;
        try{
            response = scoreService.saveUserIntegration(map) ? Response.getResponseSuccess() : Response.getResponseError();
        }catch (Exception e){
            JzbTools.logError(e);
            response =  Response.getResponseError();
        }
        return response;
    }


    /**
     *   查询积分规则
     */
    @RequestMapping("/qureyScoreRule")
    @ResponseBody
    public Response qureyScoreRule(@RequestBody  Map<String, Object> map){
        Response response;
        try{
            PageInfo pageInfo = new PageInfo();
            pageInfo.setPages(JzbDataType.getInteger(map.get("page")) == 0 ? 1 : JzbDataType.getInteger(map.get("page")));
            List<Map<String , Object>> list = scoreService.qureyScoreRule(map);
            int count  =  scoreService.qureyScoreRuleCount(map);
            response =  Response.getResponseSuccess();
            pageInfo.setList(list);
            pageInfo.setTotal(count);
            response.setPageInfo(pageInfo);
        }catch (Exception e){
            JzbTools.logError(e);
            response =  Response.getResponseError();
        }
        return response;
    }

    /**
     *   查询积分日志
     */
    @RequestMapping("/qureyScoreList")
    @ResponseBody
    public Response qureyScoreList(@RequestBody Map<String, Object> map){
        Response response;
        try{
            PageInfo pageInfo = new PageInfo();
            pageInfo.setPages(JzbDataType.getInteger(map.get("page")) == 0 ? 1 : JzbDataType.getInteger(map.get("page")));
            List<Map<String , Object>> list = scoreService.qureyScoreList(map);
            int count  =  scoreService.qureyScoreListCount(map);
            response =  Response.getResponseSuccess();
            pageInfo.setList(list);
            pageInfo.setTotal(count);
            response.setPageInfo(pageInfo);
        }catch (Exception e){
            JzbTools.logError(e);
            response =  Response.getResponseError();
        }
        return response;
    }

    /**
     *   模糊查询积分规则
     */
    @RequestMapping("/seachScoreRule")
    @ResponseBody
    public Response seachScoreRule(@RequestBody Map<String, Object> map){
        Response response;
        try{
            PageInfo pageInfo = new PageInfo();
            pageInfo.setPages(JzbDataType.getInteger(map.get("page")) == 0 ? 1 : JzbDataType.getInteger(map.get("page")));
            List<Map<String , Object>> list = scoreService.seachScoreRule(map);
            int count  =  scoreService.seachScoreRuleCount(map);
            response =  Response.getResponseSuccess();
            pageInfo.setList(list);
            pageInfo.setTotal(count);
            response.setPageInfo(pageInfo);
        }catch (Exception e){
            JzbTools.logError(e);
            response =  Response.getResponseError();
        }
        return response;
    }

    /**
     *   模糊查询积分日志
     */
        @RequestMapping("/seachScoreList")
    @ResponseBody
    public Response seachScoreList(@RequestBody Map<String, Object> map){
        Response response;
        try{
            PageInfo pageInfo = new PageInfo();
            pageInfo.setPages(JzbDataType.getInteger(map.get("page")) == 0 ? 1 : JzbDataType.getInteger(map.get("page")));
            List<Map<String , Object>> list = scoreService.seachScoreList(map);
            int count  =  scoreService.seachScoreListCount(map);
            response =  Response.getResponseSuccess();
            pageInfo.setList(list);
            pageInfo.setTotal(count);
            response.setPageInfo(pageInfo);
        }catch (Exception e){
            JzbTools.logError(e);
            response =  Response.getResponseError();
        }
        return response;
    }

    /**
     *   添加积分规则
     */
        @RequestMapping("/saveScortList")
    @ResponseBody
    public Response saveScortList(@RequestBody Map<String, Object> map){
        Response response;
        try{
            response =  scoreService.saveScortList(map) > 0 ? Response.getResponseSuccess() : Response.getResponseError();
        }catch (Exception e){
            JzbTools.logError(e);
            response =  Response.getResponseError();
        }
        return response;
    }

    /**
     *   添加积分规则
     */
    @RequestMapping("/saveScortRule")
    @ResponseBody
    public Response saveScortRule(@RequestBody Map<String, Object> map){
        Response response;
        try{
            response =  scoreService.saveScortRule(map) > 0 ? Response.getResponseSuccess() : Response.getResponseError();
        }catch (Exception e){
            JzbTools.logError(e);
            response =  Response.getResponseError();
        }
        return response;
    }

    /**
     *   修改积分日志
     */
    @RequestMapping("/upScortRule")
    @ResponseBody
    public Response upScortRule(@RequestBody Map<String, Object> map){
        Response response;
        try{
            response =  scoreService.upScortRule(map) > 0 ? Response.getResponseSuccess() : Response.getResponseError();
        }catch (Exception e){
            JzbTools.logError(e);
            response =  Response.getResponseError();
        }
        return response;
    }

    /**
     *   修改积分日志
     */
    @RequestMapping("/upScortList")
    @ResponseBody
    public Response upScortList(@RequestBody Map<String, Object> map){
        Response response;
        try{
            response =  scoreService.upScortList(map) > 0 ? Response.getResponseSuccess() : Response.getResponseError();
        }catch (Exception e){
            JzbTools.logError(e);
            response =  Response.getResponseError();
        }
        return response;
    }

    /**
     *   禁用积分规则
     */
    @RequestMapping("/removeScortRule")
    @ResponseBody
    public Response removeScortRule(@RequestBody Map<String, Object> map){
        Response response;
        try{
            response =  scoreService.removeScortRule(map) > 0 ? Response.getResponseSuccess() : Response.getResponseError();
        }catch (Exception e){
            JzbTools.logError(e);
            response =  Response.getResponseError();
        }
        return response;
    }


}
