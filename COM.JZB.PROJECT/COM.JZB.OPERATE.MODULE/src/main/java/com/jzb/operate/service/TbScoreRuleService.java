package com.jzb.operate.service;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.util.JzbTools;
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
        if (!JzbTools.isEmpty(param.get("endTime"))){
            param.put("endTime", JzbDataType.getLong(param.get("endTime")) + 86400);
        }
        return tbScoreRuleMapper.getScoreRule(param);
    }

    public int getScoreRuleCount(Map<String, Object> param) {
        return tbScoreRuleMapper.getScoreRuleCount(param);
    }
}
