package com.jzb.api.service;

import com.jzb.api.api.auth.RoleAuthApi;
import com.jzb.api.api.auth.UserAuthApi;
import com.jzb.api.api.org.CompanyOrgApi;
import com.jzb.api.api.org.DeptOrgApi;
import com.jzb.api.api.redis.UserRedisApi;
import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.tree.JzbTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author dell
 * @Version 1.0
 * @Since 1.0
 * @Date: 2019/9/5 15:29
 */
@Service
public class DeptUserService {

    @Autowired
    private RoleAuthApi roleAuthApi;
    @Autowired
    private DeptOrgApi deptOrgApi;
    @Autowired
    private UserAuthApi userAuthApi;
    @Autowired
    private UserRedisApi redisApi;
    @Autowired
    private CompanyOrgApi companyOrgApi;

    /**
     * 获取部门的用户信息，用户的角色组名称和用户积分
     *
     * @param map
     * @return com.jzb.base.message.PageInfo
     * @Author: DingSC
     * @DateTime: 2019/9/5 17:59
     */
    public PageInfo getUserDept(Map<String, Object> map) {
        PageInfo pageInfo;
        //根据用户姓名或手机号获取用户部门信息和在职状态
        Response deptRes = deptOrgApi.getDeptUser(map);
        pageInfo = deptRes.getPageInfo();
        List<Map<String, Object>> deptList = pageInfo.getList();
        if (deptList != null && deptList.size() > 0) {
            Map<String, Object> userParam = new HashMap<>(2);
            //为调用的接口赋值
            userParam.put("cid", map.get("cid"));
            userParam.put("list", deptList);
            userParam.put("userinfo", map.get("userinfo"));
            //根据用户部门id和用户id获取用户角色组信息
            Response roleRes = roleAuthApi.getUserDeptGroupList(userParam);
            String tempK = "uid,cdid";
            //获取需要拼接的字段名称
            String roleMap = "rgnames,authid";
            //处理数据
            pageInfo.setList(merge(deptList, roleRes, tempK, roleMap));
        }
        return pageInfo;
    }

    /**
     * 获取所有用户列表数据接口拼接
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     * @DateTime: 2019/9/20 10:56
     */
    public Response getAllUserList(Map<String, Object> param) {
        //第一步查询出用户表信息
        Response response = userAuthApi.searchUserUid(param);
        PageInfo userPage = response.getPageInfo();
        List<Map<String, Object>> userList = userPage.getList();
        if (userList != null && userList.size() > 0) {
            Map<String, Object> userParam = new HashMap<>(2);
            //为调用的接口赋值
            userParam.put("list", userList);
            userParam.put("userinfo", param.get("userinfo"));
            Response deptRe = companyOrgApi.getCompanyByUid(userParam);
            String tempK = "uid";
            String comMap = "joname,regname";
            //处理数据
            userPage.setList(merge(userList, deptRe, tempK, comMap));
        }
        return response;
    }

    /**
     * 将用户部门信息和角色组信息合并，并添加用户积分
     *
     * @param deptList 主list
     * @param roleRes  需要拼接的数据
     * @param keys     临时表key组成
     * @param roleMap  需要拼接的字段
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @Author: DingSC
     * @DateTime: 2019/9/20 11:46
     */
    private List<Map<String, Object>> merge(List<Map<String, Object>> deptList, Response roleRes, String keys, String roleMap) {
        int deptSize = deptList.size();
        List<Map<String, Object>> result = new ArrayList<>(deptSize);
        Map<String, Map<String, Object>> tempMap = new HashMap<>(deptSize);
        //需要拼接字段
        String[] roleS = JzbTree.toStringArray(roleMap);
        int length = roleS.length;
        //需要拼接的数据放入临时表中
        for (int i = 0; i < deptSize; i++) {
            Map<String, Object> deptMap = deptList.get(i);
            String key = getKey(deptMap, keys);
            for (int j = 0; j < length; j++) {
                deptMap.put(roleS[j], "");
            }
            deptMap.put("score", 0);
            tempMap.put(key, deptMap);
        }
        //添加需要拼接的字段和个人积分到主List，
        if (JzbDataType.isCollection(roleRes.getResponseEntity())) {
            List<Map<String, Object>> rolList = (List<Map<String, Object>>) roleRes.getResponseEntity();
            int rolSize = rolList.size();
            for (int i = 0; i < rolSize; i++) {
                Map<String, Object> rolMap = rolList.get(i);
                //需要拼接的数据key获取
                String key = getKey(rolMap, keys);
                if (tempMap.containsKey(key)) {
                    //获取拼接数据中符合条件的数据
                    Map<String, Object> deptMap = tempMap.get(key);
                    for (int j = 0; j < length; j++) {
                        deptMap.put(roleS[j], rolMap.get(roleS[j]));
                    }
                    deptMap.put("score", getScore(rolMap));
                }
            }
        }
        //将临时map中的数据加入返回list中
        for (Map<String, Object> map : tempMap.values()) {
            result.add(map);
        }
        return result;
    }

    /**
     * 获取临时表的key
     *
     * @param map
     * @param key
     * @return java.lang.String
     * @Author: DingSC
     * @DateTime: 2019/9/20 11:39
     */
    private String getKey(Map<String, Object> map, String key) {
        String result = "";
        String[] keyS = JzbTree.toStringArray(key);
        int length = keyS.length;
        if (length > 0) {
            for (int i = 0; i < length; i++) {
                result += JzbDataType.getString(map.get(keyS[i]));
            }
        }
        return result;
    }

    /**
     * 获取用户可用积分
     *
     * @param rolMap
     * @return int
     * @Author: DingSC
     * @DateTime: 2019/9/5 17:28
     */
    private int getScore(Map<String, Object> rolMap) {
        int score = 0;
        Response response = redisApi.getCacheUserInfo(rolMap);
        if (JzbDataType.isMap(response.getResponseEntity())) {
            Map<String, Object> userMap = (Map<String, Object>) response.getResponseEntity();
            score = JzbDataType.getInteger(userMap.get("score"));
        }
        return score;
    }


}
