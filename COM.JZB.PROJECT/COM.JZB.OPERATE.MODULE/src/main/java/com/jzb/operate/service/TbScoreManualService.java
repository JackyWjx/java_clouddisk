package com.jzb.operate.service;

import com.jzb.base.util.JzbRandom;
import com.jzb.operate.dao.TbScoreManualMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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
}
