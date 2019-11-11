package com.jzb.org.api.auth;

import com.jzb.base.message.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@Repository
@FeignClient(name = "jzb-auth")
@RequestMapping(value = "/auth/userControlAuth")
public interface TbUserControlAuthApi {


    /**
     * 查看单位下是否有人员授权
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getCompanyIsAuth", method = RequestMethod.POST)
    @CrossOrigin
    public Response getCompanyIsAuth(@RequestBody Map<String, Object> param);
}
