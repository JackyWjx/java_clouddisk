package com.jzb.org.service;

import com.jzb.org.dao.TenderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TenderService {

    @Autowired
    private TenderMapper tenderMapper;

    /**
     * 获取中标list
     * @param param
     * @return
     */
    public List<Map<String ,Object>> getTenderList(Map<String ,Object> param){
        return tenderMapper.queryTender(param);
    }

    /**
     * 查询总数
     * @return
     */
    public int getTenderCount(Map<String, Object> param){
        return tenderMapper.quertTenderCount(param);
    }
}
