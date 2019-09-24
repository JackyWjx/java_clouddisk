package com.jzb.base.data;

import java.util.Date;

/**
 * 针对数据对象在通信层、数据配置文件或数据库中设计大多为字符串类型，在使用的时候可以随时对类型进行转换。
 * <p>
 * 在上层的类对象中以属于的形式提供给调用者，调用者根据实际的数据类型转换成目标对象数据。<br>
 * 当给定的Object数据对象为空时，则按默认的转换类型数据转换。<br>
 * 当给定的Object数据对象不能转换成指定的类型数据，也则按默认的转换。<br>
 * 转换数据的规则，请参考 SteelDataType。若转换规则不能满足使用要求，则采用返回数据原型 getObject 另行处理。
 * @author Chad
 * @date 2013年12月7日
 * @version 1.0
 * @see JzbDataType
 * @since 1.0
 */
public class JzbDataApp {
    
    /**
     * 数据原型值
     */
    private final Object _data;
    
    /**
     * 通过使用输入的Object数据对象，构造一个新的SteelDataApp数据对象。
     * <p>
     * @param data 数据对象
     */
    public JzbDataApp(Object data) {
        _data = data;
    } // End SteelDataApp
    
    /**
     * 判断对象是否为Collection类型数据
     * @return boolean类型数据： true:Collection数据；false:非Collection数据。
     */
    public boolean isCollection() {
        return JzbDataType.isCollection(_data);
    } // End isCollection
    
    /**
     * 判断对象是否为Map类型数据
     * @return boolean类型数据： true:Map数据；false:非Map数据。
     */
    public boolean isMap() {
        return JzbDataType.isMap(_data);
    } // End isMap
    
    /**
     * 判断对象是否为Array类型数据
     * @return boolean类型数据： true:Array数据；false:非Array数据。
     */
    public boolean isArray() {
        return JzbDataType.isArray(_data);
    } // End isArray
    
    /**
     * 判断对象是否为Byte类型数据
     * @return boolean类型数据： true:Byte数据；false:非Byte数据。
     */
    public boolean isByte() {
        return JzbDataType.isByte(_data);
    } // End isByte
    
    /**
     * 判断对象是否为Short类型数据
     * @return boolean类型数据： true:Short数据；false:非Short数据。
     */
    public boolean isShort() {
        return JzbDataType.isShort(_data);
    } // End isShort
    
    /**
     * 判断对象是否为Integer类型数据
     * @return boolean类型数据： true:Integer数据；false:非Integer数据。
     */
    public boolean isInteger() {
        return JzbDataType.isInteger(_data);
    } // End isInteger
    
    /**
     * 判断对象是否为Long类型数据
     * @return boolean类型数据： true:Long数据；false:非Long数据。
     */
    public boolean isLong() {
        return JzbDataType.isLong(_data);
    } // End isLong
    
    /**
     * 判断对象是否为Double类型数据
     * @return boolean类型数据： true:Double数据；false:非Double数据。
     */
    public boolean isDouble() {
        return JzbDataType.isDouble(_data);
    } // End isDouble
    
    /**
     * 判断对象是否为Float类型数据
     * @return boolean类型数据： true:Float数据；false:非Float数据。
     */
    public boolean isFloat() {
        return JzbDataType.isFloat(_data);
    } // End isFloat
    
    /**
     * 判断对象是否为Char类型数据
     * @return boolean类型数据： true:Char数据；false:非Char数据。
     */
    public boolean isChar() {
        return JzbDataType.isChar(_data);
    } // End isChar
    
    /**
     * 判断对象是否为String类型数据<p>
     * 对象是否为String， 以及能转换为String类型的CharSequence类型数据。<br>
     * @return boolean类型数据： true:String数据；false:非String数据。
     */
    public boolean isString() {
        return JzbDataType.isString(_data);
    } // End isString
    
    /**
     * 判断对象是否为空类型数据<p>
     * 空类型数据包括：null、空字符串、长度为0的集合或数组类型数据。<br>
     * @return boolean类型数据： true:空数据；false:非空数据。
     */
    public boolean isEmpty() {
        return JzbDataType.isEmpty(_data);
    } // End isEmpty
    
    /**
     * 判断对象是否为数值类型数据<p>
     * 数值类型数据包括：可转数值的字符串、字符、布尔值。<br>
     * 字符串按正则(^[\\d]+$)匹配。<br>
     * 字符按ASCII编码值转换。<br>
     * 布尔值按true：1；false：0 转换。<br><br>
     * @return boolean类型数据： true:数值类型数据；false:非数值类型数据。
     */
    public boolean isNumeric() {
        return JzbDataType.isNumeric(_data);
    } // End isNumeric
    
    /**
     * 判断对象是否为Boolean类型数据<p>
     * @return boolean类型数据： true:Boolean数据；false:非Boolean数据。
     */
    public boolean isBoolean() {
        return JzbDataType.isBoolean(_data);
    } // End isBoolean
    
    /**
     * 判断对象是否为(Date or Calendar)类型数据<p>
     * @return boolean类型数据： true:(Date or Calendar)数据；false:非(Date or Calendar)数据。
     */
    public boolean isDateTime() {
        return JzbDataType.isDateTime(_data);
    } // End isDateTime
    
    /**
     * 判断对象是否为NULL类型数据<P>
     * @return boolean类型数据： true:Null数据；false:非Null数据。
     */
    public boolean isNull() {
        return _data == null;
    } // End isNull
    
    /**
     * 根据输入的数据对象转换成Double类型数据<p>
     * 转换规则：<br>
     * Double类型数据：直接返回原型数据值。<br>
     * 数值类型数据：升级成Double类型数据值。<br>
     * 字符类型数据：取字符的ASCII码值。<br>
     * 字符串类型数据：按Double.parseDouble解析结果值。<br>
     * 日期时间类型数据：取日期对象自 1970 年 1 月 1 日 00:00:00 GMT 后的毫秒数。<br>
     * 所有解析操作错误和异常，返回0值。当要获取更准确数据值前，需进行数据转换的格式化判断。<br><br>
     * 转换数据参考：{@link JzbDataFormat#canDouble SteelDataFormat.canDouble}
     * @return double 类型数据
     */
    public double getDouble() {
        return JzbDataType.getDouble(_data);
    } // End getDouble
    
    /**
     * 根据输入的数据对象转换成Integer类型数据<p>
     * 转换规则：<br>
     * Integer类型数据：直接返回原型数据值。<br>
     * 其它类型数据，则按先转换成Double类型数据，再取Integer数据值。<br><br>
     * 转换数据参考：{@link JzbDataApp#getDouble getDouble}
     * @return int 类型数据
     */
    public int getInteger() {
        return JzbDataType.getInteger(_data);
    } // End getInteger
    
    /**
     * 根据输入的数据对象转换成Short类型数据<p>
     * 转换规则：<br>
     * Short类型数据：直接返回原型数据值。<br>
     * 其它类型数据，则按先转换成Double类型数据，再取Short数据值。<br><br>
     * 转换数据参考：{@link JzbDataApp#getDouble getDouble}
     * @return short 类型数据
     */
    public short getShort() {
        return JzbDataType.getShort(_data);
    } // End getShort
    
    /**
     * 根据输入的数据对象转换成Long类型数据<p>
     * 转换规则：<br>
     * Long类型数据：直接返回原型数据值。<br>
     * 其它类型数据，则按先转换成Double类型数据，再取Long数据值。<br><br>
     * 转换数据参考：{@link JzbDataApp#getDouble getDouble}
     * @return long 类型数据
     */
    public long getLong() {
        return JzbDataType.getLong(_data);
    } // End getLong
    
    /**
     * 根据输入的数据对象转换成Float类型数据<p>
     * 转换规则：<br>
     * Float类型数据：直接返回原型数据值。<br>
     * 其它类型数据，则按先转换成Double类型数据，再取Float数据值。<br><br>
     * 转换数据参考：{@link JzbDataApp#getDouble getDouble}
     * @return float 类型数据
     */
    public float getFloat() {
        return JzbDataType.getFloat(_data);
    } // End getFloat
    
    /**
     * 根据输入的数据对象转换成Byte类型数据<p>
     * 转换规则：<br>
     * Byte类型数据：直接返回原型数据值。<br>
     * 其它类型数据，则按先转换成Double类型数据，再取Byte数据值。<br><br>
     * 转换数据参考：{@link JzbDataApp#getDouble getDouble}
     * @return byte 类型数据
     */
    public byte getByte() {
        return JzbDataType.getByte(_data);
    } // End getByte
    
    /**
     * 根据输入的数据对象转换成Boolean类型数据<p>
     * 转换规则：<br>
     * Boolean类型数据：直接返回原型数据值。<br>
     * 空数据对象返回false数据值。<br>
     * 字符类型数据，按"Y"、"T“、"TRUE"、"1"等转换成true数据值。否则转换成false数据值。<br>
     * 数值类型数据，按大于0的转换成true数据值。否则转换成false数据值。<br>
     * 其它数据类型，按false数据值返回。<br><br>
     * 转换数据参考：{@link JzbDataApp#getString getString}、{@link JzbDataApp#getDouble getDouble}
     * @return boolean 类型数据
     */
    public boolean getBoolean() {
        return JzbDataType.getBoolean(_data);
    } // End getBoolean
    
    /**
     * 根据输入的数据对象转换成String类型数据<p>
     * 转换规则：<br>
     * String类型数据：直接返回原型数据值。<br>
     * Boolean类型数据，按true转换成“true”，false转换成“false”数据值。<br>
     * 数值类型数据，按对象toString对应的字符串数据值。<br>
     * 如果为日期类型，返回"yyyyMMddHHmmss"字符串。<br><br>
     * 转换数据参考：{@link JzbDataApp#getBoolean getBoolean}、{@link JzbDataApp#getDouble getDouble}、{@link JzbDataApp#getDateTime getDateTime}
     * @return String 类型数据
     */
    public String getString() {
        return _data == null ? "" : JzbDataType.getString(_data);
    } // End getString
    
    /**
     * 根据输入的数据对象转换成String类型数据<p>
     * 轮换后的String数据去掉其前后空格。<br>
     * 转换数据参考：{@link JzbDataApp#getString getString}
     * @return String 类型数据
     */
    public String getTrim() {
        return getString().trim();
    } // End getTrim
    
    /**
     * 根据输入的数据对象转换成Character类型数据<p>
     * 转换规则：<br>
     * Character类型数据：直接返回原型数据值。<br>
     * Boolean类型数据，按true转换成“Y”，false转换成“N”数据值。<br>
     * 数值类型数据，按转换成Byte数据值，再转成ASCII对应的字符数据值。<br>
     * 转换失败返回空字符，不建议在不明确数据值前提下进行转换操作。<br><br>
     * 转换数据参考：{@link JzbDataApp#getBoolean getBoolean}、{@link JzbDataApp#getByte getByte}
     * @return char 类型数据
     */
    public char getChar() {
        return JzbDataType.getChar(_data);
    } // End getChar
    
    /**
     * 根据输入的数据对象转换成Date类型数据<p>
     * 转换规则：<br>
     * Date类型数据：直接返回原型数据值。<br>
     * 数值类型数据，则按先转换成Long类型数据，再按毫秒值取Date类型数据值。<br>
     * 字符串类型数据，则按yyyy-MM-dd HH:mm:ss转换成Date类型数据值。<br>
     * 其它类型数据返回系统当前时间，不建议使用不明确的数据，采用此方法进行数据类型转换。<br><br>
     * 转换数据参考：{@link JzbDataApp#getString getString}、{@link JzbDataApp#getLong getLong}
     * @return Date 类型数据
     */
    public Date getDateTime() {
        return JzbDataType.getDateTime(_data);
    } // End getDateTime
    
    /**
     * 根据输入的数据对象的原型数据返回<p>
     * @return Object 数据原型对象
     */
    public Object getObject() {
        return _data;
    } // End getObject
} // End class JzbDataApp
