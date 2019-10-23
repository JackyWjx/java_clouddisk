package com.jzb.operate.controller;

import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import com.jzb.operate.service.TbCompanyMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author chenzhengduan、
 * 企业方法论
 */
@RestController
@RequestMapping(value = "/operate/companyMethod")
public class TbCompanyMethodController {

    @Autowired
    private TbCompanyMethodService tbCompanyMethodService;

    /**
     * 导入方法论
     * @param param
     * @return
     */
    @RequestMapping(value = "/addCompanyMethod",method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response addCompanyMethod(@RequestBody Map<String, Object> param){
        Response result;
        try {
            result=tbCompanyMethodService.addCompanyMethod((List<Map<String, Object>>) param.get("list"))>0?Response.getResponseSuccess((Map<String, Object>) param.get("userinfo")):Response.getResponseError();
        }catch (Exception ex){
            JzbTools.logError(ex);
            result=Response.getResponseError();
        }
        return result;
    }
}
