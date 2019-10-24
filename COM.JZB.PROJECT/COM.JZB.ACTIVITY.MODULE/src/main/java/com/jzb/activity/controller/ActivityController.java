package com.jzb.activity.controller;

import com.jzb.activity.api.redis.UserRedisApi;
import com.jzb.activity.service.ActivityService;
import com.jzb.activity.service.NewActivityService;
import com.jzb.activity.vo.CheckParam;
import com.jzb.activity.vo.JZBJSonMax;
import com.jzb.activity.vo.JsonPageInfo;
import com.jzb.activity.vo.PageConvert;
import com.jzb.base.data.JzbDataType;
import com.jzb.base.data.date.JzbDateStr;
import com.jzb.base.data.date.JzbDateUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbPageConvert;
import com.jzb.base.util.JzbTools;
import com.netflix.discovery.converters.jackson.EurekaXmlJacksonCodec;
import org.bouncycastle.cert.ocsp.Req;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 活动控制层
 * @Author duanfeiyu
 * @Version v1.0
 * @Since 1.0
 * @Date: 2019/8/7 9:07
 */
@RestController
@RequestMapping(value = "/activity")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private NewActivityService newActivityService;

    @Autowired
    private UserRedisApi userRedisApi;

    /**
     * 查询活动的数据
     *
     * @return Response 返回json数据
     */
    @RequestMapping(value = "/queryActivityList",method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response queryActivityList(@RequestBody Map<String, Object> params) {
        Response response = null;
        try {

            // 验证参数为空返回error
            if (JzbCheckParam.haveEmpty(params,new String[]{"pageno","pagesize"})) {

                response = Response.getResponseError();

            } else {
                JzbPageConvert.setPageRows(params);

                // 获取结果集
                List<Map<String, Object>> list = activityService.queryActivityList(params);

                // 获取总数
                int count = activityService.queryActivityCount();

                // 定义pageinfo
                PageInfo pi = new PageInfo();
                pi.setList(list);
                pi.setTotal(count);

                // 定义返回
                response = Response.getResponseSuccess();
                response.setPageInfo(pi);
            }
        } catch (Exception e) {
            JzbTools.logError(e);
            response = Response.getResponseError();
        }

        return response;
    }

    /**
     * 查询推图片信息
     *
     * @return Response 返回json数据
     */
    @RequestMapping("/queryActpictureList")
    @CrossOrigin
    public Response queryActpictureList(@RequestBody Map<String, Object> params) {
        Response response = null;
        try {
            //获取前台传的值
            int count = JzbDataType.getInteger(params.get("count"));
            //判断总数是不是等于-1
            count = count <= 0 ? 0 : count;

            if (count == 0) {

                count = activityService.EnquiryCount();
            }
            List<Map<String, Object>> arr = activityService.queryActpictureList(params);
            // 定义返回
//            Map<String, Object> userInfo = (Map<String, Object>) params.get("userinfo");
            response = Response.getResponseSuccess();
            JsonPageInfo.setPageInfo(count, arr, response);
        } catch (Exception e) {
            JzbTools.logError(e);
            response = Response.getResponseError();
        }
        return response;
    }

    /**
     * 获取活动Id,然后可以发表评论信息,发表评论一次,评论次数加一
     *
     * @param param 用map接受参数
     * @return Response 返回Json数据
     */
    @RequestMapping("/insertHashMapById")
    @CrossOrigin
    public Response insertHashMapById(@RequestBody Map<String, Object> param) {
        Response response = null;
        try {
            //从redis读取
            //讨论ID
            String disid = "1";

            //disid + 随机数
            disid = disid.trim() + JZBJSonMax.getNumBigCharRandom(6).trim();

            //参与人
            String uid = "0";
            //用map存储讨论ID
            param.put("disid", disid);
            //用map存储参与人
            param.put("uid", uid);

            Map<String, Object> map = activityService.insertHashMapById(param);
            // 定义返回
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            response = Response.getResponseSuccess(userInfo);

            //调用集合方法
            JsonPageInfo.getArrayList(map, response);
        } catch (Exception e) {
            JzbTools.logError(e);
        }
        return response;
    }

    /**
     * 根据活动Id查询详情信息,阅读数加一
     *
     * @param params 用map存储
     * @return Response 返回json
     */
    @RequestMapping(value = "/getDiscuss", method = RequestMethod.POST)
    @CrossOrigin
    public Response getDiscussById(@RequestBody Map<String, Object> params) {
        Response response;
        try {
            if (params.get("actid") == null || JzbDataType.getString(params.get("actid")).equals("")) {
                response = Response.getResponseError();
            } else {
                List<Map<String, Object>> list = newActivityService.getActivityDesc(params);
                PageInfo pi = new PageInfo();
                pi.setList(list);
                pi.setTotal(list.size());
                // 定义返回
                response = Response.getResponseSuccess();
                response.setPageInfo(pi);
            }
        } catch (Exception e) {
            JzbTools.logError(e);
            response = Response.getResponseError();
        }
        return response;
    }

    /**
     * 查找评论信息只显示3条信息
     *
     * @param params map接收参数
     * @return
     */
    @RequestMapping("/findParticularsList")
    @CrossOrigin
    public Response findParticularsList(@RequestBody Map<String, Object> params) {
        Response response;
        try {

            if (params.get("actid") == null) {

                response = Response.getResponseError();
            } else {

                List<Map<String, Object>> list = newActivityService.findParticularsList(params);
                Map<String, Object> map = new HashMap<>();
                for (int i = 0, l = list.size(); i < l; i++) {
                    map.put("uid", list.get(i).get("uid"));
                    Response resp = userRedisApi.getCacheUserInfo(map);
                    Map<String, Object> user = (Map<String, Object>) resp.getResponseEntity();
                    list.get(i).put("userPhoto", user.get("portrait"));
                    list.get(i).put("cname", user.get("cname"));
                    //  list.get(i).put("addtime", JzbDateUtil.toDateString(JzbDataType.getLong(list.get(i).get("addtime")), JzbDateStr.yyyy_MM_dd));
                }
                int count = newActivityService.findParticularsListCount(params);

                PageInfo pi = new PageInfo();
                pi.setList(list);
                pi.setTotal(count);

                // 定义返回
                response = Response.getResponseSuccess();
                response.setPageInfo(pi);
            }
        } catch (Exception e) {

            JzbTools.logError(e);
            response = Response.getResponseError();
        }
        return response;
    }

    /**
     * 查找详情页面
     *
     * @param params map接收参数
     * @return
     */
    @RequestMapping("/findParticularsByList")
    @CrossOrigin
    public Response findParticularsByList(@RequestBody Map<String, Object> params) {
        Response response;
        try {

            if (!CheckParam.verifition(params, new String[]{"pageno", "pagesize", "actid"})) {

                response = Response.getResponseError();

            } else {

                PageConvert.setPageRows(params);
                // 分页查询
                List<Map<String, Object>> list = newActivityService.findParticularsByList(params);
                Map<String, Object> map = new HashMap<>();

                for (int i = 0, l = list.size(); i < l; i++) {
                    map.put("uid", list.get(i).get("uid"));
                    Response resp = userRedisApi.getCacheUserInfo(map);
                    Map<String, Object> user = (Map<String, Object>) resp.getResponseEntity();
                    list.get(i).put("userPhoto", user.get("portrait"));
                    list.get(i).put("cname", user.get("cname"));
//                    list.get(i).put("addtime", JzbDateUtil.toDateString(JzbDataType.getLong(list.get(i).get("addtime")), JzbDateStr.yyyy_MM_dd));
                }

                int count = newActivityService.findParticularsListCount(params);

                PageInfo pi = new PageInfo();
                pi.setList(list);
                pi.setTotal(count);

                // 定义返回
                response = Response.getResponseSuccess();

                response.setPageInfo(pi);

            }
        } catch (Exception e) {

            JzbTools.logError(e);
            response = Response.getResponseError();

        }
        return response;
    }

    /**
     * 根据活动名称模糊查询
     *
     * @param params 用map存储
     * @return Response 返回Json
     */
    @RequestMapping(value = "/getLikeName", method = RequestMethod.POST)
    @CrossOrigin
    public Response getLikeName(@RequestBody Map<String, Object> params) {
        Response response;
        try {

            // 验证参数为空返回error
            if (params.get("pagesize") == null || params.get("pageno") == null || params.get("keyword") == null) {

                response = Response.getResponseError();

            } else {

                // 获取行数和页数
                int rows = JzbDataType.getInteger(params.get("pagesize"));
                int page = JzbDataType.getInteger(params.get("pageno"));

                // 给param设置page and  rows
                params.put("pageno", JzbDataType.getInteger(page * rows - rows < 0 ? 0 : page * rows - rows));
                params.put("pagesize", rows);

                // 模糊查询zongshu
                int count = activityService.likeActivityCount(params);

                // 得到结果集
                List<Map<String, Object>> mapList = activityService.getLikeName(params);

                // 定义pageinfo
                PageInfo pi = new PageInfo();
                pi.setList(mapList);
                pi.setTotal(count);

                // 定义返回response
                // 定义返回
                response = Response.getResponseSuccess();
                response.setPageInfo(pi);
            }
        } catch (Exception e) {
            JzbTools.logError(e);
            response = Response.getResponseError();
        }
        return response;
    }

    /**
     * 添加评论
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/addActivityDocess", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response addActivityDocess(@RequestBody Map<String, Object> params) {

        Response result;
        try {
            if (!CheckParam.verifition(params, new String[]{"context", "actid"})) {
                result = Response.getResponseError();
            } else {
                Map<String, Object> userInfo = (Map<String, Object>) params.get("userinfo");
                params.put("uid", userInfo.get("uid"));
                int count = newActivityService.addActivityDucess(params);
                if (count > 0) {
                    // 更新评论数
                    newActivityService.updateComment(params);

                    // 定义返回结果
                    result = Response.getResponseSuccess(userInfo);


                } else {
                    result = Response.getResponseError();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = Response.getResponseError();
        }

        return result;
    }

    /**
     * 无登录点赞
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/addActivityVotes", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response addActivityVotes(@RequestBody Map<String, Object> params) {
        Response result;
        try {
            if (!CheckParam.verifition(params, new String[]{"actid"})) {
                result = Response.getResponseError();
            } else {
                int count = newActivityService.updateActivityVotes(params);
                result = count > 0 ? Response.getResponseSuccess() : Response.getResponseError();
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }


    /**
     * 无登录点赞
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/addActivityReads", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response addActivityReads(@RequestBody Map<String, Object> params) {
        Response result;
        try {
            if (!CheckParam.verifition(params, new String[]{"actid"})) {
                result = Response.getResponseError();
            } else {
                int count = newActivityService.updateActivityReads(params);
                result = count > 0 ? Response.getResponseSuccess() : Response.getResponseError();
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    // 获取点赞次数
    @RequestMapping(value = "/getResourceVotes", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response getResourceVotes(@RequestBody Map<String, Object> params) {
        Response result;
        try {
            // 验证指定参数为空则返回error
            if (params.get("actid") == null) {
                result = Response.getResponseError();
            } else {
                params.put("restype", "R0001");
                Map<String, Object> votesMap = newActivityService.getVotesViews(params);
                // 定义返回结果
                result = Response.getResponseSuccess();
                result.setResponseEntity(votesMap);
            }
        } catch (Exception e) {
            // 打印异常信息
            e.printStackTrace();
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * CRM-运营管理-活动-文章列表
     * 点击文章列表显示所有的文章列表(可加入开始时间,结束时间条件)
     *
     * @author kuangbin
     */
    @RequestMapping(value = "/getActivityList", method = RequestMethod.POST)
    @CrossOrigin
    public Response getActivityList(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 加入查询状态
            param.put("status", "1");
            String startTime = JzbDataType.getString(param.get("starttime"));
            // 判断是否有开始时间的查询条件
            if (!JzbDataType.isEmpty(startTime)) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = simpleDateFormat.parse(startTime);
                long time = date.getTime();
                // 将时间转化为时间戳存入参数中
                param.put("starttime", time);
            }
            String endTime = JzbDataType.getString(param.get("endtime"));
            // 判断是否有结束时间的查询条件
            if (!JzbDataType.isEmpty(endTime)) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = simpleDateFormat.parse(endTime);
                long time = date.getTime();
                // 将时间转化为时间戳存入参数中
                param.put("endtime", time);
            }
            // 获取前端传来的总数
            int count = JzbDataType.getInteger(param.get("count"));
            count = count < 0 ? 0 : count;
            if (count == 0) {
                // 查询所有符合条件的总数
                count = newActivityService.getActivityListCount(param);
            }
            // 返回所有的推广信息列表
            List<Map<String, Object>> activityList = newActivityService.getActivityList(param);
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            PageInfo pageInfo = new PageInfo();
            result = Response.getResponseSuccess(userInfo);
            pageInfo.setList(activityList);
            pageInfo.setTotal(count > 0 ? count : activityList.size());
            result.setPageInfo(pageInfo);
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    } // End modifyAdvertData

    /**
     * CRM-运营管理-活动-文章列表
     * 点击新建后加入新建的活动文章内容
     *
     * @author kuangbin
     */
    @RequestMapping(value = "/addActivityList", method = RequestMethod.POST)
    @CrossOrigin
    public Response addActivityList(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 获取用户信息
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("uid", JzbDataType.getString(userInfo.get("uid")));
            // 加入新建的活动文章内容
            int count = newActivityService.addActivityList(param);
            result = count >= 1 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    } // End addActivityList

    /**
     * CRM-运营管理-活动-文章列表
     * 点击修改时返回需要修改的信息
     *
     * @author kuangbin
     */
    @RequestMapping(value = "/getActivityData", method = RequestMethod.POST)
    @CrossOrigin
    public Response getActivityData(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 加入新建的活动文章内容
            List<Map<String, Object>> activity = newActivityService.getActivityData(param);

            // 获取用户信息
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            PageInfo pageInfo = new PageInfo();
            result = Response.getResponseSuccess(userInfo);
            pageInfo.setList(activity);
            result.setPageInfo(pageInfo);
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    } // End getActivityData

    /**
     * CRM-运营管理-活动-文章列表
     * 点击修改后对活动文章进行修改
     *
     * @author kuangbin
     */
    @RequestMapping(value = "/modifyActivityData", method = RequestMethod.POST)
    @CrossOrigin
    public Response modifyActivityData(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 获取用户信息
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("uid", JzbDataType.getString(userInfo.get("uid")));
            // 加入新建的活动文章内容
            int count = newActivityService.modifyActivityData(param);
            result = count >= 1 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    } // End getActivityData

    /**
     * CRM-运营管理-活动-文章列表
     * 点击删除后对活动文章进行删除操作
     *
     * @author kuangbin
     */
    @RequestMapping(value = "/removeActivityData", method = RequestMethod.POST)
    @CrossOrigin
    public Response removeActivityData(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 获取用户信息
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("uid", JzbDataType.getString(userInfo.get("uid")));
            // 加入新建的活动文章内容
            int count = newActivityService.removeActivityData(param);
            result = count >= 1 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    } // End getActivityData

    /**
     * CRM-运营管理-活动-SEO优化
     * 点击保存后对活动中的SEO优化进行修改
     *
     * @author kuangbin
     */
    @RequestMapping(value = "/modifyActivityDataSEO", method = RequestMethod.POST)
    @CrossOrigin
    public Response modifyActivityDataSEO(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 获取用户信息
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("uid", JzbDataType.getString(userInfo.get("uid")));
            // 修改修改成功数
            int count = newActivityService.modifyActivityDataSEO(param);
            result = count >= 1 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    } // End modifySolutionDomSEO
}
