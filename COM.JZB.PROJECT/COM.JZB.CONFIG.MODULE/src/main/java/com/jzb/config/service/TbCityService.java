package com.jzb.config.service;

import com.jzb.base.data.JzbDataType;
import com.jzb.config.dao.TbCityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TbCityService {

    @Autowired
    private TbCityMapper tbCityMapper;

    /**
     * 添加城市
     *
     * @param list
     * @return
     */
    public int addCityList(List<Map<String, Object>> list) {
        return tbCityMapper.addCityList(list);
    }

    /**
     * 查询城市
     *
     * @param param
     * @return
     */
    public List<Map<String, Object>> getCityList(Map<String, Object> param) {
        return tbCityMapper.getCityList(param);
    }

    /**
     * 根据地区名称获取地区ID信息
     *
     * @param param(包含地区名称)
     * @author kuangbin
     */
    public Map<String, Object> getRegionID(Map<String, Object> param) {
        Map<String, Object> regionID = new HashMap<>();
        String regionName = JzbDataType.getString(param.get("regionName"));
        // 判断地区名是否为空
        if (!JzbDataType.isEmpty(regionName)) {
            String province = "";
            String city = "";
            String county = "";
            String[] name = regionName.split("-");
            if (name.length == 1) {
                // 获取省名
                province = name[0];
            } else if (name.length == 2) {
                province = name[0];
                // 获取市名
                city = name[1];
            } else if (name.length == 3) {
                province = name[0];
                city = name[1];
                // 获取县名
                county = name[2];
            }
            param.put("province", province);
            param.put("city", city);
            param.put("county", county);
            regionID = tbCityMapper.queryRegionID(param);
        }
        return regionID;
    }

    /**
     * 根据地区ID获取地区信息
     *
     * @param param(包含地区名称)
     * @author kuangbin
     */
    public Map<String, Object> getRegionInfo(Map<String, Object> param) {
        // 加入查询状态
        param.put("status", 1);
        param.put("region", JzbDataType.getString(param.get("region")));
        Map<String, Object> region = tbCityMapper.queryRegionInfo(param);
        Map<String, Object> name = new HashMap<>();
        // 判断市级是否为空
        if (JzbDataType.isEmpty(JzbDataType.getString(region.get("city")))) {
            // 为空直接返回查询的结果
            region.put("level1", JzbDataType.getString(region.get("creaid")));
            // 判断县级是否为空
        } else if (JzbDataType.isEmpty(JzbDataType.getString(region.get("county")))) {
            // 为空加入省级名称查询省级ID
            name.put("regionName", JzbDataType.getString(region.get("province")));
            name = getRegionID(name);
            // 加入省级ID
            region.put("level1", JzbDataType.getString(name.get("creaid")));

            // 加入查询到的市级ID
            region.put("level2", JzbDataType.getString(region.get("creaid")));
        } else {
            // 都不为空查询省级ID和市级ID
            String regionName = JzbDataType.getString(region.get("province")) + "-" + JzbDataType.getString(region.get("city"));
            name.put("regionName", regionName);
            // 查询市级
            name = getRegionID(name);
            region.put("level2", JzbDataType.getString(name.get("creaid")));
            name.put("regionName", JzbDataType.getString(region.get("province")));
            // 查询省级
            name = getRegionID(name);
            region.put("level1", JzbDataType.getString(name.get("creaid")));
            // 加入查询到的县级
            region.put("level3", JzbDataType.getString(region.get("creaid")));
        }
        return region;
    }
}
