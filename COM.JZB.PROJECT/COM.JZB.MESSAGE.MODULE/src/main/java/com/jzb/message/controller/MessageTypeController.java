package com.jzb.message.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.message.service.MessageTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @Description: 消息类型控制层
 * @Author
 */
@Controller
@RequestMapping("/message/type")
public class MessageTypeController {

    @Autowired
    private MessageTypeService service;

    /**
     * 查询
     */

    @RequestMapping(value = "/queryMsgType", method = RequestMethod.POST)
    @ResponseBody
    public Response queryMsgType(@RequestBody Map<String, Object> map) {
        Response response;
        try {
            PageInfo info = new PageInfo();
            info.setPages(JzbDataType.getInteger(map.get("page")) == 0 ? 1 : JzbDataType.getInteger(map.get("page")));
            List<Map<String, Object>> list = service.queryMsgType(map);
            int count = service.queryMsgTypeCount(map);
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
    @RequestMapping(value = "/searchMsgType", method = RequestMethod.POST)
    @ResponseBody
    public Response searchMsgType(@RequestBody Map<String, Object> map) {
        Response response;
        try {
            PageInfo info = new PageInfo();
            info.setPages(JzbDataType.getInteger(map.get("page")) == 0 ? 1 : JzbDataType.getInteger(map.get("page")));
            List<Map<String, Object>> list = service.searchMsgType(map);
            int count = service.searchMsgTypeCount(map);
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
    @RequestMapping(value = "/saveMsgType", method = RequestMethod.POST)
    @ResponseBody
    public Response saveMsgType(@RequestBody Map<String, Object> map) {
        Response response;
        try {
            response = service.saveMsgType(map) > 0 ? Response.getResponseSuccess((Map)map.get("userinfo")) : Response.getResponseError();
        } catch (Exception e) {
            e.printStackTrace();
            response = Response.getResponseError();
        }
        return response;
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/upMsgType", method = RequestMethod.POST)
    @ResponseBody
    public Response upMsgType(@RequestBody Map<String, Object> map) {
        Response response;
        try {
            response = service.upMsgType(map) > 0 ? Response.getResponseSuccess((Map)map.get("userinfo")) : Response.getResponseError();
        } catch (Exception e) {
            e.printStackTrace();
            response = Response.getResponseError();
        }
        return response;
    }

    /**
     * 禁用
     */
        @RequestMapping(value = "/removeMsgType", method = RequestMethod.POST)
    @ResponseBody
    public Response removeMsgType(@RequestBody Map<String, Object> map) {
        Response response;
        try {
            response = service.removeMsgType(map) > 0 ? Response.getResponseSuccess((Map) map.get("userinfo")) : Response.getResponseError();
        } catch (Exception e) {
            e.printStackTrace();
            response = Response.getResponseError();
        }
        return response;
    }


}
