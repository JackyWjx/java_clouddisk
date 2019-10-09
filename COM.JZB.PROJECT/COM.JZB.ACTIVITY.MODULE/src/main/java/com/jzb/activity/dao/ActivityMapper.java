package com.jzb.activity.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Description: 活动Mapper接口
 * @Author duanfeiyu
 * @Version v1.0
 * @Since 1.0
 * @Date: 2019/8/7 9:07
 */
@Mapper
@Repository
public interface ActivityMapper {


    /**
     * 查询活动的数据
     *
     * @param param
     * @return List<Map < String, Object>> 返回数组
     */
    List<Map<String, Object>> queryActivityList(Map<String, Object> param);

    /**
     * 查询图片推广信息
     *
     * @param params
     * @return List<Map < String, Object>> 返回list数据
     */
    List<Map<String, Object>> queryActpictureList(Map<String, Object> params);

    /**
     * 点击数
     *
     * @param updateByid
     * @return Map<String, Object> 返回kv数据
     */
    int updateActpictureById(Map<String, Object> updateByid);

    /**
     * 查询
     *
     * @param ouid
     * @param restype
     * @return Map<String, Object> 返回kv数据
     */
    Map<String, Object> queryActivityVotesList(@Param("ouid") String ouid, @Param("actid") String actid, @Param("restype") String restype);

    /**
     * 根据资源查询活动信息
     *
     * @param resid 资源Id
     * @return
     */
    Map<String, Object> findresIdById(@Param("resid") String resid);


    /**
     * 查询点击量
     *
     * @param resid
     * @return
     */
    Map<String, Object> queryActivityById(String resid);

    /**
     * 查询总数
     *
     * @return int 返回int类型
     */
    int queryActivityCount();

    /**
     * 更新评论次数
     *
     * @param mapComments
     */
    int updateComments(Map<String, Object> mapComments);

    /**
     * 查询评论次数
     *
     * @param map map存储的数据
     * @return Map<String, Object> 返回map数组
     */
    List<Map<String, Object>> findActivity(Map<String, Object> map);

    /**
     * 根据活动查询评论数
     *
     * @param actid 活动ID
     * @return Map<String, Object> 返回kv
     */
    Map<String, Object> findActivityById(String actid);

    /**
     * 根据查询详情信息
     *
     * @return Map<String, Object> 返回kv存储
     */
    List<Map<String, Object>> getDiscussById(Map<String, Object> param);

    /**
     * 更新点站时间
     *
     * @param stringMap
     * @return
     */
    int updatevotesId(Map<String, Object> stringMap);

    /**
     * 更据活动Id更新时间
     *
     * @param addtime
     * @param id
     * @return
     */
    int updatevotesId(@Param("addtime") Long addtime, @Param("id") Integer id);

    /**
     * 添加评论信息
     *
     * @param hashMap 用map接收参数
     * @return int 返回1代表成功
     */
    int insertHashMapById(Map<String, Object> hashMap);

    /**
     * 查询活动数据
     *
     * @param actid 活动Id
     * @return
     */
    Map<String, Object> findActivityId(String actid);

    /**
     * 发表一次评论信息,评论次数加一
     *
     * @param comments 评论次数
     * @param actid
     */
    void insertcomments(Integer comments, @Param("actid") String actid);

    /**
     * 返回总数
     *
     * @return
     */
    Integer queryParticularsCount();

    /**
     * 查询详情,只显示3条
     *
     * @param params 用map kv接收参数
     * @return
     */
    List<Map<String, Object>> findParticularsList(Map<String, Object> params);

    /**
     * 查询总数
     *
     * @param params
     * @return
     */
    int findParticularsListCount(Map<String, Object> params);

    /**
     * 添加评论
     *
     * @param params
     * @return
     */
    int addActivityDiscuss(Map<String, Object> params);

    /**
     * 查询活动详情
     *
     * @param params 用map kv接收参数
     * @return
     */
    List<Map<String, Object>> findParticularsByList(Map<String, Object> params);

    /**
     * 根据活动名称模糊查询
     *
     * @param params 用map存储
     * @return
     */
    List<Map<String, Object>> getLikeName(Map<String, Object> params);


    /**
     * 查询 资源类型 资源ID 相关的数据
     *
     * @param restype 资源类型
     * @param ouid    资源ID
     * @return Map<String, Object> 返回kv
     */
    Map<String, Object> queryRestypeList(@Param("restype") String restype, @Param("ouid") String ouid);

    /**
     * 更新阅读数
     *
     * @param actid 活动Id
     * @param reads 阅读数
     * @return int 返回1成功
     */
    int updateReads(@Param("actid") String actid, @Param("reads") Integer reads);

    /**
     * 更新时间
     *
     * @param resid   资源Id
     * @param addTime 当前时间
     */
    void updateAddTime(@Param("resid") String resid, @Param("addTime") Long addTime);


    /**
     * 模糊查询总数
     *
     * @return Integer 返回Integer
     */
    int likeActivityCount(Map<String, Object> params);

    /**
     * 查询详情总数
     *
     * @return
     */
    int queryParticularsByIdCount();

    /**
     * 推广图片总数
     *
     * @return
     */
    int EnquiryCount();

    /**
     * 查询活动图片
     *
     * @param actid
     * @return
     */
    Map<String, Object> queryActivityActid(String actid);

    /**
     * 无登录点赞
     *
     * @param param
     * @return
     */
    int updateActivityVotes(Map<String, Object> param);

    /**
     * 无登录点赞
     *
     * @param param
     * @return
     */
    int updateActivityReads(Map<String, Object> param);

    /**
     * 查询点赞浏览次数
     *
     * @param param
     * @return
     */
    Map<String, Object> queryActivityCountNew(Map<String, Object> param);

    /**
     * 更新评论数
     *
     * @param param
     * @return
     */
    int updateComment(Map<String, Object> param);

    /**
     * CRM-运营管理-活动-文章列表
     * 点击文章列表显示所有的文章列表总数
     *
     * @author kuangbin
     */
    int queryActivityListCount(Map<String, Object> param);

    /**
     * CRM-运营管理-活动-文章列表
     * 点击文章列表显示所有的文章列表
     *
     * @author kuangbin
     */
    List<Map<String, Object>> queryActivityListData(Map<String, Object> param);

    /**
     * CRM-运营管理-活动-文章列表
     * 点击新建后加入新建的活动文章内容
     *
     * @author kuangbin
     */
    int insertActivityList(Map<String, Object> param);

    /**
     * CRM-运营管理-活动-文章列表
     * 点击新建后加入新建的活动文章图片
     *
     * @author kuangbin
     */
    int insertActivityPhoto(List<Map<String, Object>> photoList);

    /**
     * CRM-运营管理-活动-文章列表
     * 点击修改时返回需要修改的信息
     *
     * @author kuangbin
     */
    List<Map<String, Object>> queryActivityData(Map<String, Object> param);

    /**
     * CRM-运营管理-活动-文章列表
     * 点击修改后对活动文章进行修改
     *
     * @author kuangbin
     */
    int updateActivityData(Map<String, Object> param);

    /**
     * CRM-运营管理-活动-文章列表
     * 点击修改后对活动文章进行修改
     *
     * @author kuangbin
     */
    int updateActivityPhoto(List<Map<String, Object>> param);

    /**
     * CRM-运营管理-活动-文章列表
     * 点击修改后对活动文章进行修改
     *
     * @author kuangbin
     */
    int deleteActivityData(Map<String, Object> param);

    /**
     * CRM-运营管理-活动-SEO优化
     * 点击保存后对活动中的SEO优化进行修改
     *
     * @author kuangbin
     */
    int updateActivityDataSEO(Map<String, Object> param);
}
