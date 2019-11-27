package com.jzb.org.service;

import com.jzb.base.data.JzbDataType;
import com.jzb.org.dao.TbTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description: 类型
 * @Author Han Bin
 */
@Service
public class TbTypeService {

    @Autowired
    private TbTypeMapper service;

    /**
     * 查询类型
     * @param param
     * @return
     */
    public List<Map<String , Object>> queryTypeList(Map<String , Object> param){
        int page = JzbDataType.getInteger(param.get("pageno")) == 0 ? 0 : JzbDataType.getInteger(param.get("pageno")) - 1;
        param.put("pageno", page * JzbDataType.getInteger(param.get("pagesize")));
        param.put("pagesize", JzbDataType.getInteger(param.get("pagesize")));
        return service.queryTbTypeList(param);
    }

    /**
     * 查询类型参数
     * @param param
     * @return
     */
    public List<Map<String , Object>> queryTypeInfo(Map<String , Object> param){
        int page = JzbDataType.getInteger(param.get("pageno")) == 0 ? 0 : JzbDataType.getInteger(param.get("pageno")) - 1;
        param.put("pageno", page * JzbDataType.getInteger(param.get("pagesize")));
        param.put("pagesize", JzbDataType.getInteger(param.get("pagesize")));
        return service.queryTbTypeInfo(param);
    }

    /**
     * 查询类型总数
     * @param param
     * @return
     */
    public int queryTypeListCount(Map<String , Object> param){
        return service.queryTbTypeListCount(param);
    }

    /**
     * 查询类型参数总数
     * @param param
     * @return
     */
    public int queryTypeInfoCount(Map<String , Object> param){
        return service.queryTbTypeInfoCount(param);
    }


}
