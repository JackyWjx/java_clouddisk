package com.jzb.base.data.code;

import java.security.MessageDigest;

/**
 * 数据校验类
 * @author Chad
 * @date 2019年08月27日
 */
public class JzbDataCheck {

    /**
     * 获取MD5值
     *
     * @param data
     * @return
     */
    public static String Md5(String data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] b = md.digest(data.getBytes("UTF-8"));
        return JzbBase16.encode(b);
    } // End getMD5
} // End class JzbDataCheck
