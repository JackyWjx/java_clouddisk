package com.jzb.org.service;

import com.jzb.org.dao.TenderResultDescMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TenderResultDescService {

    @Autowired
    private TenderResultDescMapper tenderResultDescMapper;

    /**
     * 查询中标详情
     * @param param
     * @return
     */
    public Map<String, Object> getTenderResultDesc(Map<String, Object> param){
        return tenderResultDescMapper.queryTenderResultDesc(param);
    }
}
