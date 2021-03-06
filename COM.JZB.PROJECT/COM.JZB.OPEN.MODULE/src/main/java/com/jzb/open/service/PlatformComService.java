package com.jzb.open.service;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.Response;
import com.jzb.base.tree.JzbTree;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.open.api.org.PlatformCompanyApi;
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

    @Autowired
    private PlatformCompanyApi platformCompanyApi;

    @Autowired
    private OpenPageService openPageService;
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
     * 开发者列表查询count
     *
     * @param param
     * @return int
     * @Author: DingSC
     */
    public int searchAppDeveloperCount(Map<String, Object> param) {
        return platformComMapper.searchAppDeveloperCount(param);
    }

    /**
     * 开发者列表查询
     *
     * @param
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @Author: DingSC
     */
    public List<Map<String, Object>> searchAppDeveloper(Map<String, Object> param) {
        return addList(platformComMapper.searchAppDeveloper(param));
    }

    /**
     * 产品列表审批查询
     *
     * @param param
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @Author: DingSC
     */
    public List<Map<String, Object>> searchApplicationVerify(Map<String, Object> param) {
        return addList(platformComMapper.searchApplicationVerify(param));
    }

    /**
     * 产品列表审批查询总数
     *
     * @param param
     * @return int
     * @Author: DingSC
     */
    public int searchApplicationVerifyC(Map<String, Object> param) {
        return platformComMapper.searchApplicationVerifyCount(param);
    }

    /**
     * 添加企业名称和负责人名称联系电话
     *
     * @param list
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @Author: DingSC
     */
    private List<Map<String, Object>> addList(List<Map<String, Object>> list) {
        int size = list == null ? 0 : list.size();
        List<Map<String, Object>> result = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            Map<String, Object> map = list.get(i);
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
     * 审批产品列表
     *
     * @param param
     * @return int
     * @Author: DingSC
     */
    public int updateVerify(Map<String, Object> param) {
        int status = JzbDataType.getInteger(param.get("status"));
        param.put("updtime", System.currentTimeMillis());
        int result = platformComMapper.updateVerify(param);
        //更新成功且审核状态为通过，将数据加入到产品表中
        if ((result > 0) && (status == 2)) {
            List<Map<String, Object>> appList = platformComMapper.queryVerify(param);
            if (appList != null && appList.size() > 0) {
                Map<String, Object> appMap = appList.get(0);
                appMap.put("userinfo", param.get("userinfo"));
                //查询应用菜单的数据
                List<Map<String,Object>> list =  openPageService.serachApplicationMenu(param);
                List<Map<String,Object>> list1 = getApplicationPage(param);
                appMap.put("list", list);
                appMap.put("list1", list1);
                Response response = platformCompanyApi.addProductByOpen(appMap);
            }

        }
        return result;
    }

    /**
     * 新增平台开发文档表
     *
     * @param param
     * @return int
     * @Author: DingSC
     */
    public int insertPlatformHelper(Map<String, Object> param) {
        param.put("helpid", JzbRandom.getRandomCharCap(5));
        param.put("time", System.currentTimeMillis());
        if (!JzbTools.isEmpty(param.get("appline"))) {
            param.put("appline", JzbDataType.getInteger(param.get("appline")));
        }
        param.put("status", '1');
        return platformComMapper.insertPlatformHelper(param);
    }

    /**
     * 修改平台开发文档表信息
     *
     * @param param
     * @return int
     * @Author: DingSC
     */
    public int updatePlatformHelper(Map<String, Object> param) {
        param.put("time", System.currentTimeMillis());
        return platformComMapper.updatePlatformHelper(param);
    }

    /**
     * 查询平台开发文档表
     *
     * @param param
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @Author: DingSC
     */
    public List<Map<String, Object>> searchPlatformHelper(Map<String, Object> param) {
        return platformComMapper.searchPlatformHelper(param);
    }

    /**
     * 查询平台开发文档表总数
     *
     * @param param
     * @return int
     * @Author: DingSC
     */
    public int searchPlatformHelperCount(Map<String, Object> param) {
        return platformComMapper.searchPlatformHelperCount(param);
    }

    /**
     * 新增开放文档类型
     *
     * @param param
     * @return int
     * @Author: DingSC
     */
    public int insertOpenApiType(Map<String, Object> param) {
        param.put("time", System.currentTimeMillis());
        param.put("otid", JzbRandom.getRandomCharCap(5));
        param.put("status", "1");
        return platformComMapper.insertOpenApiType(param);
    }

    /**
     * 修改开放文档类型
     *
     * @param param
     * @return int
     * @Author: DingSC
     */
    public int updateOpenApiType(Map<String, Object> param) {
        param.put("time", System.currentTimeMillis());
        return platformComMapper.updateOpenApiType(param);
    }

    /**
     * 获取开放文档类型树
     *
     * @param param
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @Author: DingSC
     */
    public List<Map<String, Object>> getOpenApiType(Map<String, Object> param) {
        List<Map<String, Object>> list = platformComMapper.getOpenApiType(param);
        String id = "otid";
        String pid = "potid";
        String first = "00000";
        List<Map<String, Object>> result = JzbTree.getTreeMap(list, id, pid, first);
        return result;
    }

    /**
     * 新增文档类型接口表
     *
     * @param param
     * @return int
     * @Author: DingSC
     */
    public int insertOpenApiList(Map<String, Object> param) {
        param.put("time", System.currentTimeMillis());
        param.put("apiid", JzbRandom.getRandomCharCap(7));
        param.put("status", "1");
        return platformComMapper.insertOpenApiList(param);
    }

    /**
     * 修改文档类型接口表
     *
     * @param param
     * @return int
     * @Author: DingSC
     */
    public int updateOpenApiList(Map<String, Object> param) {
        param.put("time", System.currentTimeMillis());
        return platformComMapper.updateOpenApiList(param);
    }

    /**
     * 查询文档类型接口总数
     *
     * @param param
     * @return int
     * @Author: DingSC
     */
    public int searchOpenApiListCou(Map<String, Object> param) {
        return platformComMapper.searchOpenApiListCou(param);
    }

    /**
     * 模糊查询文档类型接口
     *
     * @param param
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @Author: DingSC
     */
    public List<Map<String, Object>> searchOpenApiList(Map<String, Object> param) {
        return platformComMapper.searchOpenApiList(param);
    }

    /**
     * 新增开发者应用
     *
     * @param param
     * @return int
     * @Author: DingSC
     */
    public int insertOrgApplication(Map<String, Object> param) {
        int dev = JzbDataType.getInteger(param.get("devtype"));
        if (dev != 1) {
            param.put("devtype", "2");
        }
        int app = JzbDataType.getInteger(param.get("apptype"));
        if (app != 2) {
            param.put("apptype", 1);
        }
        String key = "appline";
        if (!JzbTools.isEmpty(param.get(key))) {
            param.put(key, JzbDataType.getInteger(param.get(key)));
        }
        param.put("appid", JzbRandom.getRandomCharCap(11));
        param.put("time", System.currentTimeMillis());
        param.put("status", "1");
        param.put("appkey", JzbRandom.getRandom(16));
        param.put("appsecret", JzbRandom.getRandom(32));
        param.put("checkcode", JzbRandom.getRandomCharCap(32));
        return platformComMapper.insertOrgApplication(param);
    }

    /**
     * 修改开发者应用
     *
     * @param param
     * @return int
     * @Author: DingSC
     */
    public int updateOrgApplication(Map<String, Object> param) {
        param.put("time", System.currentTimeMillis());
        int result = 1;
        if (JzbDataType.getInteger(param.get("status")) == 2) {
            int verify = platformComMapper.queryVerifyCount(param);
            if (verify > 0) {
                result = 3;
            } else {
                platformComMapper.updateOrgApplication(param);
            }
        } else {
            String key = "appline";
            if (!JzbTools.isEmpty(param.get(key))) {
                param.put(key, JzbDataType.getInteger(param.get(key)));
            }
            String key1 = "devtype";
            if (!JzbTools.isEmpty(param.get(key1))) {
                int dev = JzbDataType.getInteger(param.get(key1));
                if (dev != 1) {
                    param.put(key1, "2");
                }
            }
            String key2 = "apptype";
            if (!JzbTools.isEmpty(param.get(key2))) {
                int app = JzbDataType.getInteger(param.get(key2));
                if (app != 2) {
                    param.put(key2, 1);
                }
            }
            result = platformComMapper.updateOrgApplication(param);
        }

        return result;
    }

    public List<Map<String, Object>> getApplicationPage(Map<String, Object> param) {
        return platformComMapper.getApplicationPage(param);
    }
}
