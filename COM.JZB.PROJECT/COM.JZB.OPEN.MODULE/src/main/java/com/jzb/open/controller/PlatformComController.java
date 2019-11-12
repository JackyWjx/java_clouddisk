package com.jzb.open.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbTools;
import com.jzb.open.api.auth.CompanyControllerApi;
import com.jzb.open.service.PlatformComService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author dell
 * @Version 1.0
 * @Since 1.0
 * @Date: 2019/11/7 15:03
 */
@RestController
@RequestMapping("open/com")
public class PlatformComController {

    @Autowired
    private PlatformComService platformComService;

    @Autowired
    private CompanyControllerApi companyApi;

    /**
     * 获取所有开放平台的企业id
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     */
    @RequestMapping(value = "/searchPlatformId", method = RequestMethod.POST)
    @CrossOrigin
    public Response getPlatformId(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            Map<String, Object> platCIds = platformComService.queryPlatformIds(param);
            result = Response.getResponseSuccess(userInfo);
            result.setResponseEntity(platCIds);
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 根据cid和集获取管理员信息
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     */
    @RequestMapping(value = "/getComAndMan", method = RequestMethod.POST)
    @CrossOrigin
    public Response getComAndMan(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            List<Map<String, Object>> platCIds = platformComService.getComAndMan(param);
            result = Response.getResponseSuccess(userInfo);
            result.setResponseEntity(platCIds);
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 开发者列表查询
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     */
    @RequestMapping(value = "/searchAppDeveloper", method = RequestMethod.POST)
    @CrossOrigin
    public Response searchAppDeveloper(@RequestBody Map<String, Object> param) {
        Response result;
        PageInfo Info;
        try {
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            int rows = JzbDataType.getInteger(param.get("pagesize"));
            int page = JzbDataType.getInteger(param.get("pageno"));
            if (page > 0 && rows > 0) {
                param.put("start", rows * (page - 1));
                param.put("pagesize", rows);
                List<Map<String, Object>> deList = platformComService.searchAppDeveloper(param);
                result = Response.getResponseSuccess(userInfo);
                Info = new PageInfo();
                Info.setList(deList);
                int count = JzbDataType.getInteger(param.get("count"));
                if (count == 0) {
                    int size = 0;
                    Info.setTotal(size > 0 ? size : deList.size());
                }
                result.setPageInfo(Info);
            } else {
                result = Response.getResponseError();
            }

        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }
}
