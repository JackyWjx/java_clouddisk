package com.jzb.auth.controller;

import com.jzb.auth.api.organize.DeptUserApi;
import com.jzb.auth.api.redis.UserRedisApi;
import com.jzb.auth.config.AuthConfigProperties;
import com.jzb.auth.service.AuthUserLoginService;
import com.jzb.auth.service.AuthUserService;
import com.jzb.base.constant.JzbMessageCode;
import com.jzb.base.data.JzbDataType;
import com.jzb.base.data.code.JzbDataCheck;
import com.jzb.base.message.JzbReturnCode;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbIdEntification;
import com.jzb.base.util.JzbTools;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 业务控制层
 *
 * @author Chad
 * @date 2019年7月7日
 */
@RestController
@RequestMapping(value = "/auth")
public class AuthUserController {
    @Autowired
    private AuthUserService userService;

    @Autowired
    private AuthUserLoginService userLoginService;

    @Autowired
    private DeptUserApi deptUserApi;
    /**
     * 用户信息缓存对象
     */
    @Autowired
    private UserRedisApi userRedisApi;

    /**
     * 配置数据
     */
    @Autowired
    private AuthConfigProperties authConfig;

    /**
     * 统一手机号  czd
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/updateAllPhoneByUid", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response updateAllPhoneByUid(@RequestBody Map<String, Object> param) {
        Response response;
        try {
            /**  验证指定参数为空则返回error */
            if (JzbCheckParam.haveEmpty(param, new String[]{"uid", "phone"})) {
                response = Response.getResponseError();
            } else {
                int count = userService.updateUserPhoneNo1(param);
                response = count > 0 ? Response.getResponseSuccess() : Response.getResponseError();
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            response = Response.getResponseError();
        }
        return response;
    }

    /**
     * 模糊查询用户名
     *
     * @author hanbin
     */
    @PostMapping(value = "/getPersionByName")
    @CrossOrigin
    public Response getPersionByName(@RequestBody Map<String, Object> param) {
        Response response;
        try {
            List<Map<String, Object>> list = (List<Map<String, Object>>) param.get("list");
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> umap = new HashMap<>();
                umap.put("person", param.get("person"));
                umap.put("uid", list.get(i).get("uid"));
                if (!userService.queryPersionByName(umap)) {
                    list.remove(i);
                }
            }
            response = Response.getResponseSuccess((Map<String, Object>) param.get("userinfo"));
            response.setResponseEntity(list);
        } catch (Exception e) {
            JzbTools.logError(e);
            response = Response.getResponseError();
        }
        return response;
    }

    /**
     * 主页获取用户信息
     *
     * @author kuangbin
     */
    @PostMapping(value = "/getUserInfo")
    @CrossOrigin
    public Response getUserInfo(@RequestBody Map<String, Object> param) {
        JzbTools.logInfo("=======================>> ", "getUserInfo", param.toString());
        Response response;
        try {
            // 获取用户资料
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            if (JzbDataType.isEmpty(JzbDataType.getString(param.get("uid")))) {
//                param.put("uid", JzbDataType.getString(userInfo.get("uid")));
                param.put("uid", param.get("uid"));
            }
            Map<String, Object> resuMap = userService.getUserInfo(param);
            if (!JzbTools.isEmpty(resuMap)) {
                if (!comHasUserKey(resuMap)) {
                    // 添加增加缓存必要的参数
                    resuMap.put("token", "token");
                    resuMap.put("timeout", "1800000");
                    resuMap.put("phone", JzbDataType.getString(param.get("phone")));
                    userRedisApi.cacheUserInfo(resuMap);
                }
            }
            Response res = userRedisApi.getCacheUserInfo(resuMap);
            if (param.containsKey("userinfo")) {
                response = Response.getResponseSuccess(userInfo);
            } else {
                response = Response.getResponseSuccess();
            }
            response.setResponseEntity(res.getResponseEntity());
        } catch (Exception e) {
            JzbTools.logError(e);
            response = Response.getResponseError();
        }
        return response;
    } //

    /**
     * 主页获取用户角色信息
     *
     * @author kuangbin
     */
    @PostMapping(value = "/getUserRole")
    @CrossOrigin
    public Response getUserRole(@RequestBody Map<String, Object> param) {
        Response response;
        try {
            // 获取用户资料
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("uid", JzbDataType.getString(userInfo.get("uid")));
            Map<String, Object> resuMap = userService.getUserRole(param);
            response = Response.getResponseSuccess(userInfo);
            response.setResponseEntity(resuMap);
        } catch (Exception e) {
            JzbTools.logError(e);
            response = Response.getResponseError();
        }
        return response;
    } //

    /**
     * 根据regid 或者phone 获取用户id
     *
     * @author kuangbin
     */
    @PostMapping(value = "/getUidByPhoneOrRegid")
    @CrossOrigin
    public Response getUidByPhoneOrRegid(@RequestBody Map<String, Object> param) {
        Response response;
        try {
            String s = userService.queryUidByPhoneOrRegid(param);
            response = Response.getResponseSuccess();
            response.setResponseEntity(s);
        } catch (Exception e) {
            JzbTools.logError(e);
            response = Response.getResponseError();
        }
        return response;
    } //

    /**
     * 更改个人认证信息
     *
     * @author kuangbin
     */
    @PostMapping("/userAttestation")
    @CrossOrigin
    public Response userAttestation(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            int count;
            // 获取身份证信息
            String idcard = JzbDataType.getString(param.get("idcard"));
            // 验证身份证是否合格的正则
            String regex =
                    "^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";
            if (idcard.matches(regex)) {
                // 获取认证级别
                int authid = JzbDataType.getInteger(param.get("authid"));
                if (authid == 1) {
                    // 获取当前时间
                    long authtime = System.currentTimeMillis();
                    param.put("authtime", authtime);
                    // 获取用户资料
                    Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                    param.put("uid", JzbDataType.getString(userInfo.get("uid")));
                    count = userService.updateUser(param);
                    if (count == 1) {
                        // 根据用户ID删除修改前的用户信息
                        userRedisApi.updateUserInfo(param);
                        result = Response.getResponseSuccess(userInfo);
                    } else {
                        result = Response.getResponseError();
                    }
                } else if (authid == 4) {

                    // 将图片转换为base64编码g
                    String imageBase64 = JzbIdEntification.getImageStr(JzbDataType.getString(param.get("idcardup")));

                    // 调用接口,实现身份证信息查询
                    String responseBody = JzbIdEntification.getIdCardData(imageBase64, "front");

                    // 结果转换为json串
                    JSONObject jsonObject = JSONObject.fromObject(responseBody);

                    // 获取身份证信息对象字符串
                    String idInformation = jsonObject.get("words_result").toString();

                    // 获取身份证名字对象转为字符串
                    String name = JSONObject.fromObject(idInformation).get("姓名").toString();

                    // 获取身份证姓名
                    String nameWords = JSONObject.fromObject(name).get("words").toString();

                    // 获取身份证号码转为字符串
                    String idCard = JSONObject.fromObject(idInformation).get("公民身份号码").toString();

                    // 获取身份证
                    String idcardWords = JSONObject.fromObject(idCard).get("words").toString();

                    // 获取身份证性别转为字符串
                    String idcardSex = JSONObject.fromObject(idInformation).get("性别").toString();

                    // 获取性别
                    String sexWords = JSONObject.fromObject(idcardSex).get("words").toString();

                    // 判断姓名,身份证号,性别是否属实
                    if (idcard.equals(idcardWords) && JzbDataType.getString(param.get("name")).equals(nameWords)
                            && JzbDataType.getString(param.get("sex")).equals(sexWords)) {
                        // 获取当前时间
                        long authtime = System.currentTimeMillis();
                        param.put("authtime", authtime);
                        // 获取用户资料
                        Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                        param.put("uid", JzbDataType.getString(userInfo.get("uid")));
                        count = userService.updateIdentity(param);
                        if (count == 1) {
                            // 判断缓存中是否存在对应的key
                            Response key = userRedisApi.comHasUserKey(param);
                            Object obj = key.getResponseEntity();
                            if (JzbDataType.isBoolean(obj)) {
                                boolean bl = (boolean) obj;
                                if (bl) {
                                    // 修改缓存中已经过时的数据
                                    userRedisApi.updateUserInfo(param);
                                }
                            }
                            result = Response.getResponseSuccess(userInfo);
                        } else {
                            result = Response.getResponseError();
                        }
                    } else {
                        result = Response.getResponseError();
                        result.setResponseEntity("身份证不清晰或身份证信息输入有误!");
                    }
                } else {
                    result = Response.getResponseError();
                }
            } else {
                result = Response.getResponseError();
                result.setResponseEntity("身份证信息输入有误!");
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    } // End userAttestation

    /**
     * 用户密码修改
     */
    @PostMapping("/modifyPassword")
    @CrossOrigin
    public Response modifyPassword(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            int count = userService.updateUserPassword(param);
            result = count == 1 ? Response.getResponseSuccess() : Response.getResponseError();
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    } // End updateUserPassword

    /**
     * 根据原密码进行用户密码修改
     */
    @PostMapping("/modifyPasswordByPasswd")
    @CrossOrigin
    public Response modifyPasswordByPasswd(@RequestBody Map<String, Object> param) {
        Response result;
        int count = 0;
        try {
            if (!JzbTools.isEmpty(param.get("passwd")) && !JzbTools.isEmpty(param.get("uid")) && !JzbTools.isEmpty("newpassword")) {
                String oldPasswd = userService.getInitPassWd(param);
                boolean matches = password().matches((CharSequence) param.get("passwd"), oldPasswd);
                if (matches) {
                    param.put("passwd", oldPasswd);
                    count = userService.updateUserPassword(param);
                }
            }
            result = count == 1 ? Response.getResponseSuccess() : Response.getResponseError();
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    } //

    /**
     * 根据联系方式或者ID获取用户信息
     *
     * @author kuangbin
     */
    @PostMapping("/getUserIdNameByPhone")
    @CrossOrigin
    public Response getUserIdNameByPhone(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            Map<String, Object> userMap = userService.getUserIdNameByPhone(param);
            // 获取用户资料
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            result = Response.getResponseSuccess(userInfo);
            result.setResponseEntity(userMap);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    } // End getUserIdNameByPhone

    /**
     * @return com.jzb.base.message.Response
     * @Author: DingShenChang
     * @Description:新老用户登录
     * @DateTime: 2019/8/6 9:52
     * @param:
     */
    @PostMapping("/userLogin")
    @CrossOrigin
    public Response userLogin(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            result = Response.getResponseSuccess();
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }//End newUserLogin

    /**
     * 发送验证码
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/sendCode", method = RequestMethod.POST)
    @CrossOrigin
    public Response sendCode(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            int type = JzbDataType.getInteger(param.get("type"));
            //发送注册验证码
            int a = 1;
            //发送找回密码验证码
            int b = 2;
            //发送修改手机号验证码
            int c = 3;
            Map<String, Object> message;
            if (type == a) {
                //注册模板
                param.put("groupid", authConfig.getRegister());
                message = userLoginService.sendMessage(param);
                int code = JzbDataType.getInteger(message.get("code"));
                if (code == 0) {
                    result = Response.getResponseSuccess();
                    message.remove("code");
                } else {
                    result = Response.getResponseError();
                }
            } else if (type == b) {
                //找回密码模板
                param.put("groupid", authConfig.getRetrieve());
                message = userLoginService.sendMessage1(param);
                int code = JzbDataType.getInteger(message.get("code"));
                if (code == 0) {
                    // 没有token
                    result = Response.getResponseSuccess();
                    message.remove("code");
                } else {
                    result = Response.getResponseError();
                }
            } else if (type == c) {
                //修改手机号码模板
                param.put("groupid", "7010");
                message = userLoginService.sendMessageByRelphone(param);
                int code = JzbDataType.getInteger(message.get("code"));
                if (code == 0) {
                    result = Response.getResponseSuccess();
                    message.remove("code");
                } else {
                    result = Response.getResponseError();
                }
            } else {
                message = null;
                result = Response.getResponseError();
            }
            result.setResponseEntity(message);
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }//End sendCode

    /**
     * @return com.jzb.base.message.Response
     * @Author: DingShenChang
     * @Description: 用户注册短信验证
     * @DateTime: 2019/8/8 15:51
     * @param:
     */
    @PostMapping("/userVerify")
    @CrossOrigin
    public Response userVerify(@RequestBody Map<String, Object> param) {
        Response result = new Response();
        try {
            boolean code = userLoginService.userVerify(param);
            result.setResponseEntity(code);
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }//End userVerify

    /**
     * 注册的第二步操作创建用户
     *
     * @param param
     * @return
     */
    @PostMapping("/addRegistration")
    @CrossOrigin
    public Response addRegistration(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            Map reMap = userLoginService.registrationTwo(param);
            //成功创建会返回用户id到前端
            result = reMap.size() == 0 ? Response.getResponseError() : Response.getResponseSuccess();
            result.setResponseEntity(reMap);
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }//End addRegistration

    /**
     * 认证用户验证身份证后六位数字
     *
     * @param param
     * @return
     */
    @PostMapping("/idCardVerify")
    @CrossOrigin
    public Response idCardVerify(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String[] str = {"idcard"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                param.put("uid", userInfo.get("uid"));
                Map reMap = userLoginService.idCard(param);
                result = Response.getResponseSuccess(userInfo);
                result.setResponseEntity(reMap);
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
     * 单位设置-申请/邀请成员
     * 通过名字模糊搜索所有用户或通过注册手机号搜索用户
     *
     * @author kuangbin
     */
    @RequestMapping(value = "/searchInvitee", method = RequestMethod.POST)
    @CrossOrigin
    public Response searchInvitee(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            int count = 0;
            if (!StringUtils.isEmpty(JzbDataType.getString(param.get("cname")))) {
                // 获取被邀请人总数
                count = JzbDataType.getInteger(param.get("count"));
                count = count < 0 ? 0 : count;
                if (count == 0) {
                    // 查询被邀请人总数
                    count = userService.getInviteeCount(param);
                }
            }
            // 获取所有满足条件的用户
            List<Map<String, Object>> listIncitee = userService.searchInvitee(param);
            // 获取用户资料
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            result = Response.getResponseSuccess(userInfo);
            // 分页对象
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(listIncitee);
            // 设置总数
            pageInfo.setTotal(count > 0 ? count : listIncitee.size());
            result.setPageInfo(pageInfo);
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    } // End searchInvitee

    /**
     * 根据用户表的姓名或手机号查询用户信息
     *
     * @param param
     * @return
     */
    @PostMapping("/searchUserUid")
    @CrossOrigin
    public Response searchUserUid(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            int rows = JzbDataType.getInteger(param.get("pagesize"));
            int page = JzbDataType.getInteger(param.get("pageno"));
            if (page > 0 && rows > 0) {
                param.put("pagesize", rows);
                param.put("start", rows * (page - 1));
                List<Map<String, Object>> idList = userService.searchUserName(param);
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                result = Response.getResponseSuccess(userInfo);
                PageInfo pageInfo = new PageInfo();
                pageInfo.setList(idList);
                int count = JzbDataType.getInteger(param.get("count"));
                if (count == 0) {
                    int size = userService.searchUserNameCount(param);
                    pageInfo.setTotal(size > 0 ? size : idList.size());
                }
                result.setPageInfo(pageInfo);
            } else {
                result = Response.getResponseError();
            }
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }//searchUserUid

    /**
     * 我的基本资料
     * 点击头像后修改基本资料
     *
     * @author kuangbin
     */
    @RequestMapping(value = "/modifyUserBasicData", method = RequestMethod.POST)
    @CrossOrigin
    public Response modifyUserBasicData(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 获取当前毫秒值
            long updtime = System.currentTimeMillis();
            param.put("updtime", updtime);
            // 获取用户资料
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("uid", JzbDataType.getString(userInfo.get("uid")));
            int count = userService.modifyUserBasicData(param);

            /** 如果修改成功则统一  czd*/
            if (count > 0 && !JzbTools.isEmpty(param.get("relphone"))) {
                Map<String, Object> map = new HashMap<>();
                map.put("uid", JzbDataType.getString(userInfo.get("uid")));
                map.put("phone", JzbDataType.getString(param.get("relphone")));
                userService.updateUserPhoneNo1(map);
                deptUserApi.modifyDeptUser(map);
                Map<String, Object> resuMap = userService.getUserInfo(map);
                if (!JzbTools.isEmpty(resuMap)) {
                    // 添加增加缓存必要的参数
                    resuMap.put("token", "token");
                    resuMap.put("timeout", "1800000");
                    resuMap.put("phone", JzbDataType.getString(map.get("phone")));
                    userRedisApi.cacheUserInfo(resuMap);
                }
            }

            if (count != 0) {
                comHasUserKey(param);
                result = Response.getResponseSuccess(userInfo);
            } else {
                result = Response.getResponseError();
            }
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    } // End modifyUserBasicData

    /**
     * 通过参数，获取用户ID。
     * 验证APPID是否正确。生成Token，替换原有的Token.
     *
     * @author kuangbin
     */
    @RequestMapping(value = "/getToken", method = RequestMethod.POST)
    @CrossOrigin
    public Response getToken(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String appId = param.get("appid").toString();
            String appSecret = param.get("secret").toString();
            String uid = param.get("uid").toString();
            int idType = JzbDataType.getInteger(param.get("idtype"));
            String session = param.get("session").toString();

            // 1、验证APPID是否正确 先查询到数据
            Map<String, Object> appParam = new HashMap<>(1);
            appParam.put("appid", appId);
            String oldSecret = userLoginService.getAppSecret(appParam);

            // 输入的密码正确
            if (password().matches(appSecret, oldSecret)) {
                // 暂时只支持电话号码。
                Map<String, Object> userParam = new HashMap<>();
                userParam.put("ktid", idType);
                userParam.put("keyid", uid);
                String userId = userLoginService.getUserByValue(userParam);

                // 查询用户信息
                userParam = new HashMap<>();
                userParam.put("uid", userId);
                Map<String, Object> userInfo = null;
                String token;
                Response userInfoRes = userRedisApi.getCacheUserInfo(userParam);
                if (userInfoRes.getServerResult().getResultCode() == JzbReturnCode.HTTP_200) {
                    userInfo = (Map<String, Object>) userInfoRes.getResponseEntity();
                }

                // 没有用户信息，则需要缓存用户信息
                Response tokenRes;
                if (JzbTools.isEmpty(userInfo)) {
                    // 生成新的TOKEN
                    userInfo = userLoginService.getUserInfo(userParam);
                    JzbTools.logInfo("Cache Token", userInfo.get("uid"));
                    token = JzbDataCheck.Md5(userInfo.get("uid").toString() + System.currentTimeMillis());
                    userInfo.put("session", session);
                    userInfo.put("token", token);
                    userInfo.put("phone", JzbTools.isEmpty(userInfo.get("relphone")) ? "" : userInfo.get("relphone"));
                    userInfo.put("timeout", authConfig.getTokenTimeout());
                    tokenRes = userRedisApi.cacheUserInfo(userInfo);
                } else {
                    JzbTools.logInfo("Update Token", userInfo.get("uid"));
                    token = JzbDataCheck.Md5(userInfo.get("uid").toString() + System.currentTimeMillis());
                    userInfo.put("session", session);
                    userInfo.put("token", token);
                    userInfo.put("phone", JzbTools.isEmpty(userInfo.get("relphone")) ? "" : userInfo.get("relphone"));
                    userInfo.put("timeout", authConfig.getTokenTimeout());
                    tokenRes = userRedisApi.updateUserToken(userInfo);
                }

                // 返回结果
                if (tokenRes.getServerResult().getResultCode() == JzbReturnCode.HTTP_200) {
                    result = Response.getResponseSuccess();
                    Map<String, Object> resInfo = new HashMap<>();
                    resInfo.put("name", getValue(userInfo, "cname"));
                    resInfo.put("sex", getValue(userInfo, "sex"));
                    resInfo.put("portrait", getValue(userInfo, "portrait"));
                    resInfo.put("authid", getValue(userInfo, "authid"));
                    resInfo.put("uid", getValue(userInfo, "uid"));
                    resInfo.put("relphone", getValue(userInfo, "relphone"));
                    resInfo.put("phone", getValue(userInfo, "phone"));
                    resInfo.put("nickname", getValue(userInfo, "nickname"));
                    resInfo.put("authtime", getValue(userInfo, "authtime"));
                    resInfo.put("status", getValue(userInfo, "status"));
                    resInfo.put("session", session);
                    resInfo.put("token", token);
                    result.setResponseEntity(resInfo);
                } else {
                    result = Response.getResponseError();
                }
            } else {
                JzbTools.logError("===========================>>", "Client App Secret Ferify Failed", appSecret, oldSecret);
                result = Response.getResponseError();
                result.setResponseEntity(JzbMessageCode.MSG_ERR_CODE_PASSWORD);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            result = Response.getResponseError();
        }
        return result;
    } // End getToken

    /**
     * 获取对应的数据值
     *
     * @param userInfo
     * @param key
     * @return
     */
    private String getValue(Map<String, Object> userInfo, String key) {
        return userInfo.containsKey(key) ? JzbDataType.getString(userInfo.get(key)) : "";
    } // End getValue

    /**
     * 验收TOKEN是否存在，返回用户信息
     *
     * @author kuangbin
     */
    @RequestMapping(value = "/checkToken", method = RequestMethod.POST)
    public Response checkToken(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            JzbTools.logInfo("=====================>> checkToken", param.toString());
            if (param.containsKey("token")) {
                result = userRedisApi.getTokenUser(param);
            } else {
                result = Response.getResponseTimeout();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            result = Response.getResponseError();
        }
        return result;
    } // End checkToken

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
     * 发送验证码给当前企业超级管理员
     *
     * @param param
     * @author kuangbin
     */
    @RequestMapping(value = "/sendAdministrator", method = RequestMethod.POST)
    @CrossOrigin
    public Response sendAdministrator(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            int type = JzbDataType.getInteger(param.get("type"));
            // 获取联系电话
            String telNumber = param.get("phone") == null ? "" : param.get("phone").toString();
            // 验证码长度
            int size = 6;
            Response sendResult;
            if (type == 1) {
                // 发送验证码模板
                param.put("groupid", authConfig.getVerification());
                sendResult = userLoginService.sendSmsCode(param, telNumber, size);
            } else {
                // 发送转让成功信息模板
                param.put("groupid", authConfig.getAdministrator());
                param.put("msgtag", "Administrator1015");
                param.put("senduid", "Administrator1015");
                sendResult = userLoginService.sendRemind(param);
            }
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            result = Response.getResponseSuccess(userInfo);
            result.setResponseEntity(sendResult);
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }//End sendAdministrator

    /**
     * 点击确定切换单位后将现在的单位加入用户缓存中,二次登录后直接访问该企业
     *
     * @author kuangbin
     */
    @RequestMapping(value = "/SwitchingUnit", method = RequestMethod.POST)
    @CrossOrigin
    public Response SwitchingUnit(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 获取用户资料
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("uid", JzbDataType.getString(userInfo.get("uid")));
            comHasUserKey(param);
            result = Response.getResponseSuccess(userInfo);
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    } // End SwitchingUnit

    /**
     * 根据uid判断缓存中是否存在key值
     *
     * @author kuangbin
     */
    public boolean comHasUserKey(Map<String, Object> param) {
        boolean bl;
        try {
            param.remove("userinfo");
            Response key = userRedisApi.comHasUserKey(param);
            Object obj = key.getResponseEntity();
            if (JzbDataType.isBoolean(obj)) {
                bl = (boolean) obj;
                if (bl) {
                    // 修改缓存中已经过时的数据
                    userRedisApi.updateUserInfo(param);
                }
            } else {
                bl = false;
            }
        } catch (Exception e) {
            JzbTools.logError(e);
            bl = false;
        }
        return bl;
    } // End SwitchingUnit

    /**
     * 查询用户所有权限（除用户所属部门）加上传入部门的权限
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     * @DateTime: 2019/10/14 16:42
     */
    @RequestMapping(value = "/getUserAllMenuList", method = RequestMethod.POST)
    @CrossOrigin
    public Response getUserAllMenuList(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String[] str = {"uid"};
            if (JzbCheckParam.allNotEmpty(param, str)) {
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                int rows = 2147483647;
                int page = 1;
                param.put("pagesize", rows);
                param.put("start", rows * (page - 1));
                List<Map<String, Object>> list = userService.queryUserMenuList(param);
                result = Response.getResponseSuccess(userInfo);
                result.setResponseEntity(list);
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
     * 修改用户状态（用户表和用户唯一键关联表）
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     */
    @RequestMapping(value = "/modifyUserStatus", method = RequestMethod.POST)
    @CrossOrigin
    public Response modifyUserStatus(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("uid", userInfo.get("uid"));
            int up = userService.updateUserStatus(param);
            result = up > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * CRM-销售业主-公海-业主下的人员11
     * 点击业主下的人员中的新增人员按钮进行加入员工
     *
     * @author kuangbin
     */
    @RequestMapping(value = "/addCompanyEmployee", method = RequestMethod.POST)
    @CrossOrigin
    public Response addCompanyEmployee(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            result = userService.addCompanyEmployee(param);
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * CRM-销售业主-公海-业主下的人员12
     * 点击业主下的人员中的修改人员按钮进行修改员工信息
     *
     * @author kuangbin
     */
    @RequestMapping(value = "/modifyCompanyEmployee", method = RequestMethod.POST)
    @CrossOrigin
    public Response modifyCompanyEmployee(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("upduid", userInfo.get("uid"));
            int count = userService.modifyCompanyEmployee(param);
            comHasUserKey(param);
            result = count > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 根据手机号模糊搜索用户姓名
     *
     * @author kuangbin
     */
    @RequestMapping(value = "/searchUserNameList", method = RequestMethod.POST)
    @CrossOrigin
    public Response searchUserNameList(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 获取申请人总数
            int count = JzbDataType.getInteger(param.get("count"));
            count = count < 0 ? 0 : count;
            if (count == 0) {
                // 查询申请人总数
                count = userService.searchUserNameListCount(param);
            }
            // 返回企业下所有的申请成员
            List<Map<String, Object>> userList = userService.searchUserNameList(param);
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            PageInfo pageInfo = new PageInfo();
            result = Response.getResponseSuccess(userInfo);
            pageInfo.setList(userList);
            pageInfo.setTotal(count > 0 ? count : userList.size());
            result.setPageInfo(pageInfo);
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }




    /**
     * 根据用户ids查询用户信息
     *计划管理
     * @author lifei
     */
    @RequestMapping(value = "/getUserNameList", method = RequestMethod.POST)
    @CrossOrigin
    public Response getUserNameList(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            List<Map<String, Object>> userlt=null;
            if(param.get("ids")==null){
                result = Response.getResponseError();
            }else{
                userlt = userService.getUserNameList(param);
            }

            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            PageInfo pageInfo = new PageInfo();
            result = Response.getResponseSuccess(userInfo);
            pageInfo.setList(userlt);

            result.setPageInfo(pageInfo);
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }




} // End class AuthUserController
