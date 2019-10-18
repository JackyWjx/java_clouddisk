package com.jzb.message.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Description: 消息模版DB
 * @Author
 */
@Mapper
@Repository
public interface MessageGroupTemplateMapper {
    /**
     * 查询
     */
    List<Map<String, Object>> queryMsgGroupTemplate(Map<String, Object> map);

    /**
     * 查询总数
     */
    int queryMsgGroupTemplateCount(Map<String, Object> map);

    /**
     * 模糊查询
     */
    List<Map<String, Object>> searchMsgGroupTemplate(Map<String, Object> map);

    /**
     * 模糊查询总数
     */
    int searchMsgGroupTemplateCount(Map<String, Object> map);

    /**
     * 添加
     */
    int insertMsgGroupTemplate(Map<String, Object> map);

    /**
     * 修改
     */
    int updateMsgGroupTemplate(Map<String, Object> map);

    /**
     * 禁用
     */
    int removeMsgGroupTemplate(Map<String, Object> map);
}
