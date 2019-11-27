package com.jzb.org.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import com.jzb.org.service.TbTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Description: 类型
 * @Author Han Bin
 */
@RestController
@RequestMapping("/org/type")
public class TbTypeController {

    @Autowired
    private TbTypeService service;

    /***
     * 获取类型
     * @param param
     * @return
     */
    @RequestMapping(value = "/getTypeList" , method = RequestMethod.POST)
    public Response getTypeList(@RequestBody Map<String , Object> param){
        Response response;
        try{
            PageInfo info = new PageInfo();
            info.setPages(JzbDataType.getInteger(param.get("pageno")) == 0 ? 1 : JzbDataType.getInteger(param.get("pageno")));
            List<Map<String , Object>> list  = service.queryTypeList(param);
            int count  = service.queryTypeListCount(param);
            response = Response.getResponseSuccess((Map<String, Object>) param.get("userinfo"));
            info.setTotal(count);
            info.setList(list);
            response.setPageInfo(info);
        }catch (Exception e){
            response = Response.getResponseError();
            JzbTools.logError(e);
        }
        return response;
    }

    /***
     * 获取类型参数
     * @param param
     * @return
     */
    @RequestMapping(value = "/getTypeInfo" , method = RequestMethod.POST)
    public Response getTypeInfo(@RequestBody Map<String , Object> param){
        Response response;
        try{
            PageInfo info = new PageInfo();
            info.setPages(JzbDataType.getInteger(param.get("pageno")) == 0 ? 1 : JzbDataType.getInteger(param.get("pageno")));
            List<Map<String , Object>> list  = service.queryTypeInfo(param);
            int count  = service.queryTypeInfoCount(param);
            response = Response.getResponseSuccess((Map<String, Object>) param.get("userinfo"));
            info.setTotal(count);
            info.setList(list);
            response.setPageInfo(info);
        }catch (Exception e){
            response = Response.getResponseError();
            JzbTools.logError(e);
        }
        return response;
    }

}
