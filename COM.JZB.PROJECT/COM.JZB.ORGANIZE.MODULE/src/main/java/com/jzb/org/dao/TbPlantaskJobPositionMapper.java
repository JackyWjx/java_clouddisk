package com.jzb.org.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface TbPlantaskJobPositionMapper {

    List<Map<String,Object>>  selectRole (Map<String,Object> param);

    List<Map<String,Object>>  selectRoleByDeptid (Map<String,Object> param);

    List<Map<String,Object>>  selectDept (Map<String,Object> param);

    Integer insertRoleAndDept (Map<String,Object> param);

    Integer updateRoleName (Map<String,Object> param);

}
