package com.jzb.org.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.JzbReturnCode;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbIdEntification;
import com.jzb.base.util.JzbTools;
import com.jzb.org.api.base.RegionBaseApi;
import com.jzb.org.api.redis.OrgRedisServiceApi;
import com.jzb.org.api.redis.UserRedisServiceApi;
import com.jzb.org.config.OrgConfigProperties;
import com.jzb.org.service.CompanyService;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.SendResult;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 业务控制层
 *
 * @author kuangbin
 * @date 2019年7月20日
 */
@RestController
@RequestMapping("/org")
public class CompanyController {
    @Autowired
    private CompanyService companyService;

    @Autowired
    private UserRedisServiceApi userRedisServiceApi;

    /**
     * 查询redis缓存企业对象
     */
    @Autowired
    private OrgRedisServiceApi orgRedisServiceApi;

    @Autowired
    RegionBaseApi regionBaseApi;

    @Autowired
    OrgConfigProperties orgConfigProperties;

    /**
     * 开放平台调用接口获取企业地址,地区信息
     *
     * @author kuangbin
     */
    @RequestMapping("/addOrgInfo")
    @CrossOrigin
    public Response addOrgInfo(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("uid", JzbDataType.getString(userInfo.get("uid")));
            int count = companyService.addOrgInfo(param);
            if (count == 1) {
                result = Response.getResponseSuccess(userInfo);
                // 判断缓存中是否存在CID的key值
                comHasCompanyKey(param);
            } else {
                result = Response.getResponseError();
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 获取当前用户下的所有企业
     *
     * @author kuangbin
     */
    @RequestMapping(value = "/getCompanyList", method = RequestMethod.POST)
    @CrossOrigin
    public Response getCompanyList(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 获取用户资料
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("uid", JzbDataType.getString(userInfo.get("uid")));
            // 通过用户ID获取企业ID
            List<Map<String, Object>> cidList = companyService.getCompanyList(param);
            result = Response.getResponseSuccess(userInfo);
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(cidList);
            result.setPageInfo(pageInfo);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }


    /**
     * 根据项目id获取项信息
     *
     * @author hanbin
     */
    @RequestMapping(value = "/getCompanyProjct", method = RequestMethod.POST)
    @CrossOrigin
    public Response getCompanyProjct(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            Map<String, Object> cidList = companyService.queryComapnyProjct(param);
            result = Response.getResponseSuccess((Map) param.get("userinfo"));
            result.setResponseEntity(cidList);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 根据单位id获取单位信息
     *
     * @author hanbin
     */
    @RequestMapping(value = "/getCompany", method = RequestMethod.POST)
    @CrossOrigin
    public Response getCompany(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            Map<String, Object> cidList = companyService.queryComapny(param);
            result = Response.getResponseSuccess((Map) param.get("userinfo"));
            result.setResponseEntity(cidList);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }


    /**
     * 主页获取单位信息
     *
     * @author kuangbin
     */
    @RequestMapping(value = "/getEnterpriseData", method = RequestMethod.POST)
    @CrossOrigin
    public Response getEnterpriseData(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 获取用户资料
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("uid", JzbDataType.getString(userInfo.get("uid")));
            // 从缓存中获取单位信息
            result = orgRedisServiceApi.getIdCompanyData(param);
            // 获取状态码
            int code = result.getServerResult().getResultCode();
            if (code == JzbReturnCode.HTTP_404) {
                // 缓存中没有就从数据库中查询
                Map<String, Object> record = companyService.getEnterpriseData(param);
                Response region = regionBaseApi.getRegionInfo(record);
                record.put("region", region.getResponseEntity());
                if (!JzbTools.isEmpty(record)) {
                    // 将查询出来的数据加入缓存
                    orgRedisServiceApi.cacheIdCompanyData(record);
                }
                result = Response.getResponseSuccess(userInfo);
                result.setResponseEntity(record);
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 主页获取单位部门信息
     *
     * @author kuangbin
     */
    @RequestMapping(value = "/getEnterpriseDept", method = RequestMethod.POST)
    @CrossOrigin
    public Response getEnterpriseDept(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 调用service层查询方法
            Map<String, Object> record = companyService.getEnterpriseDept(param);

            // 获取用户资料
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            result = Response.getResponseSuccess(userInfo);
            result.setResponseEntity(record);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 根据企业名称获取企业信息
     * 或根据营业执照获取企业信息
     *
     * @author kuangbin
     */
    @RequestMapping(value = "/getCompanyData", method = RequestMethod.POST)
    @CrossOrigin
    public Response getCompanyData(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String responseBody = null;
            String name = JzbDataType.getString(param.get("name"));
            if (!JzbTools.isEmpty(name)) {
                // 调用方法通过企业名称获取企业结果信息字符串
                responseBody = JzbIdEntification.getCompanyData(name);
            } else {
                // 从参数中获取图片地址
                String license = JzbDataType.getString(param.get("license"));
                if (!JzbTools.isEmpty(license)) {
                    // 将图片转换为base64编码
                    String imageBase64 = JzbIdEntification.getImageStr(JzbDataType.getString(param.get("license")));

                    // 调用接口,实现营业执照信息查询
                    responseBody = JzbIdEntification.getLicenseData(imageBase64);
                }
            }
            if (JzbTools.isEmpty(responseBody)) {
                result = Response.getResponseError();
            } else {
                // 将字符串转化为json格式返回给前台
                JSONObject jsonObject = JSONObject.fromObject(responseBody);
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                result = Response.getResponseSuccess(userInfo);
                result.setResponseEntity(jsonObject);
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 根据身份证照获取身份证信息
     *
     * @author kuangbin
     */
    @RequestMapping(value = "/getIdCardData", method = RequestMethod.POST)
    @CrossOrigin
    public Response getIdCardData(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 获取身份证图片url
            String idCard = JzbDataType.getString(param.get("idcard"));

            // 将图片转换为base64编码
            String imageBase64 = JzbIdEntification.getImageStr(idCard);
            int maybe = JzbDataType.getInteger(param.get("maybe"));
            String responseBody;
            // 操作方式1传入正面,2传入身份证照反面
            if (maybe == 1) {
                // 调用接口,实现身份证正面信息查询
                responseBody = JzbIdEntification.getIdCardData(imageBase64, "front");
            } else {
                // 调用接口,实现身份证反面信息查询
                responseBody = JzbIdEntification.getIdCardData(imageBase64, "back");
            }
            if (JzbTools.isEmpty(responseBody)) {
                result = Response.getResponseError();
                result.setResponseEntity("身份证照片不清晰或图片格式不支持!");
            } else {
                // 将字符串转化为json格式返回给前台
                JSONObject jsonObject = JSONObject.fromObject(responseBody);
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                result = Response.getResponseSuccess(userInfo);
                result.setResponseEntity(jsonObject);
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 更改企业认证信息
     *
     * @author kuangbin
     */
    @RequestMapping(value = "/modifyCompanyAttestation", method = RequestMethod.POST)
    @CrossOrigin
    public Response modifyCompanyAttestation(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 返回修改成功数
            int count = companyService.companyAttestation(param);
            if (count != 0) {
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                result = Response.getResponseSuccess(userInfo);
                // 判断缓存中是否存在CID的key值
                comHasCompanyKey(param);

            } else {
                result = Response.getResponseError();
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    } // End companyAttestation

    /**
     * 根据单位名称获取相似单位名称(注册)
     *
     * @param param
     * @return
     */
    @PostMapping("/getEnterpriseNames")
    @CrossOrigin
    public Response getEnterpriseNames(@RequestBody Map<String, Object> param) {
        Response result;
        try {
                if(param.containsKey("pageno")){
                int pageno  = JzbDataType.getInteger(param.get("pageno"))  <= 1 ? 1 : JzbDataType.getInteger(param.get("pageno"));
                param.put("start", (pageno - 1 )  * 10 );
                param.put("pagesize", 10 * pageno);
            }else{
                param.put("start", 0);
                param.put("pagesize", 10);
            }
            List<Map<String, Object>> list = companyService.getNames(param);
            result = Response.getResponseSuccess();
            result.setResponseEntity(list);
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 根据单位名称获取相似单位名称(中台)
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     * @DateTime: 2019/9/19 14:55
     */
    @PostMapping("/getEnterpriseNamesCon")
    @CrossOrigin
    public Response getEnterpriseNamesCon(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            param.put("start", 0);
            param.put("pagesize", 10);
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            if (userInfo.size() > 0) {
                param.put("uid", userInfo.get("uid"));
            }
            List<Map<String, Object>> list = companyService.getNames(param);
            result = Response.getResponseSuccess(userInfo);
            result.setResponseEntity(list);
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 创建单位 & 加入单位
     *
     * @return
     */
    @PostMapping("/addCompany")
    @CrossOrigin
    public Response addCompany(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String[] str = {"phone", "cname", "type"};
            int not = companyService.queryCnameIsNot(param);
            if(not < 1) {
                if (JzbCheckParam.allNotEmpty(param, str)) {
                    Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                    param.put("uid", userInfo.get("uid"));
                    Map<String, Object> map = companyService.addCompany(param);
                    result = Response.getResponseSuccess(userInfo);
                    result.setResponseEntity(map);
                } else {
                    Map<String, Object> map = new HashMap<>(2);
                    map.put("message", "1");
                    result = Response.getResponseError();
                    result.setResponseEntity(map);
                }
            }else{
                result = Response.getResponseError();
                Map<String , Object>  reMap =  new HashMap<>();
                reMap.put("message","单位已经存在");
                result.setResponseEntity(reMap);
            }
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 根据企业ID获取当前企业的超级管理员
     *
     * @author kuangbin
     */
    @RequestMapping(value = "/getAdministrator", method = RequestMethod.POST)
    @CrossOrigin
    public Response getAdministrator(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 根据企业ID获取当前管理员ID
            Map<String, Object> administratorId = companyService.getAdministratorId(param);

            // 获取管理员ID
            String manager = JzbDataType.getString(administratorId.get("manager"));
            if (!StringUtils.isEmpty(manager)) {
                // 在参数中加入管理员ID
                param.put("uid", manager);

                // 获取用户信息
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");

                // 根据uid获取缓存中的用户信息
                result = userRedisServiceApi.getCacheUserInfo(param);
                result.setSession(userInfo.containsKey("session") ? userInfo.get("session").toString() : "");
                result.setToken(userInfo.containsKey("token") ? userInfo.get("token").toString() : "");
            } else {
                result = Response.getResponseError();
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 根据模糊姓名获取企业下所有同字员工姓名
     *
     * @author kuangbin
     */
    @RequestMapping(value = "/searchUserName", method = RequestMethod.POST)
    @CrossOrigin
    public Response searchUserName(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 获取前台传过来的总数
            int count = JzbDataType.getInteger(param.get("count"));
            count = count < 0 ? 0 : count;
            if (count == 0) {
                // 获取员工总数
                count = companyService.getUserNameCount(param);
            }
            List<Map<String, Object>> userName = companyService.searchUserName(param);
            for (int i = 0; i < userName.size(); i++) {
                Map<String, Object> userMap = userName.get(i);
                // 根据ID从缓存中获取用户信息
                Response responseUser = userRedisServiceApi.getCacheUserInfo(userMap);
                Object objUser = responseUser.getResponseEntity();
                if (JzbDataType.isMap(objUser)) {
                    Map<String, Object> objMap = (Map<String, Object>) objUser;
                    userMap.putAll(objMap);
                }
            }
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            result = Response.getResponseSuccess(userInfo);
            // 分页对象
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(userName);
            // 设置总数
            pageInfo.setTotal(count > 0 ? count : userName.size());
            result.setPageInfo(pageInfo);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 转让企业管理员
     *
     * @author kuangbin
     */
    @RequestMapping(value = "/modifyAdministrator", method = RequestMethod.POST)
    @CrossOrigin
    public Response modifyAdministrator(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 返回修改所影响的行数
            int count = companyService.modifyAdministrator(param);
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            if (count == 1) {
                result = Response.getResponseSuccess(userInfo);
                // 判断缓存中是否存在CID的key值
                comHasCompanyKey(param);
            } else {
                result = Response.getResponseError();
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 成员邀请,申请
     * 显示成员申请人
     *
     * @author kuangbin
     */
    @RequestMapping(value = "/getApplyList", method = RequestMethod.POST)
    @CrossOrigin
    public Response getApplyList(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 获取申请人总数
            int count = JzbDataType.getInteger(param.get("count"));
            count = count < 0 ? 0 : count;
            if (count == 0) {
                // 查询申请人总数
                count = companyService.getApplyCount(param);
            }
            // 返回企业下所有的申请成员
            List<Map<String, Object>> applyList = companyService.getApplyList(param);
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            result = Response.getResponseSuccess(userInfo);
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(applyList);
            pageInfo.setTotal(count > 0 ? count : applyList.size());
            result.setPageInfo(pageInfo);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 成员邀请,申请
     * 显示企业下所有邀请成员
     *
     * @author kuangbin
     */
    @RequestMapping(value = "/getInviteeList", method = RequestMethod.POST)
    @CrossOrigin
    public Response getInviteeList(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 获取被邀请人总数
            int count = JzbDataType.getInteger(param.get("count"));
            count = count < 0 ? 0 : count;
            if (count == 0) {
                // 查询被邀请人总数
                count = companyService.getInviteeCount(param);
            }
            // 返回企业下所有的查询被邀请成员
            List<Map<String, Object>> applyList = companyService.getInviteeList(param);
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            result = Response.getResponseSuccess(userInfo);
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(applyList);
            pageInfo.setTotal(count > 0 ? count : applyList.size());
            result.setPageInfo(pageInfo);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 成员邀请,申请
     * 单个通过申请,拒绝申请
     *
     * @author kuangbin
     */
    @RequestMapping(value = "/passDenyProposer", method = RequestMethod.POST)
    @CrossOrigin
    public Response passDenyProposer(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 获取当前用户信息
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("ouid", JzbDataType.getString(userInfo.get("uid")));
            int count = companyService.passDenyProposer(param);
            if (count >0) {
                result = Response.getResponseSuccess(userInfo);
                Response sendResult;
                int maybe = JzbDataType.getInteger(param.get("maybe"));
                // 1发送通过加入单位模板,2拒绝加入单位模板
                if (maybe == 1) {
                    // 发送审核通过信息模板
                    param.put("groupid", orgConfigProperties.getApplicantPass());
                    param.put("msgtag", "Proposer1007");
                    param.put("senduid", "Proposer1007");
                    sendResult = companyService.sendRemind(param);
                } else {
                    // 发送拒绝通过信息模板
                    param.put("groupid", orgConfigProperties.getApplicantRefuse());
                    param.put("msgtag", "Proposer1008");
                    param.put("senduid", "Proposer1008");
                    sendResult = companyService.sendRemind(param);
                }
                result.setResponseEntity(sendResult);
            } else {
                result = Response.getResponseError();
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 成员邀请,申请
     * 批量通过成员申请
     *
     * @author kuangbin
     */
    @RequestMapping(value = "/modifyBatchProposer", method = RequestMethod.POST)
    @CrossOrigin
    public Response modifyBatchProposer(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 获取当前用户信息
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("ouid", JzbDataType.getString(userInfo.get("uid")));
            // 获取网关层前台传入的list
            List<Map<String, Object>> list = (List<Map<String, Object>>) param.get("list");

            // 返回批量修改成员数*/
            int count = companyService.modifyBatchProposer(list);
            if (count > 0) {
                result = Response.getResponseSuccess(userInfo);
                Response sendResult = Response.getResponseError();
                for (int i = 0; i < list.size(); i++) {
                    Map<String, Object> map = list.get(i);
                    // 通过申请模板ID
                    map.put("groupid", orgConfigProperties.getApplicantPass());
                    map.put("msgtag", "Proposer1007");
                    map.put("senduid", "Proposer1007");
                    // 配置短信信息发送短信
                    sendResult = companyService.sendRemind(map);
                }
                result.setResponseEntity(sendResult);
            } else {
                result = Response.getResponseError();
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 成员邀请,申请
     * 邀请用户加入企业
     *
     * @author kuangbin
     */
    @RequestMapping(value = "/addInvitee", method = RequestMethod.POST)
    @CrossOrigin
    public Response addInvitee(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 获取当前用户信息
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("ouid", JzbDataType.getString(userInfo.get("uid")));
            // 返回是否邀请和加入资源池成功
            int count = companyService.addInvitee(param);
            if (count > 0) {
                result = Response.getResponseSuccess();
                Response sendResult;
                int maybe = JzbDataType.getInteger(param.get("maybe"));
                // 1发送邀请加入单位模板,2取消加入单位模板
                if (maybe == 1) {
                    // 发送邀请信息模板
                    param.put("groupid", orgConfigProperties.getInvitationToJoin());
                    param.put("msgtag", "addInvitee1014");
                    param.put("senduid", "addInvitee1014");
                    sendResult = companyService.sendRemind(param);
                } else {
                    // 发送取消信息模板
                    param.put("groupid", orgConfigProperties.getDisinvite());
                    param.put("msgtag", "addInvitee1012");
                    param.put("senduid", "addInvitee1012");
                    sendResult = companyService.sendRemind(param);
                }
                result.setResponseEntity(sendResult);
            } else {
                result = Response.getResponseError();
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 我的单位基本资料
     * 主页中单位设置基本资料的数据显示
     *
     * @author kuangbin
     */
    @RequestMapping(value = "/getCompanyBasicData", method = RequestMethod.POST)
    @CrossOrigin
    public Response getCompanyBasicData(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 从缓存中获取企业信息
            Response responseCompany = orgRedisServiceApi.getIdCompanyData(param);
            Object objCompany = responseCompany.getResponseEntity();
            // 判断缓存中是否有企业信息
            if (JzbDataType.isEmpty(objCompany)) {
                // 缓存中没有企业信息则查询数据库信息
                responseCompany = getEnterpriseData(param);
                objCompany = responseCompany.getResponseEntity();
            }
            // 判断查询到的企业信息是否为MAP对象
            if (JzbDataType.isMap(objCompany)) {
                // 是map对象进行转换
                Map<Object, Object> mapCompany = (Map<Object, Object>) objCompany;
                // 获取管理员信息
                String manager = JzbDataType.getString(mapCompany.get("manager"));
                param.put("uid", manager);
                Response user = userRedisServiceApi.getCacheUserInfo(param);
                mapCompany.put("relperson", user.getResponseEntity());
                // 获取地区id
                String region = JzbDataType.getString(mapCompany.get("region"));
                param.put("region", region);
                if (!JzbDataType.isEmpty(region)) {
                    Response reg = regionBaseApi.getRegionInfo(param);
                    mapCompany.put("region", reg.getResponseEntity());
                }
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                result = Response.getResponseSuccess(userInfo);
                result.setResponseEntity(mapCompany);
            } else {
                result = Response.getResponseError();
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 我的单位基本资料
     * 主页中单位设置基本资料进行保存
     *
     * @author kuangbin
     */
    @RequestMapping(value = "/modifyCompanyBasicData", method = RequestMethod.POST)
    @CrossOrigin
    public Response modifyCompanyBasicData(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 获取当前用户信息
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("ouid", JzbDataType.getString(userInfo.get("uid")));
            int count = companyService.modifyCompanyBasicData(param);
            if (count != 0) {
                result = Response.getResponseSuccess(userInfo);
                // 判断缓存中是否存在CID的key值
                comHasCompanyKey(param);
            } else {
                result = Response.getResponseError();
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 根据用户id获取该用户加入的单位和创建的单位名称
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     * @DateTime: 2019/9/12 17:45
     */
    @RequestMapping(value = "/getCompanyByUid", method = RequestMethod.POST)
    @CrossOrigin
    public Response getCompanyByUid(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String[] str = {"list"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                Object uidS = param.get("list");
                if (JzbDataType.isCollection(uidS)) {
                    // 获取当前用户信息
                    Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                    List<Map<String, Object>> listUid = (List) uidS;
                    param.put("list", listUid);
                    List<Map<String, Object>> uList = companyService.queryCompanyByUid(param);
                    result = Response.getResponseSuccess(userInfo);
                    result.setResponseEntity(uList);
                } else {
                    result = Response.getResponseError();
                }
            } else {
                result = Response.getResponseError();
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 管理员创建伙伴单位
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     * @DateTime: 2019/9/20 18:00
     */
    @RequestMapping(value = "/addCompanyFriend", method = RequestMethod.POST)
    @CrossOrigin
    public Response addCompanyFriend(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String[] str = {"cid"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                String uid = JzbDataType.getString(userInfo.get("uid"));
                long time = System.currentTimeMillis();
                param.put("time", time);
                param.put("restime", time);
                param.put("status", "2");
                param.put("uid", uid);
                param.put("resuid", uid);
                param.put("requid", uid);
                int add = companyService.addCompanyFriend(param);
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
     * 根据cid判断缓存中是否存在key值
     *
     * @author kuangbin
     */
    public boolean comHasCompanyKey(Map<String, Object> param) {
        boolean bl;
        try {
            param.remove("userinfo");
            // 判断缓存中是否存在对应的key
            Response key = orgRedisServiceApi.comHasKey(param);
            Object obj = key.getResponseEntity();
            if (JzbDataType.isBoolean(obj)) {
                bl = (boolean) obj;
                if (bl) {
                    // 修改缓存中已经过时的数据
                    orgRedisServiceApi.updateIdCompanyData(param);
                }
            } else {
                bl = false;
            }
        } catch (Exception e) {
            JzbTools.logError(e);
            bl = false;
        }
        return bl;
    } // End comHasCompanyKey

    /**
     * 申请形象大使
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     * @DateTime: 2019/9/20 19:18
     */
    @RequestMapping(value = "/addConCompanyFriend", method = RequestMethod.POST)
    @CrossOrigin
    public Response addConCompanyFriend(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String[] str = {"cid"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                // 根据企业ID获取当前管理员ID
                Map<String, Object> administratorId = companyService.getAdministratorId(param);
                // 获取管理员ID
                String manager = JzbDataType.getString(administratorId.get("manager"));
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                String uid = JzbDataType.getString(userInfo.get("uid"));
                Map<String, Object> code = new HashMap<>(2);
                if (uid.equals(manager)) {
                    int count = companyService.queryCompanyFriend(param);
                    if (count > 0) {
                        //已申请
                        result = Response.getResponseError();
                        code.put("code", "2");
                        result.setResponseEntity(code);
                    } else {
                        long time = System.currentTimeMillis();
                        param.put("time", time);
                        param.put("status", "1");
                        param.put("uid", uid);
                        param.put("requid", uid);
                        int add = companyService.addCompanyFriend(param);
                        result = add > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
                    }
                } else {
                    result = Response.getResponseError();
                    code.put("code", "0");
                    result.setResponseEntity(code);
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
     * 修改伙伴单位状态
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     * @DateTime: 2019/9/23 16:46
     */
    @RequestMapping(value = "/modifyCompanyFriend", method = RequestMethod.POST)
    @CrossOrigin
    public Response modifyCompanyFriend(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String[] str = {"cid", "status"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                String uid = JzbDataType.getString(userInfo.get("uid"));
                long time = System.currentTimeMillis();
                param.put("time", time);
                param.put("resuid", uid);
                param.put("uid", uid);
                int add = companyService.updateCompanyFriend(param);
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
     * 管理员创建公海单位
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: Kuang Bin
     * @DateTime: 2019/9/20 18:00
     */
    @RequestMapping(value = "/addCompanyCommon", method = RequestMethod.POST)
    @CrossOrigin
    public Response addCompanyCommon(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String[] str = {"cid"};

                if (JzbCheckParam.allNotEmpty(param, str)) {
                    Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                    param.put("addtime", System.currentTimeMillis());
                    param.put("status", "1");
                    param.put("uid", userInfo.get("uid"));
                    int add = companyService.addCompanyCommon(param);
                    result = add > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
                    // 返回用户手填公司地址
                    Map<String , Object> paMap =  new HashMap<>();
                    paMap.put("adderss",param.get("adderss"));
                    result.setResponseEntity(paMap);
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
     * CRM-销售业主-公海-供应商5
     * 点击新建供应商建立单位下供应商
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: Kuang Bin
     * @DateTime: 2019/10/11
     */
    @RequestMapping(value = "/addCompanySupplier", method = RequestMethod.POST)
    @CrossOrigin
    public Response addCompanySupplier(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            String uid = JzbDataType.getString(userInfo.get("uid"));
            long time = System.currentTimeMillis();
            param.put("addtime", time);
            param.put("adduid", uid);
            param.put("uid", uid);
            param.put("status", "1");
            int add = companyService.addCompanySupplier(param);
            result = add == 1 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

}
