package com.jzb.redis.service;

import com.jzb.redis.config.JzbRedisConfig;
import com.jzb.redis.config.JzbRedisDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户Redis数据库
 * @author Chad
 * @date 2019年7月20日
 */
@Service
public class UserRedisService {
    /**
     * Redis配置对象
     */
    @Autowired
    private JzbRedisConfig redisConfig;

    /**
     * 获取Redis服务操作对象
     * @return RedisService Redis服务对象
     */
    public RedisService getRedisService() {
        return new RedisService(redisConfig.getRedisTemplate(JzbRedisDatabase.USER_DATABASE));
    } // End getRedisService
} // End class UserRedisService
