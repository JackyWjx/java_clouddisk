package com.jzb.org.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import com.jzb.org.api.base.RegionBaseApi;
import com.jzb.org.api.redis.OrgRedisServiceApi;
import com.jzb.org.api.redis.UserRedisServiceApi;
import com.jzb.org.service.CompanyService;
import com.jzb.org.service.CompanyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 业务控制层
 *
 * @author kuangbin
 * @date 2019年7月20日
 */
@RestController
@RequestMapping("org/company")
public class CompanyUserController {
    @Autowired
    private CompanyUserService companyUserService;

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
     * CRM-单位用户-所有单位-单位列表
     * 点击所有单位显示所有单位列表信息
     *
     * @author kuangbin
     */
    @RequestMapping(value = "/getCompanyList", method = RequestMethod.POST)
    @CrossOrigin
    public Response getCompanyList(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            int count = JzbDataType.getInteger(param.get("count"));
            // 获取单位总数
            count = count < 0 ? 0 : count;
            if (count == 0) {
                // 查询单位总数
                count = companyUserService.getCompanyListCount(param);
            }
            // 返回所有的企业列表
            List<Map<String, Object>> companyList = companyUserService.getCompanyList(param);
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            result = Response.getResponseSuccess(userInfo);
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(companyList);
            pageInfo.setTotal(count > 0 ? count : companyList.size());
            result.setPageInfo(pageInfo);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * CRM-单位用户-所有单位-单位列表
     * 点击调入公海是加入公海单位表
     *
     * @author kuangbin
     */
    @RequestMapping(value = "/addCompanyCommon", method = RequestMethod.POST)
    @CrossOrigin
    public Response addCompanyCommon(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            // 返回所有的企业列表
            int count = companyUserService.addCompanyCommon(param);
            result = count == 1 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * CRM-单位用户-所有单位-单位列表
     * 单位创建成功发送短信提醒
     *
     * @author kuangbin
     */
    @RequestMapping(value = "/sendRemind", method = RequestMethod.POST)
    @CrossOrigin
    public Response sendRemind(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            param.put("username", JzbDataType.getString(param.get("name")));
            param.put("companyname", JzbDataType.getString(param.get("cname")));
            param.put("relphone", JzbDataType.getString(param.get("phone")));
            param.put("groupid", "1013");
            param.put("msgtag", "sendRemind1013");
            param.put("senduid", "sendRemind1013");
            result = companyService.sendRemind(param);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * CRM-销售业主-公海-业主1
     * 点击公海显示所有的单位信息
     *
     * @author kuangbin
     */
    @RequestMapping(value = "/getCompanyCommonList", method = RequestMethod.POST)
    @CrossOrigin
    public Response getCompanyCommonList(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            int count = JzbDataType.getInteger(param.get("count"));
            // 获取单位总数
            count = count < 0 ? 0 : count;
            if (count == 0) {
                // 查询单位总数
                count = companyUserService.getCommonListCount(param);
            }
            // 返回所有的企业列表
            List<Map<String, Object>> companyList = companyUserService.getCompanyCommonList(param);
            result = Response.getResponseSuccess(userInfo);
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(companyList);
            pageInfo.setTotal(count > 0 ? count : companyList.size());
            result.setPageInfo(pageInfo);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * CRM-销售业主-公海-业主4
     * 根据单位ID显示对应的供应商或全部供应商
     *
     * @author kuangbin
     */
    @RequestMapping(value = "/getCompanySupplierList", method = RequestMethod.POST)
    @CrossOrigin
    public Response getCompanySupplierList(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            int count = JzbDataType.getInteger(param.get("count"));
            // 获取单位总数
            count = count < 0 ? 0 : count;
            if (count == 0) {
                // 查询单位总数
                count = companyUserService.getCompanySupplierCount(param);
            }
            // 返回所有的企业列表
            List<Map<String, Object>> companyList = companyUserService.getCompanySupplierList(param);
            result = Response.getResponseSuccess(userInfo);
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(companyList);
            pageInfo.setTotal(count > 0 ? count : companyList.size());
            result.setPageInfo(pageInfo);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }
}
