package com.jzb.resource.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "com.jzb.resource")
public class ResourceConfigProperties {
    private String ip;
    private String port;
    private String path;

    public String getIp() {
        return ip;
    }

    public void setIp(String value) {
        this.ip = value;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String value) {
        this.port = value;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String value) {
        this.path = value;
    }

}
