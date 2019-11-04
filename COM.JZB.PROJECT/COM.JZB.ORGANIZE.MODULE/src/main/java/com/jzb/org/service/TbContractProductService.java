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
public class TbContractProductService {

    @Autowired
    private TbContractProductMapper tbContractProductMapper;


    /**
     * 添加企业合同产品功能
     *
     * @param list
     * @return
     */
    public int addContractProduct(List<Map<String, Object>> list){
        return tbContractProductMapper.addContractProduct(list);
    }

    /**
     * 修改企业合同产品功能
     *
     * @param param
     * @return
     */
    public int updateContractProduct(Map<String, Object> param){
        return tbContractProductMapper.updateContractProduct(param);
    }

    /**
     * 查询企业合同产品功能
     *
     * @param param
     * @return
     */
    public List<Map<String, Object>> queryContractProduct(Map<String, Object> param){
        return tbContractProductMapper.queryContractProduct(param);
    }
}
