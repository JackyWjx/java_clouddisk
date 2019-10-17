package com.jzb.message.controller;


import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.message.service.MessageOrganizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * 消息平台入驻
 */
@Controller
@RequestMapping("/message/organize")
public class MessageOrganizeController {
    @Autowired
    private MessageOrganizeService service;

    /**
     * 查询
     */

    @RequestMapping(value = "/queryMsgOrganize", method = RequestMethod.POST)
    @ResponseBody
    public Response queryMsgType(@RequestBody Map<String, Object> map) {
        Response response;
        try {
            PageInfo info = new PageInfo();
            info.setPages(JzbDataType.getInteger(map.get("page")) == 0 ? 1 : JzbDataType.getInteger(map.get("page")));
            List<Map<String, Object>> list = service.queryMsgOrganize(map);
            int count = service.queryMsgOrganizeCount(map);
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
    @RequestMapping(value = "/searchMsgOrganize", method = RequestMethod.POST)
    @ResponseBody
    public Response searchMsgType(@RequestBody Map<String, Object> map) {
        Response response;
        try {
            PageInfo info = new PageInfo();
            info.setPages(JzbDataType.getInteger(map.get("page")) == 0 ? 1 : JzbDataType.getInteger(map.get("page")));
            List<Map<String, Object>> list = service.searchMsgOrganize(map);
            int count = service.searchMsgOrganizeCount(map);
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
    @RequestMapping(value = "/saveMsgOrganize", method = RequestMethod.POST)
    @ResponseBody
    public Response saveMsgType(@RequestBody Map<String, Object> map) {
        Response response;
        try {
            response = service.saveMsgOrganize(map) > 0 ? Response.getResponseSuccess((Map)map.get("userinfo")) : Response.getResponseError();
        } catch (Exception e) {
            e.printStackTrace();
            response = Response.getResponseError();
        }
        return response;
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/upMsgOrganize", method = RequestMethod.POST)
    @ResponseBody
    public Response upMsgType(@RequestBody Map<String, Object> map) {
        Response response;
        try {
            response = service.upMsgOrganize(map) > 0 ? Response.getResponseSuccess((Map)map.get("userinfo")) : Response.getResponseError();
        } catch (Exception e) {
            e.printStackTrace();
            response = Response.getResponseError();
        }
        return response;
    }

    /**
     * 禁用
     */
    @RequestMapping(value = "/removeMsgOrganize", method = RequestMethod.POST)
    @ResponseBody
    public Response removeMsgType(@RequestBody Map<String, Object> map) {
        Response response;
        try {
            response = service.removeMsgOrganize(map) > 0 ? Response.getResponseSuccess((Map)map.get("userinfo")) : Response.getResponseError();
        } catch (Exception e) {
            e.printStackTrace();
            response = Response.getResponseError();
        }
        return response;
    }

}