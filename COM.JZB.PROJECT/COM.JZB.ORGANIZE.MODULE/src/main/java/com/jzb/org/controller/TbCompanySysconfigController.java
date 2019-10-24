package com.jzb.org.controller;

import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbTools;
import com.jzb.org.service.TbCompanySysconfigService;
import org.apache.commons.math3.analysis.function.Exp;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author chenzhengduan
 * 企业系统配置表
 */
@RestController
@RequestMapping(value = "/org/companySysconfig")
public class TbCompanySysconfigController {

    @Autowired
    private TbCompanySysconfigService tbCompanySysconfigService;

    /**
     * 添加二级域名
     * @param param
     * @return
     */
    @RequestMapping(value = "/addCompanySysconfig", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response addCompanySysconfig(@RequestBody Map<String, Object> param) {
        Response response;
        try {
            // 验证指定参数为空则返回error
            if (JzbCheckParam.haveEmpty(param, new String[]{"curl", "logo", "compname", "background", "systemname", "adduid"})) {
                response = Response.getResponseError();
            } else {
                response = tbCompanySysconfigService.addCompanySysconfig(param) > 0 ? Response.getResponseSuccess((Map<String, Object>) param.get("userinfo")) : Response.getResponseError();
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            response = Response.getResponseError();
        }
        return response;
    }
}
