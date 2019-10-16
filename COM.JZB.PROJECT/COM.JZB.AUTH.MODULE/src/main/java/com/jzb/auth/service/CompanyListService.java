package com.jzb.auth.service;

import com.jzb.auth.dao.CompanyListMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/***
 *
 *@Author hanbin
 *@Data 2019/7/26   11:09
 *@Describe
 *
 */
@Service
public class CompanyListService {

    @Autowired
    CompanyListMapper mapper;

    /**
     * 创建企业
     */
    public Integer saveCompanyList(Map<String, Object> param) {
        long regtime = System.currentTimeMillis();
        param.put("regtime",regtime);
        return mapper.saveCompanyList(param);
    }

    /**
     * 单位信息表
     */
    public Integer saveCompanyInfo(Map<String, Object> param) {
        return mapper.saveCompanyInfo(param);
    }

    /**
     * 申请记录
     */
    public Integer saveInviteUser(Map<String, Object> param) {
        return mapper.saveInviteUser(param);
    }


}
