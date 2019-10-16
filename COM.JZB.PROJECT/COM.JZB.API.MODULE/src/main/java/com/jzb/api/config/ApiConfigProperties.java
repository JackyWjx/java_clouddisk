package com.jzb.api.config;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.util.JzbTools;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "com.jzb.api")
public class ApiConfigProperties {
    /**
     * 请求超时时间
     */
    private int requestTimeout;

    private String homePage;


    public int getRequestTimeout() {
        return requestTimeout == 0 ? 30000 : requestTimeout;
    }

    public void setRequestTimeout(String requestTimeout) {
        this.requestTimeout = JzbDataType.getInteger(requestTimeout);
    }

    /**
     * 获取默认主页
     * @return
     */
    public String getHomePage() {
        return JzbTools.isEmpty(homePage) ? "http://www.jizhibao.com.cn" : homePage;
    } // End getHomePage

    /**
     * 设置默认主键
     * @param page
     */
    public void setHomePage(String page) {
        homePage = page;
    } // End setHomePage

}
