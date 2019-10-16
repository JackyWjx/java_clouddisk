package com.jzb.base.data.code;

import com.jzb.base.data.JzbCharset;
import com.jzb.base.util.JzbTools;

/**
 * Base64编码数据流
 * @author Chad
 * @date 2014年4月29日
 * @version 1.0
 * @see
 * @since 1.0
 */
public class JzbBase64 {
    
    /**
     * 字符编码表
     */
    private final static char BASE_CHAR[] =
        {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
            'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
            'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};
    
    /**
     * 带特列字符编码表
     */
    private final static char BASE_ALT_CHAR[] =
        {'!', '"', '#', '$', '%', '&', '\'', '(', ')', ',', '-', '.', ':', ';', '<', '>', '@', '[', ']', '^', '`', '_',
            '{', '|', '}', '~', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
            'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '?'};
    
    /**
     * 转成byte值表
     */
    private static final byte BASE_BYTE[] = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1,
        63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11,
        12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33,
        34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51};
    
    /**
     * 带特殊字符转成byte值表
     */
    private static final byte BASE_ALT_BYTE[] = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, -1, 62, 9, 10, 11, -1,
        52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 12, 13, 14, -1, 15, 63, 16, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
        -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 17, -1, 18, 19, 21, 20, 26, 27, 28, 29, 30, 31, 32,
        33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 22, 23, 24, 25};
    
    /**
     * 私有构造函数,禁止实例化对象
     */
    private JzbBase64() {
    } // End SteelBase64
    
    /**
     * 根据指定的数据字符串，按Base64编码
     * @param data 编码字符串
     * @return String 编码结果
     */
    public static String encode(String data) {
        return encode(data.getBytes(JzbCharset.UTF8));
    }// End encode
    
    /**
     * 根据指定的数据流，按Base64编码
     * @param data 编码字符串
     * @return String 编码结果
     */
    public static String encode(byte[] data) {
        return encode(data, false);
    }// End encode
    
    /**
     * 根据指定的数据字符串，按Base64编码
     * @param data 编码字符串
     * @return String 编码结果
     */
    public static String encodeAdvanced(String data) {
        return encodeAdvanced(data.getBytes(JzbCharset.UTF8));
    } // End encodeAdvanced
    
    /**
     * 根据指定的数据流，按Base64编码
     * @param data 编码字符串
     * @return String 编码结果
     */
    public static String encodeAdvanced(byte[] data) {
        return encode(data, true);
    }// End encodeAdvanced
    
    /**
     * 根据指定的数据，按Base64编码
     * @param data 数据
     * @param advanced 是否特殊编码
     * @return String Base64编码
     */
    private static String encode(byte[] data, boolean advanced) {
        // 数据长度
        int len = data.length;
        
        // 计算多少组数据
        int arrLen = len / 3;
        
        // 标记是否要补全数据
        int tag = len - 3 * arrLen;
        
        StringBuilder result = new StringBuilder(4 * ((len + 2) / 3));
        char[] base = advanced ? BASE_ALT_CHAR : BASE_CHAR;
        int step = 0;
        
        // 编码所有匹配数据，最后不足则先不编码
        for (int i = 0; i < arrLen; i++) {
            int c1 = data[step++] & 0xFF;
            int c2 = data[step++] & 0xFF;
            int c3 = data[step++] & 0xFF;
            result.append(base[c1 >>> 2 & 0x3F]);
            result.append(base[c1 << 4 & 0x30 | (c2 >>> 4 & 0x0F)]);
            result.append(base[c2 << 2 & 0x3C | (c3 >>> 6 & 0x03)]);
            result.append(base[c3 & 0x3F]);
        }
        
        // 编码补全数据
        if (tag != 0) {
            int c1 = data[step++] & 0xFF;
            result.append(base[c1 >>> 2 & 0x3F]);
            if (tag == 1) {
                result.append(base[c1 << 4 & 0x30]);
                result.append("==");
            } else {
                int c2 = data[step++] & 0xFF;
                result.append(base[c1 << 4 & 0x30 | (c2 >>> 4 & 0x0F)]);
                result.append(base[c2 << 2 & 0x3c]);
                result.append("=");
            }
        }
        return result.toString();
    } // End encode
    
    /**
     * 根据指定的数据字符串，按Base64解码
     * @param data 解码字符串
     * @return String 解码结果
     */
    public static String decode(String data) {
        return new String(decode(data, false), JzbCharset.UTF8);
    }// End decode
    
    /**
     * 根据指定的数据字符串，按Base64解码
     * @param data 解码字符串
     * @return String 解码结果
     */
    public static String decodeAdvanced(String data) {
        return new String(decode(data, true), JzbCharset.UTF8);
    } // End decodeAdvanced
    
    /**
     * 根据指定的数据字符串，按Base64解码
     * @param data 解码字符串
     * @return byte[] 解码结果
     */
    public static byte[] decodeBuffer(String data) {
        return decode(data, false);
    }// End decodeBuffer
    
    /**
     * 根据指定的数据字符串，按Base64解码
     * @param data 解码字符串
     * @return byte[] 解码结果
     */
    public static byte[] decodeAdvancedBuffer(String data) {
        return decode(data, true);
    } // End decodeAdvancedBuffer
    
    /**
     * 根据指定的数据字符串，按Base64解码
     * @param data 解码字符串
     * @return byte[] 解码结果
     */
    private static byte[] decode(String data, boolean advanced) {
        byte[] result;
        byte base[] = advanced ? BASE_ALT_BYTE : BASE_BYTE;
        
        // 必须有数据进行解码
        if (JzbTools.isEmpty(data)) {
            result = null;
        } else {
            int len = data.length();
            int size = len / 4;
            
            // 数据长度不正确,不予解码
            if (4 * size != len) {
                throw new IllegalArgumentException("String length must be a multiple of four.");
            }
            
            // 删除补全数据
            int delChar = (data.charAt(len - 2) == '=') ? 2 : ((data.charAt(len - 1) == '=') ? 1 : 0);
            result = new byte[3 * size - delChar];
            size -= (delChar == 0 ? 0 : 1);
            int stepData = 0;
            int stepResult = 0;
            
            // 解析全数据长度编码数据
            for (int i = 0; i < size; i++) {
                int i1 = base[data.charAt(stepData++)] & 0xFF;
                int i2 = base[data.charAt(stepData++)] & 0xFF;
                int i3 = base[data.charAt(stepData++)] & 0xFF;
                int i4 = base[data.charAt(stepData++)] & 0xFF;
                result[stepResult++] = (byte)((i1 << 2 | i2 >>> 4) & 0xFF);
                result[stepResult++] = (byte)((i2 << 4 | i3 >>> 2) & 0xFF);
                result[stepResult++] = (byte)((i3 << 6 | i4) & 0xFF);
            }
            
            // 解析最后一位补全数据
            if (delChar != 0) {
                int i1 = base[data.charAt(stepData++)] & 0xFF;
                int i2 = base[data.charAt(stepData++)] & 0xFF;
                result[stepResult++] = (byte)((i1 << 2 | i2 >>> 4) & 0xFF);
                if (delChar == 1) {
                    int i3 = base[data.charAt(stepData++)] & 0xFF;
                    result[stepResult++] = (byte)((i2 << 4 | i3 >>> 2) & 0xFF);
                }
            }
        }
        return result;
    }// End decode
} // End JzbBase64