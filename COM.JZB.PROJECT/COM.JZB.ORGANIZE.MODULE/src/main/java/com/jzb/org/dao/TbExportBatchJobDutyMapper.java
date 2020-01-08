package com.jzb.org.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TbExportBatchJobDutyMapper {

    int insertExportBatch(Map<String, Object> map);

    List<String> selectDeptIdByDeptName(String name);
    int insertExportDutyInfoList(List<Map<String, Object>> dutyInfo);

    void updateExportBatch(Map<String, Object> exportMap);

    Map<String,Object> queryExportBatch(String name);

    List< Map<String,Object>> queryExportList(String name);
}
