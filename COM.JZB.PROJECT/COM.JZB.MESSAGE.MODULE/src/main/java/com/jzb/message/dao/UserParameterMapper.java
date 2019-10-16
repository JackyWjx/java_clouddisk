package com.jzb.message.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface UserParameterMapper {

    /**
     * 查询
     */
    List<Map<String, Object>> queryUserParameter(Map<String, Object> map);

    /**
     * 查询总数
     */
    int queryUserParameterCount(Map<String, Object> map);

    /**
     * 模糊查询
     */
    List<Map<String, Object>> searchUserParameter(Map<String, Object> map);

    /**
     * 模糊查询总数
     */
    int searchUserParameterCount(Map<String, Object> map);

    /**
     * 添加
     */
    int insertUserParameter(Map<String, Object> map);

    /**
     * 修改
     */
    int updateUserParameter(Map<String, Object> map);

    /**
     * 禁用
     */
    int deleteUserParameter(Map<String, Object> map);
}
