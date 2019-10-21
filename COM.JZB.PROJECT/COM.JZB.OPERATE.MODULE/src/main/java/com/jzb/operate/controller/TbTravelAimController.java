package com.jzb.operate.controller;

import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import com.jzb.operate.service.TbTravelAimService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author chenzhengduan
 * 出差目标
 */
@RestController
@RequestMapping(value = "/opreate/travelAim")
public class TbTravelAimController {

    @Autowired
    private TbTravelAimService tbTravelAimService;

    /**
     * 添加出差目标记录
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/addTravelAim", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response addTravelAim(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            result = tbTravelAimService.addTravelAim((List<Map<String, Object>>) param) > 0 ? Response.getResponseSuccess((Map<String, Object>) param.get("userinfo")) : Response.getResponseError();
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }
}