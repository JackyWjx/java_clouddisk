package com.jzb.operate.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Description: 系统日志
 * @Author Han Bin
 */
@Mapper
@Repository
public interface TbVersionLogMapper {

    /**
     *  查询
     */
    List<Map<String , Object>> queryVersionLog(Map<String , Object> map);

    /**
     *  查询总数
     */
    int queryVersionLogCount(Map<String , Object> map);

    /**
     *  模糊查询
     */
    List<Map<String , Object>> searchVersionLog(Map<String , Object> map);

    /**
     *  模糊查询总数
     */
    int searchVersionLogCount(Map<String , Object> map);

    /**
     *  添加
     */
    int  insertVersionLog(Map<String , Object> map);

    /**
     *  修改
     */
    int  updateVersionLog(Map<String , Object> map);

    /**
     *  禁用
     */
    int  deleteVersionLog(Map<String , Object> map);
}
