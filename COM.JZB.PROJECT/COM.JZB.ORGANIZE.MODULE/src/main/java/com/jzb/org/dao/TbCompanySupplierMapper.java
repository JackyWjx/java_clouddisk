package com.jzb.org.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author chenzhengduan
 * 已分配业主下的供应商
 */
@Mapper
@Repository
public interface TbCompanySupplierMapper {

    /**
     * 添加供应商
     * @param param
     * @return
     */
    int addCompanySupplier(Map<String, Object> param);


    /**
     * 修改供应商
     * @param param
     * @return
     */
    int updateCompanySupplier(Map<String, Object> param);


    /**
     * 查询供应商
     * @param param
     * @return
     */
    List<Map<String, Object>> queryCompanySupplier(Map<String, Object> param);

    /**
     * CRM-销售业主-公海-供应商6
     * 删除供应商
     *
     * @author kuangbin
     */
    int deleteCompanySupplier(Map<String, Object> param);
}
