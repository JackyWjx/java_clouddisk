package com.jzb.org.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.*;

@Mapper
@Repository
public interface TbCompanyContractFileMapper {

    /**
     * 添加附件
     *
     * @param list
     * @return
     */
    int addCompanyContractFile(List<Map<String, Object>> list);

    /**
     * 化学删除附件
     *
     * @param list
     * @return
     */
    int updateFileStatus(List<Map<String, Object>> list);


    /**
     * 查询合同下的附件
     * @param param
     * @return
     */
    List<Map<String, Object>> findFileByContId(Map<String, Object> param);
}
