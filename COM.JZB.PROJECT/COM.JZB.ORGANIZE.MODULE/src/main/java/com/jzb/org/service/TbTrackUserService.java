package com.jzb.org.service;

import com.alibaba.fastjson.JSON;
import com.jzb.base.data.JzbDataType;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.org.dao.CockpitMapper;
import com.jzb.org.dao.TbConnectionPubMapper;
import com.jzb.org.dao.TbTrackUserMapper;
import jdk.nashorn.internal.scripts.JD;
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
    CockpitMapper cockpitMapper;


    @Autowired
    TbTrackUserMapper userMapper;

    @Autowired
    TbConnectionPubMapper pubMapper;

    // 新建跟进人员记录
    public int  addTrackUser(Map<String, Object> param) {
        param.put("addtime",System.currentTimeMillis());
        param.put("tracktype",JzbDataType.getInteger(param.get("tracktype")));
        param.put("trackid", JzbRandom.getRandomCharCap(17));
        if (JzbTools.isEmpty(param.get("trackuid"))){
            param.put("trackuid",param.get("adduid"));
        }
        param.put("status",'1');
        if (!JzbTools.isEmpty(param.get("image"))){
            param.put("image",param.get("image").toString());
        }
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
    // 销售统计查询跟进人员信息
    public List<Map<String, Object>> queryTrackUserListOnSales(Map<String, Object> param) {
        if (!JzbTools.isEmpty(param.get("cdid"))){
            List<Map<String,Object>> deptChildlist = cockpitMapper.getDeptChild(param);
            for (int i = 0; i < deptChildlist.size(); i++) {
                deptChildlist.get(i).remove("pcdid");
                deptChildlist.get(i).remove("idx");
            }
            param.put("list",deptChildlist);
        }
        List<Map<String, Object>> list = userMapper.queryTrackUserList(param);
        for (int i = 0; i < list.size(); i++) {
            String trackcid = JzbDataType.getString(list.get(i).get("trackcid"));
            param.put("trackcid",trackcid);
            String level = userMapper.queryLevel(param);
            if ( !JzbTools.isEmpty(level) && level.length() < 5 ){
                list.get(i).put("level",level);
            }else {
                list.get(i).put("level",null);
            }
        }

        return list;
    }

    // 删除跟进人员记录信息
    public int delTrackUser(Map<String, Object> param) {

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
        long updtime = 0;
        for (Map<String, Object> map : list) {
            if ("QQ/微信沟通".equals(map.get("tname"))){
                target = (int) map.get("target");
                updtime = (long) map.get("updtime");
            }
        }

        Map<String, Object> qMap = methodCount(updtime, target, hisCount, currCount);
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
                updtime = (long) map.get("updtime");
            }
        }
        Map<String, Object> pMap = methodCount(updtime, target, hisCount, currCount);
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
        if (JzbTools.isEmpty(param.get("startTime")) && JzbTools.isEmpty(param.get("endTime")) ){
            Map<String, Object> map = methodTime(System.currentTimeMillis());
            param.putAll(map);
        }else {
            param.put("zero",param.get("startTime"));
            param.put("twelve",param.get("endTime"));
        }
        if (JzbTools.isEmpty(param.get("customer")) &&
                JzbTools.isEmpty(param.get("cdid")) &&
                JzbTools.isEmpty(param.get("cid")) && JzbTools.isEmpty(param.get("manager"))){
            param.put("customer",param.get("adduid"));
        }
        if (!JzbTools.isEmpty(param.get("cdid"))){
            List<Map<String,Object>> list = cockpitMapper.getDeptChild(param);
            for (int i = 0; i < list.size(); i++) {
                list.get(i).remove("pcdid");
                list.get(i).remove("idx");
            }
            param.put("list",list);
        }
        param.put("trackres",'1');
        // 愿意见
        int willCount = userMapper.getHandleCount(param);

        param.put("trackres",'2');
        // 深度见
        int deepCount = userMapper.getHandleCount(param);

        param.put("trackres",'4');
        // 上会
        int meetCount = userMapper.getHandleCount(param);

        param.put("trackres",'8');
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

    // 分别查询qq/微信/电话沟通数量
    public List<Map<String, Object>> getSingleCount(Map<String, Object> param) {
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> cmap = new HashMap<>();

        param.put("tracktype",2);
        cmap.put("朋友圈",userMapper.getSingleCount(param));

        param.put("tracktype",4);
        cmap.put("QQ",userMapper.getSingleCount(param));

        param.put("tracktype",8);
        cmap.put("微信",userMapper.getSingleCount(param));


        list.add(cmap);
        return list;
    }

    public List<Map<String, Object>> getContactList(Map<String, Object> param) {
        if (JzbTools.isEmpty(param.get("startTime")) && JzbTools.isEmpty(param.get("endTime")) ){
            Map<String, Object> map = methodTime(System.currentTimeMillis());
            param.putAll(map);
        }else {
            param.put("zero",param.get("startTime"));
            param.put("twelve",param.get("endTime"));
        }
        if (JzbTools.isEmpty(param.get("customer"))){
            param.put("customer",param.get("adduid"));
        }

        if (JzbTools.isEmpty(param.get("customer")) &&
                JzbTools.isEmpty(param.get("cdid")) &&
                JzbTools.isEmpty(param.get("cid")) && JzbTools.isEmpty(param.get("manager"))){
            param.put("customer",param.get("adduid"));
        }
        if (!JzbTools.isEmpty(param.get("cid"))){
            List<Map<String,Object>> list = cockpitMapper.getDeptChild(param);
            for (int i = 0; i < list.size(); i++) {
                list.get(i).remove("pcdid");
                list.get(i).remove("idx");
            }
            param.put("list",list);
        }
        return userMapper.getContactList(param);
    }

    // 查询有效客户
    public List<Map<String, Object>> getClient(Map<String, Object> param) {
        List<Map<String,Object>> mapList = new ArrayList<>();
        // 获取历史有效客户数量
        int hisCount = userMapper.getClient(param);

        // 获取当日有效数量
        Map<String, Object> cmap = methodTime(System.currentTimeMillis());
        param.putAll(cmap);
        int currCount = userMapper.getClient(param);
        // 设置每日任务目标
        int target = 0;
        List<Map<String, Object>> list = pubMapper.getTask(param);
        long updtime = 0;
        for (Map<String, Object> map : list) {
            if ("有效客户".equals(map.get("tname"))){
                target = (int) map.get("target");
                updtime = (long) map.get("updtime");
            }
        }

        Map<String, Object> qMap = methodCount(updtime, target, hisCount, currCount);
        Map<String,Object> map1 = new HashMap<>();
        map1.put("userClient",qMap);
        mapList.add(map1);

        return mapList;
    }

    // 根据跟进人查询 跟进阶段客户列表
    public List<Map<String, Object>> getHandleStage(Map<String, Object> param) {
        if (JzbTools.isEmpty(param.get("startTime")) && JzbTools.isEmpty(param.get("endTime")) ){
            Map<String, Object> map = methodTime(System.currentTimeMillis());
            param.putAll(map);
        }else {
            param.put("zero",param.get("startTime"));
            param.put("twelve",param.get("endTime"));
        }
        if (JzbTools.isEmpty(param.get("customer")) &&
                JzbTools.isEmpty(param.get("cdid")) &&
                JzbTools.isEmpty(param.get("cid")) && JzbTools.isEmpty(param.get("manager"))){
            param.put("customer",param.get("adduid"));
        }
        if (!JzbTools.isEmpty(param.get("cdid"))){
            List<Map<String,Object>> list = cockpitMapper.getDeptChild(param);
            for (int i = 0; i < list.size(); i++) {
                list.get(i).remove("pcdid");
                list.get(i).remove("idx");
            }
            param.put("list",list);
        }
        param.put("trackres",JzbDataType.getString(param.get("trackres")));
        return userMapper.getHandleStage(param);
    }


    public int getTrackCountOnSales(Map<String, Object> param) {
        if (!JzbTools.isEmpty(param.get("cdid"))){
            List<Map<String,Object>> list = cockpitMapper.getDeptChild(param);
            for (int i = 0; i < list.size(); i++) {
                list.get(i).remove("pcdid");
                list.get(i).remove("idx");
            }
            param.put("list",list);
        }
        return userMapper.getTrackCountOnSales(param);
    }
}
