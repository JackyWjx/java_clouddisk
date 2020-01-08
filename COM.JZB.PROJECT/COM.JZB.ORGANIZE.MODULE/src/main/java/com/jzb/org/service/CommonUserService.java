package com.jzb.org.service;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.org.dao.CommonUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * @描述
 * @创建人 chenhui
 * @创建时间 2019/11/19
 * @修改人和其它信息
 */
@Service
public class CommonUserService {
    @Autowired
    CommonUserMapper userMapper;

    @Autowired
    private CompanyService companyService;

    // 添加公海用户
    public int addCommUser(Map<String, Object> paramp) {
        Calendar c = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        long addtime= 0;
        try {
            addtime = df.parse(df.format(c.getTime())).getTime();
        } catch (Exception e) {
            JzbTools.logError(e);
        }
        paramp.put("addtime",addtime);
        paramp.put("age",JzbDataType.getInteger(paramp.get("age")));
        paramp.put("status",'1');
        if (JzbTools.isEmpty(paramp.get("uid"))){
            paramp.put("uid",JzbRandom.getRandomCharCap(12));
        }
        paramp.put("age", JzbDataType.getInteger(paramp.get("age")));
        paramp.put("source",JzbDataType.getInteger(paramp.get("source")));
        if (!JzbTools.isEmpty(paramp.get("cid"))){
                paramp.put("isrelation",1);
        }
        return userMapper.addCommUser(paramp);
    }

    // 获取公海用户总数
    public int getCount(Map<String, Object> paramp) {
        return userMapper.getCount(paramp);
    }

    // 获取公海用户信息
    public List<Map<String, Object>> queryCommonUser(Map<String, Object> paramp) {
        return userMapper.queryComUser(paramp);
    }
    // 修改公海用户信息
    public int updComUser(Map<String, Object> paramp) {
        paramp.put("source",JzbDataType.getInteger(paramp.get("source")));
        paramp.put("age",JzbDataType.getInteger(paramp.get("age")));
        paramp.put("updtime",System.currentTimeMillis());
        return userMapper.updComUser(paramp);
    }

    // 删除公海用户信息
    public int delUser(Map<String,Object> map){
        return userMapper.delUser(map);
    }
    // 用户关联单位
    public int relCompanyUser(Map<String, Object> param) {
        param.put("uid",param.get("resuid"));
        return userMapper.relCompanyUser(param);
    }
    // 用户取消关联单位
    public int cancelCompanyUser(Map<String, Object> param) {
        return userMapper.cancelCompanyUser(param);
    }

    // 用户查询已关联单位
    public List<Map<String, Object>> queryRelCommonUser(Map<String, Object> param) {
        param.put("isrelation",1);
        return userMapper.queryRelCommonUser(param);
    }

    public Map<String,Object> getPhoneKey(Map<String, Object> param) {
        return userMapper.getPhoneKey(param);
    }
}
