package com.jzb.logger.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author dell
 * @Version 1.0
 * @Since 1.0
 * @Date: 2019/11/1 13:48
 */
@Mapper
@Repository
public interface ElasticMapper {

    /**
     * 查询
     *
     * @param map
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @Author: DingSC
     */
    List<Map<String, Object>> queryApiTimeLog(Map<String, Object> map);

    /**
     * 批量新增系统请求日志表
     *
     * @param list
     * @return int
     * @Author: DingSC
     */
    int insertRequestApiLog(List<Map<String, Object>> list);

    /**
     * 获取请求日志最大日期的前一天日期
     *
     * @param
     * @return java.lang.String
     * @Author: DingSC
     */
    String queryRequestLog();
}
