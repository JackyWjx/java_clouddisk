package com.jzb.redis.service;

import org.apache.commons.lang.StringUtils;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 封装Redis操作的服务类
 * 默认的是0号数据库
 *
 * @author Chad
 * @date 2019年7月20日
 */
@Configuration
@EnableAutoConfiguration
public class RedisService {
    /**
     * 操作对象
     */
    private final StringRedisTemplate redis;

    /**
     * 构造方法
     *
     * @param template
     */
    protected RedisService(StringRedisTemplate template) {
        redis = template;
    } // End RedisService

    /**
     * 指定一个缓存键的失效时间，单位固定为（秒）
     *
     * @param key  缓存的键名
     * @param time 失效的时间（秒）
     * @return boolean: true:成功; false:失败
     */
    public boolean comSetExpire(String key, long time) {
        boolean result = false;
        try {
            if (time > 0) {
                redis.expire(key, time, TimeUnit.SECONDS);
                result = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    } // End comSetExpire

    /**
     * 获取一个键到期时间（毫秒）
     *
     * @param key 缓存键
     * @return long 到期毫秒数
     */
    public long comGetExpire(String key) {
        return redis.getExpire(key, TimeUnit.MILLISECONDS);
    } // End comGetExpire

    /**
     * 判断缓存键在缓存中是否存在
     *
     * @param key 缓存的键
     * @return boolean: true:存在;false:不存在
     */
    public boolean comHasKey(String key) {
        boolean result = false;
        try {
            result = redis.hasKey(key);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    } // End comHasKey

    /**
     * 从缓存中删除指定的键
     *
     * @param key 缓存的键列表
     * @return long
     */
    public long comRemoveKey(String... key) {
        long result;
        try {
            if (key == null) {
                result = 0;
            } else if (key.length == 1) {
                result = redis.delete(key[0]) ? 1 : -1;
            } else {
                result = redis.delete(Arrays.asList(key));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            result = -1;
        }
        return result;
    } // End comRemoveKey

    /**
     * 获取字符串值
     *
     * @param key 缓存的KEY
     * @return String 缓存数据
     */
    public String getString(String key) {
        String result;
        try {
            result = StringUtils.isEmpty(key) ? "" : redis.opsForValue().get(key);
        } catch (Exception ex) {
            ex.printStackTrace();
            result = "";
        }
        return result;
    } // End getString

    /**
     * 设置一个缓存数据
     *
     * @param key   缓存键
     * @param value 缓存值
     * @return boolean:true:缓存成功;false:缓存失败
     */
    public boolean setString(String key, String value) {
        boolean result;
        try {
            redis.opsForValue().set(key, value);
            result = true;
        } catch (Exception ex) {
            ex.printStackTrace();
            result = false;
        }
        return result;
    } // End setString

    /**
     * 设置一个缓存数据，并指定缓存时间（秒）
     *
     * @param key   缓存键
     * @param value 缓存值
     * @param time  缓存时间(秒)
     * @return boolean:true:缓存成功;false:缓存失败
     */
    public boolean setString(String key, String value, int time) {
        boolean result;
        try {
            redis.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            result = true;
        } catch (Exception ex) {
            ex.printStackTrace();
            result = false;
        }
        return result;
    } // End setString

    /**
     * 获取Map的键值数据
     *
     * @param key  缓存键名
     * @param mkey 缓存MAP数据的键
     * @return Object 缓存MAP数据键值
     */
    public Object getMapValue(String key, String mkey) {
        Object result;
        try {
            result = redis.opsForHash().get(key, mkey);
        } catch (Exception ex) {
            ex.printStackTrace();
            result = null;
        }
        return result;
    } // End getMapValue

    /**
     * 从缓存中获取一个Map数据对象
     *
     * @param key 缓存的键
     * @return Map<Object, Object> 缓存Key对应的缓存数据
     */
    public Map<Object, Object> getMap(String key) {
        Map<Object, Object> result;
        try {
            result = redis.opsForHash().entries(key);
        } catch (Exception ex) {
            ex.printStackTrace();
            result = null;
        }
        return result;
    } // End getMap

    /**
     * 缓存一个Map数据
     *
     * @param key   缓存数据的键名
     * @param value 缓存数据
     * @return boolean: true:缓存成功;false:缓存失败
     */
    public boolean setMap(String key, Map<String, Object> value) {
        boolean result;
        try {
            redis.opsForHash().putAll(key, value);
            result = true;
        } catch (Exception ex) {
            ex.printStackTrace();
            result = false;
        }
        return result;
    } // End setMap

    /**
     * 缓存一个Map数据，并指定缓存时间
     *
     * @param key   缓存数据的键名
     * @param value 缓存数据
     * @param time  缓存时间
     * @return boolean: true:缓存成功;false:缓存失败
     */
    public boolean setMap(String key, Map<String, Object> value, int time) {
        boolean result;
        try {
            redis.opsForHash().putAll(key, value);
            result = true;
            comSetExpire(key, time);
        } catch (Exception ex) {
            ex.printStackTrace();
            result = false;
        }
        return result;
    } // End setMap

    /**
     * 设置一个缓存数据到现在的Map中
     *
     * @param key   缓存的键名
     * @param mkey  缓存Map数据库的key
     * @param value 需要缓存的数据
     * @return boolean:true:缓存成功;false:缓存失败
     */
    public boolean setMapValue(String key, String mkey, Object value) {
        boolean result;
        try {
            redis.opsForHash().put(key, mkey, value);
            result = true;
        } catch (Exception ex) {
            ex.printStackTrace();
            result = false;
        }
        return result;
    } // End setMapValue

    /**
     * 删除指定缓存Map数据中的键
     *
     * @param key   缓存的键名
     * @param mkeys 缓存数据中的Key
     * @return boolean:treu:删除成功;false:删除失败
     */
    public boolean delMapValue(String key, String... mkeys) {
        boolean result;
        try {
            redis.opsForHash().delete(key, mkeys);
            result = true;
        } catch (Exception ex) {
            ex.printStackTrace();
            result = false;
        }
        return result;
    } // End delMapValue

    /**
     * 判断指定缓存Map中，是否存在mkey的缓存数据
     *
     * @param key  缓存的键名
     * @param mkey 缓存Map数据的key
     * @return boolean:true:存在指定键;false:没有指定键
     */
    public boolean hasMapKey(String key, String mkey) {
        boolean result;
        try {
            result = redis.opsForHash().hasKey(key, mkey);
        } catch (Exception ex) {
            ex.printStackTrace();
            result = false;
        }
        return result;
    } // End hasMapKey
} // End class RedisService
