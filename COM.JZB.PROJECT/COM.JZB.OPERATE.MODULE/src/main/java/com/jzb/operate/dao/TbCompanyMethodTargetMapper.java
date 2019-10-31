package com.jzb.operate.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface TbCompanyMethodTargetMapper {

    /**
     * 导入方法论目标
     * @param list
     * @return
     */
    int addMethodTarget(List<Map<String, Object>> list);

    /**
     * 查询方法论目标
     * @param param
     * @return
     */
    List<Map<String, Object>> queryMethodTarget(Map<String, Object> param);

    /**
     * 点击达成目标方法论目标
     * @param list
     * @return
     */
    int updateMethodTarget(List<Map<String, Object>> list);
}
