package com.jzb.open.controller;


import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbTools;
import com.jzb.open.service.OpenPageService;
import com.jzb.open.service.TbApplicationVerifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/open/ApplicationVerify")
public class TbApplicationVerifyController {


    @Autowired
    private TbApplicationVerifyService tbApplicationVerifyService;


    /**
     * 提交应用列表到审批列表
     * @param param
     * @return
     */
    @RequestMapping(value = "/saveApplicationVerify",method = RequestMethod.POST)
    @CrossOrigin
    public Response saveApplicationVerify(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            //如果指定参数为空则返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"appid"})) {
                result = Response.getResponseError();
            } else {
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                param.put("adduid", userInfo.get("uid"));
                param.put("appline", JzbDataType.getInteger(param.get("appline")));
                int count = tbApplicationVerifyService.saveApplicationVerify(param);
                //获取用户信息
                //响应成功结果
                result = count > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
            }
        } catch (Exception e) {
            //打印错误信息
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 提交应用菜单列表到审批表
     * @param param
     * @return
     */
    @RequestMapping(value = "/saveApplicationMenuVerify", method = RequestMethod.POST)
    @CrossOrigin
    public Response saveApplicationMenuVerify(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            //如果指定参数为空则返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"appid"})) {
                result = Response.getResponseError();
            } else {
                //获取用户信息
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                param.put("adduid", userInfo.get("uid"));
                int count = tbApplicationVerifyService.saveApplicationMenuVerify(param);
                //获取用户信息
                //响应成功结果
                result = count > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
            }
        } catch (Exception e) {
            //打印错误信息
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 提交应用页面到审批表
     * @param param
     * @return
     */
    @RequestMapping(value = "/saveApplicationPageVerify", method = RequestMethod.POST)
    @CrossOrigin
    public Response saveApplicationPageVerify(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            //如果指定参数为空则返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"appid"})) {
                result = Response.getResponseError();
            } else {
                //获取用户信息
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                param.put("adduid", userInfo.get("uid"));
                int count = tbApplicationVerifyService.saveApplicationPageVerify(param);
                //获取用户信息
                //响应成功结果
                result = count > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
            }
        } catch (Exception e) {
            //打印错误信息
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

}
