package com.jzb.operate.service;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.Response;
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

    public List<Map<String, Object>> queryTravelList(Map<String, Object> map) {

        return tbTravelMapper.queryTravelList(map);
    }

    public List<Map<String, Object>> queryTravelListDeta(Map<String, Object> map) {
        return tbTravelMapper.queryTravelListDeta(map);
    }

    public List<Map<String, Object>> queryTravelData(Map<String, Object> map) {
        return tbTravelMapper.queryTravelData(map);
    }

    public List<Map<String, Object>> queryTravelInfo(Map<String, Object> map) {
        return tbTravelMapper.queryTravelInfo(map);
    }

    public int  updateTravelFare(Map<String, Object> map) {
        return tbTravelMapper.updateTravelFare(map);
    }

    public List<Map<String, Object>> queryTrackUserList(Map<String, Object> map) {
        return tbTravelMapper.queryTravelListDeta(map);
    }

    public int setDeleteStatus(Map<String, Object> map) {
        return tbTravelMapper.setDeleteStatus(map);
    }

    public List<Map<String, Object>> queryTrackUserListByid(Map<String, Object> map) {
        return tbTravelMapper.queryTravelInfo(map);
    }

    public int countList(Map<String, Object> param) {
        return tbTravelMapper.countList(param);
    }

    public List<Map<String, Object>> queryAllTravelList(Map<String, Object> param) {
        return tbTravelMapper.queryAllTravelList(param);
    }
}
