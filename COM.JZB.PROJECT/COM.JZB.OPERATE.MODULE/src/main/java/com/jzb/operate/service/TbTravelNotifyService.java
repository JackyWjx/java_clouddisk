package com.jzb.operate.service;

import com.jzb.operate.dao.TbTravelNotifyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TbTravelNotifyService {

    @Autowired
    private TbTravelNotifyMapper tbTravelNotifyMapper;

    /**
     * 添加抄送记录
     * @param list
     * @return
     */
    public int addTravelNotify(List<Map<String, Object>> list){
        return  tbTravelNotifyMapper.addTravelNotify(list);
    }
}
