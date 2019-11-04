package com.jzb.org.controller;

import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import com.jzb.org.service.TbContractItemService;
import com.jzb.org.service.TbContractProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author chenzhengduan
 */
@RestController
@RequestMapping(value = "/org/contractProduct")
public class TbContractProductController {

    @Autowired
    private TbContractProductService tbContractProductService;

    /**
     * 添加企业合同产品功能
     * @param param
     * @return
     */
    @RequestMapping(value = "/addContractProduct",method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response addContractProduct(@RequestBody Map<String, Object> param){
        Response response;
        try {
            tbContractProductService.addContractProduct(new ArrayList<>());
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
    @RequestMapping(value = "/updateContractProduct",method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response updateContractProduct(@RequestBody Map<String, Object> param){
        Response response;
        try {
            tbContractProductService.updateContractProduct(param);
            response= Response.getResponseSuccess((Map<String, Object>) param.get("userinfo"));
        }catch (Exception ex){
            JzbTools.logError(ex);
            response=Response.getResponseError();
        }
        return response;
    }

    /**
     * 查询企业合同产品功能
     * @param param
     * @return
     */
    @RequestMapping(value = "/getContractProduct",method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response queryContractProduct(@RequestBody Map<String, Object> param){
        Response response;
        try {
            tbContractProductService.queryContractProduct(param);
            response= Response.getResponseSuccess((Map<String, Object>) param.get("userinfo"));
        }catch (Exception ex){
            JzbTools.logError(ex);
            response=Response.getResponseError();
        }
        return response;
    }
}
