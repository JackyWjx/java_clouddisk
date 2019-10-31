package com.jzb.org.service;

import com.alibaba.fastjson.JSON;
import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.org.api.base.RegionBaseApi;
import com.jzb.org.api.redis.TbCityRedisApi;
import com.jzb.org.api.redis.UserRedisServiceApi;
import com.jzb.org.dao.CompanyUserMapper;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 企业服务类
 *
 * @author kuangbin
 */
@Service
public class CompanyUserService {
    /**
     * 企业数据库操作对象
     */
    @Autowired
    private CompanyUserMapper companyUserMapper;

    /**
     * 查询redis缓存企业对象
     */
    @Autowired
    private UserRedisServiceApi userRedisServiceApi;

    /**
     * 查询redis缓存地区对象
     */
    @Autowired
    private TbCityRedisApi tbCityRedisApi;

    /**
     * 查询地区信息
     */
    @Autowired
    private RegionBaseApi regionBaseApi;

    @Autowired
    private CompanyService companyService;

    /**
     * 设置分页数
     */
    public Map<String, Object> setPageSize(Map<String, Object> param) {
        int pageno = JzbDataType.getInteger(param.get("pageno"));
        int pagesize = JzbDataType.getInteger(param.get("pagesize"));
        pagesize = pagesize <= 0 ? 15 : pagesize;
        pageno = pageno <= 0 ? 1 : pageno;
        param.put("pageno", (pageno - 1) * pagesize);
        param.put("pagesize", pagesize);
        return param;
    }

    /**
     * 我的单位基本资料CRM-单位用户-所有单位-单位列表
     * 获取单位总数
     *
     * @author kuangbin
     */
    public int getCompanyListCount(Map<String, Object> param) {
        int count;
        try {
            param.put("status", "1");
            count = companyUserMapper.queryCompanyListCount(param);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            count = 0;
        }
        return count;
    }

    /**
     * 我的单位基本资料CRM-单位用户-所有单位-单位列表
     * 查询所有的企业列表
     *
     * @author kuangbin
     */
    public List<Map<String, Object>> getCompanyList(Map<String, Object> param) {
        param.put("status", "1");
        param = setPageSize(param);
        List<Map<String, Object>> companyList = companyUserMapper.queryCompanyList(param);
        for (int i = 0; i < companyList.size(); i++) {
            Map<String, Object> companyMap = companyList.get(i);
            Response region = regionBaseApi.getRegionInfo(companyMap);
            companyMap.put("region", region.getResponseEntity());
        }
        return companyList;
    }

    /**
     * 我的单位基本资料CRM-单位用户-所有单位-单位列表
     * 查询所有的企业列表中的是否app已授权或电脑端已授权
     *
     * @author kuangbin
     */
    public List<Map<String, Object>> getAppType(Map<String, Object> param) {
        param.put("status", "1");
        return companyUserMapper.queryAppType(param);
    }

    /**
     * 我的单位基本资料CRM-单位用户-所有单位-单位列表
     * 点击调入公海是加入公海单位表
     *
     * @author kuangbin
     */
    public int addCompanyCommon(Map<String, Object> param) {
        int count;
        try {
            long addtime = System.currentTimeMillis();
            param.put("status", "1");
            param.put("addtime", addtime);
            param.put("updtime", addtime);
            count = companyUserMapper.insertCompanyCommon(param);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            count = 0;
        }
        return count;
    }

    /**
     * CRM-销售业主-公海-业主1
     * 点击公海显示所有的单位信息
     *
     * @author kuangbin
     */
    public List<Map<String, Object>> getCompanyCommonList(Map<String, Object> param) {
        param.put("status", "1");
        param = setPageSize(param);
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
                    List<Map<String, Object>> myJsonList = (List<Map<String, Object>>) myJsonArray.get(JzbDataType.getString(param.get("1")));
                    for (int i = 0; i < myJsonList.size(); i++) {
                        // 获取省份下所有城市的信息
                        Map<String, Object> provinceMap = myJsonList.get(i);

                        // 如果为传入的城市ID则进行下一步
                        if (!JzbDataType.isEmpty(provinceMap.get(JzbDataType.getString(param.get("city"))))) {
                            // 获取城市下所有的县级信息
                            List<Map<String, Object>> countyMap = (List<Map<String, Object>>) provinceMap.get(JzbDataType.getString(param.get("2")));
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
        List<Map<String, Object>> list = companyUserMapper.queryCompanyCommonList(param);
        int count = JzbDataType.getInteger(param.get("count"));
        // 获取单位总数
        count = count < 0 ? 0 : count;
        if (count == 0) {
            // 查询单位总数
            count = companyUserMapper.queryCommonListCount(param);
        }
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> companyMap = list.get(i);
            Response region = regionBaseApi.getRegionInfo(companyMap);
            companyMap.put("region", region.getResponseEntity());
            companyMap.put("count", count);
        }
        return list;
    }

    /**
     * CRM-销售业主-公海-业主4
     * 根据单位ID显示对应的供应商或全部供应商的总数
     *
     * @author kuangbin
     */
    public int getCompanySupplierCount(Map<String, Object> param) {
        int count;
        try {
            param.put("status", "1");
            count = companyUserMapper.queryCompanySupplierCount(param);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            count = 0;
        }
        return count;
    }

    /**
     * CRM-销售业主-公海-业主4
     * 根据单位ID显示对应的供应商或全部供应商
     *
     * @author kuangbin
     */
    public List<Map<String, Object>> getCompanySupplierList(Map<String, Object> param) {
        param.put("status", "1");
        param = setPageSize(param);
        List<Map<String, Object>> list = companyUserMapper.queryCompanySupplierList(param);
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> companyMap = list.get(i);
            Response region = regionBaseApi.getRegionInfo(companyMap);
            companyMap.put("region", region.getResponseEntity());
        }
        return list;
    }

    /**
     * CRM-销售业主-公海-业主下的项目6
     * 点击业主下的项目获取项目列表的总数
     *
     * @author kuangbin
     */
    public int getCompanyProjectCount(Map<String, Object> param) {
        int count;
        try {
            param.put("status", "1");
            count = companyUserMapper.queryCompanyProjectCount(param);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            count = 0;
        }
        return count;
    }

    /**
     * CRM-销售业主-公海-业主下的项目6
     * 点击业主下的项目获取项目列表
     *
     * @author kuangbin
     */
    public List<Map<String, Object>> getCompanyProjectList(Map<String, Object> param) {
        param.put("status", "1");
        param = setPageSize(param);
        List<Map<String, Object>> list = companyUserMapper.queryCompanyProjectList(param);
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> companyMap = list.get(i);
            Response region = regionBaseApi.getRegionInfo(companyMap);
            companyMap.put("region", region.getResponseEntity());
        }
        return list;
    }

    /**
     * CRM-销售业主-公海-业主下的项目7
     * 点击业主下的项目中新建项目
     *
     * @author kuangbin
     */
    public int addCompanyProject(Map<String, Object> param) {
        param.put("status", "1");
        param.put("projectid", JzbRandom.getRandomCharCap(19));
        param.put("addtime", System.currentTimeMillis());
        return companyUserMapper.insertCompanyProject(param);
    }

    /**
     * CRM-销售业主-公海-业主下的项目8
     * 点击业主下的项目中的修改项目按钮
     *
     * @author kuangbin
     */
    public int modifyCompanyProject(Map<String, Object> param) {
        param.put("status", "1");
        param.put("updtime", System.currentTimeMillis());
        return companyUserMapper.updateCompanyProject(param);
    }

    /**
     * CRM-销售业主-公海-业主下的人员11
     * 根据用户ID查询企业中是否存在用户
     *
     * @author kuangbin
     */
    public int getDeptCount(Map<String, Object> param) {
        param.put("status", "1");
        return companyUserMapper.queryDeptCount(param);
    }

    /**
     * CRM-销售业主-公海-业主下的人员11
     * 将用户加入单位资源池中
     *
     * @author kuangbin
     */
    public int addCompanyDept(Map<String, Object> param) {
        param.put("status", "1");
        int count = companyUserMapper.insertCompanyDept(param);
        if (count == 1) {
            param.put("groupid", "1014");
            param.put("msgtag", "addCompanyDept");
            param.put("senduid", "addCompanyDept");
            companyService.sendRemind(param);
        }
        return count;
    }

    /**
     * CRM-销售业主-公海-业主下的人员9
     * 查询业主下所有的人员信息总数
     *
     * @author kuangbin
     */
    public int getCompanyUserListCount(Map<String, Object> param) {
        int count;
        try {
            param.put("status", "1");
            count = companyUserMapper.queryCompanyUserListCount(param);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            count = 0;
        }
        return count;
    }

    /**
     * CRM-销售业主-公海-业主下的人员9
     * 查询业主下所有的人员信息
     *
     * @author kuangbin
     */
    public List<Map<String, Object>> getCompanyUserList(Map<String, Object> param) {
        param.put("status", "1");
        param = setPageSize(param);
        List<Map<String, Object>> list = companyUserMapper.queryCompanyUserList(param);
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> uidMap = list.get(i);
            Response region = userRedisServiceApi.getCacheUserInfo(uidMap);
            uidMap.put("uid", region.getResponseEntity());
        }
        return list;
    }
}
