package com.jzb.operate.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface TbCompanyMethodMapper {

    /**
     * 添加单位方法论（修改同步）
     * @param list
     * @return
     */
    int addCompanyMethod(List<Map<String, Object>> list);

    /**
     * 查询单位方法论
     * @param list
     * @return
     */
    List<Map<String, Object>> queryCompanyMethod(Map<String, Object> list);

    /**
     * 设置方法论状态
     * @param list
     * @return
     */
    int updateCompanyMethodStatus(Map<String, Object> list);
}