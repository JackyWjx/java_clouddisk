package com.jzb.operate.service;

import com.jzb.base.message.Response;
import com.jzb.operate.dao.TravelExpenseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author sapientia
 * @Date 2019/12/5 17:30
 */
@Service
public class TbTravelExpenseService {

    @Autowired
    private TravelExpenseMapper travelExpenseMapper;

    public int saveTravelExpense(Map<String, Object> map) {
        return travelExpenseMapper.saveTravelExpense(map);
    }

    public int updateTravelExpense(List<Map<String, Object>> list) {
        return travelExpenseMapper.updateTravelExpense(list);
    }
}
