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
public interface TbHandleContractMapper {


    /**
     * 添加合同动态属性
     * @param param
     * @return
     */
    int addHandleContract(Map<String, Object> param);


    /**
     * 修改合同动态属性
     * @param param
     * @return
     */
    int updateHandleContract(Map<String, Object> param);


    /**
     * 查询合同动态属性
     * @param param
     * @return
     */
    List<Map<String, Object>> queryHandleContract(Map<String, Object> param);
}
