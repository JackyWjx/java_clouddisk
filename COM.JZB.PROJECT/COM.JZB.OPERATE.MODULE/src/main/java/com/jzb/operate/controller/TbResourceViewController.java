package com.jzb.operate.controller;

import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.operate.service.TbResourceViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author chenzhengduan
 * 活动浏览（无登录）
 */
@RestController
@RequestMapping(value = "/resourceView")
public class TbResourceViewController {

    @Autowired
    private TbResourceViewService tbResourceViewService;

    /**
     * 浏览
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/addResourceView", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response addResourceVies(@RequestBody Map<String, Object> params) {
        Response result;
        try {
            // 验证指定参数为空则返回error
            if (JzbCheckParam.haveEmpty(params,new String[]{"actid","ouid"})) {
                result = Response.getResponseError();
            } else {
                params.put("restype", "R0001");
                // 获取是否存在
                int isAlready = tbResourceViewService.queryIsAlreadyView(params);
                // 判断如果该用户点过赞了就不允许点了
                if (isAlready <= 0) {
                    // 添加时间
                    params.put("addtime", System.currentTimeMillis());
                    // 添加结果
                    int count = tbResourceViewService.addResourceView(params);
                    if (count > 0) {
                        // 定义返回结果
                        Map<String, Object> userInfo = (Map<String, Object>) params.get("userinfo");
                        result = Response.getResponseSuccess(userInfo);
                    } else {
                        result = Response.getResponseError();
                    }
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
}