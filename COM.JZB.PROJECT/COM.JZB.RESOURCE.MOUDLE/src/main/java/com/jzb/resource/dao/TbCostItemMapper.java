package com.jzb.resource.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface TbCostItemMapper {

    /*
     * 1.根据模板列表查询费用条算
     * */
    List<Map<String, Object>> queryCostItem(Map<String, Object> param);

    /*
     * 2.添加费用条算
     * */
    int saveCostItem(Map<String, Object> param);

    /*
     * 3.修改费用条算
     * */
    int updateCostItem(Map<String, Object> param);

    /*
     * 4.修改费用条算状态
     * */
    int updateCostItemStatus(Map<String, Object> param);

    /*
     * 5.获取排序
     * */
    int getCostItemIdx();

    /*
     * 6.获取总数
     * */
    int getCostItemCount(Map<String, Object> param);
}
