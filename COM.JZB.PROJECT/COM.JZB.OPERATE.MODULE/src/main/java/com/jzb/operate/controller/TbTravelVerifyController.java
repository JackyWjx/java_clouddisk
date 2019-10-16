package com.jzb.operate.controller;

import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbTools;
import com.jzb.operate.service.TbTravelVerifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author chenzhengduan
 * <p>
 * 出差审核Controller
 */
@RestController
@RequestMapping(value = "/operate/travelVerify")
public class TbTravelVerifyController {

    @Autowired
    private TbTravelVerifyService tbTravelVerifyService;

    /**
     * 添加出差审核记录
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/addTravelVerify")
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response addTravelVerify(@RequestBody Map<String, Object> param) {

        Response result;
        try {
            // 获取参数的list
            List<Map<String, Object>> list = (List<Map<String, Object>>) param.get("verifyList");
            result = tbTravelVerifyService.addTravelVerify(list) > 0 ? Response.getResponseSuccess((Map<String, Object>) param.get("userinfo")) : Response.getResponseError();
        } catch (Exception ex) {
            // 处理异常
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 设置审核通过
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/setTrueVerify", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response setTrueVerify(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            if (JzbCheckParam.haveEmpty(param, new String[]{"uid", "travelid"})) {
                result = Response.getResponseError();
            } else {
                param.put("status", 3);
                result = tbTravelVerifyService.updateVerifyStatus(param) > 0 ? Response.getResponseSuccess((Map<String, Object>) param.get("userinfo")) : Response.getResponseError();
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 设置审核打回
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/setFalseVerify", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response setFalseVerify(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            if (JzbCheckParam.haveEmpty(param, new String[]{"uid", "travelid"})) {
                result = Response.getResponseError();
            } else {
                param.put("status", 5);
                result = tbTravelVerifyService.updateVerifyStatus(param) > 0 ? Response.getResponseSuccess((Map<String, Object>) param.get("userinfo")) : Response.getResponseError();
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }
}