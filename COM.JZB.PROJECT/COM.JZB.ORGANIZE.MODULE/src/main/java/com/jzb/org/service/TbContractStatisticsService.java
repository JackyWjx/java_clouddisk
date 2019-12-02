package com.jzb.org.service;

import com.jzb.org.dao.TbContractStatisticsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TbContractStatisticsService {

    @Autowired
    private TbContractStatisticsMapper tbContractStatisticsMapper;

    /**
     * 查找合同统计信息
     * @param param
     * @return
     */
    public List<Map<String, Object>> findContractStatisticsList(Map<String, Object> param){
        return tbContractStatisticsMapper.findContractStatisticsList(param);
    }

    /**
     * 修改合同统计信息
     * @param param
     * @return
     */
    public int updateContractStatisticsList(Map<String, Object> param){
        return tbContractStatisticsMapper.updateContractStatisticsList(param);
    }

    /**
     * 查询总数
     * @param param
     * @return
     */
    public int findContractCount(Map<String, Object> param){
        return tbContractStatisticsMapper.findContractCount(param);
    }
    /**
     * 设置删除状态
     * @param param
     * @return
     */
    public int setDeleteStatus(Map<String, Object> param){
        return tbContractStatisticsMapper.setDeleteStatus(param);
    }
}
