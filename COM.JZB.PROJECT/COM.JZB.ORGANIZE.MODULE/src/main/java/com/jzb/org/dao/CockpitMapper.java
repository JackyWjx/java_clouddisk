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
}
