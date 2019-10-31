package com.jzb.message.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import com.jzb.message.service.SendUserMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author tang sheng jun
 */
@Controller
@RequestMapping("/senduser/message")
public class SendUserMessageController {

    private final static Logger logger= LoggerFactory.getLogger(SendUserMessageController.class);

    @Autowired
    private SendUserMessageService service;
    /**
     * 查询
     *
     *  map 用户参数
     */
    @RequestMapping(value = "/querySendUserMessage", method = RequestMethod.POST)
    @ResponseBody
    public Response querySendUserMessage(@RequestBody Map<String, Object> map) {
        Response response;
        try {

            logger.info("==============>>querySendUserMessage");
            List<Map<String, Object>> list;
            PageInfo info = new PageInfo();
            info.setPages(JzbDataType.getInteger(map.get("pageno")) == 0 ? 1 : JzbDataType.getInteger(map.get("pageno")));
            response = Response.getResponseSuccess();
            if (map.containsKey("receivename") && !JzbTools.isEmpty(map.get("receivename"))) {
                list = service.searchSendUserMessage(map);
                if (JzbDataType.getInteger(map.get("count")) == 0) {
                    int count = service.searchSendUserMessageCount(map);
                    info.setTotal(count);
                }
            } else {
                list = service.querySendUserMessage(map);
                if (JzbDataType.getInteger(map.get("count")) == 0) {
                    int count = service.querySendUserMessageCount(map);
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
