package com.jzb.operate.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface CustomInterfaceMapper {

    /**
     * 中台首页-用户自定义界面1
     * 查询自定义界面数据
     *
     * @param param
     * @return
     */
    List<Map<String, Object>> queryCustomInterface(Map<String, Object> param);

    /**
     * 中台首页-用户自定义界面1
     * 初始化数据查询
     *
     * @param param
     * @return
     */
    List<Map<String, Object>> queryConfigPage(Map<String, Object> param);

    /**
     * 中台首页-用户自定义界面1
     * 初始化用户自定义数据
     *
     * @param param
     * @return
     */
    int insertCustomInterface(List<Map<String, Object>> param);

    /**
     * 中台首页-用户自定义界面2
     * 修改自定义界面数据
     *
     * @param param
     * @return
     */
    int updateCustomInterface(List<Map<String, Object>> param);
}

