package com.jzb.operate.controller;

import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import com.jzb.operate.service.TbTravelVehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 交通工具类
 * @author chenzhengduan
 */
@RestController
@RequestMapping(value = "/operate/travelVehicle")
public class TbTravelVehicleController {

    @Autowired
    private TbTravelVehicleService tbTravelVehicleService;

    /**
     * 获取交通工具
     * @param param
     * @return
     */
    @RequestMapping(value = "/getVehicleList",method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response getVehicleList(@RequestBody Map<String, Object> param){
        Response result;
        try {
            // 获取交通工具list
            List<Map<String, Object>> list = tbTravelVehicleService.queryVehicle();
            // 获取用户信息返回
            result=Response.getResponseSuccess((Map<String, Object>) param.get("userinfo"));

            // 定义返回对象pageinfo
            PageInfo pi=new PageInfo();
            pi.setList(list);
            pi.setTotal(list.size());

            result.setPageInfo(pi);
        }catch (Exception ex){
            JzbTools.logError(ex);
            result=Response.getResponseError();
        }
        return result;
    }
}
