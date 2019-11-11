package com.jzb.auth.service;

import com.jzb.auth.dao.CompanyListMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

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
    private CompanyListMapper mapper;

    /**
     * 根据用户姓名获取id合集
     *
     * @param param
     * @return java.lang.String
     * @Author: DingSC
     */
    public String searchUidByUidCname(Map<String, Object> param) {
        return mapper.searchUidByUidCname(param);
    }

}
