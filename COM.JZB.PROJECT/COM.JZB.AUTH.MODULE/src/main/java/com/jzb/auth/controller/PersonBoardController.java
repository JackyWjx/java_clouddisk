package com.jzb.auth.controller;

import com.jzb.auth.service.PersonBoardService;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author chenhui
 * @description
 * @time 2019/12/11
 * @other
 */
@RestController
@RequestMapping("/auth/board")
public class PersonBoardController {

    @Autowired
    PersonBoardService boardService;

    /**
     * 驾驶舱个人看板 用户认证统计
     */
    @RequestMapping(value = "/getAuthCount", method = RequestMethod.POST)
    public Response getInfo(@RequestBody Map<String, Object> param) {
        Response response;
        try {
            Map<String, Object> userinfo = (Map<String, Object>) param.get("userinfo");

            Map<String, Object> amap = boardService.getAuthCount(param);
            response = Response.getResponseSuccess(userinfo);
            response.setResponseEntity(amap);
        } catch (Exception e) {
            JzbTools.logError(e);
            response = Response.getResponseError();
        }
        return response;
    }

    /**
     * 加入单位发送系统消息查询加入单位人的姓名
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getCname", method = RequestMethod.POST)
    @CrossOrigin
    public Response getCname(@RequestBody Map<String, Object> param) {
        Response response;
        try {
            Map<String, Object> map = boardService.getCname(param);
            //响应成功消息
            response = Response.getResponseSuccess();
            response.setResponseEntity(map);
        } catch (Exception e) {
            //打印错误信息
            response = Response.getResponseError();
            JzbTools.logError(e);
        }
        return response;
    }


}
