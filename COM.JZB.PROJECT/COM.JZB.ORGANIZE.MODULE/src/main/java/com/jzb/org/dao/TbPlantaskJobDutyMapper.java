package com.jzb.org.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TbPlantaskJobDutyMapper {

    public List<Map<String,Object>> getAllIdByCid (Map<String,Object> param);

    public List<Map<String,Object>> selectDutyByCid (Map<String,Object> param);

    public List<Map<String,Object>> selectExistContent (Map<String,Object> param);

    Integer getAllCount(Map<String, Object> param);

    Integer updateJobResponsibilities(Map<String, Object> param);

    Integer updateJobResponsibilitiesDelStatus(Map<String, Object> param);

    void updateJobResponsibilitiesByUidAndTime(Map<String, Object> param);

    List<Map<String, Object>> AllJobRBE(Map<String, Object> param);

    Integer insertJobResponsibilities(Map<String, Object> param);

    Integer insertJobRBE(Map<String, Object> param);

    Integer insertDept(Map<String, Object> param);

    List<Map<String, Object>> selectExistCrContent(Map<String, Object> param);

    List<Map<String, Object>> selectAllDutyByRole(Map<String, Object> param);
}
