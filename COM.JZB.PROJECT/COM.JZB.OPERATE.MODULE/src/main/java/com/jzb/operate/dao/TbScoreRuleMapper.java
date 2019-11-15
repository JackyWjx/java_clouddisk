package com.jzb.operate.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface TbScoreRuleMapper {
    /**
     * 充值得分记录的查询
     * @param param
     * @return
     */
    List<Map<String, Object>> getScoreRule(Map<String, Object> param);

    /**
     * 查询日志积分的总数
     * @param param
     * @return
     */
    int getScoreRuleCount(Map<String, Object> param);
}
