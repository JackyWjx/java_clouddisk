package com.jzb.org.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Mapper
@Repository
public interface TenderResultDescMapper {

    /**
     *
     * @param param
     * @return
     */
    Map<String, Object> queryTenderResultDesc(Map<String, Object> param);
}
