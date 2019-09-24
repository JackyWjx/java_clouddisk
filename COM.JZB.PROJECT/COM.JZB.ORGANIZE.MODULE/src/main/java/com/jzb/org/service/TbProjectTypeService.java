package com.jzb.org.service;

import com.jzb.org.dao.TbProjectTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TbProjectTypeService {

    @Autowired
    private TbProjectTypeMapper tbProjectTypeMapper;

    public List<Map<String, Object>> getProjectType(){
        return tbProjectTypeMapper.queryAllType();
    }
}

