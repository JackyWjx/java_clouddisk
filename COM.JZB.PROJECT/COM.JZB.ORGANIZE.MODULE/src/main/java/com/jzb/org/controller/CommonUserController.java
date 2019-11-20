package com.jzb.org.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import com.jzb.org.service.CommonUserService;
import com.jzb.org.util.SetPageSize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @描述
 * @创建人 chenhui
 * @创建时间 2019/11/19
 * @修改人和其它信息
 */
@RestController
@RequestMapping("/orgCommon")
public class CommonUserController {
    @Autowired
    CommonUserService userService;


    // 新建公海用户
    @RequestMapping("/addCommonUser")
    public Response addCommonUser(@RequestBody Map<String,Object> paramp){
        Response response;
        try {
            Map<String,Object> userinfo = (Map<String, Object>) paramp.get("userinfo");
            paramp.put("uid",userinfo.get("uid"));
            // 添加公海用户
            int count = userService.addCommUser(paramp);
            response = count > 0 ? Response.getResponseSuccess(userinfo):Response.getResponseError();
        } catch (Exception e) {
            JzbTools.logError(e);
            response = Response.getResponseError();
        }
        return response;
    }

    // 查询公海用户
    @RequestMapping("/queryCommonUser")
    public Response queryCommonUser(@RequestBody Map<String,Object> paramp){
        Response response;
        try {
            Map<String,Object> userinfo = (Map<String, Object>) paramp.get("userinfo");
            paramp.put("uid",userinfo.get("uid"));
            int count = JzbDataType.getInteger(paramp.get("count"));
            // 查询用户总数
            count = count > 0 ? count:userService.getCount(paramp);

            // 分页处理
            SetPageSize setPageSize = new SetPageSize();
            setPageSize.setPagenoSize(paramp);
            // 查询用户
            List<Map<String,Object>> list = userService.queryCommonUser(paramp);
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(list);
            pageInfo.setTotal(count);
            response = Response.getResponseSuccess(userinfo);
            response.setPageInfo(pageInfo);
        } catch (Exception e) {
            JzbTools.logError(e);
            response = Response.getResponseError();
        }
        return response;
    }

    // 修改公海用户
    @RequestMapping("/updCommonUser")
    public Response updCommonUser(@RequestBody Map<String,Object> paramp){
        Response response;
        try {
            int count = 0;
            Map<String,Object> userinfo = (Map<String, Object>) paramp.get("userinfo");
            if (!JzbTools.isEmpty(paramp.get("uid"))){
                 count = userService.updComUser(paramp);
            }
            response = count > 0 ? Response.getResponseSuccess(userinfo):Response.getResponseError();
        } catch (Exception e) {
            JzbTools.logError(e);
            response = Response.getResponseError();
        }
        return response;
    }

    // 删除公海用户
    @RequestMapping("/delUser")
    public Response delUser(@RequestBody Map<String,Object> paramp){
        Response response;
        try {
            Map<String,Object> userinfo = (Map<String, Object>) paramp.get("userinfo");
            // 删除公海用户
            int count = userService.delUser(paramp);
            response = count > 0 ? Response.getResponseSuccess(userinfo):Response.getResponseError();
        } catch (Exception e) {
            JzbTools.logError(e);
            response = Response.getResponseError();
        }
        return response;
    }



}
