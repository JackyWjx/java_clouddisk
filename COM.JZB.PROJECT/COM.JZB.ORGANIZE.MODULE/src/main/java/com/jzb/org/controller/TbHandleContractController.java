package com.jzb.org.controller;

import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import com.jzb.org.service.TbContractServiceService;
import com.jzb.org.service.TbHandleContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author chenzhengduan
 */
@RestController
@RequestMapping(value = "/org/handleContract")
public class TbHandleContractController {
    @Autowired
    private TbHandleContractService tbHandleContractService;


    /**
     * 添加合同动态属性
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/addHandleContract",method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response addHandleContract(@RequestBody Map<String, Object> param){
        Response response;
        try {
            tbHandleContractService.addHandleContract(new ArrayList<>());
            response= Response.getResponseSuccess((Map<String, Object>) param.get("userinfo"));
        }catch (Exception ex){
            JzbTools.logError(ex);
            response=Response.getResponseError();
        }
        return response;
    }

    /**
     * 修改合同动态属性
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/updateHandleContract",method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response updateHandleContract(@RequestBody Map<String, Object> param){
        Response response;
        try {
            tbHandleContractService.updateHandleContract(param);
            response= Response.getResponseSuccess((Map<String, Object>) param.get("userinfo"));
        }catch (Exception ex){
            JzbTools.logError(ex);
            response=Response.getResponseError();
        }
        return response;
    }

    /**
     * 查询合同动态属性
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getHandleContract",method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response queryHandleContract(@RequestBody Map<String, Object> param){
        Response response;
        try {
            tbHandleContractService.queryHandleContract(param);
            response= Response.getResponseSuccess((Map<String, Object>) param.get("userinfo"));
        }catch (Exception ex){
            JzbTools.logError(ex);
            response=Response.getResponseError();
        }
        return response;
    }
}
