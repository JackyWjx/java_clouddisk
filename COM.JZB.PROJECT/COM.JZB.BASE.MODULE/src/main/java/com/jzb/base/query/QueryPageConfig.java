package com.jzb.base.query;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description: 分页封装插件
 * @Author duanfeiyu
 * @Version v1.0
 * @Since 1.0
 * @Date: 2019/8/8 18:22
 */
public class QueryPageConfig {
    /**
     * 封装分页查询
     * @param param map容器
     */
    public static void setPageSize(Map<String,Object> param){
        int pageNo = JzbDataType.getInteger(param.get("pageno"));
        int pageSize = JzbDataType.getInteger(param.get("pagesize"));
        pageNo = pageNo <= 0 ? 1 : pageNo;
        param.put("start", (pageNo <= 1) ? 0 : (pageNo - 1) * pageSize);
    } // End setPageSize
}
