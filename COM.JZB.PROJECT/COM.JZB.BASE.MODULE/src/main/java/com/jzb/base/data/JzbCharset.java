package com.jzb.base.data;

import java.nio.charset.Charset;

/**
 * 字符集常量类
 * <p>
 * 定义常用的字符集对象
 * @author Chad
 * @date 2014年5月29日
 * @version 1.0
 * @see
 * @since 1.0
 */
public final class JzbCharset {
    /**
     * ASCII编码
     */
    public static final Charset ASCII = Charset.forName("ASCII");
    
    /**
     * UTF-8编码
     */
    public static final Charset UTF8 = Charset.forName("UTF-8");
    
    /**
     * UTF-16编码
     */
    public static final Charset UTF16 = Charset.forName("UTF-16");
    
    /**
     * GB2312编码
     */
    public static final Charset GB2312 = Charset.forName("GB2312");
    
    /**
     * GBK编码
     */
    public static final Charset GBK = Charset.forName("GBK");
    
    /**
     * GB18030编码
     */
    public static final Charset GB18030 = Charset.forName("GB18030");
    
    /**
     * UNICODE编码
     */
    public static final Charset UNICODE = Charset.forName("UNICODE");
    
    /**
     * ISO8859编码
     */
    public static final Charset ISO8859 = Charset.forName("ISO8859-1");
    
    /**
     * 私有构造方法，不允许实例化
     */
    private JzbCharset() {
    } // End JzbCharset
    
    /**
     * 获取字符集的实例对象
     * <p>
     * @param charset 字符集名称
     * @return Charset 字符集对象
     */
    public static Charset getCharset(String charset) {
        return Charset.forName(charset);
    } // End getCharset
} // End class JzbCharset