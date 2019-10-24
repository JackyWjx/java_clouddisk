package com.jzb.org.controller;

import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import com.jzb.org.service.TbContractItemService;
import com.jzb.org.service.TbContractServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author chenzhengduan
 */
@RestController
@RequestMapping(value = "/org/contractService")
public class TbContractServiceController {

    @Autowired
    private TbContractServiceService tbContractServiceService;

    @RequestMapping(value = "/",method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response a(@RequestBody Map<String, Object> param){
        Response response;
        try {

            response= Response.getResponseSuccess((Map<String, Object>) param.get("userinfo"));
        }catch (Exception ex){
            JzbTools.logError(ex);
            response=Response.getResponseError();
        }
        return response;
    }

    @RequestMapping(value = "/",method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response v(@RequestBody Map<String, Object> param){
        Response response;
        try {

            response= Response.getResponseSuccess((Map<String, Object>) param.get("userinfo"));
        }catch (Exception ex){
            JzbTools.logError(ex);
            response=Response.getResponseError();
        }
        return response;
    }

    @RequestMapping(value = "/",method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response as(@RequestBody Map<String, Object> param){
        Response response;
        try {

            response= Response.getResponseSuccess((Map<String, Object>) param.get("userinfo"));
        }catch (Exception ex){
            JzbTools.logError(ex);
            response=Response.getResponseError();
        }
        return response;
    }
}