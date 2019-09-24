package com.jzb.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * 配置中心
 *
 * @author Chad
 * @date 2019年08月13日
 */
@SpringBootApplication
@EnableConfigServer
public class JzbConfigServiceApplication {
    /**
     * 服务配置的主入口
     *
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(JzbConfigServiceApplication.class, args);
    } // End main
} // End class JzbConfigServiceApplication
