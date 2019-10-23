package com.jzb.operate.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Mapper
@Repository
public interface TbSalesCompanyMapper {

    /**
     * 添加业主单位
     * @param param
     * @return
     */
    int addSalesCompany(Map<String, Object> param);
}
