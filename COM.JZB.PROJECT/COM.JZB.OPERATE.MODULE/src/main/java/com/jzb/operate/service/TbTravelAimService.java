package com.jzb.operate.service;

import com.jzb.operate.dao.TbTravelAimMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TbTravelAimService {

    @Autowired
    private TbTravelAimMapper tbTravelAimMapper;

    /**
     * 添加出差目标记录
     * @param list
     * @return
     */
    public int addTravelAim(List<Map<String, Object>> list){
        return tbTravelAimMapper.addTravelAim(list);
    }


    /**
     *查询
     * @param param
     * @return
     */
    public List<Map<String, Object>>queryTravelAim(Map<String, Object> param){
       return tbTravelAimMapper.queryTravelAim(param);
    }

}