package com.jzb.org.service;

import com.jzb.org.dao.TbCompanyProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TbCompanyProductService {

    @Autowired
    private TbCompanyProductMapper tbCompanyProductMapper;


    /**
     * 添加企业产品
     * @param param
     * @return
     */
    public int addCompanyProduct(Map<String, Object> param){
        return tbCompanyProductMapper.addCompanyProduct((List<Map<String, Object>>) param.get("list"));
    }

    /**
     * 查询该单位是否授权电脑端授权
     * @param param
     * @return
     */
    public int queryCompanyProductIsExists(Map<String, Object> param){
        return tbCompanyProductMapper.queryCompanyProductIsExists(param);
    }
}
