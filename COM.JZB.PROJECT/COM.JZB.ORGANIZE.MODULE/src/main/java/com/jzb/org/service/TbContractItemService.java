package com.jzb.org.service;

import com.jzb.org.dao.TbContractItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author chenzhengduan
 */
@Service
public class TbContractItemService {

    @Autowired
    private TbContractItemMapper tbContractItemMapper;

    /**
     * 添加企业合同条项
     *
     * @param list
     * @return
     */
    public int addContractItem(List<Map<String, Object>> list){
        return tbContractItemMapper.addContractItem(list);
    }

    /**
     * 修改企业合同条项
     *
     * @param param
     * @return
     */
    public int updateContractItem(Map<String, Object> param){
        return tbContractItemMapper.updateContractItem(param);
    }

    /**
     * 查询企业合同条项
     *
     * @param param
     * @return
     */
    public List<Map<String, Object>> queryContractItem(Map<String, Object> param){
        return tbContractItemMapper.queryContractItem(param);
    }

}
