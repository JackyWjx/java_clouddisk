package com.jzb.message.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.data.code.JzbDataCheck;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.message.service.SendUserMessageService;
import com.jzb.message.service.ShortMessageService;
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
 * @Author tang sheng jun
 */
@Controller
@RequestMapping("/senduser/message")
public class SendUserMessageController {

    @Autowired
    private SendUserMessageService service;

    @Autowired
    private ShortMessageService smsService;

    /**
     * 查询
     */
    @RequestMapping(value = "/querySendUserMessage", method = RequestMethod.POST)
    @ResponseBody
    public Response querySendUserMessage(@RequestBody Map<String, Object> map) {
        Response response;
        try {

            PageInfo info = new PageInfo();
            info.setPages(JzbDataType.getInteger(map.get("page")) == 0 ? 1 : JzbDataType.getInteger(map.get("page")));
            List<Map<String, Object>> list = service.querySendUserMessage(map);
            int count = service.querySendUserMessageCount(map);
            response = Response.getResponseSuccess((Map) map.get("userinfo"));
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
    @RequestMapping(value = "/searchSendUserMessage", method = RequestMethod.POST)
    @ResponseBody
    public Response searchSendUserMessage(@RequestBody Map<String, Object> map) {
        Response response;
        try {
            PageInfo info = new PageInfo();
            info.setPages(JzbDataType.getInteger(map.get("page")) == 0 ? 1 : JzbDataType.getInteger(map.get("page")));
            List<Map<String, Object>> list = service.searchSendUserMessage(map);
            int count = service.searchSendUserMessageCount(map);
            response = Response.getResponseSuccess((Map) map.get("userinfo"));
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
    @RequestMapping(value = "/saveSendUserMessage", method = RequestMethod.POST)
    @ResponseBody
    public Response saveSendUserMessage(@RequestBody Map<String, Object> map) {
        Response response;
        try {

            // check request param
            String appid = map.get("appId").toString();
            String secret = map.get("secret").toString();
            String msgid = map.get("msgid").toString();
            String msgtype = map.get("msgtype").toString();

            // get appid => checkcode
            Map<String, Object> checkcode = smsService.queryMsgOrganizeCheckcode(appid);
            String md5 = JzbDataCheck.Md5(appid + secret + msgtype + msgid + checkcode);

            if (md5.equals(map.get("checkcode"))) {
                response = service.saveSendUserMessage(map) > 0 ? Response.getResponseSuccess((Map) map.get("userinfo")) : Response.getResponseError();
            } else {
                response = Response.getResponseError();
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = Response.getResponseError();
        }
        return response;
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/upSendUserMessage", method = RequestMethod.POST)
    @ResponseBody
    public Response upSendUserMessage(@RequestBody Map<String, Object> map) {
        Response response;
        try {
            // check request param
            String appid = map.get("appId").toString();
            String secret = map.get("secret").toString();
            String msgid = map.get("msgid").toString();
            String msgtype = map.get("msgtype").toString();

            // get appid => checkcode
            Map<String, Object> checkcode = smsService.queryMsgOrganizeCheckcode(appid);
            String md5 = JzbDataCheck.Md5(appid + secret + msgtype + msgid + checkcode);

            if (md5.equals(map.get("checkcode"))) {
                response = service.upSendUserMessage(map) > 0 ? Response.getResponseSuccess((Map) map.get("userinfo")) : Response.getResponseError();
            } else {
                response = Response.getResponseError();
            }

        } catch (Exception e) {
            e.printStackTrace();
            response = Response.getResponseError();
        }
        return response;
    }

    /**
     * 禁用
     */
    @RequestMapping(value = "/removeSendUserMessage", method = RequestMethod.POST)
    @ResponseBody
    public Response removeSendUserMessage(@RequestBody Map<String, Object> map) {
        Response response;
        try {

            // check request param
            String appid = map.get("appId").toString();
            String secret = map.get("secret").toString();
            String msgid = map.get("msgid").toString();
            String msgtype = map.get("msgtype").toString();

            // get appid => checkcode
            Map<String, Object> checkcode = smsService.queryMsgOrganizeCheckcode(appid);
            String md5 = JzbDataCheck.Md5(appid + secret + msgtype + msgid + checkcode);

            if (md5.equals(map.get("checkcode"))) {
                response = service.removeSendUserMessage(map) > 0 ? Response.getResponseSuccess((Map) map.get("userinfo")) : Response.getResponseError();
            } else {
                response = Response.getResponseError();
            }

        } catch (Exception e) {
            e.printStackTrace();
            response = Response.getResponseError();
        }
        return response;
    }
}
