package com.jzb.org.controller;

import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbTools;
import com.jzb.org.service.TbCompanyPropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author chenzhengduan
 * 企业动态属性表
 */
@RestController
@RequestMapping(value = "/org/companyProperty")
public class TbCompanyPropertyController {

    @Autowired
    private TbCompanyPropertyService tbCompanyPropertyService;

    /**
     * 添加单位动态属性
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/addCompanyProperty", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response setCompanyProperty(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            if (JzbCheckParam.haveEmpty(param, new String[]{"cid", "typeid"})) {
                result = Response.getResponseSuccess();
            } else {
                result = tbCompanyPropertyService.addCompanyProperty(param) > 0 ? Response.getResponseSuccess((Map<String, Object>) param.get("userinfo")) : Response.getResponseError();
            }
        } catch (Exception ex) {
            // 打印异常
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 修改企业动态属性
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/updateCompanyProperty", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response updateCompanyProperty(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            result = tbCompanyPropertyService.updatePropertyByCidAndTypeid(param) > 0 ? Response.getResponseSuccess((Map<String, Object>) param.get("userinfo")) : Response.getResponseError();
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 所有业主-业主列表-分配售后人员
     * @param param
     * @return
     */
    @RequestMapping(value = "/saveCompanyProperty",method = RequestMethod.POST)
    @CrossOrigin
    public Response saveCompanyProperty(@RequestBody Map<String, Object> param) {
        Response result;
        try {
           int count =  tbCompanyPropertyService.saveCompanyProperty(param);
           //如果返回值大于0 响应成功信息
            if (count > 0) {
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                result = Response.getResponseSuccess(userInfo);
            } else {
                //返回失败的信息
                result = Response.getResponseError();
            }
        } catch (Exception e) {
            //打印错误信息
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 所有业主-业主列表-设置等级
     * @param param
     * @return
     */
    @RequestMapping(value = "/saveCompanyPropertys",method = RequestMethod.POST)
    @CrossOrigin
    public Response saveCompanyPropertys(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            int count =  tbCompanyPropertyService.saveCompanyPropertys(param);
            //如果返回值大于0 响应成功信息
            if (count > 0) {
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                result = Response.getResponseSuccess(userInfo);
            } else {
                //返回失败的信息
                result = Response.getResponseError();
            }
        } catch (Exception e) {
            //打印错误信息
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }
}
