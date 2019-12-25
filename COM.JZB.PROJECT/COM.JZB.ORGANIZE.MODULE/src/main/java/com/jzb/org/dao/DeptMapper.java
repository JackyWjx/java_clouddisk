package com.jzb.org.dao;

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
 * @Date: 2019/8/13 19:31
 */
@Mapper
@Repository
public interface DeptMapper {
    /**
     * 根据企业id获取部门信息
     *
     * @param mao
     * @return
     */
    List<Map<String, Object>> queryDeptListByCid(Map<String, Object> mao);

    /**
     * 根据名称查询产品信息
     *
     * @param map
     * @return
     */
    List<Map<String, Object>> searchProListByName(Map<String, Object> map);

    /**
    * 获取企业下所有产品线
    * @Author: DingSC
    * @DateTime: 2019/9/6 10:23
    * @param map
    * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
    */
    List<Map<String, Object>> queryProLine(Map<String, Object> map);

    /**
     * 获取产品的菜单权限页面表
     *
     * @param map
     * @return
     */
    List<Map<String, Object>> queryProductMenu(Map<String, Object> map);

    /**
     * 根据用户姓名查询用户部门信息
     *
     * @param map
     * @return
     */
    List<Map<String, Object>> queryDeptByCname(Map<String, Object> map);

    /**
     * 根据用户姓名查询用户部门信息总数
     *
     * @param map
     * @return
     */
    int queryDeptByCnameCount(Map<String, Object> map);

    /**
     * 新建部门（组织架构）
     *
     * @param map
     * @return
     */
    int insertCompanyDept(Map<String, Object> map);

    /**
     * CRM-单位用户-所有用户-单位列表
     * 新建单位加入系统名称
     *
     * @param map
     * @return
     */
    int insertCompanySysConfig(Map<String, Object> map);

    /**
     * 修改部门信息
     *
     * @param map
     * @return
     */
    int updateCompanyDept(Map<String, Object> map);

    /**
     * 删除部门信息
     *
     * @param map
     */
    void deleteCompanyDept(Map<String, Object> map);

    /**
     * 根据用户id获取用户部门信息和在职状态总数
     *
     * @param map
     * @return
     */
    int queryDeptUserCount(Map<String, Object> map);

    /**
     * 根据用户姓名或手机号获取用户部门信息和在职状态
     *
     * @param map
     * @return
     */
    List<Map<String, Object>> queryDeptUser(Map<String, Object> map);

    /**
     * 根据部门id和企业id查看部门是否存在该员工
     *
     * @param map
     * @return
     */
    int queryDeptUserIF(Map<String, Object> map);

    /**
     * 修改部门用户表信息
     *
     * @param map
     * @return
     */
    int updateDeptUser(Map<String, Object> map);
    /**
     * 修改部门用户表信息
     *
     * @param map
     * @return
     */
    int updateDeptUserByUid(Map<String, Object> map);

    /**
     * 新增部门用户表信息
     *
     * @param map
     * @return
     */
    int insertDeptUser(Map<String, Object> map);

    /**
     * 新增部门信息到另一个部门
     *
     * @param map
     * @return int
     * @Author: DingSC
     * @DateTime: 2019/9/4 16:29
     */
    int insertAllDeptUser(Map<String, Object> map);

    /**
     * 保存用户导入批次表
     *
     * @param map
     * @return
     */
    int insertExportBatch(Map<String, Object> map);

    /**
     * 修改用户导入批次表
     *
     * @param map
     * @return
     */
    int updateExportBatch(Map<String, Object> map);

    /**
     * 根据企业id和部门名称查询部门信息
     *
     * @param map
     * @return
     */
    Map<String, Object> getDeptByIdAndName(Map<String, Object> map);

    /**
     * 批量保存用户导入信息表
     *
     * @param list
     * @return
     */
    int insertExportUserInfoList(List<Map<String, Object>> list);

    /**
     * 批量保存成员邀请/申请表
     *
     * @param list
     * @return
     */
    int insertInviteUserList(List<Map<String, Object>> list);

    /**
     * 批量保存部门员工表
     *
     * @param list
     * @return
     */
    int insertDeptUserList(List<Map<String, Object>> list);

    /**
     * 获取企业下属用户总数
     *
     * @param map
     * @return
     */
    int queryDeptUserListCount(Map<String, Object> map);

    /**
     * 获取企业下属用户数据
     *
     * @param map
     * @return
     */
    List<Map<String, Object>> queryDeptUserList(Map<String, Object> map);

    /**
     * 获取部门下所有子级的用户包括自身
     *
     * @param map
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @Author: DingSC
     * @DateTime: 2019/9/4 11:33
     */
    List<Map<String, Object>> queryDeptUserChildList(Map<String, Object> map);

    String getCompanyName(Map<String, Object> map);

    /**
     * 云产品市场的查询
     * @param param
     * @return
     */
    List<Map<String, Object>> getCompanyProduct(Map<String, Object> param);
}
