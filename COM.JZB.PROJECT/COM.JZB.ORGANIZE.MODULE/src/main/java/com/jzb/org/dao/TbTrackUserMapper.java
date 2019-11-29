package com.jzb.org.dao;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author chenhui
 * @description
 * @time2019/11/25
 * @other
 */
@Repository
public interface TbTrackUserMapper {

    // 新建跟进人员记录
    int addTrackUser(Map<String, Object> param);

    // 查询跟进人员记录总数
    int getTrackCount(Map<String, Object> param);

    // 查询跟进人员记录信息
    List<Map<String, Object>> queryTrackUserList(Map<String, Object> param);

    // 删除跟进人员记录信息
    int delTrackUser(Map<String,Object> param);

    // 修改跟进人员记录信息
    int updTrackUser(Map<String, Object> param);

    int getQCount(Map<String, Object> param);

    int getPCount(Map<String, Object> param);

    /**
     * 查询意向数目
     * @param param
     * @return
     */
    int getHandleCount(Map<String, Object> param);
}
