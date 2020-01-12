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


    /**
     * 获取当前月最后一天
     * @return
     */
    public static Long getCurrentMonthLastDay() {
        Calendar cale = Calendar.getInstance();
        cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 1);
        cale.set(Calendar.DAY_OF_MONTH, 0);
        cale.set(Calendar.HOUR_OF_DAY, 23);
        cale.set(Calendar.MINUTE, 59);
        cale.set(Calendar.SECOND, 59);
        cale.set(Calendar.MILLISECOND, 999);
        return (cale.getTime()).getTime();
    }

    /**
     * 获取当前月第一天
     * @return
     */
    public static Long getCurrentMonthFirstDay() {
        Calendar cale = Calendar.getInstance();
        cale = Calendar.getInstance();
        cale.add(Calendar.MONTH, 0);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        cale.set(Calendar.HOUR_OF_DAY, 00);
        cale.set(Calendar.MINUTE, 00);
        cale.set(Calendar.SECOND, 00);
        cale.set(Calendar.MILLISECOND, 000);
        return (cale.getTime()).getTime();
    }

    /**
     * 获取当前周最后一天
     * @param date
     * @return
     */
    public static Long getLastDayOfWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(date);
            cal.set(Calendar.DAY_OF_WEEK, 2);
            cal.set(Calendar.DATE, cal.get(Calendar.DATE) + 6);
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            cal.set(Calendar.MILLISECOND, 999);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return (cal.getTime()).getTime();
    }

    /**
     * 获取当前周的第一天
     * @param date
     * @return
     */
    public static Long getFirstDayOfWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(date);
            cal.set(Calendar.DAY_OF_WEEK, 2);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return (cal.getTime()).getTime();
    }



    /**
     * 获取当前天开始时间
     * @param date
     * @return
     */
    public static Long getFirstDayOfDay(Date date) {
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(date);
            cal.set(Calendar.HOUR_OF_DAY,0);
            cal.set(Calendar.MINUTE,0);
            cal.set(Calendar.SECOND,0);
            cal.set(Calendar.MILLISECOND,0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return (cal.getTime()).getTime();
    }

    /**
     * 获取当前天结束时间
     * @param date
     * @return
     */
    public static Long getLastDayOfDay(Date date) {
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(date);
            cal.set(Calendar.HOUR_OF_DAY,23);
            cal.set(Calendar.MINUTE,59);
            cal.set(Calendar.SECOND,59);
            cal.set(Calendar.MILLISECOND,999);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return (cal.getTime()).getTime();
    }


    /**
     * 获取当年的第一天
     * @param
     * @return
     */
    public static Date getCurrYearFirst(){
        Calendar currCal=Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        return getYearFirst(currentYear);
    }

    /**
     * 获取当年的最后一天
     * @param
     * @return
     */
    public static Date getCurrYearLast(){
        Calendar currCal=Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        return getYearLast(currentYear);
    }

    /**
     * 获取某年第一天日期
     * @param year 年份
     * @return Date
     */
    public static Date getYearFirst(int year){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        Date currYearFirst = calendar.getTime();
        return currYearFirst;
    }

    /**
     * 获取某年最后一天日期
     * @param year 年份
     * @return Date
     */
    public static Date getYearLast(int year){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        Date currYearLast = calendar.getTime();

        return currYearLast;
    }




} // End class JzbDateUtil
