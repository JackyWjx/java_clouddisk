package com.jzb.org.service;

import com.jzb.org.dao.TbPlantaskJobDutyMapper;
import com.jzb.org.dao.TbPlantaskJobPositionMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class TbPlantaskJobDutyService {

    @Resource
    private TbPlantaskJobDutyMapper tbPlantaskJobDutyMapper;
    @Resource
    private TbPlantaskJobPositionMapper tbPlantaskJobPositionMapper;

    public List<Map<String, Object>> getAllIdByCid(Map<String, Object> param) {
        return tbPlantaskJobDutyMapper.getAllIdByCid(param);
    }

    public List<Map<String, Object>> selectDutyByCid(Map<String, Object> param) {
        return tbPlantaskJobDutyMapper.selectDutyByCid(param);
    }

    public List<Map<String, Object>> selectExistContent(Map<String, Object> param) {
        return tbPlantaskJobDutyMapper.selectExistContent(param);
    }

    public Integer getAllCount(Map<String, Object> param) {
        return tbPlantaskJobDutyMapper.getAllCount(param);
    }

    @Transactional
    public Integer updateJobResponsibilities(Map<String, Object> param) {
        //修改记录
        return tbPlantaskJobDutyMapper.updateJobResponsibilities(param);
    }

    public Integer updateJobResponsibilitiesDelStatus(Map<String, Object> param) {
        return tbPlantaskJobDutyMapper.updateJobResponsibilitiesDelStatus(param);
    }

    public List<Map<String, Object>> getAllJobRBE(Map<String, Object> param) {
        return tbPlantaskJobDutyMapper.AllJobRBE(param);
    }

    public Integer insertJobResponsibilities(Map<String, Object> param) {
        tbPlantaskJobDutyMapper.insertDept(param);
        List<Map<String,Object>> list=(List)param.get("list");
        if(list.size()>0){
            tbPlantaskJobDutyMapper.insertJobRBE(param);
        }
        return tbPlantaskJobDutyMapper.insertJobResponsibilities(param);
    }



    public List<Map<String, Object>> selectExistCrContent(Map<String, Object> param) {
        return tbPlantaskJobDutyMapper.selectExistCrContent(param);
    }


    public List<Map<String, Object>> selectAllDutyByRole(Map<String, Object> param) {
        return tbPlantaskJobDutyMapper.selectAllDutyByRole(param);
    }

    public List<Map<String, Object>> getAllIdByCidNotParam(Map<String, Object> param) {
        return tbPlantaskJobDutyMapper.getAllIdByCidNotParam(param);
    }

    public Integer getAllCountParam(Map<String, Object> param) {
        return tbPlantaskJobDutyMapper.getAllCountParam(param);
    }

    public void insertDictionary(Map<String, Object> dictionary) {
        tbPlantaskJobDutyMapper.insertDictionary(dictionary);
    }
}
