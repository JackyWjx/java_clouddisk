package com.jzb.org.service;

import com.alibaba.fastjson.JSON;
import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.org.api.base.RegionBaseApi;
import com.jzb.org.api.redis.TbCityRedisApi;
import com.jzb.org.api.redis.UserRedisServiceApi;
import com.jzb.org.config.OrgConfigProperties;
import com.jzb.org.controller.TbCompanyCommonController;
import com.jzb.org.dao.DeptMapper;
import com.jzb.org.dao.TbCompanyCommonMapper;
import com.jzb.org.dao.TbCompanyListMapper;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.jta.JtaAfterCompletionSynchronization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenzhengduan
 */
@Service
public class TbCompanyCommonService {

    @Autowired
    private TbCompanyCommonMapper tbCompanyCommonMapper;

    @Autowired
    private TbCompanyCommonController companyCommonController;


    @Autowired
    private TbCityRedisApi tbCityRedisApi;

    @Autowired
    private UserRedisServiceApi userRedisServiceApi;

    @Autowired
    private RegionBaseApi regionBaseApi;

    @Autowired
    private DeptMapper deptMapper;

    @Autowired
    private OrgConfigProperties config;

    @Autowired
    private CompanyService companyService;

    /**
     * 查询不带条件的业主单位全部（不带条件）
     *
     * @param param
     * @return
     */
    public List<Map<String, Object>> queryCompanyCommon(Map<String, Object> param) {
        return tbCompanyCommonMapper.queryCompanyCommon(param);
    }

    /**
     * 查询不带条件的业主单位全部（zongshu ）
     * @param param
     * @return
     */
    public int queryCompanyCommonCount(Map<String, Object> param) {
        return tbCompanyCommonMapper.queryCompanyCommonCount(param);
    }

    /**
     * 查询带条件的业主单位全部（带条件）
     *
     * @param param
     * @return
     */
    public List<Map<String, Object>> queryCompanyCommonByKeyWord(Map<String, Object> param) {
        // 定义地区list列表
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
        return tbCompanyCommonMapper.queryCompanyCommonByKeyWord(param);
    }

    /**
     * 查询带条件的业主单位全部（带条件）
     * @param param
     * @return
     */
    public int queryCompanyCommonByKeyWordCount(Map<String, Object> param){
        return tbCompanyCommonMapper.queryCompanyCommonByKeyWordCount(param);
    }

    /**
     * 查询单位名称
     *
     * @param param
     * @return
     */
    public String queryCompanyNameByID(Map<String, Object> param) {
        return tbCompanyCommonMapper.queryCompanyNameByID(param);
    }

    /**
     * 修改单位信息
     *
     * @param param
     * @return
     */
    public int updateCompany(Map<String, Object> param) {
        return tbCompanyCommonMapper.updateCompany(param);
    }


    /**
     * 查询业主列表下的查询出来的总数
     *
     * @param param
     * @return
     */
    public int getCount(Map<String, Object> param) {
        return tbCompanyCommonMapper.getCount(param);
    }

    /**
     * 所有业主-业主列表-修改
     *
     * @param param
     * @return
     */
    public int updateCompanyCommon(Map<String, Object> param) {
        long time = System.currentTimeMillis();
        param.put("updtime", time);
        return tbCompanyCommonMapper.updateCompanyCommon(param);
    }

    /**
     * 所有业主-业主列表-删除
     *
     * @param paramList
     * @return
     */
    public int deleteCompanyCommon(List<Map<String, Object>> paramList) {
        for (int i = 0; i < paramList.size(); i++) {
            paramList.get(i).put("status", 4);
        }
        return tbCompanyCommonMapper.deleteCompanyCommon(paramList);
    }

    /**
     * 所有业主-业主列表-分配业务员
     *
     * @param param
     * @return
     */
    public int updateCompanys(Map<String, Object> param) {
        int count = 0;
        int num = 0;
        // 查询该单位是否已经分配销售员
        Map<String, Object> sMap = tbCompanyCommonMapper.queryCompanySales(param);

        // 第一次分配销售员
        if (JzbTools.isEmpty(sMap)) {
            param.put("oneheader", param.get("uid"));
            count = tbCompanyCommonMapper.updateCompanys(param);
        } else {
            if (JzbTools.isEmpty(sMap.get("oneheader")) && !param.get("uid").equals(sMap.get("oneheader"))) {
                sMap.put("oneheader", param.get("uid"));
                num ++;
            } else if (JzbTools.isEmpty(sMap.get("twoheader")) && num == 0 && !param.get("uid").equals(sMap.get("twoheader")) ) {
                sMap.put("twoheader", param.get("uid"));
                num ++;
            } else if (JzbTools.isEmpty(sMap.get("threeheader")) && num == 0 && !param.get("uid").equals(sMap.get("threeheader"))) {
                sMap.put("threeheader", param.get("uid"));
                num ++;
            } else if (JzbTools.isEmpty(sMap.get("fourheader")) && num == 0 && !param.get("uid").equals(sMap.get("fourheader"))) {
                sMap.put("fourheader", param.get("uid"));
                num ++;
            } else if (JzbTools.isEmpty(sMap.get("fiveheader")) && num == 0 && !param.get("uid").equals(sMap.get("fiveheader"))) {
                sMap.put("fiveheader", param.get("uid"));
                num ++;
            } else if (JzbTools.isEmpty(sMap.get("sixheader")) && num == 0 && !param.get("uid").equals(sMap.get("sixheader"))) {
                sMap.put("sixheader", param.get("uid"));
                num ++;
            }
            count = tbCompanyCommonMapper.updateCompanysAddSales(sMap);
            num = 0;
    }
        return count ;
    }

    /**
     * @Author Reed
     * @Description 退回公海
     * @Date 18:54 2020/1/2
     * @Param [param]
     * @return int
    **/
    public int rebackCompanys(Map<String, Object> param) {
        Map<String,Object> sMap = tbCompanyCommonMapper.queryCompanySales(param);
        //判断是否为主销售
        if (param.get("uid").equals(sMap.get("oneheader"))){
            return tbCompanyCommonMapper.rebackCompanys(param);
        }else {

            int count = 0;
            // 删除自己对该单位的跟进
            param.put("saler",param.get("uid"));
            Response response = companyCommonController.delCompanysSales(param);
            int resultCode = response.getServerResult().getResultCode();
            if (resultCode == 200){
                count = 1;
            }
            // 查询该单位是否已经分配销售员
            if (!JzbTools.isEmpty(sMap.get("oneheader")) || !JzbTools.isEmpty(sMap.get("twoheader"))  || !JzbTools.isEmpty(sMap.get("threeheader")) ||
                    !JzbTools.isEmpty(sMap.get("fourheader"))  || !JzbTools.isEmpty(sMap.get("fiveheader"))  || !JzbTools.isEmpty(sMap.get("sixheader")) ){
                return count;
            }else {
                return tbCompanyCommonMapper.rebackCompanys(param);
            }
        }

    }

    /**
     * 所有业主-业主列表查询
     *
     * @param param
     * @return
     */
    public List<Map<String, Object>> getCompanyCommoms(Map<String, Object> param) {

        // 定义地区list列表
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
                            Map<String, Object> county = countyMap.get(0);
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

        List<Map<String, Object>> list = tbCompanyCommonMapper.getCompanyCommoms(param);
        Map<String ,Object> map = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            StringBuffer sb = new StringBuffer();
            sb.append(list.get(i).get("sales"));
            if (!JzbTools.isEmpty(list.get(i).get("twoheader"))){
                map.put("uid",list.get(i).get("twoheader"));
                Response serviceRegion = userRedisServiceApi.getCacheUserInfo(map);
                Map<String, Object> serviceMap = (Map<String, Object>) serviceRegion.getResponseEntity();
                if (!JzbTools.isEmpty(serviceMap)) {
                    list.get(i).put("twoheader", serviceMap.get("cname"));
                    sb.append(",").append(serviceMap.get("cname"));
                }
            }
            if (!JzbTools.isEmpty(list.get(i).get("threeheader"))){
                map.put("uid",list.get(i).get("threeheader"));
                Response serviceRegion = userRedisServiceApi.getCacheUserInfo(map);
                Map<String, Object> serviceMap = (Map<String, Object>) serviceRegion.getResponseEntity();
                if (!JzbTools.isEmpty(serviceMap)) {
                    list.get(i).put("threeheader", serviceMap.get("cname"));
                    sb.append(",").append(serviceMap.get("cname"));
                }
            }
            if (!JzbTools.isEmpty(list.get(i).get("fourheader"))){
                map.put("uid",list.get(i).get("fourheader"));
                Response serviceRegion = userRedisServiceApi.getCacheUserInfo(map);
                Map<String, Object> serviceMap = (Map<String, Object>) serviceRegion.getResponseEntity();
                if (!JzbTools.isEmpty(serviceMap)) {
                    list.get(i).put("fourheader", serviceMap.get("cname"));
                    sb.append(",").append(serviceMap.get("cname"));
                }
            }
            if (!JzbTools.isEmpty(list.get(i).get("fiveheader"))){
                map.put("uid",list.get(i).get("fiveheader"));
                Response serviceRegion = userRedisServiceApi.getCacheUserInfo(map);
                Map<String, Object> serviceMap = (Map<String, Object>) serviceRegion.getResponseEntity();
                if (!JzbTools.isEmpty(serviceMap)) {
                    list.get(i).put("fiveheader", serviceMap.get("cname"));
                    sb.append(",").append(serviceMap.get("cname"));
                }
            }
            if (!JzbTools.isEmpty(list.get(i).get("sixheader"))){
                map.put("uid",list.get(i).get("twoheadder"));
                Response serviceRegion = userRedisServiceApi.getCacheUserInfo(map);
                Map<String, Object> serviceMap = (Map<String, Object>) serviceRegion.getResponseEntity();
                if (!JzbTools.isEmpty(serviceMap)) {
                    list.get(i).put("sixheader", serviceMap.get("cname"));
                    sb.append(",").append(serviceMap.get("cname"));
                }
            }
            list.get(i).put("saleslist",sb.toString());
        }
        return list;
    }

    /**
     * CRM-所有业主-业主列表-修改
     * 点击修改判断是否是系统中的用户如果不是就新建用户
     *
     * @Author: Kuang Bin
     * @DateTime: 2019/10/11
     */
    public int modifyCompanyCommon(Map<String, Object> param) {
        long updtime = System.currentTimeMillis();
        param.put("updtime", updtime);
        param.put("status", "1");
//        if (!JzbDataType.isEmpty(JzbDataType.getString(param.get("oldphone")))) {
//            param.put("companyname", JzbDataType.getString(param.get("cname")));
//            if (!JzbDataType.isEmpty(JzbDataType.getString(param.get("password")))) {
//                // 给新负责人发送短信,系统中不存在的用户
//                param.put("groupid", config.getChangeManagerPwd());
//                param.put("senduid", "addCommon1013");
//                param.put("msgtag", "addCommon1013");
//                companyService.sendRemind(param);
//            } else {
//                // 给新负责人发送短信,系统中存在的用户没有新密码
//                param.put("groupid", config.getChangeManager());
//                param.put("senduid", "addCommon1013");
//                param.put("msgtag", "addCommon1013");
//                companyService.sendRemind(param);
//                // 加入企业部门
//                param.put("cdid", JzbDataType.getString(param.get("cid")) + "0000");
//                param.put("time", updtime);
//                deptMapper.insertDeptUser(param);
//            }
//            // 将要发送的手机号改为旧管理员手机号
//            param.put("relphone", JzbDataType.getString(param.get("oldphone")));
//            // 给老负责人发送撤销管理员信息
//            param.put("groupid", config.getOldManager());
//            param.put("senduid", "addCommon1013");
//            param.put("msgtag", "addCommon1013");
//            companyService.sendRemind(param);
//        }
        return tbCompanyCommonMapper.updateCompanyListInfo(param);
    }


    // 私海-更换销售员
    public int relpaceCompanysSales(Map<String, Object> param) {
        Map<String, Object> map = tbCompanyCommonMapper.queryCompanySales(param);
        if (param.get("uid").equals(map.get("oneheader"))){
            map.put("oneheader",param.get("saler"));
        }else if(param.get("uid").equals(map.get("twoheader"))){
            map.put("twoheader",param.get("saler"));
        }else if(param.get("uid").equals(map.get("threeheader"))){
            map.put("threeheader",param.get("saler"));
        }else if(param.get("uid").equals(map.get("fourheader"))){
            map.put("fourheader",param.get("saler"));
        }else if(param.get("uid").equals(map.get("fiveheader"))){
            map.put("fiveheader",param.get("saler"));
        }else if(param.get("uid").equals(map.get("sixheader"))){
            map.put("sixheader",param.get("saler"));
        }
        map.put("updtime",System.currentTimeMillis());
         return tbCompanyCommonMapper.updateCompanysAddSales(param);

    }

    public int delCompanysSales(Map<String, Object> param) {
        Map<String, Object> map = tbCompanyCommonMapper.queryCompanySales(param);
        if (param.get("saler").equals(map.get("oneheader"))){
            map.put("oneheader",null);
        }else if(param.get("saler").equals(map.get("twoheader"))){
            map.put("twoheader",null);
        }else if(param.get("saler").equals(map.get("threeheader"))){
            map.put("threeheader",null);
        }else if(param.get("saler").equals(map.get("fourheader"))){
            map.put("fourheader",null);
        }else if(param.get("saler").equals(map.get("fiveheader"))){
            map.put("fiveheader",null);
        }else if(param.get("saler").equals(map.get("sixheader"))){
            map.put("sixheader",null);
        }
        map.put("updtime",System.currentTimeMillis());
        return tbCompanyCommonMapper.updateCompanysAddSales(map);
    }

    // 退回公海  加入历史私海记录 todo
    public int rebackCompanysToHistory(Map<String, Object> param) {
    return tbCompanyCommonMapper.rebackCompanysToHistory(param);
    }

    public List<Map<String, Object>> getCompanyCommonListHistory(Map<String, Object> param) {
        return tbCompanyCommonMapper.getCompanyCommonListHistory(param);
    }

    public int getCompanyCommonListHistoryCount(Map<String, Object> param) {
        return tbCompanyCommonMapper.getCompanyCommonListHistoryCount(param);
    }

    public List<Map<String, Object>> queryCompanysToHistory(Map<String, Object> param) {
        // 定义地区list列表
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
        return tbCompanyCommonMapper.queryCompanysToHistory(param);
    }

    public int queryCompanysToHistoryCount(Map<String, Object> param) {
        return tbCompanyCommonMapper.queryCompanysToHistoryCount(param);
    }
}
