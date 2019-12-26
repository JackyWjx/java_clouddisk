package com.jzb.base.util;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *  @author: gongWei
 *  @Date: Created in 2019/12/26 11:55
 *  @Description:
 */
public class StrUtil {

    private StrUtil() {

    }

    /**
     * @author: gongWei
     * @Date: 2019/12/26 9:55
     * @Description: 字符型集合按特殊字符拼接转字符串
     */

    public static String list2String(List<String> list, String markStr) {
        StringBuffer sbf = new StringBuffer();
        for (int i = 0, a = list.size(); i < a; i++) {
            if(StringUtils.isBlank(list.get(i))){
                continue;
            }
            sbf.append(list.get(i)).append(markStr);
        }
        if(StringUtils.isNotBlank(sbf)){
            String returnStr = sbf.substring(0,sbf.length()-1);
            return returnStr;
        }
        return "";

    }

    /**
     * @author: gongWei
     * @Date: 2019/12/26 9:55
     * @Description: 字符串按特殊字符切割为字符型集合
     */

    public static List<String> string2List(String str, String markStr) {
        List<String> stringList = new ArrayList<>();
        if(StringUtils.isBlank(str)){
            return stringList;
        }
        stringList = Arrays.asList(str.split(markStr));
        return stringList;
    }

}
