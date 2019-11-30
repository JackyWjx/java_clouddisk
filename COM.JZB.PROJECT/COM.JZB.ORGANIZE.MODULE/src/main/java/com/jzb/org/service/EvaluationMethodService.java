package com.jzb.org.service;

import com.jzb.org.dao.EvaluationMethodMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class EvaluationMethodService {
    @Autowired
    private EvaluationMethodMapper evaluationMethodMapper;

    public Integer delEvaluationMethod(Integer typeId) {
        return evaluationMethodMapper.delEvaluationMethod(typeId);
    }

    public void addEvaluationMethod(Map<String, Object> param) {
         evaluationMethodMapper.addEvaluationMethod(param);
    }

    public List<Map<String, Object>> queryEvaluationMethod(Map<String, Object> param) {
        return evaluationMethodMapper.queryEvaluationMethod(param);
    }

    public Integer putEvaluationMethod(Map<String, Object> param) {
        return evaluationMethodMapper.putEvaluationMethod(param);
    }
}
