package com.jzb.org.service;

import com.jzb.org.dao.TbExportBatchJobDutyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TbExportBatchJobDutyService {
    @Autowired
    private TbExportBatchJobDutyMapper tbExportBatchJobDutyMapper;


    /**
     * 保存用户导入批次表
     *
     * @param map
     * @return
     */
    public int addExportBatch(Map<String, Object> map) {
        map.put("addtime", System.currentTimeMillis());
        return tbExportBatchJobDutyMapper.insertExportBatch(map);
    }

    public List<String> selectDeptIdByDeptName(String name){
        return tbExportBatchJobDutyMapper.selectDeptIdByDeptName(name);
    }

    public int insertExportDutyInfoList(List<Map<String, Object>> dutyInfo) {
        return tbExportBatchJobDutyMapper.insertExportDutyInfoList(dutyInfo);
    }

    public void updateExportBatch(Map<String, Object> exportMap) {
        exportMap.put("endtime",System.currentTimeMillis());
        tbExportBatchJobDutyMapper.updateExportBatch(exportMap);
    }
    public Map<String, Object> queryExportBatch(String exportId) {
        return tbExportBatchJobDutyMapper.queryExportBatch(exportId);
    }
    public List<Map<String, Object>> queryExportList(String exportId) {
        return tbExportBatchJobDutyMapper.queryExportList(exportId);
    }
}
