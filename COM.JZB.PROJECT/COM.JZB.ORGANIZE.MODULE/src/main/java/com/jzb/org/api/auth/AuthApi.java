package com.jzb.org.api.auth;


import com.jzb.base.message.Response;
import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;


@FeignClient(name = "jzb-auth")
@RequestMapping(value = "/auth")
public interface AuthApi {
    /**
     * 注册的第二步操作创建用户
     *
     * @param param
     * @return
     */
    @PostMapping("/addRegistration")
    @CrossOrigin
    public Response addRegistration(@RequestBody Map<String, Object> param);

    /**
     * CRM-销售业主-公海-业主下的人员11
     * 点击业主下的人员中的新增人员按钮进行加入员工
     *
     * @author kuangbin
     */
    @PostMapping("/addCompanyEmployee")
    @CrossOrigin
    public Response addCompanyEmployee(@RequestBody Map<String, Object> param);
}
