package com.jzb.org.api.auth;


import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.RequestMapping;




@FeignClient(name = "jzb-auth")
@RequestMapping(value = "/auth")
public interface AuthApi {

}
