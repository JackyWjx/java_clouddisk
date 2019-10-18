package com.jzb.message.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.data.code.JzbDataCheck;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import com.jzb.message.service.MessageListService;
import com.jzb.message.service.ShortMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @Description: 用户消息控制层
 * @Author   tang sheng jun
 */
@Controller
@RequestMapping("/message/list")
public class MessageListController {


    @Autowired
    private MessageListService messageListService;

    @Autowired
    private ShortMessageService smsService;

    /**
     * 查询
     */

    @RequestMapping(value = "/queryMsgList", method = RequestMethod.POST)
    @ResponseBody
    public Response queryMsgList(@RequestBody Map<String, Object> map) {
        Response response;
        try {
            List<Map<String, Object>> list;
            PageInfo info = new PageInfo();
            info.setPages(JzbDataType.getInteger(map.get("pageno")) == 0 ? 1 : JzbDataType.getInteger(map.get("pageno")));
            response = Response.getResponseSuccess();
            if (map.containsKey("sendname") && !JzbTools.isEmpty(map.get("sendname"))) {
                list = messageListService.searchMsgList(map);
                if (JzbDataType.getInteger(map.get("count")) == 0) {
                    int count = messageListService.searchMsgListCount(map);
                    info.setTotal(count);
                }
            } else {
                list = messageListService.queryMsgList(map);
                if (JzbDataType.getInteger(map.get("count")) == 0) {
                    int count = messageListService.queryMsgListCount(map);
                    info.setTotal(count);
                }
            }
            info.setList(list);
            response.setPageInfo(info);
        } catch (Exception e) {
            e.printStackTrace();
            response = Response.getResponseError();
        }
        return response;
    }

}
