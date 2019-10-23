package com.jzb.operate.dao;

import com.jzb.base.message.ServerResult;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface TbTravelRecordMapper {

    /**
     * 获取我的出差记录
     * @param param
     * @return
     */
    List<Map<String, Object>> queryTravelRecordByUid(Map<String, Object> param);

    /**
     * 获取总数
     * @param param
     * @return
     */
    int quertTravelRecordCountByUid(Map<String, Object> param);

    /**
     * 添加出差记录
     * @param param
     * @return
     */
    int addTravelRecord(List<Map<String, Object>> param);

    /**
     * 设置删除状态
     * @param param
     * @return
     */
    int setDeleteStatus(Map<String, Object> param);

    /**
     * 修改出差记录
     * @param param
     * @return
     */
    int updateTravelRecord(Map<String, Object> param);

    /**
     * 查询归我审批的出差记录
     * @param param
     * @return
     */
    List<Map<String, Object>> queryMyVerifyTravel(Map<String, Object> param);

    /**
     * 根据出差id查询出差记录
     * @param list
     * @return
     */
    List<Map<String, Object>> queryTravelRecordByTravelid(List<Map<String, Object>> list);

    /**
     * 撤回
     * @param param
     * @return
     */
    int updateTravelRecordStatus(Map<String, Object> param);
}
