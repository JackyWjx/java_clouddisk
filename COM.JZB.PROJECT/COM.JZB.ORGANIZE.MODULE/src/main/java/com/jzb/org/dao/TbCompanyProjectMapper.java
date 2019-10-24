package com.jzb.org.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface TbCompanyProjectMapper {

    /**
     * 查询总数
     * @param param
     * @return
     */
    int getCount(Map<String, Object> param);

    /**
     * 销售业主-公海-项目-数据查询和根据名称模糊查询
     * @param param
     * @return
     */
    List<Map<String, Object>> getComProject(Map<String, Object> param);

    /**
     * 项目的添加
     * @param param
     * @return
     */
    int saveComProject(Map<String, Object> param);

    /**
     * 关联业主  根据项目id进行项目表的修改
     * @return
     * @param param
     */
    int updateComProject(Map<String, Object> param);

}