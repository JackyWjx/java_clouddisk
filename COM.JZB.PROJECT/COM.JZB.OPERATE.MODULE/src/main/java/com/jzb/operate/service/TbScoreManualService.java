package com.jzb.operate.service;

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
        long addtime = System.currentTimeMillis();
        String ruleid = JzbRandom.getRandomCharCap(7);
        paramp.put("ruleid",ruleid);
        paramp.put("addtime",addtime);
        paramp.put("status","1");
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
        List<Map<String, Object>> maps = scoreManual.queryScoreLog(paramp);
        paramp.put("optid","123");
        // 获取登录时间
        List<Map<String, Object>> loginTime = scoreManual.queryLogin(paramp);

        // 登录累计天数
        int day = 0;
        for (int i = 1; i <= loginTime.size() ; i++) {
            boolean flag = method((Long) loginTime.get(i-1).get("updtime"), i);
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
                map1.put("optid","222");
                map1.put("uid",paramp.get("uid"));
                map1.put("updtime",updtime);
                map1.put("status","2");
                int count = scoreManual.addScoreRuleLog(map1);
            } catch (Exception e) {
                JzbTools.logError(e);
            }
        }
        return scoreManual.queryScoreLog(paramp);
    }

    public boolean method(Long current,int day){

        long zero=current/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
        long twelve=zero+24*60*60*1000-1;//今天23点59分59秒的毫秒数
        return current >= zero  && current <= twelve;

    }

    // 领取积分
    public int modifyStatus(Map<String, Object> paramp) {
        paramp.put("status","1");
        Long updtime = JzbDataType.getLong(paramp.get("updtime"));
        paramp.put("updtime",updtime);
        return scoreManual.modifyStatus(paramp);
    }
}
