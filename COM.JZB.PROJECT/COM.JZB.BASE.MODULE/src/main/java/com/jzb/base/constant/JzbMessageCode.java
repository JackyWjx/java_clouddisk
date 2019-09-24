package com.jzb.base.constant;

/**
 * 消息码常量类
 * @author Chad
 * @date 2019年08月29日
 */
public final class JzbMessageCode {
    /**
     * 私有构造方法，不允许实例化
     */
    private JzbMessageCode() {
    } // End JzbMessageCode

    /* ================================== 正确消息码 10000000 以1开始8位整数 =============================== */



    /* ================================== 正确消息码 40000000 以4开始8位整数 =============================== */
    /**
     * 用户输入密码错误，
     * 用户相关的错误码：4001xxxx
     */
    public final static int MSG_ERR_CODE_PASSWORD = 40010001;
} // End class JzbMessageCode