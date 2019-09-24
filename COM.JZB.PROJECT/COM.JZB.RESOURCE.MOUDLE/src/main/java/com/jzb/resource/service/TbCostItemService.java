package com.jzb.resource.service;

import com.jzb.base.util.JzbRandom;
import com.jzb.resource.dao.TbCostItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TbCostItemService {

    @Autowired
    private TbCostItemMapper tbCostItemMapper;

    /*
     * 1.根据模板列表查询费用条算
     * */
    public List<Map<String, Object>> queryCostItem(Map<String, Object> param) {
        return tbCostItemMapper.queryCostItem(param);
    }

    /*
     * 2.添加费用条算
     * */
    public int saveCostItem(Map<String, Object> param) {
        Long time=System.currentTimeMillis();
        param.put("addtime",time);
        param.put("updtime",time);
        param.put("itemid", param.get("typeid")+JzbRandom.getRandomCharCap(4));
        param.put("idx", getCostItemIdx());
        return tbCostItemMapper.saveCostItem(param);
    }

    /*
     * 3.修改费用条算
     * */
    public int updateCostItem(Map<String, Object> param) {
        Long time=System.currentTimeMillis();
        param.put("updtime",time);
        return tbCostItemMapper.updateCostItem(param);
    }

    /*
     * 4.修改费用条算状态
     * */
    public int updateCostItemStatus(Map<String, Object> param) {
        return tbCostItemMapper.updateCostItemStatus(param);
    }

    /*
     * 5.获得排序
     * */
    public int getCostItemIdx() {
        return tbCostItemMapper.getCostItemIdx();
    }

    /*
     * 6.获取总数
     * */
    public int getCostItemCount(Map<String, Object> param) {
        return tbCostItemMapper.getCostItemCount(param);
    }
}
