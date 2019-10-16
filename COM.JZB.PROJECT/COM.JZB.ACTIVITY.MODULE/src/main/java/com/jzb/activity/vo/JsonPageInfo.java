package com.jzb.activity.vo;

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
public class JsonPageInfo {

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
     * 封装分页查询
     * @param param map容器
     */
    public static void setPageSize(Map<String,Object> param){
        int pageNo = JzbDataType.getInteger(param.get("pageno"));
        int pageSize = JzbDataType.getInteger(param.get("pagesize"));
        pageNo = pageNo <= 0 ? 1 : pageNo;
        param.put("start", (pageNo <= 1) ? 0 : (pageNo - 1) * pageSize);
    } // End setPageSize

    /**
     * 返回结果集
     * @param records 每页有多少条数
     * @param response 用json存储
     */
    public  static  void setPageInfoList(List<Map<String,Object>> records,Response response){
        PageInfo pageInfo = new PageInfo();
        pageInfo.setList(records);
        response.setPageInfo(pageInfo);
    }

    /**
     * Map<String,Object>用pageInfo方法接收
     * @param records list集合
     * @param response json数据
     */
    public static void  getArrayList(Map<String,Object> records,Response response)
    {
        //添加List集合
        List<Map<String, Object>> mapList = new ArrayList<>();
        if (records.size() >= 0 && records != null ) {
            //把map添加在集合里
            mapList.add(records);
            JsonPageInfo.setPageInfoList(mapList, response);
        }
    }



    /**
     * List<Map<String,Object>>用pageInfo方法接收
     * @param records list集合
     * @param response json数据
     */
    public static void  getArrayList(List<Map<String,Object>> records,Response response)
    {
        //添加List集合
        List<Map<String, Object>> mapList = new ArrayList<>();
        if (records.size() > 0 && records != null ) {
            //把map添加在集合里
            mapList.addAll(records);
            JsonPageInfo.setPageInfoList(mapList, response);
        }
    }



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
