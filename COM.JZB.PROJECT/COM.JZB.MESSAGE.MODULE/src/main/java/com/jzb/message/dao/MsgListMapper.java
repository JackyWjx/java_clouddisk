package com.jzb.message.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Description: 信息平台DB
 * @Author Han Bin
 */
@Mapper
@Repository
public interface MsgListMapper {

    /**
     * 获取消息组
     */
    List<Map<String , Object>>  queryMsgUserGroup(@Param("groupid") String groupid);

    /**
     * 获取消息组
     */
    Map<String  , Object> queryMsgUserGroupTemplate(@Param("groupid") String groupid);

    /**
     * 获取配置
     */
    List<Map<String , Object>>  queryMsgGroupConfigure(@Param("groupid") String groupid);

    /**
     * 根据appId 获取checkcode
     */
    String queryMsgOrganizeCheckcode(String appid);

    /**
     * 添加一条用户消息
     */
    int insertMsgList(Map<String ,  Object> map);

    /**
     * 添加发送详情记录
     */
    int insertSendUserMessage(Map<String ,  Object> map);

    /**
     * 添加消息模板
     */
    int insertMsgUserTemplate(Map<String ,  Object> map);

    /**
     * 修改成已发送
     */
    int updateMessageListSendStatusByMegid(String msgid);

}
