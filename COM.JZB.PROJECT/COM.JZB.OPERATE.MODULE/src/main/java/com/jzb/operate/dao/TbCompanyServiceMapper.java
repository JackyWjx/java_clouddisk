package com.jzb.operate.dao;

import org.apache.ibatis.annotations.Mapper;
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
     * CRM-销售业主-我服务的业主-1
     * 获取所有的我服务的业主的总数
     *
     * @Author: Kuang Bin
     * @DateTime: 2019/10/19
     */
    int queryCompanyServiceCount(Map<String, Object> param);

    /**
     * CRM-销售业主-我服务的业主-1
     * 获取所有的我服务的业主
     *
     * @Author: Kuang Bin
     * @DateTime: 2019/10/19
     */
    List<Map<String, Object>> queryCompanyServiceList(Map<String, Object> param);

    /**
     * CRM-销售业主-我服务的业主-1
     * 获取所有的我服务的业主项目的次数
     *
     * @Author: Kuang Bin
     * @DateTime: 2019/10/19
     */
    int queryServiceCount(Map<String, Object> param);

    /**
     * 修改跟进记录
     * @param map
     * @return
     */
    int upComanyService(Map<String , Object>  map);

    /**
     * 添加跟进记录
     * @param map
     * @return
     */
    int saveComanyService(Map<String , Object>  map);

    /**
     * CRM-销售业主-我服务的业主-服务记录1
     * 显示我服务的所有记录的总数
     *
     * @Author: Kuang Bin
     * @DateTime: 2019/10/19
     */
    int queryServiceListCount(Map<String, Object> param);

    /**
     * CRM-销售业主-我服务的业主-服务记录1
     * 显示我服务的所有记录
     *
     * @Author: Kuang Bin
     * @DateTime: 2019/10/19
     */
    List<Map<String, Object>> queryServiceList(Map<String, Object> param);

    /**
     * 所有业主-销售统计分析-服务跟踪记录
     * @param param
     * @return
     */
    List<Map<String, Object>> getHandleItem(Map<String, Object> param);

    /**
     * 查询项目分配的售后人员
     */
    List<Map<String, Object>> getCompanyService(Map<String,Object> param);

    /**
     * 分配项目的售后人员进行修改
     * @param param
     * @return
     */
    int updateCompanyService(Map<String, Object> param);


    // 获取已分配售后的项目
    List<Map<String, Object>> queryServiceListGroupProject(Map<String, Object> param);
    // 获取已分配售后的项目数量
    int queryServiceCountGroup(Map<String, Object> projectMap);

    // 查询我服务的项目信息
    List<Map<String, Object>> queryMyProjectList(Map<String, Object> param);

    // 获取我服务的项目数量
    int queryMyProjectListCount(Map<String,Object> param);
}
