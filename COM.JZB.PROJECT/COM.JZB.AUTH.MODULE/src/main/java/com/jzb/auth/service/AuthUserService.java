package com.jzb.auth.service;

import com.jzb.auth.api.organize.CompanyUserApi;
import com.jzb.auth.api.redis.UserRedisApi;
import com.jzb.auth.config.AuthConfigProperties;
import com.jzb.auth.controller.AuthUserController;
import com.jzb.auth.dao.AuthUserMapper;
import com.jzb.base.data.JzbDataType;
import com.jzb.base.data.code.JzbDataCheck;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.base.entity.auth.UserInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 用户服务类
 *
 * @author Chad
 */
@Service
public class AuthUserService {
    /**
     * 用户数据库操作对象
     */
    @Autowired
    private AuthUserMapper userMapper;

    /**
     * 用户缓存操作对象
     */
    @Autowired
    private UserRedisApi userRedisApi;

    /**
     * 企业操作对象
     */
    @Autowired
    private CompanyUserApi companyUserApi;

    @Autowired
    private AuthUserController authUserController;

    /**
     * 认证修改用户数据
     *
     * @author kuangbin
     */
    public int updateUser(Map<String, Object> param) {
        int count;
        try {
            count = userMapper.updateIdentityList(param);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            count = 0;
        }
        return count;
    }

    /**
     * 高级认证修改用户信息
     *
     * @author kuangbin
     */
    public int updateIdentity(Map<String, Object> param) {
        int count;
        try {
            // 修改用户list表信息
            count = userMapper.updateIdentityList(param);
            if (count == 1) {
                // 修改用户indo表信息
                count = userMapper.insertIdentityInfo(param);
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            count = 0;
        }
        return count;
    }

    /**
     * 用户修改密码数据
     */
    public int updateUserPassword(Map<String, Object> param) {
        int count;
        try {
            long updtime = System.currentTimeMillis();
            param.put("updtime", updtime);
            param.put("newpassword", password().encode(JzbDataType.getString(param.get("newpassword"))));
            count = userMapper.updateUserPassword(param);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            count = 0;
        }
        return count;
    }

    /**
     * 加解密对象
     *
     * @return
     */
    @Bean
    public PasswordEncoder password() {
        return new BCryptPasswordEncoder();
    } // End password


    /**
     * 主页获取用户信息
     *
     * @author kuangbin
     */
    public Map<String, Object> getUserInfo(Map<String, Object> param) {
        return userMapper.queryUserInfo(param);
    }

    /**
     * 主页获取用户角色信息
     *
     * @author kuangbin
     */
    public Map<String, Object> getUserRole(Map<String, Object> param) {
        // 获取用户角色
        return userMapper.queryRoleByUid(param);
    }

    /**
     * 根据联系方式或者ID获取用户信息
     *
     * @author kuangbin
     */
    public Map<String, Object> getUserIdNameByPhone(Map<String, Object> param) {
        return userMapper.queryUserIdNameByPhone(param);
    }

    /**
     * 单位设置-申请/邀请成员
     * 查询被邀请人总数
     *
     * @author kuangbin
     */
    public int getInviteeCount(Map<String, Object> param) {
        int count;
        try {
            count = userMapper.queryInviteeCount(param);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            count = 0;
        }
        return count;
    }

    /**
     * 单位设置-申请/邀请成员
     * 通过名字模糊搜索用户或手机号搜索用户
     *
     * @author kuangbin
     */
    public List<Map<String, Object>> searchInvitee(Map<String, Object> param) {
        List<Map<String, Object>> result;
        int cname = JzbDataType.getInteger(param.get("cname"));
        if (cname == 0) {
            // 设置分页
            param = setPageSize(param);

            // 根据姓名搜索用户
            result = userMapper.searchInvitee(param);
        } else {
            // 根据电话搜索用户
            result = userMapper.queryInvitee(param);
        }
        return result;
    }

    /**
     * 根据用户姓名或手机号查询用户信息总数
     *
     * @param map
     * @return
     */
    public List<Map<String, Object>> searchUserName(Map<String, Object> map) {
        return userMapper.searchUserName(map);
    }

    /**
     * 根据用户姓名或手机号查询用户信息总数
     *
     * @param map
     * @return java.util.Map<java.lang.String, java.lang.Object>
     * @Author: DingSC
     * @DateTime: 2019/9/11 18:06
     */
    public int searchUserNameCount(Map<String, Object> map) {
        return userMapper.searchUserNameCount(map);
    }

    /**
     * 设置分页数
     */
    public Map<String, Object> setPageSize(Map<String, Object> param) {
        int pageno = JzbDataType.getInteger(param.get("pageno"));
        int pagesize = JzbDataType.getInteger(param.get("pagesize"));
        // 判断显示大小是否合理
        pagesize = pagesize <= 0 ? 15 : pagesize;

        // 判断页数是否合理
        pageno = pageno <= 0 ? 1 : pageno;
        param.put("pageno", (pageno - 1) * pagesize);
        param.put("pagesize", pagesize);
        return param;
    }

    /**
     * 我的基本资料
     * 点击头像后修改基本资料
     *
     * @author kuangbin
     */
    public int modifyUserBasicData(Map<String, Object> param) {
        int count;
        try {
            count = userMapper.modifyUserBasicData(param);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            count = 0;
        }
        return count;
    }

    /**
     * 查询用户所有权限
     *
     * @param param
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @Author: DingSC
     */
    public List<Map<String, Object>> queryUserMenuList(Map<String, Object> param) {
        return userMapper.queryUserMenuList(param);
    }

    /**
     * 激活修改用户状态
     *
     * @param param
     * @return int
     * @Author: DingSC
     */
    public int updateUserStatus(Map<String, Object> param) {
        param.put("status", "1");
        param.put("updtime", System.currentTimeMillis());
        return userMapper.updateUserStatus(param);
    }

    /**
     * CRM-销售业主-公海-业主下的人员11
     * 点击业主下的人员中的新增人员按钮进行加入员工
     *
     * @author kuangbin
     */
    public Response addCompanyEmployee(Map<String, Object> param) throws Exception {
        Response result;
        Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
        param.put("adduid", userInfo.get("uid"));
        Object redisRes = userRedisApi.getPhoneUid(JzbDataType.getString(param.get("relphone"))).getResponseEntity();
        param.put("uid", JzbDataType.getString(redisRes));
        if (JzbTools.isEmpty(redisRes)) {
            // 获取用户ID
            String uid = JzbRandom.getRandomCharCap(12);
            param.put("uid", uid);
            // 获取随机密码
            String passwd = "*jzb" + JzbRandom.getRandomNum(3);
            param.put("passwd", JzbDataCheck.Md5(passwd).toLowerCase(Locale.ENGLISH));
            param.put("passwd", password().encode(JzbDataType.getString(param.get("passwd"))));
            param.put("password", passwd);
            // 默认此用户为初级认证
            param.put("status", "1");
            param.put("authid", "1");
            param.put("phone", JzbDataType.getString(param.get("relphone")));
            param.put("addtime", System.currentTimeMillis());
            param.put("ktid", 1);
            // 创建用户
            int count = userMapper.insertCompanyEmployee(param);
            userRedisApi.cachePhoneUid(param);
            if (count >= 1) {
                // 将该用户加入单位资源池中
                result = companyUserApi.addCompanyDept(param);
                authUserController.getUserInfo(param);
            } else {
                result = Response.getResponseError();
            }
        } else {
            // 查询企业部门中是否有该员工
            result = companyUserApi.getDeptCount(param);
            if (JzbDataType.getInteger(result.getResponseEntity()) == 1) {
                result.setResponseEntity("此用户在单位中已存在!");
            } else {
                // 将该用户加入单位资源池中
                result = companyUserApi.addCompanyDept(param);
            }
        }
        return result;
    }

    /**
     * CRM-销售业主-公海-业主下的人员12
     * 点击业主下的人员中的修改人员按钮进行修改员工信息
     *
     * @author kuangbin
     */
    public int modifyCompanyEmployee(Map<String, Object> param) {
        param.put("updtime", System.currentTimeMillis());
        return userMapper.updateCompanyEmployee(param);
    }
} // End class AuthUserService
