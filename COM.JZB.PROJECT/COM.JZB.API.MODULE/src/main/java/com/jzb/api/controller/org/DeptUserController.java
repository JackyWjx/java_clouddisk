package com.jzb.api.controller.org;

import com.jzb.api.api.org.CompanyCommonApi;
import com.jzb.api.api.org.CompanyOrgApi;
import com.jzb.api.service.CompanyService;
import com.jzb.api.service.DeptUserService;
import com.jzb.api.service.JzbUserAuthService;
import com.jzb.api.util.ApiToken;
import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.office.JzbExcelOperater;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
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
    private ApiToken apiToken;

    /**
     * 用户认证服务
     */
    @Autowired
    private JzbUserAuthService authService;

    @Autowired
    private CompanyOrgApi companyOrgApi;

    @Autowired
    private CompanyCommonApi companyCommonApi;

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
            String[] str = {};
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
     * 创建伙伴单位没有负责人id时创建负责人账号
     *
     * @param param
     * @param token
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     * @DateTime: 2019/10/11 10:01
     */
    @RequestMapping(value = "/addFriendCompanyAll", method = RequestMethod.POST)
    @CrossOrigin
    public Response addFriendCompanyAll(@RequestBody Map<String, Object> param, @RequestHeader(value = "token") String token) {
        Response result;
        try {
            String[] str = {"cname", "region", "phone", "managername"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                Map<String, Object> userInfo = apiToken.getUserInfoByToken(token);
                if (userInfo.size() > 0) {
                    //先确认负责人id
                    Map<String, Object> send = companyService.getUid(param);
                    String uid = JzbDataType.getString(send.get("uid"));
                    if (JzbTools.isEmpty(uid)) {
                        result = Response.getResponseError();
                    } else {
                        //创建伙伴单位
                        param.put("userinfo", userInfo);
                        param.put("manager", uid);
                        param.put("managername", param.get("managername"));
                        //伙伴单位默认初级认证
                        param.put("authid", "8");
                        param.put("type", "1");
                        Response comRes = companyService.addCompany(param);
                        String cid = "";
                        if (JzbDataType.isMap(comRes.getResponseEntity())) {
                            Map<String, Object> comMap = (Map<String, Object>) comRes.getResponseEntity();
                            cid = JzbDataType.getString(comMap.get("cid"));
                        }
                        //创建伙伴单位表数据
                        param.put("send", send);
                        param.put("cid", cid);
                        result = companyOrgApi.addCompanyFriend(param);
                        authService.addAdmin(param);
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
                    param.put("authid", "8");
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

    /**
     * 创建公海单位没有负责人id时创建负责人账号并创建公海单位
     *
     * @param param
     * @param token
     * @return com.jzb.base.message.Response
     * @Author: Kuang Bin
     * @DateTime: 2019/10/11
     */
    @RequestMapping(value = "/addCompanyCommon", method = RequestMethod.POST)
    @CrossOrigin
    public Response addCompanyCommon(@RequestBody Map<String, Object> param, @RequestHeader(value = "token") String token) {
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
                        // 创建公海单位
                        param.put("userinfo", userInfo);
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
                        result = companyOrgApi.addCompanyCommon(param);
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
                        result = companyOrgApi.addCompanySupplier(param);
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
     * CRM-所有业主-业主列表-修改
     * 点击修改判断是否是系统中的用户如果不是就新建用户
     *
     * @param param
     * @param token
     * @return com.jzb.base.message.Response
     * @Author: Kuang Bin
     * @DateTime: 2019/10/11
     */
    @RequestMapping(value = "/modifyCompanyCommon", method = RequestMethod.POST)
    @CrossOrigin
    public Response modifyCompanyCommon(@RequestBody Map<String, Object> param, @RequestHeader(value = "token") String token) {
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
                        result = companyCommonApi.modifyCompanyCommon(param);
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
