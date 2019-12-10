package com.jzb.org.controller;

import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbPageConvert;
import com.jzb.base.util.JzbTools;
import com.jzb.org.service.ProjectTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author wangjixiang
 * 公告类型增删改
 */
@RequestMapping("/org/projectType")
@RestController
public class ProjectTypeController {

    @Autowired
    private ProjectTypeService projectTypeService;

    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(ProjectTypeController.class);


    /**
     * 获取公告类型
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getProjectType", method = RequestMethod.POST)
    @CrossOrigin
    public Response getProjectType(@RequestBody(required = false) Map<String, Object> param) {
        Response result;
        String api = "/org/projectType/getProjectType";
        Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
        try {
            if (JzbCheckParam.haveEmpty(param, new String[]{"pageno", "pagesize"})) {
                result = Response.getResponseError();
                logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getProjectType Method", "[必要参数为空]"));
            } else {
                JzbPageConvert.setPageRows(param);
                List<Map<String, Object>> ProjectType = projectTypeService.queryProjectType(param);
                result = Response.getResponseSuccess(userInfo);
                for (Map<String, Object> pt : ProjectType) {
                    Date date = new Date();
                    Long dateNum = (Long) pt.get("addtime");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    date.setTime(dateNum);//java里面应该是按毫秒
                    pt.put("addtime", sdf.format(date));
                }
                // 定义pageinfo
                PageInfo pi = new PageInfo();
                pi.setList(ProjectType);
                // 如果有一个指定参数不为空，则返回list.size()  否则返回总数
                pi.setTotal(projectTypeService.quertProjectTypeCount(param));
                result.setPageInfo(pi);
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User select ProjectType"));

            }
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getProjectType Method", "[查询公告类型异常]"));
        }
        return result;
    }

    /**
     * 增加公告类型
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/addProjectType", method = RequestMethod.POST)
    @CrossOrigin
    public Response addProjectType(@RequestBody Map<String, Object> param) {
        Response result;
        String api = "/org/projectType/addProjectType";
        Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
        try {
            if (!"".equals(param.get("cname")) && param.get("cname") != null) {
                param.put("adduid", userInfo.get("uid"));
                param.put("addtime", System.currentTimeMillis());
                param.put("status", "1");
                param.put("typeid", projectTypeService.selectMaxNum() + 1);
                projectTypeService.addProjectType(param);
                result = Response.getResponseSuccess(userInfo);
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User add ProjectType"));
            } else {
                result = Response.getResponseError();
                logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getProjectType Method", "[cname  参数未传输]"));
            }
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getProjectType Method", "增加公告类型异常"));
        }
        return result;
    }


    /**
     * 删除公告类型
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/delProjectType", method = RequestMethod.POST)
    @CrossOrigin
    public Response delProjectType(@RequestBody Map<String, Object> param) {
        Response result;
        String api = "/org/ProjectType/delProjectType";
        Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
        try {
            result = projectTypeService.delProjectType(param)>0?Response.getResponseSuccess(userInfo):Response.getResponseError();
            logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                    userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User del ProjectType"));
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getProjectType Method", "[删除公告类型异常]"));
        }
        return result;
    }

    /**
     * 修改公告类型
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/putProjectType", method = RequestMethod.POST)
    @CrossOrigin
    public Response putProjectType(@RequestBody Map<String, Object> param) {
        Response result;
        String api = "/org/ProjectType/putProjectType";
        Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
        try {
            param.put("upduid", userInfo.get("uid"));
            param.put("updtime", System.currentTimeMillis());
            result = projectTypeService.putProjectType(param)>0?Response.getResponseError():Response.getResponseSuccess(userInfo);
            logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                    userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User update ProjectType"));
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getProjectType Method", "[修改公告类型异常]"));
        }
        return result;
    }
}
