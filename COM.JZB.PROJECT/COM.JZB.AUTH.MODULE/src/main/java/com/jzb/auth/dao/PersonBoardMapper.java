package com.jzb.auth.dao;

import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * @author chenhui
 * @description
 * @time 2019/12/11
 * @other
 */
@Repository
public interface PersonBoardMapper {
    Map<String, Object> getAuthCount(Map<String, Object> param);

    /**
     * 申请加入单位查询加入单位人的姓名
     * @param param
     * @return
     */
    Map<String, Object> getCname(Map<String, Object> param);
}
