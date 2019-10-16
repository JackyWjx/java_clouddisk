package com.jzb.base.util;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 数组扩展操作
 * <p>
 * 针对数组的一些常用工具实现。<br>
 *
 * @author Chad
 * @version 1.0
 * @date 2014年8月8日
 * @see
 * @since 1.0
 */
public final class JzbArrays {
    /**
     * 私有构造方法，不允许实例化对象。
     */
    private JzbArrays() {
    } // End SteelArrays

    /**
     * 数组复制及填充方案。
     * <p>
     * 把源数组中数据复制到目标数组中，当目标数组中多余的元素以填充因子fill进行填充。
     *
     * @param src  源数组数据
     * @param dest 目标数组数据
     * @param fill 填充因子数据
     */
    public static void copyArray(byte[] src, byte[] dest, byte fill) {
        int srcLen = src.length;
        System.arraycopy(src, 0, dest, 0, srcLen);

        for (int i = srcLen, l = dest.length; i < l; i++) {
            dest[i] = fill;
        }
    } // End copyArray

    /**
     * 数组复制及填充方案。
     * <p>
     * 把源数组中数据复制到目标数组中，当目标数组中多余的元素以填充因子fill进行填充。
     *
     * @param src  源数组数据
     * @param dest 目标数组数据
     * @param fill 填充因子数据
     */
    public static void copyArray(char[] src, char[] dest, char fill) {
        int srcLen = src.length;
        System.arraycopy(src, 0, dest, 0, srcLen);

        for (int i = srcLen, l = dest.length; i < l; i++) {
            dest[i] = fill;
        }
    } // End copyArray

    /**
     * 数组复制及填充方案。
     * <p>
     * 把源数组中数据复制到目标数组中，当目标数组中多余的元素以填充因子fill进行填充。
     *
     * @param src  源数组数据
     * @param dest 目标数组数据
     * @param fill 填充因子数据
     */
    public static void copyArray(int[] src, int[] dest, int fill) {
        int srcLen = src.length;
        System.arraycopy(src, 0, dest, 0, srcLen);

        for (int i = srcLen, l = dest.length; i < l; i++) {
            dest[i] = fill;
        }
    } // End copyArray

    /**
     * 数组复制及填充方案。
     * <p>
     * 把源数组中数据复制到目标数组中，当目标数组中多余的元素以填充因子fill进行填充。
     *
     * @param <T>  泛型的数据类类型
     * @param src  源数组数据
     * @param dest 目标数组数据
     * @param fill 填充因子数据
     */
    public static <T> void copyArray(T[] src, T[] dest, T fill) {
        int srcLen = src.length;
        System.arraycopy(src, 0, dest, 0, srcLen);

        for (int i = srcLen, l = dest.length; i < l; i++) {
            dest[i] = fill;
        }
    } // End copyArray

    /**
     * 数组复制及填充方案。
     * <p>
     * 把源数组中数据复制到目标数组中，当目标数组中多余的元素以填充因子fill进行填充。
     *
     * @param <T>  泛型的数据类类型
     * @param src  源数组数据
     * @param dest 目标数组数据
     * @param fill 填充因子数据
     */
    public static <T> void copyArray(T[] src, T[] dest, T[] fill) {
        int srcLen = src.length;
        System.arraycopy(src, 0, dest, 0, srcLen);
        System.arraycopy(fill, 0, dest, srcLen, dest.length - srcLen);
    } // End copyArray

    /**
     * 数组复制及填充方案。
     * <p>
     * 把源数组中数据复制到目标数组中，当目标数组中多余的元素以填充因子fill进行填充。
     *
     * @param src  源数组数据
     * @param dest 目标数组数据
     * @param fill 填充因子数据
     */
    public static void copyArray(char[] src, char[] dest, char[] fill) {
        int srcLen = src.length;
        System.arraycopy(src, 0, dest, 0, srcLen);
        System.arraycopy(fill, 0, dest, srcLen, dest.length - srcLen);
    } // End copyArray

    /**
     * 连接两个数组。
     *
     * @param <T>        泛型的数据类类型
     * @param startArray 开始数组
     * @param endArray   结束数组
     * @return T[] 组合后的数组
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] catArray(T[] startArray, T[] endArray) {
        T[] data;

        // 判断数组不为空时，连接数组
        if (startArray != null && endArray != null) {
            // 创建一个新的数组，把startArray和endArray添加到数组中
            data = (T[]) Array.newInstance(startArray.getClass().getComponentType(), startArray.length + endArray.length);
            System.arraycopy(startArray, 0, data, 0, startArray.length);
            System.arraycopy(endArray, 0, data, startArray.length, endArray.length);
        } else {
            data = (startArray == null) ? endArray : startArray;
        }
        return data;
    } // End catArray

    /**
     * 数组数据转为List列表
     *
     * @param <T>   泛型的数据类类型
     * @param array 数组
     * @return List对象，转换后的List集合。
     */
    public static <T> List<T> arrayToList(T[] array) {
        return Arrays.asList(array);
    } // End arrayToList

    /**
     * 获取MAP中的数据值，转为字符串输出
     *
     * @param map MAP数据
     * @return String 拼接结果
     */
    public static String mapToString(Map<?, ?> map) {
        StringBuilder result = new StringBuilder();

        if (map != null) {
            for (Entry<?, ?> entry : map.entrySet()) {
                result.append("[").append(entry.getKey()).append("][").append(entry.getValue()).append("]");
            }
        } else {
            result.append("[NULL]");
        }
        return result.toString().trim();
    } // End mapToString

    /**
     * 获取MAP中的数据值，转为JSON输出
     *
     * @param map MAP数据
     * @return JSONObject JSON对象
     */
    public static JSONObject mapToJson(Map<?, ?> map) {
        JSONObject result = new JSONObject();

        if (map != null) {
            for (Entry<?, ?> entry : map.entrySet()) {
                try {
                    result.put(entry.getKey().toString(), entry.getValue().toString());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return result;
    } // End mapToJson
} // End class SteelArrays