package com.jzb.redis.config;

/**
 * Redis数据库索引
 * @author Chad
 * @date 2019年7月22日
 */
public final class JzbRedisDatabase {
    /**
     * 公共数据库
     */
    public final static int COMMON_DATABASE = 0;

    /**
     * 用户数据库
     */
    public final static int USER_DATABASE = 1;

    /**
     * 组织数据库
     */
    public final static int ORG_DATABASE = 2;

    /**
     * 系统环境变量的配置库
     */
    public final static int ENV_DATABASE = 3;

    /**
     * 构造函数
     */
    private JzbRedisDatabase() {
    } // End JzbRedisDatabase
} // End enum JzbRedisDatabase
