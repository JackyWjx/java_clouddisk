package com.jzb.resource.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface TbContractTemplateMapper {

    /**
     * 查询合同模板
     * @param param
     * @return
     */
    List<Map<String, Object>> queryContractTemplate(Map<String, Object> param);

    /**
     * 删除
     * @param list
     * @return
     */
    int updateContractTemplateStatus(List<String> list);
    /**
     * 添加合同模板
     * @param param
     * @return
     */
    int addContractTemplate(Map<String, Object> param);

    /**
     * 修改合同模板
     * @param param
     * @return
     */
    int updateContractTemplate(Map<String, Object> param);

    /**
     * 查询合同模板总数
     * @param param
     * @return
     */
    int queryContractTemplateCount(Map<String, Object> param);
}
