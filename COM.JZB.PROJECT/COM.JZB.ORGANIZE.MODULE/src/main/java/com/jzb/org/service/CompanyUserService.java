package com.jzb.org.service;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import com.jzb.org.api.redis.UserRedisServiceApi;
import com.jzb.org.dao.CompanyUserMapper;
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
        List<Map<String, Object>> list = companyUserMapper.queryCompanyList(param);
        return list;
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
}
