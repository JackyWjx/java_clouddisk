package com.jzb.resource.controller;

import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbTools;
import com.jzb.resource.service.TbMethodTargetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author chenzhengduan
 * @date 2019-8-15
 * 方法论目标
 */
@RestController
@RequestMapping(value = "/methodTarget")
public class TbMethodTargetController {

    @Autowired
    private TbMethodTargetService tbMethodTargetService;

    /**
     * 获取方法论目标
     * @param params
     * @return
     */
    @RequestMapping(value = "/getMethodTarget", method = RequestMethod.POST)
    @ResponseBody
    public Response getMethodTarget(@RequestBody Map<String, Object> params) {
        Response result;
        try {
            // 验证指定参数 为空则返回404 error
            if (JzbCheckParam.haveEmpty(params, new String[]{"dataid"})) {
                result = Response.getResponseError();
            } else {
                // 查询结果
                List<Map<String, Object>> list = tbMethodTargetService.queryMethodTarget(params);
                // 定义pageinfo
                PageInfo pi = new PageInfo<>();
                pi.setList(list);

                // 定义返回response
                // 定义返回结果
                Map<String, Object> userInfo = (Map<String, Object>) params.get("userinfo");
                result = Response.getResponseSuccess(userInfo);
                result.setPageInfo(pi);
            }
        } catch (Exception e) {
            // 打印异常信息
            e.printStackTrace();
            result = Response.getResponseError();
        }
        return result;
    }


    /**
     * 获取方法论目标
     * @param params
     * @return
     */
    @RequestMapping(value = "/getMethodTargetByTypeid", method = RequestMethod.POST)
    @ResponseBody
    public Response getMethodTargetByTypeid(@RequestBody Map<String, Object> params) {
        Response result;
        try {
            // 验证指定参数 为空则返回404 error
            if (JzbCheckParam.haveEmpty(params, new String[]{"typeid"})) {
                result = Response.getResponseError();
            } else {
                // 查询结果
                List<Map<String, Object>> list = tbMethodTargetService.queryMethodTargetByTypeid(params);
                // 定义pageinfo
                PageInfo pi = new PageInfo<>();
                pi.setList(list);

                // 定义返回response
                // 定义返回结果
                Map<String, Object> userInfo = (Map<String, Object>) params.get("userinfo");
                result = Response.getResponseSuccess(userInfo);
                result.setPageInfo(pi);
            }
        } catch (Exception e) {
            // 打印异常信息
            e.printStackTrace();
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     *  添加方法论目标
     * @param param
     * @return
     */
    @RequestMapping(value = "/addMethodTarget", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response addMethodTarget(@RequestBody Map<String, Object> param) {
        Response result;
        try {

            List<Map<String, Object>> list=(List<Map<String, Object>>) param.get("list");
            // 调用添加目标的方法

            int count = tbMethodTargetService.addMethodTarget(list);
            if (count > 0) {
                // 定义返回结果
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                result = Response.getResponseSuccess(userInfo);
            } else {
                result = Response.getResponseError();
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }



    @RequestMapping(value = "/deleteMethodTarget",method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response deleteMethod(@RequestBody Map<String, Object> param){
        Response result;
        try {
            int count=0;
            List<Map<String, Object>> list=(List<Map<String, Object>>) param.get("list");
            for (int i=0,l=list.size();i<l;i++){
                list.get(i).put("status",2);
                count= tbMethodTargetService.updateMethodTargetStatus(list.get(i));
            }
            // 调用添加目标的方法
            if (count > 0) {
                // 定义返回结果
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                result = Response.getResponseSuccess(userInfo);
            } else {
                result = Response.getResponseError();
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

}