package com.jzb.message.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import com.jzb.message.service.SendUserMessageService;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.omg.CORBA.OBJ_ADAPTER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
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
    @RequestMapping(value = "/queryUserMessage", method = RequestMethod.POST)
    @ResponseBody
    public Response queryUserMessage(@RequestBody Map<String, Object> map) {
        Response response;
        try {
            logger.info("==============>>querySendUserMessage");
            List<Map<String, Object>> list;
            PageInfo info = new PageInfo();
            info.setPages(JzbDataType.getInteger(map.get("pageno")) == 0 ? 1 : JzbDataType.getInteger(map.get("pageno")));
            response = Response.getResponseSuccess((Map)map.get("userinfo"));
            // 获取所有信息
            map.put("receiveuid",((Map) map.get("userinfo")).get("uid"));
            list = service.queryUserMessage(map);
            for (int i = 0 ; i < list.size() ;i++){
                Map<String , Object> summaryMap  = JSONObject.fromObject( list.get(i).get("summary").toString());
                list.get(i).put("summary",summaryMap);
            }
            int count = service.queryUserMessageCount(map);
            // 获取所有未读
            int noCount = service.querySendCount(map);
            info.setTotal(count);
            info.setList(list);
            response.setPageInfo(info);
            Map<String , Object> para =  new HashMap<>();
            para.put("count",noCount);
            response.setResponseEntity(para);
        } catch (Exception e) {
            e.printStackTrace();
            response = Response.getResponseError();
        }
        return response;
    }

    /**
     * 修改
     *
     *  map 用户参数
     */
    @RequestMapping(value = "/upUserMessage", method = RequestMethod.POST)
    @ResponseBody
    public Response upUserMessage(@RequestBody Map<String, Object> map) {
        Response response;
        try {
            logger.info("==============>>upUserMessage"+map.toString());
            response = Response.getResponseSuccess((Map)map.get("userinfo"));
            int count = service.updateSendCount(map);
            map.clear();
            map.put("count",count);
            response.setResponseEntity(map);
        } catch (Exception e) {
            e.printStackTrace();
            response = Response.getResponseError();
        }
        return response;
    }


    /**
     * 删除
     *
     *  map 用户参数
     */
    @RequestMapping(value = "/reUserMessage", method = RequestMethod.POST)
    @ResponseBody
    public Response reUserMessage(@RequestBody Map<String, Object> map) {
        Response response;
        try {
            logger.info("==============>>upUserMessage");
            response = Response.getResponseSuccess((Map)map.get("userinfo"));
            int count = service.deleteSend(map);
            map.clear();
            map.put("count",count);
            response.setResponseEntity(map);
        } catch (Exception e) {
            e.printStackTrace();
            response = Response.getResponseError();
        }
        return response;
    }


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
