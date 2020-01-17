package com.jzb.auth.controller;

import com.jzb.auth.api.organize.CompanyListApi;
import com.jzb.auth.service.RoleService;
import com.jzb.base.data.JzbDataType;
import com.jzb.base.entity.auth.UserInfo;
import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author dell
 * @Version 1.0
 * @Since 1.0
 * @Date: 2019/8/12 15:43
 */
@RestController
@RequestMapping("/auth/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private CompanyListApi companyListApi;

    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(RoleController.class);

    /**
     * 查询该用户是否管理员角色组下人czd
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getIsBaseByUid", method = RequestMethod.POST)
    @CrossOrigin
    public Response getIsBaseByUid(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/auth/role/getIsBaseByUid";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            String[] str = {"cid", "uid"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                int count = roleService.queryIsBasePerson(param);
                result = count > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
            } else {
                result = Response.getResponseError();
            }
        } catch (Exception e) {
            flag = false;
            JzbTools.logError(e);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getIsBaseByUid Method", e.toString()));
        }
        if (userInfo != null) {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
                    userInfo.get("msgTag").toString(), "User Login Message"));
        } else {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
        }
        return result;
    }

    /**
     * 保存管理员角色组信息
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/saveRoleBaseGroup", method = RequestMethod.POST)
    @CrossOrigin
    public Response saveRoleBaseGroup(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String[] str = {"cid","uid"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                String cid=JzbDataType.getString(param.get("cid"));
                if(JzbTools.isEmpty(cid)){
                    long addTime = System.currentTimeMillis();
                    String newCrgId = JzbDataType.getString(param.get("cid")) + 9999;
                    param.put("addTime", addTime);
                    param.put("crgId",newCrgId);
                    int count = roleService.insertRoleBaseGroup(param);
                    result = count > 0 ? Response.getResponseSuccess() : Response.getResponseError();
                }else {
                    result=Response.getResponseError();
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
     * 根据企业id获取角色组信息
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getRoleGroup", method = RequestMethod.POST)
    @CrossOrigin
    public Response getRoleGroup(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String[] str = {"cid"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                //求总数的service方法名称
                String countS = "getRoleCount";
                //根据企业id获取角色组信息
                String listS = "getRoleList";
                result = info(roleService, param, countS, listS);
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
     * 根据角色组id获取关联数据信息
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getRoleRelation", method = RequestMethod.POST)
    @CrossOrigin
    public Response getRoleRelation(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String[] str = {"crgid"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                //获取角色组角色关联表总数
                String countS = "getRelationCount";
                //获取角色组角色关联表信息
                String listS = "getRelationList";
                result = info(roleService, param, countS, listS);
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
     * 保存角色组信息
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/saveRoleGroup", method = RequestMethod.POST)
    @CrossOrigin
    public Response saveRoleGroup(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String[] str = {"cid", "cname"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                param.put("uid", userInfo.get("uid"));
                Map<String, Object> map = roleService.insertRoleGroup(param);
                result = Response.getResponseSuccess(userInfo);
                result.setResponseEntity(map);
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
     * 保存角色组菜单表
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/addRoleMenuAuth", method = RequestMethod.POST)
    @CrossOrigin
    public Response addRoleMenuAuth(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String[] str = {"pid", "crgid", "cid", "ptype"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                param.put("uid", userInfo.get("uid"));
                Map<String, Object> code = roleService.saveRoleMenuAuth(param);
                result = JzbDataType.getInteger(code.get("code")) == 1 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
                result.setResponseEntity(code);
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
     * 保存CRM菜单权限
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     * @DateTime: 2019/9/27 16:47
     */
    @RequestMapping(value = "/addCRMRoleMenu", method = RequestMethod.POST)
    @CrossOrigin
    public Response addCRMRoleMenu(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String[] str = {"pid", "list", "ptype"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                if (JzbDataType.isCollection(param.get("list"))) {
                    Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                    param.put("uid", userInfo.get("uid"));
                    List<Map<String, Object>> cidList = (List<Map<String, Object>>) param.get("list");
                    int cidSize = cidList.size();
                    Map<String, Object> cidRes = new HashMap<>(cidSize);
                    for (int i = 0; i < cidSize; i++) {
                        String cid = JzbDataType.getString(cidList.get(i).get("cid"));
                        try {
                            String crgId = cid + "0000";
                            param.put("cid", cid);
                            param.put("crgid", crgId);
                            Map<String, Object> code = roleService.saveRoleMenuAuth(param);
                            //判断企业是否保存成功
                            boolean suc = JzbDataType.getInteger(code.get("code")) == 1;
                            cidRes.put(cid, suc);
                        } catch (Exception e) {
                            e.printStackTrace();
                            cidRes.put(cid, false);
                            JzbTools.logError(e);
                        }
                    }
                    result = Response.getResponseSuccess(userInfo);
                    result.setResponseEntity(cidRes);
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
     * 修改角色组产品菜单数据
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     * @DateTime: 2019/9/10 20:32
     */
    @RequestMapping(value = "/updateRoleMenuAuth", method = RequestMethod.POST)
    @CrossOrigin
    public Response updateRoleMenuAuth(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("uid", userInfo.get("uid"));
            roleService.updateRoleMenuAndCon(param);
            result = Response.getResponseSuccess(userInfo);
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 逻辑删除角色组信息
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/removeRoleGroup", method = RequestMethod.POST)
    @CrossOrigin
    public Response removeRoleGroup(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String[] str = {"crgid"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                param.put("uid", userInfo.get("uid"));
                String crgid = roleService.queryIsBase(param);
                if (JzbTools.isEmpty(crgid)) {
                    roleService.updateRoleGroup(param);
                    result = Response.getResponseSuccess(userInfo);
                } else {
                    result = Response.getResponseError();
                    result.setResponseEntity("该角色组是管理员角色组，不能删除！");
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
     * 修改角色组信息getProductMenuList
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/modifyRoleGroup", method = RequestMethod.POST)
    @CrossOrigin
    public Response modifyRoleGroup(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String[] str = {"crgid", "cname"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                param.put("uid", userInfo.get("uid"));
                int del = roleService.updateRoleGroup1(param);
                result = del > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
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
     * 获取已经勾选的角色组菜单表信息
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getRoleMenuAuth", method = RequestMethod.POST)
    @CrossOrigin
    public Response getRoleMenuAuth(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String[] str = {"crgid"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                result = Response.getResponseSuccess(userInfo);
                result.setResponseEntity(roleService.queryRoleMenuAuth(param));
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
     * 保存角色组关联信息
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/addRoleRelation", method = RequestMethod.POST)
    @CrossOrigin
    public Response addRoleRelation(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String[] str = {"crgid", "cid", "rrid", "rrtype"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                param.put("uid", userInfo.get("uid"));
                int add = roleService.addRoleRelation(param);
                result = add > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
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
     * 删除角色组关联信息
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/removeRoleRelation", method = RequestMethod.POST)
    @CrossOrigin
    public Response removeRoleRelation(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String[] str = {"crgid", "cid", "rrid", "rrtype"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                Response response = companyListApi.getManagerByCid(param);
                String manager = JzbDataType.getString(response.getResponseEntity());
                String uid = JzbDataType.getString(param.get("rrid"));
                String crgid = roleService.queryIsBase(param);
                if (uid.equals(manager) && !JzbTools.isEmpty(crgid)) {
                    result = Response.getResponseError();
                } else {
                    Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                    param.put("uid", userInfo.get("uid"));
                    int add = roleService.updateRoleRelation(param);
                    result = add > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
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
     * 添加企业角色表信息（角色表）
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/addCompanyRole", method = RequestMethod.POST)
    @CrossOrigin
    public Response addCompanyRole(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String[] str = {"cid", "cname"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                param.put("uid", userInfo.get("uid"));
                Map<String, Object> res = roleService.addCompanyRole(param);
                String key = "add";
                //获取保存条数信息
                int add = JzbDataType.getInteger(res.get(key));
                boolean suc = add > 0;
                if (suc) {
                    result = Response.getResponseSuccess(userInfo);
                    res.remove(key);
                    result.setResponseEntity(res);
                } else {
                    result = Response.getResponseError();
                    res.remove(key);
                    result.setResponseEntity(res);
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
     * 修改角色信息
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/modifyCompanyRole", method = RequestMethod.POST)
    @CrossOrigin
    public Response modifyCompanyRole(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String[] str = {"crid", "cid"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                param.put("uid", userInfo.get("uid"));
                Map<String, Object> rMap = roleService.updateCompanyRole(param);
                result = JzbDataType.getInteger(rMap.get("update")) > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
                result.setResponseEntity(rMap);
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
     * 删除角色
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/removeCompanyRole", method = RequestMethod.POST)
    @CrossOrigin
    public Response removeCompanyRole(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String[] str = {"crid", "cid"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                param.put("uid", userInfo.get("uid"));
                roleService.deleteCompanyRole(param);
                result = Response.getResponseSuccess(userInfo);
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
     * 角色分页查询
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getCompanyRole", method = RequestMethod.POST)
    @CrossOrigin
    public Response getCompanyRole(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String[] str = {"cid"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                //根据企业id获取角色表总数
                String countS = "queryCompanyRoleCount";
                //根据企业id获取角色表信息
                String listS = "queryCompanyRoleList";
                result = info(roleService, param, countS, listS);
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
     * 根据用户部门id和用户id获取用户角色组信息
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getUserDeptGroupList", method = RequestMethod.POST)
    @CrossOrigin
    public Response getUserDeptGroupList(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String[] str = {"cid", "list"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                boolean isList = JzbDataType.isCollection(param.get("list"));
                if (isList) {
                    List list = (List) param.get("list");
                    param.put("list", list);
                    List userDgList = roleService.getUserDeptGroup(param);
                    result = Response.getResponseSuccess(userInfo);
                    result.setResponseEntity(userDgList);
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
     * 根据企业id，角色获取用户角色表信息
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getUserRole", method = RequestMethod.POST)
    @CrossOrigin
    public Response getUserRole(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String[] str = {"cid", "crid"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                //根据企业id，角色获取用户角色表总数
                String countS = "queryUserRoleCount";
                //根据企业id，角色获取用户角色表信息
                String listS = "queryUserRoleList";
                result = info(roleService, param, countS, listS);
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
     * 批量添加用户角色
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/addUserRole", method = RequestMethod.POST)
    @CrossOrigin
    public Response addUserRole(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String[] str = {"list", "crid", "cid"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                param.put("uid", userInfo.get("uid"));
                int add = roleService.insertUserRole(param);
                result = Response.getResponseSuccess(userInfo);
                result.setResponseEntity(add);
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
     * 删除用户角色表（逻辑）
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     * @DateTime: 2019/9/3 11:07
     */
    @RequestMapping(value = "/removeUserRole", method = RequestMethod.POST)
    @CrossOrigin
    public Response removeUserRole(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String[] str = {"list", "crid", "cid"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                roleService.updateUserRole(param);
                result = Response.getResponseSuccess(userInfo);
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
     * 分页调用方法
     *
     * @param service 调用的service
     * @param param   传入的参数
     * @param countSF 求总数的service方法
     * @param listSF  分页查询的service方法
     * @return
     * @throws NoSuchMethodException
     */
    private Response info(Service service, Map<String, Object> param, String countSF, String listSF) {
        Response result;
        try {
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("uid", userInfo.get("uid"));
            int rows = JzbDataType.getInteger(param.get("pagesize"));
            int page = JzbDataType.getInteger(param.get("pageno"));
            if (page > 0 && rows > 0) {
                Class<?> serviceClass = service.getClass();
                /* sql中分页要为int */
                param.put("pagesize", rows);
                param.put("start", rows * (page - 1));
                List<Map<String, Object>> roleList = (List) serviceClass.getDeclaredMethod(listSF, Map.class).invoke(service, param);
                result = Response.getResponseSuccess(userInfo);
                PageInfo pageInfo = new PageInfo();
                pageInfo.setList(roleList);
                int count = JzbDataType.getInteger(param.get("count"));
                //为0就传总数
                if (count == 0) {
                    int total = JzbDataType.getInteger(serviceClass.getDeclaredMethod(countSF, Map.class).invoke(service, param));
                    pageInfo.setTotal(total > 0 ? total : roleList.size());
                }
                result.setPageInfo(pageInfo);
            } else {
                result = Response.getResponseError();
            }
        } catch (IllegalAccessException e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        } catch (InvocationTargetException e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        } catch (NoSuchMethodException e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 查询计支宝企业内 文档管理 角色
     * 用户 给 体系建设 内超级管理员 权限
     * 查询当前用户 是否有超级权限
     *
     * @param param
     * @Author: lifeigetGroupUser
     * @DateTime: 2019/12/19 14:07
     */
    @RequestMapping(value = "/getDocMsgPower", method = RequestMethod.POST)
    @CrossOrigin
    public Response getDocMsgPower(@RequestBody Map<String, Object> param) {
        Response result = null;
        try {
            String[] str = {"uid"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                param.put("uid", userInfo.get("uid"));
                int add = roleService.getDocMsgPower(param);
                result = Response.getResponseSuccess(userInfo);
                result.setResponseEntity(add);
            }
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

}
