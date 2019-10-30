package com.jzb.message.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.data.code.JzbDataCheck;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import com.jzb.message.message.MessageQueue;
import com.jzb.message.service.SendMessageSercver;
import com.jzb.message.service.ShortMessageService;
import com.jzb.message.config.MqttGateway;
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
@RequestMapping("/message")
public class ShortSessageController {

    private final static Logger logger = LoggerFactory.getLogger(ShortSessageController.class);

    /**
     * 服务层处理
     */
    @Autowired
    private ShortMessageService smsService;

    @Autowired
    private SendMessageSercver sendMessageSercver;


    /***
     * 信息平台消息转发
     *
     * checkcode  = appId + secret + groupId + title + userType + receiver + checkCode
     * param  用户参数
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
            // get appid => checkcode
            Map<String , Object>  checkCode = smsService.queryMsgOrganizeCheckcode(appId);
            logger.info("秘钥appId and secret===================>>"+appId+"==============>>"+ secret);
            param.put("cid",checkCode.get("cid"));
            String md5 = JzbDataCheck.Md5(appId + secret + groupId + title + userType + receiver + checkCode.get("checkcode").toString());
            logger.info("MD5 ===========>>" + md5 );
            if (md5.equals(param.get("checkcode"))) {
                Map<String , Object> msgMap = sendMessageSercver.SendMessage(param,checkCode);
                result = JzbDataType.getInteger(msgMap.get("sendstatus")) == 1 ? Response.getResponseSuccess() : Response.getResponseError();
                if(msgMap.containsKey("status")){
                    msgMap.remove("receiver");
                    msgMap.remove("addtime");
                    msgMap.remove("title");
                    msgMap.remove("status");
                }
                result.setResponseEntity(msgMap);
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
     * 根据业务id删除待发送消息队列
     *
     * checkCode = appId + secret + msgtag + userType + checkCode
     * param  用户参数
     */
    @RequestMapping(value = "/cancelSend", method = RequestMethod.POST)
    @ResponseBody
    public Response cancelSend(@RequestBody Map<String, Object> param){
        Response response;
        try{
            // check request param
            String appId = param.get("appid").toString();
            String secret = param.get("secret").toString();
            String userType = param.get("usertype").toString();
            String msgtag = param.get("msgtag").toString();
            Map<String , Object>  checkCode = smsService.queryMsgOrganizeCheckcode(appId);
            logger.info("秘钥appId and secret===================>>"+appId+"==============>>"+ secret);
            String md5 = JzbDataCheck.Md5(appId + secret + msgtag + userType + checkCode.get("checkcode").toString());
            logger.info("MD5 ===========>>" + md5 );
            if (md5.equals(param.get("checkcode"))) {
                response = MessageQueue.removerSendQuere(param) ? Response.getResponseSuccess() : Response.getResponseError();
            }else{
                logger.info("MD5 ===========>> fail  error  401 " );
                response = Response.getResponseError();
                response.setResponseEntity(" fail  error  401 ");
            }
        }catch (Exception e){
            response = Response.getResponseError();
            JzbTools.logError(e);
        }
        return  response;
    }

} // End class ShortSessageController
