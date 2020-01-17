package com.jzb.org.api.auth;


import com.jzb.base.message.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@FeignClient(name = "jzb-auth")
@RequestMapping("/auth/board")
public interface PersonBoardApi {

    /**
     * 申请加入单位查询加入单位人的姓名
     * @param param
     * @return
     */
    @RequestMapping(value = "/getCname", method = RequestMethod.POST)
    @CrossOrigin
    public Response getCname(@RequestBody Map<String, Object> param);
}
