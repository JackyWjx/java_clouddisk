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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
     *
     * @param param
     * @return
     */
    public List<Map<String, Object>> getActivityDesc(Map<String, Object> param) {
        return activityMapper.getDiscussById(param);
    }


    /**
     * 获取详情
     *
     * @param param
     * @return
     */
    public List<Map<String, Object>> queryActivityPhoto(Map<String, Object> param) {
        return activityMapper.queryActivityPhoto(param);
    }

    /**
     * 分页查询 支显示3条信息
     *
     * @param params
     * @return List<Map < String, Object>> 返回list数据
     */
    public List<Map<String, Object>> findParticularsList(Map<String, Object> params) {
        return activityMapper.findParticularsList(params);
    }

    /**
     * 分页查询详情页面
     *
     * @param params
     * @return List<Map < String, Object>> 返回list数据
     */
    public List<Map<String, Object>> findParticularsByList(Map<String, Object> params) {
        return activityMapper.findParticularsByList(params);
    }

    /**
     * 查询总数
     *
     * @param params
     * @return
     */
    public int findParticularsListCount(Map<String, Object> params) {
        return activityMapper.findParticularsListCount(params);
    }

    /**
     * 查询总数
     *
     * @param params
     * @return
     */
    public int addActivityDucess(Map<String, Object> params) {
        Long addtime = System.currentTimeMillis();
        params.put("addtime", addtime);
        params.put("disid", params.get("actid").toString() + JzbRandom.getRandomCharCap(6));
        params.put("status", 1);
        return activityMapper.addActivityDiscuss(params);
    }

    /**
     * 无登录点赞
     *
     * @param param
     * @return
     */
    public int updateActivityVotes(Map<String, Object> param) {
        return activityMapper.updateActivityVotes(param);
    }

    /**
     * 无登录点赞
     *
     * @param param
     * @return
     */
    public int updateActivityReads(Map<String, Object> param) {
        return activityMapper.updateActivityReads(param);
    }

    /**
     * 查询点赞次数浏览次数
     *
     * @param param
     * @return
     */
    public Map<String, Object> getVotesViews(Map<String, Object> param) {
        return activityMapper.queryActivityCountNew(param);
    }

    /**
     * 更新评论数
     *
     * @param param
     * @return
     */
    public int updateComment(Map<String, Object> param) {
        return activityMapper.updateComment(param);
    }

    /**
     * CRM-运营管理-活动-文章列表
     * 点击文章列表显示所有的文章列表总数
     *
     * @author kuangbin
     */
    public int getActivityListCount(Map<String, Object> param) {
        // 加入查询状态
        param.put("status", "1");
        return activityMapper.queryActivityListCount(param);
    }

    /**
     * CRM-运营管理-活动-文章列表
     * 点击文章列表显示所有的文章列表
     *
     * @author kuangbin
     */
    public List<Map<String, Object>> getActivityList(Map<String, Object> param) {
        // 加入查询状态
        param.put("status", "1");

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

    /**
     * CRM-运营管理-活动-文章列表
     * 点击新建后加入新建的活动文章内容
     *
     * @author kuangbin
     */
    public int addActivityList(Map<String, Object> param) {
        long addtime = System.currentTimeMillis();
        // 获取活动ID
        String actid = JzbRandom.getRandomCharCap(11);
        param.put("addtime", addtime);
        param.put("actid", actid);
        // 加入文章信息
        int count = activityMapper.insertActivityList(param);
        if (count > 0) {
            if (!JzbDataType.isEmpty(param.get("photolist"))) {
                Object photo = param.get("photolist");
                if (JzbDataType.isCollection(photo)) {
                    List<Map<String, Object>> photoList = (List<Map<String, Object>>) photo;
                    for (int i = photoList.size()-1; i >= 0; i--) {
                        Map<String, Object> photoMap = photoList.get(i);
                        if (!JzbDataType.isEmpty(photoMap.get("photo"))){
                            // 加入默认状态
                            photoMap.put("status", "1");
                            photoMap.put("addtime", addtime);
                            photoMap.put("actid", actid);
                            photoMap.put("uid", param.get("uid"));
                            photoMap.put("summary", param.get("summary"));
                            photoMap.put("fileid", JzbRandom.getRandomCharCap(15));
                        }else {
                            photoList.remove(i);
                        }
                    }
                    // 加入活动图片
                    count = activityMapper.insertActivityPhoto(photoList);
                } else {
                    count = 0;
                }
            }
        }
        return count;
    }

    /**
     * CRM-运营管理-活动-文章列表
     * 点击修改时返回需要修改的信息
     *
     * @author kuangbin
     */
    public List<Map<String, Object>> getActivityData(Map<String, Object> param) {
        return activityMapper.queryActivityData(param);
    }

    /**
     * CRM-运营管理-活动-文章列表
     * 点击修改后对活动文章进行修改
     *
     * @author kuangbin
     */
    public int modifyActivityData(Map<String, Object> param) {
        long updtime = System.currentTimeMillis();
        param.put("updtime", updtime);
        int count = activityMapper.updateActivityData(param);
        if (count == 1) {
            if (!JzbDataType.isEmpty(param.get("photolist"))) {
                Object photo = param.get("photolist");
                if (JzbDataType.isCollection(photo)) {
                    List<Map<String, Object>> list = (List<Map<String, Object>>) photo;
                    for (int i = 0; i < list.size(); i++) {
                        Map<String, Object> photoMap = list.get(i);
                        if (!JzbDataType.isEmpty(photoMap.get("photo"))){
                        // 加入默认状态
                        photoMap.put("updtime", updtime);
                        photoMap.put("actid", JzbDataType.getString(param.get("actid")));
                        photoMap.put("uid", JzbDataType.getString(param.get("uid")));
                        photoMap.put("newFileId", JzbRandom.getRandomCharCap(15));
                        }else {
                            list.remove(i);
                        }
                    }
                    count = activityMapper.updateActivityPhoto(list);
                } else {
                    count = 0;
                }
            }
        }
        return count;
    }

    /**
     * CRM-运营管理-活动-文章列表
     * 点击删除后对活动文章进行删除操作
     *
     * @author kuangbin
     */
    public int removeActivityData(Map<String, Object> param) {
        param.put("status", 2);
        long updtime = System.currentTimeMillis();
        param.put("updtime", updtime);
        return activityMapper.deleteActivityData(param);
    }

    /**
     * CRM-运营管理-活动-SEO优化
     * 点击保存后对活动中的SEO优化进行修改
     *
     * @author kuangbin
     */
    public int modifyActivityDataSEO(Map<String, Object> param) {
        long updtime = System.currentTimeMillis();
        // 加入修改时间
        param.put("updtime", updtime);
        return activityMapper.updateActivityDataSEO(param);
    }
}
