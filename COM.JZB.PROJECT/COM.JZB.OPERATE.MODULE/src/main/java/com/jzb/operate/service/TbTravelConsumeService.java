package com.jzb.operate.service;

import com.jzb.operate.dao.TbTravelConsumeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TbTravelConsumeService {

    @Autowired
    private TbTravelConsumeMapper tbTravelConsumeMapper;

    /**
     * 添加出差报销记录
     * @param list
     * @return
     */
    public int addTravelConsume(List<Map<String, Object>> list){
        return tbTravelConsumeMapper.addTravelConsume(list);
    }
}
