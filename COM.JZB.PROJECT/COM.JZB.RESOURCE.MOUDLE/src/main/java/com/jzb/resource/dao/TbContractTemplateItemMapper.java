package com.jzb.resource.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface TbContractTemplateItemMapper {

    /**
     * 根据模板id查询项
     * @param param
     * @return
     */
    List<Map<String, Object>> quertContractTemplateItemByTempid(Map<String, Object> param);


    /**
     * 根据项id查询项
     * @param param
     * @return
     */
    List<Map<String, Object>> quertContractTemplateItemByItemid(Map<String, Object> param);

    /**
     * 修改合同模板项
     * @param list
     * @return
     */
    int updateContractTemplateItem(List<Map<String, Object>> list);


    /**
     * 添加合同模板项
     * @param list
     * @return
     */
    int  addContractTemplateItem(List<Map<String, Object>> list);
}
