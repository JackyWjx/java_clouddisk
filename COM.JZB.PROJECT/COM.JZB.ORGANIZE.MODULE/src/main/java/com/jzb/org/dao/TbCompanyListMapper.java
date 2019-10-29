package com.jzb.org.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface TbCompanyListMapper {

    /**
     * 所有业主-统计销售分析数据查询
     * @param param
     * @return
     */
    List<Map<String, Object>> getCompanyList(Map<String, Object> param);

    /**
     * 所欲业主-销售统计分析分页查询出来的总数
     * @param param
     * @return
     */
    int getCount(Map<String, Object> param);
}
