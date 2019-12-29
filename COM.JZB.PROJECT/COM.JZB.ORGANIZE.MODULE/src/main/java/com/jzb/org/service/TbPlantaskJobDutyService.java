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
        tbPlantaskJobDutyMapper.updateJobResponsibilitiesByUidAndTime(param);
      /*  List<Map<String,Object>> lists= (List<Map<String, Object>>) param.get("lists");
        List<Map<String,Object>> uniqueids = new ArrayList<>();
        for (int i=0,j=lists.size();i<j;i++){
            Map <String,Object> map = new HashMap<>();
            if(!"dutyid".equals(lists.get(i).get("dutyid"))&&lists.get(i).get("dutyid")!=null){
                map.put("id",lists.get(i).get("dutyid"));
                map.put("content",lists.get(i).get("dutycontent"));
            }
            if(!"workid".equals(lists.get(i).get("workid"))&&lists.get(i).get("workid")!=null){
                map.put("id",lists.get(i).get("workid"));
                map.put("content",lists.get(i).get("workcontent"));
            }
            if(!"outputid".equals(lists.get(i).get("outputid"))&&lists.get(i).get("outputid")!=null){
                map.put("id",lists.get(i).get("outputid"));
                map.put("content",lists.get(i).get("outputcontent"));
            }
            if(!"workstandardid".equals(lists.get(i).get("workstandardid"))&&lists.get(i).get("workstandardid")!=null){
                map.put("id",lists.get(i).get("workstandardid"));
                map.put("content",lists.get(i).get("workstandarcontent"));
            }
            if(!"kpiid".equals(lists.get(i).get("kpiid"))&&lists.get(i).get("kpiid")!=null){
                map.put("id",lists.get(i).get("kpiid"));
                map.put("content",lists.get(i).get("kpicontent"));
            }
            param.put("uniqueids",map);
        }*/
        tbPlantaskJobPositionMapper.updateRoleName(param);
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
}
