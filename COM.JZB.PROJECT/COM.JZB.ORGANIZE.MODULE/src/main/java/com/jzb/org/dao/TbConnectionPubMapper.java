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
}
