package com.jzb.resource.service;

import com.jzb.resource.dao.TbAppVersionListMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TbAppVersionListService {
    @Autowired
    private TbAppVersionListMapper tbAppVersionListMapper;


    /** 查询最新app版本 */
    public Map<String, Object> queryAppVersion(){
        return tbAppVersionListMapper.queryAppVersion();
    }
}
