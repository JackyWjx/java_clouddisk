package com.jzb.base.data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.jzb.base.data.date.JzbDateStr;
import com.jzb.base.data.regex.JzbRegex;

/**
 * 针对字符串数据与其它类型数据之间进行相互转换。
 * <p>
 * @author Chad
 * @date 2014年8月5日
 * @version 1.0
 * @see SteelDataParse
 * @since 1.0
 */
public final class JzbDataFormat {
    /**
     * 默认私有构造函数，禁止此类被实例化使用。
     */
    private JzbDataFormat() {
    } // End JzbDataFormat
    
    /**
     * 指定字符串数据是否能被指定的格式化正则表达式匹配。
     * <p>
     * @param data 源字符串数据
     * @param regex 格式化正则表达式
     * @return boolean类型数据： true:指定字符串匹配格式化正则表达式；false:指定字符串不匹配格式化正则表达式。
     */
    public static boolean canFormat(String data, String regex) {
        return data.matches(regex);
    } // End canFormat
    
    /**
     * 指定字符串数据能否被转换成数值型数据。
     * <p>
     * 判断是否能转换成数值型数据依据，以字符串是否以数字数据为准，不考虑数据的范围是否符合最大或最小值域。
     * @param data 源字符串数据
     * @return boolean类型数据： true:指定字符串能转换成数值类型数据；false:指定字符串不能转换成数值类型数据。
     */
    public static boolean canToNumeric(String data) {
        return data.matches(JzbRegex.REG_NUM_STRING);
    } // End canToNumeric
    
    /**
     * 指定字符串数据能否被转换成整型值数据。
     * <p>
     * @param data 源字符串数据
     * @return boolean类型数据： true:指定字符串能转换成整型值数据；false:指定字符串不能转换成整型值数据。
     */
    public static boolean canToInteger(String data) {
        return canToLong(data);
    } // End canToInteger
    
    /**
     * 指定字符串数据能否被转换成双精度型值数据。
     * <p>
     * @param data 源字符串数据
     * @return boolean类型数据： true:指定字符串能转换成双精度型值数据；false:指定字符串不能转换成双精度型值数据。
     */
    public static boolean canToDouble(String data) {
        return canToNumeric(data);
    } // End canToDouble
    
    /**
     * 指定字符串数据能否被转换成长整型值数据。
     * <p>
     * @param data 源字符串数据
     * @return boolean类型数据： true:指定字符串能转换成长整型值数据；false:指定字符串不能转换成长整型值数据。
     */
    public static boolean canToLong(String data) {
        return data.matches(JzbRegex.REG_INT_STRING);
    } // End canToLong
    
    /**
     * 根据指定的日期对象，转换为指定格式的日期字符串。
     * <p>
     * @param date 日期对象
     * @param dateFormat 格式化字符串
     * @return String 格式化后的日期字符串
     * @throws SteelFormatException 格式化异常
     */
    public static String dateToString(Date date, JzbDateStr dateFormat) throws Exception {
        return dateToString(date, dateFormat.toString());
    } // End dateToString
    
    /**
     * 根据指定的日期对象，转换为指定格式的日期字符串。
     * <p>
     * @param date 日期对象
     * @param dateFormat 格式化字符串
     * @return String 格式化后的日期字符串
     * @throws SteelFormatException 格式化异常
     */
    public static String dateToString(Date date, String dateFormat) throws Exception {
        try {
            // 解析字符串
            return getDateFormat(dateFormat).format(date);
        } catch (Exception ex) {
            throw new Exception("Parse date format string '" + dateFormat + "' failed. ERROR:" + ex.toString());
        }
    } // End dateToString
    
    /**
     * 按照提供的字符串格式化串与日期字符串，判断该字符串是否能转换为日期对象。
     * <p>
     * @param dateFormat 格式化串
     * @param str 日期字符串
     * @return true:可转日期对象；false:不可转日期对象。
     */
    public static boolean canToDate(JzbDateStr dateFormat, String str) {
        // 把格式化串转对正则表达式
        String format = dateFormat.getString().replaceAll("[G|y|M|w|W|D|d|F|E|a|H|K|k|h|m|s|S|z|Z]", "\\\\d");
        return str.matches("^" + format + "$");
    } // End canToDate
    
    /**
     * 把指定的日期字符串，按指定的格化式日期串转为日期对象。
     * <p>
     * @param dateStr 日期字符串
     * @param dateFormat 格式化串
     * @return Date 日期对象
     * @exception SteelFormatException 字符串格化成日期对象，格式化操作失败，则抛出操作异常。
     */
    public static Date stringToDate(String dateStr, JzbDateStr dateFormat) throws Exception {
        return stringToDate(dateStr, dateFormat.toString());
    } // End stringToDate
    
    /**
     * 把指定的日期字符串，按指定的格化式日期串转为日期对象。
     * <p>
     * @param dateStr 日期字符串
     * @param dateFormat 格式化串
     * @return Date 日期对象
     * @exception SteelFormatException 字符串格化成日期对象，格式化操作失败，则抛出操作异常。
     */
    public static Date stringToDate(String dateStr, String dateFormat) throws Exception {
        try {
            // 解析字符串为日期数据
            return getDateFormat(dateFormat).parse(dateStr);
        } catch (ParseException px) {
            throw new Exception("Parse string '" + dateStr + "' failed. ERROR:" + px.toString());
        }
    } // End stringToDate
    
    /**
     * 获取一个日期的SimpleDateFormat格式化对象。
     * <p>
     * 根据输入指定的format格式化字符串，以及默认的时区创建对象。
     * @param format 格式化字符串
     * @return SimpleDateFormat对象。
     */
    public static SimpleDateFormat getDateFormat(String format) {
        return getDateFormat(format, System.getProperty("user.timezone"));
    } // End getDateFormat
    
    /**
     * 获取一个日期的SimpleDateFormat格式化对象。
     * <p>
     * 根据输入指定的format格式化字符串，以及指定的时区创建对象。
     * @param format 格式化字符串
     * @param timeZone 格式化时区
     * @return SimpleDateFormat对象。
     */
    public static SimpleDateFormat getDateFormat(String format, String timeZone) {
        // 解析字符串为日期数据
        SimpleDateFormat simpleFormat = new SimpleDateFormat(format);
        simpleFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
        return simpleFormat;
    } // End getDateFormat
} // End class JzbDataFormat