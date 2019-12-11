package com.jzb.operate.util;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.util.JzbTools;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
     * @param  index 加密值  list  产出所有数据
     **/
    public static List<Integer> getPrindex(int index , List<Map<String , Object>> list){
        List<Integer> result =  new ArrayList<>();
        for(int i = 0 ;i < list.size() ;i++){
            int  prindex = JzbDataType.getInteger(list.get(i).get("prindex"));
            if((index & prindex ) == prindex){
                result.add(prindex);
            }
        }
        return result;
    }


}
