package com.jzb.resource.service;

import com.jzb.resource.dao.TbProductSystemStructMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TbProductSystemStructService {

    @Autowired
    private TbProductSystemStructMapper tbProductSystemStructMapper;


    // 查询
    public List<Map<String, Object>> queryStructList(Map<String, Object> param){
        return tbProductSystemStructMapper.queryStructList(param);
    }

    // 添加
    public int addStructList(Map<String, Object> param){
        return tbProductSystemStructMapper.addStructList(param);
    }

    // 修改
    public int updateStructList(Map<String, Object> param){
        return tbProductSystemStructMapper.updateStructList(param);
    }
}
