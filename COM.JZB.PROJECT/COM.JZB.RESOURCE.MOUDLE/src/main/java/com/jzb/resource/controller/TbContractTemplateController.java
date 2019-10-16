package com.jzb.resource.controller;

import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbPageConvert;
import com.jzb.base.util.JzbTools;
import com.jzb.resource.service.TbContractTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author chenzhengduan
 * 合同模板
 */
@RestController
@RequestMapping(value = "/resource/contractTemplate")
public class TbContractTemplateController {
    @Autowired
    private TbContractTemplateService tbContractTemplateService;

    /**
     * 查询合同模板
     * @param param
     * @return
     */
    @RequestMapping(value = "/getContractTemplate", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response getContractTemplate(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 判断指定参数为空则返回error
            if(JzbCheckParam.haveEmpty(param,new String[]{"page","rows"})){
                result=Response.getResponseError();
            }else {
                // 设置好分页参数
                JzbPageConvert.setPageRows(param);

                // 获取结果集
                List<Map<String, Object>> list = tbContractTemplateService.queryContractTemplate(param);

                // 定义返回结果
                Map<String, Object> userinfo=(Map<String, Object>) param.get("userinfo");
                result = Response.getResponseSuccess(userinfo);
                PageInfo pi=new PageInfo();
                pi.setList(list);
                pi.setTotal(tbContractTemplateService.getContractTemplateCount(param));
                result.setPageInfo(pi);
            }

        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 添加合同模板
     * @param param
     * @return
     */
    @RequestMapping(value = "/addContractTemplate", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response addContractTemplate(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            int count = tbContractTemplateService.addContractTemplate(param);
            if (count > 0) {
                // 定义返回结果
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                result = Response.getResponseSuccess(userInfo);
            } else {
                result = Response.getResponseError();
            }
            result = Response.getResponseSuccess();
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 修改合同模板
     * @param param
     * @return
     */
    @RequestMapping(value = "/updateContractTemplate", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response updateContractTemplate(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            int count = tbContractTemplateService.updateContractTemplate(param);
            if (count > 0) {
                // 定义返回结果
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                result = Response.getResponseSuccess(userInfo);
            } else {
                result = Response.getResponseError();
            }
            result = Response.getResponseSuccess();
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }
}
