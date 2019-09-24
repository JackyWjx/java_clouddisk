package com.jzb.base.data;

/**
 * 数据元素
 * <p>
 * @author Chad
 * @date 2015年12月7日
 * @version 1.0
 * @see JzbEntry
 * @since 1.0
 * @param <K> 键
 * @param <V> 值
 */
public class JzbEntry<K, V> {
    /**
     * 键
     */
    private final K _key;
    
    /**
     * 值
     */
    private final V _value;
    
    /**
     * 根据键和值，构造一个SteelEntry类
     * @param key 键
     * @param value 值
     */
    public JzbEntry(K key, V value) {
        _key = key;
        _value = value;
    } // End SteelEntry
    
    /**
     * 获取键
     * @return K 键名
     */
    public K getKey() {
        return _key;
    } // End getKey
    
    /**
     * 获取值
     * @return V 键值
     */
    public V getValue() {
        return _value;
    } // End getValue
} // End class SteelEntry