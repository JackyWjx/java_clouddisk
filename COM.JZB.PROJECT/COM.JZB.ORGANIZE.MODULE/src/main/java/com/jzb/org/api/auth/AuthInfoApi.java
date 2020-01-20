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
@RequestMapping(value = "/auth/user")
public interface AuthInfoApi {


    /**
     * 根据uid获取用户信息
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/queryUserInfoByUid", method = RequestMethod.POST)
    @CrossOrigin
    public Response queryUserInfoByUid(@RequestBody Map<String, Object> param);
}
