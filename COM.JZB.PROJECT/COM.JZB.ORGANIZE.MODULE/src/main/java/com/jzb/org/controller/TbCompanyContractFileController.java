package com.jzb.org.controller;

import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import com.jzb.org.service.TbCompanyContractFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author chenzhengduan
 * 合同附件表
 */
@RestController
@RequestMapping(value = "/org/companyContractFile")
public class TbCompanyContractFileController {

    @Autowired
    private TbCompanyContractFileService tbCompanyContractFileService;

    /**
     * 获取文件
     * @param param
     * @return
     */
    @RequestMapping(value = "/getFileByConId", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response getFileByConId(@RequestBody Map<String, Object> param) {
        Response response;
        try {
            response=Response.getResponseSuccess();
        }catch (Exception ex){
            JzbTools.logError(ex);
            response=Response.getResponseError();
        }
        return response;
    }

    /**
     * 添加附件
     * @param param
     * @return
     */
    @RequestMapping(value = "/addFileByConId", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response addFileByConId(@RequestBody Map<String, Object> param) {
        Response response;
        try {
            response=Response.getResponseSuccess();
        }catch (Exception ex){
            JzbTools.logError(ex);
            response=Response.getResponseError();
        }
        return response;
    }

    /**
     * 修改附件
     * @param param
     * @return
     */
    @RequestMapping(value = "/updateFileStatusByConId", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response updateFileStatusByConId(@RequestBody Map<String, Object> param) {
        Response response;
        try {
            response=Response.getResponseSuccess();
        }catch (Exception ex){
            JzbTools.logError(ex);
            response=Response.getResponseError();
        }
        return response;
    }



}
