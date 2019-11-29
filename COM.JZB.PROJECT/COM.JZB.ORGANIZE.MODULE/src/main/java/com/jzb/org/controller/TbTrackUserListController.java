package com.jzb.org.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.data.date.JzbDateStr;
import com.jzb.base.data.date.JzbDateUtil;
import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbPageConvert;
import com.jzb.base.util.JzbTools;
import com.jzb.org.api.redis.UserRedisServiceApi;
import com.jzb.org.service.TbTrackUserListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenzhengduan
 */
@RequestMapping(value = "/org/trackUserList")
@RestController
public class TbTrackUserListController {

    @Autowired
    private TbTrackUserListService tbTrackUserListService;

    @Autowired
    private UserRedisServiceApi userRedisServiceApi;
    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(TbTrackUserListController.class);

    /**
     * 查询所有跟进记录
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getTrackUserList", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response getTrackUserList(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/org/trackUserList/getTrackUserList";
        boolean flag = true;
        try {
            // 如果获取参数userinfo不为空的话
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            // 如果验证指定参数为空则返回error
            if (JzbCheckParam.haveEmpty(param, new String[]{"pagesize", "pageno"})) {
                response = Response.getResponseError();
            } else {
                // 配置分页参数
                JzbPageConvert.setPageRows(param);

                // 获取结果集
                List<Map<String, Object>> trackList = tbTrackUserListService.findTrackList(param);
                for (int i = 0; i < trackList.size(); i++) {
                    param.put("uid", trackList.get(i).get("trackuid"));
                    Response region = userRedisServiceApi.getCacheUserInfo(param);
                    trackList.get(i).put("userInfo", region.getResponseEntity());
                    trackList.get(i).put("addtime", JzbDateUtil.toDateString(JzbDataType.getLong(trackList.get(i).get("addtime")), JzbDateStr.yyyy_MM_dd_HH_mm_ss));
                }
                // 定义返回结果
                response = Response.getResponseSuccess(userInfo);
                // 定义分页对象
                PageInfo pageInfo = new PageInfo();
                // 设置list
                pageInfo.setList(trackList);
                // 设置总数
                pageInfo.setTotal(tbTrackUserListService.findTrackListCount());
                // 返回分页对象
                response.setPageInfo(pageInfo);
            }
        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getTrackUserList Method", ex.toString()));
        }
        if (userInfo != null) {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
                    userInfo.get("msgTag").toString(), "User Login Message"));
        } else {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
        }
        return response;
    }

    /**
     * 查询所有跟进记录 (带条件)
     * @param param
     * @return
     */
    @RequestMapping(value = "/getTrackUserListByKeywords", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response getTrackUserListByKeywords(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/org/trackUserList/getTrackUserListByKeywords";
        boolean flag = true;
        try {
            // 如果获取参数userinfo不为空的话
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }

            // 如果获取参数userinfo不为空的话
            if (JzbCheckParam.haveEmpty(param, new String[]{"keyword"})) {
                response = Response.getResponseError();
                logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getTrackUserListByKeywords Method", "param is NULL"));
            } else {
                // 定义list放uid和cid
                List<Map<String, Object>> list = new ArrayList<>();
                // 定义map便于list添加对象
                Map<String, Object> map=new HashMap<>();
                // 根据关键字查询出来的单位id
                List<Map<String, Object>> cnameLike = tbTrackUserListService.findCnameLike(param);
                for (int i = 0, l = cnameLike.size(); i < l; i++) {
                    map.put("value",cnameLike.get(i).get("cid"));
                    list.add(map);
                }
                // 根据关键字查询出来的用户id
                List<Map<String, Object>> unameLike = tbTrackUserListService.findUnameLike(param);
                for (int i = 0, l = unameLike.size(); i < l; i++) {
                    map.put("value",unameLike.get(i).get("uid"));
                    list.add(map);
                }
                // 把list放到参数中用于查询数据
                param.put("list",list);
                List<Map<String, Object>> trackListByKeywords = tbTrackUserListService.findTrackListByKeywords(param);
                // 处理返回数据
                for (int i = 0; i < trackListByKeywords.size(); i++) {
                    param.put("uid", trackListByKeywords.get(i).get("trackuid"));
                    // 缓存查询出用户信息
                    Response region = userRedisServiceApi.getCacheUserInfo(param);
                    // 放入每一条记录
                    trackListByKeywords.get(i).put("userInfo", region.getResponseEntity());
                    // 转换时间
                    trackListByKeywords.get(i).put("addtime", JzbDateUtil.toDateString(JzbDataType.getLong(trackListByKeywords.get(i).get("addtime")), JzbDateStr.yyyy_MM_dd_HH_mm_ss));
                }

                // 定义返回结果
                response = Response.getResponseSuccess(userInfo);
                // 定义分页对象
                PageInfo pageInfo = new PageInfo();
                // 设置list
                pageInfo.setList(trackListByKeywords);
                // 设置总数
                pageInfo.setTotal(tbTrackUserListService.findTrackListCountByKeywords(param));
                // 返回分页对象
                response.setPageInfo(pageInfo);

            }

        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getTrackUserListByKeywords Method", ex.toString()));
        }
        if (userInfo != null) {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
                    userInfo.get("msgTag").toString(), "User Login Message"));
        } else {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
        }
        return response;
    }
}
