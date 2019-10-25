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

    /**
     * 添加企业合同服务
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/addContractService",method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response addContractService(@RequestBody Map<String, Object> param){
        Response response;
        try {
            tbContractServiceService.addContractService(param);
            response= Response.getResponseSuccess((Map<String, Object>) param.get("userinfo"));
        }catch (Exception ex){
            JzbTools.logError(ex);
            response=Response.getResponseError();
        }
        return response;
    }

    /**
     * 修改企业合同服务
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/updateContractService",method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response updateContractService(@RequestBody Map<String, Object> param){
        Response response;
        try {
            tbContractServiceService.updateContractService(param);
            response= Response.getResponseSuccess((Map<String, Object>) param.get("userinfo"));
        }catch (Exception ex){
            JzbTools.logError(ex);
            response=Response.getResponseError();
        }
        return response;
    }

    /**
     * 查询企业合同服务
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getContractService",method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response queryContractService(@RequestBody Map<String, Object> param){
        Response response;
        try {
            tbContractServiceService.queryContractService(param);
            response= Response.getResponseSuccess((Map<String, Object>) param.get("userinfo"));
        }catch (Exception ex){
            JzbTools.logError(ex);
            response=Response.getResponseError();
        }
        return response;
    }
}
