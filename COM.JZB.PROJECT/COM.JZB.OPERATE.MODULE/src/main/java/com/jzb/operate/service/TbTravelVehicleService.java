package com.jzb.operate.service;

import com.jzb.operate.dao.TbTravelVehicleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

    /**
     * 查询所有交通工具
     *
     * @return
     */
    public List<Map<String, Object>> queryVehicle(){
        return tbTravelVehicleMapper.queryVehicle();
    }
}
