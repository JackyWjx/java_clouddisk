package com.jzb.api.api.auth;

import com.jzb.base.message.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 用户
 */
@FeignClient(name = "jzb-auth")
@RequestMapping(value = "/auth")
public interface UserAuthApi {
    /**
     * 获取用户TOKEN
     *
     * @param param 请求参数
     */
    @RequestMapping(value = "/getToken", method = RequestMethod.POST)
    public Response getToken(@RequestBody Map<String, Object> param);

    /**
     * 验证用户TOKEN
     * 根据token进行验证
     *
     * @param param 请求参数
     */
    @RequestMapping(value = "/checkToken", method = RequestMethod.POST)
    public Response checkToken(@RequestBody Map<String, Object> param);

    /**
     * 发送验证码
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/sendCode", method = RequestMethod.POST)
    @CrossOrigin
    public Response sendCode(@RequestBody Map<String, Object> param);

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
     * 用户注册短信验证
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     * @DateTime: 2019/9/16 16:59
     */
    @PostMapping("/userVerify")
    @CrossOrigin
    public Response userVerify(@RequestBody Map<String, Object> param);

    /**
     * 根据用户表的姓名或手机号查询用户信息
     *
     * @param param
     * @return
     */
    @PostMapping("/searchUserUid")
    @CrossOrigin
    public Response searchUserUid(@RequestBody Map<String, Object> param);
} // End interface UserAuthApi
