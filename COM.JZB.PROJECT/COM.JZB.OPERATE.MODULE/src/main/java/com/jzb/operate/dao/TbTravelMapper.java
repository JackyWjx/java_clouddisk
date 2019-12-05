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

     List<Map<String, Object>> queryTravelList(Map<String, Object> map) ;

    List<Map<String, Object>> queryTravelListDeta(Map<String, Object> map) ;

    List<Map<String, Object>> queryTravelData(Map<String, Object> map);

    List<Map<String, Object>> queryTravelInfo(Map<String, Object> map);

    int  updateTravelFare(Map<String, Object> map);

    int setDeleteStatus(Map<String, Object> map);
}
