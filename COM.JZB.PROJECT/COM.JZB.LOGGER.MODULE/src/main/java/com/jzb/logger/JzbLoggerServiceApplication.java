package com.jzb.logger;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * 消息服务类入口
 * @author Chad
 * @date 2019年08月06日
 */
@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
@MapperScan(basePackages = "com.jzb.**.dao")
@ComponentScan(basePackages = {"com.jzb.**.controller",
        "com.jzb.**.service", "com.jzb.**.dao"})
public class JzbLoggerServiceApplication {
    /**
     * 主入口
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(JzbLoggerServiceApplication.class, args);
    } // End main
} // End class JzbLoggerServiceApplication
