package com.jzb.org.controller;

import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.org.service.TenderAndDescService;
import com.jzb.org.service.TenderResultAndDescService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.zip.DataFormatException;

@RestController
@RequestMapping(value = "/org/TenderMessage")
public class TbTenderResultAndDescController {

    @Autowired
    private TenderResultAndDescService tenderResultAndDescService;

    @Autowired
    private TenderAndDescService tenderAndDescService;

    @RequestMapping(value = "/addTenderMessage", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response addTenderMessage(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");

        try {
            if(param.get("type").equals("2")){
                //中标
                if (param.get("projecttype") == null || param.get("title") == null) {
                    result = Response.getResponseError();
                } else {
                    param.put("tendid", JzbRandom.getRandomChar(32));
                    param.put("adduid",userInfo.get("uid"));
                    param.put("status", "1");
                    param.put("addtime", System.currentTimeMillis());
                    Integer changeNum = tenderResultAndDescService.addTenderMessage(param);
                    // 定义返回结果
                    result = Response.getResponseSuccess(userInfo);
                }
            }else if(param.get("type").equals("1")){
                //招標
                if (param.get("projecttype") == null || param.get("title") == null) {
                    result = Response.getResponseError();
                } else {
                    param.put("tendid", JzbRandom.getRandomChar(32));
                    param.put("adduid",userInfo.get("uid"));
                    param.put("status", "1");
                    param.put("addtime", System.currentTimeMillis());
                    Integer changeNum = tenderAndDescService.addTenderMessage(param);
                    result = Response.getResponseSuccess(userInfo);
                }
            }else {
                result=Response.getResponseError();
            }

        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }


    @RequestMapping(value = "/getTenderMessage", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response getTenderMessage(@RequestBody Map<String, Object> param) {
        Response result;

        try {
            if (param.get("pageno") == null || param.get("pagesize") == null ) {
                result = Response.getResponseError();
            } else {
                List<Map<String,Object>> tenderMessages = tenderResultAndDescService.queryTenderMessage(param);
                // 定义返回结果
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                result = Response.getResponseSuccess(userInfo);
                for (Map<String,Object> tenderMessage:tenderMessages) {
                    Date date = new Date();
                    Long dateNum = (Long) tenderMessage.get("opendate");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    date.setTime(dateNum);//java里面应该是按毫秒
                    tenderMessage.put("opendate",sdf.format(date));
                }
                // 定义pageinfo
                PageInfo pi=new PageInfo();

                pi.setList(tenderMessages);

                // 如果有一个指定参数不为空，则返回list.size()  否则返回总数
                pi.setTotal(tenderResultAndDescService.queryTenderMessageCount(param));
                result.setPageInfo(pi);
            }
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }


    @RequestMapping(value = "/putTenderMessage", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response putTenderMessage(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");

        try {
            if(param.get("type").equals("2")){
                //中标
                if (param.get("tendid") == null) {
                    result = Response.getResponseError();
                } else {
                    param.put("upduid",userInfo.get("uid"));
                    param.put("updtime", System.currentTimeMillis());
                    tenderResultAndDescService.putTenderMessage(param);
                    // 定义返回结果
                    result = Response.getResponseSuccess(userInfo);
                }
            }else if(param.get("type").equals("1")){
                //招標
                if (param.get("tendid") == null ) {
                    result = Response.getResponseError();
                } else {
                    param.put("upduid",userInfo.get("uid"));
                    param.put("updtime", System.currentTimeMillis());
                    tenderAndDescService.putTenderMessage(param);
                    result = Response.getResponseSuccess(userInfo);
                }
            }else {
                result=Response.getResponseError();
            }

        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }
 @RequestMapping(value = "/delTenderMessage", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response delTenderMessage(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");

        try {
            if(param.get("type").equals("2")){
                //中标
                if (param.get("tendid") == null) {
                    result = Response.getResponseError();
                } else {
                    tenderResultAndDescService.delTenderMessage(param);
                    // 定义返回结果
                    result = Response.getResponseSuccess(userInfo);
                }
            }else if(param.get("type").equals("1")){
                //招標
                if (param.get("tendid") == null ) {
                    result = Response.getResponseError();
                } else {
                    tenderAndDescService.delTenderMessage(param);
                    result = Response.getResponseSuccess(userInfo);
                }
            }else {
                result=Response.getResponseError();
            }

        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

}
