package com.jzb.org.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface TbCompanyProductMapper {

    /**
     * 添加企业产品
     * @param param
     * @return
     */
    int addCompanyProduct(List<Map<String, Object>> param);
}
