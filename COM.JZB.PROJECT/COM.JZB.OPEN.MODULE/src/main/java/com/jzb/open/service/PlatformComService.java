package com.jzb.open.service;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.Response;
import com.jzb.open.api.redis.OrgRedisServiceApi;
import com.jzb.open.api.redis.UserRedisServiceApi;
import com.jzb.open.dao.PlatformComMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author dell
 * @Version 1.0
 * @Since 1.0
 * @Date: 2019/11/7 15:04
 */
@Service
public class PlatformComService {
    @Autowired
    private PlatformComMapper platformComMapper;

    @Autowired
    private UserRedisServiceApi userRedisServiceApi;

    @Autowired
    private OrgRedisServiceApi orgRedisServiceApi;

    /**
     * 获取所有开放平台的企业id
     *
     * @param param
     * @return java.lang.String
     * @Author: DingSC
     */
    public Map<String, Object> queryPlatformIds(Map<String, Object> param) {
        return platformComMapper.queryPlatformIds(param);
    }

    /**
     * 根据cid和集获取管理员信息
     *
     * @param param
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @Author: DingSC
     */
    public List<Map<String, Object>> getComAndMan(Map<String, Object> param) {
        return platformComMapper.getComAndMan(param);
    }

    /**
     * 开发者列表查询
     *
     * @param
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @Author: DingSC
     */
    public List<Map<String, Object>> searchAppDeveloper(Map<String, Object> param) {
        List<Map<String, Object>> developerList = platformComMapper.searchAppDeveloper(param);
        int size = developerList == null ? 0 : developerList.size();
        List<Map<String, Object>> result = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            Map<String, Object> map = developerList.get(i);
            String cid = JzbDataType.getString(map.get("cid"));
            Map<String, Object> temp = new HashMap<>(2);
            temp.put("uid", map.get("uid"));
            temp.put("cid", cid);
            //查询企业负责人信息
            Response user = userRedisServiceApi.getCacheUserInfo(temp);
            if (JzbDataType.isMap(user.getResponseEntity())) {
                Map<String, Object> userMap = (Map<String, Object>) user.getResponseEntity();
                map.put("user", userMap.get("cname"));
                map.put("relphone", userMap.get("relphone"));
            }
            // 从缓存中获取企业信息
            Response responseCompany = orgRedisServiceApi.getIdCompanyData(temp);
            if (JzbDataType.isMap(responseCompany.getResponseEntity())) {
                Map<String, Object> comMap = (Map<String, Object>) responseCompany.getResponseEntity();
                map.put("cname", comMap.get("cname"));
            }
            result.add(map);
        }
        return result;
    }

    /**
     * 开发者列表查询count
     *
     * @param param
     * @return int
     * @Author: DingSC
     */
    public int searchAppDeveloperCount(Map<String, Object> param) {
        return platformComMapper.searchAppDeveloperCount(param);
    }
}
