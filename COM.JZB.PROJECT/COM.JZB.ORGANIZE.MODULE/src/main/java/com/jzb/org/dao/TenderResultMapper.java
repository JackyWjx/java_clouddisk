package com.jzb.org.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface TenderResultMapper {

    /**
     * 查询中标信息
     * @param param
     * @return
     */
    List<Map<String, Object>> queryTenderResult(Map<String, Object> param);

    /*
     *查询总数
     *
     * */
    int quertTenderResultCount(Map<String, Object> param);
}
