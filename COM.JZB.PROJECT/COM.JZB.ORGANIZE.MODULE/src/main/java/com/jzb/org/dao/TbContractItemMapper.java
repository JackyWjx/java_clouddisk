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
public interface TbContractItemMapper {

    /**
     * 添加企业合同条项
     *
     * @param param
     * @return
     */
    int addContractItem(Map<String, Object> param);

    /**
     * 修改企业合同条项
     *
     * @param param
     * @return
     */
    int updateContractItem(Map<String, Object> param);

    /**
     * 查询企业合同条项
     *
     * @param param
     * @return
     */
    List<Map<String, Object>> queryContractItem(Map<String, Object> param);

}
