package com.jzb.auth.controller;

import com.jzb.auth.dao.PersonBoardMapper;
import com.jzb.auth.service.PersonBoardService;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.jnlp.PersistenceService;
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
    @RequestMapping(value = "/getAuthCount",method = RequestMethod.POST)
    public Response getInfo(@RequestBody Map<String,Object> param){
        Response response;
        try {
            Map<String,Object> userinfo = (Map<String, Object>) param.get("userinfo");

            Map<String,Object> amap = boardService.getAuthCount(param);
            response = Response.getResponseSuccess(userinfo);
            response.setResponseEntity(amap);
        } catch (Exception e) {
            JzbTools.logError(e);
            response = Response.getResponseError();
        }
       return response;
    }





}
