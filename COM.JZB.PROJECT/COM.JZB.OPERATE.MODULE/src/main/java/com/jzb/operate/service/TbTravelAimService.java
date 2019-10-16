package com.jzb.operate.service;

import com.jzb.operate.dao.TbTravelAimMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TbTravelAimService {

    @Autowired
    private TbTravelAimMapper tbTravelAimMapper;

    /**
     * 添加出差目标记录
     * @param param
     * @return
     */
    public int addTravelAim(Map<String, Object> param){
        return tbTravelAimMapper.addTravelAim(param);
    }


}