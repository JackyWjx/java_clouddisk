package com.jzb.org.controller;

import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import com.jzb.org.service.TbContractItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author chenzhengduan
 */
@RestController
@RequestMapping(value = "/org/contractItem")
public class TbContractItemController {

    @Autowired
    private   TbContractItemService tbContractItemService;

    /**
     * 添加合同项
     * @param param
     * @return
     */
    @RequestMapping(value = "/addContractItem",method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response addContractItem(@RequestBody Map<String, Object> param){
        Response response;
        try {
            tbContractItemService.addContractItem(param);
            response=Response.getResponseSuccess((Map<String, Object>) param.get("userinfo"));
        }catch (Exception ex){
            JzbTools.logError(ex);
            response=Response.getResponseError();
        }
        return response;
    }

    /**
     * 修改
     * @param param
     * @return
     */
    @RequestMapping(value = "/updateContractItem",method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response updateContractItem(@RequestBody Map<String, Object> param){
        Response response;
        try {
            tbContractItemService.updateContractItem(param);
            response=Response.getResponseSuccess((Map<String, Object>) param.get("userinfo"));
        }catch (Exception ex){
            JzbTools.logError(ex);
            response=Response.getResponseError();
        }
        return response;
    }

    /**
     * 获取
     * @param param
     * @return
     */
    @RequestMapping(value = "/getContractItem",method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response queryContractItem(@RequestBody Map<String, Object> param){
        Response response;
        try {
            tbContractItemService.queryContractItem(param);
            response=Response.getResponseSuccess((Map<String, Object>) param.get("userinfo"));
        }catch (Exception ex){
            JzbTools.logError(ex);
            response=Response.getResponseError();
        }
        return response;
    }
}
