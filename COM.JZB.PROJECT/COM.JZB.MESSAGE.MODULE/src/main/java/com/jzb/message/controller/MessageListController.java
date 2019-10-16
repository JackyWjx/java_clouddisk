package com.jzb.message.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.message.service.MessageListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @Description: 用户消息控制层
 * @Author
 */
@Controller
@RequestMapping("/message/list")
public class MessageListController {


    @Autowired
    private MessageListService messageListService;

    /**
     * 查询
     */

    @RequestMapping(value = "/queryMsgList", method = RequestMethod.POST)
    @ResponseBody
    public Response queryMsgList(@RequestBody Map<String, Object> map) {
        Response response;
        try {
            PageInfo info = new PageInfo();
            info.setPages(JzbDataType.getInteger(map.get("page")) == 0 ? 1 : JzbDataType.getInteger(map.get("page")));
            List<Map<String, Object>> list = messageListService.queryMsgType(map);
            int count = messageListService.queryMsgTypeCount(map);
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
    @RequestMapping(value = "/searchMsgList", method = RequestMethod.POST)
    @ResponseBody
    public Response searchMsgList(@RequestBody Map<String, Object> map) {
        Response response;
        try {
            PageInfo info = new PageInfo();
            info.setPages(JzbDataType.getInteger(map.get("page")) == 0 ? 1 : JzbDataType.getInteger(map.get("page")));
            List<Map<String, Object>> list = messageListService.searchMsgType(map);
            int count = messageListService.searchMsgTypeCount(map);
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
    @RequestMapping(value = "/saveMsgList", method = RequestMethod.POST)
    @ResponseBody
    public Response saveMsgList(@RequestBody Map<String, Object> map) {
        Response response;
        try {
            response = messageListService.saveMsgType(map) > 0 ? Response.getResponseSuccess((Map)map.get("userinfo")) : Response.getResponseError();
        } catch (Exception e) {
            e.printStackTrace();
            response = Response.getResponseError();
        }
        return response;
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/upMsgList", method = RequestMethod.POST)
    @ResponseBody
    public Response upMsgList(@RequestBody Map<String, Object> map) {
        Response response;
        try {
            response = messageListService.upMsgType(map) > 0 ? Response.getResponseSuccess((Map)map.get("userinfo")) : Response.getResponseError();
        } catch (Exception e) {
            e.printStackTrace();
            response = Response.getResponseError();
        }
        return response;
    }

    /**
     * 禁用
     */
    @RequestMapping(value = "/removeMsgList", method = RequestMethod.POST)
    @ResponseBody
    public Response removeMsgList(@RequestBody Map<String, Object> map) {
        Response response;
        try {
            response = messageListService.removeMsgType(map) > 0 ? Response.getResponseSuccess((Map)map.get("userinfo")) : Response.getResponseError();
        } catch (Exception e) {
            e.printStackTrace();
            response = Response.getResponseError();
        }
        return response;
    }

}
