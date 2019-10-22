package com.jzb.resource.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface TbStandardTypeMapper {



    /**
     * 运营管理下菜单类别的新建
     *
     * @param param
     * @return
     */
    int saveStandardType(Map<String, Object> param);


    /**
     * 运营管理下菜单类别的修改
     *
     * @param param
     * @return
     */
    int updateStandardType(Map<String, Object> param);

    /**
     * 设置删除状态
     *
     * @param param
     * @return
     */
    int updateStatus(Map<String, Object> param);

    /**
     * 查询运营管理中的菜单类别
     *
     * @param
     * @param param
     * @return
     */
    List<Map<String, Object>> getStandardType(Map<String, Object> param);

}
