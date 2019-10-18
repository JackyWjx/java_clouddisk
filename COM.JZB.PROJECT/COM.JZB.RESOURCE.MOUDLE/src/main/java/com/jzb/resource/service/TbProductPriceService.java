package com.jzb.resource.service;

import com.jzb.base.message.Response;
import com.jzb.resource.dao.TbProductPriceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TbProductPriceService {

    @Autowired
    private TbProductPriceMapper tbProductPriceMapper;


    /**
     * 查询产品报价的总数
     * @param param
     * @return
     */
    public int getTbProductPriceCount(Map<String, Object> param) {
        return tbProductPriceMapper.getTbProductPriceCount(param);
    }

    /**
     * 根据产品线的plId查询产品表
     * @param param
     * @return
     */
    public List<Map<String, Object>> getProductPrice(Map<String, Object> param) {
         return tbProductPriceMapper.getProductPrice(param);
    }

    /**
     * 添加产品报价的数据
     * @param paramList
     * @return
     */
    public int saveProductPrice(List<Map<String, Object>> paramList) {
        return tbProductPriceMapper.saveProductPrice(paramList);
    }

    /**
     * 修改产品报价的数据
     * @param paramList
     * @return
     */
    public int updateProductPrice(List<Map<String, Object>> paramList) {
        return tbProductPriceMapper.updateProductPrice(paramList);
    }

    /**
     * 点击修改查询产品价格返回给前端
     * @param param
     * @return
     */
    public List<Map<String, Object>> getTbProductPrice(Map<String, Object> param) {
        return tbProductPriceMapper.getTbProductPrice(param);
    }

    /**
     * 查询分页的总数
     * @param param
     * @return
     */
    public int getCount(Map<String, Object> param) {
        return tbProductPriceMapper.getCount(param);
    }
}
