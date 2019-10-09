package com.jzb.resource.service;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbRandom;
import com.jzb.resource.api.redis.UserRedisApi;
import com.jzb.resource.dao.AdvertMapper;
import com.jzb.resource.dao.TbSolutionDomMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@Service
public class TbSolutionDomService {

    @Autowired
    private TbSolutionDomMapper tbSolutionDomMapper;

    @Autowired
    private UserRedisApi userRedisApi;

    /**
     * 查询方案文档
     *
     * @param param
     * @return
     */
    public List<Map<String, Object>> getSolutionDom(Map<String, Object> param) {
        List<Map<String, Object>> list = tbSolutionDomMapper.querySolutionDom(param);
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> advertMap = list.get(i);
            // 获取发布人用户信息
            String adduid = JzbDataType.getString(advertMap.get("adduid"));
            param.put("uid", adduid);
            // 从缓存中获取用户信息
            Response response = userRedisApi.getCacheUserInfo(param);
            advertMap.put("adduid", response.getResponseEntity());
        }
        return list;
    }

    /**
     * 查询方案文档(模糊查询)
     *
     * @param param
     * @return
     */
    public List<Map<String, Object>> getSolutionDomCname(Map<String, Object> param) {
        return tbSolutionDomMapper.querySolutionDomCname(param);
    }

    /**
     * 查询方案文档详情ById
     *
     * @param param
     * @return
     */
    public List<Map<String, Object>> queryDomByDomid(Map<String, Object> param) {
        return tbSolutionDomMapper.queryDomByDomid(param);
    }

    /*
     * 查询总数
     * */
    public int queryCount(Map<String, Object> param) {
        return tbSolutionDomMapper.queryCount(param);
    }

    /**
     * 文档热门榜
     */
    public List<Map<String, Object>> queryHotDom(Map<String, Object> param) {
        return tbSolutionDomMapper.queryHotDom(param);
    }

    /**
     * CRM-运营管理-解决方案-文章列表
     * 点击解决方案中的新建后加入新建的方案文章
     *
     * @author kuangbin
     */
    public int addSolutionDom(Map<String, Object> param) {
        long addtime = System.currentTimeMillis();
        // 获取文档ID
        String domid = JzbRandom.getRandomCharCap(11);
        param.put("addtime", addtime);
        param.put("domid", domid);
        param.put("status", "1");
        // 加入文章信息
        return tbSolutionDomMapper.insertSolutionDom(param);
    }

    /**
     * CRM-运营管理-解决方案-文章列表
     * 点击保存后对解决方案中的文章进行修改
     *
     * @author kuangbin
     */
    public int modifySolutionDom(Map<String, Object> param) {
        long updtime = System.currentTimeMillis();
        param.put("updtime", updtime);
        // 加入文章信息
        return tbSolutionDomMapper.updateSolutionDom(param);
    }

    /**
     * CRM-运营管理-解决方案-文章列表
     * 点击删除后对解决方案中的文章进行删除操作(即修改状态)
     *
     * @author kuangbin
     */
    public int removeSolutionDom(Map<String, Object> param) {
        long updtime = System.currentTimeMillis();
        param.put("updtime", updtime);
        param.put("status", "2");
        // 加入文章信息
        return tbSolutionDomMapper.deleteSolutionDom(param);
    }

    /**
     * CRM-运营管理-活动-文章列表
     * 点击搜索解决方案文章标题后进行模糊搜索的总数查询,可加入时间
     *
     * @author kuangbin
     */
    public int searchSolutionDomCount(Map<String, Object> param) {
        param.put("status", "1");
        return tbSolutionDomMapper.searchSolutionDomCount(param);
    }

    /**
     * CRM-运营管理-活动-文章列表
     * 点击搜索解决方案文章标题后进行模糊搜索,可加入时间
     *
     * @author kuangbin
     */
    public List<Map<String, Object>> searchSolutionDom(Map<String, Object> param) {
        param.put("status", "1");
        // 设置分页参数
        param = setPageSize(param);
        List<Map<String, Object>> list = tbSolutionDomMapper.searchSolutionDom(param);
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> advertMap = list.get(i);
            // 获取企业管理员用户信息
            String uid = JzbDataType.getString(advertMap.get("adduid"));
            param.put("uid", uid);
            // 从缓存中获取用户信息
            Response response = userRedisApi.getCacheUserInfo(param);
            advertMap.put("uid", response.getResponseEntity());
        }
        return list;
    }

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
}
