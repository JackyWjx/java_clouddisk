package com.jzb.org.service;

import com.alibaba.fastjson.JSON;
import com.jzb.base.data.JzbDataType;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.org.dao.TbConnectionPubMapper;
import com.jzb.org.dao.TbTrackUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author chenhui
 * @description
 * @time2019/11/25
 * @other
 */
@Service
public class TbTrackUserService {
    @Autowired
    TbTrackUserMapper userMapper;

    @Autowired
    TbConnectionPubMapper pubMapper;

    // 新建跟进人员记录
    public int addTrackUser(Map<String, Object> param) {
        param.put("addtime",System.currentTimeMillis());
        param.put("tracktime",JzbDataType.getLong(param.get("tracktime")));
        param.put("trackid", JzbRandom.getRandomCharCap(17));
        param.put("customer",JzbRandom.getRandomCharCap(12));
        param.put("status",'1');
        param.put("image",param.get("image").toString());
        return userMapper.addTrackUser(param);
    }
    // 查询跟进人员记录总数
    public int getTrackCount(Map<String, Object> param) {
        return userMapper.getTrackCount(param);
    }
    // 查询跟进人员记录信息
    public List<Map<String, Object>> queryTrackUserList(Map<String, Object> param) {

        return userMapper.queryTrackUserList(param);
    }

    // 删除跟进人员记录信息
    public int delTrackUser(Map<String, Object> param) {
        param.put("updtime",System.currentTimeMillis());
        return userMapper.delTrackUser(param);
    }

    // 修改跟进人员记录信息
    public int updTrackUser(Map<String, Object> param) {
        param.put("upduid",param.get("adduid"));
        param.put("updtime",System.currentTimeMillis());
        return userMapper.updTrackUser(param);
    }

    // 查询联系沟通数量
    public List<Map<String, Object>> getInfo(Map<String, Object> param) {
        List<Map<String,Object>> mapList = new ArrayList<>();
        // 获取历史QQ/微信沟通数量
        int hisCount = userMapper.getQCount(param);

        // 获取当日QQ/微信沟通数量
        Map<String, Object> cmap = methodTime(System.currentTimeMillis());
        param.putAll(cmap);
        int currCount = userMapper.getQCount(param);
        // 设置每日任务目标
        int target = 0;
        List<Map<String, Object>> list = pubMapper.getTask(param);
        long addtime = 0;
        for (Map<String, Object> map : list) {
            if ("QQ/微信沟通".equals(map.get("tname"))){
                target = (int) map.get("target");
                addtime = (long) map.get("addtime");
            }
        }

        Map<String, Object> qMap = methodCount(addtime, target, hisCount, currCount);
        Map<String,Object> map1 = new HashMap<>();
        map1.put("QQ",qMap);
        mapList.add(map1);

        //电话沟通
        param.remove("zero");
        param.remove("twelve");
        param.put("type",2);
        // 获取历史电话沟通数量
        hisCount = userMapper.getPCount(param);

        // 获取当日电话沟通数量
        Map<String, Object> pmap = methodTime(System.currentTimeMillis());
        param.putAll(pmap);
        currCount = userMapper.getPCount(param);
        for (Map<String, Object> map : list) {
            if ("电话沟通".equals(map.get("tname"))){
                target = (int) map.get("target");
                addtime = (long) map.get("addtime");
            }
        }
        Map<String, Object> pMap = methodCount(addtime, target, hisCount, currCount);
        Map<String,Object> map2 = new HashMap<>();
        map2.put("phone",pMap);
        mapList.add(map2);
        return mapList;
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
        int willCount = userMapper.getHandleCount(param);

        param.put("handlestage",2);
        // 深度见
        int deepCount = userMapper.getHandleCount(param);

        param.put("handlestage",4);
        // 上会
        int meetCount = userMapper.getHandleCount(param);

        param.put("handlestage",8);
        // 上会
        int signCount = userMapper.getHandleCount(param);
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
