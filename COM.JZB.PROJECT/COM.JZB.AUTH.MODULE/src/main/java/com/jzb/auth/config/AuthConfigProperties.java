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
