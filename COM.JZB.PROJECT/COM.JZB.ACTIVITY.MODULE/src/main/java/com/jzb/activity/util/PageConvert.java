package com.jzb.activity.util;

import com.jzb.base.data.JzbDataType;

import java.util.Map;

public class PageConvert {
    public PageConvert(){}

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
