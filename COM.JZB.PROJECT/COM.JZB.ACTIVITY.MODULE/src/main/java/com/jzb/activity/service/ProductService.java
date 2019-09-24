package com.jzb.activity.service;

import com.jzb.activity.dao.ProductMapper;
import com.jzb.activity.vo.JsonPageInfo;
import com.jzb.base.data.JzbDataType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description: 产品实现类
 * @Author duanfeiyu
 * @Version v1.0
 * @Since 1.0
 * @Date: 2019/8/13 9:46
 */
@Service
public class ProductService {
    @Autowired
    private ProductMapper productMapper;

    /**
     * 分页查询产品信息
     *
     * @param param 用 kv 存储
     * @return List<Map < String, Object>> 返回数据
     */
    public List<Map<String, Object>> getProductList(Map<String, Object> param) {
        // 调用分页查询方法
        JsonPageInfo.setPageSize(param);

        // 查询数据
        return productMapper.queryProductList(param);
    } // End getProductList

    /**
     * 分页查询产品信息
     * @param param 用 kv 存储
     * @return List<Map < String, Object>> 返回数据
     */
    public int getProductTotal(Map<String, Object> param) {
        return productMapper.queryProductTotal(param);
    } // End getProductTotal

    /**
     *查询产品总数
     * @return
     */
    public int selectProductCount() {
        return productMapper.selectProductCount();
    }

    /**
     * 查询产品包总数
     * @return
     */
    public int selectProductPackageCount() {
        return productMapper.selectProductPackageCount();
    }

    /**
     * 查询产品包总数
     * @return
     */
    public int likeProductPackageCount() {
        return productMapper.likeProductPackageCount();
    }

    /**
     * 模糊查询产品包总数
     * @return
     */
    public int likeProductList() {
        return productMapper.likeProductPackageCount();
    }
}


