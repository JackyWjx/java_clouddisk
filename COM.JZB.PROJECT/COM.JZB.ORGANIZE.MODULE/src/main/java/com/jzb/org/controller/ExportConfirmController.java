package com.jzb.org.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbTools;
import com.jzb.org.service.ExportConfirmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author dell
 * @Version 1.0
 * @Since 1.0
 * @Date: 2019/8/29 20:42
 */
@RestController
@RequestMapping("/org/export")
public class ExportConfirmController {
    @Autowired
    private ExportConfirmService exportConfirmService;

    /**
     * 根据企业id和批次id获取导入信息
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getExportUser", method = RequestMethod.POST)
    @CrossOrigin
    public Response getExportUser(@RequestBody Map<String, Object> param) {
        Response result;
        PageInfo info;
        try {
            String[] str = {"batchid"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                int rows = JzbDataType.getInteger(param.get("pagesize"));
                int page = JzbDataType.getInteger(param.get("pageno"));
                if (page > 0 && rows > 0) {
                    param.put("start", rows * (page - 1));
                    param.put("pagesize", rows);
                    List<Map<String, Object>> exportList = exportConfirmService.queryExportList(param);
                    result = Response.getResponseSuccess(userInfo);
                    info = new PageInfo();
                    info.setList(exportList);
                    int count = JzbDataType.getInteger(param.get("count"));
                    if (count == 0) {
                        int size = exportConfirmService.queryExportCount(param);
                        info.setTotal(size > 0 ? size : exportList.size());
                    }
                    result.setPageInfo(info);
                } else {
                    result = Response.getResponseError();
                }
            } else {
                result = Response.getResponseError();
            }
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 获取批次信息
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getExportBatch", method = RequestMethod.POST)
    @CrossOrigin
    public Response getExportBatch(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String[] str = {"batchid"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                List<Map<String, Object>> batchList = exportConfirmService.queryExportBatch(param);
                Map<String, Object> batchMap = new HashMap<>();
                if (batchList.size() > 0) {
                    batchMap = batchList.get(0);
                }
                result = Response.getResponseSuccess(userInfo);
                result.setResponseEntity(batchMap);
            } else {
                result = Response.getResponseError();
            }
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 确认修改批量导入的用户信息
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/modifyExport", method = RequestMethod.POST)
    @CrossOrigin
    public Response modifyExport(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String[] str = {"batchid", "cid"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                param.put("uid", userInfo.get("uid"));
                Map<String, Integer> data = exportConfirmService.updateExport(param);
                if (data.get("info") == 0 && data.get("invite") == 0 && data.get("dept") == 0) {
                    result = Response.getResponseError();
                } else {
                    result = Response.getResponseSuccess(userInfo);
                }
                result.setResponseEntity(data);
            } else {
                result = Response.getResponseError();
            }
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }


    /**
     * 注册用户并发送短信
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     * @DateTime: 2019/10/12 15:34
     */
    @RequestMapping(value = "/addAllUserAndSend", method = RequestMethod.POST)
    @CrossOrigin
    public Response addAllUserAndSend(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String[] str = {"phone", "name"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                param.put("uid", userInfo.get("uid"));
                result = exportConfirmService.addUserAndSend(param);
            } else {
                result = Response.getResponseError();
            }
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }
}
