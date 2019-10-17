package com.jzb.message.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Description: 消息参数DB
 * @Author Han Bin
 */
@Mapper
@Repository
public interface MsgTypeParaMapper {

    /**
     * 查询消息参数
     */
    List<Map<String , Object>>  queryMsgTypePara(Map<String, Object> map);

    /**
     * 查询用户参数
     */
    List<Map<String , Object>>  queryUserPara(Map<String, Object> map);

    /**
     * 查询用户参数
     */
    List<Map<String , Object>>  queryServiceProviders(Map<String, Object> map);

    /**
     * 查询消息参数总数
     */
    int  queryMsgTypeParaCount(Map<String, Object> map);


    /**
     * 查询用户参数总数
     */
    int  queryUserParaCount(Map<String, Object> map);

    /**
     * 查询服务商总数
     */
    int  queryServiceProvidersCount(Map<String, Object> map);

    /**
     * 模糊查询
     */
    List<Map<String , Object>>  searchMsgTypePara(Map<String, Object> map);

    /**
     * 模糊查询
     */
    List<Map<String , Object>>  searchUserPara(Map<String, Object> map);

    /**
     * 模糊查询
     */
    List<Map<String , Object>>  searchServiceProviders(Map<String, Object> map);

    /**
     * 模糊查询消息参数总数
     */
    int  searchMsgTypeParaCount(Map<String, Object> map);

    /**
     * 模糊查询用户参数总数
     */
    int  searchUserParaCount(Map<String, Object> map);

    /**
     * 模糊查询服务商总数
     */
    int  searchServiceProvidersCount(Map<String, Object> map);

    /**
     * 添加消息参数
     */
    int insertMsgTypePara(Map<String, Object> map);

    /**
     * 添加用户参数
     */
    int insertUserPara(Map<String, Object> map);

    /**
     * 添加服务商
     */
    int insertServiceProviders(Map<String, Object> map);

    /**
     * 修改消息参数
     */
    int updateMsgTypePara(Map<String, Object> map);

    /**
     * 修改用户参数
     */
    int updateUserPara(Map<String, Object> map);

    /**
     * 修改服务商
     */
    int updateServiceProviders(Map<String, Object> map);

    /**
     * 禁用消息参数
     */
    int deleteMsgTypePara(Map<String, Object> map);

    /**
     * 禁用用户参数
     */
    int deleteUserPara(Map<String, Object> map);

    /**
     * 禁用服务商
     */
    int deleteServiceProviders(Map<String, Object> map);

}
