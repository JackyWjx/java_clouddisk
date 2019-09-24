package com.jzb.message.config;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.util.JzbTools;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Description: 配置中心
 * @Author Han Bin
 */
@Component
@ConfigurationProperties(prefix = "com.jzb.message")
public class MessageProperties {

    /**
     * 发送短信签名
     */
    private static String title;

    /**
     * 发送短信阿里云appid
     */
    private static String msgAppid;

    /**
     * 发送短信阿里云秘钥
     */
    private static String msgSecret;

    public static String getTitle() {
        JzbTools.logInfo("========>>","getTitle",title);
        return title;
    }

    public void setTitle(String title) {
        JzbTools.logInfo("========>>","setTitle",title);
        this.title = title;
    }

    public static String getMsgAppid() {
        JzbTools.logInfo("========>>","getMsgAppid",msgAppid);
        return msgAppid;
    }

    public void setMsgAppid(String msgAppid) {
        JzbTools.logInfo("========>>","setMsgAppid",msgAppid);
        this.msgAppid = msgAppid;
    }

    public static String getMsgSecret() {
        JzbTools.logInfo("========>>","getMsgSecret",msgSecret);
        return msgSecret;
    }

    public void setMsgSecret(String megSecret) {
        JzbTools.logInfo("========>>","setMsgSecret",megSecret);
        this.msgSecret = megSecret;
    }
}

