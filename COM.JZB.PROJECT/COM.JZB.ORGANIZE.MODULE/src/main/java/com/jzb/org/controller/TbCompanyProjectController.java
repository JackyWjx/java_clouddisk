package com.jzb.org.controller;

import com.alibaba.fastjson.JSON;
import com.jzb.base.data.JzbDataType;
import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbPageConvert;
import com.jzb.base.util.JzbTools;
import com.jzb.org.api.base.RegionBaseApi;
import com.jzb.org.api.redis.TbCityRedisApi;
import com.jzb.org.service.TbCompanyProjectService;
import com.jzb.org.util.SetPageSize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "org/CompanyProject")
public class TbCompanyProjectController {

    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(TbCompanyCommonController.class);

    @Autowired
    private TbCompanyProjectService tbCompanyProjectService;

    @Autowired
    private RegionBaseApi RegionBaseApi;

    /**
     * 查询redis缓存地区对象
     */
    @Autowired
    private TbCityRedisApi tbCityRedisApi;



    /**
     *模糊查询 单位 项目名称
     * @param param
     * @return
     */
    @RequestMapping(value = "/getCompany", method = RequestMethod.POST)
    @CrossOrigin
    public Response getCompany(@RequestBody Map<String, Object> param){
        Response result;
        try {
            Map<String , Object> resultMap = new HashMap<>();
            List<Map<String , Object>> resultList = new ArrayList<>();
            List<Map<String , Object>> paraList = new ArrayList<>();
            List<Map<String , Object>> list  = (List<Map<String, Object>>) param.get("list");
            if(param.containsKey("cname")){
                for(int i = 0  ; i< list.size() ; i++){
                    Map<String , Object> comMap =  new HashMap<>();
                    comMap.put("cid",list.get(i).get("cid"));
                    comMap.put("cname",param.get("cname"));
                    Map<String , Object> com = tbCompanyProjectService.getCompany(comMap);
                    if(!JzbTools.isEmpty(com)){
                        paraList.add(list.get(i));
                    }
                }
            }else if(param.containsKey("projectname")){
                for(int i = 0  ; i< list.size() ; i++){
                    Map<String , Object> proMap =  new HashMap<>();
                    proMap.put("projectid",list.get(i).get("projectid"));
                    proMap.put("projectname",param.get("projectname"));
                    Map<String , Object> com = tbCompanyProjectService.getProject(proMap);
                    if(!JzbTools.isEmpty(com)){
                        paraList.add(list.get(i));
                    }
                }
            }
            // 索引
            int count = 1;
            // 页数
            int pageno  = JzbDataType.getInteger(param.get("pageno")) == 1 ? 0 : JzbDataType.getInteger(param.get("pageno"));
            // 页码
            int pagesize  = JzbDataType.getInteger(param.get("pagesize"));
            // 起始
            int begsize = pageno * pagesize;
            for(int  i = begsize ; i < paraList.size() ; i++){
                resultList.add(paraList.get(i));
                count ++;
                if(count >= pagesize ){
                    break;
                }
            }
            resultMap.put("list",resultList);
            resultMap.put("count",paraList.size());
            result = Response.getResponseSuccess();
            result.setResponseEntity(resultMap);
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }


    /**
     *销售业主-公海-项目-数据查询 LBQ
     * @param param
     * @return
     */
    @RequestMapping(value = "/getComProject", method = RequestMethod.POST)
    @CrossOrigin
    public Response getComProject(@RequestBody Map<String, Object> param) {
        Response result;
        //判断请求参数如果为空则返回404
        try {
            if (JzbCheckParam.haveEmpty(param, new String[]{"pageno","pagesize"})) {
                result = Response.getResponseError();
            } else {
                //设置好分页参数
                param = SetPageSize.setPagenoSize(param);

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
                //查询项目模块下的数据
                List<Map<String, Object>> list = tbCompanyProjectService.getComProject(param);
                //判断前端传过来的分页总数
                int count = tbCompanyProjectService.getCount(param);
                //获取用户项目信息
                for (int i = 0; i < list.size(); i++) {
                    Response cityList = RegionBaseApi.getRegionInfo(list.get(i));
                    list.get(i).put("region",cityList.getResponseEntity());
                }
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                PageInfo pageInfo = new PageInfo();
                pageInfo.setList(list);
                //设置分页总数
                pageInfo.setTotal(count);
                result = Response.getResponseSuccess(userInfo);
                result.setPageInfo(pageInfo);
            }
        } catch (Exception e) {
            //打印错误信息
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 项目的添加
     * @param param
     * @return
     */
    @RequestMapping(value = "/saveComProject", method = RequestMethod.POST)
    @CrossOrigin
    public Response saveComProject(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            //如果参数为为空则返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"projectname"})) {
                result = Response.getResponseError();
            } else {
                int count = tbCompanyProjectService.saveComProject(param);
                if (count > 0) {
                    //存入用户信息
                    Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                    //返回成功的响应消息
                    result = Response.getResponseSuccess(userInfo);
                } else {
                    //否则就输出错误信息
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
     * 关联业主  根据项目id对项目表做修改
     * @param param
     * @return
     */
    @RequestMapping(value = "/updateComProject",method = RequestMethod.POST)
    @CrossOrigin
    public Response updateComProject(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            List<Map<String, Object>> paramList = (List) param.get("list");

            int count = tbCompanyProjectService.updateComProject(paramList);
            if (count > 0) {
                //存入用户信息
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                PageInfo pageInfo = new PageInfo();
                //返回成功的响应消息
                result = Response.getResponseSuccess(userInfo);
            } else {
                result = Response.getResponseError();
            }

        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 取消关联  根据项目id对项目表做修改
     * @param param
     * @return
     */
    @RequestMapping(value = "/ComProject",method = RequestMethod.POST)
    @CrossOrigin
    public Response ComProject(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            List<Map<String, Object>> paramList = (List) param.get("list");

            int count = tbCompanyProjectService.ComProject(paramList);
            if (count > 0) {
                //存入用户信息
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                PageInfo pageInfo = new PageInfo();
                //返回成功的响应消息
                result = Response.getResponseSuccess(userInfo);
            } else {
                result = Response.getResponseError();
            }

        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * CRM-销售业主-我服务的业主-1
     * 根据服务的项目ID获取项目信息,后台调用不支持前台调用
     *
     * @Author: Kuang Bin
     * @DateTime: 2019/10/19
     */
    @RequestMapping(value = "/getServiceProjectList", method = RequestMethod.POST)
    @CrossOrigin
    public Response getServiceProjectList(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            //判断前端传过来的分页总数
            int count = JzbDataType.getInteger(param.get("count"));
            // 获取产品报价总数
            count = count < 0 ? 0 : count;
            if (count == 0) {
                count = tbCompanyProjectService.getServiceProjectListCount(param);
            }
            // 返回所有的企业列表
            if(JzbTools.isEmpty(param.get("projectid"))){
                JzbPageConvert.setPageRows(param);
            }
          
            List<Map<String, Object>> companyList = tbCompanyProjectService.getServiceProjectList(param);
            result = Response.getResponseSuccess();
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(companyList);
            //设置分页总数
            pageInfo.setTotal(count > 0 ? count : companyList.size());
            result.setPageInfo(pageInfo);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * CRM-销售业主-我服务的业主-2
     * 获取所有人的uid
     *
     * @Author: Kuang Bin
     * @DateTime: 2019/10/19
     */
    @RequestMapping(value = "/getServiceProjectUid", method = RequestMethod.POST)
    @CrossOrigin
    public Response getServiceProjectUid(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 返回所有的企业列表
            List<Map<String, Object>> companyList = tbCompanyProjectService.getServiceProjectUid(param);
            result = Response.getResponseSuccess();
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(companyList);
            result.setPageInfo(pageInfo);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 获取今日添加项目
     * @param param
     * @return
     */

    @RequestMapping(value = "/getComProjectCount",method = RequestMethod.POST)
    @CrossOrigin
    public Response getComProjectCount(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/org/CompanyProject/getComProjectCount";
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

            int count =  tbCompanyProjectService.getComProjectCount(param);

            PageInfo pageInfo = new PageInfo();

            pageInfo.setTotal(count);
            // 获取用户信息返回
            result = Response.getResponseSuccess((Map<String, Object>) param.get("userinfo"));
            result.setPageInfo(pageInfo);

        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getComProjectCount Method", ex.toString()));
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
