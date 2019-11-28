package com.jzb.org.service;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import com.jzb.org.api.auth.CompanyControllerApi;
import com.jzb.org.api.base.RegionBaseApi;
import com.jzb.org.api.open.PlatformComApi;
import com.jzb.org.api.redis.UserRedisServiceApi;
import com.jzb.org.dao.PlatformCompanyMapper;
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
 * @Date: 2019/11/7 16:58
 */
@Service
public class PlatformCompanyService {

    @Autowired
    private PlatformCompanyMapper platMapper;
    @Autowired
    private UserRedisServiceApi userRedisServiceApi;
    @Autowired
    private RegionBaseApi regionBaseApi;


    /**
     * 开放平台企业列表查询总数
     *
     * @param param
     * @return int
     * @Author: DingSC
     */
    public int searchPlatformComCount(Map<String, Object> param) {
        return platMapper.searchPlatformComCount(param);
    }

    /**
     * 开放平台企业列表查询
     *
     * @param param
     * @param cIdUIdMap 开放平台的cid和管理员id键值对
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @Author: DingSC
     */
    public List<Map<String, Object>> searchPlatformComList(Map<String, Object> param,
                                                           Map<String, Object> cIdUIdMap) {
        int type = JzbDataType.getInteger(param.get("type"));
        //查询出原始数据
        List<Map<String, Object>> cList = platMapper.searchPlatformComList(param);
        int size = cList == null ? 0 : cList.size();
        List<Map<String, Object>> result = new ArrayList<>(size);
        if (type == 1) {
            result = cList;
        } else {
            for (int i = 0; i < size; i++) {
                Map<String, Object> map = cList.get(i);
                String cid = JzbDataType.getString(map.get("cid"));
                Map<String, Object> temp = new HashMap<>(2);
                temp.put("uid", cIdUIdMap.get(cid));
                //查询企业负责人信息
                Response user = userRedisServiceApi.getCacheUserInfo(temp);
                if (JzbDataType.isMap(user.getResponseEntity())) {
                    Map<String, Object> userMap = (Map<String, Object>) user.getResponseEntity();
                    map.put("managername", userMap.get("cname"));
                    map.put("relphone", userMap.get("relphone"));

                }
                temp.put("region", map.get("region"));
                Response regRes = regionBaseApi.getRegionInfo(temp);
                map.put("region", regRes.getResponseEntity());
                result.add(map);
            }
        }


        return result;
    }

    /**
     * 获取开放平台企业id集
     *
     * @param plRes
     * @return java.lang.String
     * @Author: DingSC
     */
    public Map<String, Object> getCIds(Response plRes) {
        Map<String, Object> result = new HashMap<>(2);
        if (JzbDataType.isMap(plRes.getResponseEntity())) {
            result = (Map<String, Object>) plRes.getResponseEntity();

        }
        return result;
    }

    /**
     * 获取cid为key，uid为value，cid和uid合集的map
     *
     * @param plRes
     * @param type
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @Author: DingSC
     */
    public Map<String, Object> getCIdUId(Response plRes, int type) {
        Map<String, Object> result = new HashMap<>(2);
        if (JzbDataType.isCollection(plRes.getResponseEntity())) {
            List<Map<String, Object>> list = (List<Map<String, Object>>) plRes.getResponseEntity();
            int size = list == null ? 0 : list.size();
            StringBuilder cIdBuild = new StringBuilder();
            StringBuilder uIdBuild = new StringBuilder();
            for (int i = 0; i < size; i++) {
                Map<String, Object> map = list.get(i);
                String cid = JzbDataType.getString(map.get("cid"));
                String uid = JzbDataType.getString(map.get("manager"));
                cIdBuild.append(cid + ",");
                uIdBuild.append(uid + ",");
                if (type == 1) {
                } else {
                    result.put(cid, uid);
                }
            }
            result.put("cids", cIdBuild.toString());
            result.put("uids", uIdBuild.toString());

        }
        return result;
    }


    /**
     * 根据企业名称或企业cid集合获取cid合集
     *
     * @param param
     * @return java.lang.String
     * @Author: DingSC
     */
    public String searchCidByCidCname(Map<String, Object> param) {
        return platMapper.searchCidByCidCname(param);
    }

    /**
     * 开放平台添加产品
     *
     * @param param
     * @return int
     * @Author: DingSC
     */
    public int insertProductByOpen(Map<String, Object> param) {
        return platMapper.insertProductByOpen(param);
    }

    /**
     * 开放平台数据的添加
     * @param param
     * @return
     */
    public int insertProductByOpens(Map<String, Object> param) {
        return platMapper.insertProductByOpens(param);
    }

    /**
     * 添加菜单数据
     * @param list
     * @return
     */
    public int saveMune(List<Map<String, Object>> list) {
            for (int i = 0; i < list.size(); i++) {
                long time = System.currentTimeMillis();
                list.get(i).put("status", "1");
                list.get(i).put("addtime", time);
                list.get(i).put("updtime", time);
            }
        return platMapper.saveMune(list);
    }

    /**
     * 添加页面
     * @param list1
     * @return
     */
    public int savepage(List<Map<String, Object>> list1) {

        for (int i = 0; i < list1.size(); i++) {
            long time = System.currentTimeMillis();
            list1.get(i).put("status", "1");
            list1.get(i).put("addtime", time);
            list1.get(i).put("updtime", time);
        }
        return platMapper.savepage(list1);
    }

    public int getPage(Map<String, Object> map) {
        return platMapper.getPage(map);
    }

    public int updatePage(Map<String, Object> map) {
        return platMapper.updatePage(map);
    }

    public int getMune(Map<String, Object> map1) {
        return platMapper.getMune(map1);
    }

    public int updateMune(Map<String, Object> map1) {
        return platMapper.updateMune(map1);
    }
}
