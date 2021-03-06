package com.jzb.base.log;

import com.jzb.base.data.date.JzbDateStr;
import com.jzb.base.data.date.JzbDateUtil;

/**
 * 日志应用
 */
public final class JzbLoggerUtil {
    /**
     * 私有构造函数
     */
    private JzbLoggerUtil() {
    } // End JzbLoggerUtil

    /**
     * 获取API日志记录
     * @param
     * @param api
     * @param reqTag
     * @param
     * @param ip
     * @param uid
     * @param token
     * @param msgTag
     * @param msg
     * @return
     */
    public static String getApiLogger(String api, String reqTag, String msgLevel, String ip, String uid, String token, String msgTag, String msg) {
        StringBuilder result = new StringBuilder();
        result.append("[1.0][JZB0001][CRM00000000]");
        result.append("[").append(JzbDateUtil.toDateString(System.currentTimeMillis(), JzbDateStr.yyyy_MM_dd_HH_mm_ss_SSS)).append("]");
        result.append("[").append(api).append("]");
        result.append("[").append(reqTag).append("]");
        result.append("[").append(msgLevel).append("]");
        result.append("[").append(Thread.currentThread().getName()).append("]");
        result.append("[").append(ip).append("]");
        result.append("[").append(uid).append("]");
        result.append("[").append(token).append("]");
        result.append("[").append(msgTag).append("]");
        result.append("[").append(msg).append("]");
        return result.toString();
    } // End getApiLogger

    /**
     * SQL日志
     * @param startTime
     * @param msgLevel
     * @param msgTag
     * @param table
     * @param mid
     * @param msg
     * @return
     */
    public static String getSqlLogger(long startTime, String msgLevel, String msgTag, String table, String mid, String msg) {
        StringBuilder result = new StringBuilder();
        result.append("[1.0][JZB0001][CRM00000000]");
        result.append("[").append(JzbDateUtil.toDateString(startTime, JzbDateStr.yyyy_MM_dd_HH_mm_ss_SSS)).append("]");
        result.append("[").append(JzbDateUtil.toDateString(System.currentTimeMillis(), JzbDateStr.yyyy_MM_dd_HH_mm_ss_SSS)).append("]");
        result.append("[").append(msgLevel).append("]");

        Thread current = Thread.currentThread();
        StackTraceElement element = current.getStackTrace()[3];
        String thd = current.getName();
        String cls = element.getClassName();
        String method = element.getMethodName();

        result.append("[").append(thd).append("]");
        result.append("[").append(cls).append("]");
        result.append("[").append(method).append("]");
        result.append("[").append(msgTag).append("]");
        result.append("[").append(table).append("]");
        result.append("[").append(mid).append("]");
        result.append("[").append(msg).append("]");
        return result.toString();
    } // End getSqlLogger

    /**
     * 错误日志
     * @param msgTag
     * @param title
     * @param msg
     * @return
     */
    public static String getErrorLogger(String msgTag, String title, String msg) {
        StringBuilder result = new StringBuilder();
        result.append("[1.0][JZB0001][CRM00000000]");
        result.append("[").append(JzbDateUtil.toDateString(System.currentTimeMillis(), JzbDateStr.yyyy_MM_dd_HH_mm_ss_SSS)).append("]");
        result.append("[ERROR]");

        Thread current = Thread.currentThread();
        StackTraceElement element = current.getStackTrace()[2];
        String thd = current.getName();
        String cls = element.getClassName();
        String method = element.getMethodName();
        String line = element.getLineNumber() + "";

        result.append("[").append(thd).append("]");
        result.append("[").append(cls).append("]");
        result.append("[").append(method).append("]");
        result.append("[").append(line).append("]");
        result.append("[").append(msgTag).append("]");
        result.append("[").append(title).append("]");
        result.append("[").append(msg).append("]");
        return result.toString();
    } // End getErrorLogger
} // End class JzbLoggerUtil
