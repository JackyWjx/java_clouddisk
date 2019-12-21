package com.jzb.operate.dao;

import com.jzb.base.message.Response;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Author sapientia
 * @Date 2019/12/2 11:29
 *
 */
@Mapper
@Repository
public interface TbTravelMapper {

    //查询出差记录列表
    List<Map<String, Object>> queryAllTravelList(Map<String, Object> param);

    //查询出差详情列表
    List<Map<String, Object>> queryTravelList(Map<String, Object> param) ;

    //查询出差详情下的资料
    List<Map<String, Object>> queryTravelData(Map<String, Object> param);

    //查询出差详情下的情报
    List<Map<String, Object>> queryTravelInfo(Map<String, Object> param);

    //设置删除状态
    int setDeleteStatus(Map<String, Object> param);

    //获取出差记录的条数
    int countAllList(Map<String, Object> param);

    //获取出差详情的条数
    int countTravelList(Map<String, Object> param);

    //获取情报条数
    int countTravelInfo(Map<String, Object> param);

    //查询审批类型
    List<Map<String, Object>> queryTravelApproval(Map<String, Object> param);

    //查询产出
    List<Map<String, Object>> queryTravelProduce();

    //设置撤回状态
    int  setRecallStatus(Map<String, Object> param);

    //查询出差详情bycid
    List<Map<String, Object>> queryDetaBycid(Map<String, Object> param);

    int updateRebStatus(Map<String, Object> param);

    int updateInfoList(Map<String, Object> param);
}
