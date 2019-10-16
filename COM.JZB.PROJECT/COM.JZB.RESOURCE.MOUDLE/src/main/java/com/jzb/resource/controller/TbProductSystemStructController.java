package com.jzb.resource.controller;

import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import com.jzb.resource.service.TbProductSystemStructService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/resource/systemStruct")
public class TbProductSystemStructController {

    @Autowired
    private TbProductSystemStructService tbProductSystemStructService;


    /**
     * 获取Struct
     * @param param
     * @return
     */
    @RequestMapping(value = "/getProductSystemStructList",method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response getProductSystemMenuList(@RequestBody Map<String, Object> param){
        Response result;
        try {
            List<Map<String, Object>> list = tbProductSystemStructService.queryStructList(param);
            result= Response.getResponseSuccess();
            PageInfo pi=new PageInfo();
            pi.setList(list);
            result.setPageInfo(pi);
        }catch (Exception ex){
            JzbTools.logError(ex);
            result= Response.getResponseError();
        }
        return result;
    }

    /**
     * add Struct
     * @param param
     * @return
     */
    @RequestMapping(value = "/addProductSystemStructList",method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response addProductSystemStructList(@RequestBody Map<String, Object> param){
        Response result;
        try {
            result=tbProductSystemStructService.addStructList(param)>0? Response.getResponseSuccess(): Response.getResponseError();
        }catch (Exception ex){
            JzbTools.logError(ex);
            result= Response.getResponseError();
        }
        return result;
    }

    /**
     * edit Struct
     * @param param
     * @return
     */
    @RequestMapping(value = "/editProductSystemStructList",method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response editProductSystemStructList(@RequestBody Map<String, Object> param){
        Response result;
        try {
            result=tbProductSystemStructService.updateStructList(param)>0? Response.getResponseSuccess(): Response.getResponseError();
        }catch (Exception ex){
            JzbTools.logError(ex);
            result= Response.getResponseError();
        }
        return result;
    }
}
