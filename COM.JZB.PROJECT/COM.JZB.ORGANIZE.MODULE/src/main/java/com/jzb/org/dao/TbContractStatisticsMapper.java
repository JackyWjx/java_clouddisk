package com.jzb.org.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface TbContractStatisticsMapper {

    /**
     * 查找合同统计信息
     * @param param
     * @return
     */
    List<Map<String, Object>> findContractStatisticsList(Map<String, Object> param);

    /**
     * 修改合同统计信息
     * @param param
     * @return
     */
    int updateContractStatisticsList(Map<String, Object> param);

    /**
     * 查询总数
     * @param param
     * @return
     */
    int findContractCount(Map<String, Object> param);

    /**
     * 设置删除状态
     * @param param
     * @return
     */
    int setDeleteStatus(Map<String, Object> param);
}
