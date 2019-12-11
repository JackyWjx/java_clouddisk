package com.jzb.org.service;

import com.jzb.org.dao.TbDeptUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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

    public List<Map<String, Object>> queryOtherPersonByuid(Map<String, Object> param) {
        return tbDeptUserMapper.queryOtherPersonByuid(param);
    }
}
