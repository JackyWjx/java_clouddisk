package com.jzb.oauth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 这一步要能直接返回JSON的串最好
 * @author Chad
 * @date 2019年08月01日
 */
@Controller
public class LoginController {
    /**
     * 登录页，（这里返回错误标识）
     * @return
     */
    @RequestMapping(value = "/userLogin", method = RequestMethod.POST)
    @CrossOrigin
    public String userLogin(@RequestBody Map<String, Object> param) {

        System.out.println("======================================>> userLogin ");
        return "TEST";
    } // End login

    /**
     * 登录页，（这里返回错误标识）
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @CrossOrigin
    public String login() {
        System.out.println("======================================>> LOGIN ");
        return "login";
    } // End login

    /**
     * 返回主页（这里返回成功标识）
     * @return
     */
    @GetMapping("/")
    public String index() {
        System.out.println("======================================>> index ");
        return "index";
    } // index
} // End class LoginController
