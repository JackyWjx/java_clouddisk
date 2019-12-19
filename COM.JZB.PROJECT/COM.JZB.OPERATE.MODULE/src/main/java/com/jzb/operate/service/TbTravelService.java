package com.jzb.operate.service;

import com.jzb.operate.dao.TbTravelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author sapientia
 * @Date 2019/12/2 11:24
 *
 */
@Service
public class TbTravelService {

    @Autowired
    private TbTravelMapper tbTravelMapper;

    /** 查询出差详情*/
    public List<Map<String, Object>> queryTravelList(Map<String, Object> map) {
        return tbTravelMapper.queryTravelList(map);
    }

    /** 查询出差资料*/
    public List<Map<String, Object>> queryTravelData(Map<String, Object> map) {
        return tbTravelMapper.queryTravelData(map);
    }

    /** 查询出差情报*/
    public List<Map<String, Object>> queryTravelInfo(Map<String, Object> map) {
        return tbTravelMapper.queryTravelInfo(map);
    }

    /** 设置删除状态*/
    public int setDeleteStatus(Map<String, Object> map) {
        return tbTravelMapper.setDeleteStatus(map);
    }

    /** 获取出差记录条数*/
    public int countAllList(Map<String, Object> param) {
        return tbTravelMapper.countAllList(param);
    }

    /** 查询所有出差记录*/
    public List<Map<String, Object>> queryAllTravelList(Map<String, Object> param) {
        return tbTravelMapper.queryAllTravelList(param);
    }

    /** 获取出差详情条数*/
    public int countTravelList(Map<String, Object> param) {
        return tbTravelMapper.countTravelList(param);
    }

    /** 获取情报条数*/
    public int countTravelInfo(Map<String, Object> param) {
        return tbTravelMapper.countTravelInfo(param);
    }

    /**查询审批记录 */
    public List<Map<String, Object>> queryTravelApproval(Map<String, Object> param) {
        return  tbTravelMapper.queryTravelApproval(param);
    }

    /** 查询产出情况*/
    public List<Map<String, Object>> queryTravelProduce() {
        return tbTravelMapper.queryTravelProduce();
    }

    /** 设置撤回状态*/
    public int setRecallStatus(Map<String, Object> param) {
        return tbTravelMapper.setRecallStatus(param);
    }

    /** 查询出差详情bycid*/
    public List<Map<String, Object>> queryDetaBycid(Map<String, Object> param) {
        return tbTravelMapper.queryDetaBycid(param);
    }

    /** 设置报销单添加成功信息*/
    public int updateRebStatus(List<Map<String, Object>> param){
        return tbTravelMapper.updateRebStatus(param);
    }

}
