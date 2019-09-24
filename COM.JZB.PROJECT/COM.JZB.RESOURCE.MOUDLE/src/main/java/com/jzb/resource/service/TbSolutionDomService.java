package com.jzb.resource.service;

import com.jzb.resource.dao.TbSolutionDomMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TbSolutionDomService {

    @Autowired
    private TbSolutionDomMapper tbSolutionDomMapper;

    /**
     * 查询方案文档
     * @param param
     * @return
     */
    public List<Map<String, Object>> getSolutionDom(Map<String, Object> param){
        return tbSolutionDomMapper.querySolutionDom(param);
    }

    /**
     * 查询方案文档(模糊查询)
     * @param param
     * @return
     */
    public List<Map<String, Object>> getSolutionDomCname(Map<String, Object> param){
        return tbSolutionDomMapper.querySolutionDomCname(param);
    }
    /**
     * 查询方案文档详情ById
     * @param param
     * @return
     */
    public List<Map<String, Object>> queryDomByDomid(Map<String, Object> param){
        return tbSolutionDomMapper.queryDomByDomid(param);
    }

    /*
    * 查询总数
    * */
    public int queryCount(Map<String, Object> param){
        return tbSolutionDomMapper.queryCount(param);
    }

    /**
     * 文档热门榜
     */
    public List<Map<String, Object>> queryHotDom(Map<String, Object> param){
        return tbSolutionDomMapper.queryHotDom(param);
    }
}
