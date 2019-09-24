package com.jzb.operate.service;

import com.jzb.operate.dao.TbTravelRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.TypeVariable;
import java.util.List;
import java.util.Map;

@Service
public class TbTravelRecordService {

    @Autowired
    private TbTravelRecordMapper tbTravelRecordMapper;

    /**
     * 获取个人出差记录
     * @param param
     * @return
     */
    public List<Map<String, Object>> getTravelRecordListByUid(Map<String, Object> param) {
        return tbTravelRecordMapper.queryTravelRecordByUid(param);
    }

    /**
     * 根据uid 获取 出差记录总数
     * @param param
     * @return
     */
    public int  getTravelRecordCountByUid(Map<String, Object> param){
        return tbTravelRecordMapper.quertTravelRecordCountByUid(param);
    }

    /**
     * 添加出差记录
     * @param list
     * @return
     */
    public int addTravelRecord(List<Map<String, Object>> list){
        return tbTravelRecordMapper.addTravelRecord(list);
    }

    /**
     * 设置删除状态
     * @param param
     * @return
     */
    public int setDeleteStatus(Map<String, Object> param){
        return tbTravelRecordMapper.setDeleteStatus(param);
    }


    /**
     * 修改出差记录
     * @param param
     * @return
     */
    public int updateTravelRecord(Map<String, Object> param){
        return tbTravelRecordMapper.updateTravelRecord(param);
    }

    /**
     * 获取归我审批的出差记录
     * @param param
     * @return
     */
    public List<Map<String, Object>> getMyVerifyTravel(Map<String, Object> param){
      return   tbTravelRecordMapper.queryMyVerifyTravel(param);
    }

    /**
     * 根据出差id查询多条出差记录
     * @param list
     * @return
     */
    public List<Map<String, Object>> getTravelRecordByTravelid(List<Map<String, Object>> list){
        return tbTravelRecordMapper.queryTravelRecordByTravelid(list);
    }
}