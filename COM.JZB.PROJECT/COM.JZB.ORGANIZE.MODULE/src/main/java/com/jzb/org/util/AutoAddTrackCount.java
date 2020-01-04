package com.jzb.org.util;

import com.jzb.org.dao.CockpitMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * @author chenhui
 * @description
 * @time 2020/1/4
 * @other
 */
@Component
@EnableScheduling
public class AutoAddTrackCount {

    @Autowired
    CockpitMapper cockpitMapper;

    
    @Scheduled(cron = "0 15 03 ? * *") //每天上午3：15触发
    public void testimer() {
        Map<String,Object> param = new HashMap<>();
        param.put("cid","JZB0001");
        List<Map<String, Object>> userList = cockpitMapper.getAllUserByCid(param);
        param.remove("cid");
        for (int i = 0; i < userList.size(); i++) {
            Map<String, Object> map = methodTime(System.currentTimeMillis());
            map.put("trackuid",userList.get(i).get("uid"));
            Map<String,Object> umap = cockpitMapper.getAllTrackInfo(map).get(0);
            umap.put("cid",userList.get(i).get("cid"));
            umap.put("trackuid",userList.get(i).get("uid"));
            umap.put("cdid",userList.get(i).get("cdid"));
            umap.put("addtime",System.currentTimeMillis());
            umap.put("trackuname",userList.get(i).get("cname"));
            cockpitMapper.addTbtrackCount(umap);
        }

    }
    public Map<String,Object> methodTime(Long current){
        Map<String ,Object> map = new HashMap<>();
        long zero=current/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset() - 86400000;//昨天零点零分零秒的毫秒数
        long twelve=zero+24*60*60*1000-1 - 86400000;//昨天天23点59分59秒的毫秒数
        map.put("zero",zero);
        map.put("twelve",twelve);
        return map;
    }

}
