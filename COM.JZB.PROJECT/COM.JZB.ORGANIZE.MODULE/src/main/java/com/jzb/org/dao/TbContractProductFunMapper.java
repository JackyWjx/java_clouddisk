package com.jzb.org.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author chenzhengduan
 */

@Mapper
@Repository
public interface TbContractProductFunMapper {

    /**
     * 添加企业合同产品功能
     * @param param
     * @return
     */
    int addContractProductFun(Map<String, Object> param);

    /**
     * 修改企业合同产品功能
     * @param param
     * @return
     */
    int updateContractProductFun(Map<String, Object> param);

    /**
     * 查询企业合同产品功能
     * @param param
     * @return
     */
    List<Map<String, Object>> queryContractProductFun(Map<String, Object> param);



}
