package com.jzb.org.service;

import com.alibaba.fastjson.JSON;
import com.jzb.base.data.JzbDataType;
import com.jzb.base.data.code.JzbDataCheck;
import com.jzb.base.entity.auth.CompanyInfo;
import com.jzb.base.entity.organize.DeptUser;
import com.jzb.base.entity.organize.RoleGroup;
import com.jzb.base.message.Response;
import com.jzb.base.tree.JzbTree;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;

import com.jzb.base.util.SendSysMsgUtil;
import com.jzb.org.api.auth.AuthApi;
import com.jzb.org.api.message.MessageApi;
import com.jzb.org.api.redis.UserRedisServiceApi;
import com.jzb.org.config.OrgConfigProperties;
import com.jzb.org.dao.CompanyMapper;
import com.jzb.org.dao.CompanyUserMapper;
import com.jzb.org.dao.DeptMapper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * 企业服务类
 *
 * @author kuangbin
 */
@Service
public class CompanyService {
    /**
     * 企业数据库操作对象
     */
    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private DeptMapper deptMapper;

    @Autowired
    private AuthApi authApi;

    /**
     * 查询redis缓存企业对象
     */
    @Autowired
    private UserRedisServiceApi userRedisServiceApi;

    /**
     * 发送短信接口
     */
    @Autowired
    private MessageApi messageApi;

    @Autowired
    private OrgConfigProperties config;

    /**
     * 开放平台调用接口修改企业地址,地区信息
     *
     * @param param
     * @return
     * @author kuangbin
     */
    public Map<String, Object> queryComapnyProjct(Map<String, Object> param) {
        return companyMapper.queryCompanyProject(param);
    }


    /**
     * 开放平台调用接口修改企业地址,地区信息
     *
     * @param param
     * @return
     * @author kuangbin
     */
    public Map<String, Object> queryComapny(Map<String, Object> param) {
        return companyMapper.queryComapny(param);
    }

    /**
     * 开放平台调用接口修改企业地址,地区信息
     *
     * @param param
     * @return
     * @author kuangbin
     */
    public int addOrgInfo(Map<String, Object> param) {
        return companyMapper.updateOrgInfo(param);
    }

    /**
     * 入驻开放平台,写入地址信息,地区信息,理论上缓存中有完整地区信息
     *
     * @param param
     * @return
     * @author kuangbin
     */
   /* -------public int addOpenPlatform(Map<String, Object> param) {
        int count;
        try {
            param.put("region", JzbRandom.getRandomCharCap(8));
            // 修改企业list表地区ID
            count = companyMapper.updateCompanyListRegion(param);
            if (count != 0) {
                // 增加地区Info表中信息
                count = companyMapper.insertCityList(param);
                if (count != 0) {
                    count = companyMapper.updateCompanyInfoAddress(param);
                }
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            count = 0;
        }
        return count;
    }*/

    /**
     * 主页获取单位信息
     *
     * @param param
     * @return
     * @author kuangbin
     */
    public Map<String, Object> getEnterpriseData(Map<String, Object> param) {
        // 获取单位信息
        return companyMapper.queryEnterpriseData(param);
    }

    /**
     * 获取当前用户下的所以企业
     *
     * @param param
     * @return
     * @author kuangbin
     */
    public List<Map<String, Object>> getCompanyList(Map<String, Object> param) {
        // 通过用户ID获取企业ID
        return companyMapper.queryCompanyList(param);
    }

    /**
     * 主页获取单位部门信息
     *
     * @param param
     * @return
     * @author kuangbin
     */
    public Map<String, Object> getEnterpriseDept(Map<String, Object> param) {
        Map<String, Object> result;
        result = new HashMap<>();
        // 获取企业下部门数
        int department = companyMapper.queryDepartmentCount(param);

        // 在单位部门基础信息中加入部门数
        result.put("department", department);

        // 获取企业下总人数
        int headcount = companyMapper.queryHeadcountCount(param);

        // 在单位部门基础信息中加入总人数
        result.put("headcount", headcount);

        // 获取未激活人数
        int inactive = companyMapper.queryInactiveCount(param);

        // 在单位部门基础信息中加入未激活人数
        result.put("inactive", inactive);
        return result;
    }

    /**
     * 认证修改企业数据
     *
     * @author kuangbin
     */
    public int companyAttestation(Map<String, Object> param) {
        int count;
        try {
            // 获取当前时间
            long modtime = System.currentTimeMillis();
            param.put("modtime", modtime);
            // 判断是否是政府认证
            if (JzbDataType.getInteger(param.get("authid")) != 64) {
                // 修改企业List表信息,返回修改数
                count = companyMapper.updateCompanyList(param);
                if (count == 1) {
                    // 增加企业Info表信息,返回修改成功数
                    count = companyMapper.insertCompanyInfo(param);
                }
            } else {
                // 政府认证执行新的sql修改Info表
                count = companyMapper.insertCompanyInfo1(param);
                if (count == 1) {
                    // 修改list表认证信息
                    count = companyMapper.updateCompanyList(param);
                }
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            count = 0;
        }
        return count;
    }

    /**
     * 根据单位名称获取相似段位名称
     *
     * @param map
     * @return
     */
    public List getNames(Map<String, Object> map) {
        return companyMapper.searchEnterpriseNames(map);
    }

    /**
     * 创建单位 & 加入单位
     *
     * @param map
     * @return
     */
    public Map addCompany(Map<String, Object> map) {
        Map<String, Object> result = new HashMap(2);
        int type = Integer.parseInt(map.get("type").toString());
        String cname = map.get("cname").toString();
        //type的种类
        Map<String,Object> map1 = companyMapper.getAddCompany(map);
        map1.put("senduid", map1.get("senduid"));
        map1.put("msg", "您有一条加入单位的消息未处理");
        map1.put("code", "JRDW");
        map1.put("topic_name", map1.get("senduid") + "/org/addCompany");
        //发送系统平台消息 并添加存储到数据库
        messageApi.sendShortMsg(SendSysMsgUtil.setMsgArg(map1));

        int found = 1;
        int join = 2;
        if (type == found) {
            //创建单位
            result = found(map);
        }
        if (type == join) {
            // 加入单位
            result = join(map);
        }

        return result;
    }


    /**
     * 创建单位并创建资源池，加入到资源池,赋予默认角色
     *
     * @param map
     * @return java.lang.String
     * @Author: DingSC
     * @DateTime: 2019/9/17 11:04
     */
    private Map<String, Object> found(Map<String, Object> map) {
        String message = "3";
        String cid = "";
        //先查询，判断该单位是否有认证的同名单位
        List<Map<String, Object>> nameList = getNames(map);
        if (nameList.size() > 0) {
            message = "4";
            cid = JzbDataType.getString(nameList.get(0).get("cid"));
        } else {
            long time = System.currentTimeMillis();
            cid = JzbRandom.getRandomCharCap(7);
            map.put("regtime", time);
            map.put("cid", cid);
            if (JzbTools.isEmpty(map.get("manager"))) {
                map.put("manager", map.get("uid"));
            }
            int authid = JzbDataType.getInteger(map.get("authid"));
            if (JzbTools.isEmpty(authid)) {
                authid = 0;
            }
            map.put("authid", authid);
            //创建企业
            companyMapper.insertCompany(map);
            //创建资源池
            Map<String, Object> userInfo = (Map<String, Object>) map.get("userinfo");
            Map<String, Object> param = new HashMap<>(8);
            String cdId = cid + "0000";
            param.put("uid", JzbDataType.getString(userInfo.get("uid")));
            param.put("cid", cid);
            param.put("cdid", cdId);
            param.put("addtime", time);
            param.put("status", "1");
            param.put("pcdid", "00000000000");
            param.put("cname", "资源池");
            deptMapper.insertCompanyDept(param);
            //用户加入到资源池
            param.put("status", "1");
            param.put("time", time);
            String uid = JzbDataType.getString(param.get("uid"));
            String manager = JzbDataType.getString(map.get("manager"));
            if (!uid.equals(manager)) {
                //添加负责人
                //负责人手机号和uid
                param.put("phone", map.get("phone"));
                param.put("uid", manager);
                param.put("cname", map.get("managername"));
                deptMapper.insertDeptUser(param);
                param.put("uid", JzbDataType.getString(userInfo.get("uid")));
            } else {
                param.put("cname", userInfo.get("cname"));
                param.put("phone", userInfo.get("relphone"));
                deptMapper.insertDeptUser(param);
            }
            // 加入系统名称
            param.put("systemname", JzbDataType.getString(map.get("systemname")));
            deptMapper.insertCompanySysConfig(param);
            //将企业信息加入到Rides中
            getEnterpriseDept(param);
        }
        Map<String, Object> result = new HashMap<>(2);
        result.put("message", message);
        result.put("cid", cid);
        return result;
    }

    /**
     * 加入单位
     *
     * @param map
     * @return
     */
    private Map<String, Object> join(Map<String, Object> map) {
        String message = "5";
        String cid = "";
        //先查询，判断该单位是否有认证的同名单位
        List<Map<String, Object>> nameList = getNames(map);
        if (nameList.size() > 0) {
            long time = System.currentTimeMillis();
            cid = nameList.get(0).get("cid").toString();
            String manager = nameList.get(0).get("manager").toString();
            map.put("reqtype", "1");
            map.put("cid", cid);
            map.put("uidsh", manager);
            map.put("reqtime", time);
            map.put("status", 1);
            companyMapper.insertInvite(map);
        } else {
            message = "6";
        }
        Map<String, Object> result = new HashMap<>(2);
        result.put("message", message);
        result.put("cid", cid);
        return result;
    }


    /**
     * 根据企业ID获取当前企业的超级管理员
     *
     * @author kuangbin
     */
    public Map<String, Object> getAdministratorId(Map<String, Object> param) {
        return companyMapper.queryAdministratorId(param);
    }

    /**
     * 转让企业管理员
     *
     * @author kuangbin
     */
    public int modifyAdministrator(Map<String, Object> param) {
        int count;
        try {
            count = companyMapper.updateAdministrator(param);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            count = 0;
        }
        return count;
    }

    /**
     * 查询申请人总数
     *
     * @author kuangbin
     */
    public int getApplyCount(Map<String, Object> param) {
        int count;
        try {
            param.put("status", 1);
            param.put("reqtype", "1");
            count = companyMapper.queryApplyCount(param);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            count = 0;
        }
        return count;
    }

    /**
     * 查询被邀请人总数
     *
     * @author kuangbin
     */
    public int getInviteeCount(Map<String, Object> param) {
        int count;
        try {
            param.put("status", 2);
            param.put("reqtype", "2");
            count = companyMapper.queryApplyCount(param);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            count = 0;
        }
        return count;
    }

    /**
     * 成员邀请,申请
     * 显示所有申请成员
     *
     * @author kuangbin
     */
    public List<Map<String, Object>> getApplyList(Map<String, Object> param) {
        List<Map<String, Object>> result;
        // 设置分页数
        param = setPageSize(param);
        param.put("status", 1);
        param.put("reqtype", "1");
        // 查询所有的被邀请人ID
        result = companyMapper.queryApplyList(param);
        if (!JzbDataType.isEmpty(result)) {
            for (int i = 0; i < result.size(); i++) {
                Map<String, Object> applyMap = result.get(i);
                // 参数中加入uid信息
                param.put("uid", JzbDataType.getString(applyMap.get("resuid")));

                // 获取缓存中的用户信息
                Response userData = userRedisServiceApi.getCacheUserInfo(param);

                // 将查询出的结果加入返回值中
                applyMap.putAll((Map<String, Object>) userData.getResponseEntity());
            }
        }
        return result;
    }

    /**
     * 成员邀请,申请
     * 显示企业下所有邀请成员
     *
     * @author kuangbin
     */
    public List<Map<String, Object>> getInviteeList(Map<String, Object> param) {
        List<Map<String, Object>> result;
        // 设置分页数
        param = setPageSize(param);
        param.put("status", 2);
        param.put("reqtype", "2");
        // 查询所有的被邀请人ID
        result = companyMapper.queryApplyList(param);
        if (!JzbDataType.isEmpty(result)) {
            for (int i = 0; i < result.size(); i++) {
                Map<String, Object> applyMap = result.get(i);
                // 获取对应的用户ID
                String uid = JzbDataType.getString(applyMap.get("resuid"));
                // 参数中加入uid信息
                param.put("uid", uid);

                // 获取缓存中的用户信息
                Response userData = authApi.getUserInfo(param);
                // 将查询出的结果加入返回值中
                applyMap.putAll((Map<String, Object>) userData.getResponseEntity());
            }
        }
        return result;
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


    /**
     * 成员邀请,申请
     * 单个通过申请或者拒绝申请
     *
     * @author kuangbin
     */
    public int passDenyProposer(Map<String, Object> param) {
        int count = 0;
        try {
            // 获取当前时间的毫秒值
            long restime = System.currentTimeMillis();
            param.put("restime", restime);
            // 获取操作方式
            int maybe = JzbDataType.getInteger(param.get("maybe"));
            if (maybe == 1) {
                // 返回通过申请修改成功数
                count = companyMapper.updatePassProposer(param);
                if (count == 1) {

                    // 修改成功后加入企业部门资源池,即默认部门
                    count = companyMapper.insertProposer(param);
                }
            } else if (maybe == 2) {
                // 返回拒接申请修改成功数
                count = companyMapper.updateDenyProposer(param);
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            count = 0;
        }
        return count;
    }

    /**
     * 成员邀请,申请
     * 批量通过成员申请
     *
     * @author kuangbin
     */
    public int modifyBatchProposer(List<Map<String, Object>> list) {
        int count;
        try {
            // 获取当前时间的毫秒值
            long restime = System.currentTimeMillis();
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> map = list.get(i);
                map.put("restime", restime);
            }
            count = companyMapper.updateBatchPass(list);
            if (count > 0) {
                // 查询企业中部门是否存在员工
                List<Map<String, Object>> userList = companyMapper.queryBatchProposer(list);

                // 判断传过来的用户数和数据库查询到的用户数是否相等
                if (userList.size() != list.size()) {
                    for (int i = 0; i < userList.size(); i++) {
                        // 遍历查询到的员工
                        Map<String, Object> userMap = userList.get(i);
                        String uid = JzbDataType.getString(userMap.get("uid"));
                        String cid = JzbDataType.getString(userMap.get("cid"));
                        String cdid = JzbDataType.getString(userMap.get("cdid"));
                        userMap.put("updtime", restime);
                        // 遍历前台传过来的用户
                        for (int k = list.size() - 1; k >= 0; k--) {
                            Map<String, Object> Map = list.get(k);
                            String uidMap = JzbDataType.getString(Map.get("uid"));
                            String cidMap = JzbDataType.getString(Map.get("cid"));
                            String cdidMap = JzbDataType.getString(Map.get("cdid"));
                            // 如果前台没有部门,加入默认部门
                            cdidMap = cdidMap == "" || cdidMap == null ? cidMap + "0000" : cdidMap;
                            // 判断是否在同一部门,同一用户,同一企业
                            if (uidMap.equals(uid) && cidMap.equals(cid) && cdidMap.equals(cdid)) {
                                // 删除当前已经存在的用户
                                list.remove(k);
                            }
                        }
                    }
                    if (userList.size() != 0 && userList != null) {
                        // 批量修改已经存在的用户状态为1
                        companyMapper.updateBatchProposer(userList);
                    }
                    if (list.size() != 0 && list != null) {
                        // 批量加入企业部门
                        count = companyMapper.insertBatchProposer(list);
                    }
                } else {
                    // 相等则直接进行批量更新操作
                    count = companyMapper.updateBatchProposer(userList);
                }
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            count = 0;
        }
        return count;
    }

    /**
     * 成员邀请,申请
     * 邀请用户加入企业
     *
     * @author kuangbin
     */
    public int addInvitee(Map<String, Object> param) {
        int count = 0;
        try {
            // 获取当前时间的毫秒值
            long reqtime = System.currentTimeMillis();
            int maybe = JzbDataType.getInteger(param.get("maybe"));
            if (maybe == 1) {
                param.put("reqtime", reqtime);
                // 返回是否添加成功成功数
                count = companyMapper.insertInvitee(param);
                if (count == 1) {
                    // 查询企业中有没有该员工
                    count = companyMapper.queryProposerCount(param);
                    if (count == 0) {
                        // 在部门表中加入加入时间
                        param.put("restime", reqtime);

                        // 加入当前操作人ID
                        param.put("ouid", JzbDataType.getString(param.get("uid")));

                        // 将uid从当前用户ID改为当前受邀请人ID
                        param.put("uid", JzbDataType.getString(param.get("resuid")));

                        // 邀请成功后加入企业部门资源池,即默认部门
                        count = companyMapper.insertProposer(param);
                    } else {
                        // 企业中中有该员工就修改状态为1
                        count = companyMapper.updateProposer1(param);
                    }
                }
            } else if (maybe == 2) {
                param.put("restime", reqtime);
                // 返回是否修改的成功数
                count = companyMapper.updateInvitee(param);
                if (count == 1) {
                    param.put("updtime", reqtime);
                    // 取消成功后取消加入企业部门资源池
                    count = companyMapper.updateProposer(param);
                }
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            count = 0;
        }
        return count;
    }

    /**
     * 我的单位基本资料
     * 主页中单位设置基本资料进行保存
     *
     * @author kuangbin
     */
    public int modifyCompanyBasicData(Map<String, Object> param) {
        int count;
        try {
            // 获取当前时间
            long updtime = System.currentTimeMillis();
            param.put("modtime", updtime);
            // 修改企业list表中信息
            count = companyMapper.updateCompanyBasicList(param);
            if (count != 0) {
                String address = JzbDataType.getString(param.get("address"));
                String curl = JzbDataType.getString(param.get("curl"));
                // 存在地址或者网址才进行企业Info表修改
                if (!JzbDataType.isEmpty(address) || !JzbDataType.isEmpty(curl)) {
                    // 修改企业Info表数据
                    param.put("updtime", updtime);
                    count = companyMapper.updateCompanyBasicInfo(param);
                    // 修改系统名称
                    companyMapper.updateCompanySysconfig(param);
                }
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            count = 0;
        }
        return count;
    }

    /**
     * 根据模糊姓名获取企业下所有同字员工姓名
     *
     * @author kuangbin
     */
    public List<Map<String, Object>> searchUserName(Map<String, Object> param) {
        return companyMapper.searchUserName(param);
    }

    /**
     * 根据模糊姓名获取企业下所有同字员工姓名的总数
     *
     * @author kuangbin
     */
    public int getUserNameCount(Map<String, Object> param) {
        int count;
        try {
            param = setPageSize(param);
            count = companyMapper.queryUserNameCount(param);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            count = 0;
        }
        return count;
    }

    /**
     * 发送短信提醒
     *
     * @param map 包含groupid模板id和手机号
     * @return
     */
    public Response sendRemind(Map<String, Object> map) {
        Response rul;
        // 加入必填参数
        map.put("msgtag", "jzb001");
        map.put("senduid", "jzb001");
        // 将日期格式化为年月日,时分秒
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sim.format(new Date());
        Map<String, Object> codeMap = new HashMap<>();
        codeMap.put("date", date);
        // 创建单位成功判断是否有密码需要发送给用户
        codeMap.put("username", JzbDataType.getString(map.get("username")));
        if (!JzbDataType.isEmpty(JzbDataType.getString(map.get("password")))) {
            codeMap.put("password", JzbDataType.getString(map.get("password")));
        }
        codeMap.put("companyname", JzbDataType.getString(map.get("companyname")));
        Map<String, Object> smsMap = new HashMap<>(1);
        smsMap.put("sms", codeMap);
        map.put("sendpara", JSON.toJSONString(smsMap));
        //短信发送参数填写
        map.put("usertype", "1");
        map.put("title", "计支云");
        List<Map<String, Object>> receiverList = new ArrayList<>();
        Map<String, Object> receiverMap = new HashMap<>();
        Map<String, Object> mapReceiver = new HashMap<>();
        if (!JzbDataType.isEmpty(JzbDataType.getString(map.get("relphone")))) {
            receiverMap.put("photo", JzbDataType.getString(map.get("relphone")));
        } else {
            receiverMap.put("photo", JzbDataType.getString(map.get("phone")));
        }
        receiverList.add(receiverMap);
        mapReceiver.put("sms", receiverList);
        try {
            //加密内容
            String appId = "SADJHJ1FHAUS45FAJ455";
            String secret = "ABSUY0FASD4AA";
            String groupId = JzbDataType.getString(map.get("groupid"));
            String title = JzbDataType.getString(map.get("title"));
            String userType = JzbDataType.getString(map.get("usertype"));
            String receiver = JzbDataType.getString(mapReceiver);
            String checkCode = "FAHJKSFHJK400800FHAJK";
            String md5 = JzbDataCheck.Md5(appId + secret + groupId + title + userType + receiver + checkCode);
            map.put("checkcode", md5);
            map.put("appid", appId);
            map.put("secret", secret);
            map.put("receiver", receiver);
            //发送短信
            rul = messageApi.sendShortMsg(map);
        } catch (Exception e) {
            rul = Response.getResponseError();
            JzbTools.logError(e);
            e.printStackTrace();
        }
        return rul;
    }

    /**
     * 根据用户id获取该用户加入的单位和创建的单位名称
     *
     * @param param
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @Author: DingSC
     * @DateTime: 2019/9/16 9:46
     */
    public List<Map<String, Object>> queryCompanyByUid(Map<String, Object> param) {
        List<Map<String, Object>> list = companyMapper.queryCompanyByUid(param);
        int size = list.size();
        List<Map<String, Object>> result = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            Map<String, Object> map = list.get(i);
            map.put("joname", JzbTree.toStringArray(JzbDataType.getString(map.get("joname"))));
            map.put("regname", JzbTree.toStringArray(JzbDataType.getString(map.get("regname"))));
            result.add(map);
        }
        return result;
    }

    /**
     * 保存伙伴单位信息
     *
     * @param param
     * @return int
     * @Author: DingSC
     * @DateTime: 2019/9/20 17:52
     */
    public int addCompanyFriend(Map<String, Object> param) {
        if (JzbDataType.isMap(param.get("send"))) {
            Map<String, Object> send = (Map<String, Object>) param.get("send");
            if (!JzbTools.isEmpty(send.get("relphone"))) {
                //给负责人发送短信
                send.put("groupid", config.getAddCompany());
                sendRemind(send);
            }
        }
        return companyMapper.insertCompanyFriend(param);
    }

    /**
     * 判断是否已申请
     *
     * @param param
     * @return int
     * @Author: DingSC
     * @DateTime: 2019/9/24 16:51
     */
    public int queryCompanyFriend(Map<String, Object> param) {
        return companyMapper.queryCompanyFriend(param);
    }

    /**
     * 修改伙伴单位状态
     *
     * @param param
     * @return int
     * @Author: DingSC
     * @DateTime: 2019/9/23 16:48
     */
    public int updateCompanyFriend(Map<String, Object> param) {
        return companyMapper.updateCompanyFriend(param);
    }

    /**
     * 管理员创建公海单位
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: Kuang Bin
     * @DateTime: 2019/9/20 18:00
     */
    public int addCompanyCommon(Map<String, Object> param) {
        if (JzbDataType.isMap(param.get("send"))) {
            Map<String, Object> send = (Map<String, Object>) param.get("send");
            if (!JzbTools.isEmpty(send.get("relphone"))) {
                //给负责人发送短信
                send.put("groupid", config.getAddCompany());
                send.put("msgtag", "addCommon1013");
                send.put("senduid", "addCommon1013");
                sendRemind(send);
            }
        } else {
            //给负责人发送短信
            param.put("groupid", config.getAddCompany());
            param.put("msgtag", "addCommon1013");
            param.put("senduid", "addCommon1013");
            sendRemind(param);
        }
        // 新公海单位表
        companyMapper.insertCommonCompany(param);
        // 旧公海单位表
        return companyMapper.insertCompanyCommon(param);
    }

    public int queryCnameIsNot(Map<String, Object> param){
        return companyMapper.queryCnameIsNot(param);
    }

    /**
     * CRM-销售业主-公海-供应商5
     * 点击新建供应商建立单位下供应商
     *
     * @Author: Kuang Bin
     */
    public int addCompanySupplier(Map<String, Object> param) {
        if (JzbDataType.isMap(param.get("send"))) {
            Map<String, Object> send = (Map<String, Object>) param.get("send");
            if (!JzbTools.isEmpty(send.get("relphone"))) {
                //给负责人发送短信
                send.put("groupid", config.getAddCompany());
                send.put("senduid", "addCommon1013");
                send.put("msgtag", "addCommon1013");
                sendRemind(send);
            }
        }
        int count = companyMapper.insertCompanySupplier(param);
        if (count == 1) {
            count = companyMapper.insertCompanyCommon(param);
        }
        return count;
    }

    public int delCompany(Map<String, Object> param) {
        return companyMapper.delCompany(param);
    }

    // 修改公海单位
    public int updCompanyCommon(Map<String, Object> param) {
        return companyMapper.updCompanyCommon(param);

    }

    public int addCompanyToCommon(Map<String, Object> param) {
        return companyMapper.addCompanyToCommon(param);
    }

    public List<Map<String, Object>> queryUserByCid(Map<String, Object> param) {
        return companyMapper.queryUserByCid(param);
    }
}
