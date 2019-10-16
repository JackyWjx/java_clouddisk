package com.jzb.media.config;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.util.JzbTools;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Description: 配置中心
 * @Author Han Bin
 */
@Component
@ConfigurationProperties(prefix = "com.jzb.media")
public class MediaProperties {

    /**
     * 图片路径地址
     */
    private static String imageaddress;

    public static String getImageadderss() {
        JzbTools.logInfo("========>>","getImageadderss",imageaddress);
        return imageaddress;
    }

    public void setImageaddress(String imageaddress) {
        JzbTools.logInfo("========>>","setImageaddress",imageaddress);
        this.imageaddress = imageaddress;
    }
}

