package com.jzb.operate.dao;

import org.apache.ibatis.annotations.Mapper;
import org.omg.CORBA.OBJ_ADAPTER;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface TbCompanyServiceMapper {
    /**
     * 项目跟进
     * @param param
     * @return
     */
    int saveProject(Map<String, Object> param);

    /**
     * 项目跟进的查询
     * @param param
     * @return
     */
    List<Map<String, Object>> getProject(Map<String, Object> param);

    /**
     * 修改跟进记录
     * @param map
     * @return
     */
    int upComanyService(Map<String , Object>  map);

    /**
     * 修改跟进记录
     * @param map
     * @return
     */
    int saveComanyService(Map<String , Object>  map);
}
