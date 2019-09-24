package com.jzb.activity.vo;

import java.util.Random;

/**
 * @Description: 随机生成数字加英文
 * @Author duanfeiyu
 * @Version v1.0
 * @Since 1.0
 * @Date: 2019/8/10 15:49
 */
public class JZBJSonMax {
    /**
     * 随机生成包含大小写字母及数字的字符串
     * @param length
     * @return
     */
    public static String getStringRandom(int length) {
        String val = "";
        Random random = new Random();

        //参数length，表示生成几位随机数
        for(int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if( "char".equalsIgnoreCase(charOrNum) ) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2)%2 == 0 ? 65 : 97;
                val += (char)(random.nextInt(26) + temp);
            } else if( "num".equalsIgnoreCase(charOrNum) ) {
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;

    }


    /**
     * 生成随机数字,
     * @param length
     * @return
     */
    public static String getNumRandom(int length) {
        String val = "";
        Random random = new Random();
        //参数length，表示生成几位随机数
        for(int i = 0; i < length; i++) {
            val += String.valueOf(random.nextInt(10));
        }
        return val;
    }

    /**
     * 生成随机数字和小写字母串,
     * @param length
     * @return
     */
    public static String getNumSmallCharRandom(int length) {

        String val = "";
        Random random = new Random();
        //参数length，表示生成几位随机数
        for(int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if( "char".equalsIgnoreCase(charOrNum) ) {
                //输出小写字母
                val += (char)(random.nextInt(26) + 97);
            } else if( "num".equalsIgnoreCase(charOrNum) ) {
                //输出数字
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }


    /**
     * 生成随机数字和大写字母串,
     * @param length
     * @return
     */

    public static String getNumBigCharRandom(int length) {
        String val = "";
        Random random = new Random();

        //参数length，表示生成几位随机数
        for(int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if( "char".equalsIgnoreCase(charOrNum) ) {
                //输出大写字母
                val += (char)(random.nextInt(26) + 65);

            } else if( "num".equalsIgnoreCase(charOrNum) ) {
                //输出数字
                val += String.valueOf(random.nextInt(10));
            }
        }
        return val;
    }



}
