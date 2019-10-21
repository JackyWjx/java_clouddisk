package com.jzb.operate.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface TbHandleMapper {
    /**
     * CRM-销售业主-公海-业主下的人员13
     * 点击业主/项目/跟进信息获取项目下的跟进信息的总数
     *
     * @Author: Kuang Bin
     * @DateTime: 2019/10/19
     */
    int queryHandlecItemCount(Map<String, Object> param);

    /**
     * CRM-销售业主-公海-业主下的人员13
     * 点击业主/项目/跟进信息获取项目下的跟进信息
     *
     * @Author: Kuang Bin
     * @DateTime: 2019/10/19
     */
    List<Map<String, Object>> queryHandlecItem(Map<String, Object> param);

    /**
     * CRM-销售业主-公海-业主下的人员14
     * 点击业主/项目/跟进信息中点击添加跟进信息
     *
     * @Author: Kuang Bin
     * @DateTime: 2019/10/19
     */
    int insertHandlecItem(Map<String, Object> param);

    /**
     * CRM-销售业主-公海-业主下的人员15
     * 点击业主/项目/跟进信息中点击修改跟进信息
     *
     * @Author: Kuang Bin
     * @DateTime: 2019/10/19
     */
    int updateHandlecItem(Map<String, Object> param);
}
