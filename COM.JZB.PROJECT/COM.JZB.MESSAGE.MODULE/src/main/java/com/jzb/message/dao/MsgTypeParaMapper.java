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
     * 查询
     */
    List<Map<String , Object>>  queryMsgTypePara(Map<String , Object> map);

    /**
     * 查询总数
     */
    int  queryMsgTypeParaCount(Map<String , Object> map);

    /**
     * 模糊查询
     */
    List<Map<String , Object>>  searchMsgTypePara(Map<String , Object> map);

    /**
     * 模糊查询总数
     */
    int  searchMsgTypeParaCount(Map<String , Object> map);

    /**
     * 添加
     */
    int insertMsgTypePara(Map<String , Object> map);

    /**
     * 修改
     */
    int updateMsgTypePara(Map<String , Object> map);

    /**
     * 禁用
     */
    int deleteMsgTypePara(Map<String , Object> map);

}
