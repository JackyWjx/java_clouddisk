package com.jzb.org.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface TbCompanyProjectMapper {

    /**
     * 查询总数
     * @param param
     * @return
     */
    int getCount(Map<String, Object> param);

    /**
     * 销售业主-公海-项目-数据查询和根据名称模糊查询
     * @param param
     * @return
     */
    List<Map<String, Object>> getComProject(Map<String, Object> param);

    /**
     * 项目的添加
     * @param param
     * @return
     */
    int saveComProject(Map<String, Object> param);

    /**
     * 关联业主  根据项目id进行项目表的修改
     * @return
     * @param param
     */
    int updateComProject(List<Map<String, Object>> param);

    /**
     * 取消业主关联
     * @param param
     * @return
     */
    int ComProject(List<Map<String, Object>> param);

    /**
     * CRM-销售业主-我服务的业主-1
     * 根据服务的项目ID获取项目信息的总数,后台调用不支持前台调用
     *
     * @Author: Kuang Bin
     * @DateTime: 2019/10/19
     */
    int getServiceProjectListCount(Map<String, Object> param);

    /**
     * CRM-销售业主-我服务的业主-1
     * 根据服务的项目ID获取项目信息,后台调用不支持前台调用
     *
     * @Author: Kuang Bin
     * @DateTime: 2019/10/19
     */
    List<Map<String, Object>> queryServiceProjectList(Map<String, Object> param);

    /**
     * CRM-销售业主-我服务的业主-2
     * 获取所有人的uid
     *
     * @Author: Kuang Bin
     * @DateTime: 2019/10/19
     */
    List<Map<String, Object>> queryServiceProjectUid(Map<String, Object> param);

    /**
     * 模糊查询单位名称
     * @param param
     * @return
     */
    Map<String , Object> getCompany(Map<String, Object> param);

    /**
     * 模糊查询项目名称
     * @param param
     * @return
     */
    Map<String , Object> getProject(Map<String, Object> param);

    /**
     * 获取今日添加项目的数量
     * @param param
     * @return
     */
    int getComProjectCount(Map<String, Object> param);

    List<Map<String, Object>> getServiceByProjectid(Map<String, Object> param);

    /**
     * 跟进项目id获取项目信息
     * @param param
     * @return
     */
    List<Map<String, Object>> getServiceProjectInfoByProjectid(Map<String, Object> param);

    List<Map<String, Object>> getProjectidByname(Map<String, Object> param);

    List<Map<String, Object>> getProjectByCname(Map<String, Object> param);

    List<Map<String, Object>> getProjectByUname(Map<String, Object> param);
}
