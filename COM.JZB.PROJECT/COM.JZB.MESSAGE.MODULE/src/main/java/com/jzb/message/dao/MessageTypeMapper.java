package com.jzb.message.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Description: 用户消息DB
 * @Author
 */
@Mapper
@Repository
public interface MessageTypeMapper {

    /**
     * 查询
     */
    List<Map<String, Object>> queryMsgType(Map<String, Object> map);

    /**
     * 查询总数
     */
    int queryMsgTypeCount(Map<String, Object> map);

    /**
     * 模糊查询
     */
    List<Map<String, Object>> searchMsgType(Map<String, Object> map);

    /**
     * 模糊查询总数
     */
    int searchMsgTypeCount(Map<String, Object> map);

    /**
     * 添加
     */
    int insertMsgType(Map<String, Object> map);

    /**
     * 修改
     */
    int updateMsgType(Map<String, Object> map);

    /**
     * 禁用
     */
    int deleteMsgType(Map<String, Object> map);

}
