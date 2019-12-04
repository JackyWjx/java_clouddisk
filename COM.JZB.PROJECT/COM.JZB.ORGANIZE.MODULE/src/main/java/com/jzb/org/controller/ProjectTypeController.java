package com.jzb.org.controller;

import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
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

@RequestMapping("/org/projectType")
@RestController
public class ProjectTypeController {

    @Autowired
    private ProjectTypeService projectTypeService;

    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(ProjectTypeController.class);



    @RequestMapping(value = "/getProjectType", method = RequestMethod.POST)
    @CrossOrigin
    public Response getProjectType(@RequestBody(required = false) Map<String, Object> param) {
        Response result;
        String  api="/org/projectType/getProjectType";
        Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
        try {
            if(!"".equals(param.get("pageno"))&&param.get("pageno")!=null){
                int pageno = (int)param.get("pageno")-1;
                int pagesize = (int)param.get("pagesize");
                param.put("start",pageno*pagesize);
            }
            List<Map<String, Object>> ProjectType = projectTypeService.queryProjectType(param);
            result = Response.getResponseSuccess(userInfo);
            for (Map<String,Object> pt:ProjectType) {
                Date date = new Date();
                Long dateNum = (Long) pt.get("addtime");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                date.setTime(dateNum);//java里面应该是按毫秒
                pt.put("addtime",sdf.format(date));
            }
            // 定义pageinfo
            PageInfo pi=new PageInfo();
            pi.setList(ProjectType);
            // 如果有一个指定参数不为空，则返回list.size()  否则返回总数
            pi.setTotal(projectTypeService.quertProjectTypeCount(param));
            result.setPageInfo(pi);
            logger.info(JzbLoggerUtil.getApiLogger( api, "1", "INFO",
                    userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User select ProjectType"));
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getProjectType Method", "[sql select error]"));
        }
        return result;
    }

    @RequestMapping(value = "/addProjectType", method = RequestMethod.POST)
    @CrossOrigin
    public Response addProjectType(@RequestBody Map<String, Object> param) {
        Response result;
        String  api="/org/projectType/addProjectType";
        Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
        try {
            if(!"".equals(param.get("cname"))&&param.get("cname")!=null){
                param.put("adduid", userInfo.get("uid"));
                param.put("addtime", System.currentTimeMillis());
                param.put("status", "1");
                param.put("typeid", projectTypeService.selectMaxNum() + 1);
                projectTypeService.addProjectType(param);
                result = Response.getResponseSuccess(userInfo);
                logger.info(JzbLoggerUtil.getApiLogger( api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User add ProjectType"));
            }else {
                result= Response.getResponseError();
            }
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getProjectType Method", "[sql add error]"));
        }
        return result;
    }


    @RequestMapping(value = "/delProjectType", method = RequestMethod.POST)
    @CrossOrigin
    public Response delProjectType(@RequestBody Map<String, Object> param) {
        Response result;
        String  api="/org/ProjectType/addProjectType";
        Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
        try {
            String typeId = (String) param.get("typeid");
            Integer changeNum = projectTypeService.delProjectType(typeId);
            result = Response.getResponseSuccess(userInfo);
            logger.info(JzbLoggerUtil.getApiLogger( api, "1", "INFO",
                    userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User del ProjectType"));
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getProjectType Method", "[sql del error]"));

        }
        return result;
    }

    @RequestMapping(value = "/putProjectType", method = RequestMethod.POST)
    @CrossOrigin
    public Response putProjectType(@RequestBody Map<String, Object> param) {
        Response result;
        String  api="/org/ProjectType/putProjectType";
        Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
        try {
            param.put("upduid", userInfo.get("uid"));
            param.put("updtime", System.currentTimeMillis());
            Integer changeNum = projectTypeService.putProjectType(param);
            result = Response.getResponseSuccess(userInfo);
            logger.info(JzbLoggerUtil.getApiLogger( api, "1", "INFO",
                    userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User update ProjectType"));
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getProjectType Method", "[sql update error]"));
        }
        return result;
    }
}
