package com.jzb.operate.controller;

import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbTools;
import com.jzb.operate.service.TbSalesCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author chenzhengduan
 * 业主单位  与公海状态为2的业主同步
 */
@RestController
@RequestMapping(value = "/operate/salesCompany")
public class TbSalesCompanyController {

    @Autowired
    private TbSalesCompanyService tbSalesCompanyServicel;

    /**
     * 添加业主单位(暂未用到)
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/addSalesCompany", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response addSalesCompany(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            if (JzbCheckParam.haveEmpty(param, new String[]{"cid"})) {
                result = Response.getResponseError();
            } else {
                result = tbSalesCompanyServicel.addSalesCompany(param) > 0 ? Response.getResponseSuccess((Map<String, Object>) param.get("userinfo")) : Response.getResponseError();
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }
}
