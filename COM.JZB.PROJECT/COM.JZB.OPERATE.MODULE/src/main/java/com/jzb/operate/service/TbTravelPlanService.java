package com.jzb.operate.service;

import com.jzb.operate.dao.TbTravelPlanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author ：Champ-Ping
 * @date ：Created in 2019-12-03 10:49
 * @description：出差计划逻辑层
 * @modified By：
 * @version: 1.0$
 */

@Service
public class TbTravelPlanService {

    @Autowired
    private TbTravelPlanMapper travelPlanMapper;

    /**
     * 添加出差记录
     * @param list
     * @return
     */
    public int addTravelRecord(List<Map<String, Object>> list){

        return travelPlanMapper.addTravelRecord(list);
    }

    /**
     * 添加出差详情
     * @param list
     * @return
     */
    public int addTravelDetails(List<Map<String, Object>> list){

        return travelPlanMapper.addTravelDetails(list);
    }

    /**
     * 设置删除状态
     * @param param
     * @return
     */
    public int setDeleteStatus(Map<String, Object> param){
        return travelPlanMapper.setDeleteStatus(param);
    }

    /**
     * 设置撤回状态
     * @param param
     * @return
     */
    public int setRecallStatus(Map<String, Object> param){
        return travelPlanMapper.setRecallStatus(param);
    }


    /**
     * 更新出差记录
     * @param param
     * @return
     */
    public int updateTravelRecord(Map<String, Object> param){
        return travelPlanMapper.updateTravelRecord(param);
    }

    /**
     * 更新出差详情
     * @param param
     * @return
     */
    public int updateTravelDetials(Map<String, Object> param){
        return travelPlanMapper.updateTravelDetials(param);
    }


    /**
     * 获取出差详情
     * @param param
     * @return
     */
    public List<Map<String, Object>> queryTravelDetailsByTravelid(Map<String, Object> param){
        return  travelPlanMapper.queryTravelDetailsByTravelid(param);
    }

    /**
     * 根据出差id查询出差记录
     * @param map
     * @return
     */
    public Map<String, Object> queryTravelRecordByTravelid(Map<String, Object> map){
        return travelPlanMapper.queryTravelRecordByTravelid(map);
    }
}
