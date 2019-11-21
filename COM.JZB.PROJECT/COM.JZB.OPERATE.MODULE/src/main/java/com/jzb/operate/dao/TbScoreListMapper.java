package com.jzb.operate.dao;

import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * @描述
 * @创建人 chenhui
 * @创建时间 2019/11/21
 * @修改人和其它信息
 */
@Repository
public interface TbScoreListMapper {
    // 查询该操作类型最近一次的更新时间
    Map<String,Object> getTimeByOptid(Map<String, Object> param);

    // 新建积分日志数据
    int addScoreList(Map<String, Object> param);

    // 查询当日已发表数量
    int getAmount(Map<String, Object> param);
}
