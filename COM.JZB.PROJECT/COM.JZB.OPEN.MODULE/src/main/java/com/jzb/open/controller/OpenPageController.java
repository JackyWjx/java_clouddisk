package com.jzb.open.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbTools;
import com.jzb.open.service.OpenPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author dell
 * @Version 1.0
 * @Since 1.0
 * @Date: 2019/11/21 14:51
 */
@RestController
@RequestMapping("open/page")
public class OpenPageController {
    @Autowired
    private OpenPageService openPageService;

    /**
     * 模糊查询开发者应用表
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     */
    @RequestMapping(value = "/searchOrgApplication", method = RequestMethod.POST)
    @CrossOrigin
    public Response searchOrgApplication(@RequestBody Map<String, Object> param) {
        Response result;
        PageInfo Info;
        try {
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            int rows = JzbDataType.getInteger(param.get("pagesize"));
            int page = JzbDataType.getInteger(param.get("pageno"));
            if (page > 0 && rows > 0) {
                param.put("start", rows * (page - 1));
                param.put("pagesize", rows);
                List<Map<String, Object>> orgAppList = openPageService.searchOrgApplication(param);
                result = Response.getResponseSuccess(userInfo);
                Info = new PageInfo();
                Info.setList(orgAppList);
                int count = JzbDataType.getInteger(param.get("count"));
                if (count == 0) {
                    int size = openPageService.searchOrgApplicationCount(param);
                    Info.setTotal(size > 0 ? size : orgAppList.size());
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

    /**
     * 新增应用菜单表
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     */
    @RequestMapping(value = "/addApplicationMenu", method = RequestMethod.POST)
    @CrossOrigin
    public Response addApplicationMenu(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String[] str = {"appid", "cname", "menupath"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                param.put("uid", userInfo.get("uid"));
                String key = "parentid";
                if (JzbTools.isEmpty(param.get(key))) {
                    param.put(key, "000000000000000");
                }
                int add = openPageService.insertApplicationMenu(param);
                result = add > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
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
