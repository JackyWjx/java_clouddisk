package com.jzb.base.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.jzb.base.data.JzbDataApp;

/**
 * 针对键值对的数据进行Bean对象封装处理。
 * <p>
 * 这是一种扩展较好的Bean设计方案，特别是基类可以适应各种数据的情况。<br>
 * @author Chad
 * @date 2014年8月5日
 * @version 1.0
 * @see
 * @since 1.0
 */
public class JzbBaseBean<V> {
    /**
     * 数据对象
     */
    private final Map<String, V> _data;
    
    /**
     * 默认构造函数，创建一个Map存储容器对象
     */
    public JzbBaseBean() {
        _data = new ConcurrentHashMap<>();
    } // End JzbBaseBean
    
    /**
     * 设置一个字段值
     * <p>
     * @param key 字段名
     * @param value 字段值
     */
    public final void setItem(String key, V value) {
        _data.put(key, value);
    } // End setItem
    
    /**
     * 根据字段名获取字段值
     * <p>
     * @param key 字段名
     * @return V 字段值
     */
    public final V getItem(String key) {
        return _data.get(key);
    } // End getItem
    
    /**
     * 根据字段名获取字段值
     * <p>
     * @param key 字段名
     * @return V 字段值
     */
    public final String getItemTrim(String key) {
        return _data.containsKey(key) ? getItemApp(key).getTrim() : "";
    } // End getItem
    
    /**
     * 根据字段名获取字段值
     * <p>
     * @param key 字段名
     * @return LunznDataApp 字段值
     */
    public final JzbDataApp getItemApp(String key) {
        return new JzbDataApp(_data.get(key));
    } // End getItemApp
    
    /**
     * 根据字段名获取字段值
     * <p>
     * 当获取数据为null时，采用默认值进行处理。
     * @param key 字段名
     * @param defaultValue 默认值
     * @return LunznDataApp 字段值
     */
    public final JzbDataApp getItemApp(String key, V defaultValue) {
        Object value = _data.get(key);
        return new JzbDataApp((value == null ? defaultValue : value));
    } // End getItemApp
    
    /**
     * 获取Bean数据
     * <p>
     * @return Map Bean数据
     */
    public final Map<String, V> data() {
        return _data;
    } // End data
    
    /**
     * 克隆Bean数据
     * <p>
     * @return Map Bean数据
     */
    public final Map<String, V> cloneData() {
        Map<String, V> result = new HashMap<>(size());
        for (Entry<String, V> entry : _data.entrySet()) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    } // End cloneData
    
    /**
     * 获取数据大小
     * <p>
     * @return int 大小
     */
    public final int size() {
        return _data.size();
    } // End size
    
    /**
     * 输出所有字段信息
     * <p>
     * @return String Bean数据
     */
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        for (Entry<String, V> e : _data.entrySet()) {
            sb.append(", ").append(e.getKey()).append("=").append(e.getValue());
        }
        sb.append('}');
        return "{" + sb.substring(1);
    } // End toString
    
    /**
     * 加入一批数据
     * <p>
     * @param data 数组数据
     */
    public final void addData(Map<String, V> data) {
        for (Entry<String, V> entry : data.entrySet()) {
            setItem(entry.getKey(), entry.getValue());
        }
    } // End setData
} // End class JzbBaseBean
