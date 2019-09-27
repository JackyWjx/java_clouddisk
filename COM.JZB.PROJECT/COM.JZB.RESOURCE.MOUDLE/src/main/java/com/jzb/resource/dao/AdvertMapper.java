package com.jzb.resource.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Description: 广告mpper接口
 * @Author duanfeiyu
 * @Version v1.0
 * @Since 1.0
 * @Date: 2019/8/16 15:21
 */
@Mapper
@Repository
public interface AdvertMapper {
    /**
     * 广告图片查询
     * @return
     */
    List<Map<String, Object>> queryAdvertisingList();

    /**
     * CRM-运营管理-活动-推广图片
     * 点击活动获取所有的系统推广信息的总数
     *
     * @author kuangbin
     */
    int queryAdvertListCount(Map<String, Object> param);

    /**
     * CRM-运营管理-活动-推广图片
     * 点击活动获取所有的系统推广信息
     *
     * @author kuangbin
     */
    List<Map<String, Object>> queryAdvertList(Map<String, Object> param);

    /**
     * CRM-运营管理-活动-推广图片
     * 点击保存后修改对应的推广信息
     *
     * @author kuangbin
     */
    int updateAdvertData(Map<String, Object> param);
}
