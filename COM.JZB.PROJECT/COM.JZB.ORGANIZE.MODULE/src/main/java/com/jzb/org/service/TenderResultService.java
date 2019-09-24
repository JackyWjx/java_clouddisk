package com.jzb.org.service;

import com.jzb.org.dao.TenderResultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TenderResultService {
    @Autowired
    private TenderResultMapper tenderResultMapper;

    /**
     * 获取中标列表
     * @param param
     * @return
     */
    public List<Map<String, Object>> getTenderResultList(Map<String, Object> param){
        return tenderResultMapper.queryTenderResult(param);
    }

    /**
     * 查询总数
     * @return
     */
    public int quertTenderResultCount(Map<String, Object> param){
        return tenderResultMapper.quertTenderResultCount(param);
    }
}
