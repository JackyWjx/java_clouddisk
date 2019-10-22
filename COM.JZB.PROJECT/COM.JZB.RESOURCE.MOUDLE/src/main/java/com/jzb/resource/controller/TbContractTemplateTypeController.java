package com.jzb.resource.controller;

import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbPageConvert;
import com.jzb.base.util.JzbTools;
import com.jzb.resource.service.TbContractTemplateTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author chenzhengduan
 * 合同模板类型
 */
@RestController
@RequestMapping(value = "/resource/contracttemptype")
public class TbContractTemplateTypeController {


    @Autowired
    private TbContractTemplateTypeService tbContactTemplateTypeService;

    /**
     * 获取合同模板分类
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getContractTemplateType", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response getContractTemplateType(@RequestBody(required = false) Map<String, Object> param) {
        Response result;
        try {
            if (JzbCheckParam.haveEmpty(param, new String[]{"pageno", "pagesize"})) {
                result = Response.getResponseError();
            } else {
                JzbPageConvert.setPageRows(param);
                List<Map<String, Object>> list = tbContactTemplateTypeService.getContactTemplateType(param);
                Map<String, Object> userinfo=(Map<String, Object>) param.get("userinfo");
                result=Response.getResponseSuccess(userinfo);
                PageInfo pi=new PageInfo();
                pi.setList(list);
                pi.setTotal(list.size());
                result.setPageInfo(pi);
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }
}
