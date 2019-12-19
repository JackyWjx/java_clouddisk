package com.jzb.operate.service;

import com.jzb.operate.dao.TbAsApproverMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author sapientia
 * @Date 2019/12/18 17:52
 * @Descrpition
 */
@Service
public class TbAsApproverService {

    @Autowired
    private TbAsApproverMapper tbAsApproverMapper;

    /** 作为审批人查询出差*/
    public List<Map<String, Object>> queryAsApprover(Map<String, Object> param) {
        return tbAsApproverMapper.queryAsApprover(param);
    }
}
