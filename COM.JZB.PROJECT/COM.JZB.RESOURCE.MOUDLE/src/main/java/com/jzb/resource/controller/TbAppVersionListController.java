package com.jzb.resource.controller;

import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import com.jzb.resource.service.TbAppVersionListService;
import com.netflix.discovery.converters.jackson.EurekaXmlJacksonCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author chenzhengduan
 * @desc App版本库
 */
@RequestMapping("/resource/appVersion")
@RestController
public class TbAppVersionListController {

    @Autowired
    private TbAppVersionListService tbAppVersionListService;

    @RequestMapping(value = "/getNewAppVersion", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response getNewAppVersion(@RequestBody(required = false) Map<String, Object> param) {
        Response response;
        try {
            response = Response.getResponseSuccess();
            response.setResponseEntity(tbAppVersionListService.queryAppVersion());
        } catch (Exception ex) {
            JzbTools.logError(ex);
            response = Response.getResponseError();
        }
        return response;
    }
}
