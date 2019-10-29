package com.jzb.media;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * 媒体服务类入口
 * @author Chad
 */
@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
@MapperScan(basePackages = "com.jzb.**.dao")
@ComponentScan(basePackages = {"com.jzb.**.controller",
        "com.jzb.**.service", "com.jzb.**.dao", "com.jzb.**.config"})
public class JzbMediaServiceApplication {
    /**
     * 主入口
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(JzbMediaServiceApplication.class, args);
    } // End main
} // End class JzbMediaServiceApplication
