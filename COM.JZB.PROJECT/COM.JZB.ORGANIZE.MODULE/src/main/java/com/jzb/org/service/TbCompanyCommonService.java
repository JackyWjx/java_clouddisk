package com.jzb.org.service;

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
     * 修改单位信息
     * @param param
     * @return
     */
    public int updateCompany(Map<String, Object> param){
        return tbCompanyCommonMapper.updateCompany(param);
    }

}
