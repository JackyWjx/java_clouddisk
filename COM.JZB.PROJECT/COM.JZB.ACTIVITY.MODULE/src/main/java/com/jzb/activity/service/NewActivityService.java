package com.jzb.activity.service;

import com.jzb.activity.api.redis.UserRedisApi;
import com.jzb.activity.dao.ActivityMapper;
import com.jzb.base.data.JzbDataType;
import com.jzb.base.data.date.JzbDateStr;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import org.apache.poi.ss.formula.ptg.Pxg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class NewActivityService {

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private UserRedisApi userRedisApi;

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

    /**
     * CRM-运营管理-活动-文章列表
     * 点击文章列表显示所有的文章列表总数
     *
     * @author kuangbin
     */
    public int getActivityListCount(Map<String, Object> param) {
        int count;
        try {
            count = activityMapper.queryActivityListCount(param);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            count = 0;
        }
        return count;
    }

    /**
     * CRM-运营管理-活动-文章列表
     * 点击文章列表显示所有的文章列表
     *
     * @author kuangbin
     */
    public List<Map<String, Object>> getActivityList(Map<String, Object> param) {
        // 设置分页参数
        param = setPageSize(param);
        List<Map<String, Object>> list = activityMapper.queryActivityListData(param);
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> advertMap = list.get(i);
            // 获取企业管理员用户信息
            String uid = JzbDataType.getString(advertMap.get("uid"));
            param.put("uid", uid);
            // 从缓存中获取用户信息
            Response response = userRedisApi.getCacheUserInfo(param);
            advertMap.put("uid", response.getResponseEntity());
        }
        return list;
    }

    /**
     * 设置分页数
     */
    public Map<String, Object> setPageSize(Map<String, Object> param) {
        int pageno = JzbDataType.getInteger(param.get("pageno"));
        int pagesize = JzbDataType.getInteger(param.get("pagesize"));
        pagesize = pagesize <= 0 ? 15 : pagesize;
        pageno = pageno <= 0 ? 1 : pageno;
        param.put("pageno", (pageno - 1) * pagesize);
        param.put("pagesize", pagesize);
        return param;
    }
}
