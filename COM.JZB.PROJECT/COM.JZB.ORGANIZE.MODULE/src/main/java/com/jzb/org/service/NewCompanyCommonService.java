package com.jzb.org.service;

import com.alibaba.fastjson.JSON;
import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import com.jzb.org.api.base.RegionBaseApi;
import com.jzb.org.api.redis.TbCityRedisApi;
import com.jzb.org.config.OrgConfigProperties;
import com.jzb.org.controller.CommonUserController;
import com.jzb.org.controller.NewCompanyCommonController;
import com.jzb.org.dao.NewCompanyCommonMapper;
import com.jzb.org.dao.TbHandleContractMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author kuangbin
 */
@Service
public class NewCompanyCommonService {

    @Autowired
    private NewCompanyCommonController companyCommonController;

    @Autowired
    private NewCompanyCommonMapper newCompanyCommonMapper;

    @Autowired
    private OrgConfigProperties orgConfigProperties;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CommonUserController userController;

    /**
     * 查询地区信息
     */
    @Autowired
    private RegionBaseApi regionBaseApi;

    /**
     * 查询redis缓存地区对象
     */
    @Autowired
    private TbCityRedisApi tbCityRedisApi;

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
     * 查询所有的企业列表
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
        List<Map<String, Object>> list = newCompanyCommonMapper.queryCompanyCommonList(param);
        int count = JzbDataType.getInteger(param.get("count"));
        // 获取单位总数
        count = count < 0 ? 0 : count;
        if (count == 0) {
            // 查询单位总数
            count = newCompanyCommonMapper.queryCompanyCommonListCount(param);
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
     * 管理员创建公海单位
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: Kuang Bin
     * @DateTime: 2019/9/20 18:00
     */
    public int addCompanyCommonList(Map<String, Object> param) {
//        if (JzbDataType.isMap(param.get("send"))) {
//            Map<String, Object> send = (Map<String, Object>) param.get("send");
//            if (!JzbTools.isEmpty(send.get("relphone"))) {
//                //给负责人发送短信
//                send.put("groupid", orgConfigProperties.getAddCompany());
//                send.put("msgtag", "addCommon1013");
//                send.put("senduid", "addCommon1013");
//                companyService.sendRemind(send);
//            }
//        } else {
//            //给负责人发送短信
//            param.put("groupid", orgConfigProperties.getChangeManager());
//            param.put("msgtag", "addCommon1013");
//            param.put("senduid", "addCommon1013");
//            companyService.sendRemind(param);
//        }
        if (!JzbTools.isEmpty(param.get("relphone"))){
            Map<String,Object> umap = new HashMap<>();
            umap.put("uname",param.get("relperson"));
            umap.put("phone",param.get("relphone"));
            umap.put("cid",param.get("cid"));
            umap.put("userinfo",param.get("userinfo"));
            userController.addCommonUser(umap);
        }
            newCompanyCommonMapper.insertTbCompanyList(param);
            newCompanyCommonMapper.insertTbInfo(param);
        return newCompanyCommonMapper.insertCompanyCommonList(param);
    }

    /**
     * CRM-销售业主-公海-单位3
     * 点击修改按钮,进行公海单位修改
     *
     * @author kuangbin
     */
    public int modifyCompanyCommonList(Map<String, Object> param) {
        return newCompanyCommonMapper.updateCompanyCommonList(param);
    }

    /**
     * CRM-销售业主-公海-单位4
     * 点击删除按钮,进行公海单位删除
     *
     * @author kuangbin
     */
    public int removeCompanyCommonList(Map<String, Object> param) {
        param.put("status", "4");
        return newCompanyCommonMapper.deleteCompanyCommonList(param);
    }

    public int addCompanyCommonListSuppler(Map<String, Object> param) {
        Map<String,Object> remap = new HashMap<>();
        if (!JzbTools.isEmpty(param.get("uscc"))){
            Map<String,Object> umap = new HashMap<>();
            umap.put("uscc",param.get("uscc"));
            // 查询是否已经存在该公海单位
            List<Map<String,Object>> list = newCompanyCommonMapper.queryCommonCompanyByUscc(umap);
            if (JzbTools.isEmpty(list)){
                param.remove("birthday");
                param.put("relphone",param.get("phone"));
                param.put("relperson",param.get("managername"));
                // 新建供应商到公海单位
                Response response = companyCommonController.addCompanyCommonList(param);
                remap = (Map<String, Object>) response.getResponseEntity();
            }else {
                remap = list.get(0);
            }
        }
        // 新建供应商表
        param.put("supid",remap.get("cid"));
        param.put("addtime",System.currentTimeMillis());
        param.put("updtime",System.currentTimeMillis());
        param.put("status",'1');
        param.put("cid",param.get("maincid"));
        return newCompanyCommonMapper.addCompanyCommonListSuppler(param);
    }
}
