package com.jzb.operate.service;

import com.jzb.operate.dao.TbUserTravelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TbUserTravelService {

    @Autowired
    private TbUserTravelMapper tbUserTravelMapper;

    /**
     * 添加用户出差记录
     * @param list
     * @return
     */
    public int addUserTravel(List<Map<String, Object>> list){
        return tbUserTravelMapper.addUserTavel(list);
    }
}
