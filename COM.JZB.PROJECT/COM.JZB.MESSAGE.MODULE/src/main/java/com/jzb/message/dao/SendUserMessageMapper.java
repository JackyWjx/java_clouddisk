package com.jzb.message.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Description: 消息发送详情DB
 * @Author
 */
@Mapper
@Repository
public interface SendUserMessageMapper {

    /**
     * 查询
     */
    List<Map<String, Object>> querySendUserMessage(Map<String, Object> map);

    /**
     * 查询总数
     */
    int querySendUserMessageCount(Map<String, Object> map);

    /**
     * 模糊查询
     */
    List<Map<String, Object>> searchSendUserMessage(Map<String, Object> map);

    /**
     * 模糊查询总数
     */
    int searchSendUserMessageCount(Map<String, Object> map);

    /**
     * 添加
     */
    int insertSendUserMessage(Map<String, Object> map);

    /**
     * 修改
     */
    int updateSendUserMessage(Map<String, Object> map);

    /**
     * 禁用
     */
    int deleteSendUserMessage(Map<String, Object> map);
}
