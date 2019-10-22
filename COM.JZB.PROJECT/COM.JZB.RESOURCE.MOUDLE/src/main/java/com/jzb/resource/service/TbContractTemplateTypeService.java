package com.jzb.resource.service;

import com.jzb.resource.dao.TbContractTemplateTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TbContractTemplateTypeService {

    @Autowired
    private TbContractTemplateTypeMapper tbContractTemplateTypeMapper;

    /**
     * 查询合同模板类型
     * @param param
     * @return
     */
    public List<Map<String, Object>> getContactTemplateType(Map<String, Object> param){
        return tbContractTemplateTypeMapper.queryTemplateType(param);
    }
}

