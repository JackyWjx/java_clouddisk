package com.jzb.org.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface TenderMapper {

    /**
     * 查询招投标信息  list
     *
     * @param param
     * @return
     */
    List<Map<String, Object>> queryTender(Map<String, Object> param);

    /*
    *查询总数
    *
    * */
    int quertTenderCount(Map<String, Object> param);

}
