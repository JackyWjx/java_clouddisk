package com.jzb.redis.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Redis数据配置
 * @author Chad
 * @date 2019年7月20日
 */
@Configuration
@EnableAutoConfiguration
public class JzbRedisConfig {
    /**
     * Redis服务器地址
     */
    @Value("${spring.redis.host}")
    private String host;

    /**
     * Redis服务器密码
     */
    @Value("${spring.redis.password}")
    private String password;

    /**
     * Redis服务器端口
     */
    @Value("${spring.redis.port}")
    private int port;

    /**
     * Redis服务器请求超时时间
     */
    @Value("${spring.redis.timeout}")
    private int timeout;

    /**
     * Redis连接最大空闲数
     */
    @Value("${spring.redis.pool.max-idle}")
    private int maxIdle;

    /**
     * Redis连接最小空闲数
     */
    @Value("${spring.redis.pool.min-idle}")
    private int minIdle;

    /**
     * Redis最大等待
     */
    @Value("${spring.redis.pool.max-wait}")
    private long maxWait;

    /**
     * Redis 数据库集
     */
    private final Map<Integer, JedisConnectionFactory> REDIS_DB_LIST = new ConcurrentHashMap<>();

    /**
     * 锁对象
     */
    private final Object LOCK = new Object();

    /**
     * 获取一个数据库连接
     * @param index 指定数据库
     * @return StringRedisTemplate 数据库操作对象
     */
    public StringRedisTemplate getRedisTemplate(int index) {
        StringRedisTemplate result;
        if (REDIS_DB_LIST.containsKey(index)) {
            result = new StringRedisTemplate(REDIS_DB_LIST.get(index));
        } else{
            synchronized (LOCK) {
                result = REDIS_DB_LIST.containsKey(index) ? new StringRedisTemplate(REDIS_DB_LIST.get(index))
                        : getRedisTemplate0(index);
            }
        }
        return result;
    } // End getRedisTemplate

    /**
     * 获取一个数据库连接
     * @param index 指定数据库
     * @return StringRedisTemplate 数据库操作对象
     */
    private StringRedisTemplate getRedisTemplate0(int index) {
        // 设置数据库配置
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(host);
        config.setPort(port);
        config.setPassword(RedisPassword.of(password));
        config.setDatabase(index);

        // 设置连接池
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMinIdle(minIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWait);
        JedisClientConfiguration jedisConfig = JedisClientConfiguration.builder().usePooling().
                poolConfig(jedisPoolConfig).and().readTimeout(Duration.ofMillis(timeout)).build();

        // 创建连接工厂，生成Template
        JedisConnectionFactory jedisFactory = new JedisConnectionFactory(config, jedisConfig);
        jedisFactory.afterPropertiesSet();
        REDIS_DB_LIST.put(index, jedisFactory);
        return new StringRedisTemplate(jedisFactory);
    } // End getRedisTemplate
} // End class JzbRedisConfig
