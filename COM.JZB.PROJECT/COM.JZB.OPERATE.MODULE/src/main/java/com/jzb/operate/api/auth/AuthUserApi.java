package com.jzb.operate.api.auth;

import com.jzb.base.message.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

/**
 * @Description:
 * @Author Han Bin
 */
@FeignClient(name = "jzb-auth")
@RequestMapping(value = "/auth")
public interface AuthUserApi {

    /**
     * 主页获取用户信息
     *
     * @author kuangbin
     */
    @PostMapping(value = "/getUserInfo")
    @CrossOrigin
    public Response getUserInfo(@RequestBody Map<String, Object> param);
}
