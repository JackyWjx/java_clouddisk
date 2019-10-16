package com.jzb.org.controller;

import com.jzb.base.message.Response;
import com.jzb.org.service.TbResourceViewService;
import com.jzb.org.service.TbResourceVotesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/resourceVotes")
public class TbResourceVotesController {

    @Autowired
    private TbResourceVotesService tbResourceVotesService;

    @Autowired
    private TbResourceViewService tbResourceViewService;

    /**
     * 点赞
     * @param params
     * @return
     */
    @RequestMapping(value = "/addResourceVotes", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response addResourceVotes(@RequestBody Map<String, Object> params) {
        Response result;
        try {
            // 验证指定参数为空则返回error
            if (params.get("actid") == null || params.get("ouid") == null) {
                result = Response.getResponseError();
            } else {
                params.put("restype", "R0001");
                // 获取是否存在
                int isAlready = tbResourceVotesService.queryIsAlreadyVotes(params);
                // 判断如果该用户点过赞了就不允许点了
                if (isAlready <= 0) {
                    // 添加时间
                    params.put("addtime",System.currentTimeMillis());
                    // 添加结果
                    int count = tbResourceVotesService.addResourceVotes(params);
                    if (count > 0) {
                        // 定义返回结果
                        Map<String, Object> userInfo = (Map<String, Object>) params.get("userinfo");
                        result = Response.getResponseSuccess(userInfo);
                    } else {
                        result = Response.getResponseError();
                    }
                }else {
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





    // 获取点赞次数
    @RequestMapping(value = "/getResourceVotes",method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response getResourceVotes(@RequestBody Map<String ,Object> params){
        Response result;
        try {
            // 验证指定参数为空则返回error
            if (params.get("actid") == null ) {
                result = Response.getResponseError();
            } else {
                params.put("restype","R0001");
                Map<String, Integer> votesMap = tbResourceVotesService.queryResourceVotes(params);
                Map<String, Integer>  viewMap= tbResourceViewService.queryResourceView(params);
                // 定义返回结果
                Map<String, Object> userInfo = (Map<String, Object>) params.get("userinfo");
                result = Response.getResponseSuccess(userInfo);
                Map<String, Object> map=new HashMap();
                map.put("votes",votesMap);
                map.put("view",viewMap);
                result.setResponseEntity(map);
            }
        } catch (Exception e) {
            // 打印异常信息
            e.printStackTrace();
            result = Response.getResponseError();
        }
        return result;
    }

}
