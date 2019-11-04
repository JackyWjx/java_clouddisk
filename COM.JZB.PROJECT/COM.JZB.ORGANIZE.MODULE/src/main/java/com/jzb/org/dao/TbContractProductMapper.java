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
public interface TbContractProductMapper {

    /**
     * 添加企业合同产品
     * @param list
     * @return
     */
    int addContractProduct(List<Map<String, Object>> list);

    /**
     * 修改企业合同产品
     * @param param
     * @return
     */
    int updateContractProduct(Map<String, Object> param);

    /**
     * 查询企业合同产品
     * @param param
     * @return
     */
    List<Map<String, Object>> queryContractProduct(Map<String, Object> param);
}
