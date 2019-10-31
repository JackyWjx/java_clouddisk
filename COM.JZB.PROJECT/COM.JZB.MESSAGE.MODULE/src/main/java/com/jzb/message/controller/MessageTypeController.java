package com.jzb.message.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.data.code.JzbDataCheck;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import com.jzb.message.service.MessageTypeService;
import com.jzb.message.service.ShortMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * @Description: 消息类型控制层
 * @Author tang sheng jun
 */
@Controller
@RequestMapping("/message/type")
public class MessageTypeController {

    private final static Logger logger= LoggerFactory.getLogger(MessageTypeController.class);

    @Autowired
    private MessageTypeService service;

    @Autowired
    private ShortMessageService smsService;

    /**
     * 查询
     *
     * map 用户参数
     */
    @RequestMapping(value = "/queryMsgType", method = RequestMethod.POST)
    @ResponseBody
    public Response queryMsgType(@RequestBody Map<String, Object> map) {
        Response response;
        try {
            logger.info("==============>>queryMsgType");
            List<Map<String, Object>> list;
            PageInfo info = new PageInfo();
            info.setPages(JzbDataType.getInteger(map.get("pageno")) == 0 ? 1 : JzbDataType.getInteger(map.get("pageno")));
            response = Response.getResponseSuccess();
            if (map.containsKey("typename") && !JzbTools.isEmpty(map.get("typename"))) {
                list = service.searchMsgType(map);
                if (JzbDataType.getInteger(map.get("count")) == 0) {
                    int count = service.searchMsgTypeCount(map);
                    info.setTotal(count);
                }
            } else {
                list = service.queryMsgType(map);
                if (JzbDataType.getInteger(map.get("count")) == 0) {
                    int count = service.queryMsgTypeCount(map);
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
     *
     *  checkcode  = appid + secret + msgtype + typename + checkcode
     *  map 用户参数
     */
    @RequestMapping(value = "/saveMsgType", method = RequestMethod.POST)
    @ResponseBody
    public Response saveMsgType(@RequestBody Map<String, Object> map) {
        Response response;
        try {
            logger.info("==============>>saveMsgType");
            // check request param
            String appid = map.get("appid").toString();
            String secret = map.get("secret").toString();
            String msgtype = map.get("msgtype").toString();
            String typename = map.get("typename").toString();
            // get appid => checkcode
            Map<String, Object> checkcode = smsService.queryMsgOrganizeCheckcode(appid);
            String md5 = JzbDataCheck.Md5(appid + secret + msgtype + typename + checkcode.get("checkcode"));
            logger.info("---------MD5="+md5+"--------------");
            if (md5.equals(map.get("checkcode"))) {
                response = service.saveMsgType(map) > 0 ? Response.getResponseSuccess() : Response.getResponseError();
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
     *
     *  checkcode  = appid + secret + msgtype + typename + checkcode
     *  map 用户参数
     */
    @RequestMapping(value = "/upMsgType", method = RequestMethod.POST)
    @ResponseBody
    public Response upMsgType(@RequestBody Map<String, Object> map) {
        Response response;
        try {
            logger.info("==============>>upMsgType");
            // check request param
            String appid = map.get("appid").toString();
            String secret = map.get("secret").toString();
            String msgtype = map.get("msgtype").toString();
            String typename = map.get("typename").toString();
            // get appid => checkcode
            Map<String, Object> checkcode = smsService.queryMsgOrganizeCheckcode(appid);
            String md5 = JzbDataCheck.Md5(appid + secret + msgtype + typename + checkcode);
            logger.info("---------------MD5="+md5);
            if (md5.equals(map.get("checkcode"))) {
                response = service.upMsgType(map) > 0 ? Response.getResponseSuccess() : Response.getResponseError();
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
     *
     *  checkcode  = appid + secret + msgtype + typename + checkcode
     *  map 用户参数
     */
    @RequestMapping(value = "/removeMsgType", method = RequestMethod.POST)
    @ResponseBody
    public Response removeMsgType(@RequestBody Map<String, Object> map) {
        Response response;
        try {
            // check request param
            String appid = map.get("appid").toString();
            String secret = map.get("secret").toString();
            String msgtype = map.get("msgtype").toString();
            String typename = map.get("typename").toString();
            // get appid => checkcode
            Map<String, Object> checkcode = smsService.queryMsgOrganizeCheckcode(appid);
            String md5 = JzbDataCheck.Md5(appid + secret + msgtype + typename + checkcode);
            System.out.println("---------------MD5="+md5);
            if (md5.equals(map.get("checkcode"))) {
                response = service.removeMsgType(map) > 0 ? Response.getResponseSuccess() : Response.getResponseError();
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
