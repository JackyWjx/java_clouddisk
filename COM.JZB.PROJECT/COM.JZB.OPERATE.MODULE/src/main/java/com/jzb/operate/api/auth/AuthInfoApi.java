package com.jzb.operate.api.auth;

import com.jzb.base.message.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@FeignClient(name = "jzb-auth")
@RequestMapping(value = "/auth/user")
public interface AuthInfoApi {

    /**
     * @Author sapientia
     * @Date 19:21 2019/12/23
     * @Description 获取头像
     **/
    @RequestMapping(value = "/getUsersList",method = RequestMethod.POST)
    @CrossOrigin
    public Response getUsernameList(@RequestBody Map<String,Object> param);
}
