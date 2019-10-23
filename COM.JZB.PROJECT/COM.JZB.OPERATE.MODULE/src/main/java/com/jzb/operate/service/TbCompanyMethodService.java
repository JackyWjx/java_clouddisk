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
}
