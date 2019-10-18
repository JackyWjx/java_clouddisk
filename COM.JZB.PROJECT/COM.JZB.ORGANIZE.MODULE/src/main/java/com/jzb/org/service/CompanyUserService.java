package com.jzb.org.service;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.org.api.base.RegionBaseApi;
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
     * 查询地区信息
     */
    @Autowired
    private RegionBaseApi regionBaseApi;

    @Autowired
    private CompanyService companyService;
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
        List<Map<String, Object>> companyList = companyUserMapper.queryCompanyList(param);
        for (int i = 0; i < companyList.size(); i++) {
            Map<String, Object> companyMap = companyList.get(i);
            Response region = regionBaseApi.getRegionInfo(companyMap);
            companyMap.put("region", region.getResponseEntity());
        }
        return companyList;
    }

    /**
     * 我的单位基本资料CRM-单位用户-所有单位-单位列表
     * 查询所有的企业列表中的是否app已授权或电脑端已授权
     *
     * @author kuangbin
     */
    public List<Map<String, Object>> getAppType(Map<String, Object> param) {
        param.put("status", "1");
        return companyUserMapper.queryAppType(param);
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

    /**
     * CRM-销售业主-公海-业主1
     * 点击公海显示所有的单位信息的总数
     *
     * @author kuangbin
     */
    public int getCommonListCount(Map<String, Object> param) {
        int count;
        try {
            param.put("status", "1");
            count = companyUserMapper.queryCommonListCount(param);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            count = 0;
        }
        return count;
    }

    /**
     * CRM-销售业主-公海-业主1
     * 点击公海显示所有的单位信息
     *
     * @author kuangbin
     */
    public List<Map<String, Object>> getCompanyCommonList(Map<String, Object> param) {
        param.put("status", "1");
        param = setPageSize(param);
        List<Map<String, Object>> list = companyUserMapper.queryCompanyCommonList(param);
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> companyMap = list.get(i);
            Response region = regionBaseApi.getRegionInfo(companyMap);
            companyMap.put("region", region.getResponseEntity());
        }
        return list;
    }

    /**
     * CRM-销售业主-公海-业主4
     * 根据单位ID显示对应的供应商或全部供应商的总数
     *
     * @author kuangbin
     */
    public int getCompanySupplierCount(Map<String, Object> param) {
        int count;
        try {
            param.put("status", "1");
            count = companyUserMapper.queryCompanySupplierCount(param);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            count = 0;
        }
        return count;
    }

    /**
     * CRM-销售业主-公海-业主4
     * 根据单位ID显示对应的供应商或全部供应商
     *
     * @author kuangbin
     */
    public List<Map<String, Object>> getCompanySupplierList(Map<String, Object> param) {
        param.put("status", "1");
        param = setPageSize(param);
        List<Map<String, Object>> list = companyUserMapper.queryCompanySupplierList(param);
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> companyMap = list.get(i);
            Response region = regionBaseApi.getRegionInfo(companyMap);
            companyMap.put("region", region.getResponseEntity());
        }
        return list;
    }

    /**
     * CRM-销售业主-公海-业主下的项目6
     * 点击业主下的项目获取项目列表的总数
     *
     * @author kuangbin
     */
    public int getCompanyProjectCount(Map<String, Object> param) {
        int count;
        try {
            param.put("status", "1");
            count = companyUserMapper.queryCompanyProjectCount(param);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            count = 0;
        }
        return count;
    }

    /**
     * CRM-销售业主-公海-业主下的项目6
     * 点击业主下的项目获取项目列表
     *
     * @author kuangbin
     */
    public List<Map<String, Object>> getCompanyProjectList(Map<String, Object> param) {
        param.put("status", "1");
        param = setPageSize(param);
        List<Map<String, Object>> list = companyUserMapper.queryCompanyProjectList(param);
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> companyMap = list.get(i);
            Response region = regionBaseApi.getRegionInfo(companyMap);
            companyMap.put("region", region.getResponseEntity());
        }
        return list;
    }

    /**
     * CRM-销售业主-公海-业主下的项目7
     * 点击业主下的项目中新建项目
     *
     * @author kuangbin
     */
    public int addCompanyProject(Map<String, Object> param) {
        param.put("status", "1");
        param.put("projectid", JzbRandom.getRandomCharCap(19));
        return companyUserMapper.addCompanyProject(param);
    }

    /**
     * CRM-销售业主-公海-业主下的项目8
     * 点击业主下的项目中的修改项目按钮
     *
     * @author kuangbin
     */
    public int modifyCompanyProject(Map<String, Object> param) {
        param.put("status", "1");
        param.put("updtime", System.currentTimeMillis());
        return companyUserMapper.updateCompanyProject(param);
    }

    /**
     * CRM-销售业主-公海-业主下的人员11
     * 根据用户ID查询企业中是否存在用户
     *
     * @author kuangbin
     */
    public int getDeptCount(Map<String, Object> param) {
        param.put("status", "1");
        return companyUserMapper.queryDeptCount(param);
    }

    /**
     * CRM-销售业主-公海-业主下的人员11
     * 将用户加入单位资源池中
     *
     * @author kuangbin
     */
    public int addCompanyDept(Map<String, Object> param) {
        param.put("status", "1");
        int count = companyUserMapper.insertCompanyDept(param);
        if (count == 1){
            param.put("groupid", "1014");
            param.put("msgtag", "addCompanyDept");
            param.put("senduid", "addCompanyDept");
            companyService.sendRemind(param);
        }
        return count;
    }
}
