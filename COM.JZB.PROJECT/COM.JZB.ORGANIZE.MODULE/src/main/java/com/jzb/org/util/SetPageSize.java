package com.jzb.org.util;

import com.jzb.base.data.JzbDataType;

import java.util.Map;

public class SetPageSize {

    /**
     * 设置分页数
     */
    public static Map<String, Object> setPagenoSize(Map<String, Object> param) {
        int pageno = JzbDataType.getInteger(param.get("pageno"));
        int pagesize = JzbDataType.getInteger(param.get("pagesize"));
        pagesize = pagesize <= 0 ? 15 : pagesize;
        pageno = pageno <= 0 ? 1 : pageno;
        param.put("pageno", (pageno - 1) * pagesize);
        param.put("pagesize", pagesize);
        return param;
    }
}
