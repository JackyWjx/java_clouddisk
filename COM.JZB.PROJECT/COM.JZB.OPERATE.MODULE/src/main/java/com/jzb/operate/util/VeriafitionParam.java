package com.jzb.operate.util;

import com.jzb.base.util.JzbTools;

import java.util.Map;

public class VeriafitionParam {


    private VeriafitionParam(){
        // 不允许实例化
    }

    /**
     * @param param
     * @param strs
     * @return
     */
    public static boolean isNotEmpty(Map<String, Object> param, String[] strs) {
        //分割str
        boolean result = true;
        try {
            //循环判断，进if则param中有值为空，则返回true（为空）
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
}
