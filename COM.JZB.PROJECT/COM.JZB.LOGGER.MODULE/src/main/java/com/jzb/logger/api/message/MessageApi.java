package com.jzb.logger.api.message;

import com.jzb.base.message.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @Description:
 * @Author dell
 * @Version 1.0
 * @Since 1.0
 * @Date: 2019/8/7 15:46
 */
@FeignClient(name = "jzb-message")
@RequestMapping("/message")
public interface MessageApi {
    /**
     * 送短信消息，根据手机号、template和code发送短信消息demo
     * @param param 请求参数
     * @return
     */
    @RequestMapping(value = "/sendShortMessage", method = RequestMethod.POST)
     Response sendShortMessage(@RequestBody Map<String, Object> param);


    /**
    * 可以正常发送短信的接口
    * @Author: DingSC
    * @DateTime: 2019/9/16 14:25
    * @param param
    * @return com.jzb.base.message.Response
    */
    @RequestMapping(value = "/sendShortMsg", method = RequestMethod.POST)
    @ResponseBody
    public Response sendShortMsg(@RequestBody Map<String, Object> param);
}
