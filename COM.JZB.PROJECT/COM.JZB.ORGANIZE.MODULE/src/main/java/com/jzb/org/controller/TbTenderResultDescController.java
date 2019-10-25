package com.jzb.org.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.data.date.JzbDateStr;
import com.jzb.base.data.date.JzbDateUtil;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbTools;
import com.jzb.org.service.TenderResultDescService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 获取中标详情
 */
@RestController
@RequestMapping(value = "/org/tenderResultDesc")
public class TbTenderResultDescController {

    @Autowired
    private TenderResultDescService tenderResultDescService;

    /**
     * 获取中标信息详情
     * @param params
     * @return
     */
    @RequestMapping(value = "/getTenderResultDesc", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response getTenderResultDesc(@RequestBody Map<String, Object> params) {
        Response result;
        try {
            // 如果指定参数为空则返回error
            if (JzbCheckParam.haveEmpty(params,new String[]{"tendid"})) {
                result = Response.getResponseError();
            } else {
                Map<String, Object> map = tenderResultDescService.getTenderResultDesc(params);
                map.put("opendate", JzbDateUtil.toDateString(JzbDataType.getLong(map.get("opendate")), JzbDateStr.yyyy_MM_dd));
                map.put("addtime", JzbDateUtil.toDateString(JzbDataType.getLong(map.get("addtime")),JzbDateStr.yyyy_MM_dd));
                if(map.get("content")!=null){
                    map.put("content",map.get("content").toString().replace("\\n",""));
                }
                // 定义返回结果
                result = Response.getResponseSuccess((Map<String, Object>) params.get("userinfo"));
                result.setResponseEntity(map);
            }

        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }
}
