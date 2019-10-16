package com.jzb.auth;

import com.jzb.base.data.code.JzbDataCheck;
import com.jzb.base.util.JzbTools;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Locale;

public class TestMain {
    public static void main(String[] args) throws Exception {
//        System.out.println(JzbDataCheck.Md5("AAAaaa111"));


        System.out.println(new BCryptPasswordEncoder().encode(JzbDataCheck.Md5("AAAaaa111").toLowerCase(Locale.ENGLISH)));
//
//        System.out.println(new BCryptPasswordEncoder().matches(JzbDataCheck.Md5("AAAaaa111"), "$2a$10$VL.sgfoFc8wtK4ECJV302OtxCRpZ5U8CC3VdwIWsCsTp6QInWKcgC"));

//        System.out.println(new BCryptPasswordEncoder().matches("827ccb0eea8a706c4c34a16891f84e7b", "$2a$10$6LIpOha8hk0y6iO5RYkvR.yWCsSHxRrIrMSpxPmVTnNmTVL3itB.a"));
    }
}
