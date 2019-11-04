package com.jzb.org.service;

import com.jzb.base.util.JzbRandom;
import com.jzb.org.dao.TbCompanyCommonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author chenzhengduan
 */
@Service
public class TbCompanyCommonService {

    @Autowired
    private TbCompanyCommonMapper tbCompanyCommonMapper;


    /**
     * 查询不带条件的业主单位全部（不带条件）
     * @param param
     * @return
     */
    public List<Map<String, Object>> queryCompanyCommon(Map<String, Object> param){
        return tbCompanyCommonMapper.queryCompanyCommon(param);
    }


    /**
     * 查询带条件的业主单位全部（带条件）
     * @param param
     * @return
     */
    public List<Map<String, Object>> queryCompanyCommonByKeyWord(Map<String, Object> param){
        return tbCompanyCommonMapper.queryCompanyCommonByKeyWord(param);
    }


    /**
     * 查询单位名称
     * @param param
     * @return
     */
    public String queryCompanyNameByID(Map<String, Object> param){
        return tbCompanyCommonMapper.queryCompanyNameByID(param);
    }

    /**
     * 修改单位信息
     * @param param
     * @return
     */
    public int updateCompany(Map<String, Object> param){
        return tbCompanyCommonMapper.updateCompany(param);
    }


    /**
     * 查询业主列表下的查询出来的总数
     * @param param
     * @return
     */
    public int getCount(Map<String, Object> param) {
        return tbCompanyCommonMapper.getCount(param);
    }

    /**
     * 查询所有业主-业主列表
     * @param param
     * @return
     */
    public List<Map<String, Object>> getCompanyCommon(Map<String, Object> param) {
        return tbCompanyCommonMapper.getCompanyCommon(param);
    }

    /**
     * 所有业主-业主列表-新建
     * @param param
     * @return
     */
    public int saveCompanyCommon(Map<String, Object> param) {
        long time = System.currentTimeMillis();
        param.put("addtime", time);
        param.put("updtime", time);
        param.put("regtime", time);
        param.put("modtime", time);
        param.put("cid", JzbRandom.getRandomChar(7));
        return tbCompanyCommonMapper.saveCompanyCommon(param);
    }

    /**
     * 所有业主-业主列表-修改
     * @param param
     * @return
     */
    public int updateCompanyCommon(Map<String, Object> param) {
        long time = System.currentTimeMillis();
        param.put("modtime", time);
        return tbCompanyCommonMapper.updateCompanyCommon(param);
    }

    /**
     * 所有业主-业主列表-删除
     * @param paramList
     * @return
     */
    public int deleteCompanyCommon(List<Map<String, Object>> paramList) {
        for (int i = 0; i < paramList.size(); i++) {
            paramList.get(i).put("status", 4);
        }
        return tbCompanyCommonMapper.deleteCompanyCommon(paramList);
    }

    /**
     * 所有业主-业主列表-分配业务员
     * @param param
     * @return
     */
    public int updateCompanys(Map<String, Object> param) {
        param.put("status", 2);
        return tbCompanyCommonMapper.updateCompanys(param);
    }
}
