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
     * 查询我的单位方法论
     * @param list
     * @return
     */
    List<Map<String, Object>> queryMyCompanyMethod(Map<String, Object> list);

    /**
     * 设置方法论状态
     * @param list
     * @return
     */
    int updateCompanyMethodStatus(Map<String, Object> list);


    List<Map<String, Object>> getCompanyMethodByids(Map<String, Object> param);

    int addMethodData(List<Map<String, Object>> datalist);

    List<Map<String, Object>> getCompanyMethoddataAll(Map<String, Object> param);

    List<Map<String, Object>> getMyCompanyMethoddataAll(Map<String, Object> param);

    int delCompanyMethod(String param);

    int delMyCompanyMethod(String param);

    void delCompanyMethod(Map<String, Object> map);

    void delcidsandprojectid(Map<String, Object> map);

    Map<String, Object> queryMyMethodData(Map<String, Object> datalist);

    int addMyMethodData(Map<String, Object> stringObjectMap);

    Map<String, Object> queryMyMethod(Map<String, Object> stringObjectMap);

    int addMymethod(Map<String, Object> stringObjectMap);

    int addMyMethodTypeBrother(Map<String, Object> param);

    int updateMyMethodType(Map<String, Object> param);

    int addMyMethodDataBrother(Map<String, Object> param);

    int updateMyMethodData(Map<String, Object> param);

    List<Map<String, Object>> queryMyMethodType(Map<String, Object> param);
}
