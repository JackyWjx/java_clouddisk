package com.jzb.base.data;

import java.lang.reflect.Array;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import com.jzb.base.data.date.JzbDateStr;
import com.jzb.base.data.regex.JzbRegex;

/**
 * 针对数据对象的基本判断和操作，如数据类型判断，数据类型之间的转换操作。封装一些常用规则的数据类型转换。
 * <p>
 * 字符串String与其它类型转换：<br>
 * String 转数值型数据，先转换为Double，再转换为指定数据类型，默认值为0，异常处理为0。<br>
 * String 转字符型数据，取字符串的第1个字符数据为结果数据。<br>
 * String 转Boolean型数据，true/TRUE/yes/YES/Y/T/1转换为true，否则转换为false。<br>
 * 等等数据类型，都会进行一些封装处理，具体见每个方法的操作注释。<br>
 * 【注意】，本类只对一些常用的操作进行处理，或许处理与您需要的结果不相符合，则不能使用此类进行操作。<br>
 * 但相信此类仍然可以满足在开发中大多的问题处理和实现。
 * @author Chad
 * @date 2013年8月5日
 * @version 1.0
 * @see JzbDataApp
 * @since 1.0
 */
public final class JzbDataType {
    /**
     * 默认私有构造函数，禁止此类被实例化使用
     */
    private JzbDataType() {
    } // End SteelDataType
    
    /**
     * 判断对象是否为Collection类型数据
     * <p>
     * @param data 数据对象
     * @return boolean类型数据： true:Collection数据；false:非Collection数据。
     */
    public static boolean isCollection(Object data) {
        return data instanceof Collection;
    } // End isCollection
    
    /**
     * 判断对象是否为Map类型数据
     * <p>
     * @param data 数据对象
     * @return boolean类型数据： true:Map数据；false:非Map数据。
     */
    public static boolean isMap(Object data) {
        return data instanceof Map;
    } // End isMap
    
    /**
     * 判断对象是否为Array类型数据
     * <p>
     * @param data 数据对象
     * @return boolean类型数据： true:Array数据；false:非Array数据。
     */
    public static boolean isArray(Object data) {
        return data.getClass().isArray();
    } // End isArray
    
    /**
     * 判断对象是否为Byte类型数据
     * <p>
     * @param data 数据对象
     * @return boolean类型数据： true:Byte数据；false:非Byte数据。
     */
    public static boolean isByte(Object data) {
        return data instanceof Byte;
    } // End isByte
    
    /**
     * 判断对象是否为Short类型数据
     * <p>
     * @param data 数据对象
     * @return boolean类型数据： true:Short数据；false:非Short数据。
     */
    public static boolean isShort(Object data) {
        return data instanceof Short;
    } // End isShort
    
    /**
     * 判断对象是否为Integer类型数据
     * <p>
     * @param data 数据对象
     * @return boolean类型数据： true:Integer数据；false:非Integer数据。
     */
    public static boolean isInteger(Object data) {
        return data instanceof Integer;
    } // End isInteger
    
    /**
     * 判断对象是否为Long类型数据
     * <p>
     * @param data 数据对象
     * @return boolean类型数据： true:Long数据；false:非Long数据。
     */
    public static boolean isLong(Object data) {
        return data instanceof Long;
    } // End isLong
    
    /**
     * 判断对象是否为Double类型数据
     * <p>
     * @param data 数据对象
     * @return boolean类型数据： true:Double数据；false:非Double数据。
     */
    public static boolean isDouble(Object data) {
        return data instanceof Double;
    } // End isDouble
    
    /**
     * 判断对象是否为Float类型数据
     * <p>
     * @param data 数据对象
     * @return boolean类型数据： true:Float数据；false:非Float数据。
     */
    public static boolean isFloat(Object data) {
        return data instanceof Float;
    } // End isFloat
    
    /**
     * 判断对象是否为Char类型数据
     * @param data 数据对象
     * @return boolean类型数据： true:Char数据；false:非Char数据。
     */
    public static boolean isChar(Object data) {
        return data instanceof Character;
    } // End isChar
    
    /**
     * 判断对象是否为CharSequence类型数据
     * <p>
     * 对象是否为CharSequence， 即能转换为String类型的CharSequence类型数据。<br>
     * @param data 数据对象
     * @return boolean类型数据： true:CharSequence数据；false:非CharSequence数据。
     */
    public static boolean isString(Object data) {
        return data instanceof CharSequence;
    } // End isString
    
    /**
     * 判断对象是否为空类型数据
     * <p>
     * 空类型数据包括：null、空字符串、长度为0的集合或数组类型数据。<br>
     * @param data 数据对象
     * @return boolean类型数据： true:空数据；false:非空数据。
     */
    public static boolean isEmpty(Object data) {
        boolean result;
        
        // 对象为null时,返回true
        if (null == data) {
            result = true;
            
            // 字符串长度为0时,返回true,否则返回false
        } else if (isString(data) || isChar(data)) {
            result = data.toString().trim().length() == 0;
            
            // 集合，则判断长度是否为0
        } else if (data instanceof Collection) {
            result = ((Collection<?>)data).isEmpty();
            
            // 集合，则判断长度是否为0
        } else if (data instanceof Map) {
            result = ((Map<?, ?>)data).isEmpty();
            
            // 判断是否为数组
        } else if (data.getClass().isArray()) {
            result = Array.getLength(data) == 0;
        } else {
            result = false;
        }
        
        // 需要时再增加判断
        return result;
    } // End isEmpty
    
    /**
     * 判断对象是否为数值类型数据
     * <p>
     * 数值类型数据包括：可转数值的字符串、字符、布尔值。<br>
     * 字符串按正则(^[\\d]+$)匹配。<br>
     * 字符按ASCII编码值转换。<br>
     * 布尔值按true：1；false：0 转换。<br><br>
     * 转换数据参考：{@link JzbDataType#getDouble getDouble}、{@link JzbDataType#getLong getLong}、{@link JzbDataType#getString getString}
     * @param data 数据对象
     * @return boolean类型数据： true:数值类型数据；false:非数值类型数据。
     */
    public static boolean isNumeric(Object data) {
        return isInteger(data) || isShort(data) || isLong(data) || isFloat(data) || isDouble(data) || isByte(data)
            || isChar(data) || (isString(data) && data.toString().trim().matches(JzbRegex.REG_NUM_STRING));
    } // End isNumeric
    
    /**
     * 判断对象是否为Boolean类型数据
     * <p>
     * @param data 数据对象
     * @return boolean类型数据： true:Boolean数据；false:非Boolean数据。
     */
    public static boolean isBoolean(Object data) {
        return data instanceof Boolean;
    } // End isBoolean
    
    /**
     * 判断对象是否为(Date or Calendar)类型数据
     * <p>
     * @param data 数据对象
     * @return boolean类型数据： true:(Date or Calendar)数据；false:非(Date or Calendar)数据。
     */
    public static boolean isDateTime(Object data) {
        return data instanceof Date || data instanceof Calendar;
    } // End isDateTime
    
    /**
     * 根据输入的数据对象转换成Double类型数据
     * <p>
     * 转换规则：<br>
     * Double类型数据：直接返回原型数据值。<br>
     * 数值类型数据：升级成Double类型数据值。<br>
     * 字符类型数据：取字符的ASCII码值。<br>
     * 字符串类型数据：按Double.parseDouble解析结果值。<br>
     * 日期时间类型数据：取日期对象自 1970 年 1 月 1 日 00:00:00 GMT 后的毫秒数。<br>
     * 所有解析操作错误和异常，返回0值。当要获取更准确数据值前，需进行数据转换的格式化判断。<br><br>
     * @param data 数据对象
     * @return double 类型数据
     */
    public static double getDouble(Object data) {
        return isDouble(data) ? (Double)data : getDouble0(data);
    } // End getDouble
    
    /**
     * 根据输入的数据对象，按各种类型的规则，转换成Double类型数据<p>
     * @param data 数据对象
     * @return Double 类型数据
     */
    private static Double getDouble0(Object data) {
        double result;
        
        // 为空时,返回0.0
        if (null == data) {
            result = 0.0d;
            
            // 双精度类型，返回原型数据
        } else if (isDouble(data)) {
            result = (Double)data;
            
            // 布尔值，返回1 或 0
        } else if (isBoolean(data)) {
            result = (Boolean)data ? 1 : 0;
            
            // 字符型，返回ASCII码
        } else if (isChar(data)) {
            result = (Character)data;
            
            // 数值类型，直接返回取值
        } else if (isNumeric(data)) {
            result = Double.parseDouble(data.toString().trim());
            
            // 时间类型，获取毫秒值
        } else if (isDateTime(data)) {
            result = ((data instanceof Date) ? ((Date)data).getTime() : ((Calendar)data).getTimeInMillis());
            
            // 其它类型，返回0.0
        } else {
            try {
                result = Double.parseDouble(data.toString().trim());
            } catch (Exception ex) {
                result = 0.0d;
            }
        }
        
        return result;
    } // End getDouble0
    
    /**
     * 根据输入的数据对象转换成Integer类型数据
     * <p>
     * 转换规则：<br>
     * Integer类型数据：直接返回原型数据值。<br>
     * 其它类型数据，则按先转换成Double类型数据，再取Integer数据值。<br><br>
     * 转换数据参考：{@link JzbDataType#getDouble getDouble}
     * @param data 数据对象
     * @return int 类型数据
     */
    public static int getInteger(Object data) {
        return isInteger(data) ? (Integer)data : getDouble0(data).intValue();
    } // End getInteger
    
    /**
     * 根据输入的数据对象转换成Short类型数据
     * <p>
     * 转换规则：<br>
     * Short类型数据：直接返回原型数据值。<br>
     * 其它类型数据，则按先转换成Double类型数据，再取Short数据值。<br><br>
     * 转换数据参考：{@link JzbDataType#getDouble getDouble}
     * @param data 数据对象
     * @return short 类型数据
     */
    public static short getShort(Object data) {
        return isShort(data) ? (Short)data : getDouble0(data).shortValue();
    } // End getShort
    
    /**
     * 根据输入的数据对象转换成Long类型数据
     * <p>
     * 转换规则：<br>
     * Long类型数据：直接返回原型数据值。<br>
     * 其它类型数据，则按先转换成Double类型数据，再取Long数据值。<br><br>
     * 转换数据参考：{@link JzbDataType#getDouble getDouble}
     * @param data 数据对象
     * @return long 类型数据
     */
    public static long getLong(Object data) {
        return isLong(data) ? (Long)data : getDouble0(data).longValue();
    } // End getLong
    
    /**
     * 根据输入的数据对象转换成Float类型数据
     * <p>
     * 转换规则：<br>
     * Float类型数据：直接返回原型数据值。<br>
     * 其它类型数据，则按先转换成Double类型数据，再取Float数据值。<br><br>
     * 转换数据参考：{@link JzbDataType#getDouble getDouble}
     * @param data 数据对象
     * @return float 类型数据
     */
    public static float getFloat(Object data) {
        return isFloat(data) ? (Float)data : getDouble0(data).floatValue();
    } // End getFloat
    
    /**
     * 根据输入的数据对象转换成Byte类型数据
     * <p>
     * 转换规则：<br>
     * Byte类型数据：直接返回原型数据值。<br>
     * 其它类型数据，则按先转换成Double类型数据，再取Byte数据值。<br><br>
     * 转换数据参考：{@link JzbDataType#getDouble getDouble}
     * @param data 数据对象
     * @return byte 类型数据
     */
    public static byte getByte(Object data) {
        return isByte(data) ? (Byte)data : getDouble0(data).byteValue();
    } // End getByte
    
    /**
     * 根据输入的数据对象转换成Boolean类型数据
     * <p>
     * 转换规则：<br>
     * Boolean类型数据：直接返回原型数据值。<br>
     * 空数据对象返回false数据值。<br>
     * 字符类型数据，按"Y"、"T“、"TRUE"、"1"等转换成true数据值。否则转换成false数据值。<br>
     * 数值类型数据，按大于0的转换成true数据值。否则转换成false数据值。<br>
     * 其它数据类型，按false数据值返回。<br><br>
     * 转换数据参考：{@link JzbDataType#getString getString}、{@link JzbDataType#getDouble getDouble}
     * @param data 数据对象
     * @return boolean 类型数据
     */
    public static boolean getBoolean(Object data) {
        boolean result;
        // 数据对象为null, 返回false;
        if (isEmpty(data)) {
            result = false;
            
            // 数据为布尔对象, 返回其自身值
        } else if (isBoolean(data)) {
            result = ((Boolean)data);
            
            // 按"Y"、"T“、"TRUE"、"1"等转换成true数据值. 其余都为false;
        } else if (isChar(data) || isString(data)) {
            String bln = data.toString().toUpperCase();
            result = bln.equals("Y") || bln.equals("TRUE") || bln.equals("1") || bln.equals("T");
            
            // 数据类型, 大于0返回true; 否则返回false;
        } else if (isNumeric(data)) {
            result = getDouble0(data) > 0;
            
            // 其它类型.返回false;
        } else {
            result = false;
        }
        return result;
    } // End getBoolean
    
    /**
     * 根据输入的数据对象转换成String类型数据
     * <p>
     * 转换规则：<br>
     * String类型数据：直接返回原型数据值。<br>
     * Boolean类型数据，按true转换成“true”，false转换成“false”数据值。<br>
     * 数值类型数据，按对象toString对应的字符串数据值。<br>
     * 如果为日期类型，返回"yyyyMMddHHmmssSSS"字符串。<br><br>
     * 转换数据参考：{@link JzbDataType#getBoolean getBoolean}、{@link JzbDataType#getDouble getDouble}、{@link JzbDataType#getDateTime getDateTime}
     * @param data 数据对象
     * @return String 类型数据
     */
    public static String getString(Object data) {
        String result;
        
        // 空对象返回空字符串
        if (isEmpty(data)) {
            result = "";
            
            // 如果类型为字符串, 返回其自身值.
        } else if (isString(data)) {
            result = (String)data;
            
            // 如果为布尔值,返回true或false
        } else if (isBoolean(data)) {
            result = getBoolean(data) ? "true" : "false";
            
            // 如果为日期类型,返回"YYYYMMDDHHMMSS"字符串
        } else if (isDateTime(data)) {
            try {
                result = JzbDataFormat.dateToString(getDateTime(data), JzbDateStr.yyyyMMddHHmmssSSS);
            } catch (Exception ex) {
                result = data.toString();
            }
        } else {
            result = data.toString();
        }
        return result;
    } // End getString
    
    /**
     * 根据输入的数据对象转换成Character类型数据
     * <p>
     * 转换规则：<br>
     * Character类型数据：直接返回原型数据值。<br>
     * Boolean类型数据，按true转换成“Y”，false转换成“N”数据值。<br>
     * 数值类型数据，按转换成Byte数据值，再转成ASCII对应的字符数据值。<br>
     * 转换失败返回空字符，不建议在不明确数据值前提下进行转换操作。<br><br>
     * 转换数据参考：{@link JzbDataType#getBoolean getBoolean}、{@link JzbDataType#getByte getByte}
     * @param data 数据对象
     * @return char 类型数据
     */
    public static char getChar(Object data) {
        char result;
        // 如果数据为字符类型,返回其自身值
        if (isChar(data)) {
            result = (Character)data;
            
            // 如果数据为布尔类型, true 返回'Y',false返回'N'
        } else if (isBoolean(data)) {
            result = getBoolean(data) ? 'Y' : 'N';
            
            // 如果数据为数字类型,强转为字符
        } else if (isNumeric(data)) {
            result = (char)getByte(data);
            
            // 其它类型转为字符串,返回第一个字符
        } else {
            result = (getString(data) + " ").charAt(0);
        }
        
        return result;
    } // End getChar
    
    /**
     * 根据输入的数据对象转换成Date类型数据
     * <p>
     * 转换规则：<br>
     * Date类型数据：直接返回原型数据值。<br>
     * 数值类型数据，则按先转换成Long类型数据，再按毫秒值取Date类型数据值。<br>
     * 字符串类型数据，则按yyyy-MM-dd HH:mm:ss-SSS转换成Date类型数据值。<br>
     * 其它类型数据返回系统当前时间，不建议使用不明确的数据，采用此方法进行数据类型转换。<br><br>
     * 转换数据参考：{@link JzbDataType#getString getString}、{@link JzbDataType#getLong getLong}
     * @param data 数据对象
     * @return Date 类型数据
     */
    public static Date getDateTime(Object data) {
        Date date = null;
        
        // 数据类型为Date,返回其自身值
        if (data instanceof Date) {
            date = (Date)data;
            
            // 数据类型为Calendar,返回时间值
        } else if (data instanceof Calendar) {
            date = ((Calendar)data).getTime();
            
            // 数据类型为数值型,构造一个时间类型
        } else if (isNumeric(data)) {
            date = new Date(getLong(data));
            
            // 如果为字符串, 获取其值.
        } else if (isString(data)) {
            String str = getString(data).trim();
            
            try {
                // 对字符串进行转换. 转换出错,返回当前日期
                if (!isEmpty(str)) {
                    date = JzbDataFormat.stringToDate(str, JzbDateStr.yyyy_MM_dd_HH_mm_ss_SSS);
                }
            } catch (Exception ex) {
            }
        }
        
        // 返回转换数据, null转换成默认日期(当前时间)
        return date == null ? new Date() : date;
    } // End getDateTime
} // End class SteelDataType