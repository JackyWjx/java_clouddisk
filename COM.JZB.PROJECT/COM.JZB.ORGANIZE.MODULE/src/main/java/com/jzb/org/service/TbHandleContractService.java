package com.jzb.org.service;

import com.jzb.org.dao.TbHandleContractMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author chenzhengduan
 */
@Service
public class TbHandleContractService {

    @Autowired
    private TbHandleContractMapper tbHandleContractMapper;

    /**
     * 添加合同动态属性
     *
     * @param list
     * @return
     */
    public int addHandleContract(List<Map<String, Object>> list) {
        return tbHandleContractMapper.addHandleContract(list);
    }

    /**
     * 修改合同动态属性
     *
     * @param param
     * @return
     */
    public int updateHandleContract(Map<String, Object> param) {
        return tbHandleContractMapper.updateHandleContract(param);
    }

    /**
     * 查询合同动态属性
     *
     * @param param
     * @return
     */
    public List<Map<String, Object>> queryHandleContract(Map<String, Object> param) {
        return tbHandleContractMapper.queryHandleContract(param);
    }
}
