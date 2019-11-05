package com.jzb.org.api.api;


import com.jzb.base.message.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@FeignClient(name = "jzb-api")
@RequestMapping(value = "/api/org")
public interface DeptUserControllerApi {

    /**
     * 所有业主-业主列表-新建
     * @param param
     * @param token
     * @return
     */

    @RequestMapping(value = "/addCompanyCommon")
    @CrossOrigin
    public Response addCompanyCommon(@RequestBody Map<String, Object> param, @RequestHeader(value = "token") String token);

}
