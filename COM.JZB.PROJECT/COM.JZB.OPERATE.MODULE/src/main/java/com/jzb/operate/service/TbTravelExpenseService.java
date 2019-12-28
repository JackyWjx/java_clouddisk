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

    public int saveTravelExpense(Map<String, Object> param) {
        return travelExpenseMapper.saveTravelExpense(param);
    }

    public int updateTravelExpense(Map<String, Object> param) {
        return travelExpenseMapper.updateTravelExpense(param);
    }

    public List<Map<String, Object>> queryTravelExpenseByid(Map<String, Object> map) {
        return travelExpenseMapper.queryTravelExpenseByid(map);
    }

    public int setExpenseDeleteStatus(Map<String, Object> param) {
        return travelExpenseMapper.setExpenseDeleteStatus(param);
    }
}
