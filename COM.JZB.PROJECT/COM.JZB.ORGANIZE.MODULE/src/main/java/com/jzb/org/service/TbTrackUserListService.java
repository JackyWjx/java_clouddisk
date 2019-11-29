package com.jzb.org.service;

import com.jzb.org.dao.TbTrackUserListMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TbTrackUserListService {

    @Autowired
    private TbTrackUserListMapper tbTrackUserListMapper;


    /**
     * 查询所有跟进记录
     * @return
     */
    public List<Map<String, Object>> findTrackList(Map<String, Object> param){
        return tbTrackUserListMapper.findTrackList(param);
    }

    /**
     * 查询所有跟进记录总数
     * @return
     */
    public int findTrackListCount(){
        return tbTrackUserListMapper.findTrackListCount();
    }

    /**
     * 查询所有跟进记录带条件
     * @param param
     * @return
     */
    public List<Map<String, Object>> findTrackListByKeywords(Map<String, Object> param){
        return tbTrackUserListMapper.findTrackListByKeywords(param);
    }


    /**
     * 查询所有跟进记录总数带条件
     * @param param
     * @return
     */
    public int findTrackListCountByKeywords(Map<String, Object> param){
        return tbTrackUserListMapper.findTrackListCountByKeywords(param);
    }

    /**
     * 根据跟用户姓名查询用户信息
     * @param param
     * @return
     */
    public List<Map<String, Object>> findUnameLike(Map<String, Object> param){
        return tbTrackUserListMapper.findUnameLike(param);
    }

    /**
     * 根据单位名称查询单位信息
     * @param param
     * @return
     */
    public List<Map<String, Object>> findCnameLike(Map<String, Object> param){
        return tbTrackUserListMapper.findCnameLike(param);
    }
}
