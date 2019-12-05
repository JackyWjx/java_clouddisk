package com.jzb.org.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @描述
 * @创建人 王吉祥
 * @创建时间 2019/11/29
 * @招标的操作
 */
@Mapper
@Repository
public interface TenderAndDescMapper {
    int addTenderMessage(Map<String, Object> param);

    List<Map<String, Object>> queryTenderMessage(Map<String, Object> param);

    List<Map<String, Object>> queryAllTenderAndDescMessage(Map<String, Object> param);

    Integer queryTenderMessageCount(Map<String, Object> param);

    void addTenderInfoMessage(Map<String, Object> param);

    void putTenderMessage(Map<String, Object> param);

    void putTenderInfoMessage(Map<String, Object> param);

    void delTenderMessage(Map<String, Object> param);

    void delTenderInfoMessage(Map<String, Object> param);
}
