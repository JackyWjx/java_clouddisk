package com.jzb.operate.controller;

import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import com.jzb.operate.service.TbTravelVerifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/travelNotify")
public class TbTravelNotifyController {
    @Autowired
    private TbTravelVerifyService tbTravelVerifyService;

    /**
     * 添加抄送流程
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/addTravelNotify", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response addTravelNotify(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 获取参数的list
            List<Map<String, Object>> list = (List<Map<String, Object>>) param.get("notifyList");
            result = tbTravelVerifyService.addTravelVerify(list) > 0 ? Response.getResponseSuccess((Map<String, Object>) param.get("userinfo")) : Response.getResponseError();
        } catch (Exception ex) {
            // 捕获异常
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }
}
