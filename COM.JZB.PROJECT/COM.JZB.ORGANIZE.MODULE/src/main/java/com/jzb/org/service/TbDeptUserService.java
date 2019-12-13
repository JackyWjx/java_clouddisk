package com.jzb.org.service;

import com.jzb.org.dao.TbDeptUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author sapientia
 * @Date 2019/12/5 16:31
 */
@Service
public class TbDeptUserService {

    @Autowired
    private TbDeptUserMapper tbDeptUserMapper;

    public List<Map<String, Object>> queryUsernameBydept(Map<String, Object> param) {
        return tbDeptUserMapper.queryUsernameBydept(param);
    }

    public List<Map<String, Object>> queryOtherPersonBycid(Map<String, Object> param) {
        return tbDeptUserMapper.queryOtherPersonBycid(param);
    }

    public List<Map<String, Object>> queryPersonNameByuid(Map<String, Object> param) {
        return tbDeptUserMapper.queryPersonNameByuid(param);
    }

    /**
     *  @author: gongWei
     *  @Date:  2019/12/13 10:12
     *  @Description: 根据用户id获取同行人
     *  @Param:
     *  @Return:
     *  @Exception:
     */
    public String getUsernameByUids(Map<String, Object> param) {
        Map<String,Object> whereMap = new HashMap<>();
        String[] uids = param.get("uids").toString().split(",");
        List<String> uidList = new ArrayList<>(Arrays.asList(uids));
        whereMap.put("uids",uidList);
        String unameStr = tbDeptUserMapper.getUsernameByUids(whereMap);
        return unameStr;
    }
}
