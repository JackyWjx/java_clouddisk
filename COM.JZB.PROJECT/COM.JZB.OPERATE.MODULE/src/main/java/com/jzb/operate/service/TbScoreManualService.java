package com.jzb.operate.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jzb.base.data.JzbDataType;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.operate.dao.TbScoreManualMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
        *@描述
        *@创建人
        *@创建时间 2019/11/9
        *@修改人和其它信息
*/
@Service
public class TbScoreManualService {
    @Autowired
    TbScoreManualMapper scoreManual;

    @Autowired
    TbScoreService scoreService;

    // 查询积分指导手册总数
    public Integer getCount(Map<String, Object> paramap) {
        return scoreManual.getCount(paramap);
    }

    public List<Map<String, Object>> getActivity(Map<String, Object> paramap) {
        System.out.println(paramap.toString());
        return scoreManual.queryScoreManualList(paramap);

    }


     // 查询积分规则总数
    public int getScoreRuleCount(Map<String, Object> paramp) {
        return scoreManual.getScoreRuleCount(paramp);
    }
    // 查询积分规则列表
    public List<Map<String, Object>> getScoreRule(Map<String, Object> paramp) {
        return scoreManual.queryScoreRule(paramp);
    }

    // 新建积分规则
    public int insertScoreRule(Map<String, Object> paramp) {
//        long addtime = System.currentTimeMillis();
        String ruleid = JzbRandom.getRandomCharCap(7);
        String optid = JzbRandom.getRandomCharCap(6);
        paramp.put("ruleid",ruleid);
//        paramp.put("addtime",addtime);
//        paramp.put("status","1");
        paramp.put("optid",optid);
        return scoreManual.addScoreRule(paramp);
    }
    // 删除一条积分规则
    public int delScoreRule(Map<String, Object> paramp) {
        paramp.put("status","4");
        return scoreManual.delScoreRule(paramp);
    }
    // 修改一条积分规则
    public int updScoreRule(Map<String, Object> paramp) {
        long updtime = System.currentTimeMillis();
        paramp.put("updtime",updtime);
        paramp.put("upduid",paramp.get("ouid"));
        return scoreManual.updScoreRule(paramp);
    }

    // 查询我的任务
    public List<Map<String, Object>> querySocre(Map<String, Object> paramp) {
        paramp.put("optid","LOGIN");
        // 获取登录时间
        List<Map<String, Object>> loginTime = scoreManual.queryLogin(paramp);

        // 登录累计天数
        int day = 0;
        for (int i = 1; i <= loginTime.size() ; i++) {
            boolean flag = method((Long) loginTime.get(i-1).get("updtime"));
            if (flag){
                day ++;
            }else {
                break;
            }
        }
        if (day == 5){
            // 积分日志表插入连续登录5天
            try {
                long updtime = System.currentTimeMillis();
                Map<String,Object> map1 = new HashMap<>();
                map1.put("optid","ELOGIN");
                map1.put("uid",paramp.get("uid"));
                map1.put("updtime",updtime);
                map1.put("status","2");
                int count = scoreManual.addScoreRuleLog(map1);
            } catch (Exception e) {
                JzbTools.logError(e);
            }
        }

        Map<String, Object> map = methodTime(System.currentTimeMillis());
        // 获取当日文章发布数量
        map.put("optid","ARTPUB");
        map.put("uid",paramp.get("uid"));
        int pubCount = scoreManual.getPubCount(map);
        // 获取当日活动发布数量
        map.put("optid","ACTPUB");
        int actCount = scoreManual.getPubCount(map);

        List<Map<String,Object>> list = scoreManual.queryScoreLog(paramp);
        for (int i = 0; i < list.size(); i++) {
            if ("ELOGIN".equals(list.get(i).get("optid") )){
                list.get(i).put("day",day);
            }
            if ("ARTPUB".equals(list.get(i).get("optid"))){
                list.get(i).put("count",pubCount);
                list.get(i).put("max",10);
            }
            if ("ACTPUB".equals(list.get(i).get("optid"))){
                list.get(i).put("count",actCount);
                list.get(i).put("max",10);
            }
        }
        return list;
    }

    public boolean method(Long current){

        long zero=current/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
        long twelve=zero+24*60*60*1000-1;//今天23点59分59秒的毫秒数
        return current >= zero  && current <= twelve;
    }

    public Map<String,Object> methodTime(Long current){
        Map<String ,Object> map = new HashMap<>();
        long zero=current/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
        long twelve=zero+24*60*60*1000-1;//今天23点59分59秒的毫秒数
        map.put("zero",zero);
        map.put("twelve",twelve);
        return map;
    }

         // 领取积分
    public int modifyStatus(Map<String, Object> paramp) {
        paramp.put("status","1");
        Long updtime = JzbDataType.getLong(paramp.get("updtime"));
        paramp.put("updtime",updtime);
        // 用户涨积分
        scoreService.saveUserIntegration(paramp);

        return scoreManual.modifyStatus(paramp);
    }

    // 查询消费明细总记录数
    public int getConsumeCount(Map<String, Object> parmp) {
        return scoreManual.getConsumeCount(parmp);
    }

    // 查询消费明细记录
    public List<Map<String, Object>> getConsumeList(Map<String, Object> parmp) {
        if (!JzbTools.isEmpty(parmp.get("endTime"))){
            parmp.put("endTime", JzbDataType.getLong(parmp.get("endTime")) + 86400);
        }

        return scoreManual.getConsumeList(parmp);
    }

    // 获取已完成任务总数
    public int getSucCount(Map<String, Object> paramp) {
        Map<String, Object> map = methodTime(System.currentTimeMillis());
        map.put("uid",paramp.get("uid"));

        return scoreManual.getSucCount(map);
    }

    public int queryTaskCount(Map<String, Object> paramp) {
        return scoreManual.queryTaskCount(paramp);
    }
}
