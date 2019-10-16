package com.jzb.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Redis服务应用入口
 * @author Chad
 * @date 2019年7月20日
 */
@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class JzbRedisServiceApplication {
    /**
     * 主入口
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(JzbRedisServiceApplication.class, args);
    } // End main
} // End class JzbRedisServiceApplication
