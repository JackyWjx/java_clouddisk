package com.jzb.operate.service;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbRandom;
import com.jzb.operate.api.base.RegionBaseApi;
import com.jzb.operate.api.redis.UserRedisServiceApi;
import com.jzb.operate.dao.TbConsumeVerifyMapper;
import com.jzb.operate.dao.TbHandleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TbHandleService {

    @Autowired
    private TbHandleMapper tbHandleMapper;

    @Autowired
    private RegionBaseApi regionBaseApi;

    @Autowired
    private UserRedisServiceApi userRedisServiceApi;

    /**
     * CRM-销售业主-公海-业主下的人员13
     * 点击业主/项目/跟进信息获取项目下的跟进信息的总数
     *
     * @Author: Kuang Bin
     * @DateTime: 2019/10/19
     */
    public int getHandlecItemCount(Map<String, Object> param) {
        param.put("status", "1");
        return tbHandleMapper.queryHandlecItemCount(param);
    }

    /**
     * CRM-销售业主-公海-业主下的人员13
     * 点击业主/项目/跟进信息获取项目下的跟进信息
     *
     * @Author: Kuang Bin
     * @DateTime: 2019/10/19
     */
    public List<Map<String, Object>> getHandlecItem(Map<String, Object> param) {
        param.put("status", "1");
        List<Map<String, Object>> list = tbHandleMapper.queryHandlecItem(param);
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> companyMap = list.get(i);
            Response region = regionBaseApi.getRegionInfo(companyMap);
            companyMap.put("region", region.getResponseEntity());
            // 获取跟进人信息
            Response uid = userRedisServiceApi.getCacheUserInfo(companyMap);
            companyMap.put("uid", uid.getResponseEntity());
        }
        return list;
    }

    /**
     * CRM-销售业主-公海-业主下的人员14
     * 点击业主/项目/跟进信息中点击添加跟进信息
     *
     * @Author: Kuang Bin
     * @DateTime: 2019/10/19
     */
    public int addHandlecItem(Map<String, Object> param) {
        param.put("status", "1");
        param.put("addtime", System.currentTimeMillis());
        param.put("planid", JzbRandom.getRandomCharCap(11));
        return tbHandleMapper.insertHandlecItem(param);
    }

    /**
     * CRM-销售业主-公海-业主下的人员15
     * 点击业主/项目/跟进信息中点击修改跟进信息
     *
     * @Author: Kuang Bin
     * @DateTime: 2019/10/19
     */
    public int modifyHandlecItem(Map<String, Object> param) {
        param.put("updtime", System.currentTimeMillis());
        return tbHandleMapper.updateHandlecItem(param);
    }
}
