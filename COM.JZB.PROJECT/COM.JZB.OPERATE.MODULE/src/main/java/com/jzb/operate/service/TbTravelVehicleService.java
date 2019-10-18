package com.jzb.operate.service;

import com.jzb.operate.dao.TbTravelVehicleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TbTravelVehicleService {

    @Autowired
    private TbTravelVehicleMapper tbTravelVehicleMapper;

    /**
     * 获取交通工具
     * @param param
     * @return
     */
    public String getTravelName(Map<String,Object> param){
        return tbTravelVehicleMapper.queryCnameById(param);
    }
}
