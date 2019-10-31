package com.jzb.base.util;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.data.date.JzbDateStr;
import com.jzb.base.data.date.JzbDateUtil;

/**
 * long转时间格式
 *  * @author chenzhengduan
 *  * @date 2019/10/31
 */
public class JzbTimeToString {

    /**
     * 私有构造方法，不允许实例化
     */
    private JzbTimeToString(){

    }

    /**
     * 转换时间格式
     * @param times
     * @return
     */
    public static String TimeToStringyyyy_MM_dd_HH_mm_ss(Object times){
       return JzbDateUtil.toDateString(JzbDataType.getLong(times),JzbDateStr.yyyy_MM_dd_HH_mm_ss);
    }

    /**
     * 转换时间格式
     * @param times
     * @return
     */
    public static String TimeToStringyyyy_MM_dd(Object times){
        return JzbDateUtil.toDateString(JzbDataType.getLong(times),JzbDateStr.yyyy_MM_dd);
    }
}
