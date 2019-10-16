package com.jzb.message.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.message.service.UserParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;
import java.util.Map;

/**
 * @Description: 消息发送详情制层
 * @Author
 */
@Controller
@RequestMapping("/user/parameter")
public class UserParameterController {
    @Autowired
    private UserParameterService service;

    /**
     * 查询
     */
    @RequestMapping(value = "/queryUserParameter", method = RequestMethod.POST)
    @ResponseBody
    public Response queryUserParameter(@RequestBody Map<String, Object> map) {
        Response response;
        try {
            PageInfo info = new PageInfo();
            info.setPages(JzbDataType.getInteger(map.get("page")) == 0 ? 1 : JzbDataType.getInteger(map.get("page")));
            List<Map<String, Object>> list = service.queryUserParameter(map);
            int count = service.queryUserParameterCount(map);
            response = Response.getResponseSuccess((Map)map.get("userinfo"));
            info.setList(list);
            info.setTotal(count);
            response.setPageInfo(info);
        } catch (Exception e) {
            e.printStackTrace();
            response = Response.getResponseError();
        }
        return response;
    }


    /**
     * 模糊查询
     */
    @RequestMapping(value = "/searchUserParameter", method = RequestMethod.POST)
    @ResponseBody
    public Response searchUserParameter(@RequestBody Map<String, Object> map) {
        Response response;
        try {
            PageInfo info = new PageInfo();
            info.setPages(JzbDataType.getInteger(map.get("page")) == 0 ? 1 : JzbDataType.getInteger(map.get("page")));
            List<Map<String, Object>> list = service.searchUserParameter(map);
            int count = service.searchUserParameterCount(map);
            response = Response.getResponseSuccess((Map)map.get("userinfo"));
            info.setList(list);
            info.setTotal(count);
            response.setPageInfo(info);
        } catch (Exception e) {
            e.printStackTrace();
            response = Response.getResponseError();
        }
        return response;
    }


    /**
     * 添加
     */
    @RequestMapping(value = "/saveUserParameter", method = RequestMethod.POST)
    @ResponseBody
    public Response saveUserParameter(@RequestBody Map<String, Object> map) {
        Response response;
        try {
            response = service.saveUserParameter(map) > 0 ? Response.getResponseSuccess((Map)map.get("userinfo")) : Response.getResponseError();
        } catch (Exception e) {
            e.printStackTrace();
            response = Response.getResponseError();
        }
        return response;
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/upUserParameter", method = RequestMethod.POST)
    @ResponseBody
    public Response upUserParameter(@RequestBody Map<String, Object> map) {
        Response response;
        try {
            response = service.upUserParameter(map) > 0 ? Response.getResponseSuccess((Map)map.get("userinfo")) : Response.getResponseError();
        } catch (Exception e) {
            e.printStackTrace();
            response = Response.getResponseError();
        }
        return response;
    }

    /**
     * 禁用
     */
    @RequestMapping(value = "/removeUserParameter", method = RequestMethod.POST)
    @ResponseBody
    public Response removeUserParameter(@RequestBody Map<String, Object> map) {
        Response response;
        try {
            response = service.removeUserParameter(map) > 0 ? Response.getResponseSuccess((Map)map.get("userinfo")) : Response.getResponseError();
        } catch (Exception e) {
            e.printStackTrace();
            response = Response.getResponseError();
        }
        return response;
    }
}
