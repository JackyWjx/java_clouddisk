package com.jzb.auth.service;

import com.alibaba.fastjson.JSON;
import com.jzb.auth.api.message.MessageApi;
import com.jzb.auth.api.organize.CompanyApi;
import com.jzb.auth.api.redis.CommonRedisApi;
import com.jzb.auth.api.redis.UserRedisApi;
import com.jzb.auth.config.AuthConfigProperties;
import com.jzb.auth.dao.AuthUserMapper;
import com.jzb.base.data.JzbDataType;
import com.jzb.base.data.code.JzbDataCheck;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @Description:
 * @Author dell
 * @Version 1.0
 * @Since 1.0
 * @Date: 2019/8/6 17:32
 */
@Service
public class AuthUserLoginService {
    /**
     * 更新时间
     */
    private Long authtime;

    /**
     * 用户数据库操作对象
     */
    @Resource
    private AuthUserMapper userMapper;

    /**
     * 短信发送接口
     */
    @Autowired
    private MessageApi messageApi;
    /**
     * 缓存的创建和获取
     */
    @Autowired
    private CommonRedisApi commonRedisApi;

    /**
     * 单位组织的接口
     */
    @Autowired
    private CompanyApi companyApi;

    /**
     * 配置文件中的配置数据
     */
    @Autowired
    private AuthConfigProperties config;

    @Autowired
    private UserRedisApi userRedisApi;

    /**
     * @return java.util.Map
     * @Author: DingShenChang
     * @Description:注册发送验证码实现方法
     * @DateTime: 2019/8/8 15:47
     * @param:
     */
    public Map<String, Object> sendMessage(Map<String, Object> map) {
        Map<String, Object> result = new HashMap(2);
        //验证码长度
        int size = 6;
        //手机号校验
        int type = 4;
        String telNumber = map.get("phone") == null ? "" : map.get("phone").toString();
        if (verify(telNumber, type)) {
            List<Map> loList = userMapper.searchSendCode(map);
            if (loList.size() > 0) {
                result.put("code", "1");
            } else {
                result.put("code", "0");
                Response rul = sendSmsCode(map, telNumber, size);
                result.put("serverResult", rul.getServerResult());
            }
        } else {
            result.put("code", "2");
            return result;
        }
        return result;
    }

    /**
     * @return java.util.Map
     * @Author: DingShenChang
     * @Description:找回密码发送验证码实现方法
     * @DateTime: 2019/8/8 15:47
     * @param:
     */
    public Map<String, Object> sendMessage1(Map<String, Object> map) {
        Map<String, Object> result = new HashMap(2);
        //验证码长度
        int size = 6;
        //手机号校验
        int type = 4;
        String telNumber = map.get("phone") == null ? "" : map.get("phone").toString();
        if (verify(telNumber, type)) {
            List<Map> loList = userMapper.searchSendCode(map);
            if (loList.size() > 0) {
                //获取用户id和发送短信
                String uid = loList.get(0).get("uid").toString();
                Response rul = sendSmsCode(map, telNumber, size);
                result.put("code", "0");
                result.put("uid", uid);
                result.put("serverResult", rul.getServerResult());
            } else {
                result.put("code", "3");
            }
        } else {
            result.put("code", "2");
            return result;
        }
        return result;
    }

    /**
     * 发送短信并保存验证码
     *
     * @param map       包含groupid模板id
     * @param telNumber 手机号
     * @param size      验证码长度
     * @return
     */
    public Response sendSmsCode(Map<String, Object> map, String telNumber, int size) {
        Response rul;
        //发送验证码
        String authCode = JzbRandom.getRandomNum(size);
        Map<String, Object> codeMap = new HashMap<>(2);
        codeMap.put("code", authCode);
        Map<String, Object> smsMap = new HashMap<>(2);
        smsMap.put("sms", codeMap);
        //短信发送参数填写
        map.put("sendpara", JSON.toJSONString(smsMap));
        map.put("usertype", "1");
        map.put("title", "计支云");
        map.put("receiver", telNumber);
        //发送验证码到缓存，限制保存时间
        Map<String, Object> param = new HashMap<>(3);
        param.put("key", telNumber);
        param.put("value", authCode);
        param.put("time", config.getCodeTime());
        //保存验证码到redis
        commonRedisApi.cacheSmsCode(param);
        try {
            //加密内容
            String appId = "SADJHJ1FHAUS45FAJ455";
            String secret = "ABSUY0FASD4AA";
            String groupId = JzbDataType.getString(map.get("groupid"));
            String title = JzbDataType.getString(map.get("title"));
            String userType = JzbDataType.getString(map.get("usertype"));
            String receivers = JzbDataType.getString(map.get("receiver"));
            String checkCode = "FAHJKSFHJK400800FHAJK";
            String md5 = JzbDataCheck.Md5(appId + secret + groupId + title + userType + receivers + checkCode);
            map.put("checkcode", md5);
            map.put("appid", appId);
            map.put("secret", secret);
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
     * @return boolean
     * @Author: DingShenChang
     * @Description: 验证短信验证码
     * @DateTime: 2019/8/8 16:18
     * @param:
     */
    public boolean userVerify(Map<String, Object> map) {
        boolean result;
        try {
            String telNumber = map.get("phone") == null ? "" : map.get("phone").toString();
            boolean b1 = verify(telNumber, 4);
            if (b1) {
                String authCode = map.get("authCode") == null ? "" : map.get("authCode").toString();
                Response request = commonRedisApi.getPhoneCode(map);
                String localCode = request.getResponseEntity() == null ? "" : request.getResponseEntity().toString();
                if (JzbTools.isEmpty(authCode)) {
                    result = false;
                } else {
                    if (authCode.equals(localCode)) {
                        //验证码验证相等
                        result = true;
                    } else {
                        result = false;
                    }
                }
            } else {
                result = false;
            }
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 注册第二步操作创建用户
     *
     * @param map
     * @return
     */
    public Map<String, Object> registrationTwo(Map<String, Object> map) {
        Map<String, Object> response = new HashMap(4);
        String phone = map.get("phone") == null ? "" : map.get("phone").toString();
        String name = map.get("name") == null ? "" : map.get("name").toString();
        boolean b1 = verify(phone, 4);
        boolean b2 = verify(name, 2);
        if (b1 && b2) {
            List<Map> loList = userMapper.searchSendCode(map);
            if (loList.size() > 0) {
                response.put("code", "3");
            } else {
                String uid = JzbRandom.getRandomCharCap(12);
                long time = System.currentTimeMillis();
                //创建用户信息
                map.put("uid", uid);
                map.put("authid", 0);
                map.put("regtime", time);
                if (JzbTools.isEmpty(map.get("status"))) {
                    map.put("status", "1");
                }
                //添加用户注册信息
                map.put("ktid", 1);
                map.put("passwd", password().encode(JzbDataType.getString(map.get("passwd"))));
                userMapper.insertUserList(map);
                userRedisApi.cachePhoneUid(map);
                response.put("uid", uid);
            }
        } else {
            response.put("name", b2);
            response.put("phone", b1);
        }

        return response;
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
     * 认证用户验证身份证后六位数字
     *
     * @param map
     * @return java.util.Map
     * @Author: DingSC
     * @DateTime: 2019/9/16 18:06
     */
    public Map<String, Object> idCard(Map<String, Object> map) {
        Map<String, Object> response = new HashMap(2);
        List list = userMapper.idCardLastSix(map);
        if (list.size() > 0) {
            response.put("code", true);
        } else {
            response.put("code", false);
        }
        return response;
    }


    /**
     * @return boolean
     * @Author: DingShenChang
     * @Description:校验输入的数据
     * @DateTime: 2019/8/6 17:08
     * @param: i=1:单位名称；i=2:姓名；i=3:密码校验,i=4校验手机号；
     */
    private boolean verify(String obj, int i) {
        boolean result = true;
        Map reMap = new HashMap(4);
        try {
            reMap.put(1, "[\\w\\W]{3,20}");
            reMap.put(2, "[\\u4e00-\\u9fa5]{2,4}");
            reMap.put(3, "(?=.*[a-zA-Z])(?=.*\\d).{6,12}$");
            reMap.put(4, "^[1][3,4,5,6,7,8,9][0-9]{9}$");
            result = Pattern.matches(reMap.get(i).toString(), obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 查询APP的Secret
     *
     * @param param
     * @return
     */
    public String getAppSecret(Map<String, Object> param) {
        return userMapper.queryAppSecret(param).get(0).get("secret").toString();
    } // End getAppSecret

    /**
     * 查询USERID
     *
     * @param param
     * @return
     */
    public String getUserByValue(Map<String, Object> param) {
        return userMapper.queryUserByValue(param).get(0).get("uid").toString();
    } // End getUserByValue

    /**
     * 获取用户信息
     *
     * @param param
     * @return
     */
    public Map<String, Object> getUserInfo(Map<String, Object> param) {
        return userMapper.queryUserInfo(param);
    } // End getUserInfo

    /**
     * 发送短信并保存验证码
     *
     * @param map 包含groupid模板id
     * @return
     */
    public Response sendRemind(Map<String, Object> map) {
        Response rul;
        // 将日期格式化为年月日,时分秒
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sim.format(new Date());
        Map<String, Object> codeMap = new HashMap<>();
        codeMap.put("username", JzbDataType.getString(map.get("username")));
        codeMap.put("companyname", JzbDataType.getString(map.get("companyname")));
        codeMap.put("date", date);
        Map<String, Object> smsMap = new HashMap<>(1);
        smsMap.put("sms", codeMap);
        map.put("sendpara", JSON.toJSONString(smsMap));
        //短信发送参数填写
        map.put("usertype", "1");
        map.put("title", "计支云");
        try {
            //加密内容
            String appId = "SADJHJ1FHAUS45FAJ455";
            String secret = "ABSUY0FASD4AA";
            String groupId = JzbDataType.getString(map.get("groupid"));
            String title = JzbDataType.getString(map.get("title"));
            String userType = JzbDataType.getString(map.get("usertype"));
            String receiver = JzbDataType.getString(map.get("phone"));
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
}
