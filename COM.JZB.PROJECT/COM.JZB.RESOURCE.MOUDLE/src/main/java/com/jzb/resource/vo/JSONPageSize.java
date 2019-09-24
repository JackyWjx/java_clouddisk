package com.jzb.resource.vo;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;

import java.util.List;
import java.util.Map;

/**
 * @Description: 创建PageInfo返回接口
 * @Author duanfeiyu
 * @Version v1.0
 * @Since 1.0
 * @Date: 2019/8/17 10:03
 */
public class JSONPageSize {

    /**
     * 分页封装接收
     * @param count 总数
     * @param records 每页有多少条数
     * @param response 用json存储
     */
    public  static  void setPageInfo(int count, List<Map<String,Object>> records, Response response){
        PageInfo pageInfo = new PageInfo();
        pageInfo.setList(records);
        pageInfo.setTotal(count);
        response.setPageInfo(pageInfo);
    }
    /**
     * 接收list数据
     * @param list
     * @param response
     */
    public  static void  setPageSize(List<Map<String,Object>> list, Response response){
        PageInfo pageInfo = new PageInfo();
        pageInfo.setList(list);
        response.setPageInfo(pageInfo);
    }

    /**
     * 封装分页查询
     * @param param map容器
     */
    public static void setPageSize(Map<String,Object> param){
        int pageNo = JzbDataType.getInteger(param.get("pageno"));
        int pageSize = JzbDataType.getInteger(param.get("pagesize"));
        pageNo = pageNo <= 0 ?   1 : pageNo;
        param.put("start", (pageNo <= 1) ? 0 : (pageNo - 1) * pageSize);
    } // End setPageSize


    /**
     * 封装分页查询
     * @param param map容器
     * @return Map<String,Object> 返回kv
     */
    public static Map<String,Object> getPageSzie(Map<String,Object> param){
        int pageno = JzbDataType.getInteger(param.get("pageno"));
        int pagesize = JzbDataType.getInteger(param.get("pagesize"));
        pageno = pageno <= 0 ? 1 : pageno;
        pagesize = pagesize <= 0 ? 10 : pagesize;
        param.put("pageno", (pageno - 1) * pagesize);
        param.put("pagesize",pagesize);
        return param;
    }
}
