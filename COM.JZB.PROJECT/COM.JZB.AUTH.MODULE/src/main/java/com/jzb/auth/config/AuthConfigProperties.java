package com.jzb.auth.config;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.util.JzbTools;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author dell
 * @Version 1.0
 * @Since 1.0
 * @Date: 2019/8/22 10:11
 */
@Component
@ConfigurationProperties(prefix = "com.jzb.auth")
public class AuthConfigProperties {
    private int codeTime;

    /**
     * Token超时时间
     */
    private int tokenTimeout;

    /**
     * 注册发送短信模板
     *
     * @Author: DingSC
     * @DateTime: 2019/9/16 14:34
     * @param null
     * @return
     */
    private String register;
    /**
     * 找回密码发送短信模板
     *
     * @Author: DingSC
     * @DateTime: 2019/9/16 14:37
     * @param null
     * @return
     */
    private String retrieve;

    /**
     * 转让超级管理员权限发送短信
     * 您好,(XXX姓名)在(xx时间)已将(XX单位)的超级管理员权限转让给您.请您尽快登录,核对信息!
     */
    private String administrator;

    /**
     * 信息变更验证码发送短信
     * 验证码${code}，您正在尝试变更重要信息，请妥善保管账户信息。
     */
    private String verification;

    public String getVerification() {
        return verification;
    }

    public void setVerification(String verification) {
        this.verification = verification;
    }

    public String getAdministrator() {
        return administrator;
    }
    public void setAdministrator(String administrator) {
        this.administrator = administrator;
    }

    public String getRegister() {
        return register;
    }

    public void setRegister(String register) {
        this.register = register;
    }

    public String getRetrieve() {
        return retrieve;
    }

    public void setRetrieve(String retrieve) {
        this.retrieve = retrieve;
    }

    public int getCodeTime() {
        return codeTime;
    }

    public void setCodeTime(String codeTime) {
        this.codeTime = JzbDataType.getInteger(codeTime);
    }

    /**
     * 获取Token超时时间
     *
     * @return
     */
    public int getTokenTimeout() {
        JzbTools.logInfo("=========================>>", "getTokenTimeout", tokenTimeout);
        return tokenTimeout;
    } // End getTokenTimeout

    /**
     * 设置超时时间
     *
     * @param tokenTimeout
     */
    public void setTokenTimeout(String tokenTimeout) {
        this.tokenTimeout = JzbDataType.getInteger(tokenTimeout);
    } // End setTokenTimeout

}
