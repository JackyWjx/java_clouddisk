package com.jzb.org.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EvaluationMethodMapper {
    public List<Map<String, Object>> queryEvaluationMethod(Map<String, Object> param);

    void addEvaluationMethod(Map<String, Object> param);

    Integer delEvaluationMethod(Map<String, Object> param);

    Integer putEvaluationMethod(Map<String, Object> param);

    Integer quertTenderTypeCount(Map<String, Object> param);
}
