package com.jzb.message.controller;

import com.jzb.base.data.code.JzbDataCheck;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import com.jzb.message.service.ShortMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 短信消息业务处理
 *
 * @author Chad
 * @date 2019年8月7日
 */
@RestController
@RequestMapping("/sms")
public class ShortSessageController {

    private final static Logger logger = LoggerFactory.getLogger(ShortSessageController.class);

    /**
     * 服务层处理
     */
    @Autowired
    private ShortMessageService smsService;

    /**
     * 消息中心 发送接口
     */
    @RequestMapping(value = "/sendShortMsg", method = RequestMethod.POST)
    @ResponseBody
    public Response sendShortMsg(@RequestBody Map<String, Object> param) {
        Response result;
        try{
            logger.info("sendShortMsg短信===========>>"+param.toString());
            // check request param
            String appId = param.get("appid").toString();
            String groupId = param.get("groupid").toString();
            String title = param.get("title").toString();
            String userType = param.get("usertype").toString();
            String receiver = param.get("receiver").toString();
            String secret = param.get("secret").toString();
            logger.info("秘钥appId and secret===========================>>",appId, secret);
            // get appid => checkcode
            String checkCode = smsService.queryMsgOrganizeCheckcode(appId);
            String md5 = JzbDataCheck.Md5(appId + secret + groupId + title + userType + receiver + checkCode);
            if (md5.equals(param.get("checkcode"))) {
                logger.info("MD5 ===========>>" + md5 );
                result = smsService.sendShortMsg(param) == "success" ? Response.getResponseSuccess() : Response.getResponseError();
            }else{
                logger.info("MD5 ===========>> fail  error  401 " );
                result = Response.getResponseError();
                result.setResponseEntity(" fail  error  401 ");
            }
        }catch (Exception e){
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    } // End function sendShortMsg

    /**
     * 添加消息模板
     */
    @RequestMapping(value = "/saveMsgUserTeamplate", method = RequestMethod.POST)
    @ResponseBody
    public Response saveMsgUserTeamplate(@RequestBody Map<String, Object> param){
        Response response;
        try {
            response =  smsService.saveMsgUserTeamplate(param) ? Response.getResponseSuccess() : Response.getResponseError();
        }catch (Exception e){
            e.printStackTrace();
            response =  Response.getResponseError();
        }
        return response;
    } // End function saveMsgUserTeamplate

} // End class ShortSessageController
