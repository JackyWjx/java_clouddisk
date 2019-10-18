package com.jzb.message.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.data.code.JzbDataCheck;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import com.jzb.message.service.MessageGroupTemplateService;
import com.jzb.message.service.MessageTypeService;
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
 * @Description: 消息模版控制层
 * @Author tang sheng jun
 */
@Controller
@RequestMapping("/message/group/template")
public class MessageGroupTemplateController {

    @Autowired
    private MessageGroupTemplateService messageGroupTemplateService;

    @Autowired
    private ShortMessageService smsService;

    /**
     * 查询
     */

    @RequestMapping(value = "/queryMsgGroupTemplate", method = RequestMethod.POST)
    @ResponseBody
    public Response queryMsgGroupTemplate(@RequestBody Map<String, Object> map) {
        Response response;
        try {
            List<Map<String, Object>> list;
            PageInfo info = new PageInfo();
            info.setPages(JzbDataType.getInteger(map.get("pageno")) == 0 ? 1 : JzbDataType.getInteger(map.get("pageno")));
            response = Response.getResponseSuccess();
            if (map.containsKey("tempname") && !JzbTools.isEmpty(map.get("tempname"))) {
                list = messageGroupTemplateService.searchMsgGroupTemplate(map);
                if (JzbDataType.getInteger(map.get("count")) == 0) {
                    int count = messageGroupTemplateService.searchMsgGroupTemplateCount(map);
                    info.setTotal(count);
                }
            } else {
                list = messageGroupTemplateService.queryMsgGroupTemplate(map);
                if (JzbDataType.getInteger(map.get("count")) == 0) {
                    int count = messageGroupTemplateService.queryMsgGroupTemplateCount(map);
                    info.setTotal(count);
                }
            }
            info.setList(list);
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
    public Response saveMsgGroupTemplate(@RequestBody Map<String, Object> map) {
        Response response;
        try {
            // check request param
            String appid = map.get("appid").toString();
            String secret = map.get("secret").toString();
            String tempid = map.get("tempid").toString();
            String cid = map.get("cid").toString();
            // get appid => checkcode
            Map<String, Object> checkcode = smsService.queryMsgOrganizeCheckcode(appid);
            String md5 = JzbDataCheck.Md5(appid + secret + tempid + cid + checkcode);
            if (md5.equals(map.get("checkcode"))) {
                response = messageGroupTemplateService.saveMsgGroupTemplate(map) > 0 ? Response.getResponseSuccess() : Response.getResponseError();
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
    @RequestMapping(value = "/upMsgGroupTemplate", method = RequestMethod.POST)
    @ResponseBody
    public Response upMsgGroupTemplate(@RequestBody Map<String, Object> map) {
        Response response;
        try {
            // check request param
            String appId = map.get("appId").toString();
            String secret = map.get("secret").toString();
            String tempid = map.get("tempid").toString();
            String cid = map.get("cid").toString();
            // get appid => checkcode
            Map<String, Object> checkcode = smsService.queryMsgOrganizeCheckcode(appId);
            String md5 = JzbDataCheck.Md5(appId + secret + tempid + cid + checkcode);
            if (md5.equals(map.get("checkcode"))) {
                response = messageGroupTemplateService.upMsgGroupTemplate(map) > 0 ? Response.getResponseSuccess() : Response.getResponseError();
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
    @RequestMapping(value = "/removeMsgGroupTemplate", method = RequestMethod.POST)
    @ResponseBody
    public Response removeMsgGroupTemplate(@RequestBody Map<String, Object> map) {
        Response response;
        try {
            // check request param
            String appId = map.get("appId").toString();
            String secret = map.get("secret").toString();
            String tempid = map.get("tempid").toString();
            String cid = map.get("cid").toString();
            // get appid => checkcode
            Map<String, Object> checkcode = smsService.queryMsgOrganizeCheckcode(appId);
            String md5 = JzbDataCheck.Md5(appId + secret + tempid + cid + checkcode);
            if (md5.equals("checkcode")) {
                response = messageGroupTemplateService.removeMsgGroupTemplate(map) > 0 ? Response.getResponseSuccess() : Response.getResponseError();
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
