package com.jzb.base.util;

import java.security.SecureRandom;

/**
 * 随机数操作
 * <p>
 * 常用的字符串操作
 * @author Chad
 * @date 2014年5月29日
 * @version 1.0
 * @see
 * @since 1.0
 */
public final class JzbRandom {
    /**
     * 数据列表
     */
    private final static char[] CHAR_NUM_10 = new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    /**
     * 大写字母列表
     */
    private final static char[] CHAR_CAP_26 = new char[] {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
            'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    /**
     * 小写字母列表
     */
    private final static char[] CHAR_LOW_26 = new char[] {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
            'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

    /**
     * 大小写字母列表
     */
    private final static char[] CHAR_C_52 = new char[] {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
            'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};

    /**
     * 大小写字母、数字列表，去0/1/i/o
     */
    private static final char[] CHAR_C_56 = new char[] {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N',
            'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
            'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '2', '3', '4', '5', '6', '7', '8', '9'};

    /**
     * 大小写字母、数字列表
     */
    private final static char[] CHAR_C_62 =
            new char[] {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
                    'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o',
                    'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    /**
     * 随机数对象
     */
    private final static SecureRandom RANDOM = new SecureRandom();

    static {
        RANDOM.setSeed(System.currentTimeMillis());
    }

    /**
     * 禁止实例化
     */
    private JzbRandom() {
    } // End JzbRandom

    /**
     * 获取随机字符串<br>
     * 随机字符串由大写字母、小写字母、数字组成。
     * @param size 获取随机数字符个数
     * @return 返回生成的随机字符串，String数据对象
     */
    public static String getRandom(int size) {
        StringBuilder result = new StringBuilder();
        SecureRandom random = new SecureRandom();
        random.setSeed(System.currentTimeMillis());
        for (int i = 0; i < size; i++) {
            result.append(CHAR_C_62[RANDOM.nextInt(62)]);
        }
        return result.toString();
    } // End getRandom

    /**
     * 获取随机字符串<br>
     * 随机字符串由数字组成。
     *
     * @param size 获取随机数字符个数
     * @return 返回生成的随机字符串，String数据对象
     */
    public static String getRandomNum(int size) {
        StringBuilder result = new StringBuilder();
        SecureRandom random = new SecureRandom();
        random.setSeed(System.currentTimeMillis());
        for (int i = 0; i < size; i++) {
            result.append(CHAR_NUM_10[RANDOM.nextInt(10)]);
        }
        return result.toString();
    } // End getRandomNum

    /**
     * 获取随机字符串<br>
     * 随机字符串由大写字母、小写字母组成。
     *
     * @param size 获取随机数字符个数
     * @return 返回生成的随机字符串，String数据对象
     */
    public static String getRandomChar(int size) {
        StringBuilder result = new StringBuilder();
        SecureRandom random = new SecureRandom();
        random.setSeed(System.currentTimeMillis());
        for (int i = 0; i < size; i++) {
            result.append(CHAR_C_52[RANDOM.nextInt(52)]);
        }
        return result.toString();
    } // End getRandomChar

    /**
     * 获取随机字符串<br>
     * 随机字符串由小写字母组成。
     *
     * @param size 获取随机数字符个数
     * @return 返回生成的随机字符串，String数据对象
     */
    public static String getRandomCharLow(int size) {
        StringBuilder result = new StringBuilder();
        SecureRandom random = new SecureRandom();
        random.setSeed(System.currentTimeMillis());
        for (int i = 0; i < size; i++) {
            result.append(CHAR_LOW_26[RANDOM.nextInt(26)]);
        }
        return result.toString();
    } // End getRandomCharLow

    /**
     * 获取随机字符串<br>
     * 随机字符串由大写字母组成。
     *
     * @param size 获取随机数字符个数
     * @return 返回生成的随机字符串，String数据对象
     */
    public static String getRandomCharCap(int size) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < size; i++) {
            result.append(CHAR_CAP_26[RANDOM.nextInt(26)]);
        }
        return result.toString();
    } // End getRandomCharCap

    /**
     * 获取随机字符串<br>
     * 随机字符串由大写字母、小写字母、数字组成，但字符中删除了0/O/1/l/o/I区分比较难的6个字符。
     *
     * @param size 获取随机数字符个数
     * @return 返回生成的随机字符串，String数据对象
     */
    public static String getRandomRec(int size) {
        StringBuilder result = new StringBuilder();
        SecureRandom random = new SecureRandom();
        random.setSeed(System.currentTimeMillis());
        for (int i = 0; i < size; i++) {
            result.append(CHAR_C_56[random.nextInt(56)]);
        }
        return result.toString();
    } // End getRandomRec
} // End class JzbRandom
