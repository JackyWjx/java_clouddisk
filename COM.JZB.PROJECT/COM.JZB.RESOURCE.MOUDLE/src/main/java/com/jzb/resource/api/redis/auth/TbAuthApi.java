package com.jzb.resource.api.redis.auth;

import com.jzb.base.message.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@FeignClient(name = "jzb-auth")
@RequestMapping(value = "/auth/role")
public interface TbAuthApi {

    /**
     * 查询计支宝企业内 文档管理 角色
     * 用户 给 体系建设 内超级管理员 权限
     * 查询当前用户 是否有超级权限
     * @param param
     * @Author: lifei
     * @DateTime: 2019/12/19 14:07
     */
    @RequestMapping(value = "/getDocMsgPower", method = RequestMethod.POST)
    @CrossOrigin
    public Response getDocMsgPower(@RequestBody Map<String, Object> param);
}
