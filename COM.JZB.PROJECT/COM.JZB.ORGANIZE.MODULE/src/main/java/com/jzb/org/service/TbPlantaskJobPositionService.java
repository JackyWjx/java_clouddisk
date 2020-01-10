package com.jzb.org.service;

import com.jzb.org.dao.TbPlantaskJobPositionMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class TbPlantaskJobPositionService {

    @Resource
    private TbPlantaskJobPositionMapper tbPlantaskJobPositionMapper;

    public List<Map<String,Object>> getRoles(Map<String,Object> param){
        return tbPlantaskJobPositionMapper.selectRole(param);
    }

    public List<Map<String,Object>> getDepts(Map<String,Object> param){
        return tbPlantaskJobPositionMapper.selectDept(param);
    }

    public Integer addRoleAndDept (Map<String,Object> param){
        return tbPlantaskJobPositionMapper.insertRoleAndDept(param);
    }
    public List<Map<String,Object>> selectRoleByDeptid (Map<String,Object> param){
        return tbPlantaskJobPositionMapper.selectRoleByDeptid(param);
    }

    public List<Map<String, Object>> selectRoleByDeptids(Map<String, Object> param) {
        return tbPlantaskJobPositionMapper.selectRoleByDeptids(param);
    }


    public Map<String, Object> selecttContent(Map<String, Object> param) {
        return tbPlantaskJobPositionMapper.selecttContent(param);
    }
}
