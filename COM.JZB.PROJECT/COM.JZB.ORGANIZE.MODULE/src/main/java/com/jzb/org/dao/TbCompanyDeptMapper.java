package com.jzb.org.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface TbCompanyDeptMapper {
    /**
     * 包含下级  根据用户id去查部门表中的部门负责人
     * @param param
     * @return
     */
    List<Map<String,Object>> getDeptUser(Map<String, Object> param);

    List<Map<String,Object>> getDeptUserOnlyOne(Map<String, Object> param);

    int getDeptUserCount(Map<String, Object> param);

    int getDeptUserCountOnlyOne(Map<String, Object> param);
    /**
     * 查询该用户所负责的部门
     *
     */
    List<Map<String,Object>> getDeptForUser(Map<String,Object> param);
    /**
     * 查询这些部门下的所有员工
     */
    List<String> getUserForDept(List<Map<String,Object>>param);
}
