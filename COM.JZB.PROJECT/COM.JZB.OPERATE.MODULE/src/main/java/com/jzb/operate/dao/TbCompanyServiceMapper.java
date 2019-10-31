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
}
