package com.jzb.resource.service;

import com.jzb.resource.dao.TbContractTemplateItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TbContractTemplateItemService {

    @Autowired
    private TbContractTemplateItemMapper tbContractTemplateItemMapper;

    /**
     * 根据模板id查询项
     * @param param
     * @return
     */
    public List<Map<String, Object>> quertContractTemplateItemByTempid(Map<String, Object> param){
        return tbContractTemplateItemMapper.quertContractTemplateItemByTempid(param);
    }
    /**
     * 根据项id查询项
     * @param param
     * @return
     */
    public List<Map<String, Object>> quertContractTemplateItemByItemid(Map<String, Object> param){
        return tbContractTemplateItemMapper.quertContractTemplateItemByItemid(param);
    }

    /**
     * 修改合同模板项
     * @param list
     * @return
     */
    public int updateContractTemplateItem(List<Map<String, Object>> list){
        return tbContractTemplateItemMapper.updateContractTemplateItem(list);
    }


    /**
     * 添加合同模板项
     * @param list
     * @return
     */
    public int  addContractTemplateItem(List<Map<String, Object>> list){
        return tbContractTemplateItemMapper.addContractTemplateItem(list);
    }
}
