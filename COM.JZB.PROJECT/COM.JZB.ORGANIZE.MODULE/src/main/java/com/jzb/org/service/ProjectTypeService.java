package com.jzb.org.service;

import com.jzb.org.dao.ProjectTypeMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class ProjectTypeService {

    @Resource
    private ProjectTypeMapper projectTypeMapper;

    public List<Map<String, Object>> queryProjectType(Map<String, Object> param) {
        return projectTypeMapper.queryProjectType(param);
    }

    public void addProjectType(Map<String, Object> param) {
        projectTypeMapper.addProjectType(param);
    }

    public Integer selectMaxNum() {
        return  projectTypeMapper.selectMaxNum();
    }

    public Integer delProjectType(String typeId) {
        return projectTypeMapper.delProjectType(typeId);
    }

    public Integer putProjectType(Map<String, Object> param) {
        return projectTypeMapper.putProjectType(param);
    }

    public Integer quertProjectTypeCount(Map<String, Object> param) {
        return projectTypeMapper.quertProjectTypeCount(param);
    }
}
