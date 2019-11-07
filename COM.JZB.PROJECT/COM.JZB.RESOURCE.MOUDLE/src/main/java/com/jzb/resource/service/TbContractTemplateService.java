package com.jzb.resource.service;

import com.jzb.base.util.JzbRandom;
import com.jzb.resource.dao.TbContractTemplateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TbContractTemplateService {

    @Autowired
    private TbContractTemplateMapper tbContractTemplateMapper;

    /**
     * 查询合同模板
     * @param param
     * @return
     */
    public List<Map<String, Object>> queryContractTemplate(Map<String, Object> param){
        return tbContractTemplateMapper.queryContractTemplate(param);
    }

    /**
     * 添加合同模板
     * @param param
     * @return
     */
    public int addContractTemplate(Map<String, Object> param){
        long time = System.currentTimeMillis();
        param.put("addtime", time);
        param.put("updtime", time);
        param.put("typeid", "T0000");
        return tbContractTemplateMapper.addContractTemplate(param);
    }


    /**
     * 修改合同模板
     * @param param
     * @return
     */
    public int updateContractTemplate(Map<String, Object> param){
        long time = System.currentTimeMillis();
        param.put("updtime", time);
        return tbContractTemplateMapper.updateContractTemplate(param);
    }

    /**
     * 查询合同模板总数
     * @param param
     * @return
     */
    public int getContractTemplateCount(Map<String, Object> param){
        return tbContractTemplateMapper.queryContractTemplateCount(param);
    }
}
