package com.jzb.operate.service;

import com.jzb.base.util.JzbTools;
import com.jzb.operate.dao.TbScoreListMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * @描述
 * @创建人 chenhui
 * @创建时间 2019/11/21
 * @修改人和其它信息
 */
@Service
public class TbScoreListService {

    @Autowired
    TbScoreListMapper listMapper;

    public int addScoreList(Map<String, Object> param) {
        int count = 0;
        Map<String, Object> map = methodTime(System.currentTimeMillis());
        param.putAll(map);
        param.put("updtime",System.currentTimeMillis());
        param.put("status",'2');
        // 判断当日是否进行过该操作
        Map<String, Object> timeMap = listMapper.getTimeByOptid(param);
        if (JzbTools.isEmpty(timeMap)){
                count = listMapper.addScoreList(param);
            }

//        // 判断是否为登录操作
//        if (!JzbTools.isEmpty(param.get("optid")) && "LOGIN".equals(param.get("optid"))){
//            // 查询在当天是否进行过登录
//            Map<String, Object> timeMap = listMapper.getTimeByOptid(param);
//            if (JzbTools.isEmpty(timeMap.get("updtime"))){
//                count = listMapper.addScoreList(param);
//            }
//        }
//
//        // 判断是否为发布文章操作
//        if (!JzbTools.isEmpty(param.get("optid")) && "ARTPUB".equals(param.get("optid"))){
//            // 查询当日是否进行过发表
//            Map<String, Object> timeMap = listMapper.getTimeByOptid(param);
//            if (JzbTools.isEmpty(timeMap.get("updtime"))){
//                count = listMapper.addScoreList(param);
//            }
//        }
//
//        // 判断是否为培训活动操作
//        if (!JzbTools.isEmpty(param.get("optid")) && "ACTPUB".equals(param.get("optid"))){
//            // 查询当日是否进行过发表
//            Map<String, Object> timeMap = listMapper.getTimeByOptid(param);
//            if (JzbTools.isEmpty(timeMap.get("updtime"))){
//                count = listMapper.addScoreList(param);
//            }
//        }
//




        return count;
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
