package com.jzb.org.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Mapper
@Repository
public interface TenderDescMapper {

    /**
     * 查询招标公告详情
     * @param param
     * @return
     */
    Map<String, Object> queryTenderDesc(Map<String, Object> param);
}
