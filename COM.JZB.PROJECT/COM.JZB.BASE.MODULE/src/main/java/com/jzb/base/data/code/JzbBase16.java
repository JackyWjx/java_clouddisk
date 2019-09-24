package com.jzb.base.data.code;

import com.jzb.base.util.JzbTools;

/**
 * 二进制数据流转为16进制字符串表示
 * @author Chad
 * @date 2014年4月29日
 * @version 1.0
 * @see
 * @since 1.0
 */
public class JzbBase16 {
    /**
     * 默认构造函数
     */
    private JzbBase16() {
    } // End JzbBase16
    
    /**
     * 二进制数据流，转为16进制字符串
     * @param data 二进制数据流
     * @return String 16进制字符串
     */
    public static String encode(byte[] data) {
        StringBuilder base = new StringBuilder();
        for (int i = 0, l = data.length; i < l; i++) {
            base.append(encode(data[i]));
        }
        return base.toString();
    } // End encode
    
    /**
     * 一个二进制位，转换为16进制字符
     * @param data 二进制位
     * @return String 16进制字符串
     */
    private static String encode(byte data) {
        return encode0((byte)(data >>> 4 & 0x0000000F)) + "" + encode0((byte)(data & 0x0000000F));
    } // End encode
    
    /**
     * 把四位二进制数据，转为0-A的字符
     * @param b 二进制数据
     * @return Char 字符
     */
    private static char encode0(byte b) {
        char result = '0';
        switch (b) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                result += b;
                break;
            case 10:
                result = 'A';
                break;
            case 11:
                result = 'B';
                break;
            case 12:
                result = 'C';
                break;
            case 13:
                result = 'D';
                break;
            case 14:
                result = 'E';
                break;
            case 15:
                result = 'F';
                break;
            default:
                break;
        }
        return result;
    } // End encode0
    
    /**
     * 把16进制字符串转为2进制数据, 解码失败, 则返回null
     * @param base 16进制字符串
     * @return byte 数据流
     */
    public static byte[] decode(String base) {
        int len = base.length();
        byte[] result = new byte[len / 2];
        try {
            for (int i = 0; i < len / 2; i++) {
                result[i] = decode(base.charAt(i * 2), base.charAt(i * 2 + 1));
            }
        } catch (Exception ex) {
            result = null;
            JzbTools.logError(JzbBase16.class.toString(), "decode", "ERROR", ex.toString());
        }
        
        return result;
    } // End decode
    
    /**
     * 把两位16进制
     * @param c1 高位
     * @param c2 低位
     * @return byte 转换值
     */
    private static byte decode(char c1, char c2) throws Exception {
        return (byte)(((decode(c1) << 4) + decode(c2)) & 0x000000FF);
    } // End toByte
    
    /**
     * 根据16进制字符，转为整型数值
     * @param c 16进制字符
     * @return int 转为结果值
     * @throws Exception 非16进制值
     */
    private static int decode(char c) throws Exception {
        int result;
        switch (c) {
            case '0':
                result = 0;
                break;
            case '1':
                result = 1;
                break;
            case '2':
                result = 2;
                break;
            case '3':
                result = 3;
                break;
            case '4':
                result = 4;
                break;
            case '5':
                result = 5;
                break;
            case '6':
                result = 6;
                break;
            case '7':
                result = 7;
                break;
            case '8':
                result = 8;
                break;
            case '9':
                result = 9;
                break;
            case 'a':
            case 'A':
                result = 10;
                break;
            case 'b':
            case 'B':
                result = 11;
                break;
            case 'c':
            case 'C':
                result = 12;
                break;
            case 'd':
            case 'D':
                result = 13;
                break;
            case 'e':
            case 'E':
                result = 14;
                break;
            case 'f':
            case 'F':
                result = 15;
                break;
            default:
                throw new Exception("Data Error:'" + c + "'.");
        }
        return result;
    } // End decode
} // End JzbBase16