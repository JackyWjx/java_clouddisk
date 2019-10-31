package com.jzb.operate.service;

import com.jzb.operate.dao.TbCompanyMethodTargetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TbCompanyMethodTargetService {

    @Autowired
    private TbCompanyMethodTargetMapper tbCompanyMethodTargetMapper;

    /**
     * 导入方法论目标
     * @param list
     * @return
     */
    public int addMethodTarget(List<Map<String, Object>> list){
        return tbCompanyMethodTargetMapper.addMethodTarget(list);
    }

    /**
     * 查询方法论目标
     * @param param
     * @return
     */
    public List<Map<String, Object>> queryMethodTarget(Map<String, Object> param){
        return tbCompanyMethodTargetMapper.queryMethodTarget(param);
    }

    /**
     * 点击达成目标方法论目标
     * @param list
     * @return
     */
    public int updateMethodTarget(List<Map<String, Object>> list){
        return tbCompanyMethodTargetMapper.updateMethodTarget(list);
    }
}
