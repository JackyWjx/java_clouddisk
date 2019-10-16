package com.jzb.activity.service;

import java.util.List;
import java.util.Map;

/**
 * @Description: 活动Service接口
 * @Author duanfeiyu
 * @Version v1.0
 * @Since 1.0
 * @Date: 2019/8/7 9:08
 */
public interface ActivityService {


    /**
     * 查询活动的数据
     * @return List<Map<String, Object>> 返回数组
     * @param param
     */
    List<Map<String, Object>> queryActivityList(Map<String, Object> param);
    /**
     * 查询推图片信息
     * @return List<Map<String, Object>> 返回数组
     * @param params
     */
    List<Map<String, Object>> queryActpictureList(Map<String, Object> params);

    /**
     *  活动点赞
     * @param  ouid 用户Id
     * @param  actid 资源Id
     * @param  restype 资源类型
     * @return Response 返回json数组
     */
    Map<String,Object> findActpictureById(String ouid, String actid, String restype);

    /**
     * 查询总数
     * @return
     */
    int queryActivityCount();

    /**
     *  获取活动Id,然后可以发表评论信息,发表评论一次,评论次数加一
     * @param  param 用map接受参数
     * @return Response 返回Json数据
     */
    Map<String, Object> insertHashMapById(Map<String, Object> param);



    /**
     * 查询总数
     * @return Integer 返回Integer
     */
    Integer queryParticularsCount();


    /**
     * 根据活动名称模糊查询
     * @param params  用map存储
     * @return  List<Map<String, Object>> 返回数组
     */
    List<Map<String, Object>> getLikeName(Map<String, Object> params);


    /**
     *  模糊查询总数
     * @return Integer 返回Integer
     */
    int likeActivityCount(Map<String,Object> params);

    /**
     * 查询详情
     * @return
     */
    int queryParticularsByIdCount();

    /**
     * 推广图片查询
     * @return 返回总数
     */
    int EnquiryCount();
}
