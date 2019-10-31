package com.jzb.org.api.operate;

import com.jzb.base.message.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@FeignClient(name = "jzb-operate")
@RequestMapping("/opt/HandleItem")
public interface HandleItemApi {

    @RequestMapping(value = "/getHandleItem", method = RequestMethod.POST)
    @CrossOrigin
     Response getHandleItem(@RequestBody Map<String, Object> param);
}
