package com.jzb.org.controller;

import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbTools;
import com.jzb.org.api.auth.CompanyAuthTempApi;
import com.jzb.org.api.auth.TbUserControlAuthApi;
import com.jzb.org.service.TbCompanyProductService;
import com.jzb.org.service.TbCompanySysconfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequestMapping(value = "/org/companyAuth")
@RestController
public class TbCompanyAuthController {

    @Autowired
    private CompanyAuthTempApi companyAuthTempApi;


    @Autowired
    private TbCompanyProductService tbCompanyProductService;


    @Autowired
    private TbCompanySysconfigService tbCompanySysconfigService;

    @Autowired
    private TbUserControlAuthApi tbUserControlAuthApi;

    /**
     * 获取授权状态
     * @return
     */
    @RequestMapping(value = "/getCompanyAuth",method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    @CrossOrigin
    public Response getCompanyAuth(@RequestBody Map<String, Object> param){
        Response result;
        try {
            if(JzbCheckParam.haveEmpty(param,new String[]{"cid"})){
                result=Response.getResponseError();
            }else {
                Response companyIsAuth = companyAuthTempApi.getCompanyIsAuth(param);
                int count = tbCompanyProductService.queryCompanyProductIsExists(param);
                String curl = tbCompanySysconfigService.queryCompanySysconfig(param);
                Response companyIsAuth1 = tbUserControlAuthApi.getCompanyIsAuth(param);
                Map<String, Object> map=new HashMap<>();
                map.put("temp",companyIsAuth.getResponseEntity());
                map.put("computerAuth",count);
                map.put("levelTwo",curl);
                map.put("memo","");
                map.put("auth",companyIsAuth1.getResponseEntity());
                result=Response.getResponseSuccess();
                result.setResponseEntity(map);
            }
        }catch (Exception ex){
            JzbTools.logError(ex);
            result=Response.getResponseError();
        }
        return result;
    }
}
