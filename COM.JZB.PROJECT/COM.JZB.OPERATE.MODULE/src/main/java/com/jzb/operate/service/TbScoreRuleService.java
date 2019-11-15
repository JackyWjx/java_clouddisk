package com.jzb.operate.service;

import com.jzb.operate.dao.TbScoreRuleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TbScoreRuleService {

    @Autowired
    private TbScoreRuleMapper tbScoreRuleMapper;
    /**
     * 充值得分记录的查询
     * @param param
     * @return
     */
    public List<Map<String, Object>> getScoreRule(Map<String, Object> param) {
        return tbScoreRuleMapper.getScoreRule(param);
    }

    public int getScoreRuleCount(Map<String, Object> param) {
        return tbScoreRuleMapper.getScoreRuleCount(param);
    }
}
