package com.jzb.operate.controller;

import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import com.jzb.operate.service.TbUserTravelConsumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author  chenzhengduan
 * 用户出差报销
 */
@RestController
@RequestMapping(value = "/operate/userTravelConsume")
public class TbUserTravelConsumeController {

    @Autowired
    private TbUserTravelConsumeService tbUserTravelConsumeService;


    /**
     * 添加用户出差报销记录
     * @param param
     * @return
     */
    @RequestMapping(value = "/addTravelConsume",method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response addConsumeVerify(@RequestBody Map<String, Object> param){
        Response result;
        try {
            // 获取参数中的list
            List<Map<String, Object>> list = (List<Map<String, Object>>) param.get("list");
            result = tbUserTravelConsumeService.addUserTravelConsume(list) > 0 ? Response.getResponseSuccess((Map<String, Object>) param.get("userinfo")) : Response.getResponseError();
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }
}
