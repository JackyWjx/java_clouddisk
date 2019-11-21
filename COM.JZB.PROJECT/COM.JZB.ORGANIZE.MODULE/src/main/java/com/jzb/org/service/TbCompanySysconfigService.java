package com.jzb.org.service;

import com.jzb.org.dao.TbCompanySysconfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TbCompanySysconfigService {

    @Autowired
    private TbCompanySysconfigMapper tbCompanySysconfigMapper;

    /**
     * 添加二级域名
     * @param param
     * @return
     */
    public int addCompanySysconfig(Map<String, Object> param){
        long time=System.currentTimeMillis();
        param.put("addtime",time);
        param.put("updtime",time);
        return tbCompanySysconfigMapper.addCompanySysconfig(param);
    }

    /**
     * 查询该单位的二级域名
     * @param param
     * @return
     */
    public String queryCompanySysconfig(Map<String, Object> param){
        return tbCompanySysconfigMapper.queryCompanySysconfig(param);
    }

    /**
     * 根据二级域名查询当前所有记录
     * @param param
     * @return
     */
    public Map<String, Object> querySysconfig(Map<String, Object> param){
        return tbCompanySysconfigMapper.querySysconfig(param);
    }
}
