package com.jzb.auth.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author dell
 * @Version 1.0
 * @Since 1.0
 * @Date: 2019/8/12 16:21
 */
@Mapper
@Repository
public interface RoleMapper {

    /**
     * 获取角色组总数
     *
     * @param map
     * @return
     */
    int queryRoleCount(Map<String, Object> map);

    /**
     * 根据企业id获取角色组信息
     *
     * @param map
     * @return
     */
    List<Map<String, Object>> queryRoleList(Map<String, Object> map);

    /**
     * 获取角色组角色关联表总数
     *
     * @param map
     * @return
     */
    int queryRelationCount(Map<String, Object> map);

    /**
     * 获取角色组角色关联表信息
     *
     * @param map
     * @return
     */
    List<LinkedHashMap<String, Object>> queryRelationList(Map<String, Object> map);

    /**
     * 新建角色组
     *
     * @param map
     */
    void insertRoleGroup(Map<String, Object> map);

    /**
     * 查询角色组菜单表是否有数据
     *
     * @param map
     * @return
     */
    int queryRoleMenuAuth(Map<String, Object> map);

    /**
     * 批量保存角色组菜单表
     *
     * @param map
     */
    void insertRoleMenuAuth(List<Map<String, Object>> map);

    /**
     * 批量保存角色组功能权限表
     *
     * @param map
     * @return void
     * @Author: DingSC
     * @DateTime: 2019/9/9 16:21
     */
    void insertRoleControlAuth(List<Map<String, Object>> map);

    /**
     * 批量修改菜单选中状态
     *
     * @param map
     * @return
     */
    int updateRoleMenuAuthList(List<Map<String, Object>> map);

    /**
     * 单个修改菜单选中状态
     *
     * @param map
     * @return int
     * @Author: DingSC
     * @DateTime: 2019/9/10 16:55
     */
    int updateRoleMenuAuth(Map<String, Object> map);

    /**
     * 单个修改角色组功能权限表
     *
     * @param map
     * @return int
     * @Author: DingSC
     * @DateTime: 2019/9/10 17:28
     */
    int updateRoleControlAuth(Map<String, Object> map);

    /**
     * 批量修改角色组功能权限表
     *
     * @param map
     * @return int
     * @Author: DingSC
     * @DateTime: 2019/9/10 18:28
     */
    int updateRoleControlAuthList(List<Map<String, Object>> map);


    /**
     * 逻辑删除角色组数据
     *
     * @param map
     * @return
     */
    int updateRoleGroup(Map<String, Object> map);


    /**
     * 修改角色组信息
     *
     * @param map
     * @return
     */
    int updateRoleGroup1(Map<String, Object> map);

    /**
     * 查询角色组所有勾选的有效权限信息
     *
     * @param map
     * @return
     */
    List<Map<String, Object>> queryRoleMenuAuthList(Map<String, Object> map);

    /**
     * 查询关联表是否已有该关联信息
     *
     * @param map
     * @return
     */
    int queryRoleRa(Map<String, Object> map);

    /**
     * 修改角色组关联表状态
     *
     * @param map
     * @return
     */
    int updateRoleRelation(Map<String, Object> map);

    /**
     * 新增角色组关联信息
     *
     * @param map
     * @return
     */
    int insertRoleRelation(Map<String, Object> map);

    /**
     * 根据角色名称查询角色id
     *
     * @param map
     * @return
     */
    List<Map<String, Object>> queryCompanyRole(Map<String, Object> map);

    /**
     * 角色信息(修改)
     *
     * @param map
     * @return
     */
    int updateCompanyRole(Map<String, Object> map);

    /**
     * 新增角色信息
     *
     * @param map
     * @return
     */
    int insertCompanyRole(Map<String, Object> map);

    /**
     * 查询角色总数
     *
     * @param map
     * @return
     */
    int queryCompanyRoleCount(Map<String, Object> map);

    /**
     * 查询分页角色信息
     *
     * @param map
     * @return
     */
    List<LinkedHashMap> queryCompanyRoleList(Map<String, Object> map);

    /**
     * 根据用户部门信息和用户id获取用户角色组信息
     *
     * @param map
     * @return
     */
    List<LinkedHashMap<String, Object>> queryUserDeptGroup(Map<String, Object> map);

    /**
     * 用户角色表总数
     *
     * @param map
     * @return
     */
    int queryUserRoleCount(Map<String, Object> map);

    /**
     * 用户角色表list
     *
     * @param map
     * @return
     */
    List<LinkedHashMap> queryUserRoleList(Map<String, Object> map);

    /**
     * 批量添加用户到用户角色表
     *
     * @param map
     * @return int
     * @Author: DingSC
     * @DateTime: 2019/9/3 10:30
     */
    int insertUserRole(List<Map<String, Object>> map);

    /**
     * 批量修改用户角色表状态
     *
     * @param map
     * @return int
     * @Author: DingSC
     * @DateTime: 2019/9/3 10:36
     */
    int updateUserRole(List<Map<String, Object>> map);


}
