package com.jzb.operate.api.auth;

import com.jzb.base.message.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

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


    /**
     * 单位设置-申请/邀请成员
     * 通过名字模糊搜索所有用户或通过注册手机号搜索用户
     *
     * @author kuangbin
     */
    @RequestMapping(value = "/searchInvitee", method = RequestMethod.POST)
    @CrossOrigin
    public Response searchInvitee(@RequestBody Map<String, Object> param);

    /**
     * 模糊查询用户名
     *
     * @author hanbin
     */
    @PostMapping(value = "/getPersionByName")
    @CrossOrigin
    public Response getPersionByName(@RequestBody Map<String, Object> param);

    /**
     * 模糊查询用户名
     *
     * @author 陈辉
     */
    @PostMapping(value = "/getUidByUname")
    @CrossOrigin
    public Response getUidByUname(@RequestBody Map<String, Object> param);


}
