package com.jzb.base.util;

import com.jzb.base.data.JzbDataType;

import java.util.Map;

/**
 * @author chenzhengduan
 * 设置分页参数
 */
public class JzbPageConvert {

    /**
     * 私有构造方法，不允许实例化
     */
    private JzbPageConvert(){}

    /**
     * 设置好分页参数
     * @param params
     */
    public static void setPageRows(Map<String ,Object> params){
        int rows = JzbDataType.getInteger(params.get("pagesize"));
        int page = JzbDataType.getInteger(params.get("pageno"));
        params.put("pageno", JzbDataType.getInteger(page * rows - rows < 0 ? 0 : page * rows - rows));
        params.put("pagesize", rows);
    }
}
