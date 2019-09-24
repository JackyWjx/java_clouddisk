package com.jzb.org.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.data.date.JzbDateStr;
import com.jzb.base.data.date.JzbDateUtil;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbTools;
import com.jzb.org.service.TenderDescService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 招标详情
 */
@RestController
@RequestMapping(value = "/org/tenderDesc")
public class TbTenderDescController {

    @Autowired
    private TenderDescService tenderDescService;

    /**
     * 获取招标详情
     * @param params
     * @return
     */
    @RequestMapping(value = "/getTenderDesc", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response getTenderDesc(@RequestBody Map<String, Object> params) {
        Response result;
        try {
            // 判断指定参数为空则返回error
            if (JzbCheckParam.haveEmpty(params,new String[]{"tendid"})) {
                result = Response.getResponseError();
            } else {
                // 定义返回结果

                result = Response.getResponseSuccess((Map<String, Object>) params.get("userinfo"));
                Map<String, Object> map = tenderDescService.getTenderDesc(params);
                map.put("opendate", JzbDateUtil.toDateString(JzbDataType.getLong(map.get("opendate")),JzbDateStr.yyyy_MM_dd));
                map.put("addtime", JzbDateUtil.toDateString(JzbDataType.getLong(map.get("addtime")),JzbDateStr.yyyy_MM_dd));
                map.put("content",map.get("content").toString().replace("\\n",""));
                result.setResponseEntity(map);
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }
}
