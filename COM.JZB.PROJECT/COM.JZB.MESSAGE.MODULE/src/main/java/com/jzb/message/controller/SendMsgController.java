package com.jzb.message.controller;

import com.jzb.base.util.JzbTools;
import com.jzb.message.service.SendShortMsgService;
import com.jzb.message.util.JzbSendMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


/**
 * @Description: SendMsg接口支持
 * @Author Han Bin
 */
@Controller
@RequestMapping("/SMSService")
public class SendMsgController {

    private final static Logger logger = LoggerFactory.getLogger(SendMsgController.class);

    @Autowired
    SendShortMsgService sendShortMsgService;

    /**
     *  SendMsg接口
     */
    @RequestMapping(value = "/SendMsg",method = RequestMethod.POST)
    @ResponseBody
    public String  messageSendMsg(@RequestParam(value = "send_phone",required = false)String send_phone,@RequestParam(value = "sms_no",required = false)String sms_no
            ,@RequestParam(value = "sms_content",required = false)String sms_content,@RequestParam(value = "sms_provider",required = false)String sms_provider,
                @RequestParam(value = "sms_issendnow",required = false)String sms_issendnow,@RequestParam(value = "sms_type",required = false)String sms_type,@RequestParam(value = "push_time",required = false)String push_time
            ,@RequestParam(value = "sms_title",required = false)String sms_title){
        String response;
        try{
            logger.info("SendMsg短信 ============>>"+"send_phone  ======>>"+send_phone+"======>> sms_content  ======>>"+sms_content+"======>> sms_no  ======>>"+sms_no+"======>> sms_provider  ======>>"+sms_provider+"======>> sms_issendnow  ======>>"+sms_issendnow+"======>> sms_type  ======>>"+sms_type+"======>> push_time  ======>>"+push_time);
            response =  sendShortMsgService.sendSendMsg(send_phone,sms_content,sms_no,push_time,sms_title);
            logger.info("SendMsg result短信===========>>"+response);
        }catch (Exception e){
            response = "fail";
        }
        return response;
    } // messageSendMsg

} // SendMsgController
