package com.jzb.org.service;

import com.jzb.base.data.JzbDataType;
import com.jzb.org.dao.NewTbTrackUserListMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author sapientia
 * @Date 2019/12/3 19:05
 */

@Service
public class NewTbTrackUserListService {

    @Autowired
    private NewTbTrackUserListMapper newTbTrackUserListMapper;

    public List<Map<String, Object>> queryTrackUserListByKey(Map<String, Object> map) {
        return newTbTrackUserListMapper.queryTrackUserListByKey(map);
    }
}
