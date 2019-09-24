package com.jzb.operate.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.operate.service.TbVersionLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @Description: 系统日志
 * @Author Han Bin
 */
@Controller
@RequestMapping("/operate/log")
public class TbVersionLogController {

    @Autowired
    TbVersionLogService service;

    /**
     *  查询
     */
    @RequestMapping(value = "/queryVersionLog" , method = RequestMethod.POST)
    @ResponseBody
    public Response queryVersionLog(@RequestBody Map<String , Object> map){
        Response  response;
        try{
            PageInfo pageInfo = new PageInfo();
            pageInfo.setPages(JzbDataType.getInteger(map.get("page")) == 0 ? 1 : JzbDataType.getInteger(map.get("page")));
            List<Map<String , Object>> list = service.queryVersionLog(map);
            int count  =  service.queryVersionLogCount(map);
            response =  Response.getResponseSuccess();
            pageInfo.setList(list);
            pageInfo.setTotal(count);
            response.setPageInfo(pageInfo);
        }catch (Exception e){
            e.printStackTrace();
            response = Response.getResponseError();
        }
        return  response;

    }

    /**
     *  模糊查询
     */
    @RequestMapping(value = "/searchVersionLog" , method = RequestMethod.POST)
    @ResponseBody
    public Response searchVersionLog(@RequestBody Map<String , Object> map){
        Response  response;
        try{
            PageInfo pageInfo = new PageInfo();
            pageInfo.setPages(JzbDataType.getInteger(map.get("page")) == 0 ? 1 : JzbDataType.getInteger(map.get("page")));
            List<Map<String , Object>> list = service.searchVersionLog(map);
            int count  =  service.searchVersionLogCount(map);
            response =  Response.getResponseSuccess();
            pageInfo.setList(list);
            pageInfo.setTotal(count);
            response.setPageInfo(pageInfo);
        }catch (Exception e){
            e.printStackTrace();
            response = Response.getResponseError();
        }
        return  response;
    }

    /**
     *  添加
     */
    @RequestMapping(value = "/saveVersionLog" , method = RequestMethod.POST)
    @ResponseBody
    public Response  saveVersionLog(@RequestBody Map<String , Object> map){
        Response  response;
        try{
            response =  service.saveVersionLog(map) > 0 ? Response.getResponseSuccess() : Response.getResponseError();
        }catch (Exception e){
            e.printStackTrace();
            response = Response.getResponseError();
        }
        return  response;
    }

    /**
     *  修改
     */
    @RequestMapping(value = "/upVersionLog" , method = RequestMethod.POST)
    @ResponseBody
    public Response  upVersionLog(@RequestBody Map<String , Object> map){
        Response  response;
        try{
            response =  service.upVersionLog(map) > 0 ? Response.getResponseSuccess() : Response.getResponseError();
        }catch (Exception e){
            e.printStackTrace();
            response = Response.getResponseError();
        }
        return  response;
    }

    /**
     *  禁用
     */
    @RequestMapping(value = "/removeVersionLog" , method = RequestMethod.POST)
    @ResponseBody
    public Response  removeVersionLog(@RequestBody Map<String , Object> map){
        Response  response;
        try{
            response =  service.removeVersionLog(map) > 0 ? Response.getResponseSuccess() : Response.getResponseError();
        }catch (Exception e){
            e.printStackTrace();
            response = Response.getResponseError();
        }
        return  response;
    }

}
