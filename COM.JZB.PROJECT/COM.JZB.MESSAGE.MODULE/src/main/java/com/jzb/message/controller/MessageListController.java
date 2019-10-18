package com.jzb.message.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.data.code.JzbDataCheck;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import com.jzb.message.service.MessageListService;
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
 * @Description: 用户消息控制层
 * @Author
 */
@Controller
@RequestMapping("/message/list")
public class MessageListController {


    @Autowired
    private MessageListService messageListService;

    @Autowired
    private ShortMessageService smsService;

    /**
     * 查询
     */

    @RequestMapping(value = "/queryMsgList", method = RequestMethod.POST)
    @ResponseBody
    public Response queryMsgList(@RequestBody Map<String, Object> map) {
        Response response;
        List<Map<String, Object>> list;
        try {
            PageInfo info = new PageInfo();
            info.setPages(JzbDataType.getInteger(map.get("page")) == 0 ? 1 : JzbDataType.getInteger(map.get("page")));
            response = Response.getResponseSuccess();
            if (map.containsKey("sendname") && !JzbTools.isEmpty(map.get("sendname"))) {
                list = messageListService.searchMsgList(map);
                if (JzbDataType.getInteger(map.get("count")) == 0) {
                    int count = messageListService.searchMsgListCount(map);
                    info.setTotal(count);
                }
            } else {
                list = messageListService.queryMsgList(map);
                if (JzbDataType.getInteger(map.get("count")) == 0) {
                    int count = messageListService.queryMsgListCount(map);
                    info.setTotal(count);
                }
            }
            list = messageListService.queryMsgList(map);
            int count = messageListService.queryMsgListCount(map);
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
    @RequestMapping(value = "/saveMsgList", method = RequestMethod.POST)
    @ResponseBody
    public Response saveMsgList(@RequestBody Map<String, Object> map) {
        Response response;
        try {

            // check request param
            String appid = map.get("appId").toString();
            String secret = map.get("secret").toString();
            String tempid = map.get("msgid").toString();
            String cid = map.get("cid").toString();

            // get appid => checkcode
            Map<String, Object> checkcode = smsService.queryMsgOrganizeCheckcode(appid);
            String md5 = JzbDataCheck.Md5(appid + secret + tempid + cid + checkcode);
            if (md5.equals(map.get("checkcode"))) {
                response = messageListService.saveMsgList(map) > 0 ? Response.getResponseSuccess((Map) map.get("userinfo")) : Response.getResponseError();
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
    @RequestMapping(value = "/upMsgList", method = RequestMethod.POST)
    @ResponseBody
    public Response upMsgList(@RequestBody Map<String, Object> map) {
        Response response;
        try {

            // check request param
            String appid = map.get("appId").toString();
            String secret = map.get("secret").toString();
            String tempid = map.get("msgid").toString();
            String cid = map.get("cid").toString();

            // get appid => checkcode
            Map<String, Object> checkcode = smsService.queryMsgOrganizeCheckcode(appid);
            String md5 = JzbDataCheck.Md5(appid + secret + tempid + cid + checkcode);
            if (md5.equals(map.get("checkcode"))) {
                response = messageListService.upMsgList(map) > 0 ? Response.getResponseSuccess((Map) map.get("userinfo")) : Response.getResponseError();
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
    @RequestMapping(value = "/removeMsgList", method = RequestMethod.POST)
    @ResponseBody
    public Response removeMsgList(@RequestBody Map<String, Object> map) {
        Response response;
        try {
            // check request param
            String appid = map.get("appId").toString();
            String secret = map.get("secret").toString();
            String tempid = map.get("msgid").toString();
            String cid = map.get("cid").toString();

            // get appid => checkcode
            Map<String, Object> checkcode = smsService.queryMsgOrganizeCheckcode(appid);
            String md5 = JzbDataCheck.Md5(appid + secret + tempid + cid + checkcode);
            if (md5.equals(map.get("checkcode"))) {
                response = messageListService.removeMsgList(map) > 0 ? Response.getResponseSuccess((Map) map.get("userinfo")) : Response.getResponseError();
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
