package com.jzb.org.service;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbRandom;
import com.jzb.org.api.base.RegionBaseApi;
import com.jzb.org.api.redis.TbCityRedisApi;
import com.jzb.org.api.redis.UserRedisServiceApi;
import com.jzb.org.dao.TbCompanyCommonMapper;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private UserRedisServiceApi userRedisServiceApi;

    @Autowired
    private RegionBaseApi regionBaseApi;


    @Autowired
    private TbCityRedisApi tbCityRedisApi;

    /**
     * 查询不带条件的业主单位全部（不带条件）
     * @param param
     * @return
     */
    public List<Map<String, Object>> queryCompanyCommon(Map<String, Object> param){
        return tbCompanyCommonMapper.queryCompanyCommon(param);
    }


    /**
     * 查询带条件的业主单位全部（带条件）
     * @param param
     * @return
     */
    public List<Map<String, Object>> queryCompanyCommonByKeyWord(Map<String, Object> param){
        return tbCompanyCommonMapper.queryCompanyCommonByKeyWord(param);
    }


    /**
     * 修改单位信息
     * @param param
     * @return
     */
    public int updateCompany(Map<String, Object> param){
        return tbCompanyCommonMapper.updateCompany(param);
    }


    /**
     * 查询业主列表下的查询出来的总数
     * @param param
     * @return
     */
    public int getCount(Map<String, Object> param) {
        return tbCompanyCommonMapper.getCount(param);
    }

    /**
     * 查询所有业主-业主列表
     * @param param
     * @return
     */
    public List<Map<String, Object>> getCompanyCommon(Map<String, Object> param) {



        param.put("status", "1");
        if (JzbDataType.isEmpty(JzbDataType.getString(param.get("region")))) {
            // 定义地区list列表
            List<Map<String, Object>> regionList = new ArrayList<>();

            // 定义存放每个省市县地区的map
            Map<String, Object> regionMap = new HashMap<>();
            // 传入3代表查询县级地区
            if (JzbDataType.isEmpty(JzbDataType.getString(param.get("3")))) {
                // 加入县级地区id到参数对象中
                regionMap.put("region", JzbDataType.getString(param.get("region")));
                regionList.add(regionMap);
                // 等于2代表传入的是市级地区ID
            } else if (JzbDataType.isEmpty(JzbDataType.getString(param.get("2")))) {
                // 添加查询地区的key
                param.put("key", "jzb.system.city");

                // 获取所有的地区信息
                Response response = tbCityRedisApi.getCityJson(param);

                // 将字符串转化为list
                JSONArray myJsonArray = JSONArray.fromObject(JzbDataType.getString(response.getResponseEntity()));
                for (int i = 0; i < myJsonArray.size(); i++) {
                    // 获取省份信息
                    Map<String, Object> provinceMap = (Map<String, Object>) myJsonArray.get(i);

                    // 如果为传入的省份ID则进行下一步
                    if (!JzbDataType.isEmpty(provinceMap.get(JzbDataType.getString(param.get("1"))))) {
                        // 获取省份下城市list
                        List<Map<String, Object>> cityList = (List<Map<String, Object>>) provinceMap.get(JzbDataType.getString(param.get("1")));
                        for (int k = 0; k < cityList.size(); k++) {
                            // 获取省份下的城市
                            Map<String, Object> cityMap = cityList.get(i);

                            // 如果为传入的城市ID则进行下一步
                            if (!JzbDataType.isEmpty(cityMap.get(JzbDataType.getString(param.get("2"))))) {
                                // 获取城市下所有的县级信息
                                List<Map<String, Object>> countyList = (List<Map<String, Object>>) provinceMap.get(JzbDataType.getString(param.get("1")));
                                for (int b = 0; b < countyList.size(); b++) {
                                    // 获取城市下单个的县级信息
                                    Map<String, Object> countyMap = cityList.get(i);

                                    // 将县级ID加入地区map对象中
                                    regionMap.put("region", JzbDataType.getString(countyMap.get("creaid")));
                                    regionList.add(regionMap);
                                }
                            }
                        }
                    }
                }
            } else if (JzbDataType.isEmpty(JzbDataType.getString(param.get("1")))) {
                // 添加查询地区的key
                param.put("key", "jzb.system.city");

                // 获取所有的地区信息
                Response response = tbCityRedisApi.getCityJson(param);

                // 将字符串转化为list
                JSONArray myJsonArray = JSONArray.fromObject(JzbDataType.getString(response.getResponseEntity()));
                for (int i = 0; i < myJsonArray.size(); i++) {
                    // 获取省份信息
                    Map<String, Object> provinceMap = (Map<String, Object>) myJsonArray.get(i);

                    // 如果为传入的省份ID则进行下一步
                    if (!JzbDataType.isEmpty(provinceMap.get(JzbDataType.getString(param.get("1"))))) {
                        // 获取省份下城市list
                        List<Map<String, Object>> cityList = (List<Map<String, Object>>) provinceMap.get(JzbDataType.getString(param.get("1")));
                        for (int k = 0; k < cityList.size(); k++) {
                            // 获取省份下的城市
                            Map<String, Object> cityMap = cityList.get(i);
                            if (JzbDataType.isEmpty(JzbDataType.getString(cityMap.get("list")))) {
                                List<Map<String, Object>> city = (List<Map<String, Object>>) cityMap.get("list");
                                for (int t = 0; t < city.size(); t++) {
                                    // 获取城市下单个的市级信息
                                    Map<String, Object> countyMap = cityList.get(i);

                                    // 将市级ID加入地区map对象中
                                    regionMap.put("region", JzbDataType.getString(countyMap.get("creaid")));
                                    regionList.add(regionMap);
                                }
                                continue;
                            }
                            for (Map.Entry<String, Object> entry : cityMap.entrySet()) {
                                Map<String, Object> map = (Map<String, Object>) entry.getValue();
                                // 将市级ID下的所有县级ID加入地区map对象中
                                regionMap.put("region", JzbDataType.getString(map.get("creaid")));
                                regionList.add(regionMap);
                            }
                        }
                    }
                }
            }
            // 将所有结果加入参数中传入
            param.put("list", regionList);
        }



        List<Map<String, Object>>  list =  tbCompanyCommonMapper.getCompanyCommon(param);

        for (int i = 0; i < list.size(); i++) {
            HashMap<String, Object> hashMap = new HashMap<>();
            HashMap<String, Object> Map = new HashMap<>();
            HashMap<String, Object> Map1 = new HashMap<>();
            //根据uid到缓存中获取用户名称
            Map<String,Object> map  = (Map<String, Object>) userRedisServiceApi.getCacheUserInfo(list.get(i)).getResponseEntity();
            hashMap.put("uid", list.get(i).get("uid"));
            hashMap.put("cname", map.get("cname"));
            list.get(i).put("uid", hashMap);
            //根据管理员id获取用户信息
            Map.put("uid", list.get(i).get("manager"));
            Response cacheUserInfo = userRedisServiceApi.getCacheUserInfo(Map);

            list.get(i).put("manager", cacheUserInfo.getResponseEntity());
            //根据地区id从缓存中获取地区的信息
            Response regionInfo = regionBaseApi.getRegionInfo(list.get(i));
            list.get(i).put("region", regionInfo.getResponseEntity());
        }



        return list;
    }

    /**
     * 所有业主-业主列表-新建
     * @param param
     * @return
     */
    public int saveCompanyCommon(Map<String, Object> param) {
        long time = System.currentTimeMillis();
        param.put("addtime", time);
        param.put("updtime", time);
        param.put("regtime", time);
        param.put("modtime", time);
        param.put("cid", JzbRandom.getRandomChar(7));
        return tbCompanyCommonMapper.saveCompanyCommon(param);
    }

    /**
     * 所有业主-业主列表-修改
     * @param param
     * @return
     */
    public int updateCompanyCommon(Map<String, Object> param) {
        long time = System.currentTimeMillis();
        param.put("modtime", time);
        return tbCompanyCommonMapper.updateCompanyCommon(param);
    }

    /**
     * 所有业主-业主列表-删除
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
     * @param param
     * @return
     */
    public int updateCompanys(Map<String, Object> param) {
        param.put("status", 2);
        return tbCompanyCommonMapper.updateCompanys(param);
    }
}
