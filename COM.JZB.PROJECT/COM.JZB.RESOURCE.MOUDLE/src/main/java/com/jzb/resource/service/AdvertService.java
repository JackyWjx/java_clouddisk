package com.jzb.resource.service;


import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.resource.api.redis.UserRedisApi;
import com.jzb.resource.dao.AdvertMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description: 广告接口实现类
 * @Author duanfeiyu
 * @Version v1.0
 * @Since 1.0
 * @Date: 2019/8/16 15:20
 */
@Service
public class AdvertService {

    @Autowired
    private AdvertMapper advertMapper;

    @Autowired
    private UserRedisApi userRedisApi;

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
     * 查询广告信息
     *
     * @return List<Map < String, Object>> 返回数组
     */
    public List<Map<String, Object>> queryAdvertisingList() {
        List<Map<String, Object>> list = null;
        try {
            list = advertMapper.queryAdvertisingList();
        } catch (Exception e) {
            JzbTools.logError(e);
        }
        return list;
    }

    /**
     * CRM-运营管理-活动-推广图片
     * 点击活动获取所有的系统推广信息的总数
     *
     * @author kuangbin
     */
    public int getAdvertListCount(Map<String, Object> param) {
        int count;
        try {
            count = advertMapper.queryAdvertListCount(param);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            count = 0;
        }
        return count;
    }

    /**
     * CRM-运营管理-活动-推广图片
     * 点击活动获取所有的系统推广信息
     *
     * @author kuangbin
     */
    public List<Map<String, Object>> getAdvertList(Map<String, Object> param) {
        // 设置分页参数
        param = setPageSize(param);
        param.put("status", "1");
        List<Map<String, Object>> list = advertMapper.queryAdvertList(param);
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> advertMap = list.get(i);
            // 获取企业管理员用户信息
            String uid = JzbDataType.getString(advertMap.get("ouid"));
            param.put("uid", uid);
            // 从缓存中获取用户信息
            Response response = userRedisApi.getCacheUserInfo(param);
            advertMap.put("ouid", response.getResponseEntity());
        }
        return list;
    }

    /**
     * CRM-运营管理-活动-推广图片
     * 点击保存后修改对应的推广信息
     *
     * @author kuangbin
     */
    public int modifyAdvertData(Map<String, Object> param) {
        // 加入修改时间
        param.put("updtime", System.currentTimeMillis());
        return advertMapper.updateAdvertData(param);
    }

    /**
     * CRM-运营管理-活动-推广图片
     * 点击新增增加系统广告表中的推广信息
     *
     * @author kuangbin
     */
    public int addAdvertData(Map<String, Object> param) {
        // 加入广告ID
        param.put("advid", JzbRandom.getRandomCharCap(13));
        long addtime = System.currentTimeMillis();
        // 加入创建时间
        param.put("addtime", addtime);
        return advertMapper.insertAdvertData(param);
    }

    /**
     * CRM-运营管理-活动-推广图片
     * 点击删除后修改推广信息的状态
     *
     * @author kuangbin
     */
    public int removeAdvertData(Map<String, Object> param) {
        // 加入修改时间
        param.put("updtime", System.currentTimeMillis());
        param.put("status", "4");
        return advertMapper.deleteAdvertData(param);
    }
}
