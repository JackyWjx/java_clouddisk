package com.jzb.resource.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface TbContractTemplateTypeMapper {

    /**
     * 查询合同模板类型
     * @param param
     * @return
     */
    List<Map<String, Object>> queryTemplateType(Map<String, Object> param);
}
