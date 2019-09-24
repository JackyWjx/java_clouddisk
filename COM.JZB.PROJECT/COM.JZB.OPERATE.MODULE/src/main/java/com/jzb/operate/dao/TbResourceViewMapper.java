package com.jzb.operate.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Mapper
@Repository
public interface TbResourceViewMapper {

    /**
     * 添加浏览
     * @param param
     * @return
     */
    int addResourceView(Map<String ,Object> param);

    /**
     * 查询浏览总数以及是否已经浏览
     * @param param
     * @return
     */
    Map<String ,Integer> queryResourceView(Map<String ,Object> param);

    /**
     * 查询是否存在
     * @param param
     * @return
     */
    int queryIsAlreadyView(Map<String ,Object> param);
}
