package com.jzb.operate.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Description: 积分规则/日志
 * @Author Han Bin
 */
@Mapper
@Repository
public interface TbScoreMapper {

    /**
     *   查询积分规则
     */
    List<Map<String , Object>> qureyScoreRule(Map<String, Object> map);

    /**
     *   查询积分日志
     */
    List<Map<String , Object>> qureyScoreList(Map<String, Object> map);

    /**
     *   查询积分规则总数
     */
    int qureyScoreRuleCount(Map<String, Object> map);

    /**
     *   查询积分日志总数
     */
    int qureyScoreListCount(Map<String, Object> map);

    /**
     *   模糊查询积分规则
     */
    List<Map<String , Object>> seachScoreRule(Map<String, Object> map);

    /**
     *   模糊查询积分日志
     */
    List<Map<String , Object>> seachScoreList(Map<String, Object> map);

    /**
     *   模糊查询积分规则总数
     */
    int seachScoreRuleCount(Map<String, Object> map);

    /**
     *   模糊查询积分日志总数
     */
    int seachScoreListCount(Map<String, Object> map);

    /**
     *   添加积分规则
     */
    int insertScortList(Map<String, Object> map);

    /**
     *   添加积分日志
     */
    int insertScortRule(Map<String, Object> map);

    /**
     *   修改积分规则
     */
    int upScortRule(Map<String, Object> map);

    /**
     *   修改积分日志
     */
    int upScortList(Map<String, Object> map);

    /**
     *   禁用积分规则
     */
    int removeScortRule(Map<String, Object> map);
}
