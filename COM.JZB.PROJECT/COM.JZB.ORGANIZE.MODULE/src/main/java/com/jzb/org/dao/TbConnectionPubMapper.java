package com.jzb.org.dao;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author chenhui
 * @description
 * @time2019/11/26
 * @other
 */
@Repository
public interface TbConnectionPubMapper {
    // 查询发帖总数
    int getConnectionCount(Map<String, Object> param);

    // 查询发帖信息
    List<Map<String, Object>> getConnectionList(Map<String, Object> param);

    // 修改发帖信息
    int modifyConnectionList(Map<String, Object> param);

    // 新建发帖信息
    int insertConnectionList(Map<String, Object> param);

    // 删除发帖信息
    int removeConnectionList(Map<String, Object> param);

    // 查询任务参数
    List<Map<String,Object>> getTask(Map<String ,Object> param);

    // 新建任务目标参数
    int insertTask(Map<String, Object> param);

    // 修改任务目标参数
    int modifyTask(List<Map<String, Object>> param);

    // 查询朋友圈发帖记录数
    int getWechatCount(Map<String, Object> param);

    // 查询百度发文记录数
    int getBaiDuCount(Map<String, Object> param);


}
