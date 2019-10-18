package com.jzb.message.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.message.service.MessageGroupTemplateService;
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
 * @Description: 消息模版控制层
 * @Author
 */
@Controller
@RequestMapping("/message/group/template")
public class MessageGroupTemplateController {

    @Autowired
    private MessageGroupTemplateService messageGroupTemplateService;

    /**
     * 查询
     */

    @RequestMapping(value = "/queryMsgGroupTemplate", method = RequestMethod.POST)
    @ResponseBody
    public Response queryMsgType(@RequestBody Map<String, Object> map) {
        Response response;
        try {
            PageInfo info = new PageInfo();
            info.setPages(JzbDataType.getInteger(map.get("page")) == 0 ? 1 : JzbDataType.getInteger(map.get("page")));
            List<Map<String, Object>> list = messageGroupTemplateService.queryMsgType(map);
            int count = messageGroupTemplateService.queryMsgTypeCount(map);
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
    @RequestMapping(value = "/searchMsgGroupTemplate", method = RequestMethod.POST)
    @ResponseBody
    public Response searchMsgType(@RequestBody Map<String, Object> map) {
        Response response;
        try {
            PageInfo info = new PageInfo();
            info.setPages(JzbDataType.getInteger(map.get("page")) == 0 ? 1 : JzbDataType.getInteger(map.get("page")));
            List<Map<String, Object>> list = messageGroupTemplateService.searchMsgType(map);
            int count = messageGroupTemplateService.searchMsgTypeCount(map);
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
    @RequestMapping(value = "/saveMsgGroupTemplate", method = RequestMethod.POST)
    @ResponseBody
    public Response saveMsgType(@RequestBody Map<String, Object> map) {
        Response response;
        try {
            response = messageGroupTemplateService.saveMsgType(map) > 0 ? Response.getResponseSuccess((Map)map.get("userinfo")) : Response.getResponseError();
        } catch (Exception e) {
            e.printStackTrace();
            response = Response.getResponseError();
        }
        return response;
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/upMsgGroupTemplate", method = RequestMethod.POST)
    @ResponseBody
    public Response upMsgType(@RequestBody Map<String, Object> map) {
        Response response;
        try {
            response = messageGroupTemplateService.upMsgType(map) > 0 ? Response.getResponseSuccess((Map)map.get("userinfo")) : Response.getResponseError();
        } catch (Exception e) {
            e.printStackTrace();
            response = Response.getResponseError();
        }
        return response;
    }

    /**
     * 禁用
     */
    @RequestMapping(value = "/removeMsgGroupTemplate", method = RequestMethod.POST)
    @ResponseBody
    public Response removeMsgType(@RequestBody Map<String, Object> map) {
        Response response;
        try {
            response = messageGroupTemplateService.removeMsgType(map) > 0 ? Response.getResponseSuccess((Map)map.get("userinfo")) : Response.getResponseError();
        } catch (Exception e) {
            e.printStackTrace();
            response = Response.getResponseError();
        }
        return response;
    }
}
