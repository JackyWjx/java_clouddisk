package com.jzb.org.service;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.data.code.JzbDataCheck;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.org.api.auth.AuthApi;
import com.jzb.org.api.message.MessageApi;
import com.jzb.org.api.redis.UserRedisServiceApi;
import com.jzb.org.config.OrgConfigProperties;
import com.jzb.org.dao.ExportConfirmMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @Description:
 * @Author dell
 * @Version 1.0
 * @Since 1.0
 * @Date: 2019/8/29 20:43
 */
@Service
public class ExportConfirmService {
    @Autowired
    private ExportConfirmMapper export;

    @Autowired
    private CompanyService service;

    @Autowired
    private OrgConfigProperties config;

    @Autowired
    private UserRedisServiceApi userRedisServiceApi;
    @Autowired
    private AuthApi authApi;

    /**
     * 获取导入信息总数
     *
     * @param map
     * @return
     */
    public int queryExportCount(Map<String, Object> map) {
        return export.queryExportCount(map);
    }

    /**
     * 根据企业id和批次id获取导入信息
     *
     * @param map
     * @return
     */
    public List<Map<String, Object>> queryExportList(Map<String, Object> map) {
        return export.queryExportList(map);
    }

    /**
     * 获取导入批次信息
     *
     * @param map
     * @return
     */
    public List<Map<String, Object>> queryExportBatch(Map<String, Object> map) {
        return export.queryExportBatch(map);
    }

    /**
     * 确认批量导入的用户信息
     *
     * @param map
     * @return
     */
    public Map<String, Integer> updateExport(Map<String, Object> map) {
        //校验数据
        //用户导入信息表
        Map<String, Object> userInfo;
        //成员邀请/申请表
        Map<String, Object> inviteUser;
        //部门员工表
        Map<String, Object> deptUser;

        String cid = JzbDataType.getString(map.get("cid"));
        String batchid = JzbDataType.getString(map.get("batchid"));
        String phone = JzbDataType.getString(map.get("phone"));
        if (tophone(phone)) {
            int inviteStatus;
            String ustatus;
            //用户导入信息表
            userInfo = toCdId(map);
            userInfo.put("cid", cid);
            userInfo.put("batchid", batchid);
            userInfo.put("phone", phone);
            userInfo.put("status", "1");
            //用户id
            String resuid = JzbDataType.getString(userRedisServiceApi.getPhoneUid(phone).getResponseEntity());
            //受邀请人
            userInfo.put("resuid", resuid);
            deptUser = new HashMap<>(1);
            if (JzbTools.isEmpty(resuid)) {
                //非用户
                ustatus = "2";
                inviteStatus = 2;
            } else {
                //加入部门
                ustatus = "1";
                inviteStatus = 10;
                deptUser = toDeptMap(userInfo);

            }
            //邀请map获取
            inviteUser = toInviteUser(userInfo);
            inviteUser.put("ustatus", ustatus);
            //受邀请人id
            inviteUser.put("resuid", resuid);
            inviteUser.put("status", inviteStatus);
        } else {
            //用户导入信息表
            userInfo = new HashMap<>(1);
            inviteUser = new HashMap<>(1);
            deptUser = new HashMap<>(1);
        }
        //数据存储到数据库
        Map<String, Integer> result = toAdd(userInfo, inviteUser, deptUser);
        return result;
    }

    /**
     * 校验手机号
     *
     * @param obj
     * @return
     */
    private boolean tophone(String obj) {
        boolean result = true;
        Map reMap = new HashMap(4);
        try {
            reMap.put(4, "^[1][3,4,5,6,7,8,9][0-9]{9}$");
            result = Pattern.matches(reMap.get(4).toString(), obj);
        } catch (Exception e) {
            JzbTools.logError(e);
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 保存数据和修改数据
     *
     * @param userInfo
     * @param inviteUser
     * @param deptUser
     * @return
     */
    private Map<String, Integer> toAdd(Map<String, Object> userInfo, Map<String, Object> inviteUser, Map<String, Object> deptUser) {
        Map<String, Integer> result = new HashMap<>(4);
        int info = 0;
        int invite = 0;
        int dept = 0;
        //用户导入信息修改
        if (userInfo != null && userInfo.size() > 0) {
            info = export.updateExportUserInfo(userInfo);
        }
        //邀请保存
        if (inviteUser != null && inviteUser.size() > 0) {
            invite = export.insertInviteUser(inviteUser);
            //模板
            String template = config.getTemplate();
            String phone = JzbDataType.getString(inviteUser.get("resphone"));
            Map<String, Object> param = new HashMap<>(2);
            param.put("relphone", phone);
            param.put("groupid", template);
            //发送短信
            service.sendRemind(param);
        }
        //用户在指定企业但不在指定部门
        if (deptUser != null && deptUser.size() > 0) {
            dept = export.insertDeptUser(deptUser);
        }
        result.put("info", info);
        result.put("invite", invite);
        result.put("dept", dept);
        return result;
    }

    /**
     * 获取邀请表map
     *
     * @param map
     * @return
     */
    private Map<String, Object> toInviteUser(Map<String, Object> map) {
        Map<String, Object> result = new HashMap<>(8);
        result.put("reqtype", "2");
        result.put("batchid", map.get("batchid"));
        result.put("cid", map.get("cid"));
        result.put("uid", map.get("uid"));
        result.put("reqtime", System.currentTimeMillis());
        result.put("cdid", map.get("cdid"));
        result.put("resphone", map.get("phone"));
        return result;
    }


    /**
     * 获取部门用户的map
     *
     * @param map
     * @return
     */
    private Map<String, Object> toDeptMap(Map<String, Object> map) {
        Map<String, Object> result = new HashMap<>(6);
        int isDept = export.queryDeptUserCount(map);
        if (isDept > 0) {
            //存在,则不添加到部门用户表
        } else {
            result.put("cid", map.get("cid"));
            result.put("cdid", map.get("cdid"));
            result.put("uid", map.get("resuid"));
            result.put("addtime", System.currentTimeMillis());
            result.put("status", "1");
            //用户信息
            Response user = userRedisServiceApi.getCacheUserInfo(map);
            if (JzbDataType.isMap(user.getResponseEntity())) {
                Map<String, Object> userMap = (Map) user.getResponseEntity();
                String cname = JzbDataType.getString(userMap.get("cname"));
                result.put("cname", cname);
            }
        }
        return result;
    }

    /**
     * 获取部门id
     *
     * @param map
     * @return
     */
    private Map<String, Object> toCdId(Map<String, Object> map) {
        Map<String, Object> result = new HashMap<>(4);
        String cid = JzbDataType.getString(map.get("cid"));
        String cdId;
        String dept;
        Map<String, Object> deptMap = export.queryDeptMap(map);
        if (deptMap == null) {
            dept = "资源池";
            cdId = cid + "0000";
        } else {
            dept = JzbDataType.getString(deptMap.get("cname"));
            cdId = JzbDataType.getString(deptMap.get("cdid"));
        }
        result.put("cdid", cdId);
        result.put("dept", dept);
        result.put("id", map.get("id"));
        result.put("uid", map.get("uid"));
        return result;
    }

    public Response addUserAndSend(Map<String, Object> map) {
        //创建用户
        Response userRes;
        try {
            Map<String, Object> userMap = new HashMap<>(4);
            userMap.put("name", map.get("name"));
            userMap.put("phone", map.get("phone"));
            String pass = "*jzb" + JzbRandom.getRandomNum(3);
            userMap.put("passwd", JzbDataCheck.Md5(pass).toLowerCase(Locale.ENGLISH));
            userMap.put("status", "8");

            userRes = authApi.addRegistration(userMap);
            Map<String, Object> uidMap = (Map<String, Object>) userRes.getResponseEntity();
            //加入邀请
            String newUser = JzbDataType.getString(uidMap.get("uid"));
            if (!JzbTools.isEmpty(newUser)) {
                Map<String, Object> param = new HashMap<>(5);
                String company = "计支宝";
                param.put("relphone", map.get("phone"));
                param.put("groupid", config.getInvite());
                param.put("companyname", company);
                param.put("username", map.get("name"));
                param.put("password", pass);
                //发送短信
                service.sendRemind(param);
            }
        } catch (Exception e) {
            userRes = Response.getResponseError();
            JzbTools.logError(e);

        }
        return userRes;
    }
}
