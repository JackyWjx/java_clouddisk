package com.jzb.org.service;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import com.jzb.org.api.base.RegionBaseApi;
import com.jzb.org.dao.FriendComMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
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

    /**
     * 获取被邀请人企业和部门id
     *
     * @param map
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @Author: DingSC
     */
    public List<Map<String, Object>> getInviteCD(Map<String, Object> map) {
        List<Map<String, Object>> list = friendComMapper.searchInviteCidAndCdId(map);
        int size = list == null ? 0 : list.size();
        List<Map<String, Object>> result = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            Map<String, Object> tempMap = list.get(i);
            String cid = JzbDataType.getString(tempMap.get("cid"));
            if (!JzbTools.isEmpty(cid)) {
                Map<String, Object> comMap = new HashMap<>(3);
                comMap.put("id", tempMap.get("id"));
                comMap.put("cid", tempMap.get("cid"));
                comMap.put("uid", map.get("uid"));
                comMap.put("cname", map.get("cname"));
                comMap.put("phone", map.get("relphone"));
                comMap.put("status", "1");
                if (JzbTools.isEmpty(tempMap.get("cdid"))) {
                    comMap.put("cdid", cid + "0000");
                } else {
                    comMap.put("cdid", tempMap.get("cdid"));
                }
                result.add(comMap);
            }
        }
        return result;
    }

    /**
     * 修改邀请表状态
     *
     * @param map
     * @return int
     * @Author: DingSC
     */
    public int updateInvite(Map<String, Object> map) {
        return friendComMapper.updateInvite(map);
    }
}
