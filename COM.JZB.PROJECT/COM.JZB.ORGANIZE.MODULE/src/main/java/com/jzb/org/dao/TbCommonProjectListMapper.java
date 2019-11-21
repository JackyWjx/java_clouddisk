package com.jzb.org.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface TbCommonProjectListMapper {


    /**
     * 销售/业主-公海-项目-新建
     *
     * @param param
     * @return
     */
    int saveCommonProjectList(Map<String, Object> param);

    /**
     * 销售/业主-公海-项目-修改
     *
     * @param param
     * @return
     */
    int updateCommonProjectList(Map<String, Object> param);

    /**
     * 销售/业主-公海-项目-查询总数量
     *
     * @param param
     */
    int getCommonProjectListCount(Map<String, Object> param);

    /**
     * 销售/业主-公海-项目-查询
     *
     * @param param
     */
    List<Map<String, Object>> getCommonProjectList(Map<String, Object> param);

    /**
     * 销售业主-公海-项目-关联项目
     *
     * @param list
     * @return
     */
    int updateCommonProject(List<Map<String, Object>> list);

    /**
     * 销售业主-公海-项目-取消关联项目
     *
     * @param list
     * @return
     */
    int updateCommonProjects(List<Map<String, Object>> list);
}
