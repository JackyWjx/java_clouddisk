package com.jzb.org.controller;

import com.alibaba.fastjson.JSON;
import com.jzb.base.data.JzbDataType;
import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbPageConvert;
import com.jzb.base.util.JzbTimeConvert;
import com.jzb.base.util.JzbTools;
import com.jzb.org.api.api.DeptUserControllerApi;
import com.jzb.org.api.base.RegionBaseApi;
import com.jzb.org.api.redis.TbCityRedisApi;
import com.jzb.org.dao.TbCompanyListMapper;
import com.jzb.org.service.TbCompanyCommonService;
import com.jzb.org.util.SetPageSize;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.jws.Oneway;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenzhengduan
 * 已分配的业主
 */
@RestController
@RequestMapping(value = "/org/companyCommon")
public class TbCompanyCommonController {

    @Autowired
    private TbCompanyCommonService tbCompanyCommonService;

    @Autowired
    private TbCityRedisApi tbCityRedisApi;

    @Autowired
    private DeptUserControllerApi deptUserControllerApi;

    @Autowired
    private RegionBaseApi regionBaseApi;

    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(TbCompanyCommonController.class);

    /**
     * 获取单位名称
     * @author chenzhengduan
     * @param param
     * @return
     */
    @RequestMapping(value = "/getCompanyNameById", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response getCompanyNameById(@RequestBody Map<String, Object> param){
        Response response;
        try {

            if(JzbCheckParam.haveEmpty(param,new String[]{"cid"})){
                response=Response.getResponseError();
            }else {
                String cname = tbCompanyCommonService.queryCompanyNameByID(param);
                response=Response.getResponseSuccess();
                response.setResponseEntity(cname);
            }
        }catch (Exception ex){
            JzbTools.logError(ex);
            response=Response.getResponseError();
        }
        return response;
    }


    /**
     * 获取已分配的业主单位（不带条件查询）
     * @author chenzhengduan
     * @param param
     * @return
     */
    @RequestMapping(value = "/getCompanyCommonList", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response getCompanyCommonList(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/org/companyCommon/getCompanyCommonList";
        boolean flag = true;
        try {
            // 如果获取参数userinfo不为空的话
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            // 如果指定参数为空的话返回error
            if (JzbCheckParam.haveEmpty(param, new String[]{"pagesize", "pageno", "count"})) {
                result = Response.getResponseError();
            } else {

                // 设置参数
                JzbPageConvert.setPageRows(param);

                // 获取list
                List<Map<String, Object>> list = tbCompanyCommonService.queryCompanyCommon(param);

                // 创建map用来获取地区信息
                Map<String, Object> map = new HashMap<>();

                for (int i = 0, l = list.size(); i < l; i++) {
                    map.put("key", list.get(i).get("region"));
                    Response cityList = tbCityRedisApi.getCityList(map);
                    // 获取地区map
                    Map<String, Object> resultParam = null;
                    if (cityList.getResponseEntity() != null) {
                        resultParam = (Map<String, Object>) JSON.parse(cityList.getResponseEntity().toString());
                        resultParam.put("region",resultParam.get("creaid"));
                    }
                    // 转map
                    if (resultParam != null) {
                        Response response = regionBaseApi.getRegionInfo(resultParam);
                        list.get(i).put("region", response.getResponseEntity());
                    }
                }
                // 分页对象
                PageInfo pageInfo = new PageInfo();

                pageInfo.setList(list);
                // 如果前端传的count 大于0 则返回list大小
                pageInfo.setTotal(JzbDataType.getInteger(param.get("count")) > 0 ? list.size() : 0);
                // 获取用户信息返回
                result = Response.getResponseSuccess((Map<String, Object>) param.get("userinfo"));
                result.setPageInfo(pageInfo);
            }

        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getCompanyCommonList Method", ex.toString()));
        }
        if (userInfo != null) {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
                    userInfo.get("msgTag").toString(), "User Login Message"));
        } else {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
        }
        return result;
    }

    /**
     * 获取已分配的业主单位 (带条件查询)
     * @author chenzhengduan
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getCompanyCommonListByKeyword", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response getCompanyCommonListByKeyword(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/org/companyCommon/getCompanyCommonListByKeyword";
        boolean flag = true;
        try {
            // 如果获取参数userinfo不为空的话
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            // 如果指定参数为空的话返回error
            if (JzbCheckParam.haveEmpty(param, new String[]{"pagesize", "pageno", "count"})) {
                result = Response.getResponseError();
            } else {

                // 设置参数
                JzbPageConvert.setPageRows(param);

                // 获取list
                List<Map<String, Object>> list = tbCompanyCommonService.queryCompanyCommonByKeyWord(param);

                // 遍历获取地区调用redis返回
                for (int i = 0, l = list.size(); i < l; i++) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("key", list.get(i).get("region"));
                    Response cityList = tbCityRedisApi.getCityList(map);
                    // 获取地区map
                    Map<String, Object> resultParam = null;
                    if (cityList.getResponseEntity() != null) {
                        resultParam = (Map<String, Object>) JSON.parse(cityList.getResponseEntity().toString());
                        resultParam.put("region",resultParam.get("creaid"));
                    }
                    // 转map
                    if (resultParam != null) {
                        Response response = regionBaseApi.getRegionInfo(resultParam);
                        list.get(i).put("region", response.getResponseEntity());
                    }
                }
                // 分页对象
                PageInfo pageInfo = new PageInfo();

                pageInfo.setList(list);
                // 如果前端传的count 大于0 则返回list大小
                pageInfo.setTotal(JzbDataType.getInteger(param.get("count")) > 0 ? list.size() : 0);
                // 获取用户信息返回
                result = Response.getResponseSuccess((Map<String, Object>) param.get("userinfo"));
                result.setPageInfo(pageInfo);
            }

        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getCompanyCommonListByKeyword Method", ex.toString()));
        }
        if (userInfo != null) {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
                    userInfo.get("msgTag").toString(), "User Login Message"));
        } else {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
        }
        return result;
    }

    /**
     * 修改业主单位
     *@author chenzhengduan
     * @return
     */
    @RequestMapping(value = "/updateCompanyByCid", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response updateCompanyByCid(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/org/companyCommon/updateCompanyByCid";
        boolean flag = true;
        try {
            // 如果获取参数userinfo不为空的话
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            // 如果指定参数为空则返回error
            if (JzbCheckParam.haveEmpty(param, new String[]{"cid", "cname", "region", "phone", "username"})) {
                result = Response.getResponseError();
            } else {
                // 如果返回结果大于0 就返回success
                result = tbCompanyCommonService.updateCompany(param) > 0 ? Response.getResponseSuccess((Map<String, Object>) param.get("userinfo")) : Response.getResponseError();
            }
        } catch (Exception ex) {
            flag = false;
            // 打印错误信息
            JzbTools.logError(ex);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "updateCompanyByCid Method", ex.toString()));
        }
        if (userInfo != null) {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
                    userInfo.get("msgTag").toString(), "User Login Message"));
        } else {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
        }
        return result;
    }
    /**
     * 所有业主-业主列表-新建
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/saveCompanyCommon", method = RequestMethod.POST)
    @CrossOrigin
    public Response saveCompanyCommon(@RequestBody Map<String, Object> param ,@RequestHeader(value = "token") String token) {
        Response result;
        try {
            Response response = deptUserControllerApi.addCompanyCommon(param, token);

            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");

            result = Response.getResponseSuccess(userInfo);

            result.setResponseEntity(response);

        } catch (Exception e) {

            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 所有业主-业主列表-修改
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/updateCompanyCommon", method = RequestMethod.POST)
    @CrossOrigin
    public Response updateCompanyCommon(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            if (JzbCheckParam.haveEmpty(param, new String[]{"cid"})) {
                result = Response.getResponseError();
            } else {
                //修改数据
                int count = tbCompanyCommonService.updateCompanyCommon(param);
                //获取用户信息
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                result = count == 1 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
            }
        } catch (Exception e) {
            //打印错误信息
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 所有业主-业主列表-删除
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/deleteCompanyCommon", method = RequestMethod.POST)
    @CrossOrigin
    public Response deleteCompanyCommon(@RequestBody Map<String, Object> param) {
        Response result;
        try {

            List<Map<String, Object>> paramList = (List) param.get("list");
            int count = tbCompanyCommonService.deleteCompanyCommon(paramList);
            //如果返回值大于0，则返回成功信息
            if (count > 0) {
                //获取用户信息
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                result = Response.getResponseSuccess(userInfo);
            } else {
                //否则返回失败信息
                result = Response.getResponseError();
            }

        } catch (Exception e) {
            //打印错误信息
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 所有业主-业主列表-分配业务员
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/updateCompanys", method = RequestMethod.POST)
    @CrossOrigin
    public Response updateCompanys(@RequestBody Map<String, Object> param) {

        Response result;
        try {
            //如果参数为空则返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"cid"})) {
                result = Response.getResponseError();
            } else {
                //根据id进行修改，添加业务员
                int count = tbCompanyCommonService.updateCompanys(param);
                //如果返回值大于零则响应成功信息
                if (count > 0) {
                    Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                    result = Response.getResponseSuccess(userInfo);
                } else {
                    result = Response.getResponseError();
                }
            }
        } catch (Exception e) {
            //打印错误信息
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }


    /**
     * 所有业主-业主列表查询
     * @author chenzhengduan
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getCompanyCommon", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response getCompanyCommoms(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/org/companyCommon/getCompanyCommon";
        boolean flag = true;
        try {
            // 如果获取参数userinfo不为空的话
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            // 如果指定参数为空的话返回error
            if (JzbCheckParam.haveEmpty(param, new String[]{"pagesize", "pageno"})) {
                result = Response.getResponseError();
            } else {

                // 设置参数
                JzbPageConvert.setPageRows(param);

                // 获取list
                List<Map<String, Object>> list = tbCompanyCommonService.getCompanyCommoms(param);

                // 遍历获取地区调用redis返回
                for (int i = 0, l = list.size(); i < l; i++) {
                    Map<String, Object> map = new HashMap<>();
                    //根据地区id从缓存中获取地区的信息
                    Response regionInfo = regionBaseApi.getRegionInfo(list.get(i));
                    // 转map
                    if (regionInfo != null) {
                        list.get(i).put("region", regionInfo.getResponseEntity());
                    }
                }

               int count = tbCompanyCommonService.getCount(param);
                // 分页对象
                PageInfo pageInfo = new PageInfo();

                pageInfo.setList(list);
                // 如果前端传的count 大于0 则返回list大小
                pageInfo.setTotal(count);
                // 获取用户信息返回
                result = Response.getResponseSuccess((Map<String, Object>) param.get("userinfo"));
                result.setPageInfo(pageInfo);
            }

        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getCompanyCommon Method", ex.toString()));
        }
        if (userInfo != null) {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
                    userInfo.get("msgTag").toString(), "User Login Message"));
        } else {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
        }
        return result;
    }

}
