package com.jzb.org.service;

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
        return tbCompanyContractMapper.addCompanyContract(param);
    }

    /**
     * 修改合同
     * @param param
     * @return
     */
    public int updateCompanyContract(Map<String, Object> param){
        long time=System.currentTimeMillis();
        param.put("updtime",time);
        return tbCompanyContractMapper.updateCompanyContract(param);
    }

    /**
     * 删除合同
     * @param param
     * @return
     */
    public int updateDeleteStatus(Map<String, Object> param){
        long time=System.currentTimeMillis();
        param.put("updtime",time);
        return tbCompanyContractMapper.updateDeleteStatus(param);
    }


    /**
     * 查询合同
     * @param param
     * @return
     */
    public List<Map<String, Object>> quertCompantContract(Map<String, Object> param){
        return tbCompanyContractMapper.quertCompanyContract(param);
    }
}

