package com.jzb.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Eureka 服务主入口
 * @author Chad
 * @date 2019年7月7日
 */
@SpringBootApplication
@EnableEurekaServer
public class JzbEurekaServerApplication {
    /**
     * 程序入口
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(JzbEurekaServerApplication.class, args);
    } // End main
} // End class JzbEurekaServerApplication