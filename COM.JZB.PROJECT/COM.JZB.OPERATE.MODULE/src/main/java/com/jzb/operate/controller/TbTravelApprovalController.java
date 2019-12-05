package com.jzb.operate.controller;

import com.jzb.base.message.Response;
import com.jzb.operate.service.TbTravelApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author ：Champ-Ping
 * @date ：Created in 2019-12-04 20:09
 * @description：出差/报销申请
 * @modified By：
 * @version: 1.0$
 */
@RestController
@RequestMapping(value = "/operate/travelApproval")
public class TbTravelApprovalController {

    @Autowired
    TbTravelApprovalService travelApprovalService;


    /**
     * 添加出差报销申请
     * @param param
     * @return
     */
    @CrossOrigin
    @Transactional
    @PostMapping("/addTravelApproval")
    public Response addTravelRecord(@RequestBody Map<String, Object> param) {

        Response result;

        try {
            travelApprovalService.save(param);
            result = Response.getResponseSuccess((Map<String, Object>) param.get("userinfo"));
        }catch (Exception e) {
            result = Response.getResponseError();
        }
        return result;
    }

}
