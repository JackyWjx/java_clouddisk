package com.jzb.operate.util;

import com.jzb.base.util.JzbTools;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author sapientia
 * @Date 2019/12/11 11:15
 */
public class PrindexUtil {

    /**
     * @Author sapientia
     * @Date 11:17
     * @Description 工具类私有构造 不允许实例
     **/
    private PrindexUtil(){

    }

    /**
     * @Author sapientia
     * @Date 11:16 2019/12/11
     * @Description        加密
     **/
    public static int setPrindex(List<Integer> index){
        int dex = 0 ;
        for(int i = 0 ;i < index.size() ;i++){
            if (i == 0) dex = index.get(i); else dex = dex | index.get(i);
        }
        return  dex;
    }

    /**
     * @Author sapientia
     * @Date 11:17 2019/12/11
     * @Description        解密
     **/
    public static List<Integer> getPrindex(int index , List<Integer> list){
//        for(Integer integer : list){
//            if((index & integer) != integer ){
//                list.remove(integer);
//            }
//        }
        for(int i = 0 ;i < list.size() ;i++){
            if((index & list.get(i)) != list.get(i) ){
                list.remove(i);
                i -- ;
            }
        }
        return list;
    }


}
