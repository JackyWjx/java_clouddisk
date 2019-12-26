package com.jzb.resource.service;

import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbRandom;
import com.jzb.resource.dao.TbProductPriceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

        //根据产品id查询出来所有的服务项目
        List<Map<String, Object>> productPrice = null;
        for (int i = 0; i < paramList.size(); i++) {
            if (paramList != null)
             productPrice = tbProductPriceMapper.getTbProductPrice(paramList.get(i));
        }
        //在修改的时候如果新添加了服务，就进行服务的添加
          List list = new ArrayList<String>();
        for (int i = 0; i < paramList.size(); i++) {
            list.add(paramList.get(i).get("itemid"));
        }

        int count = 0;
        //循环判断前端传过来删除了的服务项目
        for (int i = 0; i < productPrice.size(); i++) {
            if (!list.contains(productPrice.get(i).get("itemid"))) {
                long time = System.currentTimeMillis();
                productPrice.get(i).put("updtime", time);
                productPrice.get(i).put("status", "2");
               count = tbProductPriceMapper.deleteProductPrice(productPrice.get(i));
            } else {
                long time = System.currentTimeMillis();
                productPrice.get(i).put("updtime", time);
                if (paramList != null) {
                    count = tbProductPriceMapper.deleteProductPrice(paramList.get(i));
                }
            }
        }

        return count;
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

    /**修改时新增数据的添加
     *
     * @param param
     * @return
     */
    public int addProductPrice(Map<String, Object> param) {
        return tbProductPriceMapper.addProductPrice(param);
    }


    public int deleteProductPrice(Map<String, Object> param) {
       return tbProductPriceMapper.deleteProductPrice(param);
    }

    /**
     * 如果为空全部删除
     * @param param
     * @return
     */
    public int updateProductPrices(Map<String,Object> param) {
       return tbProductPriceMapper.updateProductPrices(param);
    }

    /**
     * 添加服务类型
     * @param param
     * @return
     */
    public int addPriceService(Map<String, Object> param) {
        long time = System.currentTimeMillis();
        param.put("addtime", time);
        param.put("updtime", time);
        return tbProductPriceMapper.addPriceService(param);
    }

    /**
     * 修改服务类型
     * @param param
     * @return
     */
    public int updatePriceService(Map<String, Object> param) {
        long time = System.currentTimeMillis();
        param.put("updtime", time);
        return tbProductPriceMapper.updatePriceService(param);
    }
}
