package com.jzb.org.service;

import com.jzb.base.message.Response;
import com.jzb.org.api.base.RegionBaseApi;
import com.jzb.org.dao.FriendComMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description: 伙伴单位业务层
 * @Author dell
 * @Version 1.0
 * @Since 1.0
 * @Date: 2019/9/24 15:47
 */
@Service
public class FriendComService {

    @Autowired
    private FriendComMapper friendComMapper;
    @Autowired
    private RegionBaseApi regionBaseApi;

    /**
     * 通过负责人或者单位名称查伙伴单位数据
     *
     * @param map
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @Author: DingSC
     * @DateTime: 2019/9/24 15:56
     */
    public List<Map<String, Object>> searchFriendComByValue(Map<String, Object> map) {
        List<Map<String, Object>> friendList = friendComMapper.searchFriendComByValue(map);
        int size = friendList.size();
        List<Map<String, Object>> result = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            Map<String, Object> companyMap = friendList.get(i);
            Response region = regionBaseApi.getRegionInfo(companyMap);
            companyMap.put("region", region.getResponseEntity());
            result.add(companyMap);
        }
        return result;
    }

    /**
     * 通过负责人或者单位名称查伙伴单位数据总数
     *
     * @param map
     * @return int
     * @Author: DingSC
     * @DateTime: 2019/9/24 16:04
     */
    public int searchFriendComByValueCount(Map<String, Object> map) {
        return friendComMapper.searchFriendComByValueCount(map);
    }
}
