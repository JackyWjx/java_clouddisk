package com.jzb.operate.service;

import com.jzb.operate.dao.TbCompanyMethodMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TbCompanyMethodService {
    @Autowired
    private TbCompanyMethodMapper tbCompanyMethodMapper;


    /**
     * 添加单位方法论（修改同步）
     * @param list
     * @return
     */
    public int addCompanyMethod(List<Map<String, Object>> list){
        return tbCompanyMethodMapper.addCompanyMethod(list);
    }


    /**
     * 查询单位方法论
     * @param param
     * @return
     */
    public List<Map<String, Object>> queryCompanyMethod(Map<String, Object> param){
        return tbCompanyMethodMapper.queryCompanyMethod(param);
    }

    /**
     * 设置方法论状态
     * @param param
     * @return
     */
    public int updateCompanyMethodStatus(Map<String, Object> param){
        return tbCompanyMethodMapper.updateCompanyMethodStatus(param);
    }
}
