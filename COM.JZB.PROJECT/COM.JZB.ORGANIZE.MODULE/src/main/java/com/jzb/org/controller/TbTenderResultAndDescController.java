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
 * @Date 2019.12.7
 * @Description 招投标增删改查
 */
@RestController
@RequestMapping(value = "/org/TenderMessage")
public class TbTenderResultAndDescController {

    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(TbTenderResultAndDescController.class);

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
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/addTenderMessage", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response addTenderMessage(@RequestBody Map<String, Object> param) {
        Response response;
        String api = "/org/TenderMessage/addTenderMessage";
        Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
        ArrayList<String> region;
        region = (ArrayList<String>) param.get("region");
        boolean flag = true;
        //判断所传的值是否为空，为空不添加地区信息
        try {
            if (!JzbCheckParam.haveEmpty(param,new String[]{"region"})) {
                //如果只有省
                if (region.size() == 1) {
                    param.put("provice", region.get(0));
                } else if (region.size() == 2) {//如果有省市
                    param.put("provice", region.get(0));
                    param.put("city", region.get(1));
                }
            }
            //是否包含所有参数必填项
            if (JzbCheckParam.haveEmpty(param, new String[]{"type", "projecttype", "title"})) {
                response = Response.getResponseError();
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "没有必填参数"));
            } else {
                param.put("tendid", JzbRandom.getRandomChar(32));
                param.put("adduid", userInfo.get("uid"));
                param.put("status", "1");
                param.put("addtime", System.currentTimeMillis());
                if (param.get("type").equals("2")) {
                    //中标
                    response = tenderResultAndDescService.addTenderMessage(param)>0?Response.getResponseSuccess(userInfo):Response.getResponseError();
                } else if (param.get("type").equals("1")) {
                    //招標
                    response =tenderAndDescService.addTenderMessage(param)>0?Response.getResponseSuccess(userInfo):Response.getResponseError();
                } else {
                    //既不是中标，又不是招标
                    response = Response.getResponseError();
                }
            }
        } catch (Exception e) {
            flag = false;
            JzbTools.logError(e);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getApiLogger(api, "1", "ERROR",
                    userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "增加招标中标异常"));
        }
        if (userInfo != null) {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
                    userInfo.get("msgTag").toString(), "User Login Message"));
        } else {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
        }
        return response;
    }

    /**
     * 获取招标中标数据，当type=1时，获取招标数据，type=2时，中标数据，其余，查询全部
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getTenderMessage", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response getTenderMessage(@RequestBody Map<String, Object> param) {
        Response response;
        // 定义返回结果
        Map<String, Object> userInfo = null;
        String api = "/org/TenderMessage/getTenderMessage";
        boolean flag = true;
        try {
            userInfo = (Map<String, Object>) param.get("userinfo");
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            //判断页码参数是否存在
            if (JzbCheckParam.haveEmpty(param, new String[]{"pagesize", "pageno"})) {
                response = Response.getResponseError();
                logger.error(JzbLoggerUtil.getApiLogger(api, "1", "ERROR",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "data error"));

            } else {
                JzbPageConvert.setPageRows(param);
                List<Map<String, Object>> tenderMessages = tenderResultAndDescService.queryTenderMessage(param);
                response = Response.getResponseSuccess(userInfo);
                //将时间戳转化
                for (Map<String, Object> tenderMessage : tenderMessages) {
                    //将地区id转化名称与对象
                    Map<String, Object> regionMap = new HashMap<>();
                    regionMap.put("key", tenderMessage.get("city") == null ? tenderMessage.get("provice") : tenderMessage.get("city"));
                    Response proviceList = tbCityRedisApi.getCityList(regionMap);
                    Map<String, Object> resultParam = null;
                    if (proviceList.getResponseEntity() != null) {
                        resultParam = (Map<String, Object>) JSON.parse(proviceList.getResponseEntity().toString());
                        if (resultParam != null) {
                            resultParam.put("region", resultParam.get("creaid"));
                        } else {
                            resultParam = new HashMap<>();
                            resultParam.put("region", null);
                        }
                        if (resultParam != null && resultParam.get("province") != null) {
                            tenderMessage.put("provice", resultParam.get("province"));
                        }
                        if (resultParam != null && resultParam.get("city") != null) {
                            tenderMessage.put("city", resultParam.get("city"));
                        }
                    }
                    // 转map
                    if (resultParam != null) {
                        Response response1 = regionBaseApi.getRegionInfo(resultParam);
                        tenderMessage.put("region", response1.getResponseEntity());
                    }
                }
                // 定义pageinfo
                PageInfo pi = new PageInfo();
                pi.setList(tenderMessages);
                // 如果有一个指定参数不为空，则返回list.size()  否则返回总数
                pi.setTotal(tenderResultAndDescService.queryTenderMessageCount(param));
                response.setPageInfo(pi);
            }
        } catch (Exception e) {
            flag=false;
            JzbTools.logError(e);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getApiLogger(api, "2", "ERROR",
                    userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "查询招投标异常"));
        }
        if (userInfo != null) {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
                    userInfo.get("msgTag").toString(), "User Login Message"));
        } else {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
        }
        return response;
    }

    /**
     * 修改招投标信息，type=1修改招标信息，type=2时修改中标
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/putTenderMessage", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response putTenderMessage(@RequestBody Map<String, Object> param) {
        Response response;
        boolean flag = true;
        Map<String, Object> userInfo =null;
        String api = "/org/TenderMessage/putTenderMessage";
        try {
            userInfo = (Map<String, Object>) param.get("userinfo");
            if (param.get("userinfo") != null) {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.error(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            if (JzbCheckParam.haveEmpty(param, new String[]{"type", "tendid"})) {
                response = Response.getResponseError();
                logger.error(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "必要参数为空"));
            } else {
                param.put("upduid", userInfo.get("uid"));
                param.put("updtime", System.currentTimeMillis());
                if (param.get("type").equals("2")) {
                    //中标
                    tenderResultAndDescService.putTenderMessage(param);
                    // 定义返回结果
                    response = Response.getResponseSuccess(userInfo);
                } else if (param.get("type").equals("1")) {
                    //招標
                    tenderAndDescService.putTenderMessage(param);
                    response = Response.getResponseSuccess(userInfo);
                } else {
                    response = Response.getResponseError();
                    logger.error(JzbLoggerUtil.getApiLogger(api, "2", "ERROR",
                            userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "传入type参数不标准"));
                }
            }
        } catch (Exception e) {
            flag = false;
            JzbTools.logError(e);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getApiLogger(api, "2", "ERROR",
                    userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "修改招投标异常"));
        }
        if (userInfo != null) {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
                    userInfo.get("msgTag").toString(), "User Login Message"));
        } else {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
        }
        return response;
    }

    /**
     * 删除中标投标信息，前端传输集合，根据集合中的type值，分为招标中标信息，在进行删除
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/delTenderMessage", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response delTenderMessage(@RequestBody Map<String, Object> param) {
        Response response;
        boolean flag = true;
        Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
        String api = "/org/TenderMessage/delTenderMessage";
        List<Map<String, Object>> list = (List<Map<String, Object>>) param.get("list");
        //前端传输list集合过来，将其对招投标信息进行分类
        List<String> tenderMessage = new ArrayList<>();
        List<String> tenderResultMessage = new ArrayList<>();
        try {
            userInfo = (Map<String, Object>) param.get("userinfo");
            if (param.get("userinfo") != null) {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.error(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            for (Map map : list) {
                //招标
                if (map.get("type").equals("1")) {
                    tenderMessage.add((String) map.get("tendid"));
                } else if (map.get("type").equals("2")) {
                    //中标
                    tenderResultMessage.add((String) map.get("tendid"));
                } else {
                    //异常数据
                    response = Response.getResponseError();
                    logger.error(JzbLoggerUtil.getApiLogger(api, "1", "ERROR",
                            userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "data error"));
                }
            }
            //当招标内有数值时，执行招标删除
            if (tenderMessage.size() > 0) {
                param.put("tendids", tenderMessage);
                tenderAndDescService.delTenderMessage(param);
            }
            //当中标内有数值时，执行中标删除
            if (tenderResultMessage.size() > 0) {
                param.put("tendids", tenderResultMessage);
                tenderResultAndDescService.delTenderMessage(param);
            }
            response = Response.getResponseSuccess(userInfo);
        } catch (Exception e) {
            flag = false;
            JzbTools.logError(e);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getApiLogger(api, "2", "ERROR",
                    userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "招投标删除异常"));
        }
        if (userInfo != null) {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
                    userInfo.get("msgTag").toString(), "User Login Message"));
        } else {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
        }
        return response;
    }

    /**
     * 修改前发送请求查询
     * @param param
     * @return
     */
    @RequestMapping(value = "/getTenderMessageBeforePut", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response getTenderMessageBeforePut(@RequestBody Map<String, Object> param) {
        Response response;
        boolean flag = true;
        Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
        String api = "/org/TenderMessage/getTenderMessageBeforePut";
        try {
            userInfo = (Map<String, Object>) param.get("userinfo");
            if (param.get("userinfo") != null) {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.error(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            Map<String,Object> map = null;
            if(JzbCheckParam.allNotEmpty(param,new String[]{"type","tendid"})){
                map = tenderResultAndDescService.getTenderMessageBeforeUpdate(param);
            }
            response = Response.getResponseSuccess(userInfo);
            response.setResponseEntity(map);
        } catch (Exception e) {
            flag = false;
            JzbTools.logError(e);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getApiLogger(api, "2", "ERROR",
                    userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "招投标删除异常"));
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
