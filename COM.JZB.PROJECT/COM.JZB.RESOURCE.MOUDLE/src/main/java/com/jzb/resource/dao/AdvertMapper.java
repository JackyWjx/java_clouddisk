package com.jzb.resource.dao;

import java.util.List;
import java.util.Map;

/**
 * @Description: 广告mpper接口
 * @Author duanfeiyu
 * @Version v1.0
 * @Since 1.0
 * @Date: 2019/8/16 15:21
 */
public interface AdvertMapper {
    /**
     * 广告图片查询
     * @return
     */
    List<Map<String, Object>> queryAdvertisingList();
}
