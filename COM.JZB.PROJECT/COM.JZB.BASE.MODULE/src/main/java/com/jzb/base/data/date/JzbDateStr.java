package com.jzb.base.data.date;

/**
 * 可支持操作的日期格式。
 * <p>
 * @author Chad
 * @date 2014年8月5日
 * @version 1.0
 * @see
 * @since 1.0
 */
public enum JzbDateStr {
    /**
     * 指定日期是yyyyMMddHHmmss 为24小时制数据
     */
    yyyyMMddHHmmss("yyyyMMddHHmmss"),
    
    /**
     * 指定日期是yyyyMMddHHmmssSSS
     */
    yyyyMMddHHmmssSSS("yyyyMMddHHmmssSSS"),
    
    /**
     * 指定日期是yyyyMMddhhmmss 为12小时制数据
     */
    yyyyMMddhhmmss("yyyyMMddhhmmss"),
    
    /**
     * 指定日期是yyyyMMddhhmmssSSS
     */
    yyyyMMddhhmmssSSS("yyyyMMddhhmmssSSS"),
    
    /**
     * 指定日期是yyyy-MM-dd HH:mm:ss
     */
    yyyy_MM_dd_HH_mm_ss("yyyy-MM-dd HH:mm:ss"),
    
    /**
     * 指定日期是yyyy-MM-dd HH:mm:ss
     */
    yyyy_MM_dd_HH_mm_ss_SSS("yyyy-MM-dd HH:mm:ss-SSS"),
    
    /**
     * 指定日期是yyyy-MM-dd hh:mm:ss
     */
    yyyy_MM_dd_hh_mm_ss("yyyy-MM-dd hh:mm:ss"),
    
    /**
     * 指定日期是yyyy-MM-dd hh:mm:ss
     */
    yyyy_MM_dd_hh_mm_ss_SSS("yyyy-MM-dd hh:mm:ss-SSS"),
    
    /**
     * 指定日期是yyyy/MM/dd HH:mm:ss
     */
    yyyy_MM_dd_HH_mm_ss_i("yyyy/MM/dd HH:mm:ss"),
    
    /**
     * 指定日期是yyyy/MM/dd hh:mm:ss
     */
    yyyy_MM_dd_hh_mm_ss_i("yyyy/MM/dd hh:mm:ss"),
    
    /**
     * 指定日期是yyyy_MM_dd HH:mm:ss
     */
    yyyy_MM_dd_HH_mm_ss_u("yyyy_MM_dd HH:mm:ss"),
    
    /**
     * 指定日期是yyyy_MM_dd hh:mm:ss
     */
    yyyy_MM_dd_hh_mm_ss_u("yyyy_MM_dd hh:mm:ss"),
    
    /**
     * 指定日期是yyyyMMdd
     */
    yyyyMMdd("yyyyMMdd"),
    /**
     * 指定日期是yyyy-MM-dd
     */
    yyyy_MM_dd("yyyy-MM-dd"),
    
    /**
     * 指定日期是yyyy_MM_dd
     */
    yyyy_MM_dd_u("yyyy_MM_dd"),
    
    /**
     * 指字时间是HHmmss
     */
    HHmmss("HHmmss"),
    /**
     * 指字时间是hhmmss
     */
    hhmmss("hhmmss"),
    
    /**
     * 指字时间是HH:mm:ss
     */
    HH_mm_ss("HH:mm:ss"),
    
    /**
     * 指字时间是hh:mm:ss
     */
    hh_mm_ss("hh:mm:ss"),
    
    /**
     * 指定时间是HHmm
     */
    HHmm("HHmm"),
    
    /**
     * 指定时间是hhmm
     */
    hhmm("hhmm"),
    
    /**
     * 指定时间是HH:mm
     */
    HH_mm("HH:mm"),
    
    /**
     * 指定时间是hh:mm
     */
    hh_mm("hh:mm");
    
    /**
     * 保存指定的格式化串
     */
    private final String _format;
    
    /**
     * 私有的构造方法
     * @param format 格式化串
     */
    private JzbDateStr(String format) {
        _format = format;
    } // End JzbDateStr
    
    /**
     * 返回格式华日期的字符串
     * @return String 格式化串
     */
    @Override
    public String toString() {
        return _format;
    } // End toString
    
    /**
     * 获取格式化的字符串
     * @return String 日期格式化字符串
     */
    public String getString() {
        return _format;
    } // End getString
} // End enum JzbDateStr
