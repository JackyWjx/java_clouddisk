package com.jzb.org.service;

import com.jzb.org.dao.TenderDescMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TenderDescService {
    @Autowired
    private TenderDescMapper tenderDescMapper;

    /**
     * 查询招标详情
     * @param param
     * @return
     */
    public Map<String, Object> getTenderDesc(Map<String, Object> param){
        return tenderDescMapper.queryTenderDesc(param);
    }
}
