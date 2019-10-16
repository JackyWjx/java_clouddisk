package com.jzb.operate.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Description: 积分用户积分/月报
 * @Author Han Bin
 */
@Mapper
@Repository
public interface TbUserScoreMapper {

    /**
     *   查询积分
     */
    List<Map<String , Object>> qureyUserScore(Map<String, Object> map);

    /**
     *   查询积分月报
     */
    List<Map<String , Object>> qureyUserMonthScore(Map<String, Object> map);

    /**
     *   查询积分总数
     */
    int qureyUserScoreCount(Map<String, Object> map);

    /**
     *   查询积分月报总数
     */
    int qureyUserMonthScoreCount(Map<String, Object> map);

    /**
     *   模糊查询积分
     */
    List<Map<String , Object>> seachUserScore(Map<String, Object> map);

    /**
     *   模糊查询积分月报
     */
    List<Map<String , Object>> seachUserMonthScore(Map<String, Object> map);

    /**
     *   模糊查询积分总数
     */
    int seachUserScoreCount(Map<String, Object> map);

    /**
     *   模糊查询积分变更总数
     */
    int seachUserMonthScoreCount(Map<String, Object> map);

    /**
     *   添加积分
     */
    int insertUserScore(Map<String, Object> map);

    /**
     *   添加积分月报
     */
    int insertUserMonthScoreRule(Map<String, Object> map);

    /**
     *   修改积分
     */
    int upUserScore(Map<String, Object> map);

    /**
     *   修改积分月报
     */
    int upUserMonthScoreRule(Map<String, Object> map);

    /**
     *   禁用积分
     */
    int removeUserScore(Map<String, Object> map);

}
