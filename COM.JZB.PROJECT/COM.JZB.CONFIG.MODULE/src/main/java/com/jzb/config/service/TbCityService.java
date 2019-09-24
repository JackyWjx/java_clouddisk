package com.jzb.config.service;

import com.jzb.config.dao.TbCityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TbCityService {

    @Autowired
    private TbCityMapper tbCityMapper;

    /**
     * 添加城市
     * @param list
     * @return
     */
    public int addCityList(List<Map<String, Object>> list){
       return tbCityMapper.addCityList(list);
    }

    /**
     * 查询城市
     * @param param
     * @return
     */
    public List<Map<String, Object>> getCityList(Map<String, Object> param){
        return tbCityMapper.getCityList(param);
    }
}
