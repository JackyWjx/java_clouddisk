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
     * 根据服务的项目ID获取项目信息,后台调用不支持前台调用
     *
     * @Author: Kuang Bin
     * @DateTime: 2019/10/19
     */
    List<Map<String, Object>> queryServiceProjectList(List<Map<String, Object>> param);

    /**
     * CRM-销售业主-我服务的业主-2
     * 根据服务的项目ID获取模糊搜索项目信息的总数,后台调用不支持前台调用
     *
     * @Author: Kuang Bin
     * @DateTime: 2019/10/19
     */
    int queryCompanyServiceCount(Map<String, Object> param);

    /**
     * CRM-销售业主-我服务的业主-2
     * 根据服务的项目ID获取模糊搜索项目信息,后台调用不支持前台调用
     *
     * @Author: Kuang Bin
     * @DateTime: 2019/10/19
     */
    List<Map<String, Object>> queryServiceProject(Map<String, Object> param);
}
