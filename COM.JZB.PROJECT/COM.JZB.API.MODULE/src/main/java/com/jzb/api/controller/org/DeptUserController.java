package com.jzb.api.controller.org;

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
@RequestMapping(value = "/api/org")
public class DeptUserController {
    @Autowired
    private DeptUserService deptUserService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    ApiToken apiToken;

    /**
     * 用户认证服务
     */
    @Autowired
    private JzbUserAuthService authService;

    /**
     * 根据企业id和部门中的手机号或用户姓名获取组织与用户的数据
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     * @DateTime: 2019/9/5 18:59
     */
    @RequestMapping(value = "/getDeptUser", method = RequestMethod.POST)
    @CrossOrigin
    public Response getDeptUser(@RequestBody Map<String, Object> param, @RequestHeader(value = "token") String token) {
        Response result;
        try {
            String[] str = {"cid"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                int rows = JzbDataType.getInteger(param.get("pagesize"));
                int page = JzbDataType.getInteger(param.get("pageno"));
                if (page > 0 && rows > 0) {
                    Map<String, Object> userInfo = apiToken.getUserInfoByToken(token);
                    if (userInfo.size() > 0) {
                        param.put("userinfo", userInfo);
                        PageInfo pageInfo = deptUserService.getUserDept(param);
                        result = Response.getResponseSuccess(userInfo);
                        result.setPageInfo(pageInfo);
                    } else {
                        result = Response.getResponseError();
                    }
                } else {
                    result = Response.getResponseError();
                }
            } else {
                result = Response.getResponseError();
            }
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 中台新建单位
     *
     * @param param
     * @param token
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     * @DateTime: 2019/9/20 16:59
     */
    @RequestMapping(value = "/addControlCompanyAll", method = RequestMethod.POST)
    @CrossOrigin
    public Response addControlCompanyAll(@RequestBody Map<String, Object> param, @RequestHeader(value = "token") String token) {
        Response result;
        try {
            String[] str = {"cname", "region", "phone"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                Map<String, Object> userInfo = apiToken.getUserInfoByToken(token);
                if (userInfo.size() > 0) {
                    param.put("userinfo", userInfo);
                    param.put("uid", userInfo.get("uid"));
                    param.put("type", "1");
                    result = companyService.addCompany(param);
                } else {
                    result = Response.getResponseError();
                }
            } else {
                result = Response.getResponseError();
            }
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 管理员创建伙伴单位
     *
     * @param param
     * @param token
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     * @DateTime: 2019/9/23 17:37
     */
    @RequestMapping(value = "/addCompanyFriendAll", method = RequestMethod.POST)
    @CrossOrigin
    public Response addCompanyFriendAll(@RequestBody Map<String, Object> param, @RequestHeader(value = "token") String token) {
        Response result;
        try {
            String[] str = {"cname", "region", "phone"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                Map<String, Object> userInfo = apiToken.getUserInfoByToken(token);
                if (userInfo.size() > 0) {
                    param.put("userinfo", userInfo);
                    param.put("uid", userInfo.get("uid"));
                    param.put("type", "1");
                    //默认企业是初级认证
                    param.put("status", "8");
                    result = companyService.addCompany(param);
                    authService.addAdmin(param);
                } else {
                    result = Response.getResponseError();
                }
            } else {
                result = Response.getResponseError();
            }
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 注册时创建单位
     *
     * @param param
     * @param token
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     * @DateTime: 2019/9/19 11:08
     */
    @RequestMapping(value = "/addControlCompany", method = RequestMethod.POST)
    @CrossOrigin
    public Response addControlCompany(@RequestBody Map<String, Object> param, @RequestHeader(value = "token") String token) {
        Response result;
        try {
            String[] str = {"cname", "phone"};
            String regular = "[\\w\\W]{3,20}";
            if (Pattern.matches(regular, JzbDataType.getString(param.get("cname")))) {
                if (JzbCheckParam.allNotEmpty(param, str)) {
                    Map<String, Object> userInfo = apiToken.getUserInfoByToken(token);
                    if (userInfo.size() > 0) {
                        param.put("uid", userInfo.get("uid"));
                        param.put("type", "1");
                        param.put("userinfo", userInfo);
                        result = companyService.addCompany(param);
                    } else {
                        result = Response.getResponseError();
                    }
                } else {
                    result = Response.getResponseError();
                }
            } else {
                result = Response.getResponseError();
                Map<String, Object> code = new HashMap<>(2);
                code.put("message", "2");
                code.put("cid", "");
                result.setResponseEntity(code);
            }

        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 获取所有用户列表数据接口拼接
     *
     * @param param
     * @param token
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     * @DateTime: 2019/9/20 11:03
     */
    @RequestMapping(value = "/getAllUserList", method = RequestMethod.POST)
    @CrossOrigin
    public Response getAllUserList(@RequestBody Map<String, Object> param, @RequestHeader(value = "token") String token) {
        Response result;
        try {
            int rows = JzbDataType.getInteger(param.get("pagesize"));
            int page = JzbDataType.getInteger(param.get("pageno"));
            if (page > 0 && rows > 0) {
                Map<String, Object> userInfo = apiToken.getUserInfoByToken(token);
                if (userInfo.size() > 0) {
                    param.put("userinfo", userInfo);
                    result = deptUserService.getAllUserList(param);
                } else {
                    result = Response.getResponseError();
                }
            } else {
                result = Response.getResponseError();
            }

        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

}
