package com.jzb.resource.dao;

import com.jzb.base.message.Response;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface TbProductPriceMapper {

    /**
     * 查询产品报价的 总数
     *
     * @param param
     * @return
     */
    int getTbProductPriceCount(Map<String, Object> param);


    /**
     * 根据产品线plId查询产品表
     *
     * @param param
     * @return
     */
    List<Map<String, Object>> getProductPrice(Map<String, Object> param);

    /**
     * 新增产品价格报价
     *
     * @param paramList
     * @return
     */
    int saveProductPrice(List<Map<String, Object>> paramList);

    /**
     * 修改产品价格报价
     *
     * @param paramList
     * @return
     */
    int updateProductPrice(List<Map<String, Object>> paramList);


    /**
     * 点击修改查询产品价格返回给前端
     * @param param
     * @return
     */
    List<Map<String, Object>> getTbProductPrice(Map<String, Object> param);

    /**
     * 查询分页的总数
     * @param param
     * @return
     */
    int getCount(Map<String, Object> param);
}
