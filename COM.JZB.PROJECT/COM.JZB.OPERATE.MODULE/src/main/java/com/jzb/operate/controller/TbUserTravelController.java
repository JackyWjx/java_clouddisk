package com.jzb.operate.controller;

import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import com.jzb.operate.service.TbUserTravelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author chenzhengduan
 * 用户出差记录表
 */
@RestController
@RequestMapping(value = "/userTravel")
public class TbUserTravelController {

    @Autowired
    private TbUserTravelService tbUserTravelService;

    /**
     * 添加用户出差记录
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/addUserTravel")
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response addUserTravel(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            List<Map<String, Object>> list = (List<Map<String, Object>>) param.get("userTravel");
            result = tbUserTravelService.addUserTravel(list) > 0 ? Response.getResponseSuccess((Map<String, Object>) param.get("userinfo")) : Response.getResponseError();
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }
}