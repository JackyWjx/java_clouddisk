package com.jzb.resource.service;

import com.jzb.resource.dao.TbProductFunctionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TbProductFunctionService {

    @Autowired
    private TbProductFunctionMapper tbProductFunctionMapper;

    /**
     * 查询产品功能表对应的资源产品
     * @param param
     * @return
     */
    public List<Map<String, Object>> getTbProductFunction(Map<String, Object> param) {
        return tbProductFunctionMapper.getTbProductFunction(param);
    }

    /**
     * 添加产品功能
     * @param paramList
     * @return
     */
    public int saveTbProductFunction(List<Map<String, Object>> paramList) {
        return tbProductFunctionMapper.saveTbProductFunction(paramList);
    }

    /**
     * 添加产品功能
     * @param paramList
     * @return
     */
    public int updateTbProductFunction(List<Map<String, Object>> paramList) {
        return tbProductFunctionMapper.updateTbProductFunction(paramList);
    }

    /**
     * 点击修改时查询产品功能表中的数据
     * @param param
     * @return
     */
    public List<Map<String, Object>> getProductFunction(Map<String, Object> param) {
        return tbProductFunctionMapper.getProductFunction(param);
    }

    /**
     * 查询分页的总数
     * @param param
     * @return
     */
    public int getCount(Map<String, Object> param) {
        return tbProductFunctionMapper.getCount(param);
    }
}
