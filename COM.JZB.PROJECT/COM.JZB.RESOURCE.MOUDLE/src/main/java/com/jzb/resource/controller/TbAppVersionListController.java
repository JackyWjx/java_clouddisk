package com.jzb.resource.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import com.jzb.resource.service.TbAppVersionListService;
import com.netflix.discovery.converters.jackson.EurekaXmlJacksonCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chenzhengduan
 * @desc App版本库
 */
@RequestMapping("/resource/appVersion")
@RestController
public class TbAppVersionListController {

    @Autowired
    private TbAppVersionListService tbAppVersionListService;

    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(TbAppVersionListController.class);


    @RequestMapping(value = "/getNewAppVersion", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response getNewAppVersion(@RequestBody(required = false) Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/resource/appVersion/getNewAppVersion";
        boolean flag = true;
        try {
            if (param!=null&&param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            /** 获取App版本信息 */
            Map<String, Object> map = tbAppVersionListService.queryAppVersion();
            Map<String, Object> newMap=new HashMap<>();
            /** 命名规范 */
            newMap.put("versionName",map.get("vername"));
            newMap.put("versionCode",map.get("vercode"));
            newMap.put("description",JzbDataType.getString(map.get("verdesc")).replace("\\n","\n"));
            newMap.put("upgradeType", JzbDataType.getInteger(map.get("isupdate")));
            newMap.put("filePath",map.get("verurl"));
            response = Response.getResponseSuccess();
            response.setResponseEntity(newMap);
        } catch (Exception ex) {
            flag=false;
            JzbTools.logError(ex);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getNewAppVersion Method", ex.toString()));
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
