package com.jzb.resource.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.data.date.JzbDateStr;
import com.jzb.base.data.date.JzbDateUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.resource.service.TbMethodDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenzhengduan
 * @date 2019-8-15
 * 获取方法论资料
 */
@RestController
@RequestMapping(value = "/methodData")
public class TbMethodDataController {

    @Autowired
    private TbMethodDataService tbMethodDataService;

    /**
     * 查询方法论资料
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getMethodData", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    @CrossOrigin
    public Response getMethodData(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 验证如果指定参数为空则返回404  error
            if (JzbCheckParam.haveEmpty(param, new String[]{"typeid"})) {
                result = Response.getResponseError();
            } else {
                // 查询结果
                List<Map<String, Object>> list = tbMethodDataService.quertMethodData(param);
                // 遍历转格式
                for (int i = 0, l = list.size(); i < l; i++) {
                    list.get(i).put("addtime", JzbDateUtil.toDateString(JzbDataType.getLong(list.get(i).get("addtime")), JzbDateStr.yyyy_MM_dd));
                }
                // 定义pageinfo
                PageInfo pi = new PageInfo();
                pi.setList(list);

                // 定义返回结果
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
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
     * 添加方法论资料
     *
     * @param param
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/addMethodData", method = RequestMethod.POST)
    @ResponseBody
    public Response addMethodData(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 验证指定值为空则返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"typeid"})) {
                result = Response.getResponseError();
            } else {
                // 添加返回值大于0 则添加成功
                String dataid = param.get("typeid") + JzbRandom.getRandomCharCap(4);
                param.put("dataid", dataid);
                int count = tbMethodDataService.saveMethodData(param);
                if (count > 0) {
                    // 定义返回结果
                    Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                    result = Response.getResponseSuccess(userInfo);
                    // 将dataid返回
                    Map<String, Object> map = new HashMap<>();
                    map.put("dataid", dataid);
                    result.setResponseEntity(map);
                } else {
                    result = Response.getResponseError();
                }
            }
        } catch (Exception e) {
            // 打印异常信息
            e.printStackTrace();
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 修改方法论资料
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/updateMethodData", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response updateMethodData(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 验证指定值为空则返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"typeid", "dataid"})) {
                result = Response.getResponseError();
            } else {
                // 修改返回值大于0 则修改成功
                int count = tbMethodDataService.updateMethodData(param);
                if (count > 0) {
                    // 定义返回结果
                    Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                    result = Response.getResponseSuccess(userInfo);
                } else {
                    result = Response.getResponseError();
                }
            }
        } catch (Exception e) {
            // 打印异常信息
            e.printStackTrace();
            result = Response.getResponseError();
        }
        return result;
    }

    @RequestMapping(value = "/updateMethodTarget", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response updateMethodTarget() {
        Response result;
        try {

        } catch (Exception ex) {
            JzbTools.logError(ex);
        }
        return null;
    }

}