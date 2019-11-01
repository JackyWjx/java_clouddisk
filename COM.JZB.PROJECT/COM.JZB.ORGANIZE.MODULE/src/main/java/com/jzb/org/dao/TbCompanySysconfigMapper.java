package com.jzb.org.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Map;


/**
 * @author chenzhengduan
 */
@Mapper
@Repository
public interface TbCompanySysconfigMapper {

    /**
     * 添加二级域名
     * @param param
     * @return
     */
    int addCompanySysconfig(Map<String, Object> param);

    /**
     * 查询该单位的二级域名
     * @param param
     * @return
     */
    String queryCompanySysconfig(Map<String, Object> param);
}

