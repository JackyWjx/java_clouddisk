package com.jzb.base.io;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;

import com.jzb.base.util.JzbTools;

/**
 * 针对数据操作流的简单实例。
 * <p>
 * @author Chad
 * @date 2014年8月5日
 * @version 1.0
 * @see SteelDataParse
 * @since 1.0
 */
public final class JzbStreamUtil {
    /**
     * 私有构造方法，不允许实例化
     */
    private JzbStreamUtil() {
    } // End JzbStreamUtil
    
    /**
     * 关闭打开的数据流
     * @param stream 数据流
     * @return true:关闭成功;false:关闭失败.
     */
    public static boolean closeStream(Closeable stream) {
        boolean isClose = false;
        try {
            // 关闭数据流
            if (stream != null) {
                stream.close();
            }
            isClose = true;
        } catch (Exception ex) {
            JzbTools.logError("SteelSimpleStream.closeStream error.", "ERROR:", throwToString(ex));
        }
        
        // 返回数据流是否被关闭
        return isClose;
    } // End closeStream
    
    /**
     * 异常输入到字符串
     * @param t 异常
     * @return String 类型数据 异常堆栈数据
     */
    public static String throwToString(Throwable t) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(bos);
        t.printStackTrace(ps);
        return bos.toString();
    } // End throwToString
    
    /**
     * 从流中读出一行数据
     * @return String 数据行 
     */
    public static String getSystemIn() {
        int size;
        
        // 加载输出流
        BufferedInputStream bis = new BufferedInputStream(System.in);
        byte[] data;
        try {
            // 读一组数据
            data = new byte[bis.available()];
            size = bis.read(data);
        } catch (IOException e) {
            size = 0;
            data = new byte[0];
        }
        
        return size == 0 ? "" : new String(Arrays.copyOf(data, size)).trim();
    } // End getSystemIn
} // End class JzbStreamUtil