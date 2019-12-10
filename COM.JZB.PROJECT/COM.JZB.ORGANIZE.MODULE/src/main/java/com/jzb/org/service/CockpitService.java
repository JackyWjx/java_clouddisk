package com.jzb.org.service;

import com.jzb.base.util.JzbTools;
import com.jzb.org.dao.CockpitMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author chenhui
 * @description
 * @time 2019/12/6
 * @other
 */
@Service
public class CockpitService {
    @Autowired
    CockpitMapper cockpitMapper;

    /**
     * 驾驶舱/联系客户-查询
     * @param param
     * @return
     */
    public int getInfo(Map<String, Object> param) {
        if(JzbTools.isEmpty(param.get("customer"))){
            param.put("customer",param.get("adduid"));
        }
        return cockpitMapper.getInfo(param);
    }

    /**
     * 查询意向数目
     * @param param
     * @return
     */
    public List<Map<String, Object>> getHandleCount(Map<String, Object> param) {
        Map<String, Object> map = methodTime(System.currentTimeMillis());
        if (!JzbTools.isEmpty(param.get("startTime")) || !JzbTools.isEmpty(param.get("endTime"))){
            param.put("zero",param.get("startTime"));
            param.put("twelve",param.get("endTime"));
        }else {
            param.putAll(map);
        }
        param.put("trackres",1);
        // 愿意见
        int willCount = cockpitMapper.getHandleCount(param);

        param.put("trackres",2);
        // 深度见
        int deepCount = cockpitMapper.getHandleCount(param);

        param.put("trackres",4);
        // 上会
        int meetCount = cockpitMapper.getHandleCount(param);

        param.put("trackres",8);
        // 上会
        int signCount = cockpitMapper.getHandleCount(param);
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
