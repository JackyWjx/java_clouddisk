package com.jzb.activity.controller;

import com.jzb.activity.service.TbScoreActivityListService;
import com.jzb.activity.util.PageConvert;
import com.jzb.base.data.JzbDataType;
import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @描述 查询积分页面活动列表
 * @创建人 chenhui
 * @创建时间 2019/11/9
 * @修改人和其它信息 chenzhengduan 添加日志记录
 */
@RestController
@RequestMapping(value = "/score/activity")
public class TbScoreActivityListController {
    /**
     * 调用积分页面活动列表服务层方法类
     */
    @Autowired
    private TbScoreActivityListService scoreService;
    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(TbScoreActivityListController.class);


    @RequestMapping(value = "/getScoreActivityList", method = RequestMethod.POST)
    @ResponseBody
    public Response getScoreActivityList(@RequestBody Map<String, Object> paramap) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/score/activity/getScoreActivityList";
        boolean flag = true;
        try {
            if (paramap.get("userinfo") != null) {
                userInfo = (Map<String, Object>) paramap.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            // 获取单位总数
            int count = JzbDataType.getInteger(paramap.get("count"));
            count = count < 0 ? 0 : count;
            if (count == 0) {
                // 查询活动总数
                count = scoreService.getCount(paramap);
            }
            // 分页参数计算
            PageConvert pageConvert = new PageConvert();
            pageConvert.setPageRows(paramap);
            paramap.put("uid", userInfo.get("uid"));

            Object cname = userInfo.get("cname");
            // 获取活动列表
            List<Map<String, Object>> list = scoreService.getActivity(paramap);
            // 应强哥需求每条记录加个cname
            for (int i = 0; i < list.size(); i++) {
                list.get(i).put("cname", cname);
            }
            response = Response.getResponseSuccess(userInfo);
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(list);
            Map<String, Object> cmap = new HashMap<>();
            cmap.put("cname", cname);
            response.setResponseEntity(cmap);
            pageInfo.setTotal(count > 0 ? count : list.size());
            response.setPageInfo(pageInfo);

        } catch (Exception e) {
            flag = false;
            JzbTools.logError(e);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getScoreActivityList Method", e.toString()));
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
     * 添加积分活动
     *
     * @param paramp
     * @return
     */
    @RequestMapping(value = "/addScoreActivity", method = RequestMethod.POST)
    @ResponseBody
    public Response addScoreActivity(@RequestBody Map<String, Object> paramp) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/score/activity/addScoreActivity";
        boolean flag = true;
        try {
            if (paramp.get("userinfo") != null) {
                userInfo = (Map<String, Object>) paramp.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            paramp.put("uid", userInfo.get("uid"));
            // 加入新建活动内容
            int count = scoreService.addActivityList(paramp);
            response = count > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
        } catch (Exception e) {
            flag = false;
            JzbTools.logError(e);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "addScoreActivity Method", e.toString()));
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
