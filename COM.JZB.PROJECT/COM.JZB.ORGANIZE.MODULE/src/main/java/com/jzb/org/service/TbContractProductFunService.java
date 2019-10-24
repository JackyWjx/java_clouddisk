package com.jzb.org.service;

import com.jzb.org.dao.TbContractProductFunMapper;
import com.jzb.org.dao.TbContractProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author chenzhengduan
 */
@Service
public class TbContractProductFunService {

    @Autowired
    private TbContractProductFunMapper tbContractProductFunMapper;


    /**
     * 添加企业合同产品功能
     *
     * @param param
     * @return
     */
    public int addContractProductFun(Map<String, Object> param){
        return tbContractProductFunMapper.addContractProductFun(param);
    }

    /**
     * 修改企业合同产品功能
     *
     * @param param
     * @return
     */
    public int updateContractProductFun(Map<String, Object> param){
        return tbContractProductFunMapper.updateContractProductFun(param);
    }

    /**
     * 查询企业合同产品功能
     *
     * @param param
     * @return
     */
    public List<Map<String, Object>> queryContractProductFun(Map<String, Object> param){
        return tbContractProductFunMapper.queryContractProductFun(param);
    }
}
