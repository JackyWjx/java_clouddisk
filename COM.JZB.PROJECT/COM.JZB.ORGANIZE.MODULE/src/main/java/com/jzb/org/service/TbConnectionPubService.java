package com.jzb.org.service;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.org.dao.TbConnectionPubMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author chenhui
 * @description
 * @time2019/11/26
 * @other
 */
@Service
public class TbConnectionPubService {
    @Autowired
    TbConnectionPubMapper pubMapper;

    // 查询发帖总数
    public int getConnectionCount(Map<String, Object> param) {
        return pubMapper.getConnectionCount(param);
    }

    // 查询发帖信息
    public List<Map<String, Object>> getConnectionList(Map<String, Object> param) {
        if (!JzbTools.isEmpty(param.get("type"))){
            param.put("type",JzbDataType.getInteger(param.get("type")));
        }
        param.put("status",'1');
        return pubMapper.getConnectionList(param);
    }
    // 修改发帖信息
    public int modifyConnectionList(Map<String, Object> param) {
        param.put("updtime",System.currentTimeMillis());
        return pubMapper.modifyConnectionList(param);
    }
    // 新建发帖信息
    public int insertConnectionList(Map<String, Object> param) {
        param.put("type",JzbDataType.getInteger(param.get("type")));
        param.put("status",'1');
        param.put("addtime",System.currentTimeMillis());
        param.put("pubid", JzbRandom.getRandomCharCap(7));
        return pubMapper.insertConnectionList(param);
    }

    // 删除发帖信息
    public int removeConnectionList(Map<String, Object> param) {
        param.put("status",'2');
        param.put("updtime",System.currentTimeMillis());
        return pubMapper.removeConnectionList(param);
    }
    // 新建任务目标参数
    public int insertTask(Map<String, Object> param) {
        param.put("taskid",JzbRandom.getRandomCharCap(7));
        param.put("addtime",System.currentTimeMillis());
        param.put("target",JzbDataType.getInteger(param.get("target")));
        param.put("status",'1');
        return pubMapper.insertTask(param);
    }
    // 查询任务目标参数
    public List<Map<String, Object>> getTask(Map<String, Object> param) {
        return pubMapper.getTask(param);
    }

    public int modifyTask(Map<String, Object> param) {
        param.put("updtime",System.currentTimeMillis());
        return pubMapper.modifyTask(param);
    }


    public List<Map<String, Object>> getBaiduInfo(Map<String, Object> param) {
        List<Map<String,Object>> mapList = new ArrayList<>();
        param.put("type",1);
        // 获取历史百度发帖数量
        int hisCount = pubMapper.getBaiDuCount(param);

        // 获取当日百度发帖数量
        Map<String, Object> cmap = methodTime(System.currentTimeMillis());
        param.putAll(cmap);
        int currCount = pubMapper.getBaiDuCount(param);
        // 设置每日任务目标
        int target = 0;
        List<Map<String, Object>> list = pubMapper.getTask(param);
        long addtime = 0;
        for (Map<String, Object> map : list) {
            if ("百度发贴".equals(map.get("tname"))){
                target = (int) map.get("target");
                addtime = (long) map.get("addtime");
            }
        }

        Map<String, Object> baiduMap = methodCount(addtime, target, hisCount, currCount);
        Map<String,Object> map1 = new HashMap<>();
        map1.put("baidu",baiduMap);
        mapList.add(map1);

        //朋友圈
        param.remove("zero");
        param.remove("twelve");
        param.put("type",2);
        // 获取历史朋友圈发帖数量
        hisCount = pubMapper.getBaiDuCount(param);

        // 获取当日朋友圈发帖数量
        Map<String, Object> pmap = methodTime(System.currentTimeMillis());
        param.putAll(pmap);
        currCount = pubMapper.getBaiDuCount(param);
        for (Map<String, Object> map : list) {
            if ("发朋友圈".equals(map.get("tname"))){
                target = (int) map.get("target");
                addtime = (long) map.get("addtime");
            }
        }
        Map<String, Object> weChatMap = methodCount(addtime, target, hisCount, currCount);
        Map<String,Object> map2 = new HashMap<>();
        map2.put("weChat",weChatMap);
        mapList.add(map2);


        return mapList;
    }


    public Map<String,Object> methodTime(Long current){
        Map<String ,Object> map = new HashMap<>();
        long zero=current/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
        long twelve=zero+24*60*60*1000-1;//今天23点59分59秒的毫秒数
        map.put("zero",zero);
        map.put("twelve",twelve);
        return map;
    }
    public Map<String,Object> methodCount(long addtime,int target,int hisCount,int currCount){
        Map<String,Object> map = new HashMap<>();
        // 获取历史天数
        int count = (int) ((System.currentTimeMillis() - addtime) / 86400000);
        int dayAmount = count == 0 ? 1:count;
        // 历史未完成数量
        int missCount = dayAmount * target - hisCount;
        map.put("target",target);
        map.put("currCount",currCount);
        map.put("missCount",missCount);
        return map;
    }

}
