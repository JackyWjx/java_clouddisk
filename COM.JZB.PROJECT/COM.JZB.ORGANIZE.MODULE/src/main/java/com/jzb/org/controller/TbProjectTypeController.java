package com.jzb.org.controller;

import com.alibaba.fastjson.JSONObject;
import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import com.jzb.org.service.TbProjectTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author chenzhengduan
 *  获取公告类型  施工。。。。等
 */
@RestController
@RequestMapping(value = "/org/projectType")
public class TbProjectTypeController {

    @Autowired
    private TbProjectTypeService tbProjectTypeService;

    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(TbProjectTypeController.class);

    /**
     * 获取公告类型
     * @param param
     * @return
     */
    @RequestMapping(value = "/getProjectTypeList",method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response getProjectTypeList(@RequestBody(required = false) Map<String, Object> param){
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/org/projectType/getProjectTypeList";
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
            // 定义返回结果
            result = Response.getResponseSuccess();
            // 获取结果集
            List<Map<String, Object>> list = tbProjectTypeService.getProjectType();
            result.setResponseEntity(list);
        }catch (Exception ex){
            flag = false;
            JzbTools.logError(ex);
            result=Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getProjectTypeList Method", ex.toString()));
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
     * 获取评标办法
     * @param param
     * @return
     */
    @RequestMapping(value = "/getProjectMethodList")
    @ResponseBody
    @CrossOrigin
    public Response getProjectMethodList(@RequestBody(required = false) Map<String, Object> param){
        Response result;
        try {
            // 定义返回结果
            result = Response.getResponseSuccess();
            // 获取结果集
            JSONObject object=JSONObject.parseObject("{\"responseEntity\":[{\"cname\":\"综合评估法I\",\"typeid\":\"01\"},{\"cname\":\"综合评估法II\",\"typeid\":\"02\"},{\"cname\":\"固定标价评分法\",\"typeid\":\"03\"},{\"cname\":\"合理定价抽取法\",\"typeid\":\"04\"},{\"cname\":\"技术评分最低标价法\",\"typeid\":\"05\"},{\"cname\":\"合理低价法\",\"typeid\":\"06\"},{\"cname\":\"经评审最低报价法\",\"typeid\":\"07\"},{\"cname\":\"百分之综合评分法\",\"typeid\":\"08\"},{\"cname\":\"其他\",\"typeid\":\"09\"}]}");
            result.setResponseEntity(object);
        }catch (Exception ex){
            JzbTools.logError(ex);
            result=Response.getResponseError();
        }
        return result;
    }
}
