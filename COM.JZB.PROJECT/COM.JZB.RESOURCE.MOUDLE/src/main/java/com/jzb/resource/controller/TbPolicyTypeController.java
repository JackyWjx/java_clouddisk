package com.jzb.resource.controller;

import com.alibaba.fastjson.JSONArray;
import com.jzb.base.message.Response;
import com.jzb.resource.service.TbPolicyTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author chenzhengduan
 * 获取政策类型
 */
@RestController
@RequestMapping(value = "/policyType")
public class TbPolicyTypeController {

    @Autowired
    private TbPolicyTypeService tbPolicyTypeService;

    /**
     * 查询政策类型（父子级）
     * @param params
     * @return
     */
    @RequestMapping(value = "/getPolicyType",method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response getPolicyType(@RequestBody(required = false) Map<String ,Object> params){
        Response result;
        try {

            // 获取返回jsonArray
            JSONArray objects = tbPolicyTypeService.queryPolicyType(params);

            // 定义返回结果
            result = Response.getResponseSuccess();
            result.setResponseEntity(objects);

        }catch (Exception e){

            // 打印异常信息
            e.printStackTrace();
            result=Response.getResponseError();

        }
        return result;
    }
}
