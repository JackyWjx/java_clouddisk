package com.jzb.resource.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface TbMethodDataMapper {

    /*
    * 1.根据方法论类型查询方法论资料
    * */
    List<Map<String, Object>> quertMethodData(Map<String, Object> param);

    /*
     * 2.修改方法论资料
     * */
    int updateMethodData(Map<String, Object> param);

    /*
     * 3.添加方法论资料
     * */
    int saveMethodData(Map<String, Object> param);

    /*
     * 4.获取排序
     * */
    int getMethodDataIdx();



    List<Map<String, Object>> getMethodDataItme(Map<String, Object> param);

    int delMethodData(Map<String, Object> param);

    int getMethodDatazi(Map<String, Object> param);

    List<Map<String, Object>> getMethodDataTypeIdsAll(Map<String, Object> param);

    int delMethodDataids(Map<String, Object> param);

    List<Map<String, Object>> getMethodDataByTypeid(Map<String, Object> param);
}
