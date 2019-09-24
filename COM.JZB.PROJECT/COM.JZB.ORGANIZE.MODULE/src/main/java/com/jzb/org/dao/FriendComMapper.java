package com.jzb.org.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author dell
 * @Version 1.0
 * @Since 1.0
 * @Date: 2019/9/24 15:50
 */
@Mapper
@Repository
public interface FriendComMapper {
    /**
     * 通过负责人或者单位名称查伙伴单位数据
     *
     * @param map
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @Author: DingSC
     * @DateTime: 2019/9/24 15:53
     */
    List<Map<String, Object>> searchFriendComByValue(Map<String, Object> map);

    /**
    * 通过负责人或者单位名称查伙伴单位数据总数
    * @Author: DingSC
    * @DateTime: 2019/9/24 16:00
    * @param map
    * @return int
    */
    int searchFriendComByValueCount(Map<String, Object> map);
}
