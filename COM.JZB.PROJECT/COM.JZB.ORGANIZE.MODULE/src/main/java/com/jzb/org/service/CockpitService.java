package com.jzb.org.service;

import com.alibaba.fastjson.serializer.MapSerializer;
import com.jzb.base.data.JzbDataType;
import com.jzb.base.util.JzbTools;
import com.jzb.org.dao.CockpitMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        if (JzbTools.isEmpty(param.get("startTime")) && JzbTools.isEmpty(param.get("endTime")) ){
            Map<String, Object> map = methodTime(System.currentTimeMillis());
            param.putAll(map);
        }else {
            param.put("zero",param.get("startTime"));
            param.put("twelve",param.get("endTime"));
        }
        if (JzbTools.isEmpty(param.get("trackuid"))){
            param.put("trackuid",param.get("adduid"));
        }
        if (JzbTools.isEmpty(param.get("trackuid")) &&
                JzbTools.isEmpty(param.get("cdid")) &&
                JzbTools.isEmpty(param.get("cid")) && JzbTools.isEmpty(param.get("manager"))){
            param.put("trackuid",param.get("adduid"));
        }
        if (!JzbTools.isEmpty(param.get("cid"))){
            List<Map<String,Object>> list = cockpitMapper.getDeptChild(param);
            for (int i = 0; i < list.size(); i++) {
                list.get(i).remove("pcdid");
                list.get(i).remove("idx");
            }
            param.put("list",list);
        }
        return cockpitMapper.getInfo(param);
    }

    /**
     * 查询意向数目
     * @param param
     * @return
     */
    public List<Map<String, Object>> getHandleCount(Map<String, Object> param) {
        int willCount = 0;
        int deepCount = 0;
        int signCount = 0;
        int meetCount = 0;
        Map<String, Object> map = methodTime(System.currentTimeMillis());
        if (!JzbTools.isEmpty(param.get("startTime")) || !JzbTools.isEmpty(param.get("endTime"))){
            param.put("zero",param.get("startTime"));
            param.put("twelve",param.get("endTime"));
        }else {
            param.putAll(map);
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
            param.put("trackres", 1);
            // 愿意见
             willCount = cockpitMapper.getHandleCount(param);

            param.put("trackres", 2);
            // 深度见
             deepCount = cockpitMapper.getHandleCount(param);

            param.put("trackres", 4);
            // 上会
             meetCount = cockpitMapper.getHandleCount(param);

            param.put("trackres", 8);
            // 上会
             signCount = cockpitMapper.getHandleCount(param);

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

    // 查询企业认证级别个数
    public List<Map<String, Object>> getComAuthCount(Map<String, Object> param) {
        return cockpitMapper.getComAuthCount(param);
    }

    public List<Map<String, Object>> getAllDeptUser(Map<String, Object> param) {
        // 获取部门下的子级部门
        List<Map<String, Object>> list = cockpitMapper.getDeptChild(param);
        param.put("list",list);
        return cockpitMapper.getAllDeptUser(param);
    }

    public List<Map<String, Object>> getAllTrackInfo(Map<String, Object> param) {
        List<Map<String ,Object>> list = new ArrayList<>();
        List<Map<String,Object>> fList = new ArrayList<>();
        if (!JzbTools.isEmpty(param.get("cdid"))){
            List<Map<String, Object>> cdidlist = cockpitMapper.getDeptChild(param);
            param.put("list",cdidlist);
        }
        // 判断是否按天数查询
        if ("1".equals(JzbDataType.getString(param.get("type")))){
            int days = getDays(JzbDataType.getString(param.get("starttime")));
            List<Map<String, Object>> daysList = getStartAndEndTime(JzbDataType.getString(param.get("starttime")),days);
            for (int i = 0; i < daysList.size(); i++) {
                param.put("starttime",JzbDataType.getLong(daysList.get(i).get("starttime")));
                param.put("endtime",JzbDataType.getLong(daysList.get(i).get("endtime")));
                List<Map<String, Object>> allDeptUser = cockpitMapper.getAllTrackInfo(param);
                Map<String,Object> map = new HashMap<>();
                map.put(JzbDataType.getString(param.get("starttime")),allDeptUser.get(0));
                list.add(map);
                map = new HashMap<>();
            }
//            Map<String,Object> fMap = new HashMap<>();
//            for (int i = 0; i < list.size(); i++) {
//                fMap.put(JzbDataType.getString(list.get(i).get("starttime")),list.get(i));
//                fList.add(fMap);
//                fMap = new HashMap<>();
//            }
        }

        // 按 周查询
        if ("2".equals(JzbDataType.getString(param.get("type")))){
            int days = getDays(JzbDataType.getString(param.get("starttime")));
            List<Map<String, Object>> weekList = getWeekCount(JzbDataType.getString(param.get("starttime")), days);
            Map<String, Object> wMap = new HashMap<>();
            for (int i = 0; i < weekList.size(); i++) {
                param.put("starttime",weekList.get(i).get("starttime"));
                param.put("endtime",weekList.get(i).get("endtime"));
                List<Map<String, Object>> wList = cockpitMapper.getAllTrackInfo(param);
                wMap.put(JzbDataType.getString(i+1),wList.get(0));
                list.add(wMap);
            }
        }
        return list;
    }
    /**
     * @Author Reed
     * @Description 根据时间戳获取每天的时间戳
     * @Date 18:03 2020/1/3
     * @Param
     * @return
    **/
    public static List<Map<String,Object>> getStartAndEndTime(String time,int days) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<Map<String,Object>> list = null;
                Date start = null;
        try {
            Calendar cal = Calendar.getInstance();
            start = df.parse(time);
            cal.setTime(start);
            long starttime = cal.getTimeInMillis();
            long endtime = 0;
            list = new ArrayList<>();
            Map<String,Object> map = new HashMap<>();
            for (int i = 0; i < days; i++) {
                endtime = starttime + 86400000;
                map.put("starttime",starttime);
                map.put("endtime",endtime);
                starttime += 86400000;
                list.add(map);
                map = new HashMap<>();
            }
        } catch (ParseException e) {
            JzbTools.logError(e);
        }

        return list;
    }
    public static int getDays(String time) {
        String str = time;
        String[] s = str.split(" ");
        String strA = s[0];
        int  year = JzbDataType.getInteger(strA.substring(0,4));
        int  month = JzbDataType.getInteger(strA.substring(5,7));
        int days = 0;
        boolean isLeapYear = false;
        if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)) {
            System.out.println("--------------------闰年-------------------");
            isLeapYear = true;
        } else {
            System.out.println("--------------------非闰年-------------------");
            isLeapYear = false;
        }
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                days = 31;
                break;
            case 2:
                if (isLeapYear) {
                    days = 29;
                } else {
                    days = 28;
                }
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                days = 30;
                break;
            default:
                System.out.println("error!!!");
                break;
        }
        return days;
    }

    public static List<Map<String,Object>> getWeekCount(String time,int days){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<Map<String ,Object>> list = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        Date start = null;
        try {
            Calendar cal = Calendar.getInstance();
            start = df.parse(time);
            cal.setTime(start);
            long starttime = cal.getTimeInMillis();
            long endtime = 0;
            int weeks = days / 7;
            int weeksend = days %7;
            for (int i = 0; i <= weeks ; i++) {
                if (i == weeks){
                    endtime = starttime + weeksend * 86400000;
                    map.put("starttime",starttime);
                    map.put("endtime",endtime);
                }else {
                    endtime = starttime + 7 * 86400000;
                    map.put("starttime",starttime);
                    map.put("endtime",endtime);
                    starttime += 7 * 86400000;
                }
                list.add(map);
                map = new HashMap<>();
            }
        } catch (ParseException e) {
            JzbTools.logError(e);
        }
        return list;
    }
}
