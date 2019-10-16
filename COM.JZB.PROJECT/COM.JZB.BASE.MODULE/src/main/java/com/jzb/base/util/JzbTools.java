package com.jzb.base.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.Map;

import com.jzb.base.data.JzbDataFormat;
import com.jzb.base.data.JzbDataType;
import com.jzb.base.data.date.JzbDateStr;
import com.jzb.base.io.JzbStreamUtil;

/**
 * 针对数据对象在通信层、数据配置文件或数据库中设计大多为字符串类型，在使用的时候可以随时对类型进行转换。
 * <p>
 * 在上层的类对象中以属于的形式提供给调用者，调用者根据实际的数据类型转换成目标对象数据。<br>
 * 当给定的Object数据对象为空时，则按默认的转换类型数据转换。<br>
 * 当给定的Object数据对象不能转换成指定的类型数据，也则按默认的转换。<br>
 * 转换数据的规则，请参考 SteelDataType。若转换规则不能满足使用要求，则采用返回数据原型 getObject 另行处理。
 * @author Chad
 * @date 2014年12月7日
 * @version 1.0
 * @see SteelTools
 * @since 1.0
 */
public class JzbTools {
    /**
     * 私有构造方法，不允许实例化
     */
    private JzbTools() {
    } // End JzbTools
    
    /**
     * 判断一个对象是否为空
     * 字符串则去除前后的空格进行比较
     * 数组则按长度为0进行比较
     * @param object 数据对象
     * @return true:数据对象为空;false:数据对象非空.
     */
    public static boolean isEmpty(Object object) {
        return JzbDataType.isEmpty(object);
    } // End isEmpty
    
    /**
     * 输出错误信息
     * @param ex 异常对象
     */
    public static void logError(Exception ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        try {
            ex.printStackTrace(pw);
            logError(new Object[] {sw.toString()});
        } finally {
            JzbStreamUtil.closeStream(pw);
            JzbStreamUtil.closeStream(sw);
        }
    } // End logError
    
    /**
     * 输出错误信息
     * @param object 异常对象
     */
    public static void logError(Object object) {
        logError(new Object[] {object});
    } // End logError
    
    /**
     * 输出信息到控制台
     * @param object 输出的数据对象
     */
    public static void logError(Object... object) {
        StringBuilder result = new StringBuilder();
        for (int i = 0, l = object.length; i < l; i++) {
            result.append("[").append(object[i]).append("]");
        }
        Thread current = Thread.currentThread();
        StackTraceElement element = current.getStackTrace()[3];
        System.err.println(new StringBuilder("===>>[").append(getTime())
            .append("][ERROR][")
            .append(current.getName())
            .append("][")
            .append(element.getClassName())
            .append("][")
            .append(element.getMethodName())
            .append("][")
            .append(element.getLineNumber())
            .append("]")
            .append(result)
            .toString());
    } // End logInfo
    
    /**
     * 输出信息到控制台
     * @param object 输出的数据对象
     */
    public static void logInfo(Object object) {
        System.out.println("===>>[" + getTime() + "][INFO][" + object + "]");
    } // End logInfo
    
    /**
     * 输出信息到控制台
     * @param object 输出的数据对象
     */
    public static void logInfo(Object... object) {
        StringBuilder result = new StringBuilder();
        for (int i = 0, l = object.length; i < l; i++) {
            result.append("[").append(object[i]).append("]");
        }
        System.out.println("===>>[" + getTime() + "][INFO]" + result);
    } // End logInfo
    
    /**
     * 获取时间
     * @return String time
     */
    private static String getTime() {
        String time;
        try {
            time = JzbDataFormat.dateToString(new Date(), JzbDateStr.yyyy_MM_dd_HH_mm_ss_SSS);
        } catch (Exception ex) {
            logError(ex);
            time = "";
        }
        return time;
    } // End getTime
    
    /**
     * 显示Map中的数据
     * @param map 数据对象
     */
    public static void showMap(Map<?, ?> map) {
        logInfo(JzbArrays.mapToString(map));
    } // End showMap
    
    /**
     * 获取操作系统的类型
     * @return String 操作系统类型
     */
    public static String osType() {
        return ""; // TODO 2019-07-18
    } // End osType
    
    /**
     * 获取操作系统版本
     * @return String 版本号
     */
    public static String osVersion() {
        return ""; // TODO 2019-07-18
    } // End osVersion
    
    /**
     * 从流中读出一行数据
     * @return String 数据行 
     */
    public static String getSystemIn() {
        return JzbStreamUtil.getSystemIn();
    } // End getSystemIn
    
    /**
     * 线程等待
     * @param millis 等待指定毫秒
     */
    public static void waitTime(int millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception ex) {
        }
    } // End waitTime
    
    /**
     * 退出系统
     */
    public static void exitSystem() {
        System.exit(0);
    } // End exitSystem
} // End JzbTools