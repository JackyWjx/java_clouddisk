package com.jzb.org.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface TbCommonProjectInfoMapper {

    /**
     * 项目情报的新增
     * @param param
     * @return
     */
    int saveCommonProjectInfo(Map<String, Object> param);

    /**
     * 项目情报的修改
     * @param param
     * @return
     */
    int updateCommonProjectInfo(Map<String, Object> param);

    /**
     * 项目情报的查询
     * @param param
     * @return
     */
    List<Map<String, Object>> getCommonProjectInfo(Map<String, Object> param);

}
