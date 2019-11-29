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

import java.util.*;

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
        param = setPageSize(param);
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

    /**
     * 设置分页数
     */
    public Map<String, Object> setPageSize(Map<String, Object> param) {
        int pageno = JzbDataType.getInteger(param.get("pageno"));
        int pagesize = JzbDataType.getInteger(param.get("pagesize"));
        pagesize = pagesize <= 0 ? 15 : pagesize;
        pageno = pageno <= 0 ? 1 : pageno;
        param.put("pageno", (pageno - 1) * pagesize);
        param.put("pagesize", pagesize);
        return param;
    }

    /**
     * 查询意向数目
     * @param param
     * @return
     */
    public List<Map<String, Object>> getHandleCount(Map<String, Object> param) {
        param.put("handlestage",1);
        Map<String, Object> map = methodTime(System.currentTimeMillis());
        param.putAll(map);
        // 愿意见
        int willCount = tbHandleMapper.getHandleCount(param);

        param.put("handlestage",2);
        // 深度见
        int deepCount = tbHandleMapper.getHandleCount(param);

        param.put("handlestage",3);
        // 上会
        int meetCount = tbHandleMapper.getHandleCount(param);

        param.put("handlestage",4);
        // 上会
        int signCount = tbHandleMapper.getHandleCount(param);
        Map<String,Object> cmap = new HashMap<>();
        cmap.put("willCount",willCount);
        cmap.put("deepCount",deepCount);
        cmap.put("meetCount",meetCount);
        cmap.put("signCount",signCount);
        List<Map<String,Object>> list = new ArrayList<>();
        list.add(cmap);
        return list;
    }

    public Map<String,Object> methodTime(Long current){
        Map<String ,Object> map = new HashMap<>();
        long zero=current/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
        long twelve=zero+24*60*60*1000-1;//今天23点59分59秒的毫秒数
        map.put("zero",zero);
        map.put("twelve",twelve);
        return map;
    }
}
