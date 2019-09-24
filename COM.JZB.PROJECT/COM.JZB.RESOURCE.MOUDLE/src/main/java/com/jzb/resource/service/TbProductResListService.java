package com.jzb.resource.service;

import com.jzb.resource.dao.TbProductResListMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TbProductResListService {

    @Autowired
    private TbProductResListMapper tbProductResListMapper;



    /**
     * 查询模板功能信息
     * @param param
     * @return
     */
    public List<Map<String, Object>> queryProductResList(Map<String, Object> param){
        return tbProductResListMapper.queryProductResList(param);
    }

    /**
     * 添加模板功能信息
     * @param param
     * @return
     */
    public int addProductRes(Map<String, Object> param){
        return tbProductResListMapper.addProductResList(param);
    }
}
