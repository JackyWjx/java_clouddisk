package com.jzb.operate.api.message;

import com.jzb.base.message.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 *  @author: gongWei
 *  @Date: Created in 2020/1/3 14:42
 *  @Description:
 */

@FeignClient(name = "jzb-message")
@RequestMapping("/message")
public interface OptMsgApi {

    @RequestMapping(value = "/sendShortMsg" , method = RequestMethod.POST)
    @CrossOrigin
    Response sendOptSysMsg(@RequestBody Map<String, Object> param);
}
