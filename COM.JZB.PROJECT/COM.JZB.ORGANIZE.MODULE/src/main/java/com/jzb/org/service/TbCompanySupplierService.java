package com.jzb.org.service;

import com.jzb.base.util.JzbRandom;
import com.jzb.org.dao.TbCompanySupplierMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TbCompanySupplierService {

    @Autowired
    private TbCompanySupplierMapper tbCompanySupplierMapper;

    @Autowired
    private NewCompanyCommonService commonService;

    /**
     * 添加供应商
     *
     * @param param
     * @return
     */
    public int addCompanySupplier(Map<String, Object> param){
        param.put("supid", JzbRandom.getRandomCharCap(7));
        return tbCompanySupplierMapper.addCompanySupplier(param);
    }

    /**
     * 修改供应商
     *
     * @param param
     * @return
     */
    public int updateCompanySupplier(Map<String, Object> param){
        return tbCompanySupplierMapper.updateCompanySupplier(param);
    }

    /**
     * 查询供应商
     *
     * @param param
     * @return
     */
    public List<Map<String, Object>> queryCompanySupplier(Map<String, Object> param){
        return tbCompanySupplierMapper.queryCompanySupplier(param);
    }

    /**
     * CRM-销售业主-公海-供应商6
     * 删除供应商
     *
     * @author kuangbin
     */
    public int removeCompanySupplier(Map<String, Object> param) {
        param.put("status", "2");
        param.put("updtime", System.currentTimeMillis());
        return tbCompanySupplierMapper.deleteCompanySupplier(param);
    }

    /**
     * 一键认证
     * 公海添加单位信息
     */
    public List<Map<String,Object>> authCompanySupplier(Map<String, Object> param) {

        // 调用认证接口返回供应商信息
        //Map<String,Object> pmap = 认证接口

        // 将供应商加入公海企业信息表
//        commonService.addCompanyCommonList(pmap);


        return null;
    }
}
