package com.jzb.org.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface TbProjectRivalMapper {

    /**
     * 项目竞争单位的添加
     * @param param
     * @return
     */
    int saveProjectRiva(Map<String, Object> param);

    /**
     * 项目竞争单位的修改
     * @param param
     * @return
     */
    int updateProjectRiva(Map<String, Object> param);


    /**
     * 项目竞争对手的查询
     * @param param
     * @return
     */
    List<Map<String, Object>> getProjectRiva(Map<String, Object> param);
}
