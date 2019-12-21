package com.jzb.org.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @描述
 * @创建人 王吉祥
 * @创建时间 2019/11/29
 * @中标操作
 */
@Mapper
@Repository
public interface TenderResultAndDescMapper {
    int addTenderMessage(Map<String, Object> param);

    int addTenderInfoMessage(Map<String, Object> param);

    Integer queryTenderResultMessageCount(Map<String, Object> param);

    List<Map<String, Object>> queryTenderResultMessage(Map<String, Object> param);

    Map<String, Object> getTenderMessageBeforeUpdate(Map<String, Object> param);

    void updateTenderMessage(Map<String, Object> param);

    void updateTenderInfoMessage(Map<String, Object> param);

    void delTenderMessage(Map<String, Object> param);

    void delTenderInfoMessage(Map<String, Object> param);
}
