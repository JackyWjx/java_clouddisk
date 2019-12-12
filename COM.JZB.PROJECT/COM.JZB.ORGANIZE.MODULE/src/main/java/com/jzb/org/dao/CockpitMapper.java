package com.jzb.org.dao;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author chenhui
 * @description
 * @time 2019/12/6
 * @other
 */
@Repository
public interface CockpitMapper {
    //驾驶舱/联系客户-查询
    int getInfo(Map<String, Object> param);

    // 愿意见-深度见-上会-签约数量
    int getHandleCount(Map<String, Object> param);

    // 查询该部门下的所有用户的记录数
    int  getDeptUser(Map<String, Object> param);

    // 查询部门下所有愿意见-深度见-上会-签约数量
    int getDeptCount(Map<String, Object> param);

    List<Map<String, Object>> getComAuthCount(Map<String, Object> param);

    int getCompanyUser(Map<String, Object> param);

    // 查询部门下所有的下级部门
    List<Map<String,Object>> getDeptChild(Map<String, Object> param);

    List<Map<String, Object>> getAllDeptUser(Map<String, Object> param);
}
