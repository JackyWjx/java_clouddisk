package com.jzb.operate.service;

import com.jzb.operate.dao.TbSalesCompanyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TbSalesCompanyService {

    @Autowired
    private TbSalesCompanyMapper tbSalesCompanyMapper;

    /**
     * 添加业主单位
     * @param param
     * @return
     */
    public int addSalesCompany(Map<String, Object> param){

       return tbSalesCompanyMapper.addSalesCompany(param);
    }
}
