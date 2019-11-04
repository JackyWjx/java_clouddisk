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
public interface TbContractServiceMapper {

    /**
     * 添加企业合同服务
     *
     * @param list
     * @return
     */
    int addContractService(List<Map<String, Object>> list);

    /**
     * 修改企业合同服务
     *
     * @param param
     * @return
     */
    int updateContractService(Map<String, Object> param);

    /**
     * 查询企业合同服务
     *
     * @param param
     * @return
     */
    List<Map<String, Object>> queryContractService(Map<String, Object> param);

}
