package com.jzb.activity.service;

import com.jzb.activity.dao.ActivityMapper;
import com.jzb.activity.vo.DataTimes;
import com.jzb.activity.vo.JsonPageInfo;
import com.jzb.base.data.JzbDataType;

import com.jzb.base.util.JzbTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 活动实现接口
 * @Author duanfeiyu
 * @Version v1.0
 * @Since 1.0
 * @Date: 2019/8/7 9:13
 */
@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityMapper activityMapper;

    /**
     * 查询活动的数据
     * @return List<Map<String, Object>> 返回数组
     * @param param
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public List<Map<String, Object>> queryActivityList(Map<String, Object> param) {

        //创建ArrayList容器
        ArrayList<Map<String, Object>> arrayList = new ArrayList<>();
        try {
            //调用分页方法
            JsonPageInfo.setPageSize(param);
            //用list接受map集合的数据
            List<Map<String, Object>> lists =  activityMapper.queryActivityList(param);


            String actid = null;
            //遍历数据
            for (Map<String, Object> map : lists) {

                //活动内容
                String context = map.get("context").toString();
                //截取100条数据
                context = context.substring(0,  context.length() >= 100 ? 100 :context.length() - 1 );
                map.put("context",context);

                //发布时间
                Long  addtime  = JzbDataType.getLong(map.get("addtime"));
                String stampToDate = DataTimes.stampToDate(addtime);
                map.put("addtime",stampToDate);

                //获取活动Id
                actid  = map.get("actid").toString();
                Map<String, Object>  mapActid  =    activityMapper.queryActivityActid(actid);
                if (!StringUtils.isEmpty(mapActid)) {
                    map.put("photo", mapActid.get("photo"));
                }
                //添加到arrayList集合里
                arrayList.add(map);

            }



        }catch (Exception e){
            JzbTools.logError(e);
            System.out.println("查询失败");
        }
        //返回数据
        return arrayList;
    }

    /**
     * 查询推图片信息
     * @return List<Map<String, Object>> 返回数组
     * @param params
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public List<Map<String, Object>> queryActpictureList(Map<String, Object> params) {
        List<Map<String, Object>> list = null;
        try {
            //调用分页方法
             JsonPageInfo.setPageSize(params);
             //查询推广图片
             list = activityMapper.queryActpictureList(params);
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    /**
     *  点击次数
     * @param  ouid 用户Id
     * @param  actid 资源Id
     * @param  restype 资源类型
     * @return
     */
    @Override
    public Map<String, Object> findActpictureById(String ouid, String actid, String restype){
        Map<String, Object> mapList = null;
        try {
            if (!StringUtils.isEmpty(ouid) && !StringUtils.isEmpty(actid) && !StringUtils.isEmpty(restype)){
                //查询有没有存在的信息
                Map<String,Object> map = activityMapper.queryActivityVotesList(ouid,actid,restype);
                if (!JzbDataType.isEmpty(map)) {
                    String resid = (String) map.get("resid");
                    System.out.println(resid);
                    Integer id = JzbDataType.getInteger(map.get("id"));
                    if (!StringUtils.isEmpty(resid)) {
                        //获取当前时间
                        Long addtime = System.currentTimeMillis();

                        //更新点击时间
                       activityMapper.updatevotesId(addtime, id);

                        //查询活动信息
                        Map<String, Object> residMap = activityMapper.findresIdById(resid);

                        //获取点击量信息
                        int votes = JzbDataType.getInteger(residMap.get("votes"));

                        //判断数量是不是小于一
                        votes = votes <= 0 ? 0 : votes;

                        //点击量+1
                        votes =  votes + 1;

                        //创建一个Map容器,存储活动Id,和点击量
                        Map<String, Object> updateByid = new HashMap<>(16);
                        updateByid.put("resid", resid);
                        updateByid.put("votes",votes);

                        //更新点击量
                        activityMapper.updateActpictureById(updateByid);

                        //查询点击量
                        mapList = activityMapper.queryActivityById(actid);

                    }
                }
            }
        }catch (Exception e){
            JzbTools.logError(e);
        }
        return mapList;
    }

    /**
     * 查询总数
     * @return int 返回数字
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int queryActivityCount() {
        return activityMapper.queryActivityCount();
    }

    /**
     * 获取活动Id,然后可以发表评论信息,发表评论一次,评论次数加一
     * @param  param 用map接受参数
     * @return Map<String, Object> 用map接收参数
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Map<String, Object> insertHashMapById(Map<String, Object> param) {

        //讨论ID
        String disid = param.get("disid").toString();

        //活动Id
        String actid = param.get("actid").toString();

        //评论内容
        String context = param.get("context").toString();

        //参与人
        String uid = param.get("uid").toString();

        //创建map返回值
        Map<String, Object> result = new HashMap<>(16);

        try {
            //判断回复讨论ID是不是null,是空值运行下面的方法
            if (!StringUtils.isEmpty(disid ) && !StringUtils.isEmpty(uid) && !StringUtils.isEmpty(actid)) {

                //创建一个Map容器
                Map<String, Object> map = new HashMap<>(16);

                //活动Id保持在map中
                map.put("actid", actid);

                //根据讨论ID 参与人 回复讨论ID 查询相关的信息
                List<Map<String, Object>> mapCount = activityMapper.findActivity(map);

                //判断mapCount是否为空
                if (!StringUtils.isEmpty(mapCount)) {

                    //遍历
                    for (Map<String, Object> stringObjectMap : mapCount) {

                        actid = stringObjectMap.get("actid").toString();
                    }

                    //时间
                    Long addtime = System.currentTimeMillis();

                    //建立map容器
                    Map<String, Object> hashMap = new HashMap<>(16);
                    hashMap.put("disid", disid);
                    hashMap.put("uid", uid.trim());
                    hashMap.put("context", context);
                    hashMap.put("addtime", addtime);
                    hashMap.put("actid", actid);


                    //添加评论信息
                    int count = activityMapper.insertHashMapById(hashMap);

                    //如果count==1
                    if (count == 1) {

                        Map<String, Object> findactid = activityMapper.findActivityId(actid);

                        //获取评论数
                        int comments = (Integer) findactid.get("comments");

                        System.out.println(comments);

                        //评论数加一
                        comments += 1;

                        //清理容器
                        hashMap.clear();

                        //存储容器
                        hashMap.put("comments",comments );

                        //更新
                        activityMapper.insertcomments(comments, actid);
                    } else {
                        return param;
                    }

                    result.put("count", count);
                }
            }

        }catch (Exception e){
            JzbTools.logError(e);
        }
        return result;
    }

    /**
     * 查询总数
     * @return 返回总数
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Integer queryParticularsCount() {
        return activityMapper.queryParticularsCount();
    }


    /**
     * 根据活动名称模糊查询
     * @param params  用map存储
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public List<Map<String, Object>> getLikeName(Map<String, Object> params) {
        List<Map<String,Object>> list = null;
        try {
            //调用分页方法
            JsonPageInfo.setPageSize(params);
            //查询语句
            list = activityMapper.getLikeName(params);

            //遍历数据
            for (Map<String, Object> map : list) {
                //发布时间
                Long  addtime  = JzbDataType.getLong(map.get("addtime"));
                String stampToDate = DataTimes.stampToDate(addtime);
                map.put("addtime",stampToDate);
            }

        }catch (Exception e){
            JzbTools.logError(e);
        }
        return list;
    }


    /**
     *  模糊查询总数
     * @return Integer 返回Integer
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int likeActivityCount(Map<String,Object> params) {
        return activityMapper.likeActivityCount(params);
    }

    /**
     * 查询详情总数
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int queryParticularsByIdCount() {
        return activityMapper.queryParticularsByIdCount();
    }

    /**
     * 推广图片总数查询
     * @return
     */
    @Override
    public int EnquiryCount() {
        return activityMapper.EnquiryCount();
    }


}
