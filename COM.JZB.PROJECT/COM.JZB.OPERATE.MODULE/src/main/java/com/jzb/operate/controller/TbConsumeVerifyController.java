package com.jzb.operate.controller;

import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbTools;
import com.jzb.operate.api.redis.TbCityRedis;
import com.jzb.operate.service.TbConsumeVerifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenzhengduan
 * 报销审核
 */
@RestController
@RequestMapping(value = "/operate/consumeVerify")
public class TbConsumeVerifyController {

    @Autowired
    private TbConsumeVerifyService tbConsumeVerifyService;

    @Autowired
    private TbCityRedis tbCityRedis;

    /**
     * 根据城市id 获取单挑城市信息
     * @return
     */
    @RequestMapping(value = "/getcity",method = RequestMethod.GET)
    @ResponseBody
    public Response getcity(){
        Map<String, Object> param=new HashMap<>();
        param.put("key",10020600);
        Response cityList = tbCityRedis.getCityList(param);
        return cityList;
    }

    /**
     * 添加出差报销审核记录
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/addConsumeVerify", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response addConsumeVerify(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 获取前端传的list
            List<Map<String, Object>> list = (List<Map<String, Object>>) param.get("list");
            result = tbConsumeVerifyService.addConsumeVerify(list) > 0 ? Response.getResponseSuccess((Map<String, Object>) param.get("userinfo")) : Response.getResponseError();
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 设置状态出差报销审核记录
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/updateConsumeVerify", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response updateConsumeVerify(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            if(JzbCheckParam.haveEmpty(param,new String[]{"status","travelid","ouid"})){
                result=Response.getResponseError();
            }else {
                result = tbConsumeVerifyService.updateVerifyStatus(param) > 0 ? Response.getResponseSuccess((Map<String, Object>) param.get("userinfo")) : Response.getResponseError();
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }
}
