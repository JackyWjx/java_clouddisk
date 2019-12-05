package com.jzb.org.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProjectTypeMapper {
    List<Map<String, Object>> queryProjectType(Map<String, Object> param);

    void addProjectType(Map<String, Object> param);

    Integer selectMaxNum();

    Integer delProjectType(Map<String, Object> param);

    Integer putProjectType(Map<String, Object> param);

    Integer quertProjectTypeCount(Map<String, Object> param);
}
