package com.jzb.org.service;

import com.jzb.org.dao.TbContractServiceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author chenzhengduan
 */
@Service
public class TbContractServiceService {
    @Autowired
    private TbContractServiceMapper tbContractServiceMapper;

    /**
     * 添加企业合同服务
     *
     * @param list
     * @return
     */
    public int addContractService(List<Map<String, Object>> list){
        return tbContractServiceMapper.addContractService(list);
    }

    /**
     * 修改企业合同服务
     *
     * @param param
     * @return
     */
    public int updateContractService(Map<String, Object> param){
        return tbContractServiceMapper.updateContractService(param);
    }

    /**
     * 查询企业合同服务
     *
     * @param param
     * @return
     */
    public List<Map<String, Object>> queryContractService(Map<String, Object> param){
        return tbContractServiceMapper.queryContractService(param);
    }
}
