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
import com.jzb.org.api.auth.AuthInfoApi;
import com.jzb.org.api.base.RegionBaseApi;
import com.jzb.org.api.redis.TbCityRedisApi;
import com.jzb.org.dao.TbCompanyListMapper;
import com.jzb.org.service.CompanyService;
import com.jzb.org.service.TbCompanyCommonService;
import com.jzb.org.util.SetPageSize;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.jws.Oneway;
import java.util.ArrayList;
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
    private RegionBaseApi RegionBaseApi;

    @Autowired
    private TbCompanyCommonService tbCompanyCommonService;

    @Autowired
    private AuthInfoApi authInfoApi;

    @Autowired
    private TbCityRedisApi tbCityRedisApi;

    @Autowired
    private DeptUserControllerApi deptUserControllerApi;

    @Autowired
    private RegionBaseApi regionBaseApi;

    @Autowired
    private CompanyController companyController;

    @Autowired
    private CompanyService companyService;

    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(TbCompanyCommonController.class);

    /**
     * 获取单位名称
     *
     * @param param
     * @return
     * @author chenzhengduan
     */
    @RequestMapping(value = "/getCompanyNameById", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response getCompanyNameById(@RequestBody Map<String, Object> param) {
        Response response;
        try {

            if (JzbCheckParam.haveEmpty(param, new String[]{"cid"})) {
                response = Response.getResponseError();
            } else {
                String cname = tbCompanyCommonService.queryCompanyNameByID(param);
                response = Response.getResponseSuccess();
                response.setResponseEntity(cname);
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            response = Response.getResponseError();
        }
        return response;
    }


    /**
     * 获取已分配的业主单位（不带条件查询）
     *
     * @param param
     * @return
     * @author chenzhengduan
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
                List<Map<String, Object>> regionList = new ArrayList<>();
                if (!JzbDataType.isEmpty(JzbDataType.getString(param.get("province")))) {
                    // 传入3代表查询县级地区
                    if (!JzbDataType.isEmpty(JzbDataType.getString(param.get("county")))) {
                        // 定义存放每个省市县地区的map
                        Map<String, Object> regionMap = new HashMap<>();
                        // 加入县级地区id到参数对象中
                        regionMap.put("region", JzbDataType.getString(param.get("county")));
                        regionList.add(regionMap);
                        // 等于2代表传入的是市级地区ID
                    } else if (!JzbDataType.isEmpty(JzbDataType.getString(param.get("city")))) {
                        // 添加查询地区的key
                        param.put("key", "jzb.system.city");

                        // 获取所有的地区信息
                        Response response = tbCityRedisApi.getCityJson(param);

                        // 将字符串转化为map
                        Map<String, Object> myJsonArray = (Map<String, Object>) JSON.parse(response.getResponseEntity().toString());
                        // 判断返回值中是否存在省信息
                        if (!JzbDataType.isEmpty(myJsonArray.get(JzbDataType.getString(param.get("province"))))) {
                            // 获取对应省下所有的城市信息
                            List<Map<String, Object>> myJsonList = (List<Map<String, Object>>) myJsonArray.get(JzbDataType.getString(param.get("province")));
                            for (int i = 0; i < myJsonList.size(); i++) {
                                // 获取省份下所有城市的信息
                                Map<String, Object> provinceMap = myJsonList.get(i);

                                // 如果为传入的城市ID则进行下一步
                                if (!JzbDataType.isEmpty(provinceMap.get(JzbDataType.getString(param.get("city"))))) {
                                    // 获取城市下所有的县级信息
                                    List<Map<String, Object>> countyMap = (List<Map<String, Object>>) provinceMap.get(JzbDataType.getString(param.get("city")));
                                    Map<String, Object> county =  countyMap.get(0);
                                    List<Map<String, Object>> cityList = (List<Map<String, Object>>) county.get("list");
                                    for (int b = 0; b < cityList.size(); b++) {
                                        // 获取城市下单个的县级信息
                                        Map<String, Object> cityMap = cityList.get(b);

                                        // 定义存放每个省市县地区的map
                                        Map<String, Object> regionMap = new HashMap<>();

                                        // 将县级ID加入地区map对象中
                                        regionMap.put("region", JzbDataType.getString(cityMap.get("creaid")));
                                        regionList.add(regionMap);
                                    }
                                }
                            }
                        }
                    } else if (!JzbDataType.isEmpty(JzbDataType.getString(param.get("province")))) {
                        // 添加查询地区的key
                        param.put("key", "jzb.system.city");

                        // 查询本身
                        Map<String, Object> regionProvince = new HashMap<>();
                        regionProvince.put("region", param.get("province"));
                        regionList.add(regionProvince);

                        // 获取所有的地区信息
                        Response response = tbCityRedisApi.getCityJson(param);

                        // 将字符串转化为map
                        Map<String, Object> myJsonArray = (Map<String, Object>) JSON.parse(response.getResponseEntity().toString());
                        if (!JzbDataType.isEmpty(myJsonArray.get(JzbDataType.getString(param.get("province"))))) {
                            List<Map<String, Object>> myJsonList = (List<Map<String, Object>>) myJsonArray.get(JzbDataType.getString(param.get("province")));
                            for (int i = 0; i < myJsonList.size(); i++) {
                                // 获取城市信息
                                Map<String, Object> provinceMap = myJsonList.get(i);
                                for (Map.Entry<String, Object> entry : provinceMap.entrySet()) {
                                    if (!"list".equals(entry.getKey())) {
                                        String key = entry.getKey();
                                        // 定义存放每个省市县地区的map
                                        Map<String, Object> regionMap = new HashMap<>();
                                        regionMap.put("region", key);
                                        regionList.add(regionMap);
                                        List<Map<String, Object>> cityList = (List<Map<String, Object>>) entry.getValue();
                                        Map<String, Object> cityMap = cityList.get(0);
                                        List<Map<String, Object>> city = (List<Map<String, Object>>) cityMap.get("list");
                                        for (int k = 0; k < city.size(); k++) {
                                            // 获取城市下单个的县级信息
                                            Map<String, Object> cityMap1 = city.get(k);

                                            // 定义存放每个省市县地区的map
                                            Map<String, Object> region = new HashMap<>();

                                            // 将县级ID加入地区map对象中
                                            region.put("region", JzbDataType.getString(cityMap1.get("creaid")));
                                            regionList.add(region);
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // 将所有结果加入参数中传入
                    param.put("list", regionList);
                }
                // 获取list
                List<Map<String, Object>> list = tbCompanyCommonService.queryCompanyCommon(param);

                //获取地区信息
                for (int i = 0; i < list.size(); i++) {
                    Response cityList = RegionBaseApi.getRegionInfo(list.get(i));
                    list.get(i).put("region",cityList.getResponseEntity());
                }
                // 分页对象
                PageInfo pageInfo = new PageInfo();

                pageInfo.setList(list);
                // 如果前端传的count 大于0 则返回list大小
                pageInfo.setTotal(JzbDataType.getInteger(param.get("count")) > 0 ? tbCompanyCommonService.queryCompanyCommonCount(param) : 0);
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
     * 获取历史私海单位
     *
     * @param param
     * @return
     * @author chenzhengduan
     */
    @RequestMapping(value = "/getCompanyCommonListHistory", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response getCompanyCommonListHistory(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/org/companyCommon/getCompanyCommonListHistory";
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
                List<Map<String, Object>> list = tbCompanyCommonService.getCompanyCommonListHistory(param);

                // 创建map用来获取地区信息
                Map<String, Object> map = new HashMap<>();

                for (int i = 0, l = list.size(); i < l; i++) {

                    map.put("key", list.get(i).get("region"));
                    Response cityList = tbCityRedisApi.getCityList(map);
                    // 获取地区map
                    Map<String, Object> resultParam = null;
                    if (cityList.getResponseEntity() != null) {
                        resultParam = (Map<String, Object>) JSON.parse(cityList.getResponseEntity().toString());
                        if(resultParam!=null){
                            resultParam.put("region", resultParam.get("creaid"));
                        }else {
                            resultParam=new HashMap<>();
                            resultParam.put("region",null);
                        }
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
                pageInfo.setTotal(JzbDataType.getInteger(param.get("count")) > 0 ? tbCompanyCommonService.getCompanyCommonListHistoryCount(param) : 0);
                // 获取用户信息返回
                result = Response.getResponseSuccess((Map<String, Object>) param.get("userinfo"));
                result.setPageInfo(pageInfo);
            }

        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getCompanyCommonListHistory Method", ex.toString()));
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
     *
     * @param param
     * @return
     * @author chenzhengduan
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
                for (int i = 0; i < list.size(); i++) {
                    Response cityList = RegionBaseApi.getRegionInfo(list.get(i));
                    list.get(i).put("region",cityList.getResponseEntity());
                }
                // 分页对象
                PageInfo pageInfo = new PageInfo();

                pageInfo.setList(list);
                // 如果前端传的count 大于0 则返回list大小
                pageInfo.setTotal(JzbDataType.getInteger(param.get("count")) > 0 ? tbCompanyCommonService.queryCompanyCommonByKeyWordCount(param) : 0);
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
     *
     * @return
     * @author chenzhengduan
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
    public Response saveCompanyCommon(@RequestBody Map<String, Object> param, @RequestHeader(value = "token") String token) {
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

    /**私海-添加销售员
     * 所有业主-业主列表-添加销售员
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
                param.put("addtime",System.currentTimeMillis());
                //根据id进行修改，添加业务员
                int count = tbCompanyCommonService.updateCompanys(param);
                //如果返回值大于零则响应成功信息
                if (count > 0) {
                    Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                    result = Response.getResponseSuccess(userInfo);
                } else if (count == -1){
                    result = Response.getResponseError();
                    result.setResponseEntity("销售员已存在");
                }else  {
                    result = Response.getResponseError();
                    result.setResponseEntity("销售员名额已满");
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
     * 私海-更换销售员
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/relpaceCompanysSales", method = RequestMethod.POST)
    @CrossOrigin
    public Response relpaceCompanysSales(@RequestBody Map<String, Object> param) {

        Response result;
        try {
            //如果参数为空则返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"cid"})) {
                result = Response.getResponseError();
            } else {
                param.put("addtime",System.currentTimeMillis());
                //根据id进行修改，添加业务员
                int count = tbCompanyCommonService.relpaceCompanysSales(param);
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

    /**主管使用
     * 私海-删除销售员 todo
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/delCompanysSales", method = RequestMethod.POST)
    @CrossOrigin
    public Response delCompanysSales(@RequestBody Map<String, Object> param) {

        Response result;
        try {
            //如果参数为空则返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"cid"})) {
                result = Response.getResponseError();
            } else {
                param.put("addtime",System.currentTimeMillis());
                //根据id进行修改，删除业务员
                int count = tbCompanyCommonService.delCompanysSales(param);
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
     * 调出私海，退回公海
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/rebackCompanys", method = RequestMethod.POST)
    @CrossOrigin
    public Response rebackCompanys(@RequestBody Map<String, Object> param) {

        Response result;
        try {
            Map<String,Object> userinfo = (Map<String, Object>) param.get("userinfo");
            param.put("addtime",System.currentTimeMillis());
            param.put("uid",userinfo.get("uid"));
                int count = tbCompanyCommonService.rebackCompanys(param);
                //如果返回值大于零则响应成功信息
                if (count > 0) {
                    Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                    result = Response.getResponseSuccess(userInfo);
                } else {
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
     * 退回公海  加入历史私海记录
     *
     * @param param  todo
     * @return
     */
    @RequestMapping(value = "/rebackCompanysToHistory", method = RequestMethod.POST)
    @CrossOrigin
    public Response rebackCompanysToHistory(@RequestBody Map<String, Object> param) {

        Response result;
        try {
            Map<String,Object> userinfo = (Map<String, Object>) param.get("userinfo");
            param.put("addtime",System.currentTimeMillis());
            param.put("uid",userinfo.get("uid"));
            int count = tbCompanyCommonService.rebackCompanysToHistory(param);
            //如果返回值大于零则响应成功信息
            if (count > 0) {
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                result = Response.getResponseSuccess(userInfo);
            } else {
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
     * 获取私海历史单位
     *
     * @param param
     * @return
     * @author chenhui todo
     */
    @RequestMapping(value = "/queryCompanysToHistory", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response queryCompanysToHistory(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/org/companyCommon/queryCompanysToHistory";
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
                List<Map<String, Object>> list = tbCompanyCommonService.queryCompanysToHistory(param);

                // 遍历获取地区调用redis返回
                for (int i = 0; i < list.size(); i++) {
                    Response cityList = RegionBaseApi.getRegionInfo(list.get(i));
                    list.get(i).put("region",cityList.getResponseEntity());
                }
                // 分页对象
                PageInfo pageInfo = new PageInfo();

                pageInfo.setList(list);
                // 如果前端传的count 大于0 则返回list大小
                pageInfo.setTotal(JzbDataType.getInteger(param.get("count")) > 0 ? tbCompanyCommonService.queryCompanysToHistoryCount(param) : 0);
                // 获取用户信息返回
                result = Response.getResponseSuccess((Map<String, Object>) param.get("userinfo"));
                result.setPageInfo(pageInfo);
            }

        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "queryCompanysToHistory Method", ex.toString()));
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
     * 所有业主-业主列表查询
     *
     * @param param
     * @return
     * @author chenzhengduan
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
                if (param.get("status").equals("")) {
                    param.put("status", '2');
                    param.put("statuss", '1');
                } else if (param.get("status").equals("1")) {
                    param.put("status", '3');
                    param.put("statuss", '1');
                } else {
                    param.put("status", '2');
                    param.put("statuss", '3');
                }
                    // 设置参数
                    JzbPageConvert.setPageRows(param);
                param.put("uid", userInfo.get("uid"));
                // 获取list
                List<Map<String, Object>> list = tbCompanyCommonService.getCompanyCommoms(param);

                // 遍历获取地区调用redis返回
                for (int i = 0; i < list.size(); i++) {
                    Map<String, Object> companyMap = list.get(i);
                    Response region = regionBaseApi.getRegionInfo(companyMap);
                    companyMap.put("region", region.getResponseEntity());
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

    /**
     * CRM-所有业主-业主列表-修改
     * 点击修改判断是否是系统中的用户如果不是就新建用户
     *
     * @Author: Kuang Bin
     * @DateTime: 2019/10/11
     */
    @RequestMapping(value = "/modifyCompanyCommon", method = RequestMethod.POST)
    @CrossOrigin
    public Response modifyCompanyCommon(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            //修改数据
            int count = tbCompanyCommonService.modifyCompanyCommon(param);
            //获取用户信息
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            if (count >= 1) {
                result = Response.getResponseSuccess(userInfo);
                Map<String, Object> map= companyService.getEnterpriseData(param);
                companyController.comHasCompanyKey(map);
            } else {
                result = Response.getResponseError();
            }
        } catch (Exception e) {
            //打印错误信息
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }
}
