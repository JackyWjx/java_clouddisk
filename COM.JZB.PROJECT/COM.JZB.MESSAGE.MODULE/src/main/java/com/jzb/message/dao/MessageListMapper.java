package com.jzb.message.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Description: 消息参数DB
 * @Author
 */
@Mapper
@Repository
public interface MessageListMapper {
    /**
     * 查询
     */
    List<Map<String, Object>> queryMsgList(Map<String, Object> map);

    /**
     * 查询总数
     */
    int queryMsgListCount(Map<String, Object> map);

    /**
     * 模糊查询
     */
    List<Map<String, Object>> searchMsgList(Map<String, Object> map);

    /**
     * 模糊查询总数
     */
    int searchMsgListCount(Map<String, Object> map);

    /**
     * 添加
     */
    int insertMsgList(Map<String, Object> map);

    /**
     * 修改
     */
    int updateMsgList(Map<String, Object> map);

    /**
     * 禁用
     */
    int deleteMsgList(Map<String, Object> map);
}
