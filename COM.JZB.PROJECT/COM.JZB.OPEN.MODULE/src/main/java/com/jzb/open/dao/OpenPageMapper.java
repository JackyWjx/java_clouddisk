package com.jzb.open.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author dell
 * @Version 1.0
 * @Since 1.0
 * @Date: 2019/11/21 15:02
 */
@Mapper
@Repository
public interface OpenPageMapper {

    /**
     * 模糊查询开发者应用表
     *
     * @param param
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @Author: DingSC
     */
    List<Map<String, Object>> searchOrgApplication(Map<String, Object> param);

    /**
     * 模糊查询开发者应用表总数
     *
     * @param param
     * @return int
     * @Author: DingSC
     */
    int searchOrgApplicationCount(Map<String, Object> param);

    /**
     * 新增应用菜单表
     *
     * @param param
     * @return int
     * @Author: DingSC
     */
    int insertApplicationMenu(Map<String, Object> param);

    /**
     * 应用页面表新增
     *
     * @param param
     * @return int
     * @Author: DingSC
     */
    int insertApplicationPage(Map<String, Object> param);

    /**
     * 应用菜单表查询
     *
     * @param param
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @Author: DingSC
     */
    List<Map<String, Object>> getApplicationMenuPage(Map<String, Object> param);
}
