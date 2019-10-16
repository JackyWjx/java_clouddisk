package com.jzb.base.data.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jzb.base.data.JzbDataType;

/**
 * 针对常用的正则表达式字符串进行定义
 * <p>
 * @author Chad
 * @date 2014年8月5日
 * @version 1.0
 * @see
 * @since 1.0
 */
public final class JzbRegex {
    /**
     * 默认私有构造函数，禁止此类被实例化使用<p>
     */
    private JzbRegex() {
    } // End JzbRegex
    
    /**
     * 任意符合数值型数据的正则表达式。
     * <p>
     * 给定任意符合数据值型数据的字符串通过SteelDataType.getDouble则能获取对应数据值。<br>
     * 给定正则表达式只判断符合数据格式，不做数据值范围判断，如果超过最大或最小值范围，则需要数据转换时处理。<br>
     * 如:+0.0; -0.0; 1.0; -1.0; 1; 0.1等等。<br>
     * 转换数据参考：{@link JzbDataType#getDouble SteelDataType.getDouble}、{@link SteelDataCheck#checkToDouble SteelDataCheck.checkToDouble}
     */
    public final static String REG_NUM_STRING = "(^[-+]?[0]{0,}[\\d]+([.][\\d]+)?$)";
    
    /**
     * 任意符合长整数值数据的正则表达式。
     * <p>
     * 此正则表达式可加+、-符号，其后可以使用任意长度的0（忽略），再采用19位数值进行判断，没有做最大数值的验证。<br>
     * 如：给定任意符合整数值型数据的字符串通过SteelDataType.getLong则能获取对应数据值。<br>
     * 给定正则表达式只判断符合数据格式，不做数据值范围判断，如果超过最大或最小值范围，则需要数据转换时处理。<br>
     * 如：-0; +0; 1; 2; -1; -2等等。<br>
     * 转换数据参考：{@link JzbDataType#getLong SteelDataType.getLong}、{@link SteelDataCheck#checkToLong SteelDataCheck.checkToLong}
     */
    public final static String REG_INT_STRING = "(^[-+]?[0]{0,}[\\d]{1,19}$)";
    
    /**
     * 根据提供指定的字符序列，判断是否有效的长整型数据的字符串。
     * <p>
     * 当正则表达式完全匹配字符串REG_INT_STRING值时，返回true。<br>
     * @param dataStr 字符串序列
     * @return boolean类型数据： true:可转换为长整型数据字符串；false:不可转换为长整型数据字符串。
     */
    public static boolean matchNumStr(String dataStr) {
        return matcher(REG_NUM_STRING, dataStr);
    } // End matchNumStr
    
    /**
     * 根据提供指定的字符序列，判断是否有效的双精度类型数据的字符串。
     * <p>
     * 此验证暂不是一个可靠的返回结果，请慎用。<br>
     * 当正则表达式完全匹配字符串REG_NUM_STRING值时，返回true。<br>
     * @param dataStr 字符串序列
     * @return boolean类型数据： true:可转换为双精度类型数据字符串；false:不可转换为双精度类型数据字符串。
     */
    public static boolean matchIntStr(String dataStr) {
        return matcher(REG_INT_STRING, dataStr);
    } // End matchIntStr
    
    /**
     * 匹配字符串是否符合正则表达式的格式。
     * <p>
     * 当正则表达式完全匹配字符串数据时，返回true。<br>
     * @param regStr 正则表达式
     * @param dataStr 字符序列数据
     * @return boolean类型数据： true:匹配成功；false:匹配失败，不可匹配的字符序列。
     */
    public static boolean matcher(String regStr, CharSequence dataStr) {
        return Pattern.compile(regStr).matcher(dataStr).matches();
    } // End matcher
    
    /**
     * 从字符串中获取正则表达式中的第1个数据
     * @param text 文件字符串
     * @param regex 正则表达式
     * @return String 获取匹配数据
     */
    public static String getString(String text, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher match = pattern.matcher(text);
        return match.find() ? match.group(1) : "";
    } // End getString
} // End class JzbRegex