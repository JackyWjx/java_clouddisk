package com.jzb.org.service;

import com.jzb.base.util.JzbRandom;
import com.jzb.org.dao.TbCompanyContractMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TbCompanyContractService {

    @Autowired
    private TbCompanyContractMapper tbCompanyContractMapper;

    /**
     * 生成合同
     * @param param
     * @return
     */
    public int addCompanyContract(Map<String, Object> param){
        long time=System.currentTimeMillis();
        param.put("addtime",time);
        param.put("updtime",time);
        param.put("contid", JzbRandom.getRandomCharLow(19));
        return tbCompanyContractMapper.addCompanyContract(param);
    }


    /**
     * 查询合同
     * @param param
     * @return
     */
    public List<Map<String, Object>> quertCompantContract(Map<String, Object> param){
        return tbCompanyContractMapper.quertCompantContract(param);
    }
}

