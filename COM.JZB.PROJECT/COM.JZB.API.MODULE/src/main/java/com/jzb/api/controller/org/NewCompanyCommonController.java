package com.jzb.api.controller.org;

import com.jzb.api.api.org.CompanyCommonApi;
import com.jzb.api.api.org.CompanyOrgApi;
import com.jzb.api.api.org.NewCompanyCommonApi;
import com.jzb.api.service.CompanyService;
import com.jzb.api.service.DeptUserService;
import com.jzb.api.service.JzbUserAuthService;
import com.jzb.api.util.ApiToken;
import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @Description:
 * @Author dell
 * @Version 1.0
 * @Since 1.0
 * @Date: 2019/9/5 15:25
 */
@RestController
@RequestMapping(value = "/api/newCompanyCommon")
public class NewCompanyCommonController {
    @Autowired
    private DeptUserService deptUserService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ApiToken apiToken;

    /**
     * 用户认证服务
     */
    @Autowired
    private JzbUserAuthService authService;

    @Autowired
    private NewCompanyCommonApi newCompanyCommonApi;

    @Autowired
    private CompanyCommonApi companyCommonApi;

    /**
     * 创建公海单位没有负责人id时创建负责人账号并创建公海单位
     *
     * @param param
     * @param token
     * @return com.jzb.base.message.Response
     * @Author: Kuang Bin
     * @DateTime: 2019/10/11
     */
    @RequestMapping(value = "/addCompanyCommonList", method = RequestMethod.POST)
    @CrossOrigin
    public Response addCompanyCommonList(@RequestBody Map<String, Object> param, @RequestHeader(value = "token") String token) {
        Response result;
        try {
            if (toPhone(JzbDataType.getString(param.get("phone")))) {
                Map<String, Object> userInfo = apiToken.getUserInfoByToken(token);
                if (userInfo.size() > 0) {
                    param.put("userinfo", userInfo);
                    //先确认负责人id
                    Map<String, Object> send = companyService.getUid(param);
                    String uid = JzbDataType.getString(send.get("uid"));
                    if (JzbTools.isEmpty(uid)) {
                        result = Response.getResponseError();
                    } else {
                        // 创建公海单位
                        param.put("companyname", JzbDataType.getString(param.get("cname")));
                        param.put("manager", uid);
                        param.put("managername", JzbDataType.getString(param.get("managername")));
                        // 公海单位默认未认证状态
                        param.put("type", "1");
                        param.put("authid", "0");
                        Response comRes = companyService.addCompany(param);
                        String cid = "";
                        if (JzbDataType.isMap(comRes.getResponseEntity())) {
                            Map<String, Object> comMap = (Map<String, Object>) comRes.getResponseEntity();
                            cid = JzbDataType.getString(comMap.get("cid"));
                        }
                        // 创建公海单位表数据
                        param.put("cid", cid);
                        param.put("send", send);
                        result = newCompanyCommonApi.addCompanyCommonList(param);
                        authService.addAdmin(param);
                    }
                } else {
                    result = Response.getResponseError();
                }
            } else {
                result = Response.getResponseError();
                result.setResponseEntity("电话号码输入有误!");
            }
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * CRM-销售业主-公海-供应商5
     * 点击新建供应商建立单位下供应商
     *
     * @param param
     * @param token
     * @return com.jzb.base.message.Response
     * @Author: Kuang Bin
     * @DateTime: 2019/10/11
     */
    @RequestMapping(value = "/addCompanySupplier", method = RequestMethod.POST)
    @CrossOrigin
    public Response addCompanySupplier(@RequestBody Map<String, Object> param, @RequestHeader(value = "token") String token) {
        Response result;
        try {
            if (toPhone(JzbDataType.getString(param.get("phone")))) {
                Map<String, Object> userInfo = apiToken.getUserInfoByToken(token);
                if (userInfo.size() > 0) {
                    //先确认负责人id
                    Map<String, Object> send = companyService.getUid(param);
                    String uid = JzbDataType.getString(send.get("uid"));
                    if (JzbTools.isEmpty(uid)) {
                        result = Response.getResponseError();
                    } else {
                        param.put("type", "1");
                        // 创建供应商单位
                        param.put("userinfo", userInfo);
                        param.put("companyname", JzbDataType.getString(param.get("cname")));
                        param.put("manager", uid);
                        String cid = "";
                        Response comRes = companyService.addCompany(param);
                        if (JzbDataType.isMap(comRes.getResponseEntity())) {
                            Map<String, Object> comMap = (Map<String, Object>) comRes.getResponseEntity();
                            cid = JzbDataType.getString(comMap.get("cid"));
                        }
                        // 创建供应商单位表数据
                        param.put("cid", cid);
                        param.put("send", send);
                        result = newCompanyCommonApi.addCompanyCommonList(param);
                        authService.addAdmin(param);
                    }
                } else {
                    result = Response.getResponseError();
                }
            } else {
                result = Response.getResponseError();
                result.setResponseEntity("电话号码输入有误!");
            }
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 校验手机号
     *
     * @param obj
     * @return
     */
    private boolean toPhone(String obj) {
        boolean result = true;
        try {
            String eg = "^[1][3,4,5,6,7,8,9][0-9]{9}$";
            result = Pattern.matches(eg, obj);
        } catch (Exception e) {
            JzbTools.logError(e);
            e.printStackTrace();
        }
        return result;
    }

    /**
     * CRM-销售业主-公海-单位3
     * 点击修改按钮,进行公海单位修改
     *
     * @param param
     * @param token
     * @return com.jzb.base.message.Response
     * @Author: Kuang Bin
     * @DateTime: 2019/10/11
     */
    @RequestMapping(value = "/modifyCompanyCommonList", method = RequestMethod.POST)
    @CrossOrigin
    public Response modifyCompanyCommonList(@RequestBody Map<String, Object> param, @RequestHeader(value = "token") String token) {
        Response result;
        try {
            if (toPhone(JzbDataType.getString(param.get("phone")))) {
                Map<String, Object> userInfo = apiToken.getUserInfoByToken(token);
                param.put("userinfo", userInfo);
                if (userInfo.size() > 0) {
                    //先确认负责人id
                    Map<String, Object> send = companyService.getUid(param);
                    String uid = JzbDataType.getString(send.get("uid"));
                    if (JzbTools.isEmpty(uid)) {
                        result = Response.getResponseError();
                    } else {
                        param.put("manager", uid);
                        param.putAll(send);
                        result = newCompanyCommonApi.modifyCompanyCommonList(param);
                    }
                } else {
                    result = Response.getResponseError();
                }
            } else {
                result = Response.getResponseError();
                result.setResponseEntity("电话号码输入有误!");
            }
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }
}
