package com.jzb.resource.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.entity.uploader.FileInfo;
import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbPageConvert;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.resource.service.TbDocumentMsgFileInfoService;
import com.jzb.resource.service.TbDocumentMsgLookfileService;
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
 * @Author wang jixiang
 * @Date 20191218
 * 体系建设-文件目录的读取，修改，删除
 */
@RequestMapping("/resource/lookFile")
@RestController
public class TbDocumentMsgLookfileController {

    @Autowired
    private TbDocumentMsgLookfileService tbDocumentMsgLookfileService;

    private final static Logger logger = LoggerFactory.getLogger(TbDocumentMsgUsercollectController.class);


    @RequestMapping(value = "/getlookFile", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response getlookFile(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/resource/lookFile/getlookFile";
        boolean flag = true;
        try {
            if (param != null && param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            if(JzbCheckParam.haveEmpty(param,new String[]{"pageno","pagesize"})){
                response=Response.getResponseError();
                logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getContractTemplate Method", "[param error] or [param is null]"));
            }else {
                JzbPageConvert.setPageRows(param);
                param.put("uid",userInfo.get("uid"));
                List<Map<String,Object>> result = tbDocumentMsgLookfileService.getLookHistory(param);
                response = Response.getResponseSuccess();
                PageInfo pageInfo = new PageInfo();
                pageInfo.setList(result);
                pageInfo.setTotal(tbDocumentMsgLookfileService.getLookHistoryCount(param));
                response.setPageInfo(pageInfo);
            }
        } catch (Exception ex) {
            flag = false;
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
