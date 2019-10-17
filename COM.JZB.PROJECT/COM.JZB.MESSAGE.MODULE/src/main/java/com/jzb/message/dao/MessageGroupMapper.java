package com.jzb.message.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Description: 企业消息DB
 * @Author Han Bin
 */
@Mapper
@Repository
public interface MessageGroupMapper {

    /**
     * 查询企业消息组
     */
    List<Map<String , Object>> queryMessageGroup(Map<String, Object> map);

    /**
     * 查询企业消息组用户
     */
    List<Map<String , Object>> queryMessageUserGroup(Map<String, Object> map);

    /**
     * 查询企业消息组配置
     */
    List<Map<String , Object>> queryMsgGroupConfigure(Map<String, Object> map);

    /**
     * 查询企业消息组总数
     */
    int queryMessageGroupCount(Map<String, Object> map);

    /**
     * 查询企业消息组用户总数
     */
    int queryMessageUserGroupCount(Map<String, Object> map);

    /**
     * 查询企业消息组配置总数
     */
    int queryMsgGroupConfigureCount(Map<String, Object> map);

    /**
     * 模糊查询企业消息组
     */
    List<Map<String , Object>> searchMessageGroup(Map<String, Object> map);

    /**
     * 模糊查询企业消息组用户
     */
    List<Map<String , Object>> searchMessageUserGroup(Map<String, Object> map);

    /**
     * 模糊查询企业消息组配置
     */
    List<Map<String , Object>> searchMsgGroupConfigure(Map<String, Object> map);

    /**
     * 模糊查询企业消息组总数
     */
    int searchMessageGroupCount(Map<String, Object> map);

    /**
     * 模糊查询企业消息组用户总数
     */
    int searchMessageUserGroupCount(Map<String, Object> map);

    /**
     * 模糊查询企业消息组配置总数
     */
    int searchMsgGroupConfigureCount(Map<String, Object> map);

    /**
     * 添加企业消息组
     */
    int insertMessageGroup(Map<String, Object> map);

    /**
     * 添加企业消息组用户
     */
    int insertMessageUserGroup(Map<String, Object> map);

    /**
     * 添加企业消息组配置
     */
    int insertMsgGroupConfigure(Map<String, Object> map);

    /**
     * 修改企业消息组
     */
    int updateMessageGroup(Map<String, Object> map);

    /**
     * 修改企业消息组用户
     */
    int updateMessageUserGroup(Map<String, Object> map);

    /**
     * 修改企业消息组配置
     */
    int updateMsgGroupConfigure(Map<String, Object> map);

    /**
     * 禁用企业消息组
     */
    int deleteMessageGroup(Map<String, Object> map);

    /**
     * 禁用企业消息组用户
     */
    int deleteMessageUserGroup(Map<String, Object> map);

    /**
     * 禁用企业消息组配置
     */
    int deleteMsgGroupConfigure(Map<String, Object> map);

}

