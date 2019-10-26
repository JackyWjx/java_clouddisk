package com.jzb.org.service;

import com.jzb.org.dao.TbCompanyDeptMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TbCompanyDeptService {

    @Autowired
    private TbCompanyDeptMapper tbCompanyDeptMapper;
    /**
     * 包含下级的查询  根据用户id去查部门负责人id
     * @param param
     * @return
     */
    public List<Map<String,Object>> getDeptUser(Map<String, Object> param) {
        return tbCompanyDeptMapper.getDeptUser(param);
    }
}
