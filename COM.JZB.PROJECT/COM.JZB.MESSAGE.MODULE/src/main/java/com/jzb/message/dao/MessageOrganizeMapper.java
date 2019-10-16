package com.jzb.message.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Description: 消息平台入驻DB
 * @Author
 */
@Mapper
@Repository
public interface MessageOrganizeMapper {
    /**
     * 查询
     */
    List<Map<String, Object>> queryMsgOrganize(Map<String, Object> map);

    /**
     * 查询总数
     */
    int queryMsgOrganizeCount(Map<String, Object> map);

    /**
     * 模糊查询
     */
    List<Map<String, Object>> searchMsgOrganize(Map<String, Object> map);

    /**
     * 模糊查询总数
     */
    int searchMsgOrganizeCount(Map<String, Object> map);

    /**
     * 添加
     */
    int insertMsgOrganize(Map<String, Object> map);

    /**
     * 修改
     */
    int updateMsgOrganize(Map<String, Object> map);

    /**
     * 禁用
     */
    int deleteMsgOrganize(Map<String, Object> map);
}
