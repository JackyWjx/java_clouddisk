package com.jzb.activity.service;

import com.jzb.activity.dao.ActivityMapper;
import com.jzb.base.data.date.JzbDateStr;
import com.jzb.base.util.JzbRandom;
import org.apache.poi.ss.formula.ptg.Pxg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class NewActivityService {

    @Autowired
    private ActivityMapper activityMapper;

    /**
     * 获取详情
     * @param param
     * @return
     */
    public List<Map<String ,Object>> getActivityDesc(Map<String ,Object> param){
        return activityMapper.getDiscussById(param);
    }

    /**
     * 分页查询 支显示3条信息
     * @param params
     * @return List<Map<String, Object>> 返回list数据
     */
    public List<Map<String, Object>> findParticularsList(Map<String, Object> params){
        return activityMapper.findParticularsList(params);
    }

    /**
     * 分页查询详情页面
     * @param params
     * @return List<Map<String, Object>> 返回list数据
     */
    public List<Map<String, Object>> findParticularsByList(Map<String, Object> params){
        return activityMapper.findParticularsByList(params);
    }

    /**
     * 查询总数
     * @param params
     * @return
     */
    public int findParticularsListCount(Map<String, Object> params){
        return activityMapper.findParticularsListCount(params);
    }

    /**
     * 查询总数
     * @param params
     * @return
     */
    public int addActivityDucess(Map<String, Object> params){
        Long addtime=System.currentTimeMillis();
        params.put("addtime",addtime);
        params.put("disid", params.get("actid").toString()+JzbRandom.getRandomCharCap(6));
        params.put("status",1);
        return activityMapper.addActivityDiscuss(params);
    }

    /**
     * 无登录点赞
     * @param param
     * @return
     */
    public int updateActivityVotes(Map<String, Object> param){
        return activityMapper.updateActivityVotes(param);
    }

    /**
     * 无登录点赞
     * @param param
     * @return
     */
    public int updateActivityReads(Map<String, Object> param){
        return activityMapper.updateActivityReads(param);
    }

    /**
     * 查询点赞次数浏览次数
     * @param param
     * @return
     */
    public  Map<String, Object> getVotesViews(Map<String, Object> param){return  activityMapper.queryActivityCountNew(param);}

    /**
     * 更新评论数
     * @param param
     * @return
     */
    public int updateComment(Map<String, Object> param){
        return activityMapper.updateComment(param);
    }
}
