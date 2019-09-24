package com.jzb.api.service;

import com.jzb.base.util.JzbTools;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JzbEurekaService {
    /**
     * 动态获取URL
     */
    @Autowired
    private DiscoveryClient discoveryClient;

    /**
     * 获取获取的URL地址
     * @param service
     * @return
     */
    public String getServiceUrl(String service) {
        String result;
        try {
            // 从注册中心获取服务的地址列表
            List<ServiceInstance> instances = discoveryClient.getInstances(service);
            if (instances != null && instances.size() > 0) {
                // 取第一个服务器的地址（暂时这样处理）
                ServiceInstance instance = instances.get(0);
                result = instance.getUri().toString();
            } else {
                result = "";
            }
        } catch (Exception ex) {
            result = "";
        }
        return result;
    } // End getServiceUrl
} // End class JzbEurekaService
