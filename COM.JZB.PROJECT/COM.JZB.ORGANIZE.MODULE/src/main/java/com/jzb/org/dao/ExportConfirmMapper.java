package com.jzb.org.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author dell
 * @Version 1.0
 * @Since 1.0
 * @Date: 2019/8/29 20:44
 */
@Mapper
@Repository
public interface ExportConfirmMapper {

    /**
     * 获取导入信息总数
     *
     * @param map
     * @return
     */
    int queryExportCount(Map<String, Object> map);

    /**
     * 根据企业id和批次id获取导入信息
     *
     * @param map
     * @return
     */
    List<Map<String, Object>> queryExportList(Map<String, Object> map);

    /**
     * 获取导入批次信息
     *
     * @param map
     * @return
     */
    List<Map<String, Object>> queryExportBatch(Map<String, Object> map);

    /**
     * 根据企业id和部门id查看部门是否存在
     *
     * @param map
     * @return
     */
    Map<String, Object> queryDeptMap(Map<String, Object> map);

    /**
     * 判断员工是否在指定部门
     *
     * @param map
     * @return
     */
    int queryDeptUserCount(Map<String, Object> map);

    /**
     * 保存单个部门用户信息
     *
     * @param map
     * @return
     */
    int insertDeptUser(Map<String, Object> map);

    /**
     * 保存成员邀请/申请表
     *
     * @param map
     * @return
     */
    int insertInviteUser(Map<String, Object> map);

    /**
     * 修改用户导入信息
     *
     * @param map
     * @return
     */
    int updateExportUserInfo(Map<String, Object> map);
}
