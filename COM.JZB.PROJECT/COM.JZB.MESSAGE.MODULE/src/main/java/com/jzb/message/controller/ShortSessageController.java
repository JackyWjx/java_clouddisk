package com.jzb.message.controller;

import com.jzb.base.data.JzbDataType;
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
@RequestMapping("/message")
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
            // get appid => checkcode
            Map<String , Object>  checkCode = smsService.queryMsgOrganizeCheckcode(appId);
            logger.info("秘钥appId and secret===================>>"+appId+"==============>>"+ secret);
            param.put("cid",checkCode.get("cid"));

            String md5 = JzbDataCheck.Md5(appId + secret + groupId + title + userType + receiver + checkCode.get("checkcode").toString());
            logger.info("MD5 ===========>>" + md5 );
            if (md5.equals(param.get("checkcode"))) {
                Map<String , Object> msgMap = smsService.sendShortMsg(param,checkCode);
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

    /**
     * 根据业务id删除待发送消息队列
     */
    @RequestMapping(value = "/cancelSend", method = RequestMethod.POST)
    @ResponseBody
    public Response cancelSend(@RequestBody Map<String, Object> param){
        Response response;
        try{
            response = Response.getResponseSuccess();
        }catch (Exception e){
            response = Response.getResponseError();
            JzbTools.logError(e);
        }
        return  response;
    }

} // End class ShortSessageController
