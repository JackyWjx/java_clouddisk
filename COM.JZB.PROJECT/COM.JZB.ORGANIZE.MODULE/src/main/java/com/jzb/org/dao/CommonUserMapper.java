package com.jzb.org.dao;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @描述
 * @创建人 chenhui
 * @创建时间 2019/11/19
 * @修改人和其它信息
 */
@Repository
public interface CommonUserMapper {
    // 添加公海用户
    int addCommUser(Map<String, Object> paramp);

    // 获取公海用户总数
    int getCount(Map<String, Object> paramp);

    // 获取公海用户信息
    List<Map<String, Object>> queryComUser(Map<String, Object> paramp);

    // 修改公海用户信息
    int updComUser(Map<String, Object> paramp);

    // 删除公海用户信息
    int delUser(Map<String,Object> map);
}
