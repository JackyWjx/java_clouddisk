package com.jzb.operate.controller;

import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import com.jzb.operate.service.TbHandleItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/opt/HandleItem")
public class TbHandleItemController {

    @Autowired
    private TbHandleItemService tbHandleItemService;

    /**
     * 销售统计分析跟进人详情查询
     * @param param
     * @return
     */
    @RequestMapping(value = "/getHandleItem",method = RequestMethod.POST)
    @CrossOrigin
    public Response getHandleItem(@RequestBody Map<String, Object> param) {
        Response result;
        try {
           Map<String,Object> map = tbHandleItemService. getHandleItem(param);
            PageInfo pageInfo = new PageInfo();
            //获取用户信息
            //Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            //响应成功信息
            result = Response.getResponseSuccess();
            result.setResponseEntity(map);
        } catch (Exception e) {
            //打印错误信息
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }
}
