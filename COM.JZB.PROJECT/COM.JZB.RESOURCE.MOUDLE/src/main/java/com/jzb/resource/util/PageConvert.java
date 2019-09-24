package com.jzb.resource.util;

import com.jzb.base.data.JzbDataType;

import java.util.Map;

public class PageConvert {
    private PageConvert(){}

    /**
     * 设置好分页参数
     * @param params
     */
    public static void setPageRows(Map<String ,Object> params){
        int rows = JzbDataType.getInteger(params.get("rows"));
        int page = JzbDataType.getInteger(params.get("page"));
        params.put("page", JzbDataType.getInteger(page * rows - rows < 0 ? 0 : page * rows - rows));
        params.put("rows", rows);
    }
}
