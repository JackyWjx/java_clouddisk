package com.jzb.operate.service;

import com.jzb.operate.dao.TbResourceViewMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TbResourceViewService {

    @Autowired
    private TbResourceViewMapper tbResourceViewMapper;

    /**
     * 1.添加浏览
     * @param param
     * @return
     */
    public int addResourceView(Map<String ,Object> param){
        return tbResourceViewMapper.addResourceView(param);
    }

    /**
     * 查询浏览总数以及是否已经浏览
     * @param param
     * @return
     */
    public Map<String ,Integer> queryResourceView(Map<String ,Object> param){
        return tbResourceViewMapper.queryResourceView(param);
    }


    /**
     * 查询是否存在
     * @param param
     * @return
     */
    public int queryIsAlreadyView(Map<String ,Object> param){
        return tbResourceViewMapper.queryIsAlreadyView(param);
    }
}
