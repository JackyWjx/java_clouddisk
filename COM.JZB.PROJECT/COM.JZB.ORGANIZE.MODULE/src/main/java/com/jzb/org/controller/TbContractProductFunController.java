package com.jzb.org.controller;

import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import com.jzb.org.service.TbContractProductFunService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author chenzhengduan
 */
@RestController
@RequestMapping(value = "/org/contractProductFun")
public class TbContractProductFunController {
    @Autowired
    private TbContractProductFunService tbContractProductFunService;

    /**
     * 添加企业合同产品功能
     * @param param
     * @return
     */
    @RequestMapping(value = "/addContractProductFun",method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response addContractProductFun(@RequestBody Map<String, Object> param){
        Response response;
        try {
            tbContractProductFunService.addContractProductFun(new ArrayList<>());
            response= Response.getResponseSuccess((Map<String, Object>) param.get("userinfo"));
        }catch (Exception ex){
            JzbTools.logError(ex);
            response=Response.getResponseError();
        }
        return response;
    }

    /**
     * 修改企业合同产品功能
     * @param param
     * @return
     */
    @RequestMapping(value = "/updateContractProductFun",method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response updateContractProductFun(@RequestBody Map<String, Object> param){
        Response response;
        try {
            tbContractProductFunService.updateContractProductFun(param);
            response= Response.getResponseSuccess((Map<String, Object>) param.get("userinfo"));
        }catch (Exception ex){
            JzbTools.logError(ex);
            response=Response.getResponseError();
        }
        return response;
    }

    /**
     * 添加企业合同产品功能
     * @param param
     * @return
     */
    @RequestMapping(value = "/getContractProductFun",method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response queryContractProductFun(@RequestBody Map<String, Object> param){
        Response response;
        try {
            tbContractProductFunService.queryContractProductFun(param);
            response= Response.getResponseSuccess((Map<String, Object>) param.get("userinfo"));
        }catch (Exception ex){
            JzbTools.logError(ex);
            response=Response.getResponseError();
        }
        return response;
    }

}
