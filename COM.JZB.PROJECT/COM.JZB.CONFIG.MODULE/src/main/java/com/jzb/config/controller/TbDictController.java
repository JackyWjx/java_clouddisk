package com.jzb.config.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import com.jzb.config.service.TbDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @Description: 字典/字典类型
 * @Author Han Bin
 */
public class TbDictController {

    /**
     *  字典业务
     */
    @Autowired
    TbDictService service;


    /**
     *  查询字典
     */
    @RequestMapping("/queryDictItem")
    @ResponseBody
    public Response queryDictItem(@RequestBody Map<String , Object> paraMap){
        Response response;
        try{
            PageInfo pageInfo = new PageInfo();
            pageInfo.setPages(JzbDataType.getInteger(paraMap.get("page")) == 0 ? 1 : JzbDataType.getInteger(paraMap.get("page")));
            List<Map<String , Object>> list = service.queryDictItem(paraMap);
            int count  =  service.queryDictItemCount(paraMap);
            response =  Response.getResponseSuccess((Map)paraMap.get("userinfo"));
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
     *  查询字典类型
     */
    @RequestMapping("/queryDictType")
    @ResponseBody
    public Response queryDictType(@RequestBody Map<String , Object> paraMap){
        Response response;
        try{
            PageInfo pageInfo = new PageInfo();
            pageInfo.setPages(JzbDataType.getInteger(paraMap.get("page")) == 0 ? 1 : JzbDataType.getInteger(paraMap.get("page")));
            List<Map<String , Object>> list = service.queryDictType(paraMap);
            int count  =  service.queryDictTypeCount(paraMap);
            response =  Response.getResponseSuccess((Map)paraMap.get("userinfo"));
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
     *  模糊查询字典
     */
    @RequestMapping("/seachDictItem")
    @ResponseBody
    public Response seachDictItem(@RequestBody Map<String , Object> paraMap){
        Response response;
        try{
            PageInfo pageInfo = new PageInfo();
            pageInfo.setPages(JzbDataType.getInteger(paraMap.get("page")) == 0 ? 1 : JzbDataType.getInteger(paraMap.get("page")));
            List<Map<String , Object>> list = service.seachDictItem(paraMap);
            int count  =  service.seachDictItemCount(paraMap);
            response =  Response.getResponseSuccess((Map)paraMap.get("userinfo"));
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
     *  模糊查询字典类型
     */
    @RequestMapping("/seachDictType")
    @ResponseBody
    public Response seachDictType(@RequestBody Map<String , Object> paraMap){
        Response response;
        try{
            PageInfo pageInfo = new PageInfo();
            pageInfo.setPages(JzbDataType.getInteger(paraMap.get("page")) == 0 ? 1 : JzbDataType.getInteger(paraMap.get("page")));
            List<Map<String , Object>> list = service.seachDictType(paraMap);
            int count  =  service.seachDictTypeCount(paraMap);
            response =  Response.getResponseSuccess((Map)paraMap.get("userinfo"));
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
     *  添加字典
     */
    @RequestMapping("/saveDictItem")
    @ResponseBody
    public Response saveDictItem(@RequestBody Map<String , Object> paraMap){
        Response response;
        try{
            response =  service.saveDictItem(paraMap) > 0 ? Response.getResponseSuccess((Map)paraMap.get("userinfo")) : Response.getResponseError();
        }catch (Exception e){
            JzbTools.logError(e);
            response =  Response.getResponseError();
        }
        return response;
    }

    /**
     *  添加字典类型
     */
    @RequestMapping("/saveDictType")
    @ResponseBody
    public Response saveDictType(@RequestBody Map<String , Object> paraMap){
        Response response;
        try{
            response =  service.saveDictType(paraMap) > 0 ? Response.getResponseSuccess((Map)paraMap.get("userinfo")) : Response.getResponseError();
        }catch (Exception e){
            JzbTools.logError(e);
            response =  Response.getResponseError();
        }
        return response;
    }

    /**
     *  修改字典
     */
    @RequestMapping("/updateDictItem")
    @ResponseBody
    public Response updateDictItem(@RequestBody Map<String , Object> paraMap){
        Response response;
        try{
            response =  service.updateDictItem(paraMap) > 0 ? Response.getResponseSuccess((Map)paraMap.get("userinfo")) : Response.getResponseError();
        }catch (Exception e){
            JzbTools.logError(e);
            response =  Response.getResponseError();
        }
        return response;
    }

    /**
     *  修改字典类型
     */
    @RequestMapping("/updateDictType")
    @ResponseBody
    public Response updateDictType(@RequestBody Map<String , Object> paraMap){
        Response response;
        try{
            response =  service.updateDictType(paraMap) > 0 ? Response.getResponseSuccess((Map)paraMap.get("userinfo")) : Response.getResponseError();
        }catch (Exception e){
            JzbTools.logError(e);
            response =  Response.getResponseError();
        }
        return response;
    }

    /**
     *  禁用字典
     */
    @RequestMapping("/removeDictItem")
    @ResponseBody
    public Response removeDictItem(@RequestBody Map<String , Object> paraMap){
        Response response;
        try{
            response =  service.removeDictItem(paraMap) > 0 ? Response.getResponseSuccess((Map)paraMap.get("userinfo")) : Response.getResponseError();
        }catch (Exception e){
            JzbTools.logError(e);
            response =  Response.getResponseError();
        }
        return response;
    }

    /**
     *  禁用字典类型
     */
    @RequestMapping("/removeDictType")
    @ResponseBody
    public Response removeDictType(@RequestBody Map<String , Object> paraMap){
        Response response;
        try{
            response =  service.removeDictType(paraMap) > 0 ? Response.getResponseSuccess((Map)paraMap.get("userinfo")) : Response.getResponseError();
        }catch (Exception e){
            JzbTools.logError(e);
            response =  Response.getResponseError();
        }
        return response;
    }


}
