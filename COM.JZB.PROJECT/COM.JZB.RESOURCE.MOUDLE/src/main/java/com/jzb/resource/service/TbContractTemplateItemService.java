package com.jzb.resource.service;

import com.jzb.base.util.JzbRandom;
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
     * @param map
     * @return
     */
    public int updateContractTemplateItem(Map<String, Object> map){
        map.put("updtime", System.currentTimeMillis());
        return tbContractTemplateItemMapper.updateContractTemplateItem(map);
    }


    /**
     * 添加合同模板项
     * @param list
     * @return
     */
    public int  addContractTemplateItem(List<Map<String, Object>> list){
        for (int i = 0; i <list.size(); i++) {
            list.get(i).put("itemid", JzbRandom.getRandomChar(11));
        }
        return tbContractTemplateItemMapper.addContractTemplateItem(list);
    }

    public int addContractTemplateItems(Map<String, Object> map) {
       map.put("itemid", JzbRandom.getRandomChar(11));
        //map.put("tempid", JzbRandom.getRandomChar(9));
       return tbContractTemplateItemMapper.addContractTemplateItems(map);
    }
}
