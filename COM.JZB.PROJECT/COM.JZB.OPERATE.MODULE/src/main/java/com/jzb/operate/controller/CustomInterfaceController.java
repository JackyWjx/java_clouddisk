package com.jzb.operate.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import com.jzb.operate.service.CustomInterfaceService;
import com.jzb.operate.service.TbUserTravelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author kuangbin
 * 自定义界面操作类
 */
@RestController
@RequestMapping(value = "/operate/customInterface")
public class CustomInterfaceController {

    @Autowired
    private CustomInterfaceService customInterfaceService;

    /**
     * 中台首页-用户自定义界面1
     * 查询自定义界面数据
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getCustomInterface")
    @CrossOrigin
    public Response getCustomInterface(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("uid", JzbDataType.getString(userInfo.get("uid")));
            List<Map<String, Object>> record = customInterfaceService.getCustomInterface(param);
            result = Response.getResponseSuccess(userInfo);
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(record);
            result.setPageInfo(pageInfo);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 中台首页-用户自定义界面2
     * 修改自定义界面数据
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/modifyCustomInterface")
    @CrossOrigin
    public Response modifyCustomInterface(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            // 获取网关层前台传入的list
            List<Map<String, Object>> list = (List<Map<String, Object>>) param.get("list");
            long updTime = System.currentTimeMillis();
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> map = list.get(i);
                map.put("uid", userInfo.get("uid"));
                map.put("updtime", updTime);
            }
            result = customInterfaceService.modifyCustomInterface(list) > 0 ?
                    Response.getResponseSuccess(userInfo) : Response.getResponseError();
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }
}