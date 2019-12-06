package com.jzb.org.controller;


import com.jzb.base.data.JzbDataType;
import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbTools;
import com.jzb.org.api.base.RegionBaseApi;
import com.jzb.org.service.TbCommonProjectListService;
import com.jzb.org.util.SetPageSize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/org/CommonProjectList")
public class TbCommonProjectListController {
    @Autowired
    private TbCommonProjectListService tbCommonProjectListService;

    @Autowired
    private RegionBaseApi regionBaseApi;
    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(TbCommonProjectListController.class);

    /**
     * 销售/业主-公海-项目-新建
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/saveCommonProjectList", method = RequestMethod.POST)
    @CrossOrigin
    public Response saveCommonProjectList(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/org/CommonProjectList/saveCommonProjectList";
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
            //如果指定参数为空的话返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"projectname"})) {
                result = Response.getResponseError();
            } else {

                //获取用户信息
                userInfo = (Map<String, Object>) param.get("userinfo");
                param.put("adduid", userInfo.get("uid"));
                //新建项目
                int count = tbCommonProjectListService.saveCommonProjectList(param);
                //返回响应结果
                result = count > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
            }
        } catch (Exception ex) {
            //打印错误信息
            flag = false;
            JzbTools.logError(ex);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "saveCommonProjectList Method", ex.toString()));
        }
        if (userInfo != null) {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
                    userInfo.get("msgTag").toString(), "User Login Message"));
        } else {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
        }
        return result;
    }


    /**
     * 销售/业主-公海-项目-修改
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/updateCommonProjectList", method = RequestMethod.POST)
    @CrossOrigin
    public Response updateCommonProjectList(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/org/CommonProjectList/updateCommonProjectList";
        boolean flag = true;
        try {
            // 如果获取参数userinfo不为空的话
            if (param.get("userinfo") != null) {
                //获取用户信息
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            //如果指定参数为空的话返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"projectid"})) {
                result = Response.getResponseError();
            } else {

                param.put("upduid", userInfo.get("uid"));
                //新建项目
                int count = tbCommonProjectListService.updateCommonProjectList(param);
                //返回响应结果
                result = count > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
            }
        } catch (Exception ex) {
            //打印错误信息
            flag = false;
            JzbTools.logError(ex);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "updateCommonProjectList Method", ex.toString()));
        }
        if (userInfo != null) {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
                    userInfo.get("msgTag").toString(), "User Login Message"));
        } else {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
        }
        return result;
    }

    /**
     * 销售/业主-公海-项目-查询
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getCommonProjectList", method = RequestMethod.POST)
    @CrossOrigin
    public Response getCommonProjectList(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/org/CommonProjectList/getCommonProjectList";
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
            //如果指定参数为空则返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"pageno", "pagesize"})) {
                result = Response.getResponseError();
            } else {

                    int count = JzbDataType.getInteger(param.get("count"));
                // 获取公海里面项目的总数
                count = count < 0 ? 0 : count;
                if (count == 0) {
                    count = tbCommonProjectListService.getCommonProjectListCount(param);
                }
                //设置好分页参数
                SetPageSize setPageSize = new SetPageSize();
                setPageSize.setPagenoSize(param);
                //返回查询出来的结果
                List<Map<String, Object>> list = tbCommonProjectListService.getCommonProjectList(param);

                // 遍历获取地区调用redis返回
                for (int i = 0, l = list.size(); i < l; i++) {
                    Map<String, Object> map = new HashMap<>();
                    //根据地区id从缓存中获取地区的信息
                    Response regionInfo = regionBaseApi.getRegionInfo(list.get(i));
                    // 转map
                    if (regionInfo != null) {
                        list.get(i).put("region", regionInfo.getResponseEntity());
                    }
                }
                //获取用户信息
                userInfo = (Map<String, Object>) param.get("userinfo");
                PageInfo pageInfo = new PageInfo();
                //响应成功信息
                result = Response.getResponseSuccess(userInfo);
                pageInfo.setList(list);
                pageInfo.setTotal(count);
                    result.setPageInfo(pageInfo);
            }
        } catch (Exception ex) {
            //打印错误信息
            flag = false;
            JzbTools.logError(ex);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getCommonProjectList Method", ex.toString()));
        }
        if (userInfo != null) {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
                    userInfo.get("msgTag").toString(), "User Login Message"));
        } else {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
        }
        return result;
    }


    /**
     * 关联单位
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/updateCommonProject", method = RequestMethod.POST)
    @CrossOrigin
    public Response updateCommonProject(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/org/CommonProjectList/updateCommonProject";
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
            List<Map<String, Object>> list = (List<Map<String, Object>>) param.get("list");
            //把单位的cid添加到项目表中
            int count = tbCommonProjectListService.updateCommonProject(list);
             //获取用户信息
            userInfo = (Map<String, Object>) param.get("userinfo");
            result = count > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
        } catch (Exception ex) {
            //打印错误信息
            flag = false;
            JzbTools.logError(ex);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "updateCommonProject Method", ex.toString()));
        }
        if (userInfo != null) {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
                    userInfo.get("msgTag").toString(), "User Login Message"));
        } else {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
        }
        return result;
    }

    /**
     * 取消关联单位
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/updateCommonProjects", method = RequestMethod.POST)
    @CrossOrigin
    public Response updateCommonProjects(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/org/CommonProjectList/updateCommonProjects";
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
            List<Map<String, Object>> list = (List<Map<String, Object>>) param.get("list");
            //把cid从项目中取消
            int count = tbCommonProjectListService.updateCommonProjects(list);
            //获取用户信息
            userInfo = (Map<String, Object>) param.get("userinfo");
            result = count > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
        } catch (Exception ex) {
            //打印错误信息
            flag = false;
            JzbTools.logError(ex);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "updateCommonProjects Method", ex.toString()));
        }
        if (userInfo != null) {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
                    userInfo.get("msgTag").toString(), "User Login Message"));
        } else {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
        }
        return result;
    }

}
