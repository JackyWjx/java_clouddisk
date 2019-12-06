package com.jzb.org.controller;

import com.alibaba.fastjson.JSON;
import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbPageConvert;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.org.api.base.RegionBaseApi;
import com.jzb.org.api.redis.TbCityRedisApi;
import com.jzb.org.service.TenderAndDescService;
import com.jzb.org.service.TenderResultAndDescService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.DataFormatException;

/**
 * @author wang jixiang
 * 招投标增删改查
 */
@RestController
@RequestMapping(value = "/org/TenderMessage")
public class TbTenderResultAndDescController {

    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(TbTrackUserListController.class);

    @Autowired
    private TenderResultAndDescService tenderResultAndDescService;

    @Autowired
    private TenderAndDescService tenderAndDescService;

    @Autowired
    private TbCityRedisApi tbCityRedisApi;

    @Autowired
    private RegionBaseApi regionBaseApi;

    /**
     * 保存招投标信息type=1招标，type=2中标
     * @param param
     * @return
     */
    @RequestMapping(value = "/addTenderMessage", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response addTenderMessage(@RequestBody Map<String, Object> param) {
        Response result;
        String api = "/org/TenderMessage/addTenderMessage";
        Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
        ArrayList<String> region = (ArrayList<String>) param.get("region");
        if (!("").equals(region) && region != null) {
            if (region.size() == 1) {
                param.put("provice", region.get(0));
            } else if (region.size() == 2) {
                param.put("provice", region.get(0));
                param.put("city", region.get(1));
            }
        }
        try {
            if (JzbCheckParam.haveEmpty(param, new String[]{"type"})) {
                result = Response.getResponseError();
            } else {
                if (param.get("type").equals("2")) {
                    //中标
                    if (param.get("projecttype") == null || param.get("title") == null) {
                        result = Response.getResponseError();
                    } else {
                        param.put("tendid", JzbRandom.getRandomChar(32));
                        param.put("adduid", userInfo.get("uid"));
                        param.put("status", "1");
                        param.put("addtime", System.currentTimeMillis());
                        Integer changeNum = tenderResultAndDescService.addTenderMessage(param);
                        // 定义返回结果
                        result = Response.getResponseSuccess(userInfo);
                    }
                    logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                            userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "tender result Message"));
                } else if (param.get("type").equals("1")) {
                    //招標
                    if (param.get("projecttype") == null || param.get("title") == null) {
                        result = Response.getResponseError();
                    } else {
                        param.put("tendid", JzbRandom.getRandomChar(32));
                        param.put("adduid", userInfo.get("uid"));
                        param.put("status", "1");
                        param.put("addtime", System.currentTimeMillis());
                        tenderAndDescService.addTenderMessage(param);
                        result = Response.getResponseSuccess(userInfo);
                    }
                    logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                            userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "tender Message"));
                } else {
                    result = Response.getResponseError();
                    logger.error(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                            userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "data error"));
                }

            }
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                    userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "add tender or tenderresult error"));
        }
        return result;
    }

    /**
     * 获取招标中标数据，当type=1时，获取招标数据，type=2时，中标数据，其余，查询全部
     * @param param
     * @return
     */
    @RequestMapping(value = "/getTenderMessage", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response getTenderMessage(@RequestBody Map<String, Object> param) {
        Response result;
        // 定义返回结果
        Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
        String api = "/org/TenderMessage/getTenderMessage";
        try {

            if (JzbCheckParam.haveEmpty(param, new String[]{"pagesize", "pageno","type"})) {
                result = Response.getResponseError();
                logger.error(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "data error"));

            } else {
                JzbPageConvert.setPageRows(param);
                List<Map<String, Object>> tenderMessages = tenderResultAndDescService.queryTenderMessage(param);

                result = Response.getResponseSuccess(userInfo);
                for (Map<String, Object> tenderMessage : tenderMessages) {
                    Date date = new Date();
                    Long dateNum = (Long) tenderMessage.get("opendate");
                    if (!("").equals(dateNum) && dateNum != null) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        date.setTime(dateNum);//java里面应该是按毫秒
                        tenderMessage.put("opendate", sdf.format(date));
                    }


                    Map<String, Object> regionMap = new HashMap<>();
                    regionMap.put("key", tenderMessage.get("city") == null ? tenderMessage.get("provice") : tenderMessage.get("city"));
                    Response proviceList = tbCityRedisApi.getCityList(regionMap);

                    Map<String, Object> resultParam = null;
                    if (proviceList.getResponseEntity() != null) {
                        resultParam = (Map<String, Object>) JSON.parse(proviceList.getResponseEntity().toString());
                        if (resultParam != null && resultParam.get("province") != null) {
                            tenderMessage.put("provice", resultParam.get("province"));
                        }
                        if (resultParam != null && resultParam.get("city") != null) {
                            tenderMessage.put("city", resultParam.get("city"));
                        }
                    }


                }
                // 定义pageinfo
                PageInfo pi = new PageInfo();

                pi.setList(tenderMessages);

                // 如果有一个指定参数不为空，则返回list.size()  否则返回总数
                pi.setTotal(tenderResultAndDescService.queryTenderMessageCount(param));
                result.setPageInfo(pi);
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "select data message"));
            }
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                    userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "data error"));
        }
        return result;
    }

    /**
     * 修改招投标信息，type=1修改招标信息，type=2时修改中标
     * @param param
     * @return
     */
    @RequestMapping(value = "/putTenderMessage", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response putTenderMessage(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
        String api = "/org/TenderMessage/putTenderMessage";
        try {
            if(JzbCheckParam.haveEmpty(param,new String[]{"type"})){
                result = Response.getResponseError();
                logger.error(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "data error"));
            }else {
                if (param.get("type").equals("2")) {
                    //中标
                    if (param.get("tendid") == null) {
                        result = Response.getResponseError();
                        logger.error(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                                userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "data error"));
                    } else {
                        param.put("upduid", userInfo.get("uid"));
                        param.put("updtime", System.currentTimeMillis());
                        tenderResultAndDescService.putTenderMessage(param);
                        // 定义返回结果
                        result = Response.getResponseSuccess(userInfo);
                        logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                                userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "put tenderResult"));
                    }
                } else if (param.get("type").equals("1")) {
                    //招標
                    if (param.get("tendid") == null) {
                        result = Response.getResponseError();
                        logger.error(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                                userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "data error"));
                    } else {
                        param.put("upduid", userInfo.get("uid"));
                        param.put("updtime", System.currentTimeMillis());
                        tenderAndDescService.putTenderMessage(param);
                        result = Response.getResponseSuccess(userInfo);
                        logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                                userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "put tender"));
                    }
                } else {
                    result = Response.getResponseError();
                    logger.error(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                            userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "data error"));
                }
            }


        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                    userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "data error"));
        }
        return result;
    }

    /**
     * 删除中标投标信息，前端传输集合，根据集合中的type值，分为招标中标信息，在进行删除
     * @param param
     * @return
     */
    @RequestMapping(value = "/delTenderMessage", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response delTenderMessage(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
        String api = "/org/TenderMessage/delTenderMessage";
        List<Map<String,Object>> list = (List<Map<String, Object>>) param.get("list");
        List<String> tenderMessage = new ArrayList<>();
        List<String> tenderResultMessage = new ArrayList<>();
        try {
            for (Map map : list) {
                if(map.get("type").equals("1")){
                    tenderMessage.add((String) map.get("tendid"));
                }else if(map.get("type").equals("2")){
                    tenderResultMessage.add((String) map.get("tendid"));
                }else {
                    result = Response.getResponseError();
                    logger.error(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                            userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "data error"));
                }
            }
            if(tenderMessage.size()>0){
                param.put("tendids",tenderMessage);
                tenderAndDescService.delTenderMessage(param);
            }
            if(tenderResultMessage.size()>0){
                param.put("tendids",tenderResultMessage);
                tenderResultAndDescService.delTenderMessage(param);
            }
            result = Response.getResponseSuccess(userInfo);
            logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                    userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "delete message"));
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                    userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "data error"));
        }
        return result;
    }

}
