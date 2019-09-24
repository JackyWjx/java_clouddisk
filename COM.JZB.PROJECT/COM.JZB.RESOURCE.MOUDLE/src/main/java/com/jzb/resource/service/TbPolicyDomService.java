package com.jzb.resource.service;

import com.jzb.resource.dao.TbPolicyDomMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TbPolicyDomService {
    @Autowired
    private TbPolicyDomMapper tbPolicyDomMapper;

    /**
     *     查询政策文档列表(模糊查询)
     */
    public List<Map<String, Object>> queryPolicyDomList(Map<String, Object> param){
       return tbPolicyDomMapper.queryPolicyDomList(param);
    }

    /**
     *     标准文档总数
     */
    public int queryDocumentsCount(Map<String, Object> param){
       return tbPolicyDomMapper.queryDocumentsCount(param);
    }

    /**
     *     查询政策文档详情
     */
    public List<Map<String, Object>> queryPolicyDomDesc(Map<String, Object> param){
       return tbPolicyDomMapper.queryPolicyDomDesc(param);
    }

    /**
     *     查询热门文档
     */
    public List<Map<String, Object>> queryHotDom(Map<String, Object> param){
        return tbPolicyDomMapper.queryHotDom(param);
    }
}
