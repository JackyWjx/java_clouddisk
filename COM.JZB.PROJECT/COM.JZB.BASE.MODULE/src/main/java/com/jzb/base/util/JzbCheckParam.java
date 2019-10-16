package com.jzb.base.util;

import java.util.Map;

/**
 * 验证参数
 * @author chenzhengduan
 * @date 2019/9/17
 */
public class JzbCheckParam {
    /**
     * 私有构造方法，不允许实例化
     */
    private JzbCheckParam(){
    }

    /**
     * 判断指定 strs 全都不为空 则返回true
     * @param param
     * @param strs
     * @return
     */
    public static boolean allNotEmpty(Map<String, Object> param, String[] strs) {
        //分割str
        boolean result = true;
        try {
            //循环判断，进if则param中有值为空，则返回false（有空值）
            for (int i = 0, l = strs.length; i < l; i++) {
                if (JzbTools.isEmpty(param.get(strs[i]))) {
                    result = false;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    /**
     * 指定 strs 有一个为空则返回true
     * @param param
     * @param strs
     * @return
     */
    public static boolean haveEmpty(Map<String, Object> param, String[] strs) {
        //分割str
        boolean result = false;
        try {
            //循环判断，进if则param中有值为空，则返回true（有空值）
            for (int i = 0, l = strs.length; i < l; i++) {
                if (JzbTools.isEmpty(param.get(strs[i]))) {
                    result = true;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }


    /**
     * 指定 strs 有一个不为空则返回true
     * @param param
     * @param strs
     * @return
     */
    public static boolean oneIsNotEmpty(Map<String, Object> param, String[] strs) {
        //分割str
        boolean result = false;
        try {
            //循环判断，进if则param中有值为空，则返回true（有空值）
            for (int i = 0, l = strs.length; i < l; i++) {
                if (!JzbTools.isEmpty(param.get(strs[i]))) {
                    result = true;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }
}
