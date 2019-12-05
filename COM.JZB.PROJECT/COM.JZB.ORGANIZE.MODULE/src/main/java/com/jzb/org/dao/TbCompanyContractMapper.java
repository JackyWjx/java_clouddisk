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
public interface TbCompanyContractMapper {

    /**
     * 生成合同
     * @param param
     * @return
     */
    int addCompanyContract(Map<String, Object> param);

    /**
     * 修改合同
     * @param param
     * @return
     */
    int updateCompanyContract(Map<String, Object> param);


    /**
     * 修改合同已入库
     * @param param
     * @return
     */
    int updateCompanyContractStatus(Map<String, Object> param);


    /**
     * 删除合同
     * @param param
     * @return
     */
    int updateDeleteStatus(Map<String, Object> param);

    /**
     * 查询合同
     * @param param
     * @return
     */
    List<Map<String, Object>> quertCompanyContract(Map<String, Object> param);
}
