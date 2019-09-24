package com.jzb.base.data.date;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.jzb.base.data.JzbDataFormat;

/**
 * 日期操作对象
 * @author lichengdong
 * @date 2018年1月16日
 * @version 1.0
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public final class JzbDateUtil {
    /**
     * 私有构造函数，禁止实例化
     */
    private JzbDateUtil() {
    } // End JzbDateUtil
    
    /**
     * 获取加减月的日期
     * @param month 月数
     * @return int 日期
     */
    public static int addMonth(int month) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(System.getProperty("user.timezone")));
        cal.add(Calendar.MONTH, month);
        int m = cal.get(Calendar.MONTH) + 1;
        int d = cal.get(Calendar.DAY_OF_MONTH);
        return cal.get(Calendar.YEAR) * 10000 + m * 100 + d;
    } // End addMonth
    
    /**
     * 获取加减月的日期
     * @param month 月数
     * @return String 日期
     */
    public static String addMonth(int month, JzbDateStr format) {
        String result;
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(System.getProperty("user.timezone")));
        cal.add(Calendar.MONTH, month);
        try {
            result = JzbDataFormat.dateToString(cal.getTime(), format);
        } catch (Exception e) {
            result = "";
        }
        return result;
    } // End addMonth
    
    /**
     * 获取某一时刻的时间
     * @param year 年份
     * @param month 月份
     * @param day 日期
     * @param hour 小时
     * @param minute 分钟
     * @param second 秒钟
     * @return long 时间戳
     */
    public static long getTimesForDay(int year, int month, int day, int hour, int minute, int second, int milli) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day, hour, minute, second);
        cal.set(Calendar.MILLISECOND, milli);
        return cal.getTimeInMillis();
    } // End getTimesForDay
    
    /**
     * 以0：0：0为时间的前面天数。
     * @param days 天数 正数为已经过去的。负数为未来的日期
     * @return long 指定日期的时间戳
     */
    public static long addDaysToZeroTimes(int days) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DATE, days);
        return cal.getTimeInMillis();
    } // End addDaysToZeroTimes
    
    /**
     * 获取指定之前或之后的日期
     * @param days 天数 正数为已经过去的。负数为未来的日期
     * @return long 指定日期的时间戳
     */
    public static int addDays(int days) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, days);
        int m = cal.get(Calendar.MONTH) + 1;
        int d = cal.get(Calendar.DAY_OF_MONTH);
        return cal.get(Calendar.YEAR) * 10000 + m * 100 + d;
    } // End addDays
    
    /**
     * 以当前时间的前面天数。
     * @param days 天数 正数为已经过去的。负数为未来的日期
     * @return long 指定日期的时间戳
     */
    public static long addDaysToTimes(int days) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 0 - days);
        return cal.getTimeInMillis();
    } // End addDaysToTimes
    
    /**
     * 根据日期的时间戳转成指定的日期字符串
     * @param times 时间戳
     * @param format 格式化字符串
     * @return String 日期字符串
     */
    public static String toDateString(long times, JzbDateStr format) {
        try {
            return JzbDataFormat.dateToString(new Date(times), format);
        } catch (Exception e) {
            return "";
        }
    } // End toDateString
    
    /**
     * 获取当前日期，字符串
     * @return
     */
    public static String getCurStrDate() {
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        int date = cal.get(Calendar.DAY_OF_MONTH);
        return cal.get(Calendar.YEAR) * 10000 + "-" + month * 100 + "-" + date;
    } // End getCurStrDate
    
    /**
     * 获取当前日期，整型
     */
    public static int getCurIntDate() {
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        int date = cal.get(Calendar.DAY_OF_MONTH);
        return cal.get(Calendar.YEAR) * 10000 + month * 100 + date;
    } // End getCurIntDate

    /**
     * 转日期对象
     * @param date
     * @param format
     * @return
     * @throws Exception
     */
    public static Date getDate(String date, JzbDateStr format) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format.getString());
        return dateFormat.parse(date);
    } // End getDate

    /**
     * 获取0点的时间戳
     * @return
     */
    public static long getZeroTimes() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    } // End getZeroTimes
} // End class JzbDateUtil
